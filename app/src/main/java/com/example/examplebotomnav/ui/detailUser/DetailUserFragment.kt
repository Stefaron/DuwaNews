package com.example.examplebotomnav.ui.detailUser

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.examplebotomnav.LoginActivity
import com.example.examplebotomnav.R
import com.example.examplebotomnav.SessionManager
import com.example.examplebotomnav.data.User
import com.example.examplebotomnav.databinding.FragmentDetailUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class DetailUserFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var user: User
    private lateinit var uid: String

//    private lateinit var imagePfp: ImageView

    private var _binding: FragmentDetailUserBinding? = null
    private val binding get() = _binding!!

    companion object {
        var IMAGE_REQUEST_CODE = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(DetailUserViewModel::class.java)

        _binding = FragmentDetailUserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()

        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        if (uid.isNotEmpty()) {
            getUserData()
        }

        binding.profileImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_REQUEST_CODE)
        }

        // Set onClickListener for logout button
        binding.btnLogout.setOnClickListener {
            // Log out from Firebase
            FirebaseAuth.getInstance().signOut()

            // Clear user session using SessionManager
            SessionManager.clearUserSession(requireContext())

            // Navigate to login activity
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        return root
    }

    private fun getUserData() {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val uid = currentUser.uid

            databaseReference.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val email = snapshot.child("email").getValue(String::class.java)
                        val username = snapshot.child("username").getValue(String::class.java)
                        val noTelp = snapshot.child("noTelp").getValue(String::class.java)



                        if (email != null && username != null && noTelp != null) {
                            // Update UI with user data
                            binding.tvNamaUser.text = username
                            binding.tvEmail.text = email
                            binding.tvNoTelp.text = noTelp

                            storageReference = FirebaseStorage.getInstance().getReference("images").child(uid)
                            var file: File = File.createTempFile("pfp", "jpeg")
                            storageReference.getFile(file).
                            addOnSuccessListener {
                                var bitmap: Uri = Uri.fromFile(file)
                                binding.profileImage.setImageURI(bitmap)
                            }
                        } else {
                            // Handle case when user data is incomplete
                            Toast.makeText(requireContext(), "Incomplete user data", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Handle case when user data is not found
                        Toast.makeText(requireContext(), "User data not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database read error
                    Toast.makeText(requireContext(), "Failed to read user data: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            // Handle case when no user is signed in
            Toast.makeText(requireContext(), "No user signed in", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            var fileUri: Uri? = data?.data
            binding.profileImage.setImageURI(fileUri)

            val ref: StorageReference = FirebaseStorage.getInstance().getReference("images").child(uid)

            if (fileUri != null) {
                ref.putBytes(compressImage(fileUri)).addOnSuccessListener {
                    Toast.makeText(context, "Upload Berhasil!", Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    Toast.makeText(context, "Upload Gagal", Toast.LENGTH_LONG).show()
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun compressImage(uri: Uri):ByteArray {
        val stream = ByteArrayOutputStream()
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, uri)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream)
            return stream.toByteArray()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stream.toByteArray()
    }
}
