package com.example.examplebotomnav.kategori

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.examplebotomnav.databinding.FragmentDuwaHealthBinding
import com.example.examplebotomnav.kategori.adapterKategori.HealthAdapter
import com.example.examplebotomnav.kategori.responseKategori.AllResponse
import com.example.examplebotomnav.kategori.responseKategori.ResultsItem
import com.example.examplebotomnav.newsAdapter.ApiClient
import retrofit2.Call
import retrofit2.Response

class DuwaHealthFragment : Fragment() {
    private lateinit var adapter: HealthAdapter
    private val list = ArrayList<ResultsItem>()

    private var _binding: FragmentDuwaHealthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDuwaHealthBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = context?.let { HealthAdapter(list, it) }!!

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
            category = "health"
        ).enqueue(object : retrofit2.Callback<AllResponse> {
            override fun onResponse(call: Call<AllResponse>, response: Response<AllResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        setDataToAdapter(data.results)
                    }
                }
            }

            override fun onFailure(call: Call<AllResponse>, t: Throwable) {
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
