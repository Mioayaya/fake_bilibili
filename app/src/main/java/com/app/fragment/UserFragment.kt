package com.app.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.app.*
import com.app.activity.*
import com.app.utill.showToast
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.wait
import org.json.JSONArray
import java.lang.Exception
import java.net.URLEncoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread

class UserFragment: Fragment() {

    private val trueIp = "http:192.168.43.44"
    private val ImageIP = "${trueIp}:8080/bilibili/"
    private val JSPIP = "${trueIp}:8080/homework/bilibili/"
    private  var userimage:String = "${ImageIP}userImages/2.png"    // 头像地址
    private var userData = UserData("test","这是测试","9","9","","99","4","","")
    private lateinit var reviseButton:TextView  // 修改信息按钮
    private lateinit var userImageview:CircleImageView //用户头像
    private lateinit var userWatch:TextView  // 观看次数
    private lateinit var userName:TextView  // 用户名字
    private lateinit var userSign:TextView  // 个性签名
    private lateinit var userGrade:ImageView  // 等级
    private lateinit var userDoinb:TextView  // 硬币
    private lateinit var userBinb:TextView  // B币
    private lateinit var userFollow:TextView  // 关注
    private lateinit var userFans:TextView  // 粉丝
    private lateinit var userFollowButton:TextView  // 关注列表
    private lateinit var userFansButton:TextView  // 粉丝列表
    private lateinit var refresh: SwipeRefreshLayout
    private lateinit var HomeRecyclerView:RecyclerView
    private val animslist = ArrayList<AnimHistory>()
    private var isFirstLoading = "true"
    private var animName = " "
    private var animTime = " "

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        reviseButton = view.findViewById(R.id.revise_button)
        userImageview = view.findViewById(R.id.frag_user_image)
        userWatch = view.findViewById(R.id.frag_user_watch_count)
        userName = view.findViewById(R.id.frag_user_name)
        userSign = view.findViewById(R.id.frag_user_sign_text)
        userGrade = view.findViewById(R.id.frag_user_grade_image)
        refresh = view.findViewById(R.id.user_refresh)
        userDoinb = view.findViewById(R.id.frag_user_do_inb)
        userBinb = view.findViewById(R.id.frag_user_B_inb)
        userFollow = view.findViewById(R.id.frag_user_follow)
        userFans = view.findViewById(R.id.frag_user_fans)
        userFollowButton = view.findViewById(R.id.frag_follow_button)
        userFansButton = view.findViewById(R.id.frag_fans_button)
        HomeRecyclerView = view.findViewById<RecyclerView>(R.id.Home_RecyclerView)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        //
        SetUserInfor()  // 加载用户信息
        sendRequestByHttpUrl2()  //加载历史记录
        Thread.sleep(500)
        HomeRecyclerView.layoutManager = LinearLayoutManager(MyApplication.context)  // 加载适配器
        HomeRecyclerView.adapter = AnimsAdapter(animslist)  // 适配器加载历史记录列表

        loadUserInformation() //加载用户信息

        refresh.setColorSchemeColors(Color.parseColor("#FF6699"))
        refresh.setOnRefreshListener {
            thread {
                Thread.sleep(700)
                activity?.runOnUiThread{
                    SetUserInfor()  // 加载用户信息
                    sendRequestByHttpUrl2()
                    Thread.sleep(500)
                    loadUserInformation()  // 加载用户信息
                    HomeRecyclerView.adapter = AnimsAdapter(animslist)  // 适配器加载历史记录列表
                    refresh.isRefreshing = false
                }
            }
        }
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

        // 修改信息按钮
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
        }
/*
        Looper.prepare()
        Toast.makeText(applicationContext,"${jsonStr}", Toast.LENGTH_SHORT).show()
        Looper.loop()
*/

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

    private fun loadUserInformation() {
        Glide.with(this).load("${ImageIP}${userData.image}").into(userImageview) //加载头像
        userName.text = userData.name // 加载用户姓名
        userSign.text = userData.sign // 加载个性签名
        userDoinb.text = userData.doinb  // 加载硬币个数
        userBinb.text = userData.binb   // 加载b币个数
        userWatch.text = userData.watch // 加载观看次数
        userFollow.text = userData.follow // 关注人数
        userFans.text = userData.fans   // 粉丝人数
        when(userData.grade){  // 加载用户等级
            "0" -> {userGrade.setImageResource(R.drawable.ic_userlevel_0)}
            "1" -> {userGrade.setImageResource(R.drawable.ic_userlevel_1)}
            "2" -> {userGrade.setImageResource(R.drawable.ic_userlevel_2)}
            "3" -> {userGrade.setImageResource(R.drawable.ic_userlevel_3)}
            "4" -> {userGrade.setImageResource(R.drawable.ic_userlevel_4)}
            "5" -> {userGrade.setImageResource(R.drawable.ic_userlevel_5)}
            "6" -> {userGrade.setImageResource(R.drawable.ic_userlevel_6)}
        }
    }


}