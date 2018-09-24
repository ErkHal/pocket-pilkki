package com.android.erkhal.pocket_pilkki

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.BaseArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_pilkki_ar.*
import kotlin.math.sqrt

class PilkkiArActivity : AppCompatActivity(), SensorEventListener {

    //Renderables for the AR scene
    private lateinit var fishingPondRenderable: ModelRenderable

    private lateinit var arFragment: ArFragment

    lateinit var sensorManager: SensorManager

    var x = 0f
    var y = 0f
    var z = 0f

    private val SHAKE_THRESHOLD_GRAVITY = 5f
    private val SHAKE_SLOP_TIME_MS = 500

    private var mShakeTimestamp: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilkki_ar)

        arFragment = ar_fragment as ArFragment

        setupFishingPondRenderable()

        //calls the top bar functionality
        fishingBarController()

        //
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME)

        arFragment.setOnTapArPlaneListener(
                BaseArFragment.OnTapArPlaneListener { hitResult, _, _ ->

                    if (fishingPondRenderable == null) {
                        return@OnTapArPlaneListener
                    }

                    val anchor = hitResult!!.createAnchor()
                    val anchorNode = AnchorNode(anchor)
                    anchorNode.setParent(arFragment.arSceneView.scene)
                    val viewNode = TransformableNode(arFragment.transformationSystem)
                    viewNode.setParent(anchorNode)

                    disableViewNodeController(viewNode)

                    viewNode.renderable = fishingPondRenderable
                    viewNode.select()

                })
    }

    private fun disableViewNodeController(viewNode: TransformableNode) {
        viewNode.rotationController.isEnabled = false
        viewNode.scaleController.isEnabled = false
        viewNode.translationController.isEnabled = false
    }

    private fun setupFishingPondRenderable() {
        val modelUri = Uri.parse("pond.sfb")

        val renderableFuture = ModelRenderable.builder()
                .setSource(this, modelUri)
                .build()
        renderableFuture.thenAccept { it ->
            fishingPondRenderable = it
        }
    }

    private fun fishingBarController() {
        // testing the progressbar
        fishingBar.setProgress(50)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {

        val vibratorService = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        if (event != null) {
            x = event!!.values[0]
            y = event.values[1]
            z = event.values[2]


            var gX = x / SensorManager.GRAVITY_EARTH
            var gY = y / SensorManager.GRAVITY_EARTH
            var gZ = z / SensorManager.GRAVITY_EARTH

            //gForce is approx 1 when no movement
            var gForce = sqrt(gX * gX + gY * gY + gZ * gZ)

            tvGForce.text = "${gForce}"

            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                var curTime = System.currentTimeMillis().toFloat()

                //ignore shake events too close to each other (500ms)
                if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > curTime) {
                    return
                } else {
                    mShakeTimestamp = curTime.toLong()
                    vibratorService.vibrate(100)

                }
            }
        }
    }
}