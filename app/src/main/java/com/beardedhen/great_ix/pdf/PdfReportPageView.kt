package com.beardedhen.great_ix.pdf

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.beardedhen.great_ix.R
import com.beardedhen.great_ix.db.FormEntity
import kotlinx.android.synthetic.main.report_content.view.*
import java.text.SimpleDateFormat
import java.util.*

/*
 * A view that represents each page on the report.
 * The content view will contain ALL report fields - they will be
 * shown/hidden dynamically as they're required.
 */

class PdfReportPageView(context: Context, attrs: AttributeSet? = null): RelativeLayout(context, attrs) {

    companion object {
        val DATE_FORMAT = SimpleDateFormat("dd MMMM yyyy")
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.report_content, this, true)
    }

    fun loadReport(record: FormEntity) {

        val date = DATE_FORMAT.format(Date(record.timestamp))
        recipientsNameView.text = "Dear " + record.whoWasGreat + ","
        dateView.text = date
        fromNameAndDateView.text = record.yourName + " on " + date
        deedDoneView.text = "\"" + record.whatWasExcellent + "\""
    }
}