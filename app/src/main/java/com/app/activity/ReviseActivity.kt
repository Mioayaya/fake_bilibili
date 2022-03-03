package com.app.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Looper
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.app.*
import com.app.utill.showToast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception
import java.net.URLEncoder
import kotlin.concurrent.thread

class ReviseActivity:Activity() {

    //    private val trueIp = "http://192.168.137.1"
    private val trueIp = "http:192.168.43.44"
    private var userData = UserData("","","","","","0","","","")
    private val JSPIP = "${trueIp}:8080/homework/bilibili/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_revise)
        SetUserInfor()  // 加载用户信息
        Thread.sleep(500)
        val saveButton = findViewById<TextView>(R.id.revise_button_save)
        val reviseBack = findViewById<ImageView>(R.id.revise_icon_back)
        val nameEdit = findViewById<EditText>(R.id.revise_user_name)
        val signEdit = findViewById<EditText>(R.id.revise_user_sign)
        val gradeEdit = findViewById<EditText>(R.id.revise_user_grade)

        nameEdit.setText(userData.name)
        signEdit.setText(userData.sign)
        gradeEdit.setText(userData.grade)


        // 保存按钮
        saveButton.setOnClickListener {
            userData.name = nameEdit.text.toString()
            userData.sign = signEdit.text.toString()
            userData.grade = gradeEdit.text.toString()
            reviseUser()
            gradeEdit.clearFocus()
            "保存成功".showToast()
        }

        // 返回按钮
        reviseBack.setOnClickListener {
            finish()
        }


    }

    private fun reviseUser(){
        thread {
            try {
                val requestBody = FormBody.Builder()
                    .add("name", URLEncoder.encode(userData.name,"UTF-8"))
                    .add("sign", URLEncoder.encode(userData.sign,"UTF-8"))
                    .add("grade", URLEncoder.encode(userData.grade,"UTF-8"))
                    .build()
                val client = OkHttpClient()
                val request = Request.Builder()
                    //   .url("http://192.168.137.1:8080/homework/bilibili/userLogin.jsp")
                    .url("${JSPIP}userRevise.jsp")
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
        val flag = gson.fromJson(jsonStr,ReviseFlag::class.java)
        if(flag.flag=="true"){
            "增加成功".showToast()
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
                    parseWithGsonUser(data)
                    //showResult(data)
                    //parseDataWithJsonObject(data)
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    private fun parseWithGsonUser(jsonStr:String){
        val gson = Gson()
        val typeOf = object : TypeToken<List<UserInfor>>(){}.type
        val data = gson.fromJson<List<UserInfor>>(jsonStr,typeOf)
        for(info in data){
            userData.name = info.name
            userData.sign = info.sign
            userData.grade = info.grade
            userData.doinb = info.doinb.toString()
            userData.binb = info.binb.toString()
        }
/*
        Looper.prepare()
        Toast.makeText(applicationContext,"${jsonStr}", Toast.LENGTH_SHORT).show()
        Looper.loop()
*/
    }

}