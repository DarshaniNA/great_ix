package com.beardedhen.great_ix.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.beardedhen.great_ix.R
import com.beardedhen.great_ix.configuration.Configuration
import com.beardedhen.great_ix.ui.extensions.settingViews
import com.beardedhen.great_ix.utils.DisplayUtil

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
abstract class BaseFieldFragment : Fragment() {

    val configuration = Configuration.getInstance()

    abstract val layoutResId: Int
    abstract val fieldContainer: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(layoutResId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // To prevent the keyboard to be showing whenever we pause the fragment.
        DisplayUtil.setHideKeyboardOnBackgroundTouch(this.fieldContainer)

        (activity as MainActivity).refreshButtons( false )
    }

    override fun onPause() {
        super.onPause()

        // To prevent the keyboard to be showing whenever we pause the fragment.
        DisplayUtil.hideSoftKeyboard(this.fieldContainer)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.action_settings)?.isVisible = false
        menu.findItem(R.id.action_about)?.isVisible = true
    }

    /**
     * Used to validate fields (fields must be numeric, greater than 0 and redThreshold > yellowThreshold)
     */
    protected fun validateFields() : Boolean {

        var areFieldsValid = true

        fieldContainer.settingViews.map {
            if( it.visibility == View.VISIBLE && !it.validate() ) {
                areFieldsValid = false
            }
        }

        return areFieldsValid
    }

    open fun populateFields() {

        val settingViews = fieldContainer.settingViews

        settingViews.map {
            it.populate(configuration)
        }
    }
    /**
     * Used to save settings fields to SharedPreferences.
     */
    open fun saveFields() {

        val settingViews = fieldContainer.settingViews

        settingViews.map {
            it.storeValue(configuration)
        }
    }
}
