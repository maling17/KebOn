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
import com.example.kebon.model.StarterProduk
import com.squareup.picasso.Picasso

class StarterpackAdapter(
    private var data: List<StarterProduk>,
    private val listener: (StarterProduk) -> Unit
) : RecyclerView.Adapter<StarterpackAdapter.LeagueViewHolder>() {
    lateinit var ContextAdapter: Context

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvNama: TextView = view.findViewById(R.id.tv_nm_produk)
        private val tvHarga: TextView = view.findViewById(R.id.tv_harga_produk_item)
        private val ivPhoto: ImageView = view.findViewById(R.id.iv_photo_produk)

        @SuppressLint("SetTextI18n")
        fun bindItem(data: StarterProduk, listener: (StarterProduk) -> Unit, context: Context, position: Int) {
            tvNama.text = data.nm_produk
            tvHarga.text ="Rp"+data.harga_beli

            Picasso.get().load(data.url).into(ivPhoto)
            itemView.setOnClickListener {
                listener(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val inflatedView: View = layoutInflater.inflate(R.layout.produk_item, parent, false)

        return LeagueViewHolder(inflatedView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bindItem(data[position], listener, ContextAdapter, position)
    }

}