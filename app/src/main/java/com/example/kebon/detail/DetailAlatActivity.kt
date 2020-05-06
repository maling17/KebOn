package com.example.kebon.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kebon.R
import com.example.kebon.model.Produk
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_alat.*

class DetailAlatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_alat)

        val data = intent.getParcelableExtra<Produk>("data")

        tv_nama_detail_alat.text = data.nm_produk
        tv_berat_detail_alat.text = data.berat
        tv_harga_detail_alat.text = data.harga_beli
        tv_deskripsi_detail_alat.text = data.deskripsi
        tv_dimensi_detail_alat.text = data.dimensi
        tv_merk_detail_alat.text = data.merk
        tv_stok_detail_alat.text = data.stok
        Picasso.get().load(data.url).into(iv_photo_detail_alat)

    }
}
