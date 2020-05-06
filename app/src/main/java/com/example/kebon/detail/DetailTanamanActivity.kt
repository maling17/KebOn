package com.example.kebon.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kebon.R
import com.example.kebon.model.Produk
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_tanaman.*

class DetailTanamanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tanaman)


        val data = intent.getParcelableExtra<Produk>("data")

        tv_nama_detail_tanaman.text = data.nm_produk
        tv_berat_detail_tanaman.text = data.berat
        tv_harga_detail_tanaman.text = data.harga_beli
        tv_cahaya_detail_tanaman.text = data.cahaya
        tv_deskripsi_detail_tanaman.text = data.deskripsi
        tv_dimensi_detail_tanaman.text = data.dimensi
        tv_merk_detail_tanaman.text = data.merk
        tv_jenis_tanaman_detail_tanaman.text = data.jenis_produk
        tv_perawatan_detail_tanaman.text = data.perawatan
        tv_stok_detail_tanaman.text = data.stok
        tv_harga_jasa_detail_tanaman.text=data.harga_jasa
        tv_isi_tanaman.text = data.isi_produk
        tv_ukuran_detail_tanaman.text = data.ukuran
        tv_tipe_tanaman_detail_tanaman.text = data.tipe_produk
        tv_jenis_tanaman_detail_tanaman.text = data.jenis_produk

        Picasso.get().load(data.url).into(iv_photo_detail_tanaman)

    }
}
