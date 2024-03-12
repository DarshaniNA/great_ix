package com.beardedhen.great_ix.db.converter

import androidx.room.TypeConverter
import com.beardedhen.great_ix.db.ThankYouCard

class ThankYouCardConverter {

    @TypeConverter
    fun getType( numeral: Int? ): ThankYouCard? {
        return numeral?.let { ThankYouCard.getType( numeral ) } 
    }

    @TypeConverter
    fun getTypeInt( type: ThankYouCard? ): Int? {
        return type?.let { ThankYouCard.getTypeInt( type ) }
    }
}