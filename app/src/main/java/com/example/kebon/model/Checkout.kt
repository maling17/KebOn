package com.example.kebon.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Checkout (
    var namaProduk:String?="",
    var hargaProduk:Int?=0,
    var stokProduk:Int?=0,
    var totalProduk:Int?=0,
    var urlPhoto:String?=""

)
    :Parcelable