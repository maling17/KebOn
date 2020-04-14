package com.example.kebon

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.kebon.fragment.ActivityFragment
import com.example.kebon.fragment.HomeFragment
import com.example.kebon.fragment.JasaFragment
import com.example.kebon.fragment.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragment_home = HomeFragment()
        val fragment_activity = ActivityFragment()
        val fragment_jasa = JasaFragment()
        val fragment_profile = ProfileFragment()

        setFragment(fragment_home)

        btn_home.setOnClickListener {
            changeIcon(iv_home, R.drawable.ic_home_enable)
            changeIcon(iv_activity, R.drawable.ic_list_disable)
            changeIcon(iv_jasa, R.drawable.ic_farming_and_gardening)
            changeIcon(iv_profile, R.drawable.ic_profil_disable)

            changeTextColor(tv_home, R.color.colorOrange)
            changeTextColor(tv_activity, R.color.colorWhite)
            changeTextColor(tv_jasa, R.color.colorWhite)
            changeTextColor(tv_profile, R.color.colorWhite)
            setFragment(fragment_home)
        }


        btn_activity.setOnClickListener {
            changeIcon(iv_home, R.drawable.ic_home_disable)
            changeIcon(iv_activity, R.drawable.ic_list_enable)
            changeIcon(iv_jasa, R.drawable.ic_farming_and_gardening)
            changeIcon(iv_profile, R.drawable.ic_profil_disable)

            changeTextColor(tv_home, R.color.colorWhite)
            changeTextColor(tv_activity, R.color.colorOrange)
            changeTextColor(tv_jasa, R.color.colorWhite)
            changeTextColor(tv_profile, R.color.colorWhite)

            setFragment(fragment_activity)
        }


        btn_jasa.setOnClickListener {
            changeIcon(iv_home, R.drawable.ic_home_disable)
            changeIcon(iv_activity, R.drawable.ic_list_disable)
            changeIcon(iv_jasa, R.drawable.ic_farming_and_gardening_enable)
            changeIcon(iv_profile, R.drawable.ic_profil_disable)

            changeTextColor(tv_home, R.color.colorWhite)
            changeTextColor(tv_activity, R.color.colorWhite)
            changeTextColor(tv_jasa, R.color.colorOrange)
            changeTextColor(tv_profile, R.color.colorWhite)

            setFragment(fragment_jasa)
        }

        btn_profile.setOnClickListener {
            changeIcon(iv_home, R.drawable.ic_home_disable)
            changeIcon(iv_activity, R.drawable.ic_list_disable)
            changeIcon(iv_jasa, R.drawable.ic_farming_and_gardening)
            changeIcon(iv_profile, R.drawable.ic_profil_enable)

            changeTextColor(tv_home, R.color.colorWhite)
            changeTextColor(tv_activity, R.color.colorWhite)
            changeTextColor(tv_jasa, R.color.colorWhite)
            changeTextColor(tv_profile, R.color.colorOrange)

            setFragment(fragment_profile)
        }


    }

    private fun setFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_Frame, fragment)
        fragmentTransaction.commit()
    }

    private fun changeIcon(imageView: ImageView, int: Int) {
        imageView.setImageResource(int)
    }

    private fun changeTextColor(textView: TextView, id: Int) {

        textView.setTextColor(ContextCompat.getColor(this, id))
    }
}
