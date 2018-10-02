package com.android.erkhal.pocket_pilkki


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Set layout after hiding stuff
        setContentView(R.layout.activity_menu)


        // Button listeners
        btnStart.setOnClickListener {
            openPilkkiArActivity()
        }
    }

    fun openPilkkiArActivity () {
        val intent = Intent(this, PilkkiArActivity::class.java)
        startActivity(intent)
    }
}
