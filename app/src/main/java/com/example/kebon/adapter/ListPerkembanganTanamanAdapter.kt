package com.example.kebon.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kebon.R
import com.example.kebon.detail.DetailJasaActivity
import com.example.kebon.model.Perkembangan_Tanaman
import com.example.kebon.utils.Preferences
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class ListPerkembanganTanamanAdapter(
    private var data: List<Perkembangan_Tanaman>,
    private val listener: (Perkembangan_Tanaman) -> Unit
) : RecyclerView.Adapter<ListPerkembanganTanamanAdapter.LeagueViewHolder>() {
    lateinit var ContextAdapter: Context

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var mDatabase: DatabaseReference
        lateinit var preferences: Preferences
        var username: String = ""
        private val tvNama: TextView = view.findViewById(R.id.tv_nama_produk_list_perkembangan)
        private val tvTgl: TextView = view.findViewById(R.id.tv_tgl_list_perkembangan)
        private val tvStatus: TextView = view.findViewById(R.id.tv_status_list_perkembangan)
        private val ivPhoto: ImageView = view.findViewById(R.id.iv_photo_list_perkembangan)

        @SuppressLint("SetTextI18n")
        fun bindItem(
            data: Perkembangan_Tanaman,
            listener: (Perkembangan_Tanaman) -> Unit,
            context: Context,
            position: Int
        ) {

            mDatabase = FirebaseDatabase.getInstance().reference
            preferences = Preferences(context)

            username = preferences.getValues("username").toString()
            tvNama.text = data.nm_tanaman
            tvTgl.text = data.tgl_mulai
            tvStatus.text = data.status_perkembangan
            Picasso.get().load(data.url_perkembangan).into(ivPhoto)

            itemView.setOnClickListener {

                val intent=Intent(context,DetailJasaActivity::class.java)
                intent.putExtra("id_perkembangan",data.id_perkembangan)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val inflatedView: View =
            layoutInflater.inflate(R.layout.list_perkembangan_item, parent, false)

        return LeagueViewHolder(inflatedView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bindItem(data[position], listener, ContextAdapter, position)
    }

}