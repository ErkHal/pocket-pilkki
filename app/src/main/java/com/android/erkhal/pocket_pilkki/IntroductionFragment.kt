package com.android.erkhal.pocket_pilkki

import android.content.Intent
import android.preference.PreferenceManager
import android.support.v17.leanback.app.OnboardingFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class IntroductionFragment: OnboardingFragment() {

    override fun getPageTitle(pageNumber: Int): CharSequence {
        return when(pageNumber) {
            0 -> context!!.getString(R.string.onboarding_title_0)
            1 -> context!!.getString(R.string.onboarding_title_1)
            2 -> context!!.getString(R.string.onboarding_title_2)
            3 -> context!!.getString(R.string.onboarding_title_3)
            else -> "Error"
        }
    }

    override fun getPageDescription(pageNumber: Int): CharSequence {
        return when(pageNumber) {
            0 -> context!!.getString(R.string.onboarding_description_0)
            1 -> context!!.getString(R.string.onboarding_description_1)
            2 -> context!!.getString(R.string.onboarding_description_2)
            3 -> context!!.getString(R.string.onboarding_description_3)
            else -> "Error"
        }
    }

    override fun onCreateForegroundView(p0: LayoutInflater?, p1: ViewGroup?): View? {
        return null
    }

    override fun onCreateBackgroundView(p0: LayoutInflater?, p1: ViewGroup?): View? {
        return null
    }

    override fun getPageCount(): Int {
        return 4
    }

    override fun onCreateContentView(p0: LayoutInflater?, p1: ViewGroup?): View? {
        //Setup colors
        this.arrowColor = context.getColor(R.color.darkBackground)
        this.arrowBackgroundColor = context.getColor(R.color.fishingBookEntryBG)
        this.dotBackgroundColor = context.getColor(R.color.fishingBookEntryBG)
        this.startButtonText = context.getString(R.string.onboarding_startbtn)

        return ImageView(context).apply {
            setImageResource(R.drawable.pocketfishing)
        }
    }

    // Updates preferences so that the introduction is not shown again
    override fun onFinishFragment() {
        super.onFinishFragment()
        PreferenceManager.getDefaultSharedPreferences(context).edit().apply {
            putBoolean(COMPLETED_ONBOARDING_PREF_NAME, true)
            apply()
        }
        startActivity(Intent(context, PilkkiArActivity::class.java))
    }
}