package com.example.kebon.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kebon.R
import com.example.kebon.model.Perkembangan_Tanaman
import com.example.kebon.utils.Preferences
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_jasa.*

class DetailJasaActivity : AppCompatActivity() {
    private lateinit var preferences: Preferences
    private lateinit var mDatabase: DatabaseReference
    private var perkembanganList = ArrayList<Perkembangan_Tanaman>()
    private var getUsername = " "
    private var getNamaTanaman = " "
    private var getStatus = " "
    private var getDeskripsi = " "
    private var getTglUpdate = " "
    private var getTglNow = " "
    private var getEstimasiPanen = " "
    private var getUrlPhoto = " "

    private var idPerkembangan = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_jasa)
        perkembanganList.clear()
        preferences = Preferences(this)
        mDatabase = FirebaseDatabase.getInstance().reference
        getUsername = preferences.getValues("username").toString()
        idPerkembangan = intent.getStringExtra("id_perkembangan").toString()

        getDataListJasa()
    }

    private fun getDataListJasa() {

        val mDatabasePerkembangan =
            mDatabase.child("Users").child(getUsername).child("Perkembangan_Tanaman")
                .orderByChild("id_perkembangan").equalTo(idPerkembangan)

        mDatabasePerkembangan.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(
                    this@DetailJasaActivity,
                    "DATA TIDAK ADA",
                    Toast.LENGTH_SHORT
                )
                    .show()

            }

            @SuppressLint("SetTextI18n")
            override fun onDataChange(p0: DataSnapshot) {

                for (dataSnapshot in p0.children) {

                    getNamaTanaman = dataSnapshot.child("nm_tanaman").value.toString()
                    getDeskripsi = dataSnapshot.child("deskripsi_tanaman").value.toString()
                    getEstimasiPanen = dataSnapshot.child("estimasi_panen").value.toString()
                    getStatus = dataSnapshot.child("status_perkembangan").value.toString()
                    getTglNow = dataSnapshot.child("tgl_mulai").value.toString()
                    getTglUpdate = dataSnapshot.child("tgl_update").value.toString()
                    getUrlPhoto = dataSnapshot.child("url_perkembangan").value.toString()
                }

                tv_nama_detail_jasa.text = getNamaTanaman
                tv_status_detail_jasa.text = getStatus
                tv_estimasi_panen_detail_jasa.text = getEstimasiPanen
                tv_tgl_update_detail_jasa.text = getTglUpdate
                tv_tgl_update_detail_jasa_foto.text = getTglUpdate
                tv_deskripsi_detail_jasa.text = getDeskripsi
                tv_dirawat_sejak_detail_jasa.text = "Dirawat Sejak $getTglNow"
                Picasso.get().load(getUrlPhoto).into(iv_photo_detail_jasa)


            }


        })
    }
}
