package com.example.kebon.transaksi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.example.kebon.R
import com.example.kebon.utils.Preferences
import kotlinx.android.synthetic.main.activity_pilih_kurir.*

class PilihKurirActivity : AppCompatActivity() {

    private var sKurir:String=""
    private var sHarga:Int=0
    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_kurir)

        preferences = Preferences(applicationContext)


        btn_kurir_instant.setOnClickListener {
            sKurir="Instant"
            sHarga=30000

            preferences.setValues("NmKurir",sKurir)
            preferences.setValues("HargaKurir",sHarga.toString())

            changeBackground(btn_kurir_instant,
                R.drawable.rounded_layout_orange
            )
            changeBackground(btn_kurir_reguler,
                R.drawable.rounded_layout_gray
            )
            changeBackground(btn_kurir_hemat,
                R.drawable.rounded_layout_gray
            )
        }

        btn_kurir_reguler.setOnClickListener {

            sKurir="Reguler"
            sHarga=15000

            preferences.setValues("NmKurir",sKurir)
            preferences.setValues("HargaKurir",sHarga.toString())

            changeBackground(btn_kurir_instant,
                R.drawable.rounded_layout_gray
            )
            changeBackground(btn_kurir_reguler,
                R.drawable.rounded_layout_orange
            )
            changeBackground(btn_kurir_hemat,
                R.drawable.rounded_layout_gray
            )
        }

        btn_kurir_hemat.setOnClickListener {

            sKurir="Hemat"
            sHarga=7500

            preferences.setValues("NmKurir",sKurir)
            preferences.setValues("HargaKurir",sHarga.toString())

            changeBackground(btn_kurir_instant,
                R.drawable.rounded_layout_gray
            )
            changeBackground(btn_kurir_reguler,
                R.drawable.rounded_layout_gray
            )
            changeBackground(btn_kurir_hemat,
                R.drawable.rounded_layout_orange
            )
        }

        btn_konfirmasi_kurir.setOnClickListener {
            onBackPressed()
        }

    }
    private fun changeBackground(button: LinearLayout, int: Int) {
        button.setBackgroundResource(int)
    }
}