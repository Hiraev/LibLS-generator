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
import org.jetbrains.kotlin.resolve.scopes.MemberScope

class LibSLSyntheticResolver : SyntheticResolveExtension {

    private val visitedPackages = mutableListOf<FqName>()

    override fun generateSyntheticClasses(
            thisDescriptor: PackageFragmentDescriptor,
            name: Name,
            ctx: LazyClassContext,
            declarationProvider: PackageMemberDeclarationProvider,
            result: MutableSet<ClassDescriptor>
    ) {
        checkScopeAndDoIfNeeded(
                thisDescriptor.fqName,
                thisDescriptor.getMemberScope(),
                LibSLProjectProcessor::addPackageScope
        )
        super.generateSyntheticClasses(thisDescriptor, name, ctx, declarationProvider, result)
    }

    override fun generateSyntheticClasses(
            thisDescriptor: ClassDescriptor,
            name: Name,
            ctx: LazyClassContext,
            declarationProvider: ClassMemberDeclarationProvider,
            result: MutableSet<ClassDescriptor>
    ) {
        checkScopeAndDoIfNeeded(
                thisDescriptor.fqNameSafe,
                thisDescriptor.unsubstitutedMemberScope,
                LibSLProjectProcessor::addClassScope
        )
        super.generateSyntheticClasses(thisDescriptor, name, ctx, declarationProvider, result)
    }

    private fun checkScopeAndDoIfNeeded(fqName: FqName, scope: MemberScope, block: (MemberScope) -> Unit) {
        if (!visitedPackages.contains(fqName)) {
            block.invoke(scope)
            visitedPackages += fqName
        }
    }

}
