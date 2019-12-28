package uz.jamshid.winterlayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        winter.setSnowSize(20)
    }

    fun start(view: View) {
        winter.startWinter()
    }

    fun stop(view: View) {
        winter.stopWinter()
    }
}
