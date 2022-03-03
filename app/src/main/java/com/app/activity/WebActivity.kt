package com.app.activity

import android.app.Activity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import com.app.R

class WebActivity:Activity() {
    private lateinit var webUrl:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        val webview = findViewById<WebView>(R.id.web_view)
        webUrl = intent.getStringExtra("webUrl=").toString()

        webview.settings.javaScriptEnabled = true
        webview.webViewClient = WebViewClient()
        webview.loadUrl(webUrl)


        val reviseBack = findViewById<ImageView>(R.id.revise_icon_back)
        reviseBack.setOnClickListener {
            finish()
        }
    }
}