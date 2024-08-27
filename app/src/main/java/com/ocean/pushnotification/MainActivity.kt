package com.ocean.pushnotification

import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.ocean.pushnotification.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ){isGranted ->
        if (isGranted){
            // FCM SDK (and your app) can post notifications.
        }else{
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onClickListener()
    }

    private fun onClickListener(){
        binding.getToken.setOnClickListener {v ->

            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->

                if (!task.isSuccessful){
                    Log.d(
                        "MainActivity",
                        "Fetching FCM registration token failed",
                        task.exception
                    )
                    return@addOnCompleteListener
                }
                val token = task.result
                println(token)
                binding.token.setText(token)
                Toast.makeText(this@MainActivity, "get a token", Toast.LENGTH_SHORT).show()
            }

        }
        binding.goToFetchLocation.setOnClickListener {
            startActivity(Intent(this, MainActivity2::class.java))
        }
    }

//    private fun askNotificationPermission(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.PO) ==
//                PackageManager.PERMISSION_GRANTED
//            ) {
//                // FCM SDK (and your app) can post notifications.
//            } else if (shouldShowRequestPermissionRationale(Manifest.permission.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION)) {
//                // TODO: display an educational UI explaining to the user the features that will be enabled
//                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
//                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
//                //       If the user selects "No thanks," allow the user to continue without notifications.
//            } else {
//                // Directly ask for the permission
//                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
//            }
//        }
//    }
}