package com.android.erkhal.pocket_pilkki

import android.content.Context
import android.net.Uri
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.erkhal.pocket_pilkki.global.GlobalFishSpecies
import com.android.erkhal.pocket_pilkki.persistence.AsyncTasks.AllCaughtFishAsyncTask
import com.android.erkhal.pocket_pilkki.persistence.CaughtFish
import com.android.erkhal.pocket_pilkki.persistence.FishDatabase
import com.android.erkhal.pocket_pilkki.persistence.AsyncTasks.PersistCaughtFishAsyncTask
import com.google.ar.core.Anchor
import com.google.ar.core.HitResult
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_pilkki_ar.*
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet


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
    private lateinit var fishRenderables: HashMap<String, ModelRenderable>

    // AR scene anchors
    private lateinit var fishingPondAnchor: Anchor

    // Fragments
    private lateinit var arFragment: ArFragment

    //Controllers & Runnables
    private lateinit var accelerometerController: AccelerometerController
    private lateinit var fishingRunnable: FishingRunnable

    //fishing progress
    private var advancement: Int = 0

    //Timestamp for cooldown counting
    private var mShakeTimestamp: Long = 0

    private lateinit var currentFish: CaughtFish

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilkki_ar)

        setupFishingPondRenderable()
        setupFishRenderables()

        arFragment = ar_fragment as ArFragment
        accelerometerController = AccelerometerController(this)
        arFragment.setOnTapArPlaneListener { hitResult, _, _ ->
                    spawnFishingPond(hitResult)
                    startCatching()
                }

        // #### DEBUGGING LISTING ALL CAUGHT FISH IN DB ######
        val task = AllCaughtFishAsyncTask(FishDatabase.get(this))
        task.execute()
        // #####################
    }

    override fun onDeviceJerked() {

        val curTime = System.currentTimeMillis()

        if (catchingModeOn) {
            Log.d("asdfg", "YEAAA BOIIIII")

            if (advancement >= 100) {
                fishCaught()
            }

            if (curTime > mShakeTimestamp + PROGRESS_INCREMENT_COOLDOWN) {
                if (advancement <= 90) {
                    mShakeTimestamp = curTime
                    advancement =+ advancement + 35
                    Log.d("ADV", "Progressbar: INCR $advancement")
                }
            }

        } else if ( advancement < 100 && advancement > 0 && curTime > mShakeTimestamp + PROGRESS_DECREMENT_COOLDOWN){
            advancement =- advancement - 10
            Log.d("ADV", "Progressbar: DECR $advancement")
        }
        fishingBar.progress = advancement
    }

    // Persists the caught fish into database using AsyncTask run in a worker thread
    private fun persistCaughtFish(caughtFish: CaughtFish) {
        val task = PersistCaughtFishAsyncTask(FishDatabase.get(this))
        task.execute(caughtFish)
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

    private fun startCatching() {
        fishingModeOn = true
        startFishingThread()

        currentFish = GlobalFishSpecies.getRandomizedFish()
    }

    private fun fishCaught() {

        catchingModeOn = false
        fishingRunnable.quit()
        currentFish.caughtTimestamp = Date().time
        persistCaughtFish(currentFish)

        Toast.makeText(this, "$currentFish", Toast.LENGTH_LONG).show()

        // Take net into hand
        rodView.setImageResource(R.drawable.net)

        spawnFish()

        restartBtn.setOnClickListener {
            resetGnawCounter()
            rodView.setImageResource(R.drawable.fishingrod)
            restartBtn.visibility = View.INVISIBLE
            startCatching()
        }
    }

    private fun resetGnawCounter() {
        advancement = 0
        fishingBar.progress = advancement
    }

    private fun spawnFishingPond(hitResult: HitResult) {

        if(!fishingModeOn) {
            fishingPondAnchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(fishingPondAnchor)
            anchorNode.setParent(arFragment.arSceneView.scene)
            val viewNode = TransformableNode(arFragment.transformationSystem)
            viewNode.setParent(anchorNode)

            disableViewNodeController(viewNode)

            viewNode.renderable = fishingPondRenderable
            viewNode.select()
        }
    }

    private fun spawnFish() {
        val fishAnchor = fishingPondAnchor
        val anchorNode = AnchorNode(fishAnchor)
        anchorNode.setParent(arFragment.arSceneView.scene)
        var fishNode = TransformableNode(arFragment.transformationSystem)
        fishNode.setParent(anchorNode)

        fishNode.localPosition = Vector3(0f, 0.5f, 0f)

        disableViewNodeController(fishNode)

        fishNode.renderable = fishRenderables.get(currentFish.species)
        fishNode.select()

        fishNode.setOnTapListener { hitTestResult, motionEvent ->
            fishNode.setParent(null)
            restartBtn.visibility = View.VISIBLE
            Log.d("CLICK", "Fish clicked")
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

    private fun setupFishRenderables() {

        fishRenderables = HashMap()

        var modelUri = Uri.parse("Mesh_Fish.sfb")
        val renderablePike = ModelRenderable.builder()
                .setSource(this, modelUri)
                .build()
        renderablePike.thenAccept { it ->
            fishRenderables["Pike"] = it
        }

        modelUri = Uri.parse("Mesh_Trout.sfb")
        val renderableSalmon = ModelRenderable.builder()
                .setSource(this, modelUri)
                .build()
        renderableSalmon.thenAccept { it ->
            fishRenderables["Salmon"] = it
        }
    }
}