package com.beardedhen.great_ix.ui

import android.view.MotionEvent
import android.view.View
import android.widget.EditText

//fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
//
//    val listener = object : TextWatcher {
//        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//        }
//
//        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
//            afterTextChanged.invoke(text?.toString()?:"")
//        }
//
//        override fun afterTextChanged(editable: Editable?) {
//        }
//    }
//
//    setTag( R.id.text_change_listener, listener )
//
//    this.addTextChangedListener(listener)
//}
//
//fun EditText.enableTextChangedListener( enable: Boolean ) {
//
//    val listener = getTag( R.id.text_change_listener ) as TextWatcher
//
//    if( enable ) {
//        addTextChangedListener(listener)
//    } else {
//        removeTextChangedListener(listener)
//    }
//}

fun EditText.setRightDrawableTouchListener(touchAction: () -> Unit) {
    setOnTouchListener(View.OnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_UP) {
            if (event.rawX >= (this.right - this.compoundPaddingRight)) {
                touchAction()
                return@OnTouchListener true
            }
        }
        return@OnTouchListener false
    })
}
