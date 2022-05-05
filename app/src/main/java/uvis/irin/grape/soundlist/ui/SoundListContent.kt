@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)

package uvis.irin.grape.soundlist.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import uvis.irin.grape.core.ui.theme.GrapeTheme
import uvis.irin.grape.soundlist.domain.model.ResourceSound
import uvis.irin.grape.soundlist.domain.model.Sound
import uvis.irin.grape.soundlist.domain.model.SoundCategory
import java.util.*

@Composable
fun SoundListContent(viewState: SoundListViewState) {
    val pagerState = rememberPagerState()

    Scaffold(topBar = {
        SoundSectionTabBar(
            categories = viewState.categories,
            pagerState = pagerState,
        )
    }) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            HorizontalPager(
                state = pagerState,
                count = viewState.categories.size,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                Text(text = viewState.categories[page].name)
            }
        }

    }
}

@Composable
fun SoundSectionTabBar(categories: List<SoundCategory>, pagerState: PagerState) {
    val scope = rememberCoroutineScope()

    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        indicator = { },
        edgePadding = 8.dp,
        modifier = Modifier.padding(
            rememberInsetsPaddingValues(
                LocalWindowInsets.current.statusBars,
                applyBottom = false,
            ),
        ),
    ) {
        categories.forEachIndexed { index, category ->
            SoundSectionTab(
                text = category.name,
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
            )
        }
    }
}

@Composable
fun SoundSectionTab(text: String, selected: Boolean, onClick: () -> Unit) {
    val cardColors = if (selected) {
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        )
    } else {
        CardDefaults.cardColors()
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp),
        colors = cardColors,
        onClick = onClick,
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = text,
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
        SoundListContent(SoundListViewState())
    }
}
