package com.android.erkhal.pocket_pilkki.global

import com.android.erkhal.pocket_pilkki.R
import com.android.erkhal.pocket_pilkki.model.FishSpecies
import com.android.erkhal.pocket_pilkki.model.CaughtFish
import java.util.*
import kotlin.collections.ArrayList

object GlobalFishSpecies {

    var species = ArrayList<FishSpecies>()

    init {

        // Initialize all species of fish here

        species.add(FishSpecies(
                R.string.fishspecies_salmon,
                R.string.fishspecies_salmon_description,
                R.drawable.trout2,
                400f,
                10000f,
                30f,
                140f,
                2

        ))
        species.add(FishSpecies(
                R.string.fishspecies_pike,
                R.string.fishspecies_pike_description,
                R.drawable.trout,
                300f,
                7000f,
                30f,
                120f,
                4
        ))

    }

    fun getRandomizedFish(): CaughtFish {
        val randomFishSpecies = getRandomFishSpecies()
        val fishWeight = randomFishSpecies.minWeight + (Random().nextInt(randomFishSpecies.maxWeight.toInt()))
        val fishLength = randomFishSpecies.minLength + (Random().nextInt(randomFishSpecies.maxLength.toInt()))

        return CaughtFish(0, randomFishSpecies.speciesName, fishLength, fishWeight, null)
    }

    // Randomization function utilizing probabilities
    private fun getRandomFishSpecies(): FishSpecies {

        val probabilityArray = ArrayList<FishSpecies>()

        species.forEach {
            val amount = it.probability
            for(i in 1..amount) {
                probabilityArray.add(it)
            }
        }

        return probabilityArray[Random().nextInt(probabilityArray.size - 1)]
    }

    // Retrieve the image resource number for the chosen species of fish
    fun getImageResource(fish: CaughtFish): Int {
        var speciesResourceNumber: Int? = null
        species.forEach {
            if(fish.species == it.speciesName) {
                speciesResourceNumber = it.imageResource
            }
        }
        return speciesResourceNumber ?: R.drawable.fish_pike
    }
}