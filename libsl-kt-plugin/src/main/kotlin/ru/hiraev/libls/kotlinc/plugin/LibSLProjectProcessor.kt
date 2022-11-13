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
import ru.hiraev.libls.kotlinc.plugin.converter.IntermediateFormConverter
import ru.hiraev.libls.kotlinc.plugin.model.IntermediateEnumEntry
import ru.hiraev.libls.kotlinc.plugin.model.IntermediateEnumType

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
        val scopesDescriptors = flattenScopeDescriptorsList((packageScopes + classScopes).map(::findDescriptors))

        val enums = IntermediateFormConverter.convertEnumTypes(scopesDescriptors.classes)
        val enumEntries = IntermediateFormConverter.convertEnumEntries(scopesDescriptors.classes)
        val properties = IntermediateFormConverter.convertProperties(scopesDescriptors.properties)
        classScopes.clear()
        packageScopes.clear()
    }

    private fun resolveEnums(enums: List<IntermediateEnumType>, enumEntries: List<IntermediateEnumEntry>) {

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

    private fun flattenScopeDescriptorsList(descriptors: List<ScopeDescriptors>) =
            ScopeDescriptors(
                    properties = descriptors.map(ScopeDescriptors::properties).flatten(),
                    functions = descriptors.map(ScopeDescriptors::functions).flatten(),
                    classes = descriptors.map(ScopeDescriptors::classes).flatten(),
                    typealiases = descriptors.map(ScopeDescriptors::typealiases).flatten()
            )

    private data class ScopeDescriptors(
            val properties: List<PropertyDescriptor>,
            val functions: List<FunctionDescriptor>,
            val classes: List<LazyClassDescriptor>,
            val typealiases: List<LazyTypeAliasDescriptor>
    )

}
