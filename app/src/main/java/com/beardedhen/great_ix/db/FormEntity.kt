package com.beardedhen.great_ix.db

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.beardedhen.great_ix.R
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class FormEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    val timestamp: Long = Date().time,

    val yourPlaceOfWork: String,
    val yourOtherPlaceOfWork:String,
    val yourName: String,
    val yourRole: String,
    val yourOtherRole:String,
    val yourEmail: String,

    val whoWasGreat: String,
    val theirRole: String,
    val whatWasExcellent: String,

    val lessonToLearn: String,
    val howToImprove: String,

    var remainAnonymous: Boolean?,
    var canWePublishComments: PublishComments?,
    var thankYouCard: ThankYouCard?,

    var recipientsEmail:String?
) {

    companion object {
        val DATE_FORMAT = SimpleDateFormat("dd-MMM-yy hh:mm")
    }

    fun toCSV( context: Context ) : String {

        val output = listOf<String>(
            DATE_FORMAT.format( Date( timestamp )),
            yourPlaceOfWork,
            if( yourPlaceOfWork == "Other" ) yourOtherPlaceOfWork else "",
            yourName,
            yourRole,
            if( yourRole == "Other" ) yourOtherRole else "",
            yourEmail,
            whoWasGreat,
            theirRole,
            whatWasExcellent,
            lessonToLearn,
            howToImprove,
            remainAnonymous.toString(),
            canWePublishComments.toString(),
            context.resources.getString( thankYouCard?.cardNameResId?: R.string.no_card_selected ),
            recipientsEmail?:""
        ).map { it.replace(",", "\\u2C") }

        return output.joinToString( separator = "," )
    }
}