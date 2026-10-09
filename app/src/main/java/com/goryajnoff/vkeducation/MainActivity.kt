package com.goryajnoff.vkeducation

import android.content.Intent
import android.net.Uri

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.goryajnoff.vkeducation.ui.theme.VKEducationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VKEducationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->


                    MainScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}


@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // Состояние поля ввода. remember сохраняет его между рекомпозициями.
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Введите текст") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // === 1. ЯВНЫЙ INTENT ===
        Button(
            onClick = {
                // Валидация: пустую строку передавать бессмысленно.
                if (inputText.isBlank()) {
                    Toast.makeText(context, "Введите текст", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                // Создаём явный Intent: context → SecondActivity.
                val intent = Intent(context, SecondActivity::class.java).apply {
                    // putExtra кладёт пару ключ-значение.
                    putExtra(SecondActivity.EXTRA_TEXT, inputText)
                }

                // Запускаем SecondActivity.
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Открыть вторую Activity")
        }
        // === 2. НЕЯВНЫЙ INTENT ===
        Button(
            onClick = {
                val phone = inputText.trim()

                // Валидация: номер должен быть не пустой и содержать только допустимые символы.
                val phoneRegex = Regex("^[+\\d\\s\\-()]{3,}$")
                if (phone.isBlank() || !phoneRegex.matches(phone)) {
                    Toast.makeText(context, "Введите корректный номер", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                // Создаём неявный Intent.
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${Uri.encode(phone)}")
                }

                // Проверяем, есть ли вообще приложение, которое это умеет.
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "Приложение звонков не найдено", Toast.LENGTH_SHORT)
                        .show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Позвонить другу")
        }

        // === 3. СИСТЕМНЫЙ INTENT ===
        Button(
            onClick = {
                if (inputText.isBlank()) {
                    Toast.makeText(context, "Введите текст", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, inputText)
                }

                // createChooser показывает системное окно "Поделиться через…".
                context.startActivity(Intent.createChooser(intent, "Поделиться через…"))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Поделиться текстом")
        }
    }
}
