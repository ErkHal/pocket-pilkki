package com.android.erkhal.pocket_pilkki.model

data class FishSpecies(
        val speciesName: String,
        val description: String,
        val minWeight: Float,
        val maxWeight: Float,
        val minLength: Float,
        val maxLength: Float,
        val probability: Int) {

    override fun toString(): String {
        return speciesName
    }
}