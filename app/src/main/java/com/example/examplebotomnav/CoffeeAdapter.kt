package com.example.examplebotomnav

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction

class CoffeeAdapter(private val coffeeItems: ArrayList<CoffeeItem>, private val context: Context) :
    RecyclerView.Adapter<CoffeeAdapter.ViewHolder>() {
    private var favDB: Favorite? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        favDB = Favorite(context)
        // Create table on first start
        val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val firstStart = prefs.getBoolean("firstStart", true)
        if (firstStart) {
            createTableOnFirstStart()
        }
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.lay_berita,
            parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val coffeeItem = coffeeItems[position]
        readCursorData(coffeeItem, holder)
        holder.imageView.setImageResource(coffeeItem.imageResourse)
        holder.titleTextView.text = coffeeItem.title
    }

    override fun getItemCount(): Int {
        return coffeeItems.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.img_news)
        var titleTextView: TextView = itemView.findViewById(R.id.tvw_title)
        var likeCountTextView: TextView = itemView.findViewById(R.id.desc_detail)
        var favBtn: Button = itemView.findViewById(R.id.favBtn)

        init {
            // Add to fav button
            favBtn.setOnClickListener {
                val position = adapterPosition
                val coffeeItem = coffeeItems[position]
                likeClick(coffeeItem, favBtn, likeCountTextView)
            }
        }
    }

    private fun createTableOnFirstStart() {
        Favorite.insertEmpty()
        val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean("firstStart", false)
        editor.apply()
    }

    @SuppressLint("Range")
    private fun readCursorData(coffeeItem: CoffeeItem, viewHolder: ViewHolder) {
        val db: Unit = Favorite.getReadableDatabase()
        val cursor: Unit = Favorite.read_all_data(coffeeItem.key_id)
        try {
            while (cursor.moveToNext()) {
                val item_fav_status = cursor.getString(cursor.getColumnIndex(Favorite.FAVORITE_STATUS))
                coffeeItem.favStatus = item_fav_status

                // Check fav status
                if (item_fav_status != null && item_fav_status == "1") {
                    viewHolder.favBtn.setBackgroundResource(R.drawable.baseline_favorite_red)
                } else if (item_fav_status != null && item_fav_status == "0") {
                    viewHolder.favBtn.setBackgroundResource(R.drawable.baseline_favorite_gray)
                }
            }
        } finally {
            if (cursor != null && !cursor.isClosed) cursor.close()
            db.close()
        }
    }

    // Like click
    private fun likeClick(coffeeItem: CoffeeItem, favBtn: Button, textLike: TextView) {
        val refLike = FirebaseDatabase.getInstance().reference.child("likes")
        val upvotesRefLike = refLike.child(coffeeItem.key_id!!)
        if (coffeeItem.favStatus == "0") {
            coffeeItem.favStatus = "1"
            coffeeItem.title?.let {
                Favorite.insertIntoTheDatabase(
                    it, coffeeItem.imageResourse,
                    coffeeItem.key_id!!, coffeeItem.favStatus!!
                )
            }
            favBtn.setBackgroundResource(R.drawable.baseline_favorite_red)
            favBtn.isSelected = true
            upvotesRefLike.runTransaction(object : Transaction.Handler {
                override fun doTransaction(mutableData: MutableData): Transaction.Result {
                    val currentValue = mutableData.getValue(Int::class.java)
                    mutableData.value = (currentValue ?: 0) + 1
                    Handler(Looper.getMainLooper()).post {
                        textLike.text = mutableData.value.toString()
                    }
                    return Transaction.success(mutableData)
                }

                override fun onComplete(
                    databaseError: DatabaseError?,
                    b: Boolean,
                    dataSnapshot: DataSnapshot?
                ) {
                    println("Transaction completed")
                }
            })
        } else if (coffeeItem.favStatus == "1") {
            coffeeItem.favStatus = "0"
            Favorite.remove_fav(coffeeItem.key_id!!)
            favBtn.setBackgroundResource(R.drawable.baseline_favorite_gray)
            favBtn.isSelected = false
            upvotesRefLike.runTransaction(object : Transaction.Handler {
                override fun doTransaction(mutableData: MutableData): Transaction.Result {
                    val currentValue = mutableData.getValue(Int::class.java)
                    mutableData.value = (currentValue ?: 0) - 1
                    Handler(Looper.getMainLooper()).post {
                        textLike.text = mutableData.value.toString()
                    }
                    return Transaction.success(mutableData)
                }

                override fun onComplete(
                    databaseError: DatabaseError?,
                    b: Boolean,
                    dataSnapshot: DataSnapshot?
                ) {
                    println("Transaction completed")
                }
            })
        }
    }
}
