package mc.apps.safe

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_start.*
import mchou.apps.codelabs.tools.Util

class StartActivity : AppCompatActivity(){
    val TAG : String = "tests"
    var fpm = McFPManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        logo.setOnClickListener { fingerprint() }
        supportActionBar!!.hide()

        fpm.Init(this)

       /* val intent = Intent(this@StartActivity, SafeActivity::class.java)
        startActivity(intent)*/
    }


    override fun onResume() {
        super.onResume()
        loading()
    }
    private fun fingerprint() {
        fpm.Start {
            if(it== McFPManager.IAuthentify.AUTH_RESULT.OK) {
                val intent = Intent(this@StartActivity, SafeActivity::class.java)
                startActivity(intent)
            }
        }
    }
    private fun loading() {
        var longDuration = resources.getInteger(android.R.integer.config_longAnimTime)
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