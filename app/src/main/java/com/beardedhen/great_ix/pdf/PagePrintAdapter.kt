//package com.beardedhen.great_ix.pdf
//
//import android.content.Intent
//import android.graphics.Bitmap
//import android.graphics.Canvas
//import android.graphics.Rect
//import android.graphics.RectF
//import android.net.Uri
//import android.os.Bundle
//import android.os.CancellationSignal
//import android.os.ParcelFileDescriptor
//import android.print.PageRange
//import android.print.PrintAttributes
//import android.print.PrintDocumentAdapter
//import android.print.PrintDocumentInfo
//import android.print.pdf.PrintedPdfDocument
//import android.view.ViewGroup
//import androidx.core.content.ContextCompat.startActivity
//import androidx.core.content.FileProvider
//import com.beardedhen.great_ix.BuildConfig
//import java.io.File
//import java.io.FileOutputStream
//import java.io.IOException
//
//
///**
// * This will printAsPdf a view to pdf using the system dialog
// *
// * It is used in the printAsPdf method, in ViewPrinting.kt.
// *
// * @param view: The view to printAsPdf
// */
//class PagePrintAdapter(private val view: ViewGroup,
//                       private val pdfFilename: String ) : PrintDocumentAdapter() {
//
//    private val context = view.context
//
//    private lateinit var document: PrintedPdfDocument
//
//    private var numberOfPages = 0
//
//
//    override fun onLayout(oldAttributes: PrintAttributes?, newAttributes: PrintAttributes?, cancellationSignal: CancellationSignal?, callback: LayoutResultCallback?, extras: Bundle?) {
//
//        document = PrintedPdfDocument(context, newAttributes!!)
//
//        if (cancellationSignal != null && cancellationSignal.isCanceled) {
//            callback?.onLayoutCancelled()
//            return
//        }
//
//        // A page for each PdfPageView.
//        // If the page doesn't have any content - ignore it.
////        numberOfPages = view.views
////                .mapNotNull { it as? PdfReportPageView }
////                .filter { it.contentView.numberOfVisibleChildren() > 0 }
////                .count()
//        numberOfPages = 1
//
//        val info = PrintDocumentInfo.Builder( pdfFilename )
//                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
//                .setPageCount(numberOfPages)
//                .build()
//
//        callback?.onLayoutFinished(info, true)
//
//    }
//
//    // Called by the system to start writing.
//    override fun onWrite(pages: Array<out PageRange>?,
//                         destination: ParcelFileDescriptor?,
//                         cancellationSignal: CancellationSignal?,
//                         callback: WriteResultCallback?) {
//
//        // Draws everything to the document.
//        drawPages()
//        saveToFile(destination, callback)
//
//        //val pageRange = arrayOf(PageRange(0, document.pages.size))
//        //callback?.onWriteFinished(pageRange)
//
//
////        try {
////            document.writeTo( FileOutputStream( destination!!.fileDescriptor) )
////        } catch (e: IOException) {
////            callback!!.onWriteFailed(e.toString())
////            return
////        } finally {
////            document.close()
////        }
//    }
//
//
//    /**
//     * Draws every page to the document as individual pages.
//     */
//    private fun drawPages() {
//
//        // Draw the total bitmap.
//        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(bitmap)
//        view.draw(canvas)
//        val pageHeight = pageHeight(context, view.width)
//
//        for (i in 0 until numberOfPages) {
//
//            val page = document.startPage(i)
//
//            // Get the part of the bitmap to show.
//            val imageSection = Rect(0, i * pageHeight, view.width, (i+1) * pageHeight)
//
//            val destSize = RectF(0f, 0f,
//                    page.canvas.width.toFloat(), page.canvas.height.toFloat())
//
//            page.canvas.drawBitmap(bitmap, imageSection, destSize, null)
//
//            document.finishPage(page)
//        }
//
//    }
//
//
//    private fun saveToFile(destination: ParcelFileDescriptor?, callback: WriteResultCallback?) {
//
//        try {
//
//            val myPath = File( context.filesDir,"thank_you.pdf");
//            document.writeTo(FileOutputStream(myPath))
//
//           // val path: Uri = Uri.fromFile(myPath)
//            val path: Uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", myPath)
//            val emailIntent = Intent(Intent.ACTION_SEND)
//
//            // set the type to 'email'
//            emailIntent.type = "vnd.android.cursor.dir/email"
//            val to = arrayOf("asd@gmail.com")
//            emailIntent.putExtra(Intent.EXTRA_EMAIL, to)
//            // the attachment
//            emailIntent.putExtra(Intent.EXTRA_STREAM, path)
//            // the mail subject
//            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject")
//            startActivity(context, Intent.createChooser(emailIntent, "Send email..."), null)
//
//        } catch (e: IOException) {
//            callback?.onWriteFailed(e.localizedMessage)
//
//        } finally {
//            document.close()
//        }
//
////        try {
////            document.writeTo(FileOutputStream(destination?.fileDescriptor))
////
////        } catch (e: IOException) {
////            callback?.onWriteFailed(e.localizedMessage)
////            return
////
////        } finally {
////            document.close()
////        }
//    }
//
//}