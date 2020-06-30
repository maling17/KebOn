package com.example.kebon.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kebon.R
import com.example.kebon.adapter.KeranjangJasaAdapter
import com.example.kebon.model.Jasa
import com.example.kebon.transaksi.CheckoutJasaActivity
import com.example.kebon.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_checkout_jasa.*
import kotlinx.android.synthetic.main.activity_keranjang.*
import kotlinx.android.synthetic.main.fragment_jasa_keranjang.*


class JasaKeranjangFragment : Fragment() {
    private var jasaList = ArrayList<Jasa>()
    private var getUsername = ""

    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    private lateinit var mDatabase: DatabaseReference
    private lateinit var preferences: Preferences
    private var total = ""
    private var idJasa = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_jasa_keranjang, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        jasaList.clear()

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mFirebaseDatabase = mFirebaseInstance.getReference("Users")
        preferences = Preferences(activity!!.applicationContext)
        getUsername = preferences.getValues("username").toString()
        getDatajasa()

        rv_jasa_keranjang.layoutManager = LinearLayoutManager(context)
        srl_keranjang_jasa.setOnRefreshListener {
            jasaList.clear()
            val handler = Handler()
            handler.postDelayed({
                getDatajasa()
                if (srl_keranjang_jasa.isRefreshing) {
                    srl_keranjang_jasa.isRefreshing = false
                }
            }, 2000)
        }

        btn_checkout_jasa.setOnClickListener {
            val intent = Intent(context, CheckoutJasaActivity::class.java)
            startActivity(intent)
        }

    }

    private fun getDatajasa() {

        val mDatabaseStarter =
            mDatabase.child("Users").child(getUsername).child("Jasa")
                .orderByChild("status_jasa").equalTo("1")

        mDatabaseStarter.addListenerForSingleValueEvent(object : ValueEventListener {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {

                    for (getdataSnapshot in p0.children) {

                        val jasa = getdataSnapshot.getValue(Jasa::class.java)
                        jasaList.add(jasa!!)
                        total = (jasaList.sumBy { it.subtotal_produk_jasa?.toInt()!! }).toString()

                    }

                    tv_total_keranjang_jasa.text = "Rp$total"

                    rv_jasa_keranjang.adapter = KeranjangJasaAdapter(jasaList) {

                    }
                } else {
                    Toast.makeText(context, "Data Tidak Data", Toast.LENGTH_SHORT)
                        .show()
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, "" + p0.message, Toast.LENGTH_LONG).show()
            }
        })
    }

}