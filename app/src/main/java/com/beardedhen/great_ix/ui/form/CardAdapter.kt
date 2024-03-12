package com.beardedhen.great_ix.ui.form

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.beardedhen.great_ix.R
import com.beardedhen.great_ix.db.ThankYouCard


class CardAdapter(private val context: Activity) : BaseAdapter() {

    var selectedCard: ThankYouCard? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val inflater = context.layoutInflater

        val rowView = inflater.inflate(R.layout.grid_item,parent,false)
        val imageView = rowView.findViewById<ImageView>(R.id.thankYouCardView)

        val thankYouCard = ThankYouCard.values()[position]

        imageView.setImageResource( thankYouCard.drawableResId )

        setSelection( thankYouCard, rowView )

        imageView.tag = thankYouCard

        imageView.setOnClickListener {

            selectedCard = it.tag as ThankYouCard
            setSelection(thankYouCard, it)

            notifyDataSetChanged()
        }

        return rowView
    }

    private fun setSelection(
        option: ThankYouCard,
        rowView: View
    ) {
        if (option == selectedCard) {
            rowView.setBackgroundResource( R.color.yellow )
        } else {
            rowView.setBackgroundResource( R.color.light_grey )
        }
    }

    override fun getItem(position: Int): Any {
        return ThankYouCard.values()[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return ThankYouCard.values().count()
    }
}
