package com.beardedhen.great_ix.pdf

import android.view.ViewTreeObserver

/*
 * In android you can't get the layout of a view until it's been drawn.
 * In order to get around this we need a ViewTreeObserver to detect when
 * the view has been drawn, and do our calculations there.
 */

class PdfReportPageManager( private val pages: List<PdfReportPageView> ):
        ViewTreeObserver.OnGlobalLayoutListener {


    private var onComplete: ((List<PdfReportPageView>) -> Unit)? = null

    // Remember the page we listened to, so we can remove the listener.
    private var pageWithListener: PdfReportPageView? = null


    fun layoutAsPages(listener: ((List<PdfReportPageView>) -> Unit)?) {

        this.onComplete = listener

        if (pages.isEmpty()) {
            onComplete?.invoke(listOf())

        } else {
            val first = pages[0]

            // If it's been laid out, it will have a non-zero width.
            // Otherwise, we need to wait for it to be laid out.
            if (first.width == 0) {
                pageWithListener = first
                first.viewTreeObserver.addOnGlobalLayoutListener(this)

            } else {
                onGlobalLayout()
            }

        }
    }


    // This is called once the page has been drawn.
    override fun onGlobalLayout() {

        // Very important! Cancel listener to prevent infinite loop.
        if (pageWithListener != null) {
            pageWithListener!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
        }

        // Go through each page, and show as much on there as possible.
        var numberOfChildren = 0

//        for (page in pages) {
//
//            page.contentView.hideFirstChildren(numberOfChildren)
//            page.cutToA4()
//            numberOfChildren += page.contentView.numberOfVisibleChildren()
//
//        }

        onComplete?.invoke(pages)
    }

}