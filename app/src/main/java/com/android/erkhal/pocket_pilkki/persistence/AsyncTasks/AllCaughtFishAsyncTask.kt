package com.android.erkhal.pocket_pilkki.persistence.AsyncTasks

import android.os.AsyncTask
import android.util.Log
import com.android.erkhal.pocket_pilkki.persistence.CaughtFish
import com.android.erkhal.pocket_pilkki.persistence.FishDatabase

class AllCaughtFishAsyncTask(private val db: FishDatabase)
    : AsyncTask<Unit, Unit, Array<CaughtFish>>() {

    override fun doInBackground(vararg params: Unit): Array<CaughtFish> {
        return db.caughtFishDao().getAll()
    }

    override fun onPostExecute(result: Array<CaughtFish>?) {
        super.onPostExecute(result)
        for (fish in result!!) {
            Log.d("asdfg", "$fish")
        }
    }
}