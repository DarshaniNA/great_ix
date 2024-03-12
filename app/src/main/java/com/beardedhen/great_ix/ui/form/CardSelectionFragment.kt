package com.beardedhen.great_ix.ui.form

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.beardedhen.great_ix.R
import com.beardedhen.great_ix.db.ThankYouCard
import com.beardedhen.great_ix.ui.BaseFieldFragment
import com.beardedhen.great_ix.ui.FormViewModel
import com.beardedhen.great_ix.ui.MainActivity
import com.beardedhen.great_ix.ui.views.SettingsFieldView
import com.beardedhen.great_ix.ui.views.SettingsFieldView.OnOptionClickListener
import kotlinx.android.synthetic.main.fragment_card_selection.*

/**
 * Form Fragment
 */
class CardSelectionFragment : BaseFieldFragment() {

    override val layoutResId: Int
        get() = R.layout.fragment_card_selection

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

                findNavController().navigate( CardSelectionFragmentDirections.actionSelectCardFragmentToPdfPreviewFragment())
            }
        }

        populateFields()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_about -> {
                findNavController().navigate( CardSelectionFragmentDirections.actionAbout() )
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun populateFields() {

        // Show the grid if a selection has previously been made.
        val showSelector = viewModel.currentForm?.thankYouCard != null

        //comment due to the new changes
//        selectorContainer.visibility = if( showSelector ) View.VISIBLE else View.GONE
//
//        viewModel.currentForm?.let { form ->
//            val dbId = if (!showSelector){null} else {SettingsFieldView.YesNo.fromBoolean(showSelector).dbId}
//            selectCardView.populate( dbId,
//                object : OnOptionClickListener {
//                    override fun optionSelected(optionDbId: Int, selected: Boolean) {
//                        selectorContainer.visibility = if (optionDbId == SettingsFieldView.YesNo.YES.dbId ) View.VISIBLE else View.GONE
//                    }
//                }
//            )
//        }

        cardSelectorGrid.adapter = CardAdapter(context as Activity).apply {
            selectedCard = viewModel.currentForm?.thankYouCard?: ThankYouCard.CARD_1
        }
    }

    override fun saveFields() {
        viewModel.currentForm?.thankYouCard = (cardSelectorGrid.adapter as CardAdapter).selectedCard
//        viewModel.currentForm?.thankYouCard = if( selectCardView.getSelectedDbId() == SettingsFieldView.YesNo.YES.dbId ) {
//            (cardSelectorGrid.adapter as CardAdapter).selectedCard
//        } else {
//            null
//        }

        viewModel.currentForm?.let { form ->
            if( form.id == 0L  ) {
                form.id = viewModel.formDao.insert(form)
            } else {
                viewModel.formDao.update( form )
            }
        }
    }

}
