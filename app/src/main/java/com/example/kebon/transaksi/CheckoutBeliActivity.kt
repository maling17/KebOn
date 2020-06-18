package com.example.kebon.transaksi

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kebon.R
import com.example.kebon.adapter.ProdukAdapter
import com.example.kebon.alamat.DaftarAlamatActivity
import com.example.kebon.model.Alamat
import com.example.kebon.model.Checkout
import com.example.kebon.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_checkout_beli.*
import java.util.*

class CheckoutBeliActivity : AppCompatActivity() {

    private lateinit var mDatabase: DatabaseReference
    private lateinit var preferences: Preferences
    private var dataList = ArrayList<Alamat>()
    private var dataListCheckout = ArrayList<Checkout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_beli)

        mDatabase = FirebaseDatabase.getInstance().reference
        preferences = Preferences(applicationContext)
        getDataAlamat()
        rv_produk_checkout.layoutManager=LinearLayoutManager(this@CheckoutBeliActivity)
//        rv_produk_checkout.adapter=ProdukAdapter(dataListCheckout)


        tv_ubah_alamat_pengiriman_checkout.setOnClickListener {
            val intent=Intent(this@CheckoutBeliActivity,DaftarAlamatActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getDataAlamat() {
        val username: String = preferences.getValues("username_home").toString()

        tv_nama_detail_checkout.text = username

        val databaseAlamat =
            mDatabase.child("Users").child(username).child("alamat").orderByChild("status").equalTo("1")

        databaseAlamat.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@CheckoutBeliActivity, "" + p0.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (getdataSnapshot in p0.children) {

                    val alamat = getdataSnapshot.getValue(Alamat::class.java)
                    dataList.add(alamat!!)

                }

                tv_nmr_detail_checkout.text=dataList[0].nmr_telp
                tv_alamat_detail_checkout.text=dataList[0].alamat_lengkap

            }

        })

    }
    private fun getDataCheckout(){

        dataListCheckout = intent.getSerializableExtra("data") as ArrayList<Checkout>

        for (a in dataList.indices){

        }



    }
}
