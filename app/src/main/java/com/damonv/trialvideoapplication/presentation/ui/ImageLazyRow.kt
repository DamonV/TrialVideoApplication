package com.damonv.trialvideoapplication.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.damonv.trialvideoapplication.R
import com.damonv.trialvideoapplication.domain.MainUiState
import com.damonv.trialvideoapplication.presentation.theme.DarkGrey
import com.damonv.trialvideoapplication.presentation.theme.LigtGrey

@Composable
fun ImageLazyRow(modifier: Modifier = Modifier,
                 mainUiState: MainUiState,
                 listState: LazyListState,
                 listIndex: MutableState<Int>
){
    LazyRow(
        state = listState,
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(8.dp)
    ) {
        when (mainUiState) {
            is MainUiState.Success ->
                itemsIndexed(items = mainUiState.videoList) { index, item ->

                    AsyncImage(
                        model = item.posterUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.no_image),
                        modifier = Modifier
                            .size(70.dp, 70.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .clickable {
                                listIndex.value = index
                            }
                            .then(
                                if (listIndex.value == index)
                                    Modifier.border(2.dp, MaterialTheme.colorScheme.primary)
                                else Modifier)
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                }
            else -> {
                items(1) {
                    Box(
                        modifier = Modifier
                            .size(70.dp, 70.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(LigtGrey.copy(alpha = 0.3f)),
                    )
                }
            }
        }
    }

}