package com.zoryak.ai

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private lateinit var messages: LinearLayout
    private lateinit var input: EditText
    private val purple = Color.rgb(103, 80, 164)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(23, 21, 38)
        window.navigationBarColor = Color.rgb(23, 21, 38)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.rgb(247, 245, 252))
            setPadding(dp(16), dp(12), dp(16), dp(12))
        }
        val header = TextView(this).apply {
            text = "Zoryak AI 14.0"
            textSize = 24f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.rgb(35, 29, 55))
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(4), dp(12), dp(4), dp(16))
        }
        root.addView(header, LinearLayout.LayoutParams(-1, -2))

        val scroll = ScrollView(this)
        messages = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(4), dp(4), dp(4), dp(12))
        }
        scroll.addView(messages)
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))
        addMessage("Zoryak AI", "Merhaba! Ben Zoryak AI 14.0. Bu başlangıç sürümünde arayüz hazır. Gerçek yapay zekâ yanıtları için bir model sunucusuna bağlanmamız gerekiyor.", false)

        val composer = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        input = EditText(this).apply {
            hint = "Mesajını yaz…"
            textSize = 16f
            setSingleLine(false)
            maxLines = 4
            setPadding(dp(12), dp(8), dp(12), dp(8))
            background = roundedBackground(Color.WHITE, dp(16).toFloat())
        }
        composer.addView(input, LinearLayout.LayoutParams(0, -2, 1f))
        val send = Button(this).apply {
            text = "Gönder"
            setTextColor(Color.WHITE)
            background = roundedBackground(purple, dp(14).toFloat())
            setOnClickListener {
                val text = input.text.toString().trim()
                if (text.isNotEmpty()) {
                    addMessage("Sen", text, true)
                    input.setText("")
                    addMessage("Zoryak AI", "Mesajın arayüze eklendi. Bu başlangıç projesinde henüz yapay zekâ sunucusu bağlı değil.", false)
                    scroll.post { scroll.fullScroll(ScrollView.FOCUS_DOWN) }
                } else {
                    Toast.makeText(this@MainActivity, "Önce bir mesaj yaz.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        composer.addView(send, LinearLayout.LayoutParams(-2, dp(52)).apply { leftMargin = dp(8) })
        root.addView(composer, LinearLayout.LayoutParams(-1, -2))
        setContentView(root)
    }

    private fun addMessage(author: String, body: String, user: Boolean) {
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(12), dp(10), dp(12), dp(10))
            background = roundedBackground(if (user) Color.rgb(232, 224, 250) else Color.WHITE, dp(14).toFloat())
        }
        val title = TextView(this).apply {
            text = author
            textSize = 12f
            setTypeface(null, Typeface.BOLD)
            setTextColor(purple)
        }
        val text = TextView(this).apply {
            this.text = body
            textSize = 16f
            setTextColor(Color.rgb(40, 37, 48))
            setPadding(0, dp(4), 0, 0)
        }
        card.addView(title)
        card.addView(text)
        messages.addView(card, LinearLayout.LayoutParams(-1, -2).apply {
            bottomMargin = dp(10)
        })
    }

    private fun roundedBackground(color: Int, radius: Float) = android.graphics.drawable.GradientDrawable().apply {
        setColor(color)
        cornerRadius = radius
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
