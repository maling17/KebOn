package com.example.kebon.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kebon.R
import kotlinx.android.synthetic.main.activity_tips.*

class TipsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tips)

        iv_back_tips.setOnClickListener {
            finish()
        }
    }
}
