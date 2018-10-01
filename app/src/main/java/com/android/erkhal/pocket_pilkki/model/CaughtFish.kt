package com.android.erkhal.pocket_pilkki.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.android.erkhal.pocket_pilkki.R
import java.sql.Date

@Entity
data class CaughtFish(

        @PrimaryKey(autoGenerate = true) val uuid: Long,

        val species: Int,

        val length: Float,
        val weight: Float,
        var caughtTimestamp: Long?) {

    override fun toString(): String {
        return "Species: $species Length: ${length}cm Weight: ${weight}g Caught on: ${getFishCaughtDate()}"
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