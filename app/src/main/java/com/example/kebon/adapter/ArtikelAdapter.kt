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
import com.example.kebon.model.Artikel
import com.squareup.picasso.Picasso

class ArtikelAdapter(
    private var data: List<Artikel>,
    private val listener: (Artikel) -> Unit
) : RecyclerView.Adapter<ArtikelAdapter.LeagueViewHolder>() {
    lateinit var ContextAdapter: Context

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvJudul: TextView = view.findViewById(R.id.tv_judul_artikel)
        private val tvAuthor: TextView = view.findViewById(R.id.tv_author_artikel)
        private val tvTanggal: TextView = view.findViewById(R.id.tv_tanggal_rilis_artikel)
        private val ivPhoto: ImageView = view.findViewById(R.id.iv_photo_artikel)

        @SuppressLint("SetTextI18n")
        fun bindItem(data: Artikel, listener: (Artikel) -> Unit, context: Context, position: Int) {
            tvJudul.text = data.judul_artikel
            tvAuthor.text = data.author
            tvTanggal.text = data.date
            Picasso.get().load(data.url_gambar).into(ivPhoto)

            itemView.setOnClickListener {
                listener(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val inflatedView: View = layoutInflater.inflate(R.layout.artikel_item, parent, false)

        return LeagueViewHolder(inflatedView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bindItem(data[position], listener, ContextAdapter, position)
    }

}