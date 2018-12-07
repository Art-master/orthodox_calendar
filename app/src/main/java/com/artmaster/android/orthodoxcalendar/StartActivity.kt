package com.artmaster.android.orthodoxcalendar

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.artmaster.android.orthodoxcalendar.ui.init.mvp.InitAppActivity

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(applicationContext, InitAppActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }
}
