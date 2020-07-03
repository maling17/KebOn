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
import com.example.kebon.model.Detail_Transaksi
import com.example.kebon.model.Transaksi
import com.example.kebon.utils.Preferences
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class KeranjangAdapter(
    private var data: List<Detail_Transaksi>,
    private val listener: (Detail_Transaksi) -> Unit
) : RecyclerView.Adapter<KeranjangAdapter.LeagueViewHolder>() {
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
            data: Detail_Transaksi,
            listener: (Detail_Transaksi) -> Unit,
            context: Context,
            position: Int
        ) {
            mDatabase = FirebaseDatabase.getInstance().reference
            preferences = Preferences(context)


            username = preferences.getValues("username").toString()

            tvJudul.text = data.nm_produk
            tvHarga.text = data.harga_produk.toString()
            tvtotal.text = data.jumlah_beli.toString()
            val idProduk=data.id_produk.toString()
            Picasso.get().load(data.url_gambar).into(ivPhoto)

            var totaltransaksi: Int = data.jumlah_beli?.toInt()!!
            val hargaProduk = data.harga_produk?.toInt()!!
            var hargaSementara = hargaProduk
            val baseHargaProduk: Int = hargaProduk / totaltransaksi
            val getIdTransaksi =  preferences.getValues("id_transaksi")

            btnMin.setOnClickListener {

                //jika kuantitas kurang dari 1 maka dialog akan cancel
                if (totaltransaksi == 1) {
                    mDatabase.child("Users")
                        .child(username)
                        .child("Transaksi")
                        .child(getIdTransaksi!!).removeValue()
                } else {

                    val detailTransaksi = Detail_Transaksi()
                    btnMin.isClickable = true
                    totaltransaksi -= 1
                    tvtotal.text = "$totaltransaksi"
                    val totalHargaMin = hargaSementara - baseHargaProduk
                    hargaSementara = totalHargaMin
                    tvHarga.text = totalHargaMin.toString()

                    detailTransaksi.jumlah_beli = totaltransaksi.toString()
                    detailTransaksi.harga_produk = hargaSementara.toString()

                    mDatabase.child("Users")
                        .child(username)
                        .child("Transaksi")
                        .child(getIdTransaksi!!)
                        .child("Detail_Transaksi")
                        .child(idProduk)
                        .child("jumlah_beli")
                        .setValue(detailTransaksi.jumlah_beli)

                    mDatabase.child("Users")
                        .child(username)
                        .child("Transaksi")
                        .child(getIdTransaksi)
                        .child("Detail_Transaksi")
                        .child(idProduk)
                        .child("harga_produk")
                        .setValue(detailTransaksi.harga_produk)

                }

            }

            btnPlus.setOnClickListener {
                val detailTransaksi = Detail_Transaksi()
                btnMin.isClickable = true
                totaltransaksi += 1
                tvtotal.text = "$totaltransaksi"
                val totalHargaPlus = baseHargaProduk * totaltransaksi
                hargaSementara = totalHargaPlus
                tvHarga.text = totalHargaPlus.toString()

                detailTransaksi.jumlah_beli = totaltransaksi.toString()
                detailTransaksi.harga_produk = hargaSementara.toString()

                mDatabase.child("Users")
                    .child(username)
                    .child("Transaksi")
                    .child(getIdTransaksi!!)
                    .child("Detail_Transaksi")
                    .child(idProduk)
                    .child("jumlah_beli")
                    .setValue(detailTransaksi.jumlah_beli)

                mDatabase.child("Users")
                    .child(username)
                    .child("Transaksi")
                    .child(getIdTransaksi)
                    .child("Detail_Transaksi")
                    .child(idProduk)
                    .child("harga_produk")
                    .setValue(detailTransaksi.harga_produk)
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