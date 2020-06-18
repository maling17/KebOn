package com.example.kebon.sign

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kebon.MainActivity
import com.example.kebon.R
import com.example.kebon.model.User
import com.example.kebon.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var sUsername: String
    private lateinit var sPassword: String
    private lateinit var sEmail: String
    private lateinit var statusUser: String
    private var status: Boolean = false

    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    private lateinit var mDatabase: DatabaseReference

    private lateinit var preferences: Preferences

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mFirebaseDatabase = mFirebaseInstance.getReference("Users")

        preferences = Preferences(this)

        btn_signUp.setOnClickListener {
            sUsername = et_username_signUp.text.toString()
            sPassword = et_password_signUp.text.toString()
            sEmail = et_email_signUp.text.toString()
            statusUser="1"

            when {
                sUsername == "" -> {
                    et_username_signUp.error = "Silahkan isi Username"
                    et_username_signUp.requestFocus()
                }
                sPassword == "" -> {
                    et_password_signUp.error = "Silahkan isi Password"
                    et_password_signUp.requestFocus()
                }
                sEmail == "" -> {
                    et_email_signUp.error = "Silahkan isi Email"
                    et_email_signUp.requestFocus()
                }
                else -> {

                    saveUser(sUsername, sPassword, sEmail,statusUser)

                }
            }
        }

        et_password_signUp.setOnTouchListener(View.OnTouchListener { v, event ->

            val right = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= et_password_signUp.right - et_password_signUp.compoundDrawables[right].bounds.width()
                ) {
                    if (!status) {
                        status = true
                        et_password_signUp.inputType = 129

                        val drawable: Drawable =
                            et_password_signUp.context.resources.getDrawable(R.drawable.ic_password_enable)

                        et_password_signUp.setCompoundDrawablesWithIntrinsicBounds(
                            null,
                            null,
                            drawable,
                            null
                        )

                        return@OnTouchListener true
                    } else {
                        status = false
                        et_password_signUp.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD

                        val drawable: Drawable =
                            et_password_signUp.context.resources.getDrawable(R.drawable.ic_password_disable)
                        et_password_signUp.setCompoundDrawablesWithIntrinsicBounds(
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

        tv_signin.setOnClickListener {
            val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }

    private fun checkingUsername(username: String, data: User) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading")
        progressDialog.show()
        mFirebaseDatabase.child(username).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val user = dataSnapshot.getValue(User::class.java)
                if (user == null) {

                    mFirebaseDatabase.child(username).setValue(data)
                    val status="1"

                    preferences.setValues("nama", data.password.toString())
                    preferences.setValues("user", data.username.toString())
                    preferences.setValues("email", data.email.toString())
                    preferences.setValues("status", status)
                    progressDialog.dismiss()

                    val intent = Intent(
                        this@SignUpActivity,
                        MainActivity::class.java
                    )
                    startActivity(intent)
                    finishAffinity()

                } else {
                    progressDialog.dismiss()
                    Toast.makeText(this@SignUpActivity, "User sudah digunakan", Toast.LENGTH_LONG)
                        .show()

                }
            }

            override fun onCancelled(error: DatabaseError) {
                progressDialog.dismiss()
                Toast.makeText(this@SignUpActivity, "" + error.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun saveUser(sUsername: String, sPassword: String, sEmail: String,statusUser:String) {

        val user = User()
        user.email = sEmail
        user.username = sUsername
        user.password = sPassword
        user.status = statusUser

        checkingUsername(sUsername, user)

    }
}
