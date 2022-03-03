package com.app.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.app.*
import com.app.utill.showToast
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.lang.Exception
import java.net.URLEncoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread

class OtherActivity:Activity() {
    //    private val trueIp = "http://192.168.137.1"
    private val trueIp = "http:192.168.43.44"
    private val JSPIP = "${trueIp}:8080/homework/bilibili/"
    private val ImageIP = "${trueIp}:8080/bilibili/"
    private var userData = UserData("test","这是测试","9","9","","0","4","","")
    private lateinit var userName:String
    private var isMe = "false"  // 判断是不是本人
    private var follow = ""  // 是否已经关注了
    private var animName = " "
    private var animTime = " "
    private val animslist = ArrayList<Animations>()
    override fun onCreate(savedInstanceState: Bundle?) {
        init()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ohther_users)

        val userImageview = findViewById<CircleImageView>(R.id.other_user_image)
        val userWatch = findViewById<TextView>(R.id.other_user_watch_count)
        val userName = findViewById<TextView>(R.id.other_user_name)
        val userSign = findViewById<TextView>(R.id.other_user_sign_text)
        val userGrade = findViewById<ImageView>(R.id.other_user_grade_image)
        val userDoinb = findViewById<TextView>(R.id.other_user_do_inb)
        val userBinb = findViewById<TextView>(R.id.other_user_B_inb)
        val userFollow = findViewById<TextView>(R.id.other_follow_button)
        val userFollowNumber = findViewById<TextView>(R.id.other_follow_number)
        val userFans = findViewById<TextView>(R.id.other_fans_number)
        val HomeRecyclerView = findViewById<RecyclerView>(R.id.Home_RecyclerView)
        val refresh = findViewById<SwipeRefreshLayout>(R.id.user_refresh)
        val userFollowButton = findViewById<TextView>(R.id.other_follows_button)
        val userFansButton = findViewById<TextView>(R.id.other_fans_button)

        SetUserInfor()  // 加载用户信息
        initUserFollow() // 初始化follow 状态
        sendRequestByHttpUrl2() // 加载番剧
        Thread.sleep(500)
        HomeRecyclerView.layoutManager = GridLayoutManager(MyApplication.context,2)
        HomeRecyclerView.adapter = AnimsAdapter(animslist)

        refresh.setColorSchemeColors(Color.parseColor("#FF6699"))
        refresh.setOnRefreshListener {
            thread {
                Thread.sleep(700)
                runOnUiThread{
                    sendRequestByHttpUrl2()
                    Thread.sleep(500)
                    HomeRecyclerView.adapter = AnimsAdapter(animslist)
                    refresh.isRefreshing = false
                }
            }
        }
        //******************************加载用户信息********************************************/
        // 加载用户头像
        Glide.with(this).load("${ImageIP}${userData.image}").into(userImageview) // 加载头像
        userName.text = userData.name   // 加载用户姓名
        userSign.text = userData.sign   // 加载个性签名
        userDoinb.text = userData.doinb // 加载硬币个数
        userBinb.text = userData.binb   // 加载b币个数
        userWatch.text = userData.watch // 加载观看次数
        userFollowNumber.text = userData.follow // 加载关注数
        userFans.text = userData.fans // 加载粉丝数

        when(userData.grade){           // 加载用户等级
            "0" -> {userGrade.setImageResource(R.drawable.ic_userlevel_0)}
            "1" -> {userGrade.setImageResource(R.drawable.ic_userlevel_1)}
            "2" -> {userGrade.setImageResource(R.drawable.ic_userlevel_2)}
            "3" -> {userGrade.setImageResource(R.drawable.ic_userlevel_3)}
            "4" -> {userGrade.setImageResource(R.drawable.ic_userlevel_4)}
            "5" -> {userGrade.setImageResource(R.drawable.ic_userlevel_5)}
            "6" -> {userGrade.setImageResource(R.drawable.ic_userlevel_6)}
        }
        //******************************加载用户信息**********************************************/

        // 查看关注列表
        userFollowButton.setOnClickListener {
            val intent = Intent(MyApplication.context, FollowActivity::class.java)
            intent.putExtra("username=",userData.name)
            startActivity(intent)
        }
        // 查看粉丝列表
        userFansButton.setOnClickListener {
            val intent = Intent(MyApplication.context, FansActivity::class.java)
            intent.putExtra("username=",userData.name)
            startActivity(intent)
        }

        //follow.showToast()
        when(follow){
            "true" -> {
                userFollow.text = "取消关注"
            }
            "false" ->{
                userFollow.text = "关注"
            }
        }

