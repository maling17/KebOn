package com.example.kebon.detail

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.kebon.R
import com.example.kebon.model.*
import com.example.kebon.transaksi.CheckoutBeliActivity
import com.example.kebon.transaksi.CheckoutJasaActivity
import com.example.kebon.utils.Preferences
import com.google.firebase.database.*
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
    private var sIdTransaksi: String? = ""
    private var sIdJasa: String? = ""

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
        tv_harga_detail_tanaman.text = dataProduk.harga_beli
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
            /* val intent = Intent(this, MainActivity::class.java)
             startActivity(intent)*/
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
            /* val intent = Intent(this, MainActivity::class.java)
             startActivity(intent)*/
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
        val detailTransaksi = Detail_Transaksi()
        detailTransaksi.id_produk = id_produk
        detailTransaksi.jumlah_beli = totaltransaksi.toString()
        transaksi.status_beli = "1"
        detailTransaksi.url_gambar = url_gambar
        detailTransaksi.harga_produk = totalHargaProduk.toString()
        detailTransaksi.nm_produk = nama_produk
        val key = mFirebaseDatabase.child(getUsername).child("Transaksi").push().key
        transaksi.id_transaksi = key
        transaksi.tgl_transaksi = dateNow
        transaksi.username = getUsername
        preferences.setValues("id_transaksi",key.toString())

        //cek field id transaksi yang ada [Start 2]
        val check =
            mFirebaseDatabase.child(getUsername).child("Transaksi").orderByChild("status_beli")
                .equalTo("1")
                .limitToFirst(1)

        check.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(
                    this@DetailTanamanActivity,
                    "KOSONG",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (dataSnapshot in p0.children) {

                        sIdTransaksi =
                            dataSnapshot.child("id_transaksi").value.toString()

                        mFirebaseDatabase.child(getUsername).child("Transaksi")
                            .child(sIdTransaksi.toString())
                            .child("Detail_Transaksi")
                            .child(id_produk.toString())
                            .setValue(detailTransaksi)

                        preferences.setValues("id_transaksi",sIdTransaksi.toString())
                    }
                } else {
                    mFirebaseDatabase.child(getUsername).child("Transaksi")
                        .child(key.toString())
                        .setValue(transaksi)

                    mFirebaseDatabase.child(getUsername).child("Transaksi")
                        .child(key.toString())
                        .child("Detail_Transaksi")
                        .child(id_produk.toString())
                        .setValue(detailTransaksi)

                    preferences.setValues("id_transaksi",key.toString())
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun simpanTransaksiJasa() {
        val dataProduk = intent.getParcelableExtra<Produk>("data")

        // mengambil data id produk berbeda model
        id_produk = dataProduk.id_produk

        // total harga untuk subtotal produk jasa
        val totalHargaProduk = totaljasa * hargaBeliProduk

        val currentDate = LocalDate.now()
        dateNow = currentDate.toString()

        val jasa = Jasa()
        val detailJasa = Detail_Jasa()
        detailJasa.id_produk = id_produk
        detailJasa.jumlah_jasa = totaljasa.toString()
        detailJasa.url_gambar = url_gambar
        detailJasa.nm_produk = nama_produk
        detailJasa.harga_beli = totalHargaProduk.toString()
        detailJasa.harga_jasa = hargaJasaProduk.toString()

        val key = mFirebaseDatabase.child(getUsername).child("Jasa").push().key
        jasa.id_jasa = key
        jasa.status_jasa = "1"
        jasa.tgl_transaksi = dateNow

        //cek field id transaksi yang ada [Start 2]
        val check =
            mFirebaseDatabase.child(getUsername).child("Jasa").orderByChild("status_jasa")
                .equalTo("1")
                .limitToFirst(1)

        check.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(
                    this@DetailTanamanActivity,
                    "KOSONG",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (dataSnapshot in p0.children) {

                        sIdJasa =
                            dataSnapshot.child("id_jasa").value.toString()
                        mFirebaseDatabase.child(getUsername).child("Jasa")
                            .child(sIdJasa.toString())
                            .child("Detail_Jasa")
                            .child(id_produk.toString())
                            .setValue(detailJasa)
                        preferences.setValues("id_jasa",sIdJasa.toString())
                    }
                } else {
                    mFirebaseDatabase.child(getUsername).child("Jasa")
                        .child(key.toString())
                        .setValue(jasa)
                    mFirebaseDatabase.child(getUsername).child("Jasa")
                        .child(key.toString())
                        .child("Detail_Jasa")
                        .child(id_produk.toString())
                        .setValue(detailJasa)
                    preferences.setValues("id_jasa",key.toString())
                }
            }
        })

    }
}

