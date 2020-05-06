package com.example.kebon.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kebon.R
import com.example.kebon.model.Produk
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_benih.*
import kotlinx.android.synthetic.main.activity_detail_benih.*

class DetailBenihActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_benih)

        val data = intent.getParcelableExtra<Produk>("data")

        tv_nama_detail_benih.text = data.nm_produk
        tv_berat_detail_benih.text = data.berat
        tv_harga_detail_benih.text = data.harga_beli
        tv_cahaya_detail_benih.text = data.cahaya
        tv_deskripsi_detail_benih.text = data.deskripsi
        tv_dimensi_detail_benih.text = data.dimensi
        tv_merk_detail_benih.text = data.merk
        tv_perawatan_detail_benih.text = data.perawatan
        tv_stok_detail_benih.text = data.stok
        tv_ukuran_detail_benih.text = data.ukuran

        Picasso.get().load(data.url).into(iv_photo_detail_benih)

    }
}
