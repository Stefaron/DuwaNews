package com.example.examplebotomnav.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.examplebotomnav.databinding.ActivityMainBinding
import com.example.examplebotomnav.databinding.FragmentHomeBinding
import com.example.examplebotomnav.home.AdapterMain
import com.example.examplebotomnav.home.News
import com.example.examplebotomnav.newsAdapter.ApiClient
import com.example.examplebotomnav.newsAdapter.ResponseNews
import com.example.examplebotomnav.newsAdapter.ResultsItem
import retrofit2.Call
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var rvNews: RecyclerView
    private lateinit var adapterMain: AdapterMain
    private val list = ArrayList<ResultsItem>()

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapterMain = AdapterMain(list)

        val layoutmanager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvRekom.layoutManager = layoutmanager
        binding.rvRekom.adapter = adapterMain


        getNews()
        return root
    }

//    private fun showSelectedHero(hero: News) {
//        Toast.makeText(this, "Kamu memilih " + hero.name, Toast.LENGTH_SHORT).show()
//    }

    fun getNews(){
        ApiClient.apiServise.getCurrentNewsData(apikey = "pub_441345579ea14058b12ed8aad247be22ecbd4", country = "id")
            .enqueue(object : retrofit2.Callback<ResponseNews>{

                override fun onResponse(
                    call: Call<ResponseNews>,
                    response: Response<ResponseNews>
                ) {
                    if (response.isSuccessful){
                        val data = response.body()
                        if (data != null){
                            setDataToAdapter(data.results)
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseNews>, t: Throwable) {
                    t.message?.let { Log.e("Failure", it) }
                }

            })
    }

    fun setDataToAdapter(data: List<ResultsItem?>?){
        val list = ArrayList(data)
        adapterMain.setData(list)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}