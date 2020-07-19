package com.example.kebon.model

import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
class Detail_Jasa (

    var id_produk:String?="",
    var jumlah_jasa:String?="",
    var harga_beli:String?="",
    var harga_jasa:String?=""

):Parcelable
