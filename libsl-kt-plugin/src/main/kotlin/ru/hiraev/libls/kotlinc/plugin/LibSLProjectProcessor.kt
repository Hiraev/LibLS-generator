package ru.hiraev.libls.kotlinc.plugin

import kastree.ast.Node
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.descriptors.PropertyDescriptor
import org.jetbrains.kotlin.descriptors.SimpleFunctionDescriptor

object LibSLProjectProcessor {

    private val structuredProject = mutableListOf<Node.File>()

    private val functions = mutableListOf<SimpleFunctionDescriptor>()
    private val properties = mutableListOf<PropertyDescriptor>()
    private val topLevelFunctions = mutableListOf<FunctionDescriptor>()
    private val topLevelProperties = mutableListOf<PropertyDescriptor>()

    fun addAllFiles(files: Collection<Node.File>) {
        structuredProject += files
    }

    fun addFun(fun_: SimpleFunctionDescriptor) {
        functions += fun_
    }

    fun addProperty(prop_: PropertyDescriptor) {
        properties += prop_
    }

    fun addTopLevelFunction(function: FunctionDescriptor) {
        topLevelFunctions += function
    }

    fun addTopLevelProperty(property: PropertyDescriptor) {
        topLevelProperties += property
    }

    fun onCompleteAnalysis() {
        // TODO
    }

}
