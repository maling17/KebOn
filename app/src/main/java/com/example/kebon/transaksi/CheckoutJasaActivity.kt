package com.example.kebon.transaksi

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kebon.PembayaranActivity
import com.example.kebon.R
import com.example.kebon.adapter.CheckoutJasaAdapter
import com.example.kebon.model.Detail_Jasa
import com.example.kebon.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_checkout_jasa.*

class CheckoutJasaActivity : AppCompatActivity() {

    private var jasaList = ArrayList<Detail_Jasa>()
    private var getUsername = ""

    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    private lateinit var mDatabase: DatabaseReference
    private lateinit var preferences: Preferences

    private var total = "0"
    private var totaljasa = "0"
    private var getIdJasa = " "
    var getDurasi = "0"
    var getPrefDurasi = "1"
    var hari = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_jasa)

        jasaList.clear()

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mFirebaseDatabase = mFirebaseInstance.getReference("Users")

        preferences = Preferences(applicationContext)
        getUsername = preferences.getValues("username").toString()

        getDataJasa()

        //BUTTON ACTION
        tv_ubah_durasi.setOnClickListener {
            buttonPopUpLanjutJasa()
        }

        btn_lanjut_detail_checkout_jasa.setOnClickListener {
            updateDataJasa()
            val ambilTotalPembayaran = tv_total_pembayaran_checkout_jasa.text
            val intent = Intent(this, PembayaranActivity::class.java)
            intent.putExtra("total_pembayaran", ambilTotalPembayaran)
            intent.putExtra("kategori_transaksi", "jasa")
            startActivity(intent)
        }

        btn_back_checkout_jasa.setOnClickListener {
            onBackPressed()
        }
        //SwipeRefresh ACTION
        srl_checkout_jasa.setOnRefreshListener {

            jasaList.clear()

            val handler = Handler()
            handler.postDelayed({

                //  mengambil data alamat
                getPrefDurasi = preferences.getValues("durasi").toString()
                tv_durasi_jasa.text = "$getDurasi Bulan"

                // ambil data - data
                getDataJasa()

                if (srl_checkout_jasa.isRefreshing) {
                    srl_checkout_jasa.isRefreshing = false
                }
            }, 2000)
        }

        //RECYCLERVIEW
        rv_produk_checkout_jasa.layoutManager = LinearLayoutManager(this)
    }

    private fun getDataJasa() {
        var getId = ""
        val mDatabaseIdTransaksi =
            mDatabase.child("Users").child(getUsername).child("Jasa")
                .orderByChild("status_jasa").equalTo("1").limitToFirst(1)

        mDatabaseIdTransaksi.addListenerForSingleValueEvent(object : ValueEventListener {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {

                    for (getdataSnapshot in p0.children) {

                        getId = getdataSnapshot.child("id_jasa").value.toString()

                        // request data di detail transaksi
                        val mDatabaseStarter =
                            mDatabase.child("Users").child(getUsername).child("Jasa")
                                .child(getId).child("Detail_Jasa")

                        mDatabaseStarter.addListenerForSingleValueEvent(object :
                            ValueEventListener {

                            @SuppressLint("SetTextI18n")
                            @RequiresApi(Build.VERSION_CODES.O)
                            override fun onDataChange(p0: DataSnapshot) {

                                if (p0.exists()) {

                                    for (dataSnapshot in p0.children) {

                                        val transaksi =
                                            dataSnapshot.getValue(Detail_Jasa::class.java)
                                        jasaList.add(transaksi!!)
                                        total =
                                            (jasaList.sumBy { it.harga_beli?.toInt()!! }).toString()
                                        totaljasa =
                                            (jasaList.sumBy { it.harga_jasa?.toInt()!! }).toString()

                                    }

                                    preferences.setValues("totalHargaProdukBeli", total)

                                    tv_harga_produk_jasa.text = "Rp$total"
                                    tv_total_produk_beli_rv_jasa.text = "Rp$total"

                                    val totalHargaJasa = totaljasa.toInt() * getDurasi.toInt()
                                    val totalSemua = total.toInt() + totalHargaJasa
                                    tv_subtotal_perawatan_jasa.text = "Rp$totalHargaJasa"
                                    tv_harga_perawatan.text = "Rp$totalHargaJasa"
                                    tv_total_checkout_jasa.text = "Rp$totalSemua"
                                    tv_total_pembayaran_checkout_jasa.text = "Rp$totalSemua"

                                    rv_produk_checkout_jasa.adapter =
                                        CheckoutJasaAdapter(jasaList) {
                                        }

                                } else {
                                    Toast.makeText(
                                        this@CheckoutJasaActivity,
                                        "Data Tidak Data Yang INI",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }

                            }

                            override fun onCancelled(p0: DatabaseError) {
                                Toast.makeText(
                                    this@CheckoutJasaActivity,
                                    "" + p0.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        })

                    }

                } else {
                    Toast.makeText(this@CheckoutJasaActivity, "Data Tidak Data", Toast.LENGTH_SHORT)
                        .show()
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@CheckoutJasaActivity, "" + p0.message, Toast.LENGTH_LONG).show()
            }

        })


    }


    @SuppressLint("SetTextI18n")
    private fun buttonPopUpLanjutJasa() {

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.pop_up_durasi)
        dialog.setCancelable(true)
        dialog.show()

        val btnSimpan = dialog.findViewById<Button>(R.id.btn_simpan_durasi)
        val spDurasi = dialog.findViewById<Spinner>(R.id.sp_durasi)

        btnSimpan.setOnClickListener {

            getDurasi = spDurasi.selectedItem.toString()
            tv_durasi_jasa.text = "$getDurasi Bulan"
            preferences.setValues("durasi", getDurasi)

            hari = getDurasi.toInt() * 30
            tv_hari_perawatan.text = "($hari Hari)"
            dialog.dismiss()

        }
    }

    private fun updateDataJasa() {

        var getId = ""
        val mDatabaseIdTransaksi =
            mDatabase.child("Users").child(getUsername).child("Jasa")
                .orderByChild("status_jasa").equalTo("1").limitToFirst(1)

        mDatabaseIdTransaksi.addListenerForSingleValueEvent(object : ValueEventListener {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {

                    for (getdataSnapshot in p0.children) {

                        getId = getdataSnapshot.child("id_jasa").value.toString()

                        // request data di detail transaksi
                        val mDatabaseStarter =
                            mDatabase.child("Users").child(getUsername).child("Jasa")
                                .child(getId).child("Detail_Jasa")

                        mDatabaseStarter.addListenerForSingleValueEvent(object :
                            ValueEventListener {

                            @SuppressLint("SetTextI18n")
                            @RequiresApi(Build.VERSION_CODES.O)
                            override fun onDataChange(p0: DataSnapshot) {

                                if (p0.exists()) {
                                    for (datasnapshot in p0.children) {

                                        getIdJasa =
                                            datasnapshot.child("id_transaksi").value.toString()

                                        val hargaPerawatan =
                                            tv_subtotal_perawatan_jasa.text.toString()

                                        val durasiPerawatan = tv_durasi_jasa.text.toString()

                                        val sTotalSemua = tv_total_checkout_jasa.text.toString()

                                        val sTotalBayarProduk =
                                            tv_harga_produk_jasa.text.toString()

                                        val totalSemua = sTotalSemua.drop(2)
                                        val totalBayarProduk = sTotalBayarProduk.drop(2)
                                        val totalBayarPerawatan = hargaPerawatan.drop(2)

                                        queryUpdate(getId, "status_jasa", "2")

                                        queryUpdate(getId, "durasi_perawatan", durasiPerawatan)

                                        queryUpdate(getId, "subtotal_perawatan", totalBayarPerawatan)

                                        queryUpdate(getId, "total_biaya_jasa", totalSemua)

                                        queryUpdate(getId, "subtotal_produk_jasa", totalBayarProduk)

                                    }
                                } else {
                                    Toast.makeText(
                                        this@CheckoutJasaActivity,
                                        "Data Tidak Data Yang INI",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }

                            }

                            override fun onCancelled(p0: DatabaseError) {
                                Toast.makeText(
                                    this@CheckoutJasaActivity,
                                    "" + p0.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        })

                    }

                } else {
                    Toast.makeText(
                        this@CheckoutJasaActivity,
                        "Data Tidak Data",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@CheckoutJasaActivity, "" + p0.message, Toast.LENGTH_LONG)
                    .show()
            }

        })

    }

    private fun queryUpdate(id: String, node: String, value: String) {
        mDatabase.child("Users").child(getUsername)
            .child("Jasa")
            .child(id).child(node)
            .setValue(value)

    }
}
