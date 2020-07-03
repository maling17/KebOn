package com.example.kebon.detail

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.kebon.MainActivity
import com.example.kebon.R
import com.example.kebon.model.*
import com.example.kebon.transaksi.CheckoutBeliActivity
import com.example.kebon.utils.Preferences
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_package.*
import java.time.LocalDate
import java.util.*

class DetailPackageActivity : AppCompatActivity() {

    private var kategori: String? = "produk"
    var getKategori: String? = ""

    private var dataList = ArrayList<Checkout>()

    private var totalBeli: Int = 1
    private var getUsername = ""
    private var hargaProduk: Int = 0
    private var dateNow: String = ""
    private var url_gambar: String = ""
    private var nama_produk: String = ""
    private var totaltransaksi: Int = 1
    private var hargaBeliProduk: Int = 0
    private var id_produk: String? = ""
    private var id_produk_starter: String? = ""
    private var sIdTransaksi: String? = ""


    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    private lateinit var mDatabase: DatabaseReference

    private lateinit var preferences: Preferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_package)

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mFirebaseDatabase = mFirebaseInstance.getReference("Users")
        preferences = Preferences(this)

        getKategori = intent.getStringExtra(kategori)
        getUsername = preferences.getValues("username").toString()

        btn_beli_detail_package.setOnClickListener {

            buttonPopUpLanjut()

        }

        btn_back_detail_package.setOnClickListener {
            finish()
        }

        if (getKategori == "produk") {
            getDataProduk()

        } else {
            getDataStarterProduk()
        }

    }

    private fun getDataStarterProduk() {

        val dataStarter = intent.getParcelableExtra<StarterProduk>("data")
        id_produk_starter = dataStarter.id_produk
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

        val harga = tv_harga_detail_package.text.toString()
        hargaProduk = harga.toInt()

        url_gambar = dataStarter.url.toString()
        nama_produk = dataStarter.nm_produk.toString()

        Picasso.get().load(dataStarter.url).into(iv_photo_detail_package)
        Log.d(Companion.TAG, "getDataStarterProduk: $dataStarter")
    }

    private fun getDataProduk() {
        val dataProduk = intent.getParcelableExtra<Produk>("data")
        id_produk = dataProduk.id_produk
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

        val harga = tv_harga_detail_package.text.toString()
        hargaProduk = harga.toInt()

        url_gambar = dataProduk.url.toString()
        nama_produk = dataProduk.nm_produk.toString()

        Picasso.get().load(dataProduk.url).into(iv_photo_detail_package)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun buttonPopUpLanjut() {

        val dialog = Dialog(this@DetailPackageActivity)
        dialog.setContentView(R.layout.pop_up_beli)
        dialog.setCancelable(true)

        val btnHome = dialog.findViewById<Button>(R.id.btn_to_keranjang)
        val btnCheckout = dialog.findViewById<Button>(R.id.btn_to_checkout)
        val btnMin = dialog.findViewById<ImageView>(R.id.btn_min)
        val btnPlus = dialog.findViewById<ImageView>(R.id.btn_plus)
        val tvTotalBeli = dialog.findViewById<TextView>(R.id.tv_jumlah_beli)

        tvTotalBeli.text = "$totalBeli"

        btnMin.setOnClickListener {
            if (totalBeli == 1) {

                dialog.dismiss()
            } else {

                btnMin.isClickable = true
                totalBeli -= 1
                tvTotalBeli.text = "$totalBeli"
            }

        }

        btnPlus.setOnClickListener {
            totalBeli
            totalBeli += 1
            tvTotalBeli.text = "$totalBeli"

        }

        btnHome.setOnClickListener {
            if (getKategori == "produk") {
                simpanTransaksiBeli()

            } else {
                simpanTransaksiBeliStarter()
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val intent = Intent(this@DetailPackageActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
        btnCheckout.setOnClickListener {
            if (getKategori == "produk") {
                simpanTransaksiBeli()

            } else {
                simpanTransaksiBeliStarter()
            }

            val intent = Intent(
                this@DetailPackageActivity,
                CheckoutBeliActivity::class.java)
            startActivity(intent)
        }

        dialog.show()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun simpanTransaksiBeli() {

        // total harga untuk subtotal produk jasa
        val totalHargaProduk = totalBeli * hargaProduk

        val currentDate = LocalDate.now()
        dateNow = currentDate.toString()

        val transaksi = Transaksi()
        val detailTransaksi = Detail_Transaksi()

        detailTransaksi.id_produk = id_produk
        detailTransaksi.jumlah_beli = totalBeli.toString()
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
                    this@DetailPackageActivity,
                    "KOSONG",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (dataSnapshot in p0.children) {

                        val sIdTransaksi =
                            dataSnapshot.child("id_transaksi").value.toString()
                        mFirebaseDatabase.child(getUsername).child("Transaksi")
                            .child(sIdTransaksi)
                            .child("Detail_Transaksi")
                            .child(id_produk.toString())
                            .setValue(detailTransaksi)
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
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun simpanTransaksiBeliStarter() {

        // total harga untuk subtotal produk jasa
        val totalHargaProduk = totalBeli * hargaProduk

        val currentDate = LocalDate.now()
        dateNow = currentDate.toString()

        val transaksi = Transaksi()
        val detailTransaksi = Detail_Transaksi()

        detailTransaksi.id_produk = id_produk_starter
        detailTransaksi.jumlah_beli = totalBeli.toString()
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
                    this@DetailPackageActivity,
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
                            .child(sIdTransaksi!!)
                            .child("Detail_Transaksi")
                            .child(id_produk_starter.toString())
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
                        .child(id_produk_starter.toString())
                        .setValue(detailTransaksi)
                    preferences.setValues("id_transaksi",key.toString())

                }
            }
        })
    }

    companion object {
        private const val TAG = "MyActivity"
    }

}
