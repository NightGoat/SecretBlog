package ru.nightgoat.secretblog.core

sealed class BlogAction {
    class addMessage(val message: String, val isSecret: Boolean) : BlogAction()
    class removeMessage(val id: Int) : BlogAction()

    object ReverseSecretBlogsVisibility : BlogAction()
}