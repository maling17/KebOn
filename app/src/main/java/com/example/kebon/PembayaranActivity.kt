package com.example.kebon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_pembayaran.*

class PembayaranActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran)

        val getTotalPembayaran=intent.getStringExtra("total_pembayaran").toString()
        btn_back_pembayaran.setOnClickListener {
            onBackPressed()
        }

        tv_total_pembayaran.text=getTotalPembayaran
    }
}