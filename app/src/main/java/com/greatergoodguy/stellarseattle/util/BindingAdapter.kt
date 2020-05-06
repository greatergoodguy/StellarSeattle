package com.greatergoodguy.stellarseattle.util

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("showIf")
fun View.showIf(condition: Boolean) {
    this.visibility = if (condition) {
        View.VISIBLE
    } else {
        View.GONE
    }
}
