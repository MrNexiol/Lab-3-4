package com.prograils.lab3_4

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.prograils.lab3_4.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SensorEventListener, View.OnClickListener {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var working = false
    private lateinit var sensorManager: SensorManager
    private lateinit var sensorAcc: Sensor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorAcc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        binding.startStopButton.setOnClickListener(this)

        setContentView(binding.root)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            var xValue = event.values[0]
            var yValue = event.values[1]
            var zValue = event.values[2]

            if (xValue < -10) xValue = -10F; if (xValue > 10) xValue = 10F
            if (yValue < -10) yValue = -10F; if (yValue > 10) yValue = 10F
            if (zValue < -10) zValue = -10F; if (zValue > 10) zValue = 10F

            binding.xValue.text = xValue.toString()
            binding.yValue.text = yValue.toString()
            binding.zValue.text = zValue.toString()

            xValue = (-xValue + 10) / 20
            yValue = (yValue + 10) / 20
            zValue = (zValue + 10) / 20

            val params = binding.rollingView.layoutParams as ConstraintLayout.LayoutParams
            params.horizontalBias = xValue
            params.verticalBias = yValue
            binding.rollingView.layoutParams = params

            binding.root.setBackgroundColor(Color.rgb(xValue, yValue, zValue))
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onClick(v: View?) {
        working = if (working) {
            sensorManager.unregisterListener(this)
            binding.startStopButton.text = getString(R.string.start)
            false
        } else {
            sensorManager.registerListener(this, sensorAcc, SensorManager.SENSOR_DELAY_GAME)
            binding.startStopButton.text = getString(R.string.stop)
            true
        }
    }
}