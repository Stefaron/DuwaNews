package com.example.examplebotomnav.kategori

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.examplebotomnav.databinding.FragmentDuwaSportBinding
import com.example.examplebotomnav.kategori.adapterKategori.SportAdapter
import com.example.examplebotomnav.kategori.responseKategori.ResultsItem
import com.example.examplebotomnav.kategori.responseKategori.SportResponse
import com.example.examplebotomnav.newsAdapter.ApiClient
import com.example.examplebotomnav.newsAdapter.ResponseNews
import retrofit2.Call
import retrofit2.Response

class DuwaSportFragment : Fragment() {
    private lateinit var adapter: SportAdapter
    private val list = ArrayList<ResultsItem>()

    private var _binding: FragmentDuwaSportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDuwaSportBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = context?.let { SportAdapter(list, it) }!!

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvSport.layoutManager = layoutManager
        binding.rvSport.adapter = adapter

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        getNews()
        return root
    }

    private fun getNews() {
        ApiClient.apiServise.getCategoryNewsData(
            apikey = "pub_441345579ea14058b12ed8aad247be22ecbd4",
            country = "id",
            category = "sports"
        ).enqueue(object : retrofit2.Callback<SportResponse> {

            override fun onResponse(call: Call<SportResponse>, response: Response<SportResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        setDataToAdapter(data.results)
                    }
                }
            }

            override fun onFailure(call: Call<SportResponse>, t: Throwable) {
                t.message?.let { Log.e("Failure", it) }
            }

        })
    }

    private fun setDataToAdapter(data: List<ResultsItem?>?) {
        val list = ArrayList(data)
        adapter.setData(list)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
