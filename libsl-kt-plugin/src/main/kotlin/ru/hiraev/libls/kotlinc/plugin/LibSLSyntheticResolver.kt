package ru.hiraev.libls.kotlinc.plugin

import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.descriptors.DeclarationDescriptorWithVisibility
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.descriptors.PackageFragmentDescriptor
import org.jetbrains.kotlin.descriptors.PropertyDescriptor
import org.jetbrains.kotlin.descriptors.SimpleFunctionDescriptor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.DescriptorUtils
import org.jetbrains.kotlin.resolve.descriptorUtil.isEffectivelyPublicApi
import org.jetbrains.kotlin.resolve.extensions.SyntheticResolveExtension
import org.jetbrains.kotlin.resolve.lazy.LazyClassContext
import org.jetbrains.kotlin.resolve.lazy.declarations.PackageMemberDeclarationProvider
import java.util.ArrayList

class LibSLSyntheticResolver : SyntheticResolveExtension {

    private val visitedPackages = mutableListOf<FqName>()

    override fun generateSyntheticClasses(
            thisDescriptor: PackageFragmentDescriptor,
            name: Name,
            ctx: LazyClassContext,
            declarationProvider: PackageMemberDeclarationProvider,
            result: MutableSet<ClassDescriptor>
    ) {
        val fqName = thisDescriptor.fqName
        if (!visitedPackages.contains(fqName)) {
            try {
                val descriptors = DescriptorUtils.getAllDescriptors(thisDescriptor.getMemberScope())
                findAndAddPropertiesAndFunctions(descriptors)
                visitedPackages += thisDescriptor.fqName
            } catch (e: Throwable) {
            }
        }
        super.generateSyntheticClasses(thisDescriptor, name, ctx, declarationProvider, result)
    }

    override fun generateSyntheticMethods(
            thisDescriptor: ClassDescriptor,
            name: Name, bindingContext: BindingContext,
            fromSupertypes: List<SimpleFunctionDescriptor>,
            result: MutableCollection<SimpleFunctionDescriptor>
    ) {
        if (checkIfTopDescriptorsPublic(thisDescriptor)) {
            result.filter(DeclarationDescriptorWithVisibility::isEffectivelyPublicApi).forEach(LibSLProjectProcessor::addFun)
        }
        super.generateSyntheticMethods(thisDescriptor, name, bindingContext, fromSupertypes, result)
    }

    override fun generateSyntheticProperties(
            thisDescriptor: ClassDescriptor,
            name: Name,
            bindingContext: BindingContext,
            fromSupertypes: ArrayList<PropertyDescriptor>,
            result: MutableSet<PropertyDescriptor>
    ) {
        if (checkIfTopDescriptorsPublic(thisDescriptor)) {
            result.filter(DeclarationDescriptorWithVisibility::isEffectivelyPublicApi)
                    .forEach(LibSLProjectProcessor::addProperty)
        }
        super.generateSyntheticProperties(thisDescriptor, name, bindingContext, fromSupertypes, result)
    }

    private fun findAndAddPropertiesAndFunctions(descriptors: Collection<DeclarationDescriptor>) {
        descriptors
                .filterIsInstance(DeclarationDescriptorWithVisibility::class.java)
                .filter(DeclarationDescriptorWithVisibility::isEffectivelyPublicApi).forEach { declarationDescriptor ->
                    when (declarationDescriptor) {
                        is PropertyDescriptor -> LibSLProjectProcessor.addTopLevelProperty(declarationDescriptor)
                        is FunctionDescriptor -> LibSLProjectProcessor.addTopLevelFunction(declarationDescriptor)
                    }
                }
    }

    private fun checkIfTopDescriptorsPublic(descriptor: DeclarationDescriptor): Boolean {
        return if (descriptor is PackageFragmentDescriptor) {
            true
        } else {
            if ((descriptor as? ClassDescriptor)?.isEffectivelyPublicApi == true) {
                checkIfTopDescriptorsPublic(descriptor.containingDeclaration)
            } else false
        }
    }

}
