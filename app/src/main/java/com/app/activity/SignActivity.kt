package com.app.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.Login
import com.app.R
import com.app.databinding.ActivityMainBinding
import com.app.databinding.ActivitySignBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URLEncoder
import kotlin.concurrent.thread

class SignActivity: Activity() {

    private lateinit var binding:ActivitySignBinding

    var style = ""

    //    private val trueIp = "http://192.168.137.1"
    private val trueIp = "http:192.168.43.44"

    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)

        binding = ActivitySignBinding.inflate(layoutInflater) //初始化
        setContentView(binding.root)
       // supportActionBar?.hide()//隐藏标题状态栏

        val mLogin = binding.mainTitle.sign
        mLogin.setOnClickListener {
            finish()
        }

        val mBtnLogin = binding.mainBtnRegister

        mBtnLogin.setOnClickListener {
            style = "register"
            parseJson()
        }


    }

    private fun parseJson(){
        val username = binding.inputLayout.userName
        val password = binding.inputLayout.passWord

        thread {
            try {
                val requestBody = FormBody.Builder()
                    .add("style", URLEncoder.encode(style,"UTF-8"))
                    .add("username", URLEncoder.encode(username.text.toString(),"UTF-8"))
                    .add("password", URLEncoder.encode(password.text.toString(),"UTF-8"))
                    .build()
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("${trueIp}:8080/homework/bilibili/userLogin.jsp")
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
        val typeOf = object : TypeToken<List<Login>>(){}.type
        val data = gson.fromJson<List<Login>>(jsonStr,typeOf)
        for (info in data){
            //builder.append(info).append("\n")

            if(info.login == "true"){
                Looper.prepare()
                Toast.makeText(applicationContext, "登录成功！", Toast.LENGTH_SHORT).show()
                Looper.loop()
            }else if (info.login == "false"){
                Looper.prepare()
                Toast.makeText(applicationContext, "账号或密码错误！", Toast.LENGTH_SHORT).show()
                Looper.loop()
            }else if (info.register == "true"){
                Looper.prepare()
                Toast.makeText(applicationContext, "注册成功！", Toast.LENGTH_SHORT).show()
                Looper.loop()
            }else if (info.register == "false"){
                Looper.prepare()
                Toast.makeText(applicationContext, "账号已存在！", Toast.LENGTH_SHORT).show()
                Looper.loop()
            }
        }
    }


}