package com.android.erkhal.pocket_pilkki.fishingBook

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.android.erkhal.pocket_pilkki.R
import com.android.erkhal.pocket_pilkki.model.CaughtFish
import com.android.erkhal.pocket_pilkki.persistence.FishDatabase
import kotlinx.android.synthetic.main.fishing_book_activity.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking

class FishingBookActivity: Activity() {
    private lateinit var fishingBookLayoutManager: RecyclerView.LayoutManager
    private lateinit var fishingBookAdapter: CaughtFishAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fishing_book_activity)

        //Hide status bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val allCaughtFish = asyncGetAllCaughtFish()
        fishingBookAdapter = CaughtFishAdapter(this, allCaughtFish)
        fishingBookLayoutManager = GridLayoutManager(this, 2)

        fish_recycler_view.apply {
                    adapter = fishingBookAdapter
                    layoutManager = fishingBookLayoutManager
                    setHasFixedSize(true)
                }
    }

    /*
        Below are the functions used to fetch all caught fish from the database.
        It uses two functions because I didn't find out a way to execute the async coroutine
        without having to call the .await() for getting the results, and this call is only possible
        from another coroutine.
     */

    private fun asyncGetAllCaughtFish(): Array<CaughtFish> {
        return runBlocking {
        executeGetAllQuery().await()
        }
    }

    private fun executeGetAllQuery(): Deferred<Array<CaughtFish>> {
        return async(CommonPool) {
            val db = FishDatabase.get(applicationContext)
            db.caughtFishDao().getAll()
        }
    }
}