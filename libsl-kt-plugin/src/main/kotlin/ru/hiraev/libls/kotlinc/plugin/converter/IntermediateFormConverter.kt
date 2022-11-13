package ru.hiraev.libls.kotlinc.plugin.converter

import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.descriptors.PropertyDescriptor
import org.jetbrains.kotlin.js.descriptorUtils.getJetTypeFqName
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.resolve.descriptorUtil.isCompanionObject
import org.jetbrains.kotlin.resolve.descriptorUtil.isEffectivelyPublicApi
import org.jetbrains.kotlin.resolve.lazy.descriptors.LazyClassDescriptor
import org.jetbrains.kotlin.resolve.lazy.descriptors.LazyPackageDescriptor
import ru.hiraev.libls.kotlinc.plugin.model.IntermediateEnumEntry
import ru.hiraev.libls.kotlinc.plugin.model.IntermediateEnumType
import ru.hiraev.libls.kotlinc.plugin.model.IntermediateProperty

object IntermediateFormConverter {

    fun convertEnumTypes(descriptors: List<LazyClassDescriptor>): List<IntermediateEnumType> =
            descriptors.filter { it.kind == ClassKind.ENUM_CLASS }.map {
                IntermediateEnumType(it.fqNameSafe.asString())
            }

    fun convertEnumEntries(descriptors: List<LazyClassDescriptor>): List<IntermediateEnumEntry> =
            descriptors.filter { it.kind == ClassKind.ENUM_ENTRY }.map {
                IntermediateEnumEntry(it.fqNameSafe.asString(), it.containingDeclaration.fqNameSafe.asString())
            }

    fun convertProperties(descriptors: List<PropertyDescriptor>): List<IntermediateProperty> =
            descriptors.map(::convertProperty)

    private fun convertProperty(descriptor: PropertyDescriptor): IntermediateProperty =
            if (checkIfCompanionObjectOrPackageDescriptor(descriptor.containingDeclaration)) {
                convertPackageOrObjectProperty(descriptor)
            } else {
                convertClassProperty(descriptor)
            }

    private fun checkIfCompanionObjectOrPackageDescriptor(descriptor: DeclarationDescriptor) =
            descriptor.isCompanionObject() || descriptor is LazyPackageDescriptor

    private fun convertClassProperty(descriptor: PropertyDescriptor) =
            IntermediateProperty.ClassLevel(
                    name = descriptor.fqNameSafe.asString(),
                    mutable = isPropertyMutableFromOuterScope(descriptor),
                    type = descriptor.type.getJetTypeFqName(true),
                    containingTypeName = descriptor.containingDeclaration.fqNameSafe.asString()
            )

    private fun convertPackageOrObjectProperty(descriptor: PropertyDescriptor) =
            IntermediateProperty.PackageOrObjectLevel(
                    name = descriptor.fqNameSafe.asString(),
                    mutable = isPropertyMutableFromOuterScope(descriptor),
                    type = descriptor.type.getJetTypeFqName(true)
            )

    // Check if setter is private for properties
    private fun isPropertyMutableFromOuterScope(descriptor: PropertyDescriptor) =
            descriptor.isVar && descriptor.setter?.isEffectivelyPublicApi != false

}
