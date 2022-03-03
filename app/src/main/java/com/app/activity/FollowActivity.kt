package com.app.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.*
import com.app.utill.showToast
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception
import java.net.URLEncoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread

class FollowActivity:Activity() {
    private val trueIp = "http:192.168.43.44"
    private val ImageIP = "${trueIp}:8080/bilibili/"
    private val JSPIP = "${trueIp}:8080/homework/bilibili/"
    private var userName = ""
    private var userData = UserData("test","这是测试","9","9","","99","4","","")
    private val followlist = ArrayList<FollowFans>()
    override fun onCreate(savedInstanceState: Bundle?) {
        init()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follow)
        val HomeRecyclerView= findViewById<RecyclerView>(R.id.Home_RecyclerView)
        setFollow()
        Thread.sleep(500)
        HomeRecyclerView.layoutManager = LinearLayoutManager(MyApplication.context)  // 加载适配器
        HomeRecyclerView.adapter = FollowAdapter(followlist)  // 适配器加载历史记录列表

        // 返回按钮
        val reviseBack = findViewById<ImageView>(R.id.revise_icon_back)
        reviseBack.setOnClickListener {
            finish()
        }
    }

    // FollowAdpter动画类
    inner class FollowAdapter(private val followlist:List<FollowFans>):RecyclerView.Adapter<FollowAdapter.AnimsViewHolder>(){

        inner class AnimsViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
            val name: TextView = itemView.findViewById(R.id.item_follow_user_name)
            val sign: TextView = itemView.findViewById(R.id.item_follow_user_sign)
            val image:de.hdodenhof.circleimageview.CircleImageView = itemView.findViewById(R.id.item_follow_user_image)
            val grade: ImageView = itemView.findViewById(R.id.item_follow_user_grade)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimsViewHolder {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.bilibili_item_follow_fans,parent,false)
            return AnimsViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: AnimsViewHolder, position: Int) {
            val anims = followlist[position]
            holder.name.text = anims.name
            holder.sign.text = anims.sign
            when(anims.grade){  // 加载用户等级
                "0" -> {holder.grade.setImageResource(R.drawable.ic_userlevel_0)}
                "1" -> {holder.grade.setImageResource(R.drawable.ic_userlevel_1)}
                "2" -> {holder.grade.setImageResource(R.drawable.ic_userlevel_2)}
                "3" -> {holder.grade.setImageResource(R.drawable.ic_userlevel_3)}
                "4" -> {holder.grade.setImageResource(R.drawable.ic_userlevel_4)}
                "5" -> {holder.grade.setImageResource(R.drawable.ic_userlevel_5)}
                "6" -> {holder.grade.setImageResource(R.drawable.ic_userlevel_6)}
            }
            //图片加载
            Glide.with(MyApplication.context).load("${ImageIP}${anims.image}").into(holder.image)
            // 加载到holder的image里
            holder.itemView.setOnClickListener(){
                val intent = Intent(MyApplication.context, OtherActivity::class.java)
                intent.putExtra("name=",followlist[holder.absoluteAdapterPosition].name)
                startActivity(intent)
            }

        }

        override fun getItemCount(): Int {
            return followlist.size
        }
    }

    private fun setFollow() {
        thread {
            try {
                val requestBody = FormBody.Builder()
                    .add("userName", URLEncoder.encode(userName,"UTF-8"))
                    .build()
                val client = OkHttpClient()
                val request = Request.Builder()
                    //   .url("http://192.168.137.1:8080/homework/bilibili/userLogin.jsp")
                    .url("${JSPIP}setFollow.jsp")
                    .post(requestBody)
                    .build()
                val response = client.newCall(request).execute()
                val data = response.body?.string()
                if(data != null){
                    parseWithFollow(data)
                    //showResult(data)
                    //parseDataWithJsonObject(data)
                }

            }catch (e: Exception){
                e.printStackTrace()
            }
        }

    }

    private fun parseWithFollow(jsonStr:String){
        val gson = Gson()
        val typeOf = object : TypeToken<List<FollowFans>>(){}.type
        val data = gson.fromJson<List<FollowFans>>(jsonStr,typeOf)
        for(info in data){
            followlist.add(FollowFans(info.name,info.grade,info.sign,info.image))
        }
    }

    private fun init(){
        userName = intent.getStringExtra("username=").toString()
    }
}