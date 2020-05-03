package ru.libls.collector.kotlin

import kastree.ast.Node
import ru.libls.collector.kotlin.model.KtArg
import ru.libls.collector.kotlin.model.KtFunc
import ru.libls.collector.kotlin.model.KtNode
import ru.libls.collector.kotlin.model.KtType

object KotlinNodesProcessor {

    fun process(files: List<Node.File>) = files.flatMap(::processFileNode)

    private fun processFileNode(fileNode: Node.File): List<KtNode> = processDeclsList(
        static = true,
        pkg = fileNode.pkg?.names.orEmpty(),
        names = emptyList(),
        decls = fileNode.decls
    )

    private fun processDeclsList(
        static: Boolean,
        pkg: List<String>,
        names: List<String>,
        decls: List<Node.Decl>
    ): List<KtNode> {
        return decls.flatMap {
            processDecl(static, pkg, names, it)
        }
    }

    private fun processDecl(
        static: Boolean,
        pkg: List<String>,
        names: List<String>,
        decl: Node.Decl
    ): List<KtNode> = when (decl) {
        is Node.Decl.Func -> if (decl.mods.hasRejectedMods()) emptyList() else listOf(
            processFunc(
                static,
                pkg,
                names,
                decl
            )
        )
        is Node.Decl.Structured -> processStructured(pkg, names, decl)
        else -> emptyList()
    }

    private fun processFunc(
        static: Boolean,
        pkg: List<String>,
        names: List<String>,
        func: Node.Decl.Func
    ): KtNode {
        val path = pkg + names
        val self: KtType = if (static) {
            KtType.Null
        } else {
            func.receiverType?.ref?.let(::processType) ?: KtType.Plain(path.joinToString("."))
        }

        return KtFunc(
            name = (path + func.name!!).joinToString("."),
            self = self,
            returnType = func.type?.ref?.let(::processType) ?: KtType.Undefined,
            args = listOf(
                KtArg(
                    name = "self",
                    type = self,
                    default = null
                )
            ) + func.params.map {
                KtArg(
                    name = it.name,
                    type = processType(it.type?.ref!!),
                    default = null
                )
            }
        )
    }

    private fun processStructured(
        pkg: List<String>,
        names: List<String>,
        structured: Node.Decl.Structured
    ): List<KtNode> {
        if (structured.mods.hasRejectedMods()) return emptyList()
        val static =
            structured.form == Node.Decl.Structured.Form.OBJECT || structured.form == Node.Decl.Structured.Form.COMPANION_OBJECT

        return processDeclsList(static, pkg, names + structured.name, structured.members)
    }

    private fun List<Node.Modifier>.hasRejectedMods() = this
        .filterIsInstance(Node.Modifier.Lit::class.java)
        .firstOrNull {
            listOf(
                Node.Modifier.Keyword.INTERNAL,
                Node.Modifier.Keyword.PRIVATE,
                Node.Modifier.Keyword.PROTECTED
            ).contains(it.keyword)
        } != null

    private fun processType(type: Node.TypeRef): KtType {
        return when (type) {
            is Node.TypeRef.Simple -> processPieces(type.pieces.first())
            is Node.TypeRef.Nullable -> processType(type.type)
            is Node.TypeRef.Func -> KtType.Lambda
            else -> KtType.Undefined
        }
    }

    private fun processPieces(piece: Node.TypeRef.Simple.Piece): KtType {
        return if (piece.typeParams.isEmpty()) {
            KtType.Plain(piece.name)
        } else {
            KtType.Generic(
                piece.name,
                piece.typeParams.map { it?.let { processType(it.ref) } ?: KtType.Plain("Any") }
            )
        }
    }

}
