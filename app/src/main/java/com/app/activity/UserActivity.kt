package com.app.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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


class UserActivity: AppCompatActivity() {
    //    private val trueIp = "http://192.168.137.1"
    private val trueIp = "http:192.168.43.44"
    private val JSPIP = "${trueIp}:8080/homework/bilibili/"
    private val ImageIP = "${trueIp}:8080/bilibili/"
    private var userData = UserData("test","这是测试","9","9","","0","4","","")
    private val animslist = ArrayList<AnimHistory>()
    private  var userimage:String = "${ImageIP}userImages/2.png"    // 头像地址
    private var userwatch:Int = 99
    private var animName = " "
    private var animTime = " "



    override fun onCreate(savedInstanceState: Bundle?) {
        //        sendRequestByHttpUrl1()  // 加载用户数据

        super.onCreate(savedInstanceState)
        // this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_user)
        val HomeRecyclerView = findViewById<RecyclerView>(R.id.Home_RecyclerView)
        HomeRecyclerView.layoutManager = LinearLayoutManager(MyApplication.context)
        HomeRecyclerView.adapter = AnimsAdapter(animslist)

        val userImageview = findViewById<CircleImageView>(R.id.user_image)
        val userWatch = findViewById<TextView>(R.id.user_watch_count)
        val reviseButton = findViewById<TextView>(R.id.revise_button)
        val userName = findViewById<TextView>(R.id.user_name)
        val userSign = findViewById<TextView>(R.id.user_sign_text)
        val userGrade = findViewById<ImageView>(R.id.user_grade_image)
        val userDoinb = findViewById<TextView>(R.id.user_do_inb)
        val userBinb = findViewById<TextView>(R.id.user_B_inb)
        val userFollow = findViewById<TextView>(R.id.user_follow)
        val userFans = findViewById<TextView>(R.id.user_fans)
        val refresh = findViewById<SwipeRefreshLayout>(R.id.user_refresh)
        val userFollowButton = findViewById<TextView>(R.id.user_follow_button)
        val userFansButton = findViewById<TextView>(R.id.user_fans_button)
        sendRequestByHttpUrl2()  // 加载anim类数据
        SetUserInfor()  // 加载用户信息

        Thread.sleep(500)
        refresh.setColorSchemeColors(Color.parseColor("#FF6699"))
        refresh.setOnRefreshListener {
            thread {
                Thread.sleep(700)
                    runOnUiThread{
                        SetUserInfor()
                        sendRequestByHttpUrl2()
                        Thread.sleep(500)
                        Glide.with(this).load("${ImageIP}${userData.image}").into(userImageview) // 加载头像
                        userName.text = userData.name  // 加载用户姓名
                        userSign.text = userData.sign  // 加载个性签名
                        userDoinb.text = userData.doinb  // 加载硬币个数
                        userBinb.text = userData.binb   // 加载b币个数
                        userWatch.text = userData.watch // 加载观看次数
                        userFollow.text = userData.follow // 加载关注次数
                        userFans.text = userData.fans // 加载粉丝次数
                        when(userData.grade){   // 加载用户等级
                            "0" -> {userGrade.setImageResource(R.drawable.ic_userlevel_0)}
                            "1" -> {userGrade.setImageResource(R.drawable.ic_userlevel_1)}
                            "2" -> {userGrade.setImageResource(R.drawable.ic_userlevel_2)}
                            "3" -> {userGrade.setImageResource(R.drawable.ic_userlevel_3)}
                            "4" -> {userGrade.setImageResource(R.drawable.ic_userlevel_4)}
                            "5" -> {userGrade.setImageResource(R.drawable.ic_userlevel_5)}
                            "6" -> {userGrade.setImageResource(R.drawable.ic_userlevel_6)}
                        }
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
        userFollow.text = userData.follow // 加载关注次数
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

        // 点击头像选择更换头像
        userImageview.setOnClickListener {
            val intent = Intent(MyApplication.context, HeadImageActivity::class.java)
            startActivity(intent)
        }

        // 修改信息
        reviseButton.setOnClickListener {
            val intent = Intent(MyApplication.context, ReviseActivity::class.java)
            startActivity(intent)
        }


    }

    // Anims动画类
    inner class AnimsAdapter(private val animslist:List<AnimHistory>):RecyclerView.Adapter<AnimsAdapter.AnimsViewHolder>(){

        inner class AnimsViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
            val title: TextView = itemView.findViewById(R.id.item_name_history)
            val description: TextView = itemView.findViewById(R.id.item_desc_history)
            val image:com.makeramen.roundedimageview.RoundedImageView = itemView.findViewById(R.id.item_image_history)
            val time:TextView = itemView.findViewById(R.id.item_time_history)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimsViewHolder {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.bilibili_item_history,parent,false)
            return AnimsViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: AnimsViewHolder, position: Int) {
            val anims = animslist[position]
            holder.title.text = anims.name
            holder.description.text = anims.description
            holder.time.text = anims.time
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

    @SuppressLint("NotifyDataSetChanged")
    private fun sendRequestByHttpUrl2(){
        thread {
            try {
                val client = OkHttpClient()
                //构造数据传送体
                //构建请求
                val request = Request.Builder()
                    .url("${trueIp}:8080/homework/bilibili/getHistory.jsp")
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
            val data=ArrayList<AnimHistory>()
            //json数组
            val jsonArray= JSONArray(jsonData)
            animslist.clear()
            for(i in 0 until jsonArray.length()){
                val jsonObject=jsonArray.getJSONObject(i)
                val id = jsonObject.getString("id")
                val name=jsonObject.getString("name")
                val description=jsonObject.getString("description")
                val image=jsonObject.getString("image")
                val time = jsonObject.getString("time")
                data.add(AnimHistory(id,name,description,image,time))
                animslist.add(AnimHistory(id,name,description,image,time))
            }
            //animHistory.sortBy { user -> user.time } 			// 指定以name属性进行升序排序
            animslist.sortByDescending { user -> user.time } // 指定以name属性进行降序排序
        }catch (e: Exception){
            e.printStackTrace()
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
            userData.follow = info.follow
            userData.fans = info.fans
            /*
            Looper.prepare()
            "${userData.sign}".showToast()
            Looper.loop()
             */
        }
/*
        Looper.prepare()
        Toast.makeText(applicationContext,"${jsonStr}", Toast.LENGTH_SHORT).show()
        Looper.loop()
*/
    }


}

