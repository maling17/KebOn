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
import com.example.kebon.model.*
import com.squareup.picasso.Picasso

class CheckoutJasaAdapter(
    private var data: List<Detail_Jasa>,
    private val listener: (Detail_Jasa) -> Unit
) : RecyclerView.Adapter<CheckoutJasaAdapter.LeagueViewHolder>() {
    lateinit var ContextAdapter: Context

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvNama: TextView = view.findViewById(R.id.tv_nama_produk_checkout)
        private val tvHarga: TextView = view.findViewById(R.id.tv_harga_beli_checkout)
        private val tvJumlahBeli:TextView=view.findViewById(R.id.tv_jumlah_beli_checkout)
        private val ivPhoto: ImageView = view.findViewById(R.id.iv_produk_checkout)

        @SuppressLint("SetTextI18n")
        fun bindItem(data: Detail_Jasa, listener: (Detail_Jasa) -> Unit, context: Context, position: Int) {
            tvNama.text = data.nm_produk
            tvHarga.text ="Rp"+data.harga_beli
            tvJumlahBeli.text=data.jumlah_jasa

            Picasso.get().load(data.url_gambar).into(ivPhoto)
            itemView.setOnClickListener {
                listener(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val inflatedView: View = layoutInflater.inflate(R.layout.checkout_item, parent, false)

        return LeagueViewHolder(inflatedView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bindItem(data[position], listener, ContextAdapter, position)
    }

}