package com.app.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.Animations
import com.app.MyApplication
import com.app.R
import com.app.utill.showToast
import com.bumptech.glide.Glide
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.lang.Exception
import java.net.URLEncoder
import kotlin.concurrent.thread

class ShowSearchActivity:Activity() {


    //    private val trueIp = "http://192.168.137.1"
    private val trueIp = "http:192.168.43.44"
    private val JSPIP = "${trueIp}:8080/homework/bilibili/"
    private val ImageIP = "${trueIp}:8080/bilibili/"

    private val animslist = ArrayList<Animations>()
    var searchText = " "

    override fun onCreate(savedInstanceState: Bundle?) {
        animslist.clear()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showsearch)
        val HomeRecyclerView = findViewById<RecyclerView>(R.id.Home_RecyclerView)
        val searchCancelButton = findViewById<TextView>(R.id.search_cancel_button)
        searchCancelButton.setOnClickListener { finish() }
        // 搜索框
        val searchEditText = findViewById<TextView>(R.id.home_edit_text)
        searchEditText.setOnClickListener { finish() }
        searchText = intent.getStringExtra("searchText=").toString()
        searchEditText.text = searchText
//        searchText.showToast()
        parseJson()
        Thread.sleep(500)

        HomeRecyclerView.layoutManager = LinearLayoutManager(this)
        HomeRecyclerView.adapter = AnimsAdapter(animslist)

    }

    // Anims动画类
    inner class AnimsAdapter(private val animslist:List<Animations>):RecyclerView.Adapter<AnimsAdapter.AnimsViewHolder>(){

        inner class AnimsViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
            val title: TextView = itemView.findViewById(R.id.item_name_one)
            val description: TextView = itemView.findViewById(R.id.item_desc_one)
            val image:com.makeramen.roundedimageview.RoundedImageView = itemView.findViewById(R.id.item_image_one)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimsViewHolder {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.bilibili_item_one,parent,false)
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

    private fun parseJson(){
        thread {
            try {
                val requestBody = FormBody.Builder()
                    .add("searchText", URLEncoder.encode(searchText,"UTF-8"))
                    .build()
                val client = OkHttpClient()
                val request = Request.Builder()
                    //   .url("http://192.168.137.1:8080/homework/bilibili/userLogin.jsp")
                    .url("${trueIp}:8080/homework/bilibili/search.jsp")
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
        try {
       /*
            Looper.prepare()
            Toast.makeText(applicationContext,jsonStr, Toast.LENGTH_SHORT).show()
            Looper.loop()
*/
            val data=ArrayList<Animations>()
            //json数组
            val jsonArray= JSONArray(jsonStr)
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
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

}