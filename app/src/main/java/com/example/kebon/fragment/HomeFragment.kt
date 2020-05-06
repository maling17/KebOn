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
import com.example.kebon.adapter.ArtikelAdapter
import com.example.kebon.adapter.ProdukAdapter
import com.example.kebon.adapter.StarterpackAdapter
import com.example.kebon.detail.*
import com.example.kebon.home.ProdukPopulerActivity
import com.example.kebon.home.StarterPackActivity
import com.example.kebon.home.TipsActivity
import com.example.kebon.model.Artikel
import com.example.kebon.model.Produk
import com.example.kebon.model.StarterProduk
import com.example.kebon.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
    private lateinit var preferences: Preferences
    private lateinit var mDatabase: DatabaseReference
    private var dataListStarter = ArrayList<StarterProduk>()
    private var dataList = ArrayList<Produk>()
    private var dataListArtikel = ArrayList<Artikel>()
    var produk2: String? = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        preferences = Preferences(activity!!.applicationContext)
        mDatabase = FirebaseDatabase.getInstance().reference

        getDataStarterPack()
        getDataProduk()
        getDataArtikel()

        tv_nama_home.text = preferences.getValues("username")

        tv_lihat_starterPack.setOnClickListener {
            val intent = Intent(context, StarterPackActivity::class.java)
            startActivity(intent)
        }

        tv_lihat_produkPopuler.setOnClickListener {
            val intent = Intent(context, ProdukPopulerActivity::class.java)
            startActivity(intent)
        }

        tv_lihat_tipsBerkebun.setOnClickListener {
            val intent = Intent(context, TipsActivity::class.java)
            startActivity(intent)
        }

        rv_starter_pack.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        rv_produk_populer.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        rv_artikel.layoutManager = LinearLayoutManager(context)

    }

    private fun getDataStarterPack() {
        val mDatabaseStarter =
            mDatabase.child("Produk").orderByChild("kategori").equalTo("Starterpack")

        mDatabaseStarter.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {

                    dataList.clear()

                    for (getdataSnapshot in p0.children) {

                        val produk = getdataSnapshot.getValue(StarterProduk::class.java)
                        dataListStarter.add(produk!!)

                    }
                }

                rv_starter_pack.adapter = StarterpackAdapter(dataListStarter) {
                    produk2 = "starter"

                    val intent =
                        Intent(context, DetailPackageActivity::class.java).putExtra("data", it)
                            .putExtra("produk", produk2)
                    startActivity(intent)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, "" + p0.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getDataProduk() {
        val mDatabaseProduk = mDatabase.child("Produk")

        mDatabaseProduk.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {

                dataList.clear()
                for (getdataSnapshot in p0.children) {

                    val produk = getdataSnapshot.getValue(Produk::class.java)
                    dataList.add(produk!!)
                }
                rv_produk_populer.adapter = ProdukAdapter(dataList) {

                    produk2 = "produk"
                    val indexArrayList = dataList.indexOf(it) //mengindex dulu isi yang ada di array
                    val filter =
                        dataList[indexArrayList].kategori //mengfilter dan mengambil value dari kategori

                    if (filter == "Alat") {
                        val intent =
                            Intent(context, DetailAlatActivity::class.java).putExtra("data", it)
                                .putExtra("produk", produk2)
                        startActivity(intent)
                    }
                    if (filter == "Tanaman") {
                        val intent =
                            Intent(context, DetailTanamanActivity::class.java)
                                .putExtra("data", it)
                                .putExtra("produk", produk2)
                        startActivity(intent)
                    }
                    if (filter == "Bibit") {
                        val intent =
                            Intent(context, DetailBenihActivity::class.java)
                                .putExtra("produk", produk2)
                                .putExtra("data", it)
                        startActivity(intent)
                    }
                    if (filter == "Pupuk") {
                        val intent =
                            Intent(context, DetailAlatActivity::class.java).putExtra("data", it)
                                .putExtra("produk", produk2)
                        startActivity(intent)
                    }
                    if (filter == "Starterpack") {
                        val intent =
                            Intent(context, DetailPackageActivity::class.java).putExtra("data", it)
                                .putExtra("produk", produk2)
                        startActivity(intent)
                    }

                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, "" + p0.message, Toast.LENGTH_LONG).show()
            }
        })
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

                rv_artikel.adapter = ArtikelAdapter(dataListArtikel) {

                    val intent =
                        Intent(context, DetailArtikelActivity::class.java).putExtra("data", it)

                    startActivity(intent)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, "" + p0.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}