        userFollow.setOnClickListener {
            if(follow=="false"){  // 没有被关注

                SetUserFollow()  // 关注
                Thread.sleep(250)
                if(isMe == "true"){
                    "不可以关注自己哦~".showToast()
                }
                //follow="true"  // 代替数据库的测试
                else {
                    userFollow.text = "取消关注"
                    follow="true"
                    "关注成功".showToast()
                }
            }else {   // 已经关注了
                SetUserFollow()  // 取关
                Thread.sleep(250)
                //follow="false" // 代替数据库的测试
                userFollow.text = "关注"
                follow="false"
                "取关成功".showToast()

            }
        }

    }

    // Anims动画类
    inner class AnimsAdapter(private val animslist:List<Animations>): RecyclerView.Adapter<AnimsAdapter.AnimsViewHolder>(){

        inner class AnimsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val title: TextView = itemView.findViewById(R.id.anim_title)
            val description: TextView = itemView.findViewById(R.id.anim_desc)
            val image:com.makeramen.roundedimageview.RoundedImageView = itemView.findViewById(R.id.anim_image)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimsViewHolder {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.bilibili_item_video_image,parent,false)
            return AnimsViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: AnimsViewHolder, position: Int) {
            val anims = animslist[position]
            holder.title.text = anims.name
            holder.description.text = anims.description
            //图片加载
            Glide.with(MyApplication.context).load("${ImageIP}${anims.image}").into(holder.image)
            // 加载到holder的image里

            holder.itemView.setOnClickListener(){
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                val formatted = current.format(formatter)
                animTime = formatted.toString()
                //    animTime = "2021-12-21 15:34:27"
                animName = animslist[holder.absoluteAdapterPosition].name
                setHistory()
                val intent = Intent(MyApplication.context, VideoSelect::class.java)
                intent.putExtra("name=",animslist[holder.absoluteAdapterPosition].name)
                intent.putExtra("backimage=",animslist[holder.absoluteAdapterPosition].image)
                startActivity(intent)
            }

        }

        override fun getItemCount(): Int {
            return animslist.size
        }
    }

    private fun setHistory() {
        thread {
            try {
                val requestBody = FormBody.Builder()
                    .add("animName", URLEncoder.encode(animName,"UTF-8"))
                    .add("animTime", URLEncoder.encode(animTime,"UTF-8"))
                    .build()
                val client = OkHttpClient()
                val request = Request.Builder()
                    //   .url("http://192.168.137.1:8080/homework/bilibili/userLogin.jsp")
                    .url("${JSPIP}animHistory.jsp")
                    .post(requestBody)
                    .build()
                val response = client.newCall(request).execute()
                val data = response.body?.string()
                if(data != null){
                    parseWithHistory(data)
                    //showResult(data)
                    //parseDataWithJsonObject(data)
                }

            }catch (e: Exception){
                e.printStackTrace()
            }
        }

    }

    private fun parseWithHistory(jsonStr:String){
        val gson = Gson()
        val flag = gson.fromJson(jsonStr, ReviseFlag::class.java)
        if(flag.flag=="true"){
            "观看成功".showToast()
        }
    }

    private fun initUserFollow(){
        thread {
            try {
                val requestBody = FormBody.Builder()
                    .add("userName", URLEncoder.encode(userName,"UTF-8"))
                    .build()
                val client = OkHttpClient()
                val request = Request.Builder()
                    //   .url("http://192.168.137.1:8080/homework/bilibili/userLogin.jsp")
                    .url("${JSPIP}initFollow.jsp")
                    .post(requestBody)
                    .build()
                val response = client.newCall(request).execute()
                val data = response.body?.string()
                if(data != null){
                    parseWithInitFollow(data)
                    //showResult(data)
                    //parseDataWithJsonObject(data)
                }

            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    private fun parseWithInitFollow(jsonStr:String){

        val gson = Gson()
        val typeOf = object : TypeToken<List<initFollow>>(){}.type
        val data = gson.fromJson<List<initFollow>>(jsonStr,typeOf)
        for(info in data){
            follow = info.follow
            /*
            Looper.prepare()
            "${userData.sign}".showToast()
            Looper.loop()
             */
        }


    }

    private fun SetUserFollow(){
        thread {
            try {
                val requestBody = FormBody.Builder()
                    .add("userName", URLEncoder.encode(userName,"UTF-8"))
                    .add("follow", URLEncoder.encode(follow,"UTF-8"))
                    .build()
                val client = OkHttpClient()
                val request = Request.Builder()
                    //   .url("http://192.168.137.1:8080/homework/bilibili/userLogin.jsp")
                    .url("${JSPIP}userFollow.jsp")
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
        val typeOf = object : TypeToken<List<Follow>>(){}.type
        val data = gson.fromJson<List<Follow>>(jsonStr,typeOf)
        for(info in data){
            isMe = info.isME
            follow = info.follow

        }

    }

    private fun SetUserInfor(){
        thread {
            try {
                val requestBody = FormBody.Builder()
                    .add("userName", URLEncoder.encode(userName,"UTF-8"))
                    .build()
                val client = OkHttpClient()
                val request = Request.Builder()
                    //   .url("http://192.168.137.1:8080/homework/bilibili/userLogin.jsp")
                    .url("${JSPIP}otherUser.jsp")
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
            userData.follow = info.follow
            userData.fans = info.fans
            /*
            Looper.prepare()
            "${userData.sign}".showToast()
            Looper.loop()
             */
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun sendRequestByHttpUrl2(){
        thread {
            try {
                val client = OkHttpClient()
                //构造数据传送体
                //构建请求
                val request = Request.Builder()
                    .url("${trueIp}:8080/homework/bilibili/initAnim.jsp")
                    .build()
                //执行
                val response= client.newCall(request).execute()
                //得到返回值
                val responseData=response.body?.string()

                if(responseData!= null){
                    refresh(responseData)

                }else{
                    "null".showToast()
                }

            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
    private fun refresh(jsonData:String){
        try {
            val data=ArrayList<Animations>()
            //json数组
            val jsonArray= JSONArray(jsonData)
            animslist.clear()
            for(i in 0 until jsonArray.length()){
                val jsonObject=jsonArray.getJSONObject(i)
                val id = jsonObject.getString("id")
                val name=jsonObject.getString("name")
                val description=jsonObject.getString("description")
                val image=jsonObject.getString("image")
                data.add(Animations(id,name,description,image))
                animslist.add(Animations(id,name,description,image))
            }
            animslist.shuffle();
        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    private fun init(){
        userName = intent.getStringExtra("name=").toString()
    }
}