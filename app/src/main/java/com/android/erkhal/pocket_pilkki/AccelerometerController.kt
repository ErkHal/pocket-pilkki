package com.android.erkhal.pocket_pilkki

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import kotlin.math.sqrt

//Constants
const val SHAKE_THRESHOLD_ACTIVITY = 1.5f // G-Force required for fish jerk

/**
 * Takes measurements about the phone's accelerometer, and uses that to determine when the player
 * has jerked the phone enough to trigger any action. The constant SHAKE_THRESHOLD_ACTIVITY
 * is used to calibrate the needed G-force the phone needs to receive before acting on it.
 */
class AccelerometerController(private val context: Context): SensorEventListener {

    interface AcceleroMeterControllerListener {
        fun onDeviceJerked()
    }

    private var x = 0f
    private var y = 0f
    private var z = 0f

    init {

        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("Accelerometer", "Sensor accuracy changed to $accuracy")
    }

    override fun onSensorChanged(event: SensorEvent?) {

        if (event != null) {
            x = event.values[0]
            y = event.values[1]
            z = event.values[2]

            val gX = x / SensorManager.GRAVITY_EARTH
            val gY = y / SensorManager.GRAVITY_EARTH
            val gZ = z / SensorManager.GRAVITY_EARTH

            //gForce is approximately 1 when no movement
            val gForce = sqrt(gX * gX + gY * gY + gZ * gZ)

            if (gForce > SHAKE_THRESHOLD_ACTIVITY) {
                //notify the main thread of the successful device jerk
                notifyActivity()
            }
        }
    }

    private fun notifyActivity() {
        val listener = context as AccelerometerController.AcceleroMeterControllerListener
        listener.onDeviceJerked()
    }
}