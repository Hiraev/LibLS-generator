package ru.hiraev.libls.kotlinc.plugin

import kastree.ast.Node
import org.jetbrains.kotlin.descriptors.DeclarationDescriptorWithVisibility
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.descriptors.PropertyDescriptor
import org.jetbrains.kotlin.resolve.DescriptorUtils
import org.jetbrains.kotlin.resolve.descriptorUtil.isEffectivelyPublicApi
import org.jetbrains.kotlin.resolve.lazy.descriptors.LazyClassDescriptor
import org.jetbrains.kotlin.resolve.lazy.descriptors.LazyTypeAliasDescriptor
import org.jetbrains.kotlin.resolve.scopes.MemberScope

object LibSLProjectProcessor {

    private val structuredProject = mutableListOf<Node.File>()

    private val packageScopes = mutableListOf<MemberScope>()
    private val classScopes = mutableListOf<MemberScope>()

    fun addAllFiles(files: Collection<Node.File>) {
        structuredProject += files
    }

    fun addClassScope(scope: MemberScope) {
        classScopes += scope
    }

    fun addPackageScope(scope: MemberScope) {
        packageScopes += scope
    }

    fun onCompleteAnalysis() {
        processScopes()

        // TODO
    }

    private fun processScopes() {
        val classScopesDescriptors = classScopes.map(::findDescriptors)
        val packageScopesDescriptors = packageScopes.map(::findDescriptors)
        classScopes.clear()
        packageScopes.clear()
    }

    private fun findDescriptors(
            scope: MemberScope
    ): ScopeDescriptors {
        val publicDescriptors = DescriptorUtils.getAllDescriptors(scope)
                .filterIsInstance(DeclarationDescriptorWithVisibility::class.java)
                .filter(DeclarationDescriptorWithVisibility::isEffectivelyPublicApi)
        return ScopeDescriptors(
                properties = publicDescriptors.filterIsInstance<PropertyDescriptor>(),
                functions = publicDescriptors.filterIsInstance<FunctionDescriptor>(),
                classes = publicDescriptors.filterIsInstance<LazyClassDescriptor>(),
                typealiases = publicDescriptors.filterIsInstance<LazyTypeAliasDescriptor>()
        )
    }

    private data class ScopeDescriptors(
            val properties: List<PropertyDescriptor>,
            val functions: List<FunctionDescriptor>,
            val classes: List<LazyClassDescriptor>,
            val typealiases: List<LazyTypeAliasDescriptor>
    )

}
