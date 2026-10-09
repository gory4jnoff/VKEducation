package com.goryajnoff.vkeducation

import android.content.Intent.EXTRA_TEXT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.goryajnoff.vkeducation.ui.theme.VKEducationTheme

class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val receivedText: String = intent.getStringExtra(EXTRA_TEXT).orEmpty()
        setContent {
            VKEducationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val displayText = if (receivedText.isBlank()) {
                        "Текст не найден"
                    } else {
                        receivedText
                    }


                    Text(modifier = Modifier.padding(innerPadding), text = displayText)
                }
            }
        }
    }

    companion object {
        // Ключ для передачи данных. Константа, чтобы не опечататься.
        const val EXTRA_TEXT = "extra_text"
    }
}



