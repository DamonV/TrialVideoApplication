package com.damonv.trialvideoapplication.presentation

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.damonv.trialvideoapplication.domain.MainUiState
import com.damonv.trialvideoapplication.domain.MainUiState.*
import com.damonv.trialvideoapplication.domain.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    repo: VideoRepository,
    @ApplicationContext private val context: Context
): ViewModel() {

    //it's ui-state only variable
    var listIndex = mutableStateOf(0)

    //main app stateFlow in unidirectional data flow
    val mainUiStateFlow: StateFlow<MainUiState> = repo.flowOfState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            //subscribe immediately for the lifetime of MainViewModel.
            //there is no reason to suspend during configuration change when the subscriber disconnects
            initialValue = Loading
        )

    val exoPlayer = ExoPlayer.Builder(context).build().apply {
            prepare()
            playWhenReady = true
            repeatMode = Player.REPEAT_MODE_ONE
        }

    var lastUri: String? = null

    fun clearPlayerItems() {
        lastUri = null
        exoPlayer.clearMediaItems()
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer.release()
    }
}