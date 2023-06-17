package com.acutecoder.jetpacktest

import java.util.Stack
import kotlin.math.pow

/**
 * Created by Bhuvaneshwaran
 * on 6:38 PM, 6/16/2023
 *
 * @author AcuteCoder
 */

object Calculator {

    private val operatorStack = Stack<Char>()
    private val operandStack = Stack<String>()

    fun evalSafely(exp: String): Output {
        val expression = scanAndAddStar(exp)
        return try {
            val out = eval(expression)
            Output(expression, out)
        } catch (e: Exception) {
            return if (!hasEqualBrackets(expression)) {
                try {
                    val balancedExpression = balanceBrackets(expression)
                    val out = eval(balancedExpression)
                    Output(balancedExpression, out)
                } catch (e: Exception) {
                    Output(expression, "Syntax Error")
                }
            } else Output(expression, "Syntax Error")
        }
    }

    private fun scanAndAddStar(exp: String): String {
        var i = 0
        val out = StringBuilder(exp)
        while (i < exp.length - 2) {
            if (exp[i].isDigit() && exp[i + 1] == '(')
                out.insert(i + 1, "*")
            i++
        }
        return out.toString()
    }

    private fun balanceBrackets(exp: String): String {
        var out = exp
        val openCount = out.count("(")
        var closeCount = out.count(")")
        while (closeCount < openCount) {
            out += ")"
            closeCount++
        }
        return out
    }

    private fun hasEqualBrackets(exp: String) =
        exp.count("(") == exp.count(")")

    fun eval(exp: String): String {
        operatorStack.clear()
        operandStack.clear()

        var i = 0
        while (i < exp.length) {
            val c = exp[i]
            if (c.isDigit()) {
                var digit = c.toString()
                i++
                while (i < exp.length && (exp[i].isDigit() || exp[i] == '.')) {
                    digit += exp[i]
                    i++
                }
                i--
                operandStack.push(digit)
            } else if (c == '(') {
                operatorStack.push(c)
            } else if (c == ')') {
                while (operatorStack.peek() != '(') {
                    val b = operandStack.pop()
                    val a = operandStack.pop()
                    val op = operatorStack.pop()
                    operandStack.push(performOperation(a.toDouble(), b.toDouble(), op).toString())
                }
                operatorStack.pop()
            } else if (isOperator(c)) {
                while (!operatorStack.empty() && precedence(c) <= precedence(operatorStack.peek())) {
                    val b = operandStack.pop()
                    val a = operandStack.pop()
                    val op = operatorStack.pop()
                    operandStack.push(performOperation(a.toDouble(), b.toDouble(), op).toString())
                }
                operatorStack.push(c)
            }
            i++
        }
        while (!operatorStack.empty()) {
            val b = operandStack.pop()
            val a = operandStack.pop()
            val op = operatorStack.pop()
            operandStack.push(performOperation(a.toDouble(), b.toDouble(), op).toString())
        }

        return operandStack.pop()
    }

    private fun performOperation(a: Double, b: Double, op: Char): Double {
        return when (op) {
            '+' -> a + b
            '-' -> a - b
            '*' -> a * b
            '/' -> a / b
            '^' -> a.pow(b)
            '%' -> a.mod(b)
            else -> 0.0
        }
    }

    private fun isOperator(c: Char) =
        c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '%'

    private fun precedence(c: Char) = when (c) {
        '+' -> 1
        '-' -> 1
        '*' -> 2
        '/' -> 2
        '%' -> 2
        '^' -> 3
        else -> 0
    }

    fun canAddDot(expression: String): Boolean {
        var num = ""
        var i = expression.length - 1

        while (i >= 0) {
            val c = expression[i]
            if (c.isDigit() || c == '.')
                num += c
            else
                break
            i--
        }

        return !num.contains(".")
    }
}

private fun String.count(target: String): Int {
    var count = 0
    var i = 0

    while (i < length - target.length) {
        if (substring(i, i + target.length) == target)
            count++
        i += target.length
    }

    return count
}

data class Output(val expression: String, val output: String)
