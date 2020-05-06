package com.example.kebon.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kebon.R
import com.example.kebon.adapter.ListStarterpackAdapter
import com.example.kebon.detail.DetailPackageActivity
import com.example.kebon.model.StarterProduk
import com.example.kebon.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_starter_pack.*


class StarterPackActivity : AppCompatActivity() {

    private lateinit var preferences: Preferences
    private lateinit var database: DatabaseReference
    private var dataList = ArrayList<StarterProduk>()
    var produk2: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starter_pack)
        preferences = Preferences(applicationContext)
        database = FirebaseDatabase.getInstance().reference

        getDataStarterPack()

        rv_list_starter_pack.layoutManager = GridLayoutManager(this@StarterPackActivity, 2)

        iv_back_starter.setOnClickListener {
            finish()
        }
    }

    private fun getDataStarterPack() {
        val mDatabaseStarter =
            database.child("Produk").orderByChild("kategori").equalTo("Starterpack")
        mDatabaseStarter.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@StarterPackActivity, "" + p0.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    dataList.clear()
                    for (getdataSnapshot in p0.children) {
                        val produk = getdataSnapshot.getValue(StarterProduk::class.java)
                        dataList.add(produk!!)
                    }
                }
                rv_list_starter_pack.adapter = ListStarterpackAdapter(dataList) {
                    produk2 = "starter"

                    val intent =
                        Intent(
                            this@StarterPackActivity,
                            DetailPackageActivity::class.java
                        ).putExtra("data", it)
                            .putExtra("produk", produk2)
                    startActivity(intent)
                }
            }

        })
    }
}
