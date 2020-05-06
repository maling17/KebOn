package com.example.kebon.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Artikel(

    var author: String? = "",
    var date: String? = "",
    var id_artikel: String? = "",
    var isi_artikel: String? = "",
    var judul_artikel: String? = "",
    var url_gambar: String? = "",
    var publisher: String? = ""


) : Parcelable