package com.android.erkhal.pocket_pilkki

import android.content.Context
import android.net.Uri
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.ar.core.HitResult
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_pilkki_ar.*

// Constants
const val CATCHING_MODE_DURATION_MILLIS: Long = 1000
const val PROGRESS_INCREMENT_COOLDOWN = 1000
const val PROGRESS_DECREMENT_COOLDOWN = 200

class PilkkiArActivity: AppCompatActivity(),
        AccelerometerController.AcceleroMeterControllerListener,
        FishingRunnable.OnFishGnawingListener {

    //This is flipped to true when the fish is gnawing the bait
    private var catchingModeOn = false
    private var fishingModeOn = false

    //Renderables for the AR scene
    private lateinit var fishingPondRenderable: ModelRenderable

    // Fragments
    private lateinit var arFragment: ArFragment

    //Controllers & Runnables
    private lateinit var accelerometerController: AccelerometerController
    private lateinit var fishingRunnable: FishingRunnable

    //fishing progress
    private var advancement: Int = 0

    //timstamp for cooldown counting
    private var mShakeTimestamp: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilkki_ar)

        arFragment = ar_fragment as ArFragment

        accelerometerController = AccelerometerController(this)

        setupFishingPondRenderable()


        arFragment.setOnTapArPlaneListener(
                { hitResult, _, _ ->
                    spawnFishingPond(hitResult)
                })
    }

    override fun onDeviceJerked() {
        if (catchingModeOn) {
            Log.d("asdfg", "YEAAA BOIIIII")
            val curTime = System.currentTimeMillis()

            if (advancement == 100) {
                Log.d("ADV", "FISH CAUGHT HERRA ISÄ SENTÄÄ")
                catchingModeOn = false
                fishingRunnable.quit()
                resetGnawCounter()
                Toast.makeText(this, "FISH CAUGHT HERRANJUMALA SENTÄÄ", Toast.LENGTH_LONG)
                        .show()
            }

            if (curTime > mShakeTimestamp + PROGRESS_INCREMENT_COOLDOWN) {
                if (advancement <= 90) {
                    mShakeTimestamp = curTime
                    advancement = advancement + 20
                    Log.d("ADV", "Progressbar: INCR $advancement")
                }
            } else if ( advancement > 0 && curTime > mShakeTimestamp + PROGRESS_DECREMENT_COOLDOWN){
                advancement = advancement - 10
                Log.d("ADV", "Progressbar: DECR $advancement")
            }
        }
        fishingBar.progress = advancement
    }

    override fun onFishGnawing() {

        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        catchingModeOn = true
        vibrator.vibrate(CATCHING_MODE_DURATION_MILLIS)
        this.runOnUiThread {
            run {
                Handler().postDelayed({ catchingModeOn = false }, CATCHING_MODE_DURATION_MILLIS)
            }
        }
    }

    private fun toggleFishingMode() {
        fishingModeOn = !fishingModeOn
    }

    private fun resetGnawCounter() {
        advancement = 0
    }

    private fun spawnFishingPond(hitResult: HitResult) {

        if(!fishingModeOn) {
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)
            val viewNode = TransformableNode(arFragment.transformationSystem)
            viewNode.setParent(anchorNode)

            disableViewNodeController(viewNode)

            viewNode.renderable = fishingPondRenderable
            viewNode.select()

            toggleFishingMode()
            startFishingThread()
        }
    }

    private fun disableViewNodeController(viewNode: TransformableNode) {
        viewNode.rotationController.isEnabled = false
        viewNode.scaleController.isEnabled = false
        viewNode.translationController.isEnabled = false
    }

    private fun startFishingThread() {
        fishingRunnable = FishingRunnable(this, 50)
        val fishingThread = Thread(fishingRunnable)
        fishingThread.start()
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
}