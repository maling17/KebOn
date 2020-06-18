package com.example.kebon.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kebon.R
import com.example.kebon.model.Jasa
import com.example.kebon.utils.Preferences
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class KeranjangJasaAdapter(
    private var data: List<Jasa>,
    private val listener: (Jasa) -> Unit
) : RecyclerView.Adapter<KeranjangJasaAdapter.LeagueViewHolder>() {
    lateinit var ContextAdapter: Context
    var total: Int = 0

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        lateinit var mDatabase: DatabaseReference
        lateinit var preferences: Preferences
        var username: String = ""
        private val tvJudul: TextView = view.findViewById(R.id.tv_judul_keranjang)
        private val tvHarga: TextView = view.findViewById(R.id.tv_harga_keranjang)
        private val tvtotal: TextView = view.findViewById(R.id.tv_jumlah_beli_keranjang)
        private val ivPhoto: ImageView = view.findViewById(R.id.iv_photo_keranjang)
        private val btnMin: ImageView = view.findViewById(R.id.btn_min)
        private val btnPlus: ImageView = view.findViewById(R.id.btn_plus)

        @SuppressLint("SetTextI18n")
        fun bindItem(
            data: Jasa,
            listener: (Jasa) -> Unit,
            context: Context,
            position: Int
        ) {
            mDatabase = FirebaseDatabase.getInstance().reference
            preferences = Preferences(context)

            username = preferences.getValues("username").toString()

            tvJudul.text = data.nm_produk
            tvHarga.text = data.subtotal_produk_jasa.toString()
            tvtotal.text = data.jumlah_jasa.toString()
            Picasso.get().load(data.url_gambar).into(ivPhoto)

            var totaljasa: Int = data.jumlah_jasa?.toInt()!!
            val hargaProduk = data.subtotal_produk_jasa?.toInt()!!
            var hargaSementara = hargaProduk
            val baseHargaProduk: Int = hargaProduk / totaljasa
            val getIdjasa = data.id_jasa

            btnMin.setOnClickListener {

                //jika kuantitas kurang dari 1 maka dialog akan cancel
                if (totaljasa == 1) {
                    btnMin.isClickable = false
                } else {

                    val jasa = Jasa()
                    btnMin.isClickable = true
                    totaljasa -= 1
                    tvtotal.text = "$totaljasa"
                    val totalHargaMin = hargaSementara - baseHargaProduk
                    hargaSementara = totalHargaMin
                    tvHarga.text = totalHargaMin.toString()

                    jasa.jumlah_jasa = totaljasa.toString()
                    jasa.subtotal_produk_jasa = hargaSementara.toString()

                    mDatabase.child("Users")
                        .child(username)
                        .child("Jasa")
                        .child(getIdjasa!!)
                        .child("jumlah_jasa")
                        .setValue(jasa.jumlah_jasa)

                    mDatabase.child("Users")
                        .child(username)
                        .child("Jasa")
                        .child(getIdjasa)
                        .child("subtotal_produk_jasa")
                        .setValue(jasa.subtotal_produk_jasa)

                }

            }

            btnPlus.setOnClickListener {
                val jasa = Jasa()
                btnMin.isClickable = true
                totaljasa += 1
                tvtotal.text = "$totaljasa"
                val totalHargaPlus = baseHargaProduk * totaljasa
                hargaSementara = totalHargaPlus
                tvHarga.text = totalHargaPlus.toString()

                jasa.jumlah_jasa = totaljasa.toString()
                jasa.subtotal_produk_jasa = hargaSementara.toString()

                mDatabase.child("Users")
                    .child(username)
                    .child("Jasa")
                    .child(getIdjasa!!)
                    .child("jumlah_jasa")
                    .setValue(jasa.jumlah_jasa)

                mDatabase.child("Users")
                    .child(username)
                    .child("Jasa")
                    .child(getIdjasa)
                    .child("subtotal_produk_jasa")
                    .setValue(jasa.subtotal_produk_jasa)
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