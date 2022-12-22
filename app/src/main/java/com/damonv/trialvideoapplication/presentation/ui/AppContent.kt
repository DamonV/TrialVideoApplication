package com.damonv.trialvideoapplication.presentation.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.damonv.trialvideoapplication.presentation.MainViewModel

//root UI compose function

@Composable
fun AppContent(viewModel: MainViewModel){
    val textIsVisible = remember { mutableStateOf(false) } //just text view visibility state
    val configuration = LocalConfiguration.current

    //let's observe main stateFlow stored in the viewModel
    val mainState = viewModel.mainUiStateFlow.collectAsState()

    //LazyRow state. It's already using rememberSaveable.
    //It helps come through over configuration change without viewModel
    val listState = rememberLazyListState()

    var layoutSize by remember { mutableStateOf(IntSize.Zero) }
    var purpleButtonSize by remember { mutableStateOf(IntSize.Zero) }
    var lazyRowSize by remember { mutableStateOf(IntSize.Zero) }

    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            ConstraintLayout(
                Modifier
                    .onSizeChanged { layoutSize = it }
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                val (player, button, lazyRow) = createRefs()

                VideoPlayerBox(
                    modifier = Modifier
                        .heightIn(0.dp,
                            with(LocalDensity.current) {
                                (layoutSize.height
                                        - 2*(purpleButtonSize.height + lazyRowSize.height)).toDp()
                            } - 16.dp
                        )
                        .fillMaxWidth()
                        .constrainAs(player) {
                            centerVerticallyTo(parent)
                        },
                    textVisibility= textIsVisible.value,
                    mainState,
                    viewModel
                )
                PurpleButton(
                    modifier = Modifier
                        .onSizeChanged { purpleButtonSize = it }
                        .constrainAs(button) {
                            top.linkTo(player.bottom, margin = 8.dp)
                            centerHorizontallyTo(parent)
                        }
                        .padding(8.dp),
                    textVisibilityState = textIsVisible
                )
                ImageLazyRow(
                    modifier = Modifier
                        .onSizeChanged { lazyRowSize = it }
                        .fillMaxWidth()
                        .constrainAs(lazyRow) {
                            bottom.linkTo(parent.bottom)
                        },
                    mainState.value,
                    listState,
                    viewModel.listIndex
                )
            }
        }
        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                Arrangement.SpaceBetween,
                Alignment.CenterHorizontally
            ) {
                VideoPlayerBox(
                    modifier = Modifier.weight(1f),
                    textVisibility= textIsVisible.value,
                    mainState,
                    viewModel
                )
                PurpleButton(
                    textVisibilityState = textIsVisible
                )
                ImageLazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    mainState.value,
                    listState,
                    viewModel.listIndex
                )
            }
        }
    }
}

@Composable
fun PurpleButton(modifier: Modifier = Modifier, textVisibilityState: MutableState<Boolean>) {
    Button(
        onClick = { textVisibilityState.value = !textVisibilityState.value },
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White
        ),
        shape = ShapeDefaults.ExtraSmall,
        modifier = modifier
    ) {
        Text("TEXT")
    }
}