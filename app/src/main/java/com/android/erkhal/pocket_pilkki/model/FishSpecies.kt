package com.android.erkhal.pocket_pilkki.model

import android.net.Uri

/**
 *  This data class represents a species of fish that is used in the game
 *  The icon and the 3D model of all fish in the game is always tied to
 *  a certain species of fish. Since this data is not changed or user-spesific,
 *  it is not stored into a database.
 */
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