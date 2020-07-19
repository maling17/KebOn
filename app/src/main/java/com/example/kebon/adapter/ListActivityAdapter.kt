package com.example.kebon.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kebon.PembayaranActivity
import com.example.kebon.R
import com.example.kebon.detail.DetailActivityActivity
import com.example.kebon.model.Transaksi
import com.example.kebon.utils.Preferences
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class ListActivityAdapter(
    private var data: List<Transaksi>,
    private val listener: (Transaksi) -> Unit
) : RecyclerView.Adapter<ListActivityAdapter.LeagueViewHolder>() {
    lateinit var ContextAdapter: Context

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var mDatabase: DatabaseReference
        lateinit var preferences: Preferences
        var username: String = ""
        var nmProduk = ""
        var hargaBeliProduk = ""
        var urlGambar = ""
        private val tvNama: TextView = view.findViewById(R.id.tv_nama_produk_list_activity)
        private val tvHarga: TextView = view.findViewById(R.id.tv_total_bayar_list_activity)
        private val tvStatus: TextView = view.findViewById(R.id.tv_status_list_activity)
        private val tvJumlah: TextView = view.findViewById(R.id.tv_jumlah_beli_list_activity)
        private val ivPhoto: ImageView = view.findViewById(R.id.iv_photo_list_activity)

        @SuppressLint("SetTextI18n")
        fun bindItem(
            data: Transaksi,
            listener: (Transaksi) -> Unit,
            context: Context,
            position: Int
        ) {

            mDatabase = FirebaseDatabase.getInstance().reference
            preferences = Preferences(context)

            username = preferences.getValues("username").toString()

            tvHarga.text = "Rp${data.total_biaya}"

            when (data.status_beli) {
                "2" -> tvStatus.text = "Menunggu Pembayaran dan Bukti Pembayaran"
                "3" -> tvStatus.text = "Menunggu Konfirmasi Pembayaran"
                "4" -> tvStatus.text = "Produk Sedang Dalam Perjalanan"
                "5" -> tvStatus.text = "Produk sudah sampai"
            }

            val idTransaksi = data.id_transaksi

            val mDatabaseTransaksi =
                mDatabase.child("Users").child(username).child("Transaksi")
                    .child(idTransaksi.toString()).child("Detail_Transaksi")

            mDatabaseTransaksi.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(context, "data KOSONG", Toast.LENGTH_SHORT).show()

                }

                override fun onDataChange(p0: DataSnapshot) {
                    for (dataSnapshot in p0.children) {

                        val qtyProduk = dataSnapshot.child("jumlah_beli").value.toString()
                        tvJumlah.text = "x$qtyProduk"

                        val idProduk = dataSnapshot.child("id_produk").value.toString()
                        val mDatabaseProduk =
                            mDatabase.child("Produk").orderByChild("id_produk").equalTo(idProduk)

                        mDatabaseProduk.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                Toast.makeText(context, p0.message, Toast.LENGTH_SHORT).show()
                            }

                            override fun onDataChange(p0: DataSnapshot) {

                                for (getDataSnapshot in p0.children) {
                                    nmProduk = getDataSnapshot.child("nm_produk").value.toString()
                                    hargaBeliProduk =
                                        getDataSnapshot.child("harga_beli").value.toString()
                                    urlGambar = getDataSnapshot.child("url").value.toString()
                                }

                                tvNama.text = nmProduk
                                Picasso.get().load(urlGambar).into(ivPhoto)

                            }

                        })

                    }
                }

            })
            itemView.setOnClickListener {
                listener(data)
                preferences.setValues("id_transaksi", data.id_transaksi.toString())

                when (data.status_beli) {
                    "2" -> {
                        val intent = Intent(context, PembayaranActivity::class.java)
                        intent.putExtra("kategori_transaksi", "beli")
                        val totalTransaksi = data.total_biaya.toString()
                        intent.putExtra("total_pembayaran", totalTransaksi)
                        context.startActivity(intent)
                    }
                    "3" -> {
                        val intent = Intent(context, DetailActivityActivity::class.java)
                        intent.putExtra("status_beli", "3")

                        context.startActivity(intent)

                    }
                    "4" -> {
                        val intent = Intent(context, DetailActivityActivity::class.java)
                        intent.putExtra("status_beli", "4")
                        context.startActivity(intent)
                    }
                    "5" -> {
                        Toast.makeText(context, "Pesanan Sudah Sampai", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val inflatedView: View = layoutInflater.inflate(R.layout.list_activity_item, parent, false)

        return LeagueViewHolder(inflatedView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bindItem(data[position], listener, ContextAdapter, position)
    }

}