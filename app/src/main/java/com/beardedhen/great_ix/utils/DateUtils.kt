package com.beardedhen.great_ix.utils

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {

    companion object {

        private const val DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm a"
        private const val PDF_TIMESTAMP_FORMAT = "yyyy-MM-dd kk:mm:ss"
        private const val LISTING_TIMESTAMP_FORMAT = "dd-MMM-yy kk:mm"
        private const val TIME_24H_FORMAT = "kk:mm"

        /**
         * e.g. 12345 -> 0:12
         * 1234567 -> 20:32
         *
         * @param millis a length of time in millis.
         * @return    That time formatted as mm:ss.
         */
        fun formatMillisAsTime(seconds: Int): String {
            return String.format(Locale.getDefault(), "%d:%02d", seconds / 60, seconds % 60 )
        }

        fun toDisplayDateTime( millisecs: Long ) : String {
            val dateTime = Date(millisecs)
            return SimpleDateFormat(DATE_TIME_FORMAT).format(dateTime)
        }

        fun fromDisplayDateTime( dateTime: String ) : Long {
            val date = SimpleDateFormat(DATE_TIME_FORMAT).parse(dateTime)
            return date.time
        }

        fun getPDFTimestamp() : String {
            return SimpleDateFormat(PDF_TIMESTAMP_FORMAT).format( Date() )
        }


        fun getFormattedDate( recordTime: Long): String {

            val smsTime = Calendar.getInstance()
            smsTime.timeInMillis = recordTime

            val now = Calendar.getInstance()

            val prefix = if (now[Calendar.DATE] == smsTime[Calendar.DATE]) {
                "Today "
            } else if (now[Calendar.DATE] - smsTime[Calendar.DATE] == 1) {
                "Yesterday "
            } else {
                ""
            }

            return if( prefix.isEmpty() ) {
                SimpleDateFormat(LISTING_TIMESTAMP_FORMAT).apply{
                    timeZone = now.timeZone
                }.
                format( smsTime.time)
            } else {
                prefix + SimpleDateFormat(TIME_24H_FORMAT).apply{
                    timeZone = now.timeZone
                }.
                format( smsTime.time)
            }
        }
    }
}