package com.android.erkhal.pocket_pilkki

import android.util.Log
import java.util.*

const val MAX_RANDOM_VALUE = 100
const val RNG_COOLDOWN = 3000

class FishingRunnable(ctx: OnFishGnawingListener, gnawProbability: Int): Runnable {

    private var context = ctx
    var fishingModeRunning = true

    private var probability = gnawProbability

    interface OnFishGnawingListener {
        fun onFishGnawing()
    }

    override fun run() {
        var lastTime = Date()

        while(fishingModeRunning) {
            if(Date().time - lastTime.time > RNG_COOLDOWN) {
                val rand = Random().nextInt(MAX_RANDOM_VALUE)
                Log.d("ränd", "$rand")

                if(rand <= probability) {
                    context.onFishGnawing()
                }
                lastTime = Date()
            }
        }
    }

    fun quit() {
        fishingModeRunning = false
        Log.d("kalastus", "Quitting fishing mode")
    }
}