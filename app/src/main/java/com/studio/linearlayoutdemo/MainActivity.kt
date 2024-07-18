package com.studio.linearlayoutdemo

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var index = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val lineFeedLayout = this.findViewById<LineFeedLayout>(R.id.lineFeedLayout).apply {
            verticalGap = 20
            horizontalGap = 50
        }
        this.findViewById<TextView>(R.id.btnAdd).setOnClickListener {

            TextView(this).apply {
                text = "Tag $index"
                textSize = 20f
                setBackgroundColor(Color.parseColor("#888888"))
                gravity = Gravity.CENTER
                setPadding(8, 3, 8, 3)
                setTextColor(Color.parseColor("#FFFFFF"))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    rightMargin = 15
                    bottomMargin = 40
                }
            }.also { lineFeedLayout.addView(it) }

            index++
        }
    }
}