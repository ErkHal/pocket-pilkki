package com.android.erkhal.pocket_pilkki.fishingBook

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Environment
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.android.erkhal.pocket_pilkki.R
import com.android.erkhal.pocket_pilkki.global.GlobalFishSpecies
import com.android.erkhal.pocket_pilkki.model.CaughtFish
import kotlinx.android.synthetic.main.fishing_book_entry.view.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class FishingBookEntryViewHolder(val view: View, val context: Context) : RecyclerView.ViewHolder(view) {

    init {
        view.share_button.setOnClickListener {
            val shareableFishBitmap = captureView(view)
            val imageFilepath = saveShareableImage(shareableFishBitmap)
            shareImage(imageFilepath)
        }
    }

    private fun captureView(view: View): Bitmap {
        // Hides the share button from the image
        (view.share_button as View).visibility = View.INVISIBLE
        val bitmap =
                Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.layout(0, 0, view.width, view.height)
        view.draw(canvas)
        // Show the share button again
        (view.share_button as View).visibility = View.VISIBLE
        return bitmap
    }

    private fun saveShareableImage(shareableFishBitmap: Bitmap?): String {
        val rootPath = Environment.getExternalStorageDirectory()
        val imgFile = File(rootPath, "${UUID.randomUUID()}.png")
        try {
            val outputStream = FileOutputStream(imgFile)
            shareableFishBitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Couldn't save image !", Toast.LENGTH_SHORT).show()
        }
        return imgFile.path
    }

    private fun shareImage(filepath: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/png"
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(filepath))
        context.startActivity(shareIntent)
    }

    fun assignValues(fish: CaughtFish, context: Context) {
        view.fish_entry_species.text = context.getString(fish.species)
        view.fish_measurements.text = context.getString(
                R.string.fish_measurements, fish.getFishLength(), fish.getFishWeight())
        view.fish_caught_date.text = context.getString(R.string.fish_caught_on, fish.getFishCaughtDate().toString())
        view.fish_image.setImageDrawable(context.getDrawable(GlobalFishSpecies.getImageResource(fish)))
    }
}