package com.example.examplebotomnav.home

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
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
import com.example.examplebotomnav.R
import com.example.examplebotomnav.databinding.ActivityMainBinding
import com.example.examplebotomnav.newsAdapter.ApiClient
import com.example.examplebotomnav.newsAdapter.ResponseNews
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {

    private lateinit var rvNews: RecyclerView
    private lateinit var adapterMain: AdapterMain
    private val list = ArrayList<News>()

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

        adapterMain = AdapterMain(this@MainActivity, arrayListOf())

        binding.

        rvNews = findViewById(R.id.rv_rekom)
        rvNews.setHasFixedSize(true)



        list.addAll(getListHeroes())
        showRecyclerList()

    }

    private fun getListHeroes(): ArrayList<News> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.getStringArray(R.array.data_photo)
        val listHero = ArrayList<News>()
        for (i in dataName.indices) {
            val hero = News(dataName[i], dataDescription[i], dataPhoto[i])
            listHero.add(hero)
        }
        return listHero
    }

    private fun showRecyclerList() {
        rvNews.layoutManager = LinearLayoutManager(this)
        val listHeroAdapter = AdapterMain(list)
        rvNews.adapter = listHeroAdapter

        listHeroAdapter.setOnItemClickCallback(object : AdapterMain.OnItemClickCallback {
            override fun onItemClicked(data: News) {
                showSelectedHero(data)
            }
        })
    }

    private fun showSelectedHero(hero: News) {
        Toast.makeText(this, "Kamu memilih " + hero.name, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        if (searchItem != null) {
            searchView = MenuItemCompat.getActionView(searchItem) as SearchView
            searchView.setOnCloseListener {
                // Panggil method untuk menutup search view
                closeSearchView()
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
                    // do your logic here
                    // Toast.makeText(applicationContext, query, Toast.LENGTH_SHORT).show()
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

    fun remoteGetUser(){
        ApiClient.apiServise.getCurrentNewsData(country = "id", apikey = "pub_441345579ea14058b12ed8aad247be22ecbd4&").enqueue(object : Callback<ArrayList<ResponseNews>>{
            override fun onResponse(
                call: Call<ArrayList<ResponseNews>>,
                response: Response<ArrayList<ResponseNews>>
            ){
                if (response.isSuccessful){
                    val data = response.body()
                    if (data != null) {
                        setDataToAdapter(data)
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<ResponseNews>>, t: Throwable){
                Log.d("Error", ""+ t.stackTraceToString())
            }

        })
    }

    fun setDataToAdapter(data: ArrayList<ResponseNews>){
        adapterMain.setData(data)
    }
}