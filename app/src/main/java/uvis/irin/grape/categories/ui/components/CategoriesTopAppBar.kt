package uvis.irin.grape.categories.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import uvis.irin.grape.R
import uvis.irin.grape.categories.ui.model.UiCategory
import uvis.irin.grape.core.ui.components.AppBarTitle
import uvis.irin.grape.core.ui.components.NavigateBackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesTopAppBar(
    modifier: Modifier = Modifier,
    category: UiCategory,
    onNavigationIconClicked: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            AppBarTitle(
                text = if (category.isFirstCategory) stringResource(R.string.app_name) else category.name,
            )
        },
        navigationIcon = {
            if (!category.isFirstCategory) {
                NavigateBackButton(
                    onClick = onNavigationIconClicked,
                    contentDescription = stringResource(R.string.navigationIconContentDescription),
                )
            }
        }
    )
}
