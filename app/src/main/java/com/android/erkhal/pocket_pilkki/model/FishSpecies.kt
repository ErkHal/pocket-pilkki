package com.android.erkhal.pocket_pilkki.model

data class FishSpecies(
        val speciesName: Int,
        val description: Int,
        val imageResource: Int,
        val minWeight: Float,
        val maxWeight: Float,
        val minLength: Float,
        val maxLength: Float,
        val probability: Int)