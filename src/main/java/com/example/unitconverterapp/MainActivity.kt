package com.example.unitconverterapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import com.example.unitconverterapp.ui.theme.CustomTypography


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                typography = CustomTypography
            ) {
                UnitConverterApp()
            }
        }
    }
}

@Composable
fun UnitConverterApp() {
    var inputValue by remember { mutableStateOf("") }
    var sourceUnit by remember { mutableStateOf("Inch") }
    var targetUnit by remember { mutableStateOf("Centimeter") }
    var result by remember { mutableStateOf("") }

    val units = listOf("Inch", "Foot", "Yard", "Mile", "Centimeter", "Meter", "Kilometer",
        "Pound", "Ounce", "Ton", "Kilogram", "Gram",
        "Celsius", "Fahrenheit", "Kelvin")

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = "Unit-Converter", style = MaterialTheme.typography.headlineLarge)

        OutlinedTextField(
            value = inputValue,
            onValueChange = { inputValue = it },
            label = { Text("Enter Value" , color = Color(0xFF1E90FF)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color(0xFF1E90FF),
                unfocusedIndicatorColor = Color.Gray,
                cursorColor = Color(0xFF1E90FF)
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ){
            UnitDropdownMenu(
                label = "From",
                selectedUnit = sourceUnit,
                units = units
            ) { sourceUnit = it }

            UnitDropdownMenu(
                label = "To",
                selectedUnit = targetUnit,
                units = units
            ) { targetUnit = it }
        }

        Button(onClick = {
            result = try {
                val input = inputValue.toDouble()
                convertUnits(input, sourceUnit, targetUnit).toString()
            } catch (e: Exception) {
                "Invalid Input"
            }
        },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1E90FF)
            )
        ) {
            Text(text = "CONVERT" , color = Color.White)
        }

        Text(text = "Result: ", style = MaterialTheme.typography.headlineLarge)

        OutlinedTextField(
            value = result,
            onValueChange = {},
            label = { Text("Converted value " , color = Color(0xFF1E90FF))},
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFF1E90FF), 
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color(0xFF1E90FF)
            )
        )
    }
}

@Composable
fun UnitDropdownMenu(label: String, selectedUnit: String, units: List<String>, onUnitSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text("$label: $selectedUnit")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            units.forEach { unit ->
                DropdownMenuItem(
                    text = { Text(unit) },
                    onClick = {
                    onUnitSelected(unit)
                    expanded = false
                }
                )
            }
        }
    }
}

fun convertUnits(value: Double, from: String, to: String): Double {
    val lengthConversions = mapOf(
        "Inch" to 2.54, "Foot" to 30.48, "Yard" to 91.44, "Mile" to 160934.0, "Centimeter" to 1.0, "Meter" to 100.0, "Kilometer" to 100000.0
    )
    val weightConversions = mapOf(
        "Pound" to 0.453592, "Ounce" to 0.0283495, "Ton" to 907.185, "Kilogram" to 1.0, "Gram" to 0.001
    )

    return when {
        from == to -> value
        from in lengthConversions && to in lengthConversions ->
            value * (lengthConversions[from]!! / lengthConversions[to]!!)
        from in weightConversions && to in weightConversions ->
            value * (weightConversions[from]!! / weightConversions[to]!!)
        from == "Celsius" && to == "Fahrenheit" -> (value * 1.8) + 32
        from == "Fahrenheit" && to == "Celsius" -> (value - 32) / 1.8
        from == "Celsius" && to == "Kelvin" -> value + 273.15
        from == "Kelvin" && to == "Celsius" -> value - 273.15
        else -> throw IllegalArgumentException("Invalid Conversion")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewApp() {
    UnitConverterApp()
}

