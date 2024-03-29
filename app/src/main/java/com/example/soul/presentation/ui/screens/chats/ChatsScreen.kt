package com.example.soul.presentation.ui.screens.chats

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.soul.navigation.NavigationTree
import com.example.soul.presentation.ui.components.BottomNavigationView
import com.example.soul.presentation.ui.screens.search.SearchWidgetState
import com.example.soul.presentation.ui.theme.AppTheme.colors

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChatsScreen(
    navController: NavController,
    viewModel: ChatsViewModel = viewModel()
) {
    val searchWidgetState by viewModel.chatWidgetState
    val searchTextState by viewModel.chatTextState
    val chatList: List<String> = viewModel.chatList

    Scaffold(
        topBar = {
            SearchAppBar(
                searchWidgetState = searchWidgetState,
                searchTextState = searchTextState,
                onTextChange = {
                    viewModel.updateSearchTextState(newValue = it)
                },
                onCloseClicked = {
                    viewModel.updateSearchWidgetState(newValue = SearchWidgetState.CLOSED)
                },
                onSearchClicked = {
                    Log.d("Searched Text", it)
                },
                onSearchTriggered = {
                    viewModel.updateSearchWidgetState(newValue = SearchWidgetState.OPENED)
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colors.primaryBackground)
        ) {
            LazyColumn(
                modifier = Modifier.padding(10.dp)
            ) {
                item {
                    chatList.map { item ->
                        ChatItem(nickname = item, navController = navController)
                    }
                }
            }
            BottomNavigationView(navController = navController)
        }
    }
}

@Composable
fun ChatItem(nickname: String, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Image,
            modifier = Modifier
                .width(40.dp)
                .height(40.dp),
            contentDescription = "back",
            tint = colors.primaryButtonColor
        )

        Spacer(modifier = Modifier.padding(16.dp))

        Column {
            Text(
                text = nickname,
                color = colors.primaryTextColor,
                fontSize = 16.sp,
                modifier = Modifier.clickable { navController.navigate(NavigationTree.Chat.name) }
            )

            Text(
                text = "Був в мережі 4 год тому",
                color = colors.primaryHint,
                fontSize = 12.sp
            )
        }
    }
}


@Composable
fun SearchAppBar(
    searchWidgetState: SearchWidgetState,
    searchTextState: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    onSearchTriggered: () -> Unit
) {
    when (searchWidgetState) {
        SearchWidgetState.CLOSED -> {
            DefaultAppBar(
                onSearchClicked = onSearchTriggered
            )
        }
        SearchWidgetState.OPENED -> {
            SearchAppBar(
                text = searchTextState,
                onTextChange = onTextChange,
                onCloseClicked = onCloseClicked,
                onSearchClicked = onSearchClicked
            )
        }
    }
}

@Composable
fun DefaultAppBar(onSearchClicked: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "Chats", color = colors.primaryTextColor
            )
        },
        actions = {
            IconButton(
                onClick = { onSearchClicked() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon",
                    tint = colors.primaryButtonColor
                )
            }
        },
        backgroundColor = colors.primaryBackground
    )
}

@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = colors.primaryBackground
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = text,
            onValueChange = {
                onTextChange(it)
            },
            placeholder = {
                Text(
                    modifier = Modifier
                        .alpha(ContentAlpha.medium),
                    text = "Search your chats...",
                    color = colors.primaryHint,
                )
            },
            textStyle = TextStyle(fontSize = MaterialTheme.typography.subtitle1.fontSize),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    modifier = Modifier
                        .alpha(ContentAlpha.medium),
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = colors.primaryButtonColor
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (text.isNotEmpty()) {
                            onTextChange("")
                        } else {
                            onCloseClicked()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Icon",
                        tint = colors.primaryButtonColor
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(text)
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                textColor = colors.primaryTextColor,
                backgroundColor = colors.primaryBackground,
                cursorColor = Color.White.copy(alpha = ContentAlpha.medium)
            ),
        )
    }
}


@Composable
@Preview
fun DefaultAppBarPreview() {
    DefaultAppBar(onSearchClicked = {})
}

@Composable
@Preview
fun SearchAppBarPreview() {
    SearchAppBar(
        text = "Some random text",
        onTextChange = {},
        onCloseClicked = {},
        onSearchClicked = {}
    )
}