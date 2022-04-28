package uvis.irin.grape

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import uvis.irin.grape.core.ui.theme.GrapeTheme
import uvis.irin.grape.soundlist.ui.SoundListScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GrapeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SoundListScreen()
                }
            }
        }
    }
}

// @Preview(showBackground = true)
// @Composable
// fun DefaultPreview() {
//    GrapeTheme {
//        Greeting("Android")
//    }
// }
