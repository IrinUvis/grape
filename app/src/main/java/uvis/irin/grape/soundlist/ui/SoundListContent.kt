@file:OptIn(ExperimentalMaterial3Api::class)

package uvis.irin.grape.soundlist.ui

import android.content.res.Configuration
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Scaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import uvis.irin.grape.core.ui.components.TopAppBar
import uvis.irin.grape.core.ui.theme.GrapeTheme

@Composable
fun SoundListContent() {
    Scaffold(
        topBar = { SoundSectionTabBar() },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Card {
                Text(text = "Hello Grape!")
            }
        }
    }
}

@Composable
fun SoundSectionTabBar() {
    TopAppBar(
        contentPadding = rememberInsetsPaddingValues(
            LocalWindowInsets.current.statusBars,
            applyBottom = false,
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(4.dp)
                .horizontalScroll(rememberScrollState())
        ) {
            SoundSectionTab(name = "Stonoga", false)
            SoundSectionTab(name = "Jagoda", false)
            SoundSectionTab(name = "Brozi", true)
            SoundSectionTab(name = "Polska", false)
            SoundSectionTab(name = "Jail", true)
            SoundSectionTab(name = "Ochelska", false)
        }
    }
}

@Composable
fun SoundSectionTab(name: String, active: Boolean) {
    val cardColors = if (active) {
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        )
    } else {
        CardDefaults.cardColors()
    }

    Card(
        modifier = Modifier.padding(horizontal = 4.dp),
        colors = cardColors,
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = name,
        )
    }
}

@Preview(
    name = "Night Mode - Empty",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    name = "Day Mode - Empty",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Suppress("UnusedPrivateMember")
@Composable
private fun SoundListContentPreview() {
    GrapeTheme {
        SoundListContent()
    }
}
