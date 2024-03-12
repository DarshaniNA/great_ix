package com.beardedhen.great_ix.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.beardedhen.great_ix.GreatIxApplication.Companion.context
import com.beardedhen.great_ix.R
import com.beardedhen.great_ix.db.FormEntity
import com.beardedhen.great_ix.utils.DateUtils
import kotlinx.android.synthetic.main.form_list_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class FormsListAdapter(
    private val formModels: List<FormEntity>
): RecyclerView.Adapter<FormsListAdapter.FormItemViewHolder>() {

    companion object {
        val DATE_FORMAT = SimpleDateFormat("dd-MMM-yy kk:mm")
    }

    inner class FormItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: FormEntity) = with(itemView) {
            nameLabel.text = item.whoWasGreat
            dateLabel.text = DateUtils.getFormattedDate( Date(item.timestamp).time )

            item.thankYouCard?.let { thankYouCard ->
                cardImageView.setImageResource( thankYouCard.drawableResId )
            } ?: run {
                cardImageView.setImageDrawable(null)
            }

            formItemContainer.setOnClickListener { view ->

                val action = ListingFragmentDirections.actionListingFragmentToFormFragment(item.id)

                findNavController( view ).navigate( action )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormItemViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.form_list_item, parent, false)
        return FormItemViewHolder(v)
    }

    override fun getItemCount(): Int {
        return formModels.size
    }

    override fun onBindViewHolder(holder: FormItemViewHolder, position: Int) {
        holder.bind(formModels[position])
    }
}
