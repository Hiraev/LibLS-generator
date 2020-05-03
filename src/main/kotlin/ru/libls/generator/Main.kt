package ru.libls.generator

import kastree.ast.Node
import kastree.ast.psi.Parser
import ru.libls.collector.Collector
import ru.libls.collector.kotlin.KotlinNodesProcessor
import ru.libls.collector.kotlin.model.KtFunc
import ru.libls.collector.kotlin.model.KtType
import java.io.File

fun main() {

    val files = Collector.collect(File("/Users/Malik/apple-hls-server/http-server"), "kt")

    val fileNodes = files.map { Parser.parseFile(it.readText()) }

    val processed = KotlinNodesProcessor.process(fileNodes)

    processed.filterIsInstance<KtFunc>().forEach(::printFunc)

//    KotlinCoreEnvironment.createForProduction(
//        Disposer.newDisposable(),
//        CompilerConfiguration(),
//        EnvironmentConfigFiles.JVM_CONFIG_FILES
//    )
//
//    val defaultProject = DefaultProjectFactory.getInstance().defaultProject
//    println(defaultProject)

}

fun parseMember(node: Node.Decl, tabs: Int) {
    print("\t".repeat(tabs))
    when (node) {
        is Node.Decl.Structured -> {
            println("Structured: ${node.name}")
            node.members.forEach { parseMember(it, tabs + 1) }
        }
        is Node.Decl.Constructor -> {
            println("Constructor: ${node.block}")
        }
        is Node.Decl.Func -> {
            println("Func: ${node.name} ${node.mods}")
            node.params.forEach {
                it.type?.ref?.tag
            }
        }
    }
}

fun printFunc(func: KtFunc) {
    print("fun ")
    print(func.name)
    print("(")
    print(func.args.joinToString(", ") { "${it.name}: ${it.type.convertToString()}" })
    print("): ")
    print(func.returnType.convertToString())
    print(";\n")
}

fun KtType.convertToString(): String = when (this) {
    is KtType.Plain -> this.name
    is KtType.Undefined -> "???"
    is KtType.Generic -> "$name<${parameters.joinToString(", ") { it.convertToString() }}>"
    is KtType.Lambda -> "{LAMBDA}"
    is KtType.Null -> "null"
}

inline fun <T> a_inline(t: T) {

}