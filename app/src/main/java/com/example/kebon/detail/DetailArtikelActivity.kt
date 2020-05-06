package com.example.kebon.detail

import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import com.example.kebon.R
import com.example.kebon.model.Artikel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_artikel.*

class DetailArtikelActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_artikel)

        getData()

        btn_back_detail_artikel.setOnClickListener {
            finish()
        }
    }

    private fun getData() {
        val data = intent.getParcelableExtra<Artikel>("data")!!

        val isiArtikel = data.isi_artikel

        tv_isi_detail_artikel.text = Html.fromHtml(isiArtikel)
        tv_judul_detail_artikel.text = data.judul_artikel
        tv_author_detail_artikel.text = data.author
        tv_tanggal_detail_artikel.text = data.date
        Picasso.get().load(data.url_gambar).into(iv_photo_detail_artikel)
    }

}
