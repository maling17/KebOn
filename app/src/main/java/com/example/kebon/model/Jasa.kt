package com.example.kebon.model

import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
class Jasa (
    var id_jasa:String?="",
    var durasi_perawatan:String?="",
    var subtotal_produk_jasa:String?="",
    var subtotal_perawatan:String?="",
    var total_biaya_jasa:String?="",
    var tgl_transaksi:String?="",
    var status_jasa:String?=""

):Parcelable
