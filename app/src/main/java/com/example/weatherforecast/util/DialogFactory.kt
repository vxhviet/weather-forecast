package com.example.weatherforecast.util

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialog
import com.example.weatherforecast.R

/**
 * Created by viet on 1/19/21.
 */
object DialogFactory {
    fun createSimpleOkDialog(context: Context,
                             message: String? = null,
                             callback: ((Dialog?) -> Unit)? = null): Dialog {

        return AppCompatDialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            setContentView(R.layout.dialog_simple)

            findViewById<TextView>(R.id.ds_content_tv)?.text = message
            findViewById<Button>(R.id.ds_ok_btn)?.setOnClickListener {
                dismiss()
                callback?.invoke(this)
            }
        }
    }
}