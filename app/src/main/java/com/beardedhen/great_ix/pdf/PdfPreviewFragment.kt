package com.beardedhen.great_ix.pdf

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.beardedhen.great_ix.BuildConfig
import com.beardedhen.great_ix.R
import com.beardedhen.great_ix.configuration.Configuration
import com.beardedhen.great_ix.db.FormEntity
import com.beardedhen.great_ix.export.FileManager
import com.beardedhen.great_ix.ui.FormViewModel
import com.beardedhen.great_ix.ui.GreatIxAlert
import com.beardedhen.great_ix.ui.MainActivity
import kotlinx.android.synthetic.main.activity_report.view.*
import kotlinx.android.synthetic.main.fragment_form.*
import kotlinx.android.synthetic.main.fragment_pdf_preview.*
import kotlinx.android.synthetic.main.postcard_preview.view.*
import java.io.File


class PdfPreviewFragment : Fragment() {

    private lateinit var viewModel: FormViewModel
    private val configuration = Configuration.getInstance()

    private val formPostcardView: ViewGroup by lazy {
        val inflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val root = inflater.inflate(R.layout.postcard_preview, formRoot, false) as ViewGroup
        root.postcardContent.postcardBackView.loadPostcardDetails(viewModel.currentForm!!)
        root.postcardContent.postcardFrontView.loadSelectedThankyouCard(viewModel.currentForm!!)
        root
    }

    private val formPostcardBackView: ViewGroup by lazy {
        val inflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val root = inflater.inflate(R.layout.postcard_preview, formRoot, false) as ViewGroup
        root.postcardContent.postcardBackView.loadPostcardDetails(viewModel.currentForm!!)
        root.postcardContent.postcardBackView.removePostcardBackground()
        root.postcardContent.postcardFrontView.visibility = View.GONE
        root
    }

    private val formReportView: ViewGroup by lazy {
        val inflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val root = inflater.inflate(R.layout.activity_report, formRoot, false) as ViewGroup
        root.reportContent.reportPage1.loadReport(viewModel.currentForm!!)
        root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as MainActivity).let { mainActivity ->
            viewModel = mainActivity.viewModel
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pdf_preview, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.action_settings)?.isVisible = false
        menu.findItem(R.id.action_about)?.isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_about -> {
                findNavController().navigate( PdfPreviewFragmentDirections.actionAbout() )
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var previewPostcard = true
        previewSwitcher.displayedChild = 1
        postcardContainer.setImageBitmap(formPostcardView.generateBitmap(true))

        previewButton.setOnClickListener {

            if (previewPostcard) {
                previewSwitcher.displayedChild = 0
                pdfContainer.setImageBitmap(formReportView.generateBitmap())
                previewButton.text = resources.getString(R.string.postcard_preview)
                previewPostcard = false
            } else {
                previewSwitcher.displayedChild = 1
                postcardContainer.setImageBitmap(formPostcardView.generateBitmap(true))
                previewButton.text = resources.getString(R.string.letter_preview)
                previewPostcard = true
            }

        }
        submitButton.setOnClickListener {
            // Disable the button to present double tabs as we are waiting for the email
            // to be generated
            submitButton.isEnabled = false

            GreatIxAlert( context = context!!,
                title = resources.getString(R.string.submit_confirmation_title),
                positiveButtonFunction = {
                    submitDocuments()
                },
                message = resources.getString(R.string.submit_confirmation)).display()
        }
    }

    private fun submitDocuments() {
        viewModel.currentForm?.let { form ->
            val formPDF = saveAsPdf( form )
            val postcardPDF = saveAsPdf(form, true)
            val csfFile = FileManager.createCSVExportFile(context!!, form )
            sendEmail( postcardPDF, formPDF, csfFile, configuration.yourName, form.whoWasGreat )
        }

        findNavController().navigate( PdfPreviewFragmentDirections.actionSubmitForm())
    }

    private fun saveAsPdf(formEntity: FormEntity, isPostcardView: Boolean = false) : File {

        val pdfFilename = FileManager.buildEmailFilename( context!!, formEntity )

        return if (isPostcardView) {
            formPostcardBackView.saveAsPdf("$pdfFilename postcard", isPostcardView)
        } else {
            formReportView.saveAsPdf("$pdfFilename letter", isPostcardView)
        }
    }

    private fun sendEmail(postcardPdf: File,
                          formPdf: File,
                          formCsv: File,
                          fromName:String,
                          whoWasGreat: String) {

        // See https://guides.codepath.com/android/Sharing-Content-with-Intents
        val provider: String = BuildConfig.APPLICATION_ID + ".provider"
        val pdfUri = FileProvider.getUriForFile(context!!, provider, formPdf)
        val postcardPdfUri = FileProvider.getUriForFile(context!!, provider, postcardPdf)
        val csvUri = FileProvider.getUriForFile(context!!, provider, formCsv)
        val attachments: ArrayList<Uri> = arrayListOf(postcardPdfUri, pdfUri, csvUri)

        val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_EMAIL, resources.getStringArray(R.array.recipient_email))
        intent.putExtra(Intent.EXTRA_SUBJECT, "GREAT-ix - $fromName commends $whoWasGreat")
        intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.email_body))
        intent.putParcelableArrayListExtra( Intent.EXTRA_STREAM, attachments )

        startActivity(Intent.createChooser(intent, "Send Email"))
    }


}
