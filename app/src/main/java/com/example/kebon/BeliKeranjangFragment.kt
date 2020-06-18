package com.example.kebon

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kebon.adapter.KeranjangAdapter
import com.example.kebon.model.Transaksi
import com.example.kebon.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_beli_keranjang.*
import kotlinx.android.synthetic.main.pop_up_beli.*

class BeliKeranjangFragment : Fragment() {
    private var transaksiList = ArrayList<Transaksi>()
    private var getUsername = ""

    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    private lateinit var mDatabase: DatabaseReference
    private lateinit var preferences: Preferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_beli_keranjang, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mFirebaseDatabase = mFirebaseInstance.getReference("Users")
        preferences = Preferences(activity!!.applicationContext)
        getUsername = preferences.getValues("username").toString()
        getDataTransaksi()

        rv_beli_keranjang.layoutManager=LinearLayoutManager(context)

    }

    private fun getDataTransaksi() {
        val mDatabaseStarter =
            mDatabase.child("Users").child(getUsername).child("Transaksi")

        mDatabaseStarter.addListenerForSingleValueEvent(object : ValueEventListener {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {

                    for (getdataSnapshot in p0.children) {

                        val transaksi = getdataSnapshot.getValue(Transaksi::class.java)
                        transaksiList.add(transaksi!!)
                        val total= (transaksiList.sumBy { it.subtotal_produk_beli?.toInt()!! }).toString()
                        tv_total_keranjang.text="Rp$total"

                    }

                    rv_beli_keranjang.adapter = KeranjangAdapter(transaksiList){



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