package com.example.weatherforecast.base

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Dialog
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherforecast.data.source.remote.ErrorResponse
import com.example.weatherforecast.util.DialogFactory

/**
 * Created by viet on 1/19/21.
 */
abstract class BaseActivity : AppCompatActivity() {
    private var loadingView: View? = null
    private var isProcessingError = false
    private var errorDialog: Dialog? = null

    open fun onError(errorResponse: ErrorResponse) {
        showAlert(errorResponse)
    }

    private fun showAlert(errorResponse: ErrorResponse) {
        if (isFinishing || isDestroyed || isProcessingError)
            return
        isProcessingError = true
        errorDialog = DialogFactory.createSimpleOkDialog(
            this,
            errorResponse.message ?: errorResponse.toString()
        )
        errorDialog?.setOnDismissListener {
            isProcessingError = false
        }
        errorDialog?.show()
    }

    protected fun setLoadingView(view: View) {
        loadingView = view
    }

    open fun onChangedLoadingStatus(isShowLoader: Boolean) {
        loadingView?.let {
            if (isShowLoader) {
                it.visibility = View.VISIBLE
                it.alpha = 0f
                it.animate().alpha(1f).setDuration(500)
                    .setInterpolator(AccelerateInterpolator())
                    .setListener(null) // remove previous listener set in hide loader
            } else {
                it.alpha = 1f
                it.animate().alpha(0f).setDuration(500)
                    .setInterpolator(AccelerateInterpolator())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            super.onAnimationEnd(animation)
                            it.visibility = View.GONE
                        }
                    })
            }
        }
    }
}