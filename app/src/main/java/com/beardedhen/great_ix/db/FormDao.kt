package com.beardedhen.great_ix.db

import androidx.room.*

@Dao
abstract class FormDao {

    @Query("SELECT * FROM FormEntity where id = :id")
    abstract fun get( id: Long ): FormEntity

    @Query("SELECT COUNT(*) FROM FormEntity")
    abstract fun count(): Long

    @Insert
    abstract fun insert(value: FormEntity) : Long

    @Update
    abstract fun update(value: FormEntity)

    @Delete
    abstract fun delete(value: FormEntity)

    @Query("SELECT * FROM FormEntity order by timestamp desc")
    abstract fun getAll(): List<FormEntity>

    @Query("SELECT distinct theirRole FROM FormEntity order by theirRole")
    abstract fun getTheirRollSeeds(): List<String>

    @Query("SELECT distinct whatWasExcellent FROM FormEntity order by whatWasExcellent")
    abstract fun getWhatWasExcellentSeeds(): List<String>


}