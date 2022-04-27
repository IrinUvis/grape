package uvis.irin.grape.soundlist.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import uvis.irin.grape.Greeting

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundListContent() {
    Column {

    }

    Box(contentAlignment = Alignment.Center) {
        Card {
            Greeting("Android")
        }
    }
}