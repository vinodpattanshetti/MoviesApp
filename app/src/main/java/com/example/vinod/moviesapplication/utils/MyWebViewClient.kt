package com.example.vinod.moviesapplication.utils

import android.os.Build
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class MyWebViewClient : WebViewClient() {

  override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      view.loadUrl(request.url.toString())
    }
    return true

  }

}
