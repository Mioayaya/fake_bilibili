package com.app.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.*
import com.app.utill.showToast
import com.bumptech.glide.Glide
import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.lang.Exception
import java.net.URLEncoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread

class HeadImageActivity:Activity() {
    private val trueIp = "http:192.168.43.44"
    private val ImageIP = "${trueIp}:8080/bilibili/"
    private val JSPIP = "${trueIp}:8080/homework/bilibili/"
    private var userimage:String = "${ImageIP}userImages/2.png"    // 头像地址
    private val headlist = ArrayList<Headimage>()
    private var headimageStr = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_headimage)
        val HomeRecyclerView = findViewById<RecyclerView>(R.id.Home_RecyclerView)
        HomeRecyclerView.layoutManager = GridLayoutManager(MyApplication.context,3)
        getHeadImages()
        Thread.sleep(500)
        HomeRecyclerView.adapter = HeadimageAdapter(headlist)
        // 返回按钮
        val reviseBack = findViewById<ImageView>(R.id.revise_icon_back)
        reviseBack.setOnClickListener {
            finish()
        }
    }
    // Headimage动画类
    inner class HeadimageAdapter(private val headlist:List<Headimage>):RecyclerView.Adapter<HeadimageAdapter.HeadimageViewHolder>(){

        inner class HeadimageViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
            val image:de.hdodenhof.circleimageview.CircleImageView = itemView.findViewById(R.id.item_user_image)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeadimageViewHolder {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.bilibili_item_headimage,parent,false)
            return HeadimageViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: HeadimageViewHolder, position: Int) {
            val anims = headlist[position]
            //图片加载
            Glide.with(MyApplication.context).load("${ImageIP}${anims.image}").into(holder.image)
            // 加载到holder的image里
            holder.itemView.setOnClickListener(){
                headimageStr = headlist[holder.absoluteAdapterPosition].image
                sentHeadImageStr()
                Thread.sleep(300)
                finish()
            }

        }

        override fun getItemCount(): Int {
            return headlist.size
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getHeadImages(){
        thread {
            try {
                val client = OkHttpClient()
                //构造数据传送体
                //构建请求
                val request = Request.Builder()
                    .url("${trueIp}:8080/homework/bilibili/getHeadImage.jsp")
                    .build()
                //执行
                val response= client.newCall(request).execute()
                //得到返回值
                val responseData=response.body?.string()

                if(responseData!= null){
                    getJsonDataForImage(responseData)

                }else{
                    "null".showToast()
                }

            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    private fun getJsonDataForImage(jsonData:String){
        try {
            val data=ArrayList<Headimage>()
            //json数组
            val jsonArray= JSONArray(jsonData)
            headlist.clear()
            for(i in 0 until jsonArray.length()){
                val jsonObject=jsonArray.getJSONObject(i)
                val image=jsonObject.getString("image")
                data.add(Headimage(image))
                headlist.add(Headimage(image))
            }
            //animHistory.sortBy { user -> user.time } 			// 指定以name属性进行升序排序
        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    private fun sentHeadImageStr() {
        thread {
            try {
                val requestBody = FormBody.Builder()
                    .add("headimageStr", URLEncoder.encode(headimageStr,"UTF-8"))
                    .build()
                val client = OkHttpClient()
                val request = Request.Builder()
                    //   .url("http://192.168.137.1:8080/homework/bilibili/userLogin.jsp")
                    .url("${JSPIP}sentHeadImage.jsp")
                    .post(requestBody)
                    .build()
                val response = client.newCall(request).execute()
                val data = response.body?.string()
                if(data != null){
                    parseWithHead(data)
                    //showResult(data)
                    //parseDataWithJsonObject(data)
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    private fun parseWithHead(jsonStr:String){


        val gson = Gson()
        val flag = gson.fromJson(jsonStr, ReviseFlag::class.java)
        if(flag.flag=="true"){
            "观看成功".showToast()
        }
    }

}