package com.android.erkhal.pocket_pilkki.persistence

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import java.sql.Date
import java.util.*

@Entity
data class CaughtFish(

        @PrimaryKey(autoGenerate = true) val uuid: Long,

        val species: Int,

        val length: Float,
        val weight: Float,
        val caughtTimestamp: Long) {

    override fun toString(): String {
        return "Length: $length, Weight: $weight, Caught on: ${getCaughtDate()}"
    }

    fun getCaughtDate(): Date {
        return Date(caughtTimestamp)
    }
}