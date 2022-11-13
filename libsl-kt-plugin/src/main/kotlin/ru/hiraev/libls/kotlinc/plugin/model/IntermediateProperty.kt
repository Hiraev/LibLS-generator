package ru.hiraev.libls.kotlinc.plugin.model

sealed class IntermediateProperty {

    data class ClassLevel(
            val name: String,
            val mutable: Boolean, // from outer scope
            val type: String,
            val containingTypeName: String
    ) : IntermediateProperty()

    data class PackageOrObjectLevel(
            val name: String,
            val mutable: Boolean, // from outer scope
            val type: String
    ) : IntermediateProperty()

}
