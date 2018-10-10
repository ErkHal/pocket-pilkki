package com.android.erkhal.pocket_pilkki.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.sql.Date

/**
 * This class represents a fish that is caught by the player.
 * It gets the icon and the model from the resource identifier of that fish's species
 */
@Entity
data class CaughtFish(

        @PrimaryKey(autoGenerate = true) val uuid: Long,

        val species: Int,

        val length: Float,
        val weight: Float,
        var location: String?,
        var caughtTimestamp: Long?) {

    override fun toString(): String {
        return "Species: $species Length: ${length}cm Weight: ${weight}kg Caught on: ${getFishCaughtDate()}"
    }

    fun getFishLength(): String {
        return length.toString()
    }

    fun getFishWeight(): String {
        return weight.toString()
    }

    fun getFishCaughtDate(): Date {
        return Date(caughtTimestamp ?: 12345)
    }
}