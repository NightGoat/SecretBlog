package ru.nightgoat.secretblog.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import ru.nightgoat.secretblog.Greeting
import ru.nightgoat.secretblog.core.StoreViewModel

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {
    private val viewModel = StoreViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                Button(onClick = {
                    viewModel.addMessage("NEW_")
                }) {
                    Text("--->")
                }
            }
        }
    }
}
