package com.example.kebon.detail

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.kebon.MainActivity
import com.example.kebon.R
import com.example.kebon.model.Jasa
import com.example.kebon.model.Produk
import com.example.kebon.model.Transaksi
import com.example.kebon.transaksi.CheckoutBeliActivity
import com.example.kebon.transaksi.CheckoutJasaActivity
import com.example.kebon.utils.Preferences
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_tanaman.*
import java.time.LocalDate

class DetailTanamanActivity : AppCompatActivity() {

    private var totaljasa: Int = 1
    private var totaltransaksi: Int = 1
    private var getUsername = ""
    private var hargaBeliProduk: Int = 0
    private var hargaJasaProduk: Int = 0
    private var dateNow: String = ""
    private var url_gambar: String = ""
    private var nama_produk: String = ""
    private var id_produk: String? = ""

    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    private lateinit var mDatabase: DatabaseReference

    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tanaman)

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mFirebaseDatabase = mFirebaseInstance.getReference("Users")
        preferences = Preferences(this)

        getDataProduk()
        getUsername = preferences.getValues("username").toString()

        btn_jasa_detail_tanaman.setOnClickListener {
            buttonPopUpLanjutJasa()
        }
        btn_back_detail_tanaman.setOnClickListener {
            finish()
        }
        btn_beli_detail_tanaman.setOnClickListener {
            buttonPopUpLanjutTransaksi()
        }

    }

    private fun getDataProduk() {
        val dataProduk = intent.getParcelableExtra<Produk>("data")
        id_produk = dataProduk.id_produk
        tv_nama_detail_tanaman.text = dataProduk.nm_produk
        tv_berat_detail_tanaman.text = dataProduk.berat
        tv_harga_detail_tanaman.text = dataProduk.harga_jasa
        tv_cahaya_detail_tanaman.text = dataProduk.cahaya
        tv_deskripsi_detail_tanaman.text = dataProduk.deskripsi
        tv_harga_jasa_detail_tanaman.text = dataProduk.harga_jasa
        tv_dimensi_detail_tanaman.text = dataProduk.dimensi
        tv_merk_detail_tanaman.text = dataProduk.merk
        tv_jenis_tanaman_detail_tanaman.text = dataProduk.jenis_produk
        tv_perawatan_detail_tanaman.text = dataProduk.perawatan
        tv_stok_detail_tanaman.text = dataProduk.stok
        tv_isi_tanaman.text = dataProduk.isi_produk
        tv_ukuran_detail_tanaman.text = dataProduk.ukuran
        tv_tipe_tanaman_detail_tanaman.text = dataProduk.tipe_produk
        tv_jenis_tanaman_detail_tanaman.text = dataProduk.jenis_produk

        val hargaBeli = tv_harga_detail_tanaman.text.toString()
        val hargaJasa = tv_harga_jasa_detail_tanaman.text.toString()
        url_gambar = dataProduk.url.toString()
        nama_produk = dataProduk.nm_produk.toString()
        hargaBeliProduk = hargaBeli.toInt()
        hargaJasaProduk = hargaJasa.toInt()
        Picasso.get().load(dataProduk.url).into(iv_photo_detail_tanaman)
    }

    private fun buttonPopUpLanjutTransaksi() {

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.pop_up_beli)
        dialog.setCancelable(true)

        val btnHome = dialog.findViewById<Button>(R.id.btn_to_keranjang)
        val btnCheckout = dialog.findViewById<Button>(R.id.btn_to_checkout)
        val btnMin = dialog.findViewById<ImageView>(R.id.btn_min)
        val btnPlus = dialog.findViewById<ImageView>(R.id.btn_plus)
        val tvTotaljasa = dialog.findViewById<TextView>(R.id.tv_jumlah_beli)

        tvTotaljasa.text = "$totaltransaksi"

        btnMin.setOnClickListener {

            //jika kuantitas kurang dari 1 maka dialog akan cancel
            if (totaltransaksi == 1) {

                dialog.dismiss()
            } else {

                btnMin.isClickable = true
                totaltransaksi -= 1
                tvTotaljasa.text = "$totaltransaksi"
            }

        }

        btnPlus.setOnClickListener {
            totaltransaksi
            totaltransaksi += 1
            tvTotaljasa.text = "$totaltransaksi"

        }

        btnHome.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                simpanTransaksiBeli()
            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        btnCheckout.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                simpanTransaksiBeli()
            }
            val intent = Intent(
                this,
                CheckoutBeliActivity::class.java
            )
            startActivity(intent)

        }

        dialog.show()

    }

    private fun buttonPopUpLanjutJasa() {

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.pop_up_beli)
        dialog.setCancelable(true)

        val btnHome = dialog.findViewById<Button>(R.id.btn_to_keranjang)
        val btnCheckout = dialog.findViewById<Button>(R.id.btn_to_checkout)
        val btnMin = dialog.findViewById<ImageView>(R.id.btn_min)
        val btnPlus = dialog.findViewById<ImageView>(R.id.btn_plus)
        val tvTotaljasa = dialog.findViewById<TextView>(R.id.tv_jumlah_beli)

        tvTotaljasa.text = "$totaljasa"

        btnMin.setOnClickListener {

            //jika kuantitas kurang dari 1 dialog akan cancel
            if (totaljasa == 1) {

                dialog.dismiss()
            } else {

                btnMin.isClickable = true
                totaljasa -= 1
                tvTotaljasa.text = "$totaljasa"
            }

        }

        btnPlus.setOnClickListener {
            totaljasa
            totaljasa += 1
            tvTotaljasa.text = "$totaljasa"

        }

        btnHome.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                simpanTransaksiJasa()
            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        btnCheckout.setOnClickListener {

            val intent = Intent(
                this,
                CheckoutJasaActivity::class.java
            )
            startActivity(intent)
        }

        dialog.show()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun simpanTransaksiBeli() {
        val dataProduk = intent.getParcelableExtra<Produk>("data")

        // mengambil data id produk berbeda model
        id_produk = dataProduk.id_produk

        // total harga untuk subtotal produk jasa
        val totalHargaProduk = totaltransaksi * hargaBeliProduk

        val currentDate = LocalDate.now()
        dateNow = currentDate.toString()

        val transaksi = Transaksi()
        transaksi.id_produk = id_produk
        transaksi.jumlah_beli = totaltransaksi.toString()
        transaksi.status_beli = "1"
        val key = mFirebaseDatabase.child(getUsername).child("Transaksi").push().key
        transaksi.id_transaksi = key
        transaksi.subtotal_produk_beli = totalHargaProduk.toString()
        transaksi.tgl_transaksi = dateNow
        transaksi.kategori = "beli"
        transaksi.url_gambar = url_gambar
        transaksi.nm_produk = nama_produk
        mFirebaseDatabase.child(getUsername).child("Transaksi").child(key.toString())
            .setValue(transaksi)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun simpanTransaksiJasa() {
        val dataProduk = intent.getParcelableExtra<Produk>("data")

        // mengambil data id produk berbeda model
        id_produk = dataProduk.id_produk

        // total harga untuk subtotal produk jasa
        val totalHargaProduk = totaljasa * hargaJasaProduk

        val currentDate = LocalDate.now()
        dateNow = currentDate.toString()

        val jasa = Jasa()
        jasa.id_produk = id_produk
        jasa.jumlah_jasa = totaljasa.toString()
        jasa.status_jasa = "1"
        val key = mFirebaseDatabase.child(getUsername).child("Transaksi").child("Jasa").push().key
        jasa.id_jasa = key
        jasa.subtotal_produk_jasa = totalHargaProduk.toString()
        jasa.tgl_transaksi = dateNow
        jasa.kategori = "jasa"
        jasa.url_gambar = url_gambar
        jasa.nm_produk = nama_produk

        mFirebaseDatabase.child(getUsername).child("Transaksi").child("Jasa").child(key.toString())
            .setValue(jasa)

    }
}
