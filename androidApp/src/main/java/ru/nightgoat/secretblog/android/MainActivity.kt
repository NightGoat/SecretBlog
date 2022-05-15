package ru.nightgoat.secretblog.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.android.ext.android.inject
import ru.nightgoat.secretblog.core.AppState
import ru.nightgoat.secretblog.core.BlogAction
import ru.nightgoat.secretblog.core.BlogEffect
import ru.nightgoat.secretblog.core.StoreViewModel
import ru.nightgoat.secretblog.models.BlogMessage

class MainActivity : AppCompatActivity() {
    private val viewModel: StoreViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by viewModel.observeState().collectAsState()
            val effects by viewModel.observeSideEffect().collectAsState(initial = BlogEffect.Empty)
            val listState = rememberLazyListState()
            when (effects) {
                is BlogEffect.ScrollToLastElement -> {
                    LaunchedEffect(state) {
                        listState.scrollToItem(state.visibleMessages.lastIndex.takeIf { it > 0 }
                            ?: 0)
                    }
                }
            }
            LaunchedEffect(viewModel) {
                viewModel.dispatch(BlogAction.Start)
            }
            MainContent(
                state = state,
                listState = listState,
                onSendMessageClick = {
                    viewModel.addMessage(
                        message = it
                    )
                },
                onClearButtonClick = {
                    viewModel.clearDB()
                }
            )

        }
    }
}

@Composable
fun MainContent(
    state: AppState,
    listState: LazyListState = LazyListState(),
    onSendMessageClick: (String) -> Unit,
    onClearButtonClick: () -> Unit
) {
    Surface(color = Color.LightGray) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Toolbar(
                onClearButtonClick = onClearButtonClick
            )

            Messages(
                modifier = Modifier.weight(1f),
                listState = listState,
                state = state
            )
            InputToolbar {
                onSendMessageClick(it)
            }
        }
    }
}

@Composable
fun Toolbar(
    onClearButtonClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            fontSize = 24.sp,
            text = "Secret blog"
        )
        Button(onClick = {
            onClearButtonClick()
        }) {
            Text("Clear")
        }
    }
}

@Composable
fun Messages(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    state: AppState
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        horizontalAlignment = Alignment.End
    ) {
        items(state.visibleMessages) { message ->
            Card(modifier = Modifier.padding(8.dp)) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = message.text
                )
            }
        }
    }
}

@Composable
fun InputToolbar(
    onSendMessageClick: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        TextField(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            value = text,
            onValueChange = {
                text = it
            })
        Button(
            onClick = {
                onSendMessageClick(text)
                text = ""
            }) {
            Text("Send")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    MainContent(
        state = AppState(
            blogMessages = listOf(
                BlogMessage(text = "Hello"),
                BlogMessage(text = "World"),
                BlogMessage(text = "YABADABADU")
            )
        ),
        onSendMessageClick = {},
        onClearButtonClick = {}
    )
}