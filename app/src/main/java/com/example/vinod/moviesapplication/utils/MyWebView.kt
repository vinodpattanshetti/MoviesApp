package com.example.vinod.moviesapplication.utils

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebChromeClient
import android.webkit.WebView

class MyWebView : WebView {

  constructor(context: Context) : super(context) {
    initDefaultSetting()
  }

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    initDefaultSetting()
  }

  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
    context, attrs, defStyleAttr
  ) {
    initDefaultSetting()
  }

  private fun initDefaultSetting() {
    val webSettings = this.settings
    webSettings.javaScriptEnabled = true
    webSettings.loadWithOverviewMode = true
    webSettings.useWideViewPort = true
    webChromeClient = WebChromeClient()
    webViewClient = MyWebViewClient()
  }

  /**
   * Load Web View with url
   */
  fun load(url: String) {
    this.loadUrl(url)
  }

}
