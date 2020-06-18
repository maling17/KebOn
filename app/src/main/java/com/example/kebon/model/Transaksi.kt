package com.example.kebon.model

import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
class Transaksi (
    var id_transaksi:String?="",
    var jenis_pengiriman:String?="",
    var subtotal_produk_beli:String?="",
    var subtotal_pengiriman:String?="",
    var total_biaya:String?="",
    var tgl_transaksi:String?="",
    var jumlah_beli:String?="",
    var status_beli:String?="",
    var id_produk:String?="",
    var nm_produk:String?="",
    var url_gambar:String?="",
    var kategori:String?=""
):Parcelable
