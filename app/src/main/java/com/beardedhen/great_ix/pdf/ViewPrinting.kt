package com.beardedhen.great_ix.pdf

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import com.beardedhen.great_ix.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


///**
// * This will printAsPdf any view to a pdf.
// * @param jobName: The jobName to use
// * @return the printAsPdf job, so it can be cancelled. Feel free to ignore.
// */
//fun ViewGroup.printAsPdf( pdfFilename: String,
//                           jobName: String = "${System.currentTimeMillis()}_job"): PrintJob {
//    val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
//    return printManager.print(jobName, PagePrintAdapter(this, pdfFilename), null)
//}

fun ViewGroup.saveAsPdf(pdfFilename: String, postcardView: Boolean) : File {
    val document = generatePdfDocument(postcardView)
    try {

        val myPath = File( context.filesDir,"$pdfFilename.pdf")
        document.writeTo(FileOutputStream(myPath))

        return myPath

    } catch (e: IOException) {
        throw e

    } finally {
        document.close()
    }
}

fun ViewGroup.generateBitmap(isPostCard: Boolean = false) : Bitmap {
    if (isPostCard) {
        getA6Dimensions()
    } else {
        val widthMm = context.resources.getInteger(R.integer.a4_width_mm)
        val widthPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, widthMm.toFloat(), resources.displayMetrics)
        val heightPixels = pageHeight(context, widthPixels)

        measure( View.MeasureSpec.makeMeasureSpec(widthPixels.toInt(),
                                                  View.MeasureSpec.EXACTLY),
                                                  View.MeasureSpec.makeMeasureSpec(heightPixels, View.MeasureSpec.EXACTLY))

        layout(0, 0, measuredWidth, measuredHeight)
    }

    // Draw the total bitmap.
    val bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    draw(canvas)

    return bitmap
}

fun ViewGroup.getA6Dimensions() {
    val widthMm = context.resources.getInteger(R.integer.a6_width_mm)
    val heightMm = context.resources.getInteger(R.integer.a6_height_mm)*2
    val widthPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, widthMm.toFloat(), resources.displayMetrics).toInt()
    val heightPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, heightMm.toFloat(), resources.displayMetrics).toInt()

    measure( View.MeasureSpec.makeMeasureSpec(widthPixels, View.MeasureSpec.EXACTLY),View.MeasureSpec.makeMeasureSpec(heightPixels, View.MeasureSpec.EXACTLY))
    layout(0, 0, measuredWidth, measuredHeight)
}

private fun ViewGroup.generatePdfDocument(isPostCard: Boolean): PdfDocument {
    var widthMm = context.resources.getInteger(R.integer.a4_width_mm)
    var heightMm = pageHeight(context, widthMm)
    if (isPostCard) {
        widthMm = context.resources.getInteger(R.integer.a6_width_mm)
        heightMm = context.resources.getInteger(R.integer.a6_height_mm)
    }

    val widthPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, widthMm.toFloat(), resources.displayMetrics)
    var heightPixels = pageHeight(context, widthPixels)
    if (isPostCard) {
        heightPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, heightMm.toFloat(), resources.displayMetrics).toInt()
    }

    measure( View.MeasureSpec.makeMeasureSpec(widthPixels.toInt(), View.MeasureSpec.EXACTLY),View.MeasureSpec.makeMeasureSpec(heightPixels, View.MeasureSpec.EXACTLY))
    layout(0, 0, measuredWidth, measuredHeight)

    // Draw the total bitmap.
    val bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    draw(canvas)

    val document = PdfDocument()
    val page = document.startPage(PdfDocument.PageInfo.Builder(widthMm, heightMm, 1).create())

    // Get the part of the bitmap to show.
    val imageSection = Rect(0, 0, measuredWidth, measuredHeight)
    val destSize = RectF(0f, 0f, widthMm.toFloat(), heightMm.toFloat())

    page.canvas.drawBitmap(bitmap, imageSection, destSize, null)
    document.finishPage(page)
    return document
}


 fun pageHeight(context: Context, width: Int): Int {

    /*
     * Other countries don't use A4 as standard. The US uses some other format (because americans)
     * so we can override that here and give a different page height for americans.
     */

    return (width * context.getFloat(R.dimen.page_height_ratio)).toInt()
}

fun pageHeight(context: Context, width: Float): Int {
    return (width * context.getFloat(R.dimen.page_height_ratio)).toInt()
}


// Convenience function.
fun Context.getFloat(id: Int): Float {
    val outValue = TypedValue()
    resources.getValue(id, outValue, true)
    return outValue.float
}