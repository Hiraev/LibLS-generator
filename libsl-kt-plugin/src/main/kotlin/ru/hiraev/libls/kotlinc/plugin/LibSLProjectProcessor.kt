package ru.hiraev.libls.kotlinc.plugin

import kastree.ast.Node
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.descriptors.DeclarationDescriptorWithVisibility
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.descriptors.PropertyDescriptor
import org.jetbrains.kotlin.descriptors.SimpleFunctionDescriptor
import org.jetbrains.kotlin.resolve.DescriptorUtils
import org.jetbrains.kotlin.resolve.descriptorUtil.isEffectivelyPublicApi
import org.jetbrains.kotlin.resolve.scopes.MemberScope

object LibSLProjectProcessor {

    private val structuredProject = mutableListOf<Node.File>()

    private val functions = mutableListOf<SimpleFunctionDescriptor>()
    private val properties = mutableListOf<PropertyDescriptor>()
    private val topLevelFunctions = mutableListOf<FunctionDescriptor>()
    private val topLevelProperties = mutableListOf<PropertyDescriptor>()
    private val scopes = mutableListOf<MemberScope>()

    fun addAllFiles(files: Collection<Node.File>) {
        structuredProject += files
    }

    fun addFun(fun_: SimpleFunctionDescriptor) {
        functions += fun_
    }

    fun addProperty(prop_: PropertyDescriptor) {
        properties += prop_
    }

    fun addScope(scope: MemberScope) {
        scopes += scope
    }

    fun onCompleteAnalysis() {
        findTopLevelDescriptors()
        // TODO
    }

    private fun findTopLevelDescriptors() {
        scopes.forEach {
            findAndAddPropertiesAndFunctions(DescriptorUtils.getAllDescriptors(it))
        }
        scopes.clear()
    }

    private fun findAndAddPropertiesAndFunctions(descriptors: Collection<DeclarationDescriptor>) {
        descriptors
                .filterIsInstance(DeclarationDescriptorWithVisibility::class.java)
                .filter(DeclarationDescriptorWithVisibility::isEffectivelyPublicApi).forEach { declarationDescriptor ->
                    when (declarationDescriptor) {
                        is PropertyDescriptor -> topLevelProperties += declarationDescriptor
                        is FunctionDescriptor -> topLevelFunctions += declarationDescriptor
                    }
                }
    }

}
