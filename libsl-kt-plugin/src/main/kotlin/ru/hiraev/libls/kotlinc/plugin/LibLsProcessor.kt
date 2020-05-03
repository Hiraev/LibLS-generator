package ru.hiraev.libls.kotlinc.plugin

import kastree.ast.Node

object LibLsProcessor {

    //    private val file: File = File("/Users/Malik/Desktop/testcomp2.txt")
    //    private val writer: BufferedWriter

    val structuredProject = mutableListOf<Node.File>()
    private val candidatesFunctions = mutableListOf<Node.Decl.Func>()
    private val candidatesProperties = mutableListOf<Node.Decl.Property>()

    fun resolveTypeIfNeeded() {

    }

    fun findAllResolutionCandidates() {
        structuredProject.map(Node.File::decls).forEach {
            it.forEach((::processDecl))
        }
    }

    private fun processDecl(decl: Node.Decl) {
        when (decl) {
            is Node.Decl.Structured -> decl.members.forEach(::processDecl)
            is Node.Decl.Func -> processFunc(decl)
            is Node.Decl.Property -> processProperty(decl)
        }
    }

    private fun processFunc(func: Node.Decl.Func) {
        if (func.type == null) candidatesFunctions += func
    }

    private fun processProperty(property: Node.Decl.Property) {
        if (property.vars.size == 1 && property.vars.first()?.type == null) candidatesProperties += property
    }

    fun onCompleteAnalysis() {

    }

}
