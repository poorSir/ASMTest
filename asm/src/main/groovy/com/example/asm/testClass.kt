package com.example.asm

class testClass {



    fun test(){
        val l1 = System.currentTimeMillis()
        Thread.sleep(5000)
        val l2 = System.currentTimeMillis()-l1
        println(l2)
    }
}