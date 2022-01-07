package kz.kakainchik.sportradarsample.ui

import android.media.AudioManager
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import kz.kakainchik.sportradarsample.R
import kz.kakainchik.sportradarsample.ext.getVolumeFromPercent
import kz.kakainchik.sportradarsample.ui.custom.VolumeControl

class MainActivity : AppCompatActivity() {
    private lateinit var audio: AudioManager

    lateinit var volumeControl: VolumeControl
    lateinit var linesText: EditText
    lateinit var volumeText: EditText
    lateinit var currentVolumeText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        audio = applicationContext.getSystemService(AUDIO_SERVICE) as AudioManager

        volumeControl = findViewById(R.id.volume_control)
        linesText = findViewById(R.id.lines_text)
        volumeText = findViewById(R.id.volume_text)
        currentVolumeText = findViewById<TextView>(R.id.current_volume_text).apply {
            //Set text by default
            text = getString(R.string.value_volume_text, volumeControl.volValue)
        }

        volumeControl.setOnVolumeChangeListener { v ->
            currentVolumeText.text = getString(R.string.value_volume_text, v)
            audio.setStreamVolume(volumeControlStream,
                audio.getVolumeFromPercent(v, volumeControlStream),
                AudioManager.FLAG_SHOW_UI)
        }

        findViewById<MaterialButton>(R.id.lines_button).setOnClickListener {
            val num = linesText.text.toString().toIntOrNull()
            if(num != null) {
                volumeControl.linesCount = num
            } else showToast(getText(R.string.invalid_number_error))
        }

        findViewById<MaterialButton>(R.id.volume_button).setOnClickListener {
            val num = volumeText.text.toString().toFloatOrNull()
            if(num != null) {
                volumeControl.volValue = num
            } else showToast(getText(R.string.invalid_number_error))
        }
    }

    override fun onResume() {
        super.onResume()

        volumeControlStream = AudioManager.STREAM_MUSIC
    }

    private fun showToast(text: CharSequence) {
        Toast.makeText(this,
            text,
            Toast.LENGTH_SHORT)
            .show()
    }
}