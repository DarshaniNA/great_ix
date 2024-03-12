package com.beardedhen.great_ix.db


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.beardedhen.great_ix.db.converter.PublishCommentsConverter
import com.beardedhen.great_ix.db.converter.ThankYouCardConverter


/**
 * Room tutorial: https://medium.com/mindorks/room-kotlin-android-architecture-components-71cad5a1bb35
 */
@Database(
    entities = [
        FormEntity::class
    ], version = 7
)

@TypeConverters(
    PublishCommentsConverter::class,
    ThankYouCardConverter::class
)
abstract class GreatIxDatabase : RoomDatabase() {


    abstract fun formDao(): FormDao

    companion object {
        private var INSTANCE: GreatIxDatabase? = null
        private lateinit var context: Context

        fun getDataBase(context: Context): GreatIxDatabase? {
            if (INSTANCE == null){

                this.context = context

                synchronized(GreatIxDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        GreatIxDatabase::class.java, "greatIxDatabase"
                    )
                        .allowMainThreadQueries()       // FIXME - probably OK for this App!
                        .fallbackToDestructiveMigration()       // FIXME!!!
                        .build()
                }
            }
            return INSTANCE
        }
    }
}