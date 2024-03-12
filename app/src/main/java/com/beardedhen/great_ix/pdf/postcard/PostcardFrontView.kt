package com.beardedhen.great_ix.pdf.postcard

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.beardedhen.great_ix.R
import com.beardedhen.great_ix.db.FormEntity
import kotlinx.android.synthetic.main.postcard_front_content.view.*

class PostcardFrontView (context: Context, attrs: AttributeSet? = null): RelativeLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.postcard_front_content, this, true)
    }

    fun loadSelectedThankyouCard(record: FormEntity) {
        postcardFront.setImageResource(record.thankYouCard?.drawableResId?:R.drawable.card_not_selected)
    }
}
