package com.example.kebon.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kebon.R
import com.example.kebon.adapter.ArtikelAdapter
import com.example.kebon.detail.DetailArtikelActivity
import com.example.kebon.model.Artikel
import com.example.kebon.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_tips.*
import kotlinx.android.synthetic.main.fragment_home.*

class TipsActivity : AppCompatActivity() {
    private lateinit var mDatabase: DatabaseReference
    private var dataListArtikel = ArrayList<Artikel>()
    private lateinit var preferences: Preferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tips)

        preferences = Preferences(applicationContext)
        mDatabase = FirebaseDatabase.getInstance().reference

        getDataArtikel()
        rv_list_tips_berkebun.layoutManager=LinearLayoutManager(this@TipsActivity)
        iv_back_tips.setOnClickListener {
            finish()
        }
    }

    private fun getDataArtikel() {
        val mDatabaseStarter =
            mDatabase.child("Artikel")

        mDatabaseStarter.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {

                    for (getdataSnapshot in p0.children) {

                        val artikel = getdataSnapshot.getValue(Artikel::class.java)
                        dataListArtikel.add(artikel!!)

                    }
                }

                rv_list_tips_berkebun.adapter = ArtikelAdapter(dataListArtikel) {

                    val intent =
                        Intent(this@TipsActivity, DetailArtikelActivity::class.java).putExtra("data", it)

                    startActivity(intent)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@TipsActivity, "" + p0.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}
