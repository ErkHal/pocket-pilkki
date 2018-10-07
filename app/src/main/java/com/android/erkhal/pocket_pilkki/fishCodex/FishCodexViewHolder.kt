package com.android.erkhal.pocket_pilkki.fishCodex

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import com.android.erkhal.pocket_pilkki.utils.Utils
import com.android.erkhal.pocket_pilkki.R
import com.android.erkhal.pocket_pilkki.model.FishSpecies
import kotlinx.android.synthetic.main.fishing_book_entry.view.*

class FishCodexViewHolder(private val codexEntryView: View, private val context: Context): RecyclerView.ViewHolder(codexEntryView) {

    private lateinit var species: FishSpecies
    private fun isCaught() = Utils.isCaught(species, context)

    fun assignValues(speciesInformation: FishSpecies) {

        species = speciesInformation

        codexEntryView.apply {
            share_button.hide()
            fish_image.setImageDrawable(context.getDrawable(getImageResource()))
            //Regular strings
            fish_entry_species.text = context.getString(speciesInformation.speciesName)
            fish_measurements.text = context.getString(speciesInformation.description)
            fish_caught_date.text = context.getString(getCaughtStatusText())
            //Accessibility strings
            fish_entry_species.contentDescription = context.getString(speciesInformation.speciesName)
            fish_measurements.contentDescription = context.getString(speciesInformation.description)
            fish_caught_date.contentDescription = context.getString(getCaughtStatusText())
        }
    }

    private fun getImageResource(): Int {
        return when(isCaught()) {
            true -> species.imageResource
            false -> R.drawable.ic_help_black_24dp
        }
    }

    private fun getCaughtStatusText(): Int {
        return when(isCaught()) {
            true -> R.string.fishspecies_caught
            false -> R.string.fishspecies_not_yet_caught
        }
    }
}