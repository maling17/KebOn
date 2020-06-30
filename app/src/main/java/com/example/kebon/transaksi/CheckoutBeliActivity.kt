package com.example.kebon.transaksi

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kebon.PembayaranActivity
import com.example.kebon.R
import com.example.kebon.adapter.CheckoutAdapter
import com.example.kebon.alamat.PilihAlamatActivity
import com.example.kebon.model.Transaksi
import com.example.kebon.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_checkout_beli.*

class CheckoutBeliActivity : AppCompatActivity() {

    private var checkoutList = ArrayList<Transaksi>()
    private var getUsername = ""

    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    private lateinit var mDatabase: DatabaseReference
    private lateinit var preferences: Preferences

    private var total = ""
    private var getHargaKurir = ""
    private var getNamaKurir = ""
    private var getNamaAlamat = ""
    private var getNmrTelp = ""
    private var getAlamatLengkap = ""
    var getIdTransaksi = ""


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_beli)

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mFirebaseDatabase = mFirebaseInstance.getReference("Users")
        preferences = Preferences(applicationContext)
        getUsername = preferences.getValues("username").toString()

        //  mengambil nama kurir dan ongkos kirimnya
        getHargaKurir = preferences.getValues("HargaKurir").toString()
        getNamaKurir = preferences.getValues("NmKurir").toString()

        //  mengambil data alamat
        getNamaAlamat = preferences.getValues("nm_alamat").toString()
        getNmrTelp = preferences.getValues("nmr_telp").toString()
        getAlamatLengkap = preferences.getValues("alamat_lengkap").toString()

        // ambil data - data
        getDataTransaksi()
        getDataAlamat()
        getDataKurir()

        //  untuk merefresh data
        srl_checkout_beli.setOnRefreshListener {

            checkoutList.clear()

            val handler = Handler()
            handler.postDelayed({

                // mengambil data kurir yang disimpan di sharedprefrences
                getHargaKurir = preferences.getValues("HargaKurir").toString()
                getNamaKurir = preferences.getValues("NmKurir").toString()

                //  mengambil data alamat yang disimpan di sharedprefrences
                getNamaAlamat = preferences.getValues("nm_alamat").toString()
                getNmrTelp = preferences.getValues("nmr_telp").toString()
                getAlamatLengkap = preferences.getValues("alamat_lengkap").toString()

                // ambil data - data
                getDataTransaksi()
                getDataAlamat()
                getDataKurir()

                if (srl_checkout_beli.isRefreshing) {
                    srl_checkout_beli.isRefreshing = false
                }
            }, 2000)
        }

        // SEMUA RECYCLERVIEW
        rv_produk_checkout.layoutManager = LinearLayoutManager(this@CheckoutBeliActivity)

        // SEMUA ACTION BUTTON

        tv_ubah_pengiriman_checkout.setOnClickListener {
            val intent = Intent(this, PilihKurirActivity::class.java)
            startActivity(intent)
        }
        tv_ubah_alamat_pengiriman_checkout.setOnClickListener {
            val intent = Intent(this, PilihAlamatActivity::class.java)
            startActivity(intent)
        }

        btn_lanjut_detail_checkout.setOnClickListener {
            updateDataTransaksi()
            val ambilTotalPembayaran = tv_total_pembayaran_checkout.text
            val intent = Intent(this, PembayaranActivity::class.java)
            intent.putExtra("total_pembayaran", ambilTotalPembayaran)
            startActivity(intent)
        }


    }

    private fun getDataTransaksi() {
        val mDatabaseStarter =
            mDatabase.child("Users").child(getUsername).child("Transaksi")
                .orderByChild("status_beli").equalTo("1")

        mDatabaseStarter.addListenerForSingleValueEvent(object : ValueEventListener {

            @SuppressLint("SetTextI18n")
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {

                    for (getdataSnapshot in p0.children) {

                        val transaksi = getdataSnapshot.getValue(Transaksi::class.java)
                        checkoutList.add(transaksi!!)
                        total =
                            (checkoutList.sumBy { it.subtotal_produk_beli?.toInt()!! }).toString()
                    }

                    tv_total_harga_produk_beli.text = "Rp$total"
                    tv_total_produk_beli_rv_produk.text = "Rp$total"

                    val totalPembayaran = total.toInt() + getHargaKurir.toInt()
                    tv_total_pembayaran_checkout.text = "Rp$totalPembayaran"
                    tv_total_checkout.text = "Rp$totalPembayaran"

                    rv_produk_checkout.adapter = CheckoutAdapter(checkoutList) {

                    }
                } else {
                    Toast.makeText(this@CheckoutBeliActivity, "Data Tidak Data", Toast.LENGTH_SHORT)
                        .show()
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@CheckoutBeliActivity, "" + p0.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getDataKurir() {
        if (getNamaKurir != null) {
            tv_jenis_pengiriman.text = getNamaKurir
            tv_harga_ongkir_checkout.text = "Rp$getHargaKurir"
            tv_subtotal_pengiriman.text = "Rp$getHargaKurir"
        } else {
            Toast.makeText(this, "data belum diambil", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getDataAlamat() {
        if (getNamaAlamat != null) {
            tv_nama_detail_checkout.text = getNamaAlamat
            tv_nmr_detail_checkout.text = getNmrTelp
            tv_alamat_detail_checkout.text = getAlamatLengkap
        } else {
            Toast.makeText(this, "data alamat belum diambil", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateDataTransaksi() {

        val hargaOngkir = getHargaKurir
        val namaKurir = getNamaKurir

        val mDatabaseIdTransaksi =
            mDatabase.child("Users").child(getUsername).child("Transaksi")

        mDatabaseIdTransaksi.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@CheckoutBeliActivity, "data tidak ada", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (datasnapshot in p0.children) {

                    getIdTransaksi = datasnapshot.child("id_transaksi").value.toString()
                    val hargaBeli = datasnapshot.child("subtotal_produk_beli").value.toString()
                    val totalBayar: Int = hargaBeli.toInt() + hargaOngkir.toInt()


                    Toast.makeText(
                        this@CheckoutBeliActivity,
                        totalBayar.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                    mDatabase.child("Users").child(getUsername).child("Transaksi")
                        .child(getIdTransaksi).child("status_beli").setValue("2")
                    mDatabase.child("Users").child(getUsername).child("Transaksi")
                        .child(getIdTransaksi).child("jenis_pengiriman").setValue(namaKurir)
                    mDatabase.child("Users").child(getUsername).child("Transaksi")
                        .child(getIdTransaksi).child("subtotal_pengiriman").setValue(hargaOngkir)
                    mDatabase.child("Users").child(getUsername).child("Transaksi")
                        .child(getIdTransaksi).child("total_biaya").setValue(totalBayar.toString())
                }

            }
        })

    }

}
