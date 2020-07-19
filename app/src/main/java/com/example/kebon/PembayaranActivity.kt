package com.example.kebon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kebon.transaksi.UploadBuktiActivity
import kotlinx.android.synthetic.main.activity_pembayaran.*

class PembayaranActivity : AppCompatActivity() {

    private var kategoriTransaksi=" "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran)

        kategoriTransaksi=intent.getStringExtra("kategori_transaksi").toString()
        Toast.makeText(this, kategoriTransaksi, Toast.LENGTH_LONG).show()

        val getTotalPembayaran=intent.getStringExtra("total_pembayaran").toString()
        btn_back_pembayaran.setOnClickListener {
            onBackPressed()
        }
        btn_home_pembayaran.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
        btn_saya_sudah_bayar.setOnClickListener {
            val intent = Intent(this,UploadBuktiActivity::class.java)
            intent.putExtra("kategori_transaksi",kategoriTransaksi)
            startActivity(intent)

        }
        tv_total_pembayaran.text="Rp$getTotalPembayaran"
    }
}