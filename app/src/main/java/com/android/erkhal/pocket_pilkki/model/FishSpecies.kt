package com.android.erkhal.pocket_pilkki.model

data class FishSpecies(val speciesName: String, val description: String) {

    override fun toString(): String {
        return speciesName
    }
}