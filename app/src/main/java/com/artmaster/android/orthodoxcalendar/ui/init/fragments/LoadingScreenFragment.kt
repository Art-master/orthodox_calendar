package com.artmaster.android.orthodoxcalendar.ui.init.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.LOADING_ANIMATION_DURATION
import com.artmaster.android.orthodoxcalendar.data.components.CustomizableTextView
import java.util.*

/**
 * Class create Fragment and execute animation of text in it.
 */
class LoadingScreenFragment : Fragment() {

    lateinit var loadingScreenText: CustomizableTextView

    var time: Long = LOADING_ANIMATION_DURATION.toLong()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data = arguments?.getLong(Constants.Keys.ANIM_TIME.value)
        data?.let { time = it }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_loading_screen, container, false)
        loadingScreenText = view.findViewById(R.id.loadingScreenText)
        loadingScreenText.setDurationAnim(time)
        loadingScreenText.setVisible(true)
        loadingScreenText.setText(getResourceText())
        loadingScreenText.reverseAnimation()
        loadingScreenText.show()

        return view
    }

    private fun getResourceText(): String {
        val strings = resources.getStringArray(R.array.loading_strings_array)
        val num = Random().nextInt(strings.size.dec())
        return strings[num]
    }

    override fun onPause() {
        super.onPause()
        loadingScreenText.hide()
        loadingScreenText.setText("")
        loadingScreenText.cancelAnimation()
        loadingScreenText.clearAnimation()
    }
}
