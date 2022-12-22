package com.damonv.trialvideoapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.damonv.trialvideoapplication.presentation.MainViewModel
import com.damonv.trialvideoapplication.presentation.theme.TrialVideoApplicationTheme
import com.damonv.trialvideoapplication.presentation.ui.AppContent
import dagger.hilt.android.AndroidEntryPoint

const val TAG = "TRV"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val mViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrialVideoApplicationTheme {
                AppContent(mViewModel)
            }
        }
    }
}