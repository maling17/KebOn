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
import com.example.kebon.model.Jasa
import com.example.kebon.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_checkout_jasa.*
import kotlinx.android.synthetic.main.fragment_jasa_keranjang.*

class CheckoutJasaActivity : AppCompatActivity() {

    private var jasaList = ArrayList<Jasa>()
    private var getUsername = ""

    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    private lateinit var mDatabase: DatabaseReference
    private lateinit var preferences: Preferences

    private var total = ""
    private var totaljasa = ""
    private var getIdJasa = ""
    var getDurasi = ""
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

        //  mengambil data alamat
        getPrefDurasi = preferences.getValues("durasi").toString()
        getDurasi = getPrefDurasi

        getDataJasa()

        // TEXTVIEW ACTION
        hari = getDurasi.toInt() * 30
        tv_durasi_jasa.text = "$getDurasi Bulan"
        tv_hari_perawatan.text = "($hari Hari)"

        //BUTTON ACTION
        tv_ubah_durasi.setOnClickListener {
            buttonPopUpLanjutJasa()
        }
        btn_lanjut_detail_checkout_jasa.setOnClickListener {
            updateDataJasa()
            val ambilTotalPembayaran = tv_total_pembayaran_checkout_jasa.text
            val intent = Intent(this, PembayaranActivity::class.java)
            intent.putExtra("total_pembayaran", ambilTotalPembayaran)
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
        jasaList.clear()
        val mDatabaseStarter =
            mDatabase.child("Users").child(getUsername).child("Jasa")
                .orderByChild("status_jasa").equalTo("1")

        mDatabaseStarter.addListenerForSingleValueEvent(object : ValueEventListener {

            @SuppressLint("SetTextI18n")
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {

                    for (getdataSnapshot in p0.children) {

                        val jasa = getdataSnapshot.getValue(Jasa::class.java)
                        jasaList.add(jasa!!)
                        total =
                            (jasaList.sumBy { it.subtotal_produk_jasa?.toInt()!! }).toString()
                        totaljasa =
                            (jasaList.sumBy { it.subtotal_perawatan?.toInt()!! }).toString()
                    }

                    tv_harga_produk_jasa.text = "Rp$total"
                    tv_total_produk_beli_rv_jasa.text = "Rp$total"

                    val totalHargaJasa = totaljasa.toInt() * getDurasi.toInt()
                    val totalSemua = total.toInt() + totalHargaJasa
                    tv_subtotal_perawatan_jasa.text = "Rp$totalHargaJasa"
                    tv_harga_perawatan.text = "Rp$totalHargaJasa"
                    tv_total_checkout_jasa.text = "Rp$totalSemua"
                    tv_total_pembayaran_checkout_jasa.text = "Rp$totalSemua"

                    rv_produk_checkout_jasa.adapter = CheckoutJasaAdapter(jasaList) {

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

        val ambilDurasi = getDurasi

        val mDatabaseIdTransaksi =
            mDatabase.child("Users").child(getUsername).child("Jasa")

        mDatabaseIdTransaksi.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@CheckoutJasaActivity, "data tidak ada", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (datasnapshot in p0.children) {

                    getIdJasa = datasnapshot.child("id_jasa").value.toString()
                    val hargaJasa = datasnapshot.child("subtotal_perawatan").value.toString()
                    val hargaBeli = datasnapshot.child("subtotal_produk_jasa").value.toString()
                    val totalhargaJasa: Int = hargaJasa.toInt() * ambilDurasi.toInt()
                    val totalSemua = totalhargaJasa + hargaBeli.toInt()

                    mDatabase.child("Users").child(getUsername).child("Jasa")
                        .child(getIdJasa).child("status_jasa").setValue("2")
                    mDatabase.child("Users").child(getUsername).child("Jasa")
                        .child(getIdJasa).child("durasi_perawatan").setValue(ambilDurasi)
                    mDatabase.child("Users").child(getUsername).child("Jasa")
                        .child(getIdJasa).child("subtotal_perawatan")
                        .setValue(totalhargaJasa.toString())
                    mDatabase.child("Users").child(getUsername).child("Jasa")
                        .child(getIdJasa).child("total_biaya_jasa").setValue(totalSemua.toString())
                }

            }
        })

    }
}
