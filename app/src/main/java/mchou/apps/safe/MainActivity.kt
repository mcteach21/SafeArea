package mchou.apps.safe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

      /*  *//* Biometric *//*
        if(isHardwareSupported(this))
            setBiometric()*/
    }

    /*private fun isHardwareSupported(context : Context): Boolean{
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
                        Log.d(TAG, "An unrecoverable error occurred")
                    }
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Log.d(TAG, "Fingerprint recognised successfully")
                    val intent = Intent(this@MainActivity, SafeActivity::class.java)
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


    /*private val KEY_NAME = "example_key"
    private lateinit var fingerprintManager: FingerprintManager
    private lateinit var keyguardManager: KeyguardManager

    private lateinit var keyStore: KeyStore
    private lateinit var keyGenerator: KeyGenerator
    private lateinit var cipher: Cipher

    private fun generateKey() {
        try
        {
            keyStore = KeyStore.getInstance("AndroidKeyStore")
        }
        catch (e:Exception) {
            e.printStackTrace()
        }
        try
        {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore")
        }
        catch (e:NoSuchAlgorithmException) {
            throw RuntimeException("Failed to get KeyGenerator instance", e)
        }
        catch (e:NoSuchProviderException) {
            throw RuntimeException("Failed to get KeyGenerator instance", e)
        }
        try
        {
            keyStore.load(null)
            keyGenerator.init(
                KeyGenParameterSpec.Builder(KEY_NAME,
                (KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT))
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setUserAuthenticationRequired(true)
                .setEncryptionPaddings(
                    KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .build())
            keyGenerator.generateKey()
        }
        catch (e:NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
        catch (e:InvalidAlgorithmParameterException) {
            throw RuntimeException(e)
        }
        catch (e:CertificateException) {
            throw RuntimeException(e)
        }
        catch (e:IOException) {
            throw RuntimeException(e)
        }
    }

    //init the Cipher
    @TargetApi(Build.VERSION_CODES.M)
    fun cipherInit():Boolean {
        try
        {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES
                    +KeyProperties.BLOCK_MODE_CBC
                    +KeyProperties.ENCRYPTION_PADDING_PKCS7)
        }
        catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed get Cipher")
        }
        catch (e: NoSuchPaddingException) {
            throw RuntimeException("Failed  to get Cipher")
        }
        try
        {
            keyStore.load(null)
            val key = keyStore.getKey(KEY_NAME, null) as SecretKey
            cipher.init(Cipher.ENCRYPT_MODE, key) //Cipher initialization using secret key
            return true
        }
        catch (e: KeyPermanentlyInvalidatedException) {
            return false
        }
        catch (e : KeyStoreException) {
            throw RuntimeException("Failed init Cipher")
        }
        catch (e: CertificateException) {
            throw RuntimeException("Failed init Cipher")
        }
        catch (e: UnrecoverableKeyException) {
            throw RuntimeException("Failed init Cipher")
        }
        catch (e: IOException) {
            throw RuntimeException("Failed init Cipher")
        }
        catch (e:NoSuchAlgorithmException) {
            throw RuntimeException("Failed init Cipher")
        }
        catch (e: InvalidKeyException) {
            throw RuntimeException("Failed init Cipher")
        }
    }*/
}
