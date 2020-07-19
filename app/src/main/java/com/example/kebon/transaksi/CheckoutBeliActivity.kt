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
import com.example.kebon.model.Detail_Transaksi
import com.example.kebon.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_checkout_beli.*

class CheckoutBeliActivity : AppCompatActivity() {

    private var checkoutList = ArrayList<Detail_Transaksi>()
    private var getUsername = ""

    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    private lateinit var mDatabase: DatabaseReference
    private lateinit var preferences: Preferences

    private var total = "0"
    private var getHargaKurir = "0"
    private var getNamaKurir = " "
    private var getNamaAlamat = " "
    private var getNmrTelp = " "
    private var getAlamatLengkap = " "
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
            intent.putExtra("kategori_transaksi", "beli")
            startActivity(intent)
        }

    }

    private fun getDataTransaksi() {
        var getId = ""
        val mDatabaseIdTransaksi =
            mDatabase.child("Users").child(getUsername).child("Transaksi")
                .orderByChild("status_beli").equalTo("1").limitToFirst(1)

        mDatabaseIdTransaksi.addListenerForSingleValueEvent(object : ValueEventListener {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {

                    for (getdataSnapshot in p0.children) {

                        getId = getdataSnapshot.child("id_transaksi").value.toString()

                        // request data di detail transaksi
                        val mDatabaseStarter =
                            mDatabase.child("Users").child(getUsername).child("Transaksi")
                                .child(getId).child("Detail_Transaksi")

                        mDatabaseStarter.addListenerForSingleValueEvent(object :
                            ValueEventListener {

                            @SuppressLint("SetTextI18n")
                            @RequiresApi(Build.VERSION_CODES.O)
                            override fun onDataChange(p0: DataSnapshot) {

                                if (p0.exists()) {

                                    for (getdataSnapshot in p0.children) {

                                        val transaksi =
                                            getdataSnapshot.getValue(Detail_Transaksi::class.java)
                                        checkoutList.add(transaksi!!)
                                        total =
                                            (checkoutList.sumBy { it.harga_beli?.toInt()!! }).toString()

                                    }
                                    tv_total_harga_produk_beli.text = "Rp$total"
                                    tv_total_produk_beli_rv_produk.text = "Rp$total"


                                    val totalPembayaran = total.toInt() + getHargaKurir.toInt()

                                    tv_total_pembayaran_checkout.text = "Rp$totalPembayaran"

                                    tv_total_checkout.text = "Rp$totalPembayaran"

                                    preferences.setValues("totalHargaProdukBeli", total)
                                    tv_total_harga_produk_beli.text = "Rp$total"

                                    rv_produk_checkout.adapter = CheckoutAdapter(checkoutList) {
                                    }

                                } else {
                                    Toast.makeText(
                                        this@CheckoutBeliActivity,
                                        "Data Tidak Data Yang INI",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }

                            override fun onCancelled(p0: DatabaseError) {
                                Toast.makeText(
                                    this@CheckoutBeliActivity,
                                    "" + p0.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        })

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
            tv_nama_detail_actibity.text = getNamaAlamat
            tv_nmr_telp_detail_activity.text = getNmrTelp
            tv_alamat_lengkap_detail_activity.text = getAlamatLengkap
        } else {
            Toast.makeText(this, "data alamat belum diambil", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateDataTransaksi() {

        val hargaOngkir = getHargaKurir
        val namaKurir = getNamaKurir
        var getId = ""
        val mDatabaseIdTransaksi =
            mDatabase.child("Users").child(getUsername).child("Transaksi")
                .orderByChild("status_beli").equalTo("1").limitToFirst(1)

        mDatabaseIdTransaksi.addListenerForSingleValueEvent(object : ValueEventListener {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {

                    for (getdataSnapshot in p0.children) {

                        getId = getdataSnapshot.child("id_transaksi").value.toString()

                        // request data di detail transaksi
                        val mDatabaseStarter =
                            mDatabase.child("Users").child(getUsername).child("Transaksi")
                                .child(getId).child("Detail_Transaksi")

                        mDatabaseStarter.addListenerForSingleValueEvent(object :
                            ValueEventListener {

                            @SuppressLint("SetTextI18n")
                            @RequiresApi(Build.VERSION_CODES.O)
                            override fun onDataChange(p0: DataSnapshot) {

                                if (p0.exists()) {
                                    for (datasnapshot in p0.children) {

                                        getIdTransaksi =
                                            datasnapshot.child("id_transaksi").value.toString()

                                        val getIdProduk =
                                            datasnapshot.child("id_produk").value.toString()

                                        val hargaBeli = datasnapshot
                                            .child("harga_produk").value.toString()


                                        val sTotalSemua = tv_total_checkout.text.toString()
                                        val sTotalBayarProduk =
                                            tv_total_harga_produk_beli.text.toString()

                                        val totalSemua = sTotalSemua.drop(2)
                                        val totalBayarProduk = sTotalBayarProduk.drop(2)


                                        queryUpdate(getId, "status_beli", "2")

                                        queryUpdate(getId, "jenis_pengiriman", namaKurir)

                                        queryUpdate(getId, "subtotal_pengiriman", hargaOngkir)

                                        queryUpdate(getId, "total_biaya", totalSemua)

                                        queryUpdate(getId, "subtotal_produk_beli", totalBayarProduk)

                                        queryUpdate(getId, "nm_alamat", getNamaAlamat)

                                        queryUpdate(getId, "nmr_telp", getNmrTelp)

                                        queryUpdate(getId, "alamat_lengkap", getAlamatLengkap)
                                    }
                                } else {
                                    Toast.makeText(
                                        this@CheckoutBeliActivity,
                                        "Data Tidak Data Yang INI",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }

                            }

                            override fun onCancelled(p0: DatabaseError) {
                                Toast.makeText(
                                    this@CheckoutBeliActivity,
                                    "" + p0.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        })

                    }

                } else {
                    Toast.makeText(
                        this@CheckoutBeliActivity,
                        "Data Tidak Data",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@CheckoutBeliActivity, "" + p0.message, Toast.LENGTH_LONG)
                    .show()
            }

        })

    }

    private fun queryUpdate(id: String, node: String, value: String) {
        mDatabase.child("Users").child(getUsername)
            .child("Transaksi")
            .child(id).child(node)
            .setValue(value)

    }
}
