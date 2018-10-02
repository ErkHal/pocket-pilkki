package com.android.erkhal.pocket_pilkki.model

import android.net.Uri

data class FishSpecies(
        val speciesName: Int,
        val description: Int,
        val imageResource: Int,
        val modelFilepath: Uri,
        val minWeight: Float,
        val maxWeight: Float,
        val minLength: Float,
        val maxLength: Float,
        val probability: Int)