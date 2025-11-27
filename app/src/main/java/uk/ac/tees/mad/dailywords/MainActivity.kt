package uk.ac.tees.mad.dailywords

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import uk.ac.tees.mad.dailywords.ui.presentation.navigation.MainScreen
import uk.ac.tees.mad.dailywords.ui.theme.DailyWordsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DailyWordsTheme {
                MainScreen()
            }
        }
    }
}