package com.beardedhen.great_ix.db.converter

import androidx.room.TypeConverter
import com.beardedhen.great_ix.db.PublishComments

class PublishCommentsConverter {

    @TypeConverter
    fun getType( numeral: Int ): PublishComments {
        return PublishComments.getType( numeral )
    }

    @TypeConverter
    fun getTypeInt( type: PublishComments ): Int {
        return PublishComments.getTypeInt( type )
    }
}