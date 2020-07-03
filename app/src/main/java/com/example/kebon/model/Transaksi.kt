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
    var status_beli:String?="",
    var nm_alamat:String?="",
    var nmr_telp:String?="",
    var alamat_lengkap:String?="",
    var username:String?=""
):Parcelable
