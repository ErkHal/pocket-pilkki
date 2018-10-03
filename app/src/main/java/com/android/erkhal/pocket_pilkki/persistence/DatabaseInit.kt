package com.android.erkhal.pocket_pilkki.persistence

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.android.erkhal.pocket_pilkki.model.CaughtFish

@Database(entities = [(CaughtFish::class)],
        version = 1, exportSchema = false)
abstract class FishDatabase: RoomDatabase() {
    abstract fun caughtFishDao(): CaughtFishDao

    companion object {
        private var fishDbInstance: FishDatabase? = null

        @Synchronized
        fun get(context: Context): FishDatabase {
            if(fishDbInstance == null) {
                fishDbInstance = Room.databaseBuilder(
                        context.applicationContext,
                        FishDatabase::class.java,
                        "fish.db")
                        .build()
            }
            return fishDbInstance!!
        }
    }
}