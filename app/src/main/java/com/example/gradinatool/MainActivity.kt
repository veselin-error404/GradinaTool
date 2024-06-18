package com.example.gradinatool

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextArea: EditText = findViewById(R.id.edit_text_area)
        val editTextApplicationRate: EditText = findViewById(R.id.edit_text_application_rate)
        val editTextSprayerVolume: EditText = findViewById(R.id.edit_text_sprayer_volume)
        val editTextDilutionRatio: EditText = findViewById(R.id.edit_text_dilution_ratio)
        val buttonCalculate: Button = findViewById(R.id.button_calculate)
        val textResult: TextView = findViewById(R.id.text_result)

        buttonCalculate.setOnClickListener {
            val areaText = editTextArea.text.toString()
            val applicationRateText = editTextApplicationRate.text.toString()
            val sprayerVolumeText = editTextSprayerVolume.text.toString()
            val dilutionRatioText = editTextDilutionRatio.text.toString()

            if (areaText.isEmpty() || applicationRateText.isEmpty() || sprayerVolumeText.isEmpty() || dilutionRatioText.isEmpty()) {
                Toast.makeText(this, "Моля въведете всички стойности", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val area = areaText.toDoubleOrNull()
            val applicationRate = applicationRateText.toDoubleOrNull()
            val sprayerVolume = sprayerVolumeText.toDoubleOrNull()

            if (area == null || applicationRate == null || sprayerVolume == null) {
                Toast.makeText(this, "Моля въведете валидни числа", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dilutionRatioParts = dilutionRatioText.split(":")
            if (dilutionRatioParts.size != 2) {
                Toast.makeText(this, "Моля въведете валидна пропорция", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val concentratePart = dilutionRatioParts[0].toDoubleOrNull()
            val waterPart = dilutionRatioParts[1].toDoubleOrNull()

            if (concentratePart == null || waterPart == null) {
                Toast.makeText(this, "Моля въведете валидна пропорция", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Calculate total concentrate needed for the given area
            val totalConcentrate = area * applicationRate

            // Calculate the amount of water needed based on the dilution ratio
            val waterNeeded = (waterPart / concentratePart) * totalConcentrate

            // Calculate the total volume of the solution needed in liters
            val totalVolume = (totalConcentrate + waterNeeded) / 1000

            // Calculate the number of sprayers needed
            val amountOfSprayers = if (totalVolume <= sprayerVolume) {
                1
            } else {
                ceil(totalVolume / sprayerVolume).toInt()
            }

            textResult.text = """
                - Количество концентрат: $totalConcentrate ml
                - Количество Вода: $waterNeeded ml
                - Брой пръскачки: $amountOfSprayers
            """.trimIndent()


        }

        // Check if it's night mode or day mode and update the UI accordingly
        if (isDarkTheme(this)) {
            buttonCalculate.setTextColor(Color.WHITE)
            buttonCalculate.setBackgroundColor(Color.DKGRAY)
        } else {
            buttonCalculate.setTextColor(Color.BLACK)
            buttonCalculate.setBackgroundColor(Color.LTGRAY)
        }
    }

    fun isDarkTheme(activity: Activity): Boolean {
        return activity.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
}
