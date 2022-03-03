package com.app.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.app.*
import com.app.databinding.ActivityVideoBinding
import com.app.utill.showToast
import com.bumptech.glide.Glide
import com.dou361.ijkplayer.widget.PlayStateParams
import com.dou361.ijkplayer.widget.PlayerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import master.flame.danmaku.controller.DrawHandler
import master.flame.danmaku.danmaku.model.BaseDanmaku
import master.flame.danmaku.danmaku.model.DanmakuTimer
import master.flame.danmaku.danmaku.model.IDanmakus
import master.flame.danmaku.danmaku.model.android.DanmakuContext
import master.flame.danmaku.danmaku.model.android.Danmakus
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URLEncoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class VideoActivity : AppCompatActivity() {
    private val trueIp = "http:192.168.43.44"
    private val ImageIP = "${trueIp}:8080/bilibili/"
    private val JSPIP = "${trueIp}:8080/homework/bilibili/"
    private lateinit var binding: ActivityVideoBinding
    var userData:ArrayList<UserInfo> = ArrayList<UserInfo>()
    var danmakuData:ArrayList<Danmaku> = ArrayList<Danmaku>()
    var flag = 0
    private var addimage = "${ImageIP}userImages/2.png"
    var nameAddress = ""
    private var video_id = ""
    private var video_name = ""
    var styleComment = " "
    var timeComment= " "
    var messageComment = " "
    var imageComment = " "

    var danMakuStyle = ""
    var timeDanmaku= ""
    var messageDanmaku = ""
    var showDanmaku = false
    var mContext: DanmakuContext = DanmakuContext.create()
    private var username =""
    private var parser : BaseDanmakuParser = object : BaseDanmakuParser() {
        override fun parse(): IDanmakus {
            return Danmakus()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {


        initData()
        parseJson()   // 视频网络封装
        parseJsonDanmmu()  //弹幕网络封装
        initComment()   //评论网络封装
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        binding = ActivityVideoBinding.inflate(layoutInflater) //初始化
        setContentView(binding.root)
        val refresh = findViewById<SwipeRefreshLayout>(R.id.video_refresh)
        refresh.setColorSchemeColors(Color.parseColor("#FF6699"))
        refresh.setOnRefreshListener {
            thread {
                Thread.sleep(700)
                refresh.isRefreshing = false
            }
        }

        val recyclerView =binding.recyclerviewTrain

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = UserAdapter(userData)
        adapter.notifyDataSetChanged()
        val GBDM = findViewById<TextView>(R.id.gbdm)
        // 关闭弹幕
        GBDM.setOnClickListener {
            if(GBDM.text=="关闭弹幕"){
                val mDanmakuView = binding.DanMaku
                mDanmakuView.hide()
                GBDM.text="开启弹幕"
            }else{
                val mDanmakuView = binding.DanMaku
                if (mDanmakuView.isPrepared && mDanmakuView.isPaused) {
                    mDanmakuView.hide();
                }
                GBDM.text="关闭弹幕"
            }
        }
        val nonoo = binding.nonono
        //bin.visibility=View.GONE
        //nonoo.visibility=View.GONE
        recyclerView.adapter = adapter
        //recyclerView.visibility=View.GONE
        //评论区
        val btnComment = binding.Comment
        val btnUserComment = binding.userComment
        btnComment.setOnClickListener(){
            if(btnUserComment.text.toString() == ""){
                Toast.makeText(applicationContext, "输入不能为空！", Toast.LENGTH_SHORT).show()
            }else{
                //userData.add(UserInfo(username,"2小时前",btnUserComment.text.toString(),addimage))
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                val formatted = current.format(formatter)
                styleComment = "post"
                timeComment = formatted.toString()
                messageComment = btnUserComment.text.toString()
                parseJsonComment()
                btnUserComment.setText("")
                btnUserComment.clearFocus()
            }

            runOnUiThread(){
                val recyclerView =binding.recyclerviewTrain
                val adapter = UserAdapter(userData)
                adapter.notifyDataSetChanged()
                recyclerView.adapter = adapter
            }

        }

        //视频
        val play: PlayerView = PlayerView(this)
            .setTitle(video_name)
            .setScaleType(PlayStateParams.fitparent)
            .hideMenu(true)
            .forbidTouch(false)
            .setPlaySource(nameAddress)
            .hideBack(true)

        play.startPlay()
        play.setAutoReConnect(true,10)
        play.setForbidDoulbeUp(true)
        val qp = binding.qp

        qp.setOnClickListener(){
            val intent = Intent(this,ceVideoActivity::class.java)
            intent.putExtra("username",username)
            intent.putExtra("video_id",video_id)
            intent.putExtra("address",nameAddress)
            intent.putExtra("video_name",video_name)
            intent.putExtra("time",play.currentPosition)
            startActivity(intent)
        }

        //.setPlaySource(nameAddress)

        val btnGo = binding.go
        val input = binding.input

        val mTimeTask = object : TimerTask() {
            override fun run() {

                if(flag < danmakuData.size && danmakuData.size>0){
                    if(danmakuData[flag].DTime.toInt() < play.currentPosition && play.currentPosition <(danmakuData[flag].DTime.toInt()+1000)){
                        addDanmaku(danmakuData[flag].DMessage,false)
                        flag += 1
                    }
                }else if (danmakuData.size>0 && flag >= danmakuData.size){
                    flag = 0
                    if(danmakuData[flag].DTime.toInt() < play.currentPosition && play.currentPosition <(danmakuData[flag].DTime.toInt()+1000)){
                        addDanmaku(danmakuData[flag].DMessage,false)
                        flag += 1
                    }
                }
            }
        }
        Timer().schedule(mTimeTask,0,100)

        val mDanmakuView = binding.DanMaku
        //addDanmaku()

        btnGo.setOnClickListener(){
        if(input.text.toString() != "")    {
            addDanmaku(input.text.toString(), false)
            danMakuStyle = "post"
            timeDanmaku = play.currentPosition.toString()
            messageDanmaku = input.text.toString()
            input.setText("")
            parseJsonDanmmu()
        }else{
            "不能输入为空哦~".showToast()
        }


        }



        mDanmakuView.enableDanmakuDrawingCache(true)

        mDanmakuView.setCallback(object : DrawHandler.Callback{
            override fun prepared() {
                showDanmaku = true
                mDanmakuView.start()
                //generateSomeDanmaku()
                //addDanmaku("是不是就发送一条？", false)
            }

            override fun updateTimer(timer: DanmakuTimer?) {

            }

            override fun danmakuShown(danmaku: BaseDanmaku?) {

            }

            override fun drawingFinished() {

            }

        })

        //mDanmakuView.prepare(parser,mContext)
        mDanmakuView.prepare(parser,mContext)

    }

    //视频网络封装
    @SuppressLint("SetTextI18n")
    private fun parseJson(){
        thread {
            try {
                val requestBody = FormBody.Builder()
                    .add("id", URLEncoder.encode(video_id,"UTF-8"))
                    .build()
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("${JSPIP}asuna_video.jsp")
                    .post(requestBody)
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
        val typeOf = object : TypeToken<List<Video>>(){}.type
        val data = gson.fromJson<List<Video>>(jsonStr,typeOf)
        for (info in data){
            //builder.append(info).append("\n")
            video_id = info.id
            nameAddress = info.nameAddress
            video_name = info.videoName
        }


    }



    //评论网络封装
    private fun parseJsonComment(){
        thread {
            try {
                val requestBody = FormBody.Builder()
                    .add("style", URLEncoder.encode(styleComment,"UTF-8"))
                  //.add("name_Comment",URLEncoder.encode(username,"UTF-8"))
                    .add("time_Comment",URLEncoder.encode(timeComment,"UTF-8"))
                    .add("message_Comment",URLEncoder.encode(messageComment,"UTF-8"))
                  //.add("image_Comment",URLEncoder.encode(imageComment,"UTF-8"))
                    .add("id",URLEncoder.encode(video_id,"UTF-8"))
                    .build()
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("${JSPIP}/asuna_comment.jsp")
                    .post(requestBody)
                    .build()
                val response = client.newCall(request).execute()
                val data = response.body?.string()
                if (data != null) {
                    parseWithGsonComment(data)
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

   private fun initComment(){
       thread {
           try {
               val requestBody = FormBody.Builder()
                   .add("style", URLEncoder.encode(styleComment,"UTF-8"))
                   .add("id",URLEncoder.encode(video_id,"UTF-8"))
                   .build()
               val client = OkHttpClient()
               val request = Request.Builder()
                   .url("${JSPIP}/getComment.jsp")
                   .post(requestBody)
                   .build()
               val response = client.newCall(request).execute()
               val data = response.body?.string()
               if (data != null) {
                   parseWithGsonComment(data)
               }
           }catch (e: Exception){
               e.printStackTrace()
           }
       }
   }

    @SuppressLint("NotifyDataSetChanged")
    private fun parseWithGsonComment(jsonStr:String){
        val gson = Gson()
        val typeOf = object : TypeToken<List<UserInfo>>(){}.type
        val data = gson.fromJson<List<UserInfo>>(jsonStr,typeOf)
        /*userData.add(UserInfo("info.Username","info.UserTime","info.UserMessaged",addimage))
         Looper.prepare()
         Toast.makeText(applicationContext, "???", Toast.LENGTH_SHORT).show()
         Looper.loop()*/
        userData.clear()
        for (info in data){
            //commentData.add(Comment(info.name,info.time,info.message,info.image,info.video))
            //userData.add(UserInfo(info.name,info.time,info.messaged,info.image))
            userData.add(UserInfo(info.Username,info.UserTime,info.UserMessaged,info.UserImage))
        }
        Thread.sleep(500)
        runOnUiThread(){
            val recyclerView =binding.recyclerviewTrain
            val adapter = UserAdapter(userData)
            adapter.notifyDataSetChanged()
            recyclerView.adapter = adapter
        }

    }

    //弹幕网络封装
    private fun parseJsonDanmmu(){
        thread {
            try {
                val requestBody = FormBody.Builder()
                    .add("name_Danmaku", URLEncoder.encode(username,"UTF-8"))
                    .add("style",URLEncoder.encode(danMakuStyle,"UTF-8"))
                    .add("time_Danmaku",URLEncoder.encode(timeDanmaku,"UTF-8"))
                    .add("message_Danmaku",URLEncoder.encode(messageDanmaku,"UTF-8"))
                    .add("id",URLEncoder.encode(video_id,"UTF-8"))
                    .build()
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("${JSPIP}/asuna_danmaku.jsp")
                    .post(requestBody)
                    .build()
                val response = client.newCall(request).execute()
                val data = response.body?.string()
                if (data != null) {
                    parseWithGsonDanmu(data)
                }

            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    private fun parseWithGsonDanmu(jsonStr:String){

        val gson = Gson()
        val typeOf = object : TypeToken<List<Danmaku>>(){}.type
        val data = gson.fromJson<List<Danmaku>>(jsonStr,typeOf)
        for (info in data){
            danmakuData.add(Danmaku(info.DUsername,info.DTime,info.DMessage,video_id))

        }

    }



    private fun addDanmaku(context:String,withBorder:Boolean){
        val mBaseDan :BaseDanmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL)
        val mDanmakuView = binding.DanMaku
        mBaseDan.text = context
        mBaseDan.padding = 30
        mBaseDan.textSize = 60f
        mBaseDan.textColor = Color.RED
        mBaseDan.time = mDanmakuView.currentTime
        if (withBorder) {
            mBaseDan.borderColor = Color.GREEN
        }
        mDanmakuView.addDanmaku(mBaseDan)
    }

    override fun onPause() {
        super.onPause()
        val mDanmakuView = binding.DanMaku
        if (mDanmakuView.isPrepared) {
            mDanmakuView.pause();
        }
        val video = PlayerView(this)
        video.pausePlay()
        video.setForbidDoulbeUp(true)
    }

    override fun onResume() {
        super.onResume()
        val mDanmakuView = binding.DanMaku
        if (mDanmakuView.isPrepared && mDanmakuView.isPaused) {
            mDanmakuView.resume();
        }

        val video = PlayerView(this)
        video.setForbidDoulbeUp(true)


    }

    override fun onDestroy() {
        super.onDestroy()
        val mDanmakuView = binding.DanMaku

        mDanmakuView.release()

        val video = PlayerView(this)
        video.onDestroy()
    }

    //View Holder 界面控件的容器
    private class TrainViewHolder(view: View): RecyclerView.ViewHolder(view){
        //获取对应的控件
        val tvName : TextView = view.findViewById( R.id.user_name)
        val tvTime : TextView = view.findViewById(R.id.user_time)
        val tvMessage : TextView = view.findViewById(R.id.user_message)
        val tvImage : ImageView = view.findViewById(R.id.user_img)

    }
    //适配器
    private inner class UserAdapter(val userList: List<UserInfo>) : RecyclerView.Adapter<TrainViewHolder>(){

        //创建视图
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainViewHolder {

            val view = LayoutInflater.from(parent.context).inflate(R.layout.user_elem_layout,parent,false)

            return TrainViewHolder(view)
        }

        //绑定数据
        override fun onBindViewHolder(holder: TrainViewHolder, position: Int) {
            val info = userList[position]
            holder.tvName.text = info.Username
            holder.tvTime.text = info.UserTime
            holder.tvMessage.text = info.UserMessaged
            val Context: Context = holder.tvImage.context
            Glide.with(Context).load("${ImageIP}${info.UserImage}").dontAnimate().circleCrop().into(holder.tvImage)
            holder.tvImage.setOnClickListener {

                val intent = Intent(MyApplication.context, OtherActivity::class.java)
                intent.putExtra("name=",userList[holder.absoluteAdapterPosition].Username)
                startActivity(intent)
            }
        }
        //列表的行数
        //override fun getItemCount() = trainList.size
        override fun getItemCount() = userList.size
    }


    //初始化数据
    private fun initData(){

        username = intent.getStringExtra("username").toString()
        video_id = intent.getStringExtra("video_id").toString()

    }




}