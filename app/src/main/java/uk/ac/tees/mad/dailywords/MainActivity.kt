package uk.ac.tees.mad.dailywords

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.dailywords.ui.presentation.auth.navigation.Navigation
import uk.ac.tees.mad.dailywords.ui.theme.DailyWordsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navcontroller = rememberNavController()
            DailyWordsTheme {
                Navigation(navcontroller = navcontroller)
            }
        }

    }
}