package com.beardedhen.great_ix.ui

import android.app.Activity
import android.content.Intent
import android.widget.Toast

/**
 * Activity Extensions
 */

fun Activity.showToast( message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG ).show()
}

fun Activity.showToast( messageResId: Int ) {
    Toast.makeText(this, messageResId, Toast.LENGTH_LONG ).show()
}

fun Activity.getStartIntent(cls: Class<out Activity> ) : Intent {
    return Intent(this, cls)
}
//
//fun Activity.showOkCancelDialog(@StringRes titleResId: Int,
//                                @StringRes messageResId: Int,
//                                okListener: DialogInterface.OnClickListener,
//                                cancelListener: DialogInterface.OnClickListener ) {
//
//    val builder = AlertDialog.Builder(this )
//    builder.setTitle(titleResId)
//    builder.setMessage(messageResId)
//    builder.setPositiveButton(R.string.general_ok, okListener)
//    builder.setNegativeButton(R.string.general_cancel, cancelListener)
//
//    val dialog: AlertDialog = builder.create()
//    dialog.show()
//}
