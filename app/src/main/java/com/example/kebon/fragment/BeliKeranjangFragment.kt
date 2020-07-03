package com.example.kebon.fragment

import android.annotation.SuppressLint
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
import com.example.kebon.adapter.KeranjangAdapter
import com.example.kebon.model.Detail_Transaksi
import com.example.kebon.transaksi.CheckoutBeliActivity
import com.example.kebon.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_beli_keranjang.*


class BeliKeranjangFragment : Fragment() {
    private var transaksiList = ArrayList<Detail_Transaksi>()
    private var getUsername = ""

    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    private lateinit var mDatabase: DatabaseReference
    private lateinit var preferences: Preferences
    private var total = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_beli_keranjang, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        transaksiList.clear()

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mFirebaseDatabase = mFirebaseInstance.getReference("Users")
        preferences = Preferences(activity!!.applicationContext)
        getUsername = preferences.getValues("username").toString()


        getDataTransaksi()

        btn_checkout_beli.setOnClickListener {
            val intent = Intent(context, CheckoutBeliActivity::class.java)
            startActivity(intent)
        }

        rv_beli_keranjang.layoutManager = LinearLayoutManager(context)

        srl_keranjang_beli.setOnRefreshListener {
            transaksiList.clear()
            val handler = Handler()
            handler.postDelayed({
                getDataTransaksi()
                if (srl_keranjang_beli.isRefreshing) {
                    srl_keranjang_beli.isRefreshing = false
                }
            }, 2000)
        }

    }

    private fun getDataTransaksi() {
        var getId = ""
        val mDatabaseIdTransaksi =
            mDatabase.child("Users").child(getUsername).child("Transaksi")
                .orderByChild("status_beli").equalTo("1").limitToFirst(1)

        mDatabaseIdTransaksi.addListenerForSingleValueEvent(object : ValueEventListener {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {

                    for (getdataSnapshot in p0.children) {

                        getId = getdataSnapshot.child("id_transaksi").value.toString()

                        // request data di detail transaksi
                        val mDatabaseStarter =
                            mDatabase.child("Users").child(getUsername).child("Transaksi")
                                .child(getId).child("Detail_Transaksi")

                        mDatabaseStarter.addListenerForSingleValueEvent(object :
                            ValueEventListener {

                            @SuppressLint("SetTextI18n")
                            @RequiresApi(Build.VERSION_CODES.O)
                            override fun onDataChange(p0: DataSnapshot) {

                                if (p0.exists()) {

                                    for (getdataSnapshot in p0.children) {

                                        val transaksi =
                                            getdataSnapshot.getValue(Detail_Transaksi::class.java)
                                        transaksiList.add(transaksi!!)
                                        total =
                                            (transaksiList.sumBy { it.harga_produk?.toInt()!! }).toString()
                                        Toast.makeText(
                                            context,
                                            transaksiList[0].nm_produk,
                                            Toast.LENGTH_LONG
                                        )
                                            .show()
                                    }

                                    preferences.setValues("totalHargaProdukBeli", total)
                                    tv_total_keranjang.text = "Rp$total"

                                        rv_beli_keranjang.adapter = KeranjangAdapter(transaksiList) {
                                        }

                                } else {
                                    Toast.makeText(
                                        context,
                                        "Data Tidak Data Yang INI",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }

                            }

                            override fun onCancelled(p0: DatabaseError) {
                                Toast.makeText(context, "" + p0.message, Toast.LENGTH_LONG).show()
                            }

                        })

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