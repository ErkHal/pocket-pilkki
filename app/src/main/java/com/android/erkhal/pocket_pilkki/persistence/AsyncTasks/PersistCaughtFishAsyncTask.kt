package com.android.erkhal.pocket_pilkki.persistence.AsyncTasks

import android.os.AsyncTask
import android.util.Log
import com.android.erkhal.pocket_pilkki.model.CaughtFish
import com.android.erkhal.pocket_pilkki.persistence.FishDatabase

/*
    Executes the persisting of the caught fish into the CaughtFish table in a worker thread
 */
class PersistCaughtFishAsyncTask(private val db: FishDatabase)
    : AsyncTask<CaughtFish, Unit, Unit>() {

    override fun doInBackground(vararg params: CaughtFish?) {
        if(params[0] != null) {
            val caughtFish = params[0] as CaughtFish
            Log.d("FishSaving", "$caughtFish")
            db.caughtFishDao().insert(caughtFish)
        }
    }

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)
        Log.d("caughtFish", "Saved fish !")
    }
}