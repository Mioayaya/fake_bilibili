package com.app.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.*
import com.app.databinding.ActivityVideoSelectBinding
import com.app.utill.showToast
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.makeramen.roundedimageview.RoundedImageView
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception
import java.net.URLEncoder
import kotlin.concurrent.thread

class VideoSelect : AppCompatActivity() {

    private val trueIp = "http:192.168.43.44"
    private val ImageIP = "${trueIp}:8080/bilibili/"
    private val JSPIP = "${trueIp}:8080/homework/bilibili/"
    private val MainImage = "https://i0.hdslb.com/bfs/bangumi/fcdb9db0cc3f5c0c3aaa4ca1a9ddea6c121db471.jpg@282w_375h.webp"
    private lateinit var binding: ActivityVideoSelectBinding
    var videoData = ArrayList<VideoJS>()
    private var JJvideoData = JJVideo("","","","","")
    private var backImage = ""
    private var videoname = ""
    @SuppressLint("NotifyDataSetChanged", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {

        initData()
        parseJsonVideo()
        getVideo()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_select)

        //Glide.with(this).load("http:192.168.43.44:8080/bilibili/videoImages/SAO.png").into(videoBackground) // 加载背景
        //"${ImageIP}${backImage}".showToast()
        binding = ActivityVideoSelectBinding.inflate(layoutInflater) //初始化
        setContentView(binding.root)
        supportActionBar?.hide()//隐藏标题状态栏
        Thread.sleep(250)
        val recyclerView =binding.recyclerviewTrain
        val layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        recyclerView.layoutManager = layoutManager
        val JJimage = findViewById<ImageView>(R.id.video_jj_image)
        val jj = findViewById<TextView>(R.id.video_jj_text)
        val part1 = findViewById<TextView>(R.id.video_jj_part1)
        val part2 = findViewById<TextView>(R.id.video_jj_part2)
        val part3 = findViewById<TextView>(R.id.video_jj_part3)

        Glide.with(MyApplication.context).load(JJvideoData.image).into(JJimage)
        jj.text = JJvideoData.jj
        part1.text = JJvideoData.part1
        part2.text = JJvideoData.part2
        part3.text = JJvideoData.part3

        val adapter = UserAdapter(videoData)

        adapter.notifyDataSetChanged()

        recyclerView.adapter = adapter

        val background = binding.videBackGround

        val Context: Context = background.context
        Glide.with(Context).load("${ImageIP}${backImage}").into(background)

        val fm = binding.videoName
        fm.text = videoname

    }

    //评论网络封装
    private fun parseJsonVideo(){
        thread {
            try {
                val requestBody = FormBody.Builder()
                    .add("name", URLEncoder.encode(videoname,"UTF-8"))
                    .build()
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("${JSPIP}asuna_videonum.jsp")
                    .post(requestBody)
                    .build()
                val response = client.newCall(request).execute()
                val data = response.body?.string()
                if (data != null) {
                    parseWithGsonVideo(data)
                }

            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun parseWithGsonVideo(jsonStr:String){
        val gson = Gson()
        val typeOf = object : TypeToken<List<VideoJS>>(){}.type
        val data = gson.fromJson<List<VideoJS>>(jsonStr,typeOf)
        /*userData.add(UserInfo("info.Username","info.UserTime","info.UserMessaged",addimage))
         Looper.prepare()
         Toast.makeText(applicationContext, "???", Toast.LENGTH_SHORT).show()
         Looper.loop()*/
        videoData.clear()
        for (info in data){
            //commentData.add(Comment(info.name,info.time,info.message,info.image,info.video))
            //userData.add(UserInfo(info.name,info.time,info.messaged,info.image))
            videoData.add(VideoJS(info.js,info.mc,info.id))

        }


        runOnUiThread(){
            videoData.sortBy { user -> user.js }
            val recyclerView =binding.recyclerviewTrain
            val adapter = UserAdapter(videoData)
            adapter.notifyDataSetChanged()
            recyclerView.adapter = adapter
        }

    }

    //View Holder 界面控件的容器
    private class TrainViewHolder(view: View): RecyclerView.ViewHolder(view){
        //获取对应的控件
        val tvJS : TextView = view.findViewById( R.id.video_js)
        val tvMC : TextView = view.findViewById(R.id.video_mz)

    }
    //适配器
    private inner class UserAdapter(val userList: List<VideoJS>) : RecyclerView.Adapter<TrainViewHolder>(){

        //创建视图
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainViewHolder {

            val view = LayoutInflater.from(parent.context).inflate(R.layout.video_num_layout,parent,false)

            return TrainViewHolder(view)
        }

        //绑定数据
        override fun onBindViewHolder(holder: TrainViewHolder, position: Int) {
            val info = userList[position]
            holder.tvJS.text = info.js
            holder.tvMC.text = info.mc

            holder.itemView.setOnClickListener(){
                val intent = Intent(holder.itemView.context, VideoActivity::class.java)
                intent.putExtra("username","弹幕发送姬")
                intent.putExtra("video_id",info.id)
                startActivity(intent)
            }


        }

        //列表的行数
        //override fun getItemCount() = trainList.size
        override fun getItemCount() = userList.size


    }

    private fun getVideo() {
        thread {
            try {
                val requestBody = FormBody.Builder()
                    .add("videoname", URLEncoder.encode(videoname,"UTF-8"))
                    .build()
                val client = OkHttpClient()
                val request = Request.Builder()
                    //   .url("http://192.168.137.1:8080/homework/bilibili/userLogin.jsp")
                    .url("${JSPIP}getVideo.jsp")
                    .post(requestBody)
                    .build()
                val response = client.newCall(request).execute()
                val data = response.body?.string()
                if(data != null){
                    parseWithVideo(data)
                    //showResult(data)
                    //parseDataWithJsonObject(data)
                }

            }catch (e: Exception){
                e.printStackTrace()
            }
        }

    }

    private fun parseWithVideo(jsonStr:String){
        val gson = Gson()
        val typeOf = object : TypeToken<List<JJVideo>>(){}.type
        val data = gson.fromJson<List<JJVideo>>(jsonStr,typeOf)
        for(info in data){
            JJvideoData.image = info.image
            JJvideoData.jj = info.jj
            JJvideoData.part1 = info.part1
            JJvideoData.part2 = info.part2
            JJvideoData.part3 = info.part3
        }
    }


    //初始化数据
    private fun initData(){
    //    videoData.add(VideoJS("第一话","炎柱 炼狱杏寿郎","1"))
        videoname = intent.getStringExtra("name=").toString()
        backImage = intent.getStringExtra("backimage=").toString()



    }

}