package com.example.kebon.detail

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kebon.R
import com.example.kebon.adapter.CheckoutAdapter
import com.example.kebon.model.Detail_Transaksi
import com.example.kebon.model.Transaksi
import com.example.kebon.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_detail_activity.*

class DetailActivityActivity : AppCompatActivity() {
    private var getUsername = ""
    private var produkList = ArrayList<Detail_Transaksi>()
    private var transaksiList = ArrayList<Transaksi>()
    private var total = ""
    private var statusBeli = ""


    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    private lateinit var mDatabase: DatabaseReference
    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_activity)

        statusBeli = intent.getStringExtra("status_beli").toString()

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mFirebaseDatabase = mFirebaseInstance.getReference("Users")
        preferences = Preferences(applicationContext)
        getUsername = preferences.getValues("username").toString()

        val getIdTransaksi = preferences.getValues("id_transaksi").toString()

        getDataDetailTransaksi()
        getDataTransaksi()

        rv_barang_activity.layoutManager = LinearLayoutManager(this)

        when (statusBeli) {
            "3" -> {
                changeBackground(btn_pesanan_diterima, R.drawable.btn_round_cream)
                btn_pesanan_diterima.isClickable = false
            }
            "4" -> {
                changeBackground(btn_pesanan_diterima, R.drawable.btn_round_orange)
                btn_pesanan_diterima.isClickable = true
                btn_pesanan_diterima.setOnClickListener {
                    queryUpdate(getIdTransaksi, "status_beli", "5")
                    finish()
                }
            }
        }

        btn_back_detail_activity.setOnClickListener {
            finish()
        }


    }

    private fun getDataTransaksi() {

        val getIdTransaksi = preferences.getValues("id_transaksi").toString()
        val mDatabaseIdTransaksi =
            mDatabase.child("Users").child(getUsername).child("Transaksi").child(getIdTransaksi)

        mDatabaseIdTransaksi.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(
                    this@DetailActivityActivity,
                    "DATA TIDAK ADA",
                    Toast.LENGTH_SHORT
                )
                    .show()

            }

            @SuppressLint("SetTextI18n")
            override fun onDataChange(p0: DataSnapshot) {

                val getNmAlamat =
                    p0.child("nm_alamat").value.toString()
                val getNmrAlamat =
                    p0.child("nmr_telp").value.toString()
                val getAlamatLengkap =
                    p0.child("alamat_lengkap").value.toString()
                val getNamaKurir = p0.child("jenis_pengiriman").value.toString()
                val getHargaKurir =
                    p0.child("subtotal_pengiriman").value.toString()
                val getSubtotalProduk =
                    p0.child("subtotal_produk_beli").value.toString()
                val getTotal = p0.child("total_biaya").value.toString()
                val getTglTransaksi = p0.child("tgl_transaksi").value.toString()
                val getStatus = p0.child("status_beli").value.toString()

                when (getStatus) {
                    "2" -> tv_status_detail_activity.text = "Status: Menunggu Pembayaran"
                    "3" -> tv_status_detail_activity.text = "Status: Sedang Dalam Perjalanan"
                    "4" -> tv_status_detail_activity.text = "Status: Produk Sudah Diterima"
                }

                tv_nama_alamat_detail_activity.text = getNmAlamat
                tv_nmr_telp_detail_activity.text = getNmrAlamat
                tv_alamat_lengkap_detail_activity.text = getAlamatLengkap
                tv_kurir_detail_activity.text = getNamaKurir
                tv_harga_ongkir_detail_activity.text = "Rp$getHargaKurir"
                tv_subtotal_produk_detail_activity.text = "Rp$getSubtotalProduk"
                tv_pengiriman_detail_activity.text = "Rp$getHargaKurir"
                tv_total_detail_activity.text = "Rp$getTotal"
                tv_tanggal_detail_activity.text = getTglTransaksi

            }

        })
    }


    private fun getDataDetailTransaksi() {

        val getIdTransaksi = preferences.getValues("id_transaksi").toString()

        // request data di detail transaksi
        val mDatabaseStarter =
            mDatabase.child("Users").child(getUsername).child("Transaksi")
                .child(getIdTransaksi).child("Detail_Transaksi")

        mDatabaseStarter.addListenerForSingleValueEvent(object :
            ValueEventListener {

            @SuppressLint("SetTextI18n")
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {

                    for (dataSnapshot in p0.children) {

                        val transaksi =
                            dataSnapshot.getValue(Detail_Transaksi::class.java)
                        produkList.add(transaksi!!)

                    }
                    rv_barang_activity.adapter = CheckoutAdapter(produkList) {

                    }
                } else {
                    Toast.makeText(
                        this@DetailActivityActivity,
                        "Data Tidak Data Yang INI",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(
                    this@DetailActivityActivity,
                    "" + p0.message,
                    Toast.LENGTH_LONG
                ).show()
            }

        })


    }

    private fun queryUpdate(id: String, node: String, value: String) {
        mDatabase.child("Users").child(getUsername)
            .child("Transaksi")
            .child(id).child(node)
            .setValue(value)

    }

    private fun changeBackground(button: Button, int: Int) {
        button.setBackgroundResource(int)
    }

}
