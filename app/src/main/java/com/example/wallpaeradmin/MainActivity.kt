package com.example.wallpaeradmin

import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wallpaeradmin.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainActivity : AppCompatActivity() {

    lateinit var uri: Uri
    lateinit var reference: DatabaseReference
    lateinit var storageRef: StorageReference


    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        reference = FirebaseDatabase.getInstance().reference
        storageRef = FirebaseStorage.getInstance().reference

        binding.uploadId.setOnClickListener {

            var intent = Intent(ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 1)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
                uri = data?.data!!
                Log.e(TAG, "onActivityResult: ========" + uri.lastPathSegment)

                val ref = storageRef.child("images/${uri.lastPathSegment}.jpg")
                var uploadTask = ref.putFile(uri!!)

                val urlTask = uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    ref.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result

                        var key = reference.root.push().key
                        var data = WallpaperModel(downloadUri.toString(),key!!)
                        reference.root.child("Images").child(key.toString()).setValue(data)

                    } else {
                        // Handle failures
                        // ...
                    }
                }

            }
        }
    }
}