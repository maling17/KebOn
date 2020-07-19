package com.example.kebon.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kebon.R
import com.example.kebon.adapter.ListProdukAdapter
import com.example.kebon.detail.DetailAlatActivity
import com.example.kebon.detail.DetailBenihActivity
import com.example.kebon.detail.DetailPackageActivity
import com.example.kebon.detail.DetailTanamanActivity
import com.example.kebon.model.Produk
import com.example.kebon.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_produk_populer.*
import java.util.*

class ProdukPopulerActivity : AppCompatActivity() {

    private lateinit var preferences: Preferences
    private lateinit var database: DatabaseReference
    private var dataList = ArrayList<Produk>()
    var produk2: String? = ""
    var kategori="netral"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produk_populer)

        preferences = Preferences(applicationContext)
        database = FirebaseDatabase.getInstance().reference

        kategori=intent.getStringExtra("Jasa").toString()

        when(kategori){
            "netral"->getData()
            "Jasa"->getDataTanaman()
        }

        rv_list_produk_populer.layoutManager=GridLayoutManager(this@ProdukPopulerActivity,2)

        iv_back_produk_populer.setOnClickListener {
            finish()
        }
    }

    private fun getData() {
        val mDatabase = database.child("Produk")
        mDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@ProdukPopulerActivity, p0.message, Toast.LENGTH_LONG).show()

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    dataList.clear()
                    for (getDataSnapshot in p0.children) {
                        val produk = getDataSnapshot.getValue(Produk::class.java)
                        dataList.add(produk!!)
                    }
                }
                rv_list_produk_populer.adapter = ListProdukAdapter(dataList) {
                    produk2 = "produk"
                    val indexArrayList = dataList.indexOf(it) //mengindex dulu isi yang ada di array
                    val filter =
                        dataList[indexArrayList].kategori //mengfilter dan mengambil value dari kategori

                    if (filter == "Alat") {
                        val intent =
                            Intent(
                                this@ProdukPopulerActivity,
                                DetailAlatActivity::class.java
                            ).putExtra("data", it)
                                .putExtra("produk", produk2)
                        startActivity(intent)
                    }
                    if (filter == "Tanaman") {
                        val intent =
                            Intent(this@ProdukPopulerActivity, DetailTanamanActivity::class.java)
                                .putExtra("data", it)
                                .putExtra("produk", produk2)
                        startActivity(intent)
                    }
                    if (filter == "Bibit") {
                        val intent =
                            Intent(this@ProdukPopulerActivity, DetailBenihActivity::class.java)
                                .putExtra("produk", produk2)
                                .putExtra("data", it)
                        startActivity(intent)
                    }
                    if (filter == "Pupuk") {
                        val intent =
                            Intent(
                                this@ProdukPopulerActivity,
                                DetailAlatActivity::class.java
                            ).putExtra("data", it)
                                .putExtra("produk", produk2)
                        startActivity(intent)
                    }
                    if (filter == "Starterpack") {
                        val intent =
                            Intent(
                                this@ProdukPopulerActivity,
                                DetailPackageActivity::class.java
                            ).putExtra("data", it)
                                .putExtra("produk", produk2)
                        startActivity(intent)
                    }

                }
            }

        })

    }
    private fun getDataTanaman() {
        val mDatabase = database.child("Produk").orderByChild("kategori").equalTo("Tanaman")
        mDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@ProdukPopulerActivity, p0.message, Toast.LENGTH_LONG).show()

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    dataList.clear()
                    for (getDataSnapshot in p0.children) {
                        val produk = getDataSnapshot.getValue(Produk::class.java)
                        dataList.add(produk!!)
                    }
                }
                rv_list_produk_populer.adapter = ListProdukAdapter(dataList) {
                    produk2 = "produk"
                    val indexArrayList = dataList.indexOf(it) //mengindex dulu isi yang ada di array
                    val filter =
                        dataList[indexArrayList].kategori //mengfilter dan mengambil value dari kategori

                    if (filter == "Alat") {
                        val intent =
                            Intent(
                                this@ProdukPopulerActivity,
                                DetailAlatActivity::class.java
                            ).putExtra("data", it)
                                .putExtra("produk", produk2)
                        startActivity(intent)
                    }
                    if (filter == "Tanaman") {
                        val intent =
                            Intent(this@ProdukPopulerActivity, DetailTanamanActivity::class.java)
                                .putExtra("data", it)
                                .putExtra("produk", produk2)
                        startActivity(intent)
                    }
                    if (filter == "Bibit") {
                        val intent =
                            Intent(this@ProdukPopulerActivity, DetailBenihActivity::class.java)
                                .putExtra("produk", produk2)
                                .putExtra("data", it)
                        startActivity(intent)
                    }
                    if (filter == "Pupuk") {
                        val intent =
                            Intent(
                                this@ProdukPopulerActivity,
                                DetailAlatActivity::class.java
                            ).putExtra("data", it)
                                .putExtra("produk", produk2)
                        startActivity(intent)
                    }
                    if (filter == "Starterpack") {
                        val intent =
                            Intent(
                                this@ProdukPopulerActivity,
                                DetailPackageActivity::class.java
                            ).putExtra("data", it)
                                .putExtra("produk", produk2)
                        startActivity(intent)
                    }

                }
            }

        })

    }
}
