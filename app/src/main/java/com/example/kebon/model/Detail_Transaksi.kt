package com.example.kebon.model

import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
class Detail_Transaksi (

    var jumlah_beli:String?="",
    var id_produk:String?="",
    var harga_beli:String?=""

):Parcelable
