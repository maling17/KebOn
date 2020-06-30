package com.example.kebon.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kebon.R
import com.example.kebon.model.Alamat

class PilihAlamatAdapter(
    private var data: List<Alamat>,
    private val listener: (Alamat) -> Unit
) : RecyclerView.Adapter<PilihAlamatAdapter.LeagueViewHolder>() {
    lateinit var ContextAdapter: Context

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvNamaAlamat: TextView = view.findViewById(R.id.tv_nm_alamat)
        val tvNmrHp: TextView = view.findViewById(R.id.tv_nomor_hp_alamat)
        val tvAlamatLengkap: TextView = view.findViewById(R.id.tv_alamat_lengkap)


        @SuppressLint("SetTextI18n")
        fun bindItem(data: Alamat, listener: (Alamat) -> Unit, context: Context, position: Int) {

            tvNamaAlamat.text=data.nm_alamat
            tvNmrHp.text=data.nmr_telp
            tvAlamatLengkap.text=data.alamat_lengkap

            itemView.setOnClickListener {
                listener(data)

                if (itemView.isSelected) {
                    itemView.setBackgroundResource(R.color.colorOrange)
                }else{
                    itemView.setBackgroundResource(R.color.colorGray)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val inflatedView: View = layoutInflater.inflate(R.layout.alamat_item, parent, false)

        return LeagueViewHolder(inflatedView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bindItem(data[position], listener, ContextAdapter, position)
    }

}