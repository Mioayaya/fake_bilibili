package com.app.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.app.R
import com.app.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {


    val fragmentList = listOf(HomeFragment(), HistoryFragment(),UserFragment(),LoginFragment())

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val contentViewPager = findViewById<ViewPager>(R.id.content_view_pager)
        // 设置fragment页面的缓存数据
        contentViewPager.offscreenPageLimit = fragmentList.size

        contentViewPager.adapter = MyAdapter(supportFragmentManager)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> contentViewPager.currentItem = 0
                R.id.nav_history -> contentViewPager.currentItem = 1
                R.id.nav_user -> contentViewPager.currentItem = 2
                R.id.nav_login -> contentViewPager.currentItem = 3
            }
            false
        }
        contentViewPager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                bottomNav.menu.getItem(position).isChecked = true
            }

            override fun onPageScrollStateChanged(state: Int) {}

        })
    }

    inner class MyAdapter(fm: FragmentManager):
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }


    }



}






