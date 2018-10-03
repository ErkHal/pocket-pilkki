package com.android.erkhal.pocket_pilkki.fishCodex

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.android.erkhal.pocket_pilkki.R
import com.android.erkhal.pocket_pilkki.global.GlobalFishSpecies
import kotlinx.android.synthetic.main.fishing_book_activity.*

class FishCodexActivity: Activity() {
    private lateinit var codexLayoutManager: RecyclerView.LayoutManager
    private lateinit var codexAdapter: FishSpeciesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fishing_book_activity)

        //Hide status bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val fishDataset = GlobalFishSpecies.species.toList()
        codexAdapter = FishSpeciesAdapter(this, fishDataset)
        codexLayoutManager = LinearLayoutManager(this)

        fish_recycler_view.apply {
            adapter = codexAdapter
            layoutManager = codexLayoutManager
            setHasFixedSize(true)
        }
    }
}