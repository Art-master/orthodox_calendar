package com.artmaster.android.orthodoxcalendar.ui.review.mvp

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.common.OrtUtils
import com.artmaster.android.orthodoxcalendar.common.msg.Error
import com.artmaster.android.orthodoxcalendar.common.msg.Message
import com.artmaster.android.orthodoxcalendar.common.msg.Warning
import com.artmaster.android.orthodoxcalendar.data.font.CustomFont
import com.artmaster.android.orthodoxcalendar.data.font.CustomLeadingMarginSpan2
import com.artmaster.android.orthodoxcalendar.data.font.JustifiedTextView
import com.artmaster.android.orthodoxcalendar.databinding.FragmentHolidayBinding
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.ui.MessageBuilderFragment
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.mvp.MainCalendarActivity
import com.artmaster.android.orthodoxcalendar.ui.review.impl.HolidayReviewContract
import com.artmaster.android.orthodoxcalendar.ui.user_holiday.mvp.UserHolidayActivity
import com.squareup.picasso.Picasso
import dagger.android.support.AndroidSupportInjection
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter

class HolidayFragment : MvpAppCompatFragment(), HolidayReviewContract.View {

    @InjectPresenter(tag = "HolidayFragmentPresenter")
    lateinit var presenter: HolidayReviewPresenter

    private var _binding: FragmentHolidayBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(holiday: Holiday): HolidayFragment {
            val intent = Intent()
            intent.putExtra(Constants.Keys.HOLIDAY_ID.value, holiday.id)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

            val fragment = HolidayFragment()
            fragment.arguments = intent.extras
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)

        if (!presenter.isInRestoreState(this)) {
            presenter.attachView(this)
            val id = requireArguments().getLong(Constants.Keys.HOLIDAY_ID.value)
            presenter.init(id)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, groupContainer: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHolidayBinding.inflate(inflater, groupContainer, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (_binding != null) presenter.viewIsReady()
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        when (result.resultCode) {
            Constants.Keys.HOLIDAY.hashCode() -> {
                val id = requireArguments().getLong(Constants.Keys.HOLIDAY_ID.value)
                presenter.init(id)
            }
        }
    }

    override fun initButtons(holiday: Holiday) {
        _binding?.apply {
            if (holiday.isCreatedByUser.not()) {
                editHolidayButton.hide()
                deleteHolidayButton.hide()
                return
            }
            editHolidayButton.setOnClickListener {
                val intent = buildIntentForEditHolidayActivity(holiday)
                resultLauncher.launch(intent)
            }

            deleteHolidayButton.setOnClickListener {
                showConfirmationMessage(Warning.DELETE_HOLIDAY)
            }
        }
    }

    private fun showConfirmationMessage(msgType: Warning) {
        val bundle = Bundle()
        bundle.putString(Message.TYPE, msgType.name)

        val dialogFragment = MessageBuilderFragment()
        dialogFragment.arguments = bundle
        dialogFragment.show(parentFragmentManager, "confirmDelete")
        dialogFragment.onClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    presenter.removeHoliday()
                    startActivity(buildIntentForMainActivity())
                    onDestroy()
                }
            }
        }
    }

    private fun buildIntentForMainActivity(): Intent {
        val intent = Intent(requireContext(), MainCalendarActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        intent.putExtra(Constants.Keys.HOLIDAY.value, Constants.Keys.HOLIDAY.hashCode())
        return intent
    }

    private fun buildIntentForEditHolidayActivity(holiday: Holiday): Intent {
        val intent = Intent(requireContext(), UserHolidayActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        intent.putExtra(Constants.Keys.HOLIDAY.value, holiday)
        intent.putExtra(Constants.Keys.NEED_UPDATE.value, true)
        return intent
    }

    override fun showHolidayName(name: String) {
        _binding?.apply {
            holidayNameInPage.text = name
        }
    }

    override fun showDescription(initialLater: String, description: String) {
        if (description.isEmpty() || _binding == null) return

        _binding?.apply {
            buildInitialLater(initialLater)
            val desc = prepareDescription(description)
            relativeLayout.addView(desc)
        }
    }

    private fun prepareDescription(description: String): JustifiedTextView {
        return JustifiedTextView(requireContext()).apply {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.size_text_basic).toFloat())
            setTypeface(CustomFont.getFont(requireContext(), getString(R.string.font_basic)))
            setLeadingMargin(3, 8)
            val strWithoutSpaces = deleteSpaces(description)
            setText(getTextByPadding(strWithoutSpaces))
            setTextColor(binding.newDateStyleTextView.textColors)
        }
    }

    private fun deleteSpaces(string: String): String {
        val deleteSpaces = Regex.fromLiteral("[\\s&&[^\t?\n]]+")
        return string.replace(deleteSpaces, " ")
    }

    private fun buildInitialLater(later: String) {
        binding.initialLaterTextView.apply {
            val textName = resources.getString(R.string.font_for_bukvica)
            val size = resources.getDimensionPixelSize(R.dimen.size_text_bukvica).toFloat()
            setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
            setTextColor(Color.RED)
            typeface = CustomFont.getFont(requireContext(), textName)
            scaleX = 2F
            text = later
        }
    }

    private fun getTextByPadding(string: String): SpannableString {
        val finalStr = SpannableString(string)
        val size = OrtUtils.convertDpToPixels(requireContext(), 25f)
        finalStr.setSpan(CustomLeadingMarginSpan2(3, size), 0, finalStr.length, 0)
        return finalStr
    }

    override fun showNewStyleDate(date: String, isCustomHoliday: Boolean) {
        if (date.isEmpty() || _binding == null) return

        _binding?.apply {
            val str = getString(R.string.new_style_date_string, date)
            newDateStyleTextView.text = if (isCustomHoliday) date else str
        }
    }

    override fun showOldStyleDate(date: String) {
        if (date.isEmpty() || _binding == null) return

        val str = getString(R.string.old_style_date_string, date)
        binding.oldDateStyleTextView.text = str
    }

    override fun showImageHoliday(resId: Int, placeholderId: Int) {
        if (_binding == null) return
        Picasso.get()
                .load(resId)
                .placeholder(placeholderId)
                .error(placeholderId)
                .into(binding.imageHoliday)
    }

    override fun showErrorMessage(msgType: Error) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.destroyView(this)
        _binding = null
    }
}
