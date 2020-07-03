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
import com.example.kebon.MainActivity
import com.example.kebon.R
import com.example.kebon.model.Detail_Transaksi
import com.example.kebon.model.Produk
import com.example.kebon.model.Transaksi
import com.example.kebon.transaksi.CheckoutBeliActivity
import com.example.kebon.utils.Preferences
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_benih.*
import java.time.LocalDate

class DetailBenihActivity : AppCompatActivity() {
    private var totaltransaksi: Int = 1
    private var getUsername = ""
    private var hargaBeliProduk: Int = 0
    private var dateNow: String = ""
    private var url_gambar: String = ""
    private var nama_produk: String = ""

    private var id_produk: String? = ""
    private var sIdTransaksi: String? = ""

    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    private lateinit var mDatabase: DatabaseReference

    private lateinit var preferences: Preferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_benih)

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mFirebaseDatabase = mFirebaseInstance.getReference("Users")
        preferences = Preferences(this)
        getUsername = preferences.getValues("username").toString()

        getDataProduk() // mengambil data-data produk yang telah di klik

        btn_beli_detail_benih.setOnClickListener {
            buttonPopUpLanjutTransaksi()
        }
        btn_back_detail_benih.setOnClickListener {
            finish()
        }
    }

    private fun getDataProduk() {
        val data = intent.getParcelableExtra<Produk>("data")
        id_produk = data.id_produk
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

        val hargaBeli = tv_harga_detail_benih.text.toString()
        url_gambar = data.url.toString()
        nama_produk = data.nm_produk.toString()

        hargaBeliProduk = hargaBeli.toInt()

        Picasso.get().load(data.url).into(iv_photo_detail_benih)
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

        //cek field id transaksi yang ada [Start 2]
        val check =
            mFirebaseDatabase.child(getUsername).child("Transaksi").orderByChild("status_beli")
                .equalTo("1")
                .limitToFirst(1)

        check.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(
                    this@DetailBenihActivity,
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
}
