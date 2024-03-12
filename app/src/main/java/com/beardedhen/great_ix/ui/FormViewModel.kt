package com.beardedhen.great_ix.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import com.beardedhen.great_ix.db.FormDao
import com.beardedhen.great_ix.db.FormEntity
import com.beardedhen.great_ix.db.GreatIxDatabase


class FormViewModel(val application: Application) : ViewModel() {

    var formDao: FormDao = GreatIxDatabase.getDataBase(application)!!.formDao()

    var currentForm: FormEntity? = null

    fun getCard(id: Long): FormEntity {
        return formDao.get( id )
    }

    fun addForm(formRecord: FormEntity) : Long {
        return formDao.insert( formRecord )
    }
}