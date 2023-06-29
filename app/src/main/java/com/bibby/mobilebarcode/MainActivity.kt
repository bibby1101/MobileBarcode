package com.bibby.mobilebarcode

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.bibby.mobilebarcode.ui.theme.MobileBarcodeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileBarcodeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Greeting("Android")
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        InputMobileBarcode(context = LocalContext.current)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUnitApi::class)
@Composable
private fun InputMobileBarcode(context: Context) {
    var value by remember {
        mutableStateOf(context.getSharedPreferences("bibby", Context.MODE_PRIVATE).getString("mobilebarcode", ""))
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            fontSize = TextUnit(30f, TextUnitType.Sp),
            text = "簡易型手機條碼顯示"
        )

        value?.let {
            TextField(
                value = it,
                onValueChange = { newText ->
                    value = newText
                },
                singleLine = true
            )
        }

        Text(
            text = "手機條碼: $value"
        )

        Button(
            onClick = {
                val sharedPref = context.getSharedPreferences("bibby", Context.MODE_PRIVATE) ?: return@Button
                with (sharedPref.edit()) {
                    putString("mobilebarcode", value)
                    apply()
                    Toast.makeText(context, "已儲存，請手動更新桌面顯示條碼的 Widget", Toast.LENGTH_LONG).show()
                }
            }
        ) {
            Text(text = "儲存")
        }

        Text(
            text = "[注意] 儲存手機條碼後，目前請手動更新桌面顯示條碼的 Widget"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MobileBarcodeTheme {
        //Greeting("Android")
        InputMobileBarcode(context = LocalContext.current)
    }
}