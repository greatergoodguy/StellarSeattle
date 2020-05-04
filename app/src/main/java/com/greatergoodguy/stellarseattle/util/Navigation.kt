package com.greatergoodguy.stellarseattle.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri

fun showExternalBrowser(context: Context, url: String) {
    showExternalBrowser(context, url.toUri())
}


fun showExternalBrowser(context: Context, uri: Uri) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = uri
    context.startActivity(intent)
}
