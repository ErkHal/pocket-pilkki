package com.android.erkhal.pocket_pilkki.DatabaseUtils

import android.content.Context
import com.android.erkhal.pocket_pilkki.R
import com.android.erkhal.pocket_pilkki.global.GlobalFishSpecies
import com.android.erkhal.pocket_pilkki.model.CaughtFish
import com.android.erkhal.pocket_pilkki.model.FishSpecies
import com.android.erkhal.pocket_pilkki.persistence.FishDatabase
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import java.util.*

class Utils {

    companion object {

        fun getAllCaughtFish(context: Context): List<CaughtFish> {
            return asyncGetAllCaughtFish(context)
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

            GlobalFishSpecies.species.forEach {
                val amount = it.probability
                for(i in 1..amount) {
                    probabilityArray.add(it)
                }
            }

            return probabilityArray[Random().nextInt(probabilityArray.size - 1)]
        }

        // Retrieve the image resource number for the chosen species of fish
        fun getImageResource(fish: CaughtFish): Int {
            return GlobalFishSpecies.species.find { it.speciesName == fish.species }?.imageResource ?: R.drawable.fish_pike
        }

        fun isCaught(wantedSpecies: FishSpecies, context: Context): Boolean {
            val allCaughtFish = asyncGetAllCaughtFish(context)
            return allCaughtFish.any { caughtFish ->
                caughtFish.species == wantedSpecies.speciesName
            }
        }

        /*
       Below are the functions used to fetch all caught fish from the database.
       It uses two functions because I didn't figure out a way to execute the async coroutine
       without having to call the .await() for getting the results, and this call is only possible
       from another coroutine.
        */
        private fun asyncGetAllCaughtFish(context: Context): List<CaughtFish> {

            fun executeGetAllQuery(context: Context): Deferred<List<CaughtFish>> {
                return async(CommonPool) {
                    val db = FishDatabase.get(context)
                    db.caughtFishDao().getAll()
                }
            }

            return runBlocking {
                executeGetAllQuery(context).await()
            }
        }
    }
}