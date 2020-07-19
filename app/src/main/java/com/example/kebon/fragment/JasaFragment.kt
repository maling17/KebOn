package com.example.kebon.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kebon.R
import com.example.kebon.adapter.ListPerkembanganTanamanAdapter
import com.example.kebon.home.ProdukPopulerActivity
import com.example.kebon.model.Perkembangan_Tanaman
import com.example.kebon.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_jasa.*


class JasaFragment : Fragment() {
    private lateinit var preferences: Preferences
    private lateinit var mDatabase: DatabaseReference
    private var perkembanganList = ArrayList<Perkembangan_Tanaman>()
    var nama = ""
    private var getUsername = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_jasa, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        perkembanganList.clear()
        preferences = Preferences(activity!!.applicationContext)
        mDatabase = FirebaseDatabase.getInstance().reference

        btn_tambah_jasa.setOnClickListener {
            val intent = Intent(context, ProdukPopulerActivity::class.java)
            intent.putExtra("Jasa", "Jasa")
            startActivity(intent)
        }

        getUsername = preferences.getValues("username").toString()
        getDataListJasa()
        rv_jasa_perkembangan.layoutManager = LinearLayoutManager(context)

    }

    private fun getDataListJasa() {

        val mDatabasePerkembangan =
            mDatabase.child("Users").child(getUsername).child("Perkembangan_Tanaman")

        mDatabasePerkembangan.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(
                    context,
                    "DATA TIDAK ADA",
                    Toast.LENGTH_SHORT
                )
                    .show()

            }

            override fun onDataChange(p0: DataSnapshot) {

                for (getdataSnapshot in p0.children) {
                    val perkembanganTanaman =
                        getdataSnapshot.getValue(Perkembangan_Tanaman::class.java)
                    perkembanganList.add(perkembanganTanaman!!)

                }

                rv_jasa_perkembangan.adapter = ListPerkembanganTanamanAdapter(perkembanganList) {

                }


            }


        })
    }

}
