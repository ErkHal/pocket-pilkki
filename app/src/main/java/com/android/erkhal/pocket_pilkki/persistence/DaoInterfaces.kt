package com.android.erkhal.pocket_pilkki.persistence

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.android.erkhal.pocket_pilkki.model.CaughtFish

@Dao
interface CaughtFishDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(fish: CaughtFish)

    @Query("SELECT * FROM caughtfish as fish WHERE fish.species = :species")
    fun getAllOfSpecies(species: Int): Array<CaughtFish>

    @Query("SELECT * FROM caughtfish")
    fun getAll(): Array<CaughtFish>
}