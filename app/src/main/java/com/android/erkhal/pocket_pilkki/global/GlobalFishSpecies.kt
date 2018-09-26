package com.android.erkhal.pocket_pilkki.global

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.provider.Settings.Global.getString
import android.util.Log
import com.android.erkhal.pocket_pilkki.R
import com.android.erkhal.pocket_pilkki.model.FishSpecies
import com.android.erkhal.pocket_pilkki.persistence.CaughtFish
import java.util.*
import kotlin.collections.ArrayList

object GlobalFishSpecies {

    var species = ArrayList<FishSpecies>()

    init {

        // Initialize all species of fish here

        species.add(FishSpecies(
                "Salmon",
                "This is a description of salmon",
                400f,
                10000f,
                30f,
                140f,
                2

        ))
        species.add(FishSpecies(
                "Pike",
                "This is a description of pike",
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
}