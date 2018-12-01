package com.artmaster.android.orthodoxcalendar.presentation.init.mvp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.LOADING_ANIMATION
import com.artmaster.android.orthodoxcalendar.data.font.CustomizableTextView

import java.util.Random

/**
 * Class create Fragment and execute animation of text in it.
 * Created by Art-_-master on 29.06.2017.
 */
class LoadingScreenFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_loading_screen, container, false)

        val loadingScreenText: CustomizableTextView = view.findViewById(R.id.loadingScreenText)
        loadingScreenText.setDurationAnim(LOADING_ANIMATION)
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
}
