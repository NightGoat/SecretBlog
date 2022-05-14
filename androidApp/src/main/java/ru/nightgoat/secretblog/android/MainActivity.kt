package ru.nightgoat.secretblog.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.android.ext.android.inject
import ru.nightgoat.secretblog.Greeting
import ru.nightgoat.secretblog.core.BlogAction
import ru.nightgoat.secretblog.core.StoreViewModel

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {
    private val viewModel: StoreViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by viewModel.observeState().collectAsState()
            LaunchedEffect(viewModel) {
                viewModel.dispatch(BlogAction.Start)
            }
            Column {
                Button(onClick = {
                    viewModel.addMessage("NEW_")
                }) {
                    Text("--->")
                }
                LazyColumn {
                    items(state.visibleMessages) { message ->
                        Text(message.text)
                    }
                }
            }
        }
    }
}
