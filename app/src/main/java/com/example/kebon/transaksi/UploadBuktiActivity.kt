package com.example.kebon.transaksi

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kebon.MainActivity
import com.example.kebon.R
import com.example.kebon.utils.Preferences
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_upload_bukti.*
import java.util.*

class UploadBuktiActivity : AppCompatActivity() {

    lateinit var mDatabase: DatabaseReference
    lateinit var preferences: Preferences
    private var newIdProduk = 0
    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var filePath: Uri
    var getUrl = ""
    val REQUEST_CODE = 100
    var getUsername = ""
    var idTransaksi = ""
    var idJasa = ""
    private var kategoriTransaksi = " "
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_bukti)

        kategoriTransaksi = intent.getStringExtra("kategori_transaksi").toString()

        mDatabase = FirebaseDatabase.getInstance().reference
        preferences = Preferences(this)
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference
        getUsername = preferences.getValues("username").toString()
        idTransaksi = preferences.getValues("id_transaksi").toString()
        idJasa = preferences.getValues("id_jasa").toString()

        Toast.makeText(this, idJasa, Toast.LENGTH_LONG).show()

        btn_intent_gallery.setOnClickListener { pickImageFromGallery() }
        btn_konfirmasi_bukti.setOnClickListener {
            when (kategoriTransaksi) {
                "beli" -> simpanPhoto()
                "jasa" -> simpanPhotoJasa()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            filePath = data!!.data!!

            btn_tambah_photo_bukti.setImageURI(data.data) // handle chosen image
            btn_tambah_photo_bukti.layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT
            btn_tambah_photo_bukti.layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT
            btn_tambah_photo_bukti.setPadding(0, 0, 0, 0)
            btn_tambah_photo_bukti.setBackgroundResource(0)
        }
    }

    private fun pickImageFromGallery() {

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"

        val mimeTypes =
            arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(intent, REQUEST_CODE)

    }

    private fun simpanPhoto() {

        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading...")
        progressDialog.show()

        getUrl = ""
        val ref = storageReference.child("bukti/" + UUID.randomUUID().toString())
        val uploadTask = ref.putFile(filePath)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                progressDialog.dismiss()
                val downloadUrl = task.result
                getUrl = downloadUrl.toString()

                queryUpdate("Transaksi",idTransaksi, "url_bukti_pembayaran", getUrl)
                queryUpdate("Transaksi",idTransaksi, "status_beli", "3")

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "dapat url gagal", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun simpanPhotoJasa() {

        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading...")
        progressDialog.show()

        getUrl = ""
        val ref = storageReference.child("bukti/" + UUID.randomUUID().toString())
        val uploadTask = ref.putFile(filePath)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                progressDialog.dismiss()
                val downloadUrl = task.result
                getUrl = downloadUrl.toString()

                queryUpdate("Jasa",idJasa, "url_bukti_pembayaran_jasa", getUrl)
                queryUpdate("Jasa",idJasa, "status_jasa", "3")

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "dapat url gagal", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun queryUpdate(kategori:String,id: String, node: String, value: String) {
        mDatabase.child("Users").child(getUsername)
            .child(kategori)
            .child(id).child(node)
            .setValue(value)

    }
}