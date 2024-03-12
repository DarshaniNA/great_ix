package com.beardedhen.great_ix.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView


fun ImageView.colorButton( colorBackground: GradientDrawable ){
    setColorFilter(Color.WHITE)
    //colorBackground.setStroke(0, Color.WHITE)
    background = colorBackground
}

//fun ImageView.resetButtonColor( ){
//    setColorFilter(Color.BLACK)
//    background = ContextCompat.getDrawable( context, R.drawable.circlebutton_selector_bg )
//}
//
//fun ImageView.deactivateButtonColors( ){
//    setColorFilter(Color.WHITE)
//    background = ContextCompat.getDrawable( context, R.drawable.circle_with_border_disabled )
//}


/**
 * Returns the immediate children of a view group as a sequence.
 */
private fun ViewGroup.asSequence(): Sequence<View> = object : Sequence<View> {

    override fun iterator(): Iterator<View> = object : Iterator<View> {
        private var nextValue: View? = null
        private var done = false
        private var position: Int = 0

        override public fun hasNext(): Boolean {
            if (nextValue == null && !done) {
                nextValue = getChildAt(position)
                position++
                if (nextValue == null) done = true
            }
            return nextValue != null
        }

        override fun next(): View {
            if (!hasNext()) {
                throw NoSuchElementException()
            }
            val answer = nextValue
            nextValue = null
            return answer!!
        }
    }
}

/**
 * views extension element.
 */
public val ViewGroup.views: List<View>
    get() = asSequence().toList()


object DisplayUtil {

    /**
     * Recursed views extension element.
     */
    private val ViewGroup.viewsRecursive: List<View>
    get() = views.flatMap {
        when (it) {
                    is ViewGroup -> it.viewsRecursive
            else -> listOf(it)
        }
    }

    /**
     * Set all TextView children to have the given typeface.
     */
    fun setFontOnContainer( container: ViewGroup, typeface : Typeface) {

        val views = container.viewsRecursive
        for( view : View in views) {

            if( view is TextView ) {

                view.typeface = typeface
            }
        }
    }

    fun getContainerChildren( container: ViewGroup ) : Sequence<View> {
        return container.asSequence()
    }

    fun rotateBitmap(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    fun dpToPx(dp: Int, context: Context): Int {
        val density = context.getResources()
                .getDisplayMetrics()
                .density
        return Math.round(dp.toFloat() * density)
    }


    /**
     * Attempts to hide the keyboard using the InputMethodManager
     * @param currentFocus The result of getCurrentFocus()
     */
    fun hideSoftKeyboard(currentFocus: View?) {
        if (currentFocus != null) {
            val imm = currentFocus.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }

    /**
     * Convenience function for calling setHideKeyboardOnBackgroundTouch with an activity
     * as opposed to a view.
     */
    fun setHideKeyboardOnBackgroundTouch(activity: Activity) {

        val contentView = activity.findViewById<View>(android.R.id.content)
        setHideKeyboardOnBackgroundTouch(contentView)
    }

//    fun setRemoveFocusOnBackgroundTouch(activity: Activity) {
//
//        val contentView = activity.findViewById<View>(android.R.id.content)
//        setRemoveFocusOnBackgroundTouch(contentView)
//    }

    /**
     * Recursively traverses the view hierarchy for non EditText controls
     * and adds a touch listener to each so that any displayed keyboard is
     * closed when a user touches away from an EditText control.
     *
     * @param view the outside view
     */
    fun setHideKeyboardOnBackgroundTouch(view: View) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {

            view.setOnTouchListener { v, event ->
                hideSoftKeyboard(view)
                false
            }
        }

        // If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {

            for (i in 0 until view.childCount) {

                val innerView = view.getChildAt(i)

                setHideKeyboardOnBackgroundTouch(innerView)
            }
        }
    }

//    fun setRemoveFocusOnBackgroundTouch(view: View) {
//
//        // Set up touch listener for non-text box views (and scroll views since they cause issues)
//        // to remove focus of the selected text box.
//        if (view !is TextFieldView && view !is ScrollView) {
////            view.setOnTouchListener { v, event ->
////                view.requestFocus()
////                false
////            }
//        }
//
//        // If a layout container, iterate over children and seed recursion.
//        if (view is ViewGroup) {
//
//            for (i in 0 until view.childCount) {
//
//                val innerView = view.getChildAt(i)
//
//                setRemoveFocusOnBackgroundTouch(innerView)
//            }
//        }
//    }
}