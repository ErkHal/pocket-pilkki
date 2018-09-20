package com.android.erkhal.pocket_pilkki

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.BaseArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_pilkki_ar.*

class PilkkiArActivity : AppCompatActivity() {

    //Renderables for the AR scene
    private lateinit var fishingPondRenderable: ModelRenderable

    private lateinit var arFragment: ArFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilkki_ar)

        arFragment = ar_fragment as ArFragment

        setupFishingPondRenderable()

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
}
