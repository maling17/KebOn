package com.example.kebon.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kebon.R
import kotlinx.android.synthetic.main.activity_starter_pack.*

class StarterPackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starter_pack)

        iv_back_starter.setOnClickListener {
            finish()
        }
    }
}
