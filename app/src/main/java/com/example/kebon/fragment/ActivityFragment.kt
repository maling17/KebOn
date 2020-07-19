package com.example.kebon.fragment

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kebon.R
import com.example.kebon.adapter.ListActivityAdapter
import com.example.kebon.adapter.ListJasaActivityAdapter
import com.example.kebon.model.Jasa
import com.example.kebon.model.Transaksi
import com.example.kebon.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_keranjang.*
import kotlinx.android.synthetic.main.fragment_activity.*

/**
 * A simple [Fragment] subclass.
 */
class ActivityFragment : Fragment() {
    private lateinit var preferences: Preferences
    private lateinit var mDatabase: DatabaseReference
    private var transaksiList = ArrayList<Transaksi>()
    private var jasaList = ArrayList<Jasa>()
    private var getUsername = ""

    var produk2: String? = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        transaksiList.clear()
        preferences = Preferences(activity!!.applicationContext)
        mDatabase = FirebaseDatabase.getInstance().reference

        getUsername = preferences.getValues("username").toString()
        getDataTransaksi()

        btn_beli_aktivitas.setOnClickListener {
            changeBackground(btn_beli_aktivitas, R.drawable.left_rounded_button_orange)
            changeBackground(btn_jasa_aktivitas, R.drawable.right_rounded_button_cream)
            jasaList.clear()
            getDataTransaksi()
            rv_aktivitas.layoutManager = LinearLayoutManager(context)
            rv_jasa_list_aktivitas.visibility=View.INVISIBLE
            rv_aktivitas.visibility=View.VISIBLE

        }

        btn_jasa_aktivitas.setOnClickListener {
            changeBackground(btn_beli_aktivitas, R.drawable.left_rounded_button_cream)
            changeBackground(btn_jasa_aktivitas, R.drawable.right_rounded_button_orange)
            transaksiList.clear()
            getDataJasa()
            rv_jasa_list_aktivitas.layoutManager = LinearLayoutManager(context)
            rv_jasa_list_aktivitas.visibility=View.VISIBLE
            rv_aktivitas.visibility=View.INVISIBLE
        }

        //  untuk merefresh data
        srl_list_activity.setOnRefreshListener {

            transaksiList.clear()

            val handler = Handler()
            handler.postDelayed({

                // ambil data - data
                getDataTransaksi()

                if (srl_list_activity.isRefreshing) {
                    srl_list_activity.isRefreshing = false
                }
            }, 2000)
        }


        rv_aktivitas.layoutManager = LinearLayoutManager(context)
        rv_jasa_list_aktivitas.layoutManager = LinearLayoutManager(context)

    }

    private fun getDataTransaksi() {

        val mDatabaseIdTransaksi =
            mDatabase.child("Users").child(getUsername).child("Transaksi")

        mDatabaseIdTransaksi.addListenerForSingleValueEvent(object :
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
                    val transaksi = getdataSnapshot.getValue(Transaksi::class.java)
                    transaksiList.add(transaksi!!)

                }
                Log.d(TAG, "onDataChangeActivity: ${transaksiList[0].total_biaya}")
                rv_aktivitas.adapter = ListActivityAdapter(transaksiList) {

                }


            }


        })
    }
    private fun getDataJasa() {

        val mDatabaseIdTransaksi =
            mDatabase.child("Users").child(getUsername).child("Jasa")

        mDatabaseIdTransaksi.addListenerForSingleValueEvent(object :
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
                    val jasa = getdataSnapshot.getValue(Jasa::class.java)
                    jasaList.add(jasa!!)

                }

                rv_jasa_list_aktivitas.adapter = ListJasaActivityAdapter(jasaList) {

                }


            }


        })
    }

    companion object {
        private const val TAG = "MyActivity"
    }
    private fun changeBackground(button: LinearLayout, int: Int) {
        button.setBackgroundResource(int)
    }

}
