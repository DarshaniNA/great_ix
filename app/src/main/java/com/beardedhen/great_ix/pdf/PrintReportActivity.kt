package com.beardedhen.great_ix.pdf

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.beardedhen.great_ix.R
import com.beardedhen.great_ix.db.FormEntity
import com.beardedhen.great_ix.ui.FormViewModel
import com.beardedhen.great_ix.ui.ViewModelFactory
import com.beardedhen.great_ix.ui.getRecordIdExtra
import com.beardedhen.great_ix.utils.DateUtils
import kotlinx.android.synthetic.main.activity_report.*


class PrintReportActivity: AppCompatActivity() {

    private var pageResizer: PdfReportPageManager? = null

    private var hasPrinted = false

    private lateinit var viewModel: FormViewModel

    private lateinit var formEntity: FormEntity

//    private val pages by lazy { listOf(reportPage1, reportPage2, reportPage3) }
    private val pages by lazy { listOf(reportPage1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        viewModel = ViewModelProviders.of(this, ViewModelFactory(application)).get(FormViewModel::class.java)

        val id = intent.getRecordIdExtra()

        formEntity = viewModel.getCard( id )
        pages.forEach { it.loadReport(formEntity) }
    }

    override fun onResume() {
        super.onResume()

        // Don't go back to this activity.
        if (hasPrinted) {
            finish()
        } else {
            pageResizer = PdfReportPageManager(pages)
            pageResizer!!.layoutAsPages {


                //reportContentParent.isDrawingCacheEnabled = true
//                val screen = getBitmapFromView(reportContentParent) // here give id of our root layout (here its my RelativeLayout's id)
//
//                // Get the print manager.
//                val printHelper = PrintHelper(this)
//
//                // Set the desired scale mode.
//                printHelper.scaleMode = PrintHelper.SCALE_MODE_FIT
//
//                // Print the bitmap.
//                printHelper.printBitmap("Print Bitmap", screen)

                val pdfFilename = formEntity.id.toString() + " - " + DateUtils.getPDFTimestamp()

//                val attachments: List<Uri> = listOf()
//
//                intent.putExtra(Intent.EXTRA_STREAM, pdfFilename)
//                    } else {
//                        if (attachments is ArrayList<*>) {
//                            intent.putParcelableArrayListExtra(
//                                Intent.EXTRA_STREAM,
//                                attachments as ArrayList<Uri?>?
//                            )
//                        } else {
//                            throw IllegalArgumentException("sendEmails - attachments must be an ArrayList")
//                        }
//                    }
//                }
//
//
//                val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
//                intent.type = "text/plain"
//                intent.putExtra(Intent.EXTRA_EMAIL, "andy@beardedhen.com")
//                intent.putExtra(Intent.EXTRA_SUBJECT, "GREAT-ix")
//                intent.putExtra(Intent.EXTRA_TEXT, "Email body.")
//
//                intent.putParcelableArrayListExtra(
//                    Intent.EXTRA_STREAM,
//                    attachments as ArrayList<Uri?>?
//                )
//
//
//                startActivity(Intent.createChooser(intent, "Send Email"))



                //reportContent.saveAsPdf(pdfFilename)
                hasPrinted = true
            }
        }
    }

    private fun getBitmapFromView(view: View): Bitmap {
        //Define a bitmap with the same size as the view
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        //Bind a canvas to it
        val canvas = Canvas(returnedBitmap)
        //Get the view's background
        val bgDrawable = view.background
        if (bgDrawable != null)
        //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas)
        else
        //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE)
        // draw the view on the canvas
        view.draw(canvas)
        //return the bitmap
        return returnedBitmap
    }
}