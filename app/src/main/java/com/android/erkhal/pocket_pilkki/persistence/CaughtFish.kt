package com.android.erkhal.pocket_pilkki.persistence

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.sql.Date

@Entity
data class CaughtFish(

        @PrimaryKey(autoGenerate = true) val uuid: Long,

        val species: String,

        val length: Float,
        val weight: Float,
        var caughtTimestamp: Long?) {

    override fun toString(): String {
        return "Species: $species Length: ${length}cm Weight: ${weight}g Caught on: ${getCaughtDate()}"
    }

    fun toMeasurementsString(): String {
        return "Length: ${length}cm \n Weight: ${weight}g"
    }

    fun getCaughtDate(): Date {
        return Date(caughtTimestamp ?: 12345)
    }
}