package com.example.kebon.model

import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
class Alamat (
    var alamat_lengkap:String?="",
    var id_alamat:String?="",
    var kabupaten:String?="",
    var kecamatan:String?="",
    var kode_pos:String?="",
    var nm_alamat:String?="",
    var nmr_telp:String?="",
    var provinsi:String?="",
    var status:String?=""
):Parcelable
