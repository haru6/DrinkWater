package br.com.drinkwater

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import br.com.drinkwater.R.color.*
import br.com.drinkwater.R.string.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var timePicker: TimePicker
    private lateinit var editInterval: EditText
    private lateinit var btnVerify: Button
    private var activated: Boolean = false
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recebendoValores()
        preferences()
        eventoInterval()
    }

    private fun recebendoValores(){
        editInterval = edit_interval
        btnVerify = btn_verify
        timePicker = time_picker
        timePicker.setIs24HourView(true)
    }

    private fun eventoInterval(){
        btnVerify.setOnClickListener(View.OnClickListener {
            val sInvterval = editInterval.text.toString()
            val hour: Int = timePicker.currentHour
            val minute: Int = timePicker.currentMinute

            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            if(sInvterval.isEmpty()){
                Toast.makeText(this, error_msg, Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            val interval: Int = sInvterval.toInt()

            when(activated){
                false -> {
                    btnVerify.text = "Pause"
                    btnVerify.backgroundTintList = ContextCompat.getColorStateList(this, black)
                    activated = true

                    editor.putBoolean("activated", true)
                    editor.putInt("hour", hour)
                    editor.putInt("minute", minute)
                    editor.putInt("interval", interval)
                    editor.apply()
                }
                true ->{
                    btnVerify.text = "Notify"
                    btnVerify.backgroundTintList = ContextCompat.getColorStateList(this, dark_blue)
                    activated = false

                    editor.putBoolean("activated", false)
                    editor.remove("hour")
                    editor.remove("minute")
                    editor.remove("interval")
                    editor.apply()
                }
            }

            Log.d("Teste", "hora: ${hour} minuto: ${minute} interval: ${sInvterval}")
        })
    }

    private fun preferences(){
        sharedPreferences = getSharedPreferences("db", Context.MODE_PRIVATE)
        activated = sharedPreferences.getBoolean("activated", false)

        if(activated){
            btnVerify.text = "Pause"
            btnVerify.backgroundTintList = ContextCompat.getColorStateList(this, black)

            val intervals = sharedPreferences.getInt("interval", 0)
            val hours = sharedPreferences.getInt("hour", timePicker.currentHour)
            val minutes = sharedPreferences.getInt("minute", timePicker.currentMinute)

            editInterval.setText(intervals.toString())
            timePicker.currentHour = hours
            timePicker.currentMinute = minutes
        }
    }
}