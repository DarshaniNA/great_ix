package com.beardedhen.great_ix.ui.form

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.beardedhen.great_ix.R
import com.beardedhen.great_ix.db.PublishComments
import com.beardedhen.great_ix.ui.BaseFieldFragment
import com.beardedhen.great_ix.ui.FormViewModel
import com.beardedhen.great_ix.ui.GreatIxAlert
import com.beardedhen.great_ix.ui.MainActivity
import com.beardedhen.great_ix.ui.views.SettingsFieldView
import kotlinx.android.synthetic.main.fragment_form_part_2.*

/**
 * Form Fragment - Part 2
 */
class FormPart2Fragment : BaseFieldFragment() {

    override val layoutResId: Int
        get() = R.layout.fragment_form_part_2

    override val fieldContainer: ViewGroup
        get() = pageContainer

    private lateinit var viewModel: FormViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as MainActivity).let { mainActivity ->
            viewModel = mainActivity.viewModel
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nextButton.setOnClickListener{

            if( validateFields() ) {
                saveFields()
                findNavController().navigate( FormPart2FragmentDirections.actionFormPart2ToCardSelection())
            } else {
                GreatIxAlert( context = context!!,
                    message = "Please complete these details" ).display()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        populateFields()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_about -> {
                findNavController().navigate( FormPart2FragmentDirections.actionAbout() )
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun populateFields() {
        viewModel.currentForm?.let { entity ->
            remainAnonymousView.populate(entity.remainAnonymous)
            canWePublishView.populate(entity.canWePublishComments?.dbId)
//            recipientsEmailView.populate( entity.recipientsEmail )
        }
    }

    override fun saveFields() {

        viewModel.currentForm?.let { formEntity ->

            formEntity.remainAnonymous = remainAnonymousView.getSelectedDbId() == SettingsFieldView.YesNo.YES.dbId
            formEntity.canWePublishComments = PublishComments.getType(canWePublishView.getSelectedDbId())
//            formEntity.recipientsEmail = recipientsEmailView.getValue()
        }
    }
}
