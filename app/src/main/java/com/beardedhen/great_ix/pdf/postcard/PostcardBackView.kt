package com.beardedhen.great_ix.pdf.postcard

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.beardedhen.great_ix.R
import com.beardedhen.great_ix.db.FormEntity
import kotlinx.android.synthetic.main.postcard_back_content.view.*
import java.text.SimpleDateFormat
import java.util.*

class PostcardBackView (context: Context, attrs: AttributeSet? = null): RelativeLayout(context, attrs) {

    companion object {
        val DATE_FORMAT = SimpleDateFormat("dd MMMM yyyy")
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.postcard_back_content, this, true)
    }

    fun loadPostcardDetails(record: FormEntity) {

        val date = DATE_FORMAT.format(Date(record.timestamp))
        nameNominated.text = "Dear " + record.whoWasGreat + ",\n"
        formDate.text = date
        thankYouView.text = "Thank you, " + record.yourName + " :-)"
        msgFromSubmission.text = record.whatWasExcellent + "\n"
    }

    fun removePostcardBackground() {
        contentView.background = resources.getDrawable(R.color.Transparent)
    }

}
