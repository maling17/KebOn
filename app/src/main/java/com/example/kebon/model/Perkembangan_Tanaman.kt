package com.example.kebon.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Perkembangan_Tanaman(

   var id_perkembangan:String="",
   var nm_tanaman:String="",
   var status_perkembangan:String="",
   var estimasi_panen:String="",
   var deskripsi_tanaman:String="",
   var tgl_mulai:String="",
   var tgl_update:String="",
   var url_perkembangan:String="",
   var id_jasa:String=""


) : Parcelable