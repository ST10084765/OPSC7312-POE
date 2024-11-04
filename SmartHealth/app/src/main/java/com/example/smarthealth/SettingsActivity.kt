package com.example.smarthealth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

class SettingsActivity : AppCompatActivity() {

    private lateinit var languageSettingButton: Button
    private lateinit var notificationSettingButton: Button
    private lateinit var privacySettingButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Initialize buttons
        languageSettingButton = findViewById(R.id.language_setting)
        notificationSettingButton = findViewById(R.id.notification_setting)
        privacySettingButton = findViewById(R.id.privacy_setting)

        // Set up click listeners for each setting option
        languageSettingButton.setOnClickListener {
            val languages = arrayOf("English", "isiZulu", "Afrikaans")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Choose Language")
            builder.setItems(languages) { _, which ->
                val selectedLang = when (which) {
                    0 -> "en"
                    1 -> "zu"
                    2 -> "af"
                    else -> "en"
                }
                setLocale(selectedLang)
                startActivity(Intent(this, SettingsActivity::class.java))
                finish()
            }
            builder.show()
        }


        notificationSettingButton.setOnClickListener {
            Toast.makeText(this, "Notification settings clicked", Toast.LENGTH_SHORT).show()
        }

        privacySettingButton.setOnClickListener {
            Toast.makeText(this, "Privacy settings clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLanguageDialog() {
        val languages = arrayOf("English", "isiZulu", "Afrikaans")
        val languageTags = arrayOf("en", "zu", "af")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose Language")
        builder.setItems(languages) { _, which ->
            val selectedLocale = languageTags[which]

            // Use AppCompatDelegate to set the app's locale
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(selectedLocale)
            AppCompatDelegate.setApplicationLocales(appLocale)

            // The activity may be recreated to apply the new locale
        }
        builder.show()
    }

    private fun setLocale(lang: String) {
        val appLocale = LocaleListCompat.forLanguageTags(lang)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

}
