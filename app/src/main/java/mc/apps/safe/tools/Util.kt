package mchou.apps.codelabs.tools

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.core.animation.doOnEnd
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import android.R
import android.content.pm.PackageManager



class Util {

    /**
     * static
     */
    companion object{

        /**
         * Activities
         */
        fun start(context: Context, activity : Class<*>) {
            val intent = Intent(context,activity)
            context.startActivity(intent)
        }

        /**
         * Html
         */
        fun toHtml(txt : String): Spanned {
            @Suppress("DEPRECATION")
            return (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                Html.fromHtml(txt, Html.FROM_HTML_MODE_LEGACY) else
                Html.fromHtml(txt))
        }

        /**
         * Animations
         */
        fun circleAppear(view: View) {
            val duration = view.context.resources.getInteger(android.R.integer.config_longAnimTime).toLong()
            val cx = view.width / 2
            val cy = view.height / 2
            val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()
            val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius)
            view.visibility = View.VISIBLE
            anim.setDuration(duration).start()
        }
        fun circleVanish(view: View) {
            val duration = view.context.resources.getInteger(android.R.integer.config_longAnimTime).toLong()
            val cx = view.width / 2
            val cy = view.height / 2
            val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()
            val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, finalRadius, 0f)

            anim.setDuration(duration).doOnEnd { view.visibility = View.GONE  }
            anim.start()
        }

        fun fadeIn(view: View, fast : Boolean){
            view.alpha = 0.2f
            val anim_duration : Long = if(fast) 500  else 1500

            view.animate().apply {
                interpolator = LinearInterpolator()
                duration = anim_duration
                alpha(1f)
                startDelay = 500
                start()
            }
        }

        /**
         * Directory & Files
         */
        //https://stackoverflow.com/questions/30480906/save-bitmap-to-app-folder
        fun save(context : Context, bitmap : Bitmap, name : String) : Boolean {
            var dir = getAppFolder(context, "bitmaps")
            if(!dir.exists())
                return false

            val file = File(dir, name)
            var fos: FileOutputStream?
            try {
                fos = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.close()
                //Toast.makeText(context,"Code Imge saved!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                //Log.i("tests","Save - Error : ${e.message}")
                return false
            }

            return true
        }

        private fun getAppFolder(context : Context, subDir : String): File {
            //var path = Environment.getExternalStorageDirectory().canonicalPath+File.separator+directory
            val appFilesDir = context.filesDir
            val dir = File(appFilesDir, subDir)
            if(!dir.exists())
                dir.mkdir()
            return dir
        }


        /**
         * Email ..
         */
        /**
         * Email client intent to send support mail
         * Appends the necessary device information to email body
         * useful when providing support
         */
        fun sendEmail(context: Context) {
            var body: String? = null
            try {
                body = context.packageManager.getPackageInfo(context.packageName, 0).versionName
                body =
                    "\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
                            Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
                            "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER
            } catch (e: PackageManager.NameNotFoundException) {
            }

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "message/rfc822"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("mchou120@gmail.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Query from android app")
            intent.putExtra(Intent.EXTRA_TEXT, body)
            context.startActivity(Intent.createChooser(intent,"mc")) // context.getString(R.string.choose_email_client )))
        }
    }
}