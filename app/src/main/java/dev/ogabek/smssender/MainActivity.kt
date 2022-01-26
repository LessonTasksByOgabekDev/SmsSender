package dev.ogabek.smssender

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private val SMS_CODE = 1000;

    private lateinit var phoneNumber: EditText
    private lateinit var textSMS: EditText
    private lateinit var sendBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }

    }

    private fun initViews() {
        phoneNumber = findViewById(R.id.phoneNumber)
        textSMS = findViewById(R.id.textSMS)
        sendBtn = findViewById(R.id.btnSend)

        sendBtn.setOnClickListener {
            sendSMS(phoneNumber.text.toString(), textSMS.text.toString())
        }

    }

    private fun sendSMS(number: String, sms: String) {
        try {
            val smsManager: SmsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(number, null,sms, null, null)
            Toast.makeText(this, "Your Message has been sent to $number", Toast.LENGTH_SHORT).show()
            phoneNumber.setText("+998")
            textSMS.setText("")
        } catch (e: Exception) {
            Toast.makeText(this, "${e.message.toString()}", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.SEND_SMS), SMS_CODE)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            SMS_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Thank you to give permission to us", Toast.LENGTH_SHORT).show()
                } else {
                    checkPermission()
                }
            }
        }
    }
}