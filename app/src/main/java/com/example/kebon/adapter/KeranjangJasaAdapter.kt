package com.example.kebon.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kebon.R
import com.example.kebon.model.Detail_Jasa
import com.example.kebon.utils.Preferences
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class KeranjangJasaAdapter(
    private var data: List<Detail_Jasa>,
    private val listener: (Detail_Jasa) -> Unit
) : RecyclerView.Adapter<KeranjangJasaAdapter.LeagueViewHolder>() {
    lateinit var ContextAdapter: Context
    var total: Int = 0

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        lateinit var mDatabase: DatabaseReference
        lateinit var preferences: Preferences
        var username: String = ""

        var nmProduk = ""
        var hargaBeliProduk = ""
        var urlGambar = ""

        private val tvNama: TextView = view.findViewById(R.id.tv_nama_produk_list_activity)
        private val tvHarga: TextView = view.findViewById(R.id.tv_harga_keranjang)
        private val tvtotal: TextView = view.findViewById(R.id.tv_jumlah_beli_keranjang)
        private val ivPhoto: ImageView = view.findViewById(R.id.iv_photo_keranjang)
        private val btnMin: ImageView = view.findViewById(R.id.btn_min)
        private val btnPlus: ImageView = view.findViewById(R.id.btn_plus)

        @SuppressLint("SetTextI18n")
        fun bindItem(
            data: Detail_Jasa,
            listener: (Detail_Jasa) -> Unit,
            context: Context,
            position: Int
        ) {
            mDatabase = FirebaseDatabase.getInstance().reference
            preferences = Preferences(context)

            username = preferences.getValues("username").toString()
            val idProduk = data.id_produk.toString()
            val mDatabaseProduk =
                mDatabase.child("Produk").orderByChild("id_produk").equalTo(idProduk)


            mDatabaseProduk.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(context, p0.message, Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(p0: DataSnapshot) {

                    for (dataSnapshot in p0.children) {
                        nmProduk = dataSnapshot.child("nm_produk").value.toString()
                        hargaBeliProduk = dataSnapshot.child("harga_beli").value.toString()
                        urlGambar = dataSnapshot.child("url").value.toString()
                    }

                    tvNama.text = nmProduk
                    Picasso.get().load(urlGambar).into(ivPhoto)

                }

            })

            tvHarga.text=data.harga_beli
            tvtotal.text=data.jumlah_jasa
            var totaljasa: Int = data.jumlah_jasa?.toInt()!!
            val hargaProduk = data.harga_beli?.toInt()!!
            var hargaSementara = hargaProduk
            val baseHargaProduk: Int = hargaProduk / totaljasa
            val getIdjasa = preferences.getValues("id_jasa")
            val getIdProduk=data.id_produk.toString()

            btnMin.setOnClickListener {

                //jika kuantitas kurang dari 1 maka dialog akan cancel
                if (totaljasa == 1) {
                    btnMin.isClickable = false
                } else {

                    val detailJasa = Detail_Jasa()
                    btnMin.isClickable = true
                    totaljasa -= 1
                    tvtotal.text = "$totaljasa"
                    val totalHargaMin = hargaSementara - baseHargaProduk
                    hargaSementara = totalHargaMin
                    tvHarga.text = totalHargaMin.toString()

                    detailJasa.jumlah_jasa = totaljasa.toString()
                    detailJasa.harga_beli = hargaSementara.toString()

                    mDatabase.child("Users")
                        .child(username)
                        .child("Jasa")
                        .child(getIdjasa!!)
                        .child("Detail_Jasa")
                        .child(getIdProduk)
                        .child("jumlah_jasa")
                        .setValue(detailJasa.jumlah_jasa)

                    mDatabase.child("Users")
                        .child(username)
                        .child("Jasa")
                        .child(getIdjasa)
                        .child("Detail_Jasa")
                        .child(getIdProduk)
                        .child("harga_beli")
                        .setValue(detailJasa.harga_beli)

                }

            }

            btnPlus.setOnClickListener {
                val detailJasa = Detail_Jasa()
                btnMin.isClickable = true
                totaljasa += 1
                tvtotal.text = "$totaljasa"
                val totalHargaPlus = baseHargaProduk * totaljasa
                hargaSementara = totalHargaPlus
                tvHarga.text = totalHargaPlus.toString()

                detailJasa.jumlah_jasa = totaljasa.toString()
                detailJasa.harga_beli = hargaSementara.toString()

                mDatabase.child("Users")
                    .child(username)
                    .child("Jasa")
                    .child(getIdjasa!!)
                    .child("Detail_Jasa")
                    .child(getIdProduk)
                    .child("jumlah_jasa")
                    .setValue(detailJasa.jumlah_jasa)

                mDatabase.child("Users")
                    .child(username)
                    .child("Jasa")
                    .child(getIdjasa)
                    .child("Detail_Jasa")
                    .child(getIdProduk)
                    .child("harga_beli")
                    .setValue(detailJasa.harga_beli)
            }
            itemView.setOnClickListener {
                listener(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val inflatedView: View = layoutInflater.inflate(R.layout.keranjang_item, parent, false)

        return LeagueViewHolder(inflatedView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bindItem(data[position], listener, ContextAdapter, position)

    }

}