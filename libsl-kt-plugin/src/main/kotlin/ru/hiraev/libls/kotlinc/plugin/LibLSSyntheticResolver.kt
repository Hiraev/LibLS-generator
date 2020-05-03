package ru.hiraev.libls.kotlinc.plugin

import org.jetbrains.kotlin.backend.common.descriptors.allParameters
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.extensions.SyntheticResolveExtension
import org.jetbrains.kotlin.resolve.lazy.LazyClassContext
import org.jetbrains.kotlin.resolve.lazy.declarations.ClassMemberDeclarationProvider
import org.jetbrains.kotlin.resolve.lazy.declarations.PackageMemberDeclarationProvider
import org.jetbrains.kotlin.resolve.source.getPsi
import org.jetbrains.kotlin.types.KotlinType
import java.io.BufferedWriter
import java.io.File
import java.util.*

class LibLSSyntheticResolver : SyntheticResolveExtension {

    private val file: File = File("/Users/Malik/Desktop/testcomp.txt")
    private val standardNames = listOf(
        "equals", "hashCode", "copy", "component1", "component2"
    )
    private val writer: BufferedWriter

    init {
        if (!file.exists()) file.createNewFile()
        writer = file.bufferedWriter()
        writer.write("Time: ${TimeZone.getDefault().displayName}\n")
        writer.flush()
    }

    override fun generateSyntheticClasses(
        thisDescriptor: ClassDescriptor,
        name: Name,
        ctx: LazyClassContext,
        declarationProvider: ClassMemberDeclarationProvider,
        result: MutableSet<ClassDescriptor>
    ) {
        var node: ASTNode? = thisDescriptor.source.getPsi()?.node?.firstChildNode

        //writer.write("::Second: ${name.asString()} ${name.identifier} ${thisDescriptor.kind.name} ${thisDescriptor.findPsi()?.node}} ${thisDescriptor.defaultType}\n ")
//        writer.write("::Second: ${name.asString()} ${thisDescriptor.javaClass} $node\n")
//        writer.flush()

        writer.write("generateSyntheticClasses ${thisDescriptor}")
        super.generateSyntheticClasses(thisDescriptor, name, ctx, declarationProvider, result)
    }

    override fun addSyntheticSupertypes(thisDescriptor: ClassDescriptor, supertypes: MutableList<KotlinType>) {
        val a = 1
        super.addSyntheticSupertypes(thisDescriptor, supertypes)
    }

    // classes with packages
    // check result
    override fun generateSyntheticClasses(
        thisDescriptor: PackageFragmentDescriptor,
        name: Name,
        ctx: LazyClassContext,
        declarationProvider: PackageMemberDeclarationProvider,
        result: MutableSet<ClassDescriptor>
    ) {
        // (thisDescriptor as LazyPackageDescriptor).declarationProvider.getDeclarations(DescriptorKindFilter.FUNCTIONS, {d -> true})
        val a = 1
        super.generateSyntheticClasses(thisDescriptor, name, ctx, declarationProvider, result)
    }

    // Good
    override fun generateSyntheticMethods(
        thisDescriptor: ClassDescriptor,
        name: Name,
        bindingContext: BindingContext,
        fromSupertypes: List<SimpleFunctionDescriptor>,
        result: MutableCollection<SimpleFunctionDescriptor>
    ) {
        if (result.isNotEmpty()) result.forEach(::processFunction)
        val a = 1
        super.generateSyntheticMethods(thisDescriptor, name, bindingContext, fromSupertypes, result)
    }

    private fun processFunction(simpleFunctionDescriptor: SimpleFunctionDescriptor) {
        val name = simpleFunctionDescriptor.name.asString()
        simpleFunctionDescriptor.allParameters
        if (!standardNames.contains(name)) {
            writer.write("$name\n")
            writer.flush()
        }
    }

    // this is class properties
    override fun generateSyntheticProperties(
        thisDescriptor: ClassDescriptor,
        name: Name,
        bindingContext: BindingContext,
        fromSupertypes: ArrayList<PropertyDescriptor>,
        result: MutableSet<PropertyDescriptor>
    ) {
        val a = 1
        super.generateSyntheticProperties(thisDescriptor, name, bindingContext, fromSupertypes, result)
    }

    override fun generateSyntheticSecondaryConstructors(
        thisDescriptor: ClassDescriptor,
        bindingContext: BindingContext,
        result: MutableCollection<ClassConstructorDescriptor>
    ) {
        val a = 1
        super.generateSyntheticSecondaryConstructors(thisDescriptor, bindingContext, result)
    }

    override fun getSyntheticCompanionObjectNameIfNeeded(thisDescriptor: ClassDescriptor): Name? {
        val a = 1
        return super.getSyntheticCompanionObjectNameIfNeeded(thisDescriptor)
    }

    override fun getSyntheticFunctionNames(thisDescriptor: ClassDescriptor): List<Name> {
        return super.getSyntheticFunctionNames(thisDescriptor)
    }

    override fun getSyntheticNestedClassNames(thisDescriptor: ClassDescriptor): List<Name> {
        val a = 1
        return super.getSyntheticNestedClassNames(thisDescriptor)
    }

}

data class Info(
    val descriptorName: String,
    val descriptorNameKindName: String,
    val descriptorDefaultType: String,
    val methodName: String,
    val descriptorIsInline: Boolean
)

data class Func(
    val name: String,
    val params: List<Pair<String, String>>,
    val returnType: String
)
