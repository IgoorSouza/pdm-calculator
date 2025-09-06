package com.igor.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvDisplay: TextView
    private var currentInput = ""
    private var lastOperator = ""
    private var firstValue = 0.0
    private var isNewOperation = true
    private var lastWasOperator = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvDisplay = findViewById(R.id.tvDisplay)

        val numberButtonsIds = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        for (buttonId in numberButtonsIds) {
            findViewById<Button>(buttonId).setOnClickListener { onNumberClick(it as Button) }
        }

        val operatorButtonsIds = listOf(
            R.id.btnAdd, R.id.btnSub, R.id.btnMul, R.id.btnDiv
        )

        for (buttonId in operatorButtonsIds) {
            findViewById<Button>(buttonId).setOnClickListener { onOperatorClick(it as Button) }
        }

        findViewById<Button>(R.id.btnDot).setOnClickListener { onDotClick() }
        findViewById<Button>(R.id.btnEquals).setOnClickListener { onEqualClick() }
        findViewById<Button>(R.id.btnC).setOnClickListener { onClearClick() }
    }

    private fun onNumberClick(button: Button) {
        if (isNewOperation) {
            tvDisplay.text = ""
            isNewOperation = false
            currentInput = ""
        }

        currentInput += button.text
        tvDisplay.text = currentInput
        lastWasOperator = false
    }

    private fun onDotClick() {
        if (isNewOperation) {
            currentInput = "0."
            isNewOperation = false
        } else if (!currentInput.contains(".")) {
            currentInput += "."
        }

        tvDisplay.text = currentInput
        lastWasOperator = false
    }

    private fun onOperatorClick(button: Button) {
        if (currentInput.isEmpty()) return
        if (lastWasOperator) return
        if (lastOperator.isNotEmpty() && currentInput.substringAfter(lastOperator).isNotEmpty()) {
            return
        }

        firstValue = currentInput.toDouble()
        lastOperator = button.text.toString()
        currentInput += lastOperator
        tvDisplay.text = currentInput
        isNewOperation = false
        lastWasOperator = true
    }

    private fun onEqualClick() {
        if (currentInput.isEmpty() || lastOperator.isEmpty() || lastWasOperator) return

        val parts = currentInput.split(lastOperator)
        if (parts.size < 2) return

        val secondValue = parts[1].toDouble()
        var result = 0.0

        try {
            result = when (lastOperator) {
                "+" -> firstValue + secondValue
                "−" -> firstValue - secondValue
                "×" -> firstValue * secondValue
                "÷" -> {
                    if (secondValue == 0.0) {
                        tvDisplay.text = "Erro"
                        return
                    } else {
                        firstValue / secondValue
                    }
                }
                else -> 0.0
            }
        } catch (_: Exception) {
            tvDisplay.text = "Erro"
            return
        }

        firstValue = result
        currentInput = result.toString()
        tvDisplay.text = if (result % 1 == 0.0) {
            result.toInt().toString()
        } else {
            result.toString()
        }
        lastOperator = ""
        isNewOperation = true
        lastWasOperator = false
    }

    private fun onClearClick() {
        currentInput = ""
        firstValue = 0.0
        lastOperator = ""
        isNewOperation = true
        lastWasOperator = false
        tvDisplay.text = "0"
    }
}
