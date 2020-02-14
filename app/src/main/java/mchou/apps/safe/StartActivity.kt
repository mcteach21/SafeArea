package mchou.apps.safe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_start.*
import mchou.apps.codelabs.tools.Util
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.AttributeSet
import android.view.View


class StartActivity : AppCompatActivity(){
    val TAG : String = "Tests"
    var fpm = McFPManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        logo.setOnClickListener { fingerprint() }   //biometric() /*Util.start(applicationContext, MainActivity::class.java)*/ }
        supportActionBar!!.hide()


        //loading()
        fpm.Init(this)
        //fingerprint()
    }

    override fun onResume() {
        super.onResume()
        loading()
    }
    private fun fingerprint() {
        fpm.Start {
            Log.i(TAG, "result : $it")
            if(it==McFPManager.IAuthentify.AUTH_RESULT.OK) {
                Log.d(TAG, "Fingerprint recognised successfully")
                val intent = Intent(this@StartActivity, SafeActivity::class.java)
                startActivity(intent)
            }
        }
    }
    private fun loading() {
        var longDuration = resources.getInteger(android.R.integer.config_longAnimTime)
        //var shortDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
        logo.alpha = 0f
        //logo.visibility = View.VISIBLE
        Handler().postDelayed(
            {
                Util.circleAppear(main_title)
                Util.circleAppear(desc)
                Util.fadeIn(logo,false)

                fingerprint()
            },
            longDuration.toLong()
        )

    }
}

    /* private fun biometric() {
         *//* Biometric *//*
        if(isHardwareSupported(this))
            setBiometric()
    }

    private fun isHardwareSupported(context : Context): Boolean{
        val fingerprintManager =  FingerprintManagerCompat.from(context)
        return fingerprintManager.isHardwareDetected
    }
    private fun isFingerprintAvailable(context : Context): Boolean {
        val fingerprintManager =  FingerprintManagerCompat.from(this)
        return fingerprintManager.hasEnrolledFingerprints()
    }
    private fun setBiometric(){

        if (!isFingerprintAvailable(this))
            Toast.makeText(this,"Register at least one fingerprint in Settings", Toast.LENGTH_LONG).show()
        else {
            //Create a thread pool with a single thread//
            val newExecutor = Executors.newSingleThreadExecutor()
            val authCallback = object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    } else {
                        Log.d(TAG , "An unrecoverable error occurred")
                    }
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Log.d(TAG, "Fingerprint recognised successfully")
                    val intent = Intent(this@StartActivity, SafeActivity::class.java)
                    startActivity(intent)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Log.d(TAG, "Fingerprint not recognised")
                }
            }

            val myBiometricPrompt = BiometricPrompt(this, newExecutor, authCallback)

            //Create the BiometricPrompt instance//
            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Secure Area!")
                //.setSubtitle("your print please :)")
                //.setDescription("Finger print for authentication...")
                .setNegativeButtonText("Cancel")
                .build()

            myBiometricPrompt.authenticate(promptInfo)
        }

    }*/






