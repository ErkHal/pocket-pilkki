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
                600f,
                10000f,
                30f,
                140f,
                15

        ))
        species.add(FishSpecies(
                R.string.fishspecies_pike,
                R.string.fishspecies_pike_description,
                R.drawable.trout,
                500f,
                7000f,
                30f,
                120f,
                20
        ))
        species.add(FishSpecies(
                R.string.fishspecies_crawfish,
                R.string.fishspecies_crawfish_description,
                R.drawable.crawfish,
                300f,
                3500f,
                15f,
                75f,
                15
        ))
        species.add(FishSpecies(
                R.string.fishspecies_goldfish,
                R.string.fishspecies_goldfish_description,
                R.drawable.goldfish,
                800f,
                15000f,
                30f,
                135f,
                10
        ))
        species.add(FishSpecies(
                R.string.fishspecies_halibut,
                R.string.fishspecies_halibut_description,
                R.drawable.halibut,
                10000f,
                200000f,
                30f,
                300f,
                13
        ))
        species.add(FishSpecies(
                R.string.fishspecies_killerwhale,
                R.string.fishspecies_killerwhale_description,
                R.drawable.killerwhale,
                1400000f,
                2800000f,
                500f,
                850f,
                3
        ))
        species.add(FishSpecies(
                R.string.fishspecies_kingfish,
                R.string.fishspecies_kingfish_description,
                R.drawable.kingfish,
                2000f,
                75000f,
                20f,
                260f,
                10
        ))
        species.add(FishSpecies(
                R.string.fishspecies_piranha,
                R.string.fishspecies_piranha_description,
                R.drawable.piranha,
                500f,
                4000f,
                10f,
                70f,
                10
        ))
        species.add(FishSpecies(
                R.string.fishspecies_pufferfish,
                R.string.fishspecies_pufferfish_description,
                R.drawable.pufferfish,
                500f,
                15000f,
                10f,
                75f,
                10
        ))
        species.add(FishSpecies(
                R.string.fishspecies_yeltrout,
                R.string.fishspecies_yeltrout_description,
                R.drawable.yeltrout,
                1000f,
                30000f,
                30f,
                140f,
                15
        ))
        species.add(FishSpecies(
                R.string.fishspecies_shark,
                R.string.fishspecies_shark_description,
                R.drawable.shark,
                600000f,
                2200000f,
                200f,
                700f,
                6
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