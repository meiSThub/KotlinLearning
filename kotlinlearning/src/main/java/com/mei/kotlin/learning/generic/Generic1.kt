package com.mei.kotlin.learning.generic

/**
 * (generics) ，表示变量类型的参数化
 */

private class MyGeneric<T>(var name: T)

fun main() {
//    var myGeneric:MyGeneric<String> = MyGeneric("hello world")
    var myGeneric = MyGeneric("hello world")
    println(myGeneric.name)
}

