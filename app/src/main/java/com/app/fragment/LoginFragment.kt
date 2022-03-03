package com.app.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.app.Login
import com.app.MyApplication
import com.app.R
import com.app.activity.SignActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception
import java.lang.StringBuilder
import java.net.URLEncoder
import kotlin.concurrent.thread

class LoginFragment:Fragment() {



    var style = ""

    private lateinit var mBtnLogin: TextView
    private lateinit var progress:View
    private lateinit var mInputLayout:View
    private lateinit var mName: LinearLayout
    private lateinit var mPsw: LinearLayout
    private lateinit var mSign: View
    private lateinit var username: EditText    // 账号
    private lateinit var password: EditText     // 密码

    //    private val trueIp = "http://192.168.137.1"
    private val trueIp = "http:192.168.43.44"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        mBtnLogin = view.findViewById(R.id.main_btn_login)
        progress = view.findViewById(R.id.layout_progress)
        mInputLayout = view.findViewById(R.id.input_layout)
        mName = view.findViewById(R.id.input_layout_name)
        mPsw = view.findViewById(R.id.input_layout_psw)
        mSign = view.findViewById(R.id.main_title)
        username = view.findViewById(R.id.userName)
        password = view.findViewById(R.id.passWord)


        return view

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mSign.setOnClickListener {
            val intent = Intent(MyApplication.context,SignActivity::class.java)
            startActivity(intent)
        }

        mBtnLogin.setOnClickListener {
            style = "login"
            parseJson()
        }

    }

    private fun parseJson(){


        thread {
            try {
                val requestBody = FormBody.Builder()
                    .add("style", URLEncoder.encode(style,"UTF-8"))
                    .add("username", URLEncoder.encode(username.text.toString(),"UTF-8"))
                    .add("password", URLEncoder.encode(password.text.toString(),"UTF-8"))
                    .build()
                val client = OkHttpClient()
                val request = Request.Builder()
                 //   .url("http://192.168.137.1:8080/homework/bilibili/userLogin.jsp")
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
                Toast.makeText(MyApplication.context, "登录成功！", Toast.LENGTH_SHORT).show()
                /*
                val intent = Intent(this,VideoActivity::class.java)
                intent.putExtra("username",username.text.toString())
                intent.putExtra("video_id","1")
                startActivity(intent)
                */
                Looper.loop()


            }else if (info.login == "false"){
                Looper.prepare()
                Toast.makeText(MyApplication.context, "账号或密码错误！", Toast.LENGTH_SHORT).show()
                Looper.loop()
            }else if (info.register == "true"){
                Looper.prepare()
                Toast.makeText(MyApplication.context, "注册成功！", Toast.LENGTH_SHORT).show()
                Looper.loop()
            }else if (info.register == "false"){
                Looper.prepare()
                Toast.makeText(MyApplication.context, "账号已存在！", Toast.LENGTH_SHORT).show()
                Looper.loop()
            }
        }
    }



}