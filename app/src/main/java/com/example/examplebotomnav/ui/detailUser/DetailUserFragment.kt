package com.example.examplebotomnav.ui.detailUser

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.examplebotomnav.LoginActivity
import com.example.examplebotomnav.R
import com.example.examplebotomnav.SessionManager
import com.example.examplebotomnav.databinding.ActivityRegistBinding
import com.example.examplebotomnav.databinding.FragmentNotificationsBinding
import com.google.firebase.auth.FirebaseAuth

class DetailUserFragment : Fragment() {

    private lateinit var auth: FirebaseAuth


    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(DetailUserViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize Firebase Auth
        val auth = FirebaseAuth.getInstance()

        // Menambahkan onClickListener pada button logout
        binding.btnLogout.setOnClickListener {
            // Melakukan logout dari Firebase
            FirebaseAuth.getInstance().signOut()

            // Clear user session using SessionManager
            SessionManager.clearUserSession(requireContext())

            // Melakukan navigasi ke halaman login
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
        return root
    }

    private fun clearUserSession() {
        val sharedPreferences = activity?.getSharedPreferences("UserSession", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.clear()
        editor?.apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}