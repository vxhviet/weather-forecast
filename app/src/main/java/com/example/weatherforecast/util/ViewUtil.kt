package com.example.weatherforecast.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by viet on 1/20/21.
 */
fun View.hideKeyboard() {
    val im = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    im.hideSoftInputFromWindow(this.windowToken, 0)
}

fun RecyclerView.showHideDividerOnScroll(divider: View) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        var currentScrollPosition = 0

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            currentScrollPosition += dy
            ViewUtil.showHideDividerBasedOnCurrentScrollPosition(currentScrollPosition, divider)
        }
    })
}

class ViewUtil {
    companion object {
        fun showHideDividerBasedOnCurrentScrollPosition(currentScrollPosition: Int, divider: View?) {
            divider?.let {
                if (currentScrollPosition == 0) {
                    it.visibility = View.INVISIBLE
                } else {
                    it.visibility = View.VISIBLE
                }
            }
        }
    }
}