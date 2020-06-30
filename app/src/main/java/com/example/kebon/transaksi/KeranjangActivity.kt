package com.example.kebon.transaksi

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.kebon.R
import com.example.kebon.fragment.BeliKeranjangFragment
import com.example.kebon.fragment.JasaKeranjangFragment
import kotlinx.android.synthetic.main.activity_keranjang.*

class KeranjangActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keranjang)

        val beliFragment = BeliKeranjangFragment()
        val jasaFragment = JasaKeranjangFragment()

        setFragment(beliFragment)

        btn_beli_keranjang.setOnClickListener {
            changeBackground(btn_beli_keranjang, R.drawable.left_rounded_button_orange)
            changeBackground(btn_jasa_keranjang, R.drawable.right_rounded_button_cream)

            setFragment(beliFragment)
        }
        btn_jasa_keranjang.setOnClickListener {
            changeBackground(btn_beli_keranjang, R.drawable.left_rounded_button_cream)
            changeBackground(btn_jasa_keranjang, R.drawable.right_rounded_button_orange)
            setFragment(jasaFragment)
        }


        iv_back_keranjang.setOnClickListener {
            finish()
        }
    }

    private fun setFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.keranjang_frame, fragment)
        fragmentTransaction.commit()
    }

    private fun changeBackground(button: LinearLayout, int: Int) {
        button.setBackgroundResource(int)
    }


}
