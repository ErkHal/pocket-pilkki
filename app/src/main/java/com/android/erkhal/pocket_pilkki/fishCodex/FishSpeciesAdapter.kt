package com.android.erkhal.pocket_pilkki.fishCodex

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.erkhal.pocket_pilkki.R
import com.android.erkhal.pocket_pilkki.global.GlobalFishSpecies
import com.android.erkhal.pocket_pilkki.model.FishSpecies

class FishSpeciesAdapter(private val context: Context, private val speciesDataset: List<FishSpecies>): RecyclerView.Adapter<FishCodexViewHolder>() {

    override fun getItemCount() = speciesDataset.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FishCodexViewHolder {
        val fishCodexEntryView = LayoutInflater.from(parent.context).inflate(R.layout.fishing_book_entry, parent, false)
        return FishCodexViewHolder(fishCodexEntryView)
    }

    override fun onBindViewHolder(viewHolder: FishCodexViewHolder, position: Int) {
        viewHolder.assignValues(GlobalFishSpecies.species.toList()[position], context)
    }
}