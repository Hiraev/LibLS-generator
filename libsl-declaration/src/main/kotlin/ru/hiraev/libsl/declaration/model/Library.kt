package ru.hiraev.libsl.declaration.model

data class Library(
    val name: String,
    val types: List<Type>,
    val functions: List<Function>,
    val globalProperties: List<Property>
)
