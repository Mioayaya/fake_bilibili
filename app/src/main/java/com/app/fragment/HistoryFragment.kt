package com.app.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.app.*
import com.app.activity.DetailActivity
import com.app.activity.VideoSelect
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

class HistoryFragment:Fragment() {

    private lateinit var HomeRecyclerView: RecyclerView
    private val animHistory = ArrayList<AnimHistory>()
    private lateinit var refresh: SwipeRefreshLayout
    //    private val trueIp = "http://192.168.137.1"
    private val trueIp = "http:192.168.43.44"
    private val ImageIP = "${trueIp}:8080/bilibili/"
    private val JSPIP = "${trueIp}:8080/homework/bilibili/"
    private var animName = " "
    private var animTime = " "

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history,container,false)
        HomeRecyclerView = view.findViewById<RecyclerView>(R.id.Home_RecyclerView)
        refresh = view.findViewById(R.id.anim_refresh)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sendRequestByHttpUrl2()
        Thread.sleep(500)
        HomeRecyclerView.layoutManager = LinearLayoutManager(MyApplication.context)
        HomeRecyclerView.adapter = AnimsAdapter(animHistory)

        refresh.setColorSchemeColors(Color.parseColor("#FF6699"))
        refresh.setOnRefreshListener {
            thread {
                Thread.sleep(700)
                activity?.runOnUiThread{
                    sendRequestByHttpUrl2()
                    Thread.sleep(500)
                    HomeRecyclerView.adapter = AnimsAdapter(animHistory)
                    refresh.isRefreshing = false
                }
            }
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
            animHistory.clear()
            for(i in 0 until jsonArray.length()){
                val jsonObject=jsonArray.getJSONObject(i)
                val id = jsonObject.getString("id")
                val name=jsonObject.getString("name")
                val description=jsonObject.getString("description")
                val image=jsonObject.getString("image")
                val time = jsonObject.getString("time")
                data.add(AnimHistory(id,name,description,image,time))
                animHistory.add(AnimHistory(id,name,description,image,time))
            }
            //animHistory.sortBy { user -> user.time } 			// 指定以name属性进行升序排序
            animHistory.sortByDescending { user -> user.time } // 指定以name属性进行降序排序
        }catch (e: Exception){
            e.printStackTrace()
        }

    }
}