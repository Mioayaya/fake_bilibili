package com.app.activity

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.Animations
import com.app.Login
import com.app.MyApplication
import com.app.R
import com.app.utill.showToast
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.lang.Exception
import java.net.URLEncoder
import kotlin.concurrent.thread

class SearchActivity: AppCompatActivity() {

    private var searchText = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // "取消"的点击事件：销毁本活动，返回上一级
        val searchCancelButton = findViewById<TextView>(R.id.search_cancel_button)
        searchCancelButton.setOnClickListener { finish() }
        // 搜索框
        val searchEditText = findViewById<EditText>(R.id.home_edit_text)
        searchEditText.setOnEditorActionListener { _, keyCode, _ ->
            // 如果点击了回车键，即搜索键，就弹出一个toast
            if (keyCode == EditorInfo.IME_ACTION_SEARCH) {
                searchText = searchEditText.text.toString()
//                "你输入了${searchEditText.text}".showToast()
                val intent = Intent(MyApplication.context, ShowSearchActivity::class.java)
                intent.putExtra("searchText=",searchText)
                startActivity(intent)
                true
            } else {
                false
            }
        }


    }


}