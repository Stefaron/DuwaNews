package com.example.examplebotomnav.home

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.widget.SearchView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.examplebotomnav.DetailNews
import com.example.examplebotomnav.R
import com.example.examplebotomnav.databinding.ActivityMainBinding
import com.example.examplebotomnav.newsAdapter.AdapterMain
import com.example.examplebotomnav.newsAdapter.ApiClient
import com.example.examplebotomnav.newsAdapter.ResponseNews
import com.example.examplebotomnav.newsAdapter.ResultsItem
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var rvNews: RecyclerView
    private lateinit var adapterMain: AdapterMain
    private val list = ArrayList<ResultsItem>()

    private lateinit var searchView: SearchView

    private lateinit var binding: ActivityMainBinding

    private fun closeSearchView() {
        // Tutup search view
        searchView.onActionViewCollapsed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvNews = findViewById(R.id.rv_search_results)
        adapterMain = AdapterMain(list, this)
        rvNews.adapter = adapterMain

        val layoutManager = LinearLayoutManager(this) // Change to desired layout manager
        rvNews.layoutManager = layoutManager

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val recyclerView = findViewById<RecyclerView>(R.id.rv_search_results)
        val fragmentContainer = findViewById<FrameLayout>(R.id.nav_host_fragment_activity_main)
        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        if (searchItem != null) {
            searchView = MenuItemCompat.getActionView(searchItem) as SearchView
            searchView.isIconified = false
            searchView.setOnCloseListener {
                // Panggil method untuk menutup search view
                closeSearchView()
                recyclerView.visibility = View.INVISIBLE
                fragmentContainer.visibility = View.VISIBLE
                // Kembalikan nilai true untuk menandakan bahwa event sudah ditangani
                true
            }

            val searchPlate = searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
            searchPlate.hint = "Search"
            val searchPlateView: View =
                searchView.findViewById(androidx.appcompat.R.id.search_plate)
            searchPlateView.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    android.R.color.transparent
                )
            )

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        getSearchNews(query)

                        recyclerView.visibility = View.VISIBLE
                        fragmentContainer.visibility = View.INVISIBLE
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })

            val searchManager =
                getSystemService(Context.SEARCH_SERVICE) as SearchManager
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }
        return super.onCreateOptionsMenu(menu)
    }

    fun getSearchNews(query: String) {
        ApiClient.apiServise.searchNews(
            apikey = "pub_441345579ea14058b12ed8aad247be22ecbd4",
            q = query,
            country = "id",
        ).enqueue(object : retrofit2.Callback<ResponseNews> {

            override fun onResponse(
                call: Call<ResponseNews>,
                response: Response<ResponseNews>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        setDataToAdapter(data.results)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseNews>, t: Throwable) {
                t.message?.let { Log.e("Failure", it) }
            }

        })
    }

    fun setDataToAdapter(data: List<ResultsItem?>?) {
        val list = ArrayList(data)
        adapterMain.setData(list)
    }
}