package ru.hiraev.libls.kotlinc.plugin

import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.PackageFragmentDescriptor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.resolve.extensions.SyntheticResolveExtension
import org.jetbrains.kotlin.resolve.lazy.LazyClassContext
import org.jetbrains.kotlin.resolve.lazy.declarations.ClassMemberDeclarationProvider
import org.jetbrains.kotlin.resolve.lazy.declarations.PackageMemberDeclarationProvider

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
            LibSLProjectProcessor.addScope(thisDescriptor.getMemberScope())
            visitedPackages += fqName
        }
        super.generateSyntheticClasses(thisDescriptor, name, ctx, declarationProvider, result)
    }

    override fun generateSyntheticClasses(
            thisDescriptor: ClassDescriptor,
            name: Name,
            ctx: LazyClassContext,
            declarationProvider: ClassMemberDeclarationProvider,
            result: MutableSet<ClassDescriptor>
    ) {
        val fqName = thisDescriptor.fqNameSafe
        if (!visitedPackages.contains(fqName)) {
            LibSLProjectProcessor.addScope(thisDescriptor.unsubstitutedMemberScope)
            visitedPackages += fqName
        }
        super.generateSyntheticClasses(thisDescriptor, name, ctx, declarationProvider, result)
    }

}
