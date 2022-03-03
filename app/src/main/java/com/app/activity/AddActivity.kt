package com.app.activity

import android.app.Activity
import android.os.Bundle
import android.widget.ImageView
import com.app.R
import com.bumptech.glide.Glide

class AddActivity:Activity() {
    private val XCWImage = "https://img0.baidu.com/it/u=2054661815,1866571479&fm=26&fmt=auto"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        val addBg = findViewById<ImageView>(R.id.add_bg)
        Glide.with(this).load(XCWImage).into(addBg)


        // 返回按钮
        val reviseBack = findViewById<ImageView>(R.id.revise_icon_back)
        reviseBack.setOnClickListener {
            finish()
        }
    }
}