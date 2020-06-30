package com.example.kebon.alamat

import android.os.Build
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kebon.R
import com.example.kebon.adapter.PilihAlamatAdapter
import com.example.kebon.model.Alamat
import com.example.kebon.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_pilih_alamat.*

class PilihAlamatActivity : AppCompatActivity() {
    private var alamatList = ArrayList<Alamat>()
    private var getUsername = ""

    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    private lateinit var mDatabase: DatabaseReference
    private lateinit var preferences: Preferences

    private var namaAlamat: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_alamat)

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mFirebaseDatabase = mFirebaseInstance.getReference("Users")
        preferences = Preferences(applicationContext)
        getUsername = preferences.getValues("username").toString()

        rv_pilih_alamat.layoutManager = LinearLayoutManager(this)

        getDataAlamat()

        btn__simpan_pilih_alamat.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getDataAlamat() {

        val mDatabaseStarter =
            mDatabase.child("Users").child(getUsername).child("alamat")

        mDatabaseStarter.addListenerForSingleValueEvent(object : ValueEventListener {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {

                    for (getdataSnapshot in p0.children) {

                        val alamat = getdataSnapshot.getValue(Alamat::class.java)
                        namaAlamat = getdataSnapshot.child("nm_alamat").value.toString()
                        alamatList.add(alamat!!)

                    }

                    rv_pilih_alamat.adapter = PilihAlamatAdapter(alamatList) {

                        val indexArrayList =
                            alamatList.indexOf(it) //mengindex dulu isi yang ada di array

                        val getNamaAlamat =
                            alamatList[indexArrayList].nm_alamat.toString() //mengfilter dan mengambil value dari nm_alamat di tabel alamat

                        val getNomorTelp =
                            alamatList[indexArrayList].nmr_telp.toString() //mengfilter dan mengambil value dari nmr_telp di tabel alamat

                        val getAlamatLengkap =
                            alamatList[indexArrayList].alamat_lengkap.toString() //mengfilter dan mengambil value dari alamat_lengkap di tabel alamat

                        preferences.setValues("nm_alamat",getNamaAlamat)
                        preferences.setValues("nmr_telp",getNomorTelp)
                        preferences.setValues("alamat_lengkap",getAlamatLengkap)

                    }

                } else {
                    Toast.makeText(this@PilihAlamatActivity, "Data Tidak Data", Toast.LENGTH_SHORT)
                        .show()
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@PilihAlamatActivity, "" + p0.message, Toast.LENGTH_LONG).show()
            }

        })
    }

}