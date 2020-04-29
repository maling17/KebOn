package com.example.kebon.sign

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kebon.MainActivity
import com.example.kebon.R
import com.example.kebon.model.User
import com.example.kebon.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SignInActivity : AppCompatActivity() {

    lateinit var username: String
    lateinit var password: String
    lateinit var mDatabase: DatabaseReference
    lateinit var preferences: Preferences
    var status: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mDatabase = FirebaseDatabase.getInstance().getReference("Users")
        preferences = Preferences(this)

        btn_login.setOnClickListener {

            username = etUsername.text.toString()
            password = etPassword.text.toString()

            when {
                username == "" -> {
                    etUsername.error = "Silahkan tulis Username Anda"
                    etUsername.requestFocus()
                }
                password == "" -> {
                    etPassword.error = "Silahkan tulis Password Anda"
                    etPassword.requestFocus()
                }
                else -> {
                    pushLogin(username, password)
                }
            }
        }

        etPassword.setOnTouchListener(OnTouchListener { v, event ->

            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etPassword.right - etPassword.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    if (!status) {
                        status = true
                        etPassword.inputType = 129

                        val drawable: Drawable =
                            etPassword.context.resources.getDrawable(R.drawable.ic_password_enable)

                        etPassword.setCompoundDrawablesWithIntrinsicBounds(
                            null,
                            null,
                            drawable,
                            null
                        )

                        return@OnTouchListener true
                    } else {
                        status = false
                        etPassword.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD

                        val drawable: Drawable =
                            etPassword.context.resources.getDrawable(R.drawable.ic_password_disable)
                        etPassword.setCompoundDrawablesWithIntrinsicBounds(
                            null,
                            null,
                            drawable,
                            null
                        )

                        return@OnTouchListener true
                    }


                }
            }
            false
        })


        tv_signgup.setOnClickListener {
            val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(intent)
        }


    }

    private fun pushLogin(username: String, password: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading")
        progressDialog.show()

        mDatabase.child(username).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val user = dataSnapshot.getValue(User::class.java)
                if (user == null) {
                    Toast.makeText(this@SignInActivity, "User tidak ditemukan", Toast.LENGTH_LONG)
                        .show()


                } else {
                    if (user.password.equals(password)) {
                        Toast.makeText(this@SignInActivity, "Selamat Datang", Toast.LENGTH_LONG)
                            .show()

                        preferences.setValues("nama", user.nm_lengkap.toString())
                        preferences.setValues("user", user.username.toString())
                        preferences.setValues("email", user.email.toString())
                        preferences.setValues("jk", user.jk.toString())
                        preferences.setValues("nmr_hp", user.nmr_hp.toString())
                        preferences.setValues("tgl_lahir", user.tgl_lahir.toString())
                        preferences.setValues("status", "1")

                        finishAffinity()

                        val intent = Intent(
                            this@SignInActivity,
                            MainActivity::class.java
                        )
                        progressDialog.dismiss()
                        startActivity(intent)

                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(
                            this@SignInActivity,
                            "Password Anda Salah",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignInActivity, "" + error.message, Toast.LENGTH_LONG).show()
            }
        })
    }


}

