package com.beardedhen.great_ix.ui.form

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.beardedhen.great_ix.R
import com.beardedhen.great_ix.db.FormEntity
import com.beardedhen.great_ix.ui.BaseFieldFragment
import com.beardedhen.great_ix.ui.FormViewModel
import com.beardedhen.great_ix.ui.GreatIxAlert
import com.beardedhen.great_ix.ui.MainActivity
import com.beardedhen.great_ix.ui.event.DoneEvent
import kotlinx.android.synthetic.main.fragment_form.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * Form Fragment
 */
class FormFragment : BaseFieldFragment() {

    private val args: FormFragmentArgs by navArgs()

    override val layoutResId: Int
        get() = R.layout.fragment_form

    override val fieldContainer: ViewGroup
        get() = pageContainer

    private lateinit var viewModel: FormViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).let { mainActivity ->

            viewModel = mainActivity.viewModel

            if( args.formId != -1L ) {
                mainActivity.viewModel.currentForm = viewModel.formDao.get( args.formId )
            } else {
                mainActivity.viewModel.currentForm = null
            }
        }
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        val callback: OnBackPressedCallback = object : OnBackPressedCallback(
//            true
//        ) {
//            override fun handleOnBackPressed() {
//                if ((activity as MainActivity).viewModel.currentForm == null) {
//                    val dialog= AlertDialog.Builder(activity as MainActivity).setMessage("Are you sure you want to exit?").setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, i ->
//                        (activity as MainActivity).finish()
//                    }).show()
//                }
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(
//            this,
//            callback
//        )
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nextButton.setOnClickListener{

            if( validateFields() ) {
                saveFields()
                findNavController().navigate( FormFragmentDirections.actionFormPart1ToPart2())
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

    override fun onStart() {
        super.onStart()

        with (EventBus.getDefault()){
            if (!isRegistered( this@FormFragment ) ) {
                register(this@FormFragment )
            }
        }
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_about -> {
                findNavController().navigate( FormFragmentDirections.actionAbout() )
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun populateFields() {
        viewModel.currentForm?.let { entity ->
            whoWasGreatView.populate(entity.whoWasGreat)
            theirRoleView.populate(entity.theirRole, viewModel.formDao.getTheirRollSeeds())
            whatWasExcellentView.populate(entity.whatWasExcellent, viewModel.formDao.getWhatWasExcellentSeeds())
            lessonToLearnView.populate(entity.lessonToLearn)
            howToImproveView.populate(entity.howToImprove)
            recipientsEmailView.populate( entity.recipientsEmail )
        }
    }

    override fun saveFields() {

        viewModel.currentForm = FormEntity(
            id = viewModel.currentForm?.id?:0,
            yourPlaceOfWork = configuration.yourPlaceOfWork,
            yourOtherPlaceOfWork = configuration.yourOtherPlaceOfWork,
            yourName = configuration.yourName,
            yourRole = configuration.yourRole,
            yourOtherRole = configuration.yourOtherRole,
            yourEmail = configuration.yourEmail,

            whoWasGreat = whoWasGreatView.getValue(),
            theirRole = theirRoleView.getValue(),
            whatWasExcellent = whatWasExcellentView.getValue(),

            lessonToLearn = lessonToLearnView.getValue(),
            howToImprove = howToImproveView.getValue(),

            remainAnonymous = viewModel.currentForm?.remainAnonymous,
            canWePublishComments = viewModel.currentForm?.canWePublishComments,
            thankYouCard = viewModel.currentForm?.thankYouCard, // Null unless previously recorded.
//            recipientsEmail = viewModel.currentForm?.recipientsEmail
            recipientsEmail = recipientsEmailView.getValue()
        )
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public fun onMessageEvent(event: DoneEvent) {

        scrollView.post {
            scrollView.fullScroll(View.FOCUS_DOWN)
        }
    }
}
