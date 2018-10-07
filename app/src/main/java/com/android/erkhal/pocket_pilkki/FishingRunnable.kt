package com.android.erkhal.pocket_pilkki

import android.content.Context
import android.util.Log
import java.util.*

//Constants for RNG
const val MAX_RANDOM_VALUE = 100
const val RNG_COOLDOWN = 3000

/**
 * This class represents a runnable that generates RNG numbers, and notifies the main thread when
 * a number exceeds the threshold that is defined with the gnawingProbability parameter.
 * In this use case, it is used to simulate a probability of a fish starting to gnaw the bait
 * placed in the water.
 */
class FishingRunnable(private val context: OnFishGnawingListener, private val gnawingProbability: Int): Runnable {

    //This boolean is used to exit out of the RNG loop
    private var fishingModeRunning = true

    interface OnFishGnawingListener {
        fun onFishGnawing()
    }

    override fun run() {
        var lastTime = Date()

        while(fishingModeRunning) {
            if(Date().time - lastTime.time > RNG_COOLDOWN) {
                val rand = Random().nextInt(MAX_RANDOM_VALUE)
                Log.d("ränd", "$rand")

                if(rand <= gnawingProbability) {
                    context.onFishGnawing()
                }
                lastTime = Date()
            }
        }
    }

    fun quit() {
        fishingModeRunning = false
        Log.d("FishingRunnable", "Quitting fishing mode")
    }
}