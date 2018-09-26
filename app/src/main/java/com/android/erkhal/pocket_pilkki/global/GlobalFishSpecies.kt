package com.android.erkhal.pocket_pilkki.global

import android.content.Context
import com.android.erkhal.pocket_pilkki.R
import com.android.erkhal.pocket_pilkki.model.FishSpecies

class GlobalFishSpecies(context: Context) {

    companion object {
        var species = ArrayList<FishSpecies>()
    }

    init {
        species.add(FishSpecies(
                context.getString(R.string.fishspecies_salmon),
                context.getString(R.string.fishspecies_salmon_description)
        ))
        species.add(FishSpecies(
                context.getString(R.string.fishspecies_pike),
                context.getString(R.string.fishspecies_pike_description)
        ))
    }
}