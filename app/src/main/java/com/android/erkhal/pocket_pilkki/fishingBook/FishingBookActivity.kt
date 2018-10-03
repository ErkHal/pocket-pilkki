package com.android.erkhal.pocket_pilkki.fishingBook

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.android.erkhal.pocket_pilkki.DatabaseUtils.Utils
import com.android.erkhal.pocket_pilkki.R
import kotlinx.android.synthetic.main.fishing_book_activity.*

class FishingBookActivity: Activity() {
    private lateinit var fishingBookLayoutManager: RecyclerView.LayoutManager
    private lateinit var fishingBookAdapter: CaughtFishAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fishing_book_activity)

        //Hide status bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val allCaughtFish = Utils.getAllCaughtFish(this)
        fishingBookAdapter = CaughtFishAdapter(this, allCaughtFish)
        fishingBookLayoutManager = GridLayoutManager(this, 2)

        fish_recycler_view.apply {
                    adapter = fishingBookAdapter
                    layoutManager = fishingBookLayoutManager
                    setHasFixedSize(true)
                }
    }
}