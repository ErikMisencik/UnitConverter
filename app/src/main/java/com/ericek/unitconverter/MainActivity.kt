package com.ericek.unitconverter

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ericek.unitconverter.ui.theme.UnitConverterTheme
import java.math.BigDecimal
import java.math.RoundingMode


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UnitConverterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) { ->
                    UnitConverter()
                }
            }
        }
    }
}


@Composable
fun UnitConverter() {

    var inputValue by remember { mutableStateOf("") }
    var outputValue by remember { mutableStateOf("") }
    var inputUnit by remember { mutableStateOf("Meters") }
    var outputUnit by remember { mutableStateOf("Meters") }
    var iExpanded by remember { mutableStateOf(false) }
    var oExpanded by remember { mutableStateOf(false) }
    var iSet by remember { mutableStateOf(false) }
    var oSet by remember { mutableStateOf(false) }
    val conversionFactors = mapOf(
        "Centimeters" to mapOf(
            "Meters" to 0.01,
            "Feet" to 1 / 30.48,
            "Millimeters" to 10.0,
            "Kilometers" to 0.00001
        ),
        "Meters" to mapOf(
            "Centimeters" to 100.0,
            "Feet" to 3.28084,
            "Millimeters" to 1000.0,
            "Kilometers" to 0.001
        ),
        "Feet" to mapOf(
            "Centimeters" to 30.48,
            "Meters" to 1 / 3.28084,
            "Millimeters" to 304.8,
            "Kilometers" to 0.0003048
        ),
        "Millimeters" to mapOf(
            "Centimeters" to 0.1,
            "Meters" to 0.001,
            "Feet" to 1 / 304.8,
            "Kilometers" to 0.000001
        ),
        "Kilometers" to mapOf(
            "Centimeters" to 100000.0,
            "Meters" to 1000.0,
            "Feet" to 3280.84,
            "Millimeters" to 1000000.0
        ),
    )

    fun updateOutputValue(context: Context) {
        outputValue = if (inputValue.isNotEmpty()) {
            if (inputValue.toDoubleOrNull() == null) {
                Toast.makeText(context, "Please enter a valid number", Toast.LENGTH_SHORT).show()
                return
            }
            val input = BigDecimal(inputValue)
            val factor =
                conversionFactors[inputUnit]?.get(outputUnit)?.toBigDecimal() ?: BigDecimal.ONE
            val result = input.multiply(factor)
                .setScale(6, RoundingMode.HALF_UP) // Rounds to 6 decimal places
            result.stripTrailingZeros()
                .toPlainString() // Removes trailing zeros and avoids scientific notation
        } else {
            ""
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(top = 280.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Unit Converter", fontSize = 25.sp,
            style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(20.dp))
        val context = LocalContext.current
        OutlinedTextField(
            value = inputValue,
            onValueChange = {
                inputValue = it
                updateOutputValue(context)
            },
            label = { Text("Input Value") }
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row {
            Text(text = "Result: $outputValue $outputUnit", fontSize = 18.sp,
                style = MaterialTheme.typography.headlineMedium)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row {
            Spacer(modifier = Modifier.height(10.dp))
            Box() {
                Button(onClick = { iExpanded = true }) {
                    if (!iSet) {
                        Text(
                            "Select", modifier = Modifier.padding(10.dp), fontSize = 16.sp
                        )
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Arrow Down")
                    } else {
                        Text(
                            inputUnit, modifier = Modifier.padding(10.dp), fontSize = 16.sp
                        )
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Arrow Down")
                    }
                }
                //how to set composable to be expanded
                DropdownMenu(expanded = iExpanded, onDismissRequest = { iExpanded = false },
                    offset = androidx.compose.ui.unit.DpOffset(0.dp, 10.dp)
                ) {
                    DropdownMenuItem(
                        text = { Text("Millimeters") },
                        onClick = {
                            inputUnit = "Millimeters"; iSet = true; iExpanded =
                            false; updateOutputValue(context)
                        })
                    DropdownMenuItem(
                        text = { Text("Centimeters") },
                        onClick = {
                            inputUnit = "Centimeters"; iSet = true; iExpanded =
                            false; updateOutputValue(context)
                        })
                    DropdownMenuItem(
                        text = { Text("Meters") },
                        onClick = {
                            inputUnit = "Meters"; iSet = true; iExpanded = false; updateOutputValue(
                            context
                        )
                        })
                    DropdownMenuItem(
                        text = { Text("Kilometers") },
                        onClick = {
                            inputUnit = "Kilometers"; iSet = true; iExpanded =
                            false; updateOutputValue(context)
                        })
                    DropdownMenuItem(
                        text = { Text("Feet") },
                        onClick = {
                            inputUnit = "Feet"; iSet = true; iExpanded = false; updateOutputValue(
                            context
                        )
                        })
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
            Box() {
                Button(onClick = { oExpanded = true }) {
                    if (!oSet) {
                        Text(
                            "Select", modifier = Modifier.padding(10.dp), fontSize = 16.sp
                        )
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Arrow Down")
                    } else {
                        Text(
                            outputUnit, modifier = Modifier.padding(10.dp), fontSize = 16.sp
                        )
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Arrow Down")
                    }
                }
                DropdownMenu(expanded = oExpanded, onDismissRequest = { oExpanded = false },
                    offset = androidx.compose.ui.unit.DpOffset(0.dp, 10.dp)) {
                    DropdownMenuItem(
                        text = { Text("Millimeters") },
                        onClick = {
                            outputUnit = "Millimeters"; oSet = true; oExpanded =
                            false; updateOutputValue(context)
                        })
                    DropdownMenuItem(
                        text = { Text("Centimeters") },
                        onClick = {
                            outputUnit = "Centimeters"; oSet = true; oExpanded =
                            false; updateOutputValue(context)
                        })
                    DropdownMenuItem(
                        text = { Text("Meters") },
                        onClick = {
                            outputUnit = "Meters"; oSet = true; oExpanded =
                            false; updateOutputValue(context)
                        })
                    DropdownMenuItem(
                        text = { Text("Kilometers") },
                        onClick = {
                            outputUnit = "Kilometers"; oSet = true; oExpanded =
                            false; updateOutputValue(context)
                        })
                    DropdownMenuItem(
                        text = { Text("Feet") },
                        onClick = {
                            outputUnit = "Feet"; oSet = true; oExpanded = false; updateOutputValue(
                            context
                        )
                        })
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun UnitConverterPreview() {
    UnitConverter()
}


