package ru.hiraev.libls.kotlinc.plugin

import kastree.ast.Node
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.descriptors.DeclarationDescriptorWithVisibility
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.descriptors.PropertyDescriptor
import org.jetbrains.kotlin.resolve.DescriptorUtils
import org.jetbrains.kotlin.resolve.descriptorUtil.isEffectivelyPublicApi
import org.jetbrains.kotlin.resolve.scopes.MemberScope

object LibSLProjectProcessor {

    private val structuredProject = mutableListOf<Node.File>()

    private val functions = mutableListOf<FunctionDescriptor>()
    private val properties = mutableListOf<PropertyDescriptor>()
    private val scopes = mutableListOf<MemberScope>()

    fun addAllFiles(files: Collection<Node.File>) {
        structuredProject += files
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
                .filter(DeclarationDescriptorWithVisibility::isEffectivelyPublicApi)
                .forEach { declarationDescriptor ->
                    when (declarationDescriptor) {
                        is PropertyDescriptor -> properties += declarationDescriptor
                        is FunctionDescriptor -> functions += declarationDescriptor
                    }
                }
    }

}
