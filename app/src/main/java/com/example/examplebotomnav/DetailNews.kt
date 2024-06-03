package com.example.examplebotomnav

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.example.examplebotomnav.databinding.ActivityDetailNewsBinding
import com.example.examplebotomnav.detailNews.DetailResponse
import com.example.examplebotomnav.detailNews.ResultsItem
import com.example.examplebotomnav.newsAdapter.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailNews : AppCompatActivity() {

    private lateinit var binding: ActivityDetailNewsBinding

    companion object {
        const val EXTRA_ARTICLE_ID = "article_id"
        const val TAG = "DetailNews"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val articleId = intent.getStringExtra(EXTRA_ARTICLE_ID)

        articleId?.let {
            fetchArticleDetails(it)
            showProgressBar()
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun fetchArticleDetails(articleId: String) {
        val call = ApiClient.apiServise.getDetailNewsData(apikey = "pub_441345579ea14058b12ed8aad247be22ecbd4", id = articleId)

        call.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(call: Call<DetailResponse>, response: Response<DetailResponse>) {
                if (response.isSuccessful) {
                    val detailResponse = response.body()
                    detailResponse?.results?.let { results ->
                        // Assuming there is only one result with the given articleId
                        val article = results.firstOrNull { it?.articleId == articleId }
                        article?.let { bindArticleDetails(it) }
                        hideProgressBar()
                    }
                } else {
                    Log.e(TAG, "Failed to fetch article: ${response.errorBody()?.string()}")
                    hideProgressBar()
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                Log.e(TAG, "Error fetching article", t)
                hideProgressBar()
            }
        })
    }

    private fun bindArticleDetails(article: ResultsItem) {
        binding.apply {
            titleDetail.text = article.title
            descDetail.text = article.description
            tglDetail.text = article.pubDate
            Glide.with(this@DetailNews)
                .load(article.imageUrl)
                .into(detailImage)
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }
}