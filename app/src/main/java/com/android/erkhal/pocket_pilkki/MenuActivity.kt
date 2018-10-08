package com.android.erkhal.pocket_pilkki


import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import com.android.erkhal.pocket_pilkki.fishCodex.FishCodexActivity
import com.android.erkhal.pocket_pilkki.fishingBook.FishingBookActivity
import kotlinx.android.synthetic.main.activity_menu.*

/**
 * Represents the Menu activity, which is the first activity the player sees when starting the game.
 * Has navigation to enter the Codex, Fishing Book or to start fishing
 */
class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        //Set layout after hiding stuff
        setContentView(R.layout.activity_menu)

        checkPermissions()

        // Button listeners
        btnStart.setOnClickListener {
            openPilkkiArActivity()
        }

        btnBook.setOnClickListener {
            openFishingBook()
        }

        btnCodex.setOnClickListener {
            openFishCodex()
        }
    }

    private fun openFishCodex() {
        val intent = Intent(this, FishCodexActivity::class.java)
        startActivity(intent)
    }

    private fun openPilkkiArActivity () {
        val intent = Intent(this, PilkkiArActivity::class.java)
        startActivity(intent)
    }

    private fun openFishingBook() {
        val openFishingBookIntent = Intent(this, FishingBookActivity::class.java)
        startActivity(openFishingBookIntent)
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.ACCESS_FINE_LOCATION), 0)
        }
    }
}
