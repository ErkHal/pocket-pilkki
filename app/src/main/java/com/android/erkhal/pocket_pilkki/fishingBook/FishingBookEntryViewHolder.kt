package com.android.erkhal.pocket_pilkki.fishingBook

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.android.erkhal.pocket_pilkki.R
import com.android.erkhal.pocket_pilkki.global.GlobalFishSpecies
import com.android.erkhal.pocket_pilkki.model.CaughtFish
import kotlinx.android.synthetic.main.fishing_book_entry.view.*

class FishingBookEntryViewHolder(var view: View): RecyclerView.ViewHolder(view) {

    init {
        // Define click listener for the ViewHolder's View.
        view.setOnClickListener { Log.d("asdfg", "Element $adapterPosition clicked.") }
    }

    fun assignValues(fish: CaughtFish, context: Context) {
        view.fish_entry_species.text = context.getString(fish.species)
        view.fish_measurements.text = context.getString(
                R.string.fish_measurements, fish.getFishLength(), fish.getFishWeight())
        view.fish_caught_date.text = context.getString(R.string.fish_caught_on, fish.getFishCaughtDate().toString())
        view.fish_image.setImageDrawable(context.getDrawable(GlobalFishSpecies.getImageResource(fish)))
    }
}