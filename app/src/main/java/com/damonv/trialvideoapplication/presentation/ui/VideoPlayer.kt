package com.damonv.trialvideoapplication.presentation.ui

import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.ui.PlayerView
import com.damonv.trialvideoapplication.R
import com.damonv.trialvideoapplication.TAG
import com.damonv.trialvideoapplication.domain.MainUiState
import com.damonv.trialvideoapplication.presentation.MainViewModel
import com.damonv.trialvideoapplication.presentation.theme.customTextSyle
import kotlinx.coroutines.flow.filter
import kotlin.math.roundToInt

@Composable
fun VideoPlayerBox(
    modifier: Modifier = Modifier,
    textVisibility: Boolean = false,
    mainState: State<MainUiState>,
    viewModel: MainViewModel
) {

    val context = LocalContext.current

    LaunchedEffect(mainState) {

        //another option would be flows combine.
        //like this: snapshotFlow{listIndex.value}.combine(mainViewModel.mainUiStateFlow)
        snapshotFlow {
            Pair(viewModel.listIndex.value, mainState.value)
        }
            .filter { it.second !is MainUiState.Loading }
            .collect {
                Log.d(TAG, "collect snapshot")
                if (it.second is MainUiState.Success) {
                    val item = it.second as MainUiState.Success
                    val index = it.first
                    Log.d(TAG, "        snapshot $index")
                    if (!item.errMsg.isNullOrEmpty() || item.videoList == null)
                        viewModel.clearPlayerItems()
                    else if (index in item.videoList.indices) {
                        val newUri = item.videoList[index].fileUrl
                        if (newUri != viewModel.lastUri){
                            viewModel.lastUri = newUri
                            viewModel.exoPlayer.setMediaItem(
                                MediaItem.fromUri(
                                    Uri.parse(newUri)
                                )
                            )
                        }
                    }
                } else viewModel.clearPlayerItems()
            }
    }

    var boxSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .onSizeChanged { boxSize = it }
    ) {
        //DisposableEffect(Unit) { onDispose { exoPlayer.release() } }

        var xPos by remember { mutableStateOf(0f)}
        var yPos by remember { mutableStateOf(0f)}
        var text by remember { mutableStateOf(TextFieldValue("Editable & Draggable")) }
        var textSize by remember { mutableStateOf(IntSize.Zero) }

        AndroidView(
            factory = {
                PlayerView(context).apply {
                    player = viewModel.exoPlayer
                    layoutParams =
                        FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                }
            }
        )

        val state = mainState.value
        when {
            state is MainUiState.Loading -> BoxText("Loading..", MaterialTheme.colorScheme.primary)
            state is MainUiState.Success && !state.errMsg.isNullOrEmpty()
                -> BoxText("${state.errMsg}", MaterialTheme.colorScheme.error)
        }

        if (textVisibility)
            BasicTextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true,
                textStyle = customTextSyle,
                modifier = Modifier
                    .onSizeChanged { textSize = it }
                    .widthIn(min = 20.dp)
                    .offset { IntOffset(xPos.roundToInt(), yPos.roundToInt()) }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                xPos = (xPos + dragAmount.x)
                                    .coerceIn(0f, (boxSize.width - textSize.width).toFloat())
                                yPos = (yPos + dragAmount.y)
                                    .coerceIn(0f, (boxSize.height - textSize.height).toFloat())
                            }
                        )
                    }
            )
    }
}

@Composable
fun BoxText(text: String, color: Color){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Center
    ) {
        Text(
            text,
            color = color,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Center)
                .padding(16.dp)
        )
    }
}