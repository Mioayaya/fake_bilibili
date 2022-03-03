package com.app.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NoScrollViewPager:ViewPager {
    constructor(context:Context):super(context)
    constructor(context: Context,attrs:AttributeSet):super(context,attrs)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }
}