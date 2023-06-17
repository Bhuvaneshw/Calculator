package com.acutecoder.jetpacktest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acutecoder.jetpacktest.ui.theme.JetPackTestTheme

/**
 * Created by Bhuvaneshwaran
 * on 12:56 AM, 6/16/2023
 *
 * @author AcuteCoder
 */

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            JetPackTestTheme {
                MainUI()
            }
        }
    }

    @Preview
    @Composable
    private fun MainUI() {
        var expression by remember {
            mutableStateOf("")
        }
        var prevExpression by remember {
            mutableStateOf("")
        }
        Body {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutputView(prevExpression, 20.sp)
                OutputView(expression)
                Spacer(modifier = Modifier.height(10.dp))
                Row1({
                    expression = ""
                    prevExpression = ""
                }, {
                    expression = expression.dropLast(1)
                }) {
                    expression += it
                }
                Row2 {
                    expression += it
                }
                Row3 {
                    expression += it
                }
                Row4 {
                    expression += it
                }
                Row5({
                    val output = Calculator.evalSafely(expression)
                    expression = output.output
                    prevExpression = output.expression
                }) {
                    if (it != "." || Calculator.canAddDot(expression))
                        expression += it
                }
            }
        }
    }

    @Composable
    private fun Row5(calculate: () -> Unit, callback: (String) -> Unit) {
        ButtonRow {
            Button(
                text = "(", modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFF323232)),
                ratio = .5f
            ) { callback("(") }
            Button(
                text = ")", modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFF323232)),
                ratio = .5f
            ) { callback(")") }
            Button(text = "0", modifier = Modifier.weight(2f)) { callback("0") }
            Button(text = ".", modifier = Modifier.weight(2f)) { callback(".") }
            Button(
                text = "=",
                modifier = Modifier
                    .weight(2f)
                    .background(Color(0xFFF36E4B))
            ) { calculate() }
        }
    }

    @Composable
    private fun Row4(callback: (String) -> Unit) {
        ButtonRow {
            Button(text = "7", modifier = Modifier.weight(1f)) { callback("7") }
            Button(text = "8", modifier = Modifier.weight(1f)) { callback("8") }
            Button(text = "9", modifier = Modifier.weight(1f)) { callback("9") }
            Button(
                text = "+",
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFF323232))
            ) { callback("+") }
        }
    }

    @Composable
    private fun Row3(callback: (String) -> Unit) {
        ButtonRow {
            Button(text = "4", modifier = Modifier.weight(1f)) { callback("4") }
            Button(text = "5", modifier = Modifier.weight(1f)) { callback("5") }
            Button(text = "6", modifier = Modifier.weight(1f)) { callback("6") }
            Button(
                text = "-",
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFF323232))
            ) { callback("-") }
        }
    }

    @Composable
    private fun Row2(callback: (String) -> Unit) {
        ButtonRow {
            Button(text = "1", modifier = Modifier.weight(1f)) { callback("1") }
            Button(text = "2", modifier = Modifier.weight(1f)) { callback("2") }
            Button(text = "3", modifier = Modifier.weight(1f)) { callback("3") }
            Button(
                text = "*",
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFF323232))
            ) { callback("*") }
        }
    }

    @Composable
    private fun Row1(clear: () -> Unit, delete: () -> Unit, callback: (String) -> Unit) {
        ButtonRow {
            Button(
                text = "AC", modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFF323232))
            ) { clear() }
            Button(
                text = "Del", modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFF323232))
            ) { delete() }
            Button(
                text = "^",
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFF323232)),
            ) { callback("^") }
            Button(
                text = "%",
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFF323232))
            ) { callback("%") }
            Button(
                text = "/",
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFF323232))
            ) { callback("/") }
        }
    }

    @Composable
    private fun Button(
        text: String,
        modifier: Modifier = Modifier,
        ratio: Float = 1f,
        onClick: () -> Unit
    ) {
        Box(
            modifier = Modifier
                .padding(6.dp)
                .clip(CircleShape)
                .background(Color(0xFF1A1A1A))
                .then(modifier)
                .aspectRatio(ratio)
                .clickable { onClick() }
                .padding(vertical = 20.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text, color = Color.White
            )
        }
    }

    @Composable
    private fun ButtonRow(content: @Composable RowScope.() -> Unit) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            content()
        }
    }

    @Composable
    private fun Body(content: @Composable () -> Unit) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF000000))
                .padding(6.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            content()
        }
    }

    @Composable
    private fun OutputView(expression: String, fontSize: TextUnit = 30.sp) {
        Text(
            text = expression,
            color = Color.White,
            textAlign = TextAlign.End,
            fontSize = fontSize,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        )
    }
}
