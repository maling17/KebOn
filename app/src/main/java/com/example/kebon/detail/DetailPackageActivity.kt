package com.example.kebon.detail

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kebon.MainActivity
import com.example.kebon.R
import com.example.kebon.model.Checkout
import com.example.kebon.model.Produk
import com.example.kebon.model.StarterProduk
import com.example.kebon.transaksi.CheckoutBeliActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_package.*
import java.util.*

class DetailPackageActivity : AppCompatActivity() {

    private var kategori: String? = "produk"
    private var dataList = ArrayList<Checkout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_package)

        val getKategori = intent.getStringExtra(kategori)


        btn_beli_detail_package.setOnClickListener {
            val namaProduk: String = tv_nama_detail_package.text.toString()

            val hargaBeli: String = tv_harga_detail_package.text.toString()
            val newHargaBeli = hargaBeli.toInt()

            val stok: String = tv_stok_detail_package.text.toString()
            val newStok = stok.toInt()

            val data = Checkout(namaProduk, newHargaBeli, newStok)

            dataList.add(data)

            buttonPopUp()

            /*Toast.makeText(this@DetailPackageActivity, "" + data.namaProduk, Toast.LENGTH_LONG)
                .show()*/
        }

        iv_photo_detail_package.setOnClickListener {
            dataList.clear()
            Toast.makeText(this, "data clear", Toast.LENGTH_LONG).show()
        }

        if (getKategori == "produk") {
            getDataProduk()

        } else {
            getDataStarterProduk()
        }

    }

    private fun getDataStarterProduk() {

        val dataStarter = intent.getParcelableExtra<StarterProduk>("data")
        tv_nama_detail_package.text = dataStarter.nm_produk
        tv_berat_detail_package.text = dataStarter.berat
        tv_harga_detail_package.text = dataStarter.harga_beli
        tv_cahaya_detail_package.text = dataStarter.cahaya
        tv_deskripsi_detail_package.text = dataStarter.deskripsi
        tv_dimensi_detail_package.text = dataStarter.dimensi
        tv_merk_detail_package.text = dataStarter.merk
        tv_jenis_tanaman_detail_package.text = dataStarter.jenis_produk
        tv_perawatan_detail_package.text = dataStarter.perawatan
        tv_stok_detail_package.text = dataStarter.stok
        tv_isi_package.text = dataStarter.isi_produk
        tv_ukuran_detail_package.text = dataStarter.ukuran
        tv_tipe_tanaman_detail_package.text = dataStarter.tipe_produk
        tv_jenis_tanaman_detail_package.text = dataStarter.jenis_produk

        Picasso.get().load(dataStarter.url).into(iv_photo_detail_package)
    }

    private fun getDataProduk() {
        val dataProduk = intent.getParcelableExtra<Produk>("data")
        tv_nama_detail_package.text = dataProduk.nm_produk
        tv_berat_detail_package.text = dataProduk.berat
        tv_harga_detail_package.text = dataProduk.harga_beli
        tv_cahaya_detail_package.text = dataProduk.cahaya
        tv_deskripsi_detail_package.text = dataProduk.deskripsi
        tv_dimensi_detail_package.text = dataProduk.dimensi
        tv_merk_detail_package.text = dataProduk.merk
        tv_jenis_tanaman_detail_package.text = dataProduk.jenis_produk
        tv_perawatan_detail_package.text = dataProduk.perawatan
        tv_stok_detail_package.text = dataProduk.stok
        tv_isi_package.text = dataProduk.isi_produk
        tv_ukuran_detail_package.text = dataProduk.ukuran
        tv_tipe_tanaman_detail_package.text = dataProduk.tipe_produk
        tv_jenis_tanaman_detail_package.text = dataProduk.jenis_produk

        Picasso.get().load(dataProduk.url).into(iv_photo_detail_package)
    }

    private fun buttonPopUp() {
        val dialog = Dialog(this@DetailPackageActivity)
        dialog.setContentView(R.layout.pop_up_beli)
        dialog.setCancelable(true)

        val btnHome = dialog.findViewById<Button>(R.id.btn_to_home)
        val btnCheckout = dialog.findViewById<Button>(R.id.btn_to_checkout)

        btnHome.setOnClickListener {
            val intent = Intent(this@DetailPackageActivity, MainActivity::class.java)
            startActivity(intent)
        }
        btnCheckout.setOnClickListener {
            val intent = Intent(this@DetailPackageActivity, CheckoutBeliActivity::class.java)
            startActivity(intent)
        }
        dialog.show()

    }
}
