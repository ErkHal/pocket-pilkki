package com.android.erkhal.pocket_pilkki

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.android.erkhal.pocket_pilkki.global.GlobalFishSpecies
import com.android.erkhal.pocket_pilkki.persistence.AsyncTasks.AllCaughtFishAsyncTask
import com.android.erkhal.pocket_pilkki.persistence.AsyncTasks.PersistCaughtFishAsyncTask
import com.android.erkhal.pocket_pilkki.persistence.CaughtFish
import com.android.erkhal.pocket_pilkki.persistence.FishDatabase
import com.google.ar.core.Anchor
import com.google.ar.core.HitResult
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_pilkki_ar.*
import kotlinx.android.synthetic.main.dialog_caught_fish_info.view.*
import java.util.*
import kotlin.collections.HashMap


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

    // The current fish to be caught is stored in this variable
    private var currentFish: CaughtFish? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilkki_ar)

        setupRenderables()

        arFragment = ar_fragment as ArFragment
        accelerometerController = AccelerometerController(this)
        arFragment.setOnTapArPlaneListener { hitResult, _, _ ->
                    spawnFishingPond(hitResult)
                    startCatching()
                }

        tvProgressBar.text = getString(R.string.progress_bar_finding_spot)

        // #### DEBUGGING LISTING ALL CAUGHT FISH IN DB ######
        val task = AllCaughtFishAsyncTask(FishDatabase.get(this))
        task.execute()
        // #####################
    }

    override fun onDeviceJerked() {

        val curTime = System.currentTimeMillis()

        if (catchingModeOn) {

            if (advancement >= 100) {
                fishCaught()
            }

            if (curTime > mShakeTimestamp + PROGRESS_INCREMENT_COOLDOWN) {
                if (advancement <= 99) {
                    mShakeTimestamp = curTime
                    advancement += 35
                    Log.d("ADV", "Progressbar: INCR $advancement")
                }
            }

        } else if ( advancement < 100 && advancement > 0 && curTime > mShakeTimestamp + PROGRESS_DECREMENT_COOLDOWN){
            advancement -= 10
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
        vibrator.vibrate(VibrationEffect.createOneShot(CATCHING_MODE_DURATION_MILLIS, VibrationEffect.DEFAULT_AMPLITUDE))
        this.runOnUiThread {
            run {
                Handler().postDelayed({ catchingModeOn = false }, CATCHING_MODE_DURATION_MILLIS)
            }
        }
    }

    private fun startCatching() {
        fishingModeOn = true
        tvProgressBar.text = getString(R.string.progress_bar)
        tvProgressBar.setTextColor(getColor(R.color.material_deep_teal_200))
        resetGnawCounter()
        startFishingThread()
        rodView.setImageResource(R.drawable.fishingrod)
        currentFish = GlobalFishSpecies.getRandomizedFish()
    }

    private fun fishCaught() {

        Toast.makeText(this, getString(R.string.dialog_fish_caught), Toast.LENGTH_SHORT).show()
        catchingModeOn = false
        fishingRunnable.quit()
        currentFish?.caughtTimestamp = Date().time

        // Take net into hand
        rodView.setImageResource(R.drawable.net)
        spawnFish()
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

            // Disable plane rendering, since that is no longer needed.
            arFragment.arSceneView.planeRenderer.isEnabled = false
        }
    }

    private fun spawnFish() {
        val fishAnchor = fishingPondAnchor
        val anchorNode = AnchorNode(fishAnchor)
        anchorNode.setParent(arFragment.arSceneView.scene)
        val fishNode = TransformableNode(arFragment.transformationSystem)
        fishNode.setParent(anchorNode)

        fishNode.localPosition = Vector3(0f, 0.5f, 0f)

        disableViewNodeController(fishNode)

        fishNode.renderable = fishRenderables.get(currentFish?.species)
        fishNode.select()

        // Show dialog when the player taps on the fish
        fishNode.setOnTapListener { hitTestResult, motionEvent ->
            fishNode.setParent(null)
            showCaughtDialog()
        }
    }

    //Inflates the view and populates the data in the fish layout
    private fun showCaughtDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setCancelable(false)
        val inflatedFishView = layoutInflater.inflate(R.layout.dialog_caught_fish_info, null)
        dialogBuilder.setView(inflatedFishView)
        inflatedFishView.fish_image.setImageDrawable(getDrawable(getFishIcon(currentFish?.species!!)!!))
        inflatedFishView.fish_species.text = currentFish?.species ?: "Eipä ollu"
        inflatedFishView.fish_measurements.text = currentFish?.toMeasurementsString() ?: "Eipä ollu"

        dialogBuilder.setPositiveButton(R.string.fish_caught_positive) { _, _ ->
            persistCaughtFish(currentFish!!)
            startCatching()
        }
        dialogBuilder.setNegativeButton(R.string.fish_caught_negative) { _, _ ->
            currentFish = null
            startCatching()
        }

        dialogBuilder.create().show()
    }

    private fun getFishIcon(species: String): Int? {
        var resourceId: Int? = null
        when(species) {
            "Salmon" -> resourceId = R.drawable.fish_salmon
            "Pike" -> resourceId = R.drawable.fish_pike
        }
        return resourceId
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

    private fun setupRenderables() {

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

        modelUri = Uri.parse("pond.sfb")
        val renderableFishingPond = ModelRenderable.builder()
                .setSource(this, modelUri)
                .build()
        renderableFishingPond.thenAccept { it ->
            fishingPondRenderable = it
        }
    }
}