package com.example.guidemetravelersapp.helpers.commonComposables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.guidemetravelersapp.R
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun FullsizeImage(imageUrl: String) {
    Column(Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center) {
        Box(modifier = Modifier.fillMaxWidth()) {
            CoilImage(imageModel = imageUrl,
                contentDescription = "User profile photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(RectangleShape))
        }
    }
}

@Composable
fun LoadingBar() {
    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
}

@Composable
fun LoadingSpinner() {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colors.onBackground
        )
    }
}

@Composable
fun QuerySearch(
    modifier: Modifier = Modifier,
    query: String,
    label: String,
    onSearchActionClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onQueryChanged: (String) -> Unit
) {
    var showClearButton by remember { mutableStateOf(false) }

    TextField(
        modifier = modifier
            .onFocusChanged { focusState ->
                showClearButton = (focusState.isFocused)
            }
            .height(56.dp),
        value = query,
        onValueChange = onQueryChanged,
        label = { Text(text = label, style = MaterialTheme.typography.caption) },
        textStyle = MaterialTheme.typography.caption,
        singleLine = true,
        trailingIcon = {
            if (showClearButton) {
                IconButton(onClick = { onClearClick() }) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Clear")
                }
            }

        },
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Unspecified),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
            )
        },
        keyboardActions = KeyboardActions(onSearch = {
            onSearchActionClick()
        }),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text
        ),
    )
}

@Composable
fun <T> AutoCompleteTextView(
    modifier: Modifier,
    query: String,
    queryLabel: String,
    onQueryChanged: (String) -> Unit = {},
    predictions: List<T>,
    onDoneActionClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onItemClick: (T) -> Unit = {},
    itemContent: @Composable (T) -> Unit = {}
) {

    val view = LocalView.current
    val lazyListState = rememberLazyListState()
    LazyColumn(
        state = lazyListState,
        modifier = modifier.heightIn(max = TextFieldDefaults.MinHeight * 6)
    ) {

        item {
            QuerySearch(
                modifier = modifier,
                query = query,
                label = queryLabel,
                onQueryChanged = onQueryChanged,
                onSearchActionClick = {
                    view.clearFocus()
                    onDoneActionClick()
                },
                onClearClick = {
                    onClearClick()
                }
            )
        }

        if (predictions.count() > 0) {
            itemsIndexed(predictions) { _, prediction ->
                Row(
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable {
                            view.clearFocus()
                            onItemClick(prediction)
                        }
                ) {
                    itemContent(prediction)
                }
            }
        }
    }
}