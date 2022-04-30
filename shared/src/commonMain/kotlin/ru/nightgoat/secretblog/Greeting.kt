package ru.nightgoat.secretblog

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}