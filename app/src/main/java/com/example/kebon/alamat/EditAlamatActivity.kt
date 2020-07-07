package com.example.kebon.alamat

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kebon.R
import com.example.kebon.model.Alamat
import com.example.kebon.utils.Preferences
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_edit_alamat.*

class EditAlamatActivity : AppCompatActivity() {

    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    private lateinit var mDatabase: DatabaseReference
    private lateinit var preferences: Preferences

    private var getUsername = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_alamat)

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mFirebaseDatabase = mFirebaseInstance.getReference("Users")
        preferences = Preferences(applicationContext)
        getUsername = preferences.getValues("username").toString()

        btn_simpan_edit_alamat.setOnClickListener {
            simpanAlamat()
        }
        btn_back_alamat.setOnClickListener {
            finish()
        }


    }

    private fun simpanAlamat() {


        val etNama = et_nama_edit_profile.text.toString()
        val etNmr = et_nmr_tlp_edit_alamat.text.toString()
        val etKota = et_kota_edit_alamat.text.toString()
        val etKecamatan = et_kecamatan_edit_alamat.text.toString()
        val etAlamatLengkap = et_alamat_edit_alamat.text.toString()
        val etProvinsi = et_provinsi_edit_alamat.text.toString()
        val etKodePos = et_kodePos_edit_alamat.text.toString()
        val key = mFirebaseDatabase.child(getUsername).child("alamat").push().key
        val alamat = Alamat()
        alamat.alamat_lengkap = etAlamatLengkap
        alamat.kecamatan = etKecamatan
        alamat.kabupaten = etKota
        alamat.nm_alamat = etNama
        alamat.nmr_telp = etNmr
        alamat.kode_pos = etKodePos
        alamat.provinsi=etProvinsi
        alamat.status="1"
        alamat.id_alamat=key.toString()

        mFirebaseDatabase.child(getUsername).child("alamat").child(key.toString()).setValue(alamat)

    }

}

