package com.beardedhen.great_ix.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.SpannableStringBuilder
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import com.beardedhen.great_ix.R
import kotlinx.android.synthetic.main.alert_dialog_horizontal.*
import kotlinx.android.synthetic.main.alert_dialog_vertical.*

class GreatIxAlert(
    private val context: Context,
    private val title: String? = "Error",
    private val titleResId: Int? = null,
    private val message: String? = null,
    private val messageResId: Int? = null,
    private val messageBuilder: SpannableStringBuilder? = null,
    private val positiveButtonFunction: (() -> Unit)? = null,
    private val negativeButtonFunction: (() -> Unit)? = null,
    private val otherButtonFunction: (() -> Unit)? = null,
    private val positiveButtonText: String? = "OK",
    private val negativeButtonText: String? = null,
    private val otherButtonText: String? = null,
    private val isHorizontal: Boolean = true
) {

    fun display() {
        val dialog = Dialog(context)

        with(dialog) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(if (isHorizontal) R.layout.alert_dialog_horizontal else R.layout.alert_dialog_vertical)

            val positiveButton =
                if (isHorizontal) horizontalPositiveButton else verticalPositiveButton
            val negativeButton =
                if (isHorizontal) horizontalNegativeButton else verticalNegativeButton
            val otherButton = if (isHorizontal) horizontalOtherButton else verticalOtherButton
            val dialogTitle = if (isHorizontal) horizontalDialogTitle else verticalDialogTitle
            val dialogMessage = if (isHorizontal) horizontalDialogMessage else verticalDialogMessage

            dialogTitle.visibility = View.VISIBLE
            titleResId?.let {
                dialogTitle.setText( titleResId )
            } ?: run {
                title?.let {
                    dialogTitle.text = title
                } ?: run {
                    dialogTitle.visibility = View.GONE
                }
            }

            message?.let {
                dialogMessage.text = message
            } ?: run {
                messageBuilder?.let {
                    dialogMessage.text = messageBuilder
                } ?: run {
                    messageResId?.let {
                        dialogMessage.setText( messageResId )
                    }
                }
            }

            createButton(
                dialog,
                isHorizontal,
                positiveButton,
                positiveButtonText
            ) { positiveButtonFunction?.invoke() }
            createButton(
                dialog,
                isHorizontal,
                negativeButton,
                negativeButtonText
            ) { negativeButtonFunction?.invoke() }
            createButton(
                dialog,
                isHorizontal,
                otherButton,
                otherButtonText
            ) { otherButtonFunction?.invoke() }

            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }
    }

    private fun createButton(
        dialog: Dialog,
        isHorizontal: Boolean,
        button: Button,
        buttonText: String?,
        buttonFunction: () -> Unit
    ) {

        buttonText?.let {
            button.text = buttonText
            if (isHorizontal && buttonText == "") {
                (button.layoutParams as LinearLayout.LayoutParams).weight = 0F
            }
            button.setOnClickListener() {
                buttonFunction()
                dialog.dismiss()
            }
        } ?: run {
            button.visibility = View.GONE
        }
    }
}