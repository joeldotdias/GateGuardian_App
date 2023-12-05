package com.example.gateguardianapp.data.cloud

import android.content.Context
import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class FirebaseCloudClient {

    companion object {
        fun uploadToCloud(uri: Uri, name: String, type: String, context: Context) {
            val storage = Firebase.storage
            val storageRef = storage.reference
            val uploadRef = storageRef.child("images/$type/$name.jpg")

            val byteArray = context.contentResolver
                .openInputStream(uri)?.use { stream ->
                    stream.readBytes()
                }

            byteArray?.let {
                uploadRef.putBytes(byteArray)
            }
        }

        fun downloadImageUrl(name: String, type: String): Uri? {
            var imageUrl: Uri? = null
            val storageRef = Firebase.storage.reference
            storageRef.child("images/$type/$name.jpg").downloadUrl
                .addOnSuccessListener { url ->
                    imageUrl = url
                }
            return imageUrl
        }
    }
}