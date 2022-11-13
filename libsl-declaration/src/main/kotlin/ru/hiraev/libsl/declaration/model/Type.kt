package ru.hiraev.libsl.declaration.model

sealed class Type {
    data class RegularType(
        val name: String,
        val params: List<TypeParam>, // generics
        val parents: List<String>
    ) : Type()
    object NoType : Type()
}

/**
 * Some difficult examples
 */
class V<T, L : Comparable<T>> where T : Int, T : Comparable<Int> {

    abstract class Companion<T> {
        abstract fun f(): T
    }

}

