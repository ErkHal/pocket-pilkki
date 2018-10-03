package com.android.erkhal.pocket_pilkki.fishingBook

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.erkhal.pocket_pilkki.R
import com.android.erkhal.pocket_pilkki.model.CaughtFish

class CaughtFishAdapter(private val context: Context, private var fishArray: List<CaughtFish>):
        RecyclerView.Adapter<FishingBookEntryViewHolder>() {

    override fun getItemCount() = fishArray.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FishingBookEntryViewHolder {
        val bookEntryView = LayoutInflater.from(parent.context).inflate(R.layout.fishing_book_entry, parent, false)
        return FishingBookEntryViewHolder(bookEntryView, context)
    }

    override fun onBindViewHolder(holder: FishingBookEntryViewHolder, position: Int) {
        holder.assignValues(fishArray[position], context)
    }

}