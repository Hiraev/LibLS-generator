package ru.hiraev.libls.kotlinc.plugin.converter

import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.descriptors.TypeParameterDescriptor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.resolve.calls.inference.returnTypeOrNothing
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.resolve.descriptorUtil.getSuperInterfaces
import org.jetbrains.kotlin.resolve.lazy.descriptors.LazyClassDescriptor
import ru.hiraev.libsl.declaration.model.Argument
import ru.hiraev.libsl.declaration.model.ArgumentType
import ru.hiraev.libsl.declaration.model.Function
import ru.hiraev.libsl.declaration.model.Type
import ru.hiraev.libsl.declaration.model.TypeParam

object KotlinToLibSLConverter {

    fun convertFunctionDescriptor(functionDescriptor: FunctionDescriptor): Function {
        return Function(
                name = functionDescriptor.fqNameSafe.asString(),
                returnType = ArgumentType(functionDescriptor.returnTypeOrNothing.toString()),
                arguments = functionDescriptor.valueParameters.map {
                    Argument(
                            name = it.name.asString(),
                            type = it.type.toString()
                    )
                },
                receiver = null
        )
    }

    fun convertLazyClassDescriptor(lazyClassDescriptor: LazyClassDescriptor): Type.RegularType {
        val name = lazyClassDescriptor.fqNameSafe.asString()
        val typeParameters = lazyClassDescriptor.declaredTypeParameters
        // Это вложенность дескрипторов, то есть какие дескрипторы стоят выше.
        // val parents = lazyClassDescriptor.parents.map(DeclarationDescriptor::fqNameSafe).map(FqName::asString)
        val parents = lazyClassDescriptor
                .getSuperInterfaces()
                .map(DeclarationDescriptor::fqNameSafe)
                .map(FqName::asString)
        return Type.RegularType(
                name = name,
                params = typeParameters.map { TypeParam(it.name.asString()) },
                parents = parents.toList()
        )
    }

    /**
     * Enum классы не могут быть inner и не могут содержать парамтеризированные типы
     */
    fun convertEnumClass(lazyClassDescriptor: LazyClassDescriptor) {

    }

    fun convertTypeParameterDescriptor(typeParameterDescriptor: TypeParameterDescriptor) {

    }

    /**
     * for extension function .extensionReceiverParameter.value.type
     * for in class functions .dispatchReceiverParameter?.type
     */

}
