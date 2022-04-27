package uvis.irin.grape

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uvis.irin.grape.core.ui.theme.GrapeTheme
import uvis.irin.grape.soundlist.ui.SoundListContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GrapeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SoundListContent()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name", modifier = Modifier.padding(8.dp))
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GrapeTheme {
        Greeting("Android")
    }
}
