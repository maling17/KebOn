package com.example.kebon.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(

    var email: String? = "",
    var jk: String? = "",
    var nm_lengkap: String? = "",
    var username: String? = "",
    var nmr_hp: String? = "",
    var password: String? = "",
    var tgl_lahir: String? = ""

) : Parcelable