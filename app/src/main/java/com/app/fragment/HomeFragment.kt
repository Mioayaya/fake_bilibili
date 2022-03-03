package com.app.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.app.MyApplication
import com.app.R
import com.app.UserData
import com.app.UserInfor
import com.app.activity.*
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception
import kotlin.concurrent.thread

class HomeFragment:Fragment() {
    //    private val trueIp = "http://192.168.137.1"
    private val trueIp = "http:192.168.43.44"
    private val ImageIP = "${trueIp}:8080/bilibili/"
    //    private  var userimage:String = "http://192.168.137.1:8080/bilibili/userImages/2.png" //电脑
    private  var userimage:String = "${trueIp}:8080/bilibili/userImages/2.png"   //手机
    private var userData = UserData("test","这是测试","9","9","","99","4","","")
    val titleTypeList = listOf("首页","热门","推荐","番剧")

    // 初始化一个容器
    val fragmentList = listOf(AnimFragment(), HotFragment(),RecommendFragment(),CartFragment())
    lateinit var tablLayout:TabLayout
    lateinit var viewPager:ViewPager
    lateinit var toolbar:Toolbar
    lateinit var searchview:CardView
    lateinit var headview:CircleImageView   // 头像




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home,container,false)
        tablLayout = view.findViewById(R.id.anim_tab_layout)
        viewPager = view.findViewById(R.id.anim_view_pager)
        toolbar = view.findViewById(R.id.home_tool_bar)
        searchview = view.findViewById(R.id.home_search_card)
        headview = view.findViewById(R.id.home_head_circle)
        //usButton = view.findViewById(R.id.tool_us)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        SetUserInfor() // 加载用户信息
        Thread.sleep(250)
        Glide.with(this).load("${ImageIP}${userData.image}").into(headview) //加载头像
        toolbar.inflateMenu(R.menu.home_tool_bar_menu)
        // 标题栏的menu菜单点击
        toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.tool_us -> {val intent = Intent(MyApplication.context, UsActivity::class.java)
                    startActivity(intent) }
                R.id.tool_add -> {val intent = Intent(MyApplication.context,AddActivity::class.java)
                    startActivity(intent)}
            }
            false
        }

        // 点击搜索框进入到搜索页面
        searchview.setOnClickListener {
            val intent = Intent(MyApplication.context, SearchActivity::class.java)
            startActivity(intent)
        }

        // 点击头像页面进入到个人主页
        headview.setOnClickListener{
            SetUserInfor() // 加载用户信息
            Thread.sleep(250)
            Glide.with(this).load("${ImageIP}${userData.image}").into(headview) //加载头像
            val intent = Intent(MyApplication.context, UserActivity::class.java)
            startActivity(intent)
        }

        // 点击纸飞机到 开发人员的联系方式
    //    usButton.setOnClickListener {
    //    }



        // 设置缓存
        viewPager.offscreenPageLimit = titleTypeList.size
        viewPager.adapter = MyAdapter(childFragmentManager)

        tablLayout.setupWithViewPager(viewPager)
    }

    inner class MyAdapter(fm: FragmentManager):
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getPageTitle(position: Int): CharSequence {
            return titleTypeList[position]
        }

    }
    @SuppressLint("NotifyDataSetChanged")
    private fun SetUserInfor(){
        thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("http://192.168.43.44:8080/homework/bilibili/userInfor.jsp")
                    .build()
                val response = client.newCall(request).execute()
                val data = response.body?.string()
                if(data != null){
                    parseWithGson(data)
                    //showResult(data)
                    //parseDataWithJsonObject(data)
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    private fun parseWithGson(jsonStr:String){
        val gson = Gson()
        val typeOf = object : TypeToken<List<UserInfor>>(){}.type
        val data = gson.fromJson<List<UserInfor>>(jsonStr,typeOf)
        for(info in data){
            userData.name = info.name
            userData.sign = info.sign
            userData.grade = info.grade
            userData.doinb = info.doinb.toString()
            userData.binb = info.binb.toString()
            userData.watch = info.watch.toString()
            userData.image = info.image
        }
/*
        Looper.prepare()
        Toast.makeText(applicationContext,"${jsonStr}", Toast.LENGTH_SHORT).show()
        Looper.loop()
*/

    }
}