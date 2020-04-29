package com.example.kebon.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kebon.home.ProdukPopulerActivity
import com.example.kebon.R
import com.example.kebon.home.StarterPackActivity
import com.example.kebon.home.TipsActivity
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tv_lihat_starterPack.setOnClickListener {
            val intent = Intent(context, StarterPackActivity::class.java)
            startActivity(intent)
        }
        tv_lihat_produkPopuler.setOnClickListener {
            val intent = Intent(context, ProdukPopulerActivity::class.java)
            startActivity(intent)
        }
        tv_lihat_tipsBerkebun.setOnClickListener {
            val intent = Intent(context, TipsActivity::class.java)
            startActivity(intent)
        }

    }

}
