package com.example.kebon

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_produk_populer.*

class ProdukPopulerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produk_populer)

        iv_back_produk_populer.setOnClickListener {
            finish()
        }
    }
}