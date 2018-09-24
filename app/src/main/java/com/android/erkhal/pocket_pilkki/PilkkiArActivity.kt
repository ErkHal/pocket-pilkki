package com.android.erkhal.pocket_pilkki

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.ar.core.HitResult
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_pilkki_ar.*

class PilkkiArActivity : AppCompatActivity(), AccelerometerController.AcceleroMeterControllerListener {

    //Renderables for the AR scene
    private lateinit var fishingPondRenderable: ModelRenderable

    // Fragments
    private lateinit var arFragment: ArFragment

    //Controllers
    private lateinit var accelerometerController: AccelerometerController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilkki_ar)

        arFragment = ar_fragment as ArFragment

        accelerometerController = AccelerometerController(this)

        setupFishingPondRenderable()

        //calls the top bar functionality
        fishingBarController()

        arFragment.setOnTapArPlaneListener(
                { hitResult, _, _ ->
                    spawnFishingPond(hitResult)
                })
    }

    override fun onDeviceJerked() {

    }

    private fun disableViewNodeController(viewNode: TransformableNode) {
        viewNode.rotationController.isEnabled = false
        viewNode.scaleController.isEnabled = false
        viewNode.translationController.isEnabled = false
    }

    private fun spawnFishingPond(hitResult: HitResult) {

        val anchor = hitResult!!.createAnchor()
        val anchorNode = AnchorNode(anchor)
        anchorNode.setParent(arFragment.arSceneView.scene)
        val viewNode = TransformableNode(arFragment.transformationSystem)
        viewNode.setParent(anchorNode)

        disableViewNodeController(viewNode)

        viewNode.renderable = fishingPondRenderable
        viewNode.select()

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
}