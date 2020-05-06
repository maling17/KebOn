package com.example.kebon.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Checkout (
    var namaProduk:String?="",
    var hargaProduk:Int?=null,
    var stokProduk:Int?=null

)
    :Parcelable