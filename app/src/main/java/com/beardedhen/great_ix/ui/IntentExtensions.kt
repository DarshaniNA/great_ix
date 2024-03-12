package com.beardedhen.great_ix.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.beardedhen.great_ix.Extras

/**
 * Intent Extensions
 */
fun Intent.start(context: Context) {
    context.startActivity(this)
}

fun Intent.startForResult(context: Activity, resultCode: Int) {
    context.startActivityForResult(this, resultCode)
}
//
//
//fun Intent.addRiskRecord(recordId: Long ) : Intent {
//    putExtra( Extras.EXTRA_RECORD_ID, recordId )
//    return this
//}
//
fun Intent.addFormId(formId: Long) : Intent {
    putExtra(Extras.EXTRA_RECORD_ID, formId)
    return this
}

//
//fun Intent.hasRecordIdExtra():Boolean {
//    return hasExtra( Extras.EXTRA_RECORD_ID )
//}
//
fun Intent.getRecordIdExtra():Long {
    return getLongExtra(Extras.EXTRA_RECORD_ID, 0)
}
