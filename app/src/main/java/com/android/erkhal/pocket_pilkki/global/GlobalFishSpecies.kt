package com.android.erkhal.pocket_pilkki.global

import android.net.Uri
import com.android.erkhal.pocket_pilkki.R
import com.android.erkhal.pocket_pilkki.model.FishSpecies

object GlobalFishSpecies {

    var species = HashSet<FishSpecies>()

    init {

        // Initialize all species of fish here

        species.add(FishSpecies(
                R.string.fishspecies_salmon,
                R.string.fishspecies_salmon_description,
                R.drawable.trout2,
                Uri.parse("Mesh_Trout.sfb"),
                600f,
                10000f,
                30f,
                140f,
                15

        ))
        species.add(FishSpecies(
                R.string.fishspecies_pike,
                R.string.fishspecies_pike_description,
                R.drawable.trout,
                Uri.parse("Mesh_Fish.sfb"),
                500f,
                7000f,
                30f,
                120f,
                20
        ))
        species.add(FishSpecies(
                R.string.fishspecies_crawfish,
                R.string.fishspecies_crawfish_description,
                R.drawable.crawfish,
                Uri.parse("NOVELO_CRAYFISH.sfb"),
                300f,
                3500f,
                15f,
                75f,
                15
        ))
        species.add(FishSpecies(
                R.string.fishspecies_goldfish,
                R.string.fishspecies_goldfish_description,
                R.drawable.goldfish,
                Uri.parse("Mesh_Goldfish.sfb"),
                800f,
                15000f,
                30f,
                135f,
                10
        ))
        species.add(FishSpecies(
                R.string.fishspecies_halibut,
                R.string.fishspecies_halibut_description,
                R.drawable.halibut,
                Uri.parse("Mesh_Halibut.sfb"),
                10000f,
                200000f,
                30f,
                300f,
                13
        ))
        species.add(FishSpecies(
                R.string.fishspecies_killerwhale,
                R.string.fishspecies_killerwhale_description,
                R.drawable.killerwhale,
                Uri.parse("Mesh_Orca.sfb"),
                1400000f,
                2800000f,
                500f,
                850f,
                3
        ))
        species.add(FishSpecies(
                R.string.fishspecies_kingfish,
                R.string.fishspecies_kingfish_description,
                R.drawable.kingfish,
                Uri.parse("Mesh_Kingfish.sfb"),
                2000f,
                75000f,
                20f,
                260f,
                10
        ))
        species.add(FishSpecies(
                R.string.fishspecies_piranha,
                R.string.fishspecies_piranha_description,
                R.drawable.piranha,
                Uri.parse("Piranha.sfb"),
                500f,
                4000f,
                10f,
                70f,
                10
        ))
        species.add(FishSpecies(
                R.string.fishspecies_pufferfish,
                R.string.fishspecies_pufferfish_description,
                R.drawable.pufferfish,
                Uri.parse("NOVELO_PUFFERFISH.sfb"),
                500f,
                15000f,
                10f,
                75f,
                10
        ))
        species.add(FishSpecies(
                R.string.fishspecies_yeltrout,
                R.string.fishspecies_yeltrout_description,
                R.drawable.yeltrout,
                Uri.parse("NOVELO_TROUT.sfb"),
                1000f,
                30000f,
                30f,
                140f,
                15
        ))
        species.add(FishSpecies(
                R.string.fishspecies_shark,
                R.string.fishspecies_shark_description,
                R.drawable.shark,
                Uri.parse("shark.sfb"),
                600000f,
                2200000f,
                200f,
                700f,
                6
        ))
    }
}