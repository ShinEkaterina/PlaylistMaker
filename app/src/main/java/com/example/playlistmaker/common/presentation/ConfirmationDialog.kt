package com.example.playlistmaker.common.presentation

import android.app.AlertDialog
import android.content.Context
import androidx.core.content.ContextCompat
import com.example.playlistmaker.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object ConfirmationDialog {
    fun showConfirmationDialog(
        context:Context,
        title: String,
        message: String,
        positiveButton: String,
        negativeButton: String,
        positiveAction: () -> Unit,
        negativeAction: () -> Unit,
        positiveColor: Int? = null,
        negativeColor: Int? = null
    ) {

        val posColor = positiveColor
            ?: ContextCompat.getColor(context, R.color.blue)
        val megColor = negativeColor
            ?: ContextCompat.getColor(context, R.color.blue)

        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButton) { dialog, which ->
                positiveAction()
            }
            .setNegativeButton(negativeButton) { dialog, which ->
                negativeAction()
            }.create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(megColor)
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(posColor)
        }

        dialog.show()
    }
}