package com.example.smarthealth

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var stepCounterSensor: Sensor? = null
    private var isTracking = false
    private var stepCount: Int = 0

    private lateinit var stepsTextView: TextView
    private lateinit var startTrackingButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize UI elements
        stepsTextView = findViewById(R.id.steps_text_view)
        startTrackingButton = findViewById(R.id.start_tracking_button)

        // Initialize SensorManager and step counter sensor
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        // Check if the device has a step counter sensor
        if (stepCounterSensor == null) {
            Toast.makeText(this, "No step counter sensor found!", Toast.LENGTH_LONG).show()
            startTrackingButton.isEnabled = false
        }

        // Handle the Start Tracking button click
        startTrackingButton.setOnClickListener {
            if (!isTracking) {
                startTrackingSteps()
            } else {
                stopTrackingSteps()
            }
        }

        // Initialize the BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Set up the selected item listener for the BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    // Stay on the home page
                    true
                }
                R.id.menu_settings -> {
                    // Navigate to the SettingsActivity
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun startTrackingSteps() {
        // Start step tracking by registering the listener
        sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL)
        isTracking = true
        startTrackingButton.text = "Stop Tracking"
        Toast.makeText(this, "Tracking started", Toast.LENGTH_SHORT).show()
    }

    private fun stopTrackingSteps() {
        // Stop step tracking by unregistering the listener
        sensorManager.unregisterListener(this, stepCounterSensor)
        isTracking = false
        startTrackingButton.text = "Start Tracking"
        Toast.makeText(this, "Tracking stopped", Toast.LENGTH_SHORT).show()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            // Update the step count from the sensor event
            stepCount = event.values[0].toInt()
            stepsTextView.text = "$stepCount"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not needed for this implementation
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the sensor listener to save battery when the activity is destroyed
        if (isTracking) {
            sensorManager.unregisterListener(this, stepCounterSensor)
        }
    }

}
