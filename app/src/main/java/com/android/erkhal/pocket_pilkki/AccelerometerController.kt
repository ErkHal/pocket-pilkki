package com.android.erkhal.pocket_pilkki

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Vibrator
import android.util.Log
import kotlin.math.sqrt

//Constants
const val SHAKE_THRESHOLD_ACTIVITY = 2f // G-Force required for fish jerk

class AccelerometerController(ctx: Context): SensorEventListener {

    interface AcceleroMeterControllerListener {
        fun onDeviceJerked()
    }

    private var context = ctx
    private var sensorManager: SensorManager

    private var x = 0f
    private var y = 0f
    private var z = 0f

    private var mShakeTimestamp: Long = 0

    init {

        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("Accelerometer", "Sensor accuracy changed to $accuracy")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val vibratorService = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        if (event != null) {
            x = event.values[0]
            y = event.values[1]
            z = event.values[2]


            val gX = x / SensorManager.GRAVITY_EARTH
            val gY = y / SensorManager.GRAVITY_EARTH
            val gZ = z / SensorManager.GRAVITY_EARTH

            //gForce is approx 1 when no movement
            val gForce = sqrt(gX * gX + gY * gY + gZ * gZ)


            if (gForce > SHAKE_THRESHOLD_ACTIVITY) {

                //call shake stuff
                notifyActivity()

            }
        }
    }
    private fun notifyActivity() {
        val listener = context as AccelerometerController.AcceleroMeterControllerListener
        listener.onDeviceJerked()
    }
}