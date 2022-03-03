package com.app.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.ImageView
import android.widget.TextView
import com.app.MyApplication
import com.app.R
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class UsActivity:Activity() {

    private val headImageERSJ = "https://i2.hdslb.com/bfs/face/c54739e08ec1e426b259d0164fb5a082793af1dd.jpg@240w_240h_1c_1s.webp" // 头像二人三角
    private val headImageXCMD = "https://i2.hdslb.com/bfs/face/cf8d0946c4ec4bad786bbb2cecd501abaadca90e.jpg@.webp" // 头像星川麻冬
    private val headImageRaftern = "https://avatars.githubusercontent.com/u/54238854?v=4" // github头像
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_us)
        val headViewERSJ = findViewById<CircleImageView>(R.id.head_image_ERSJ)
        val headViewXCMD = findViewById<CircleImageView>(R.id.head_image_XCMD)
        val headViewRaftern = findViewById<CircleImageView>(R.id.head_image_Raftern)
        Glide.with(this).load(headImageERSJ).into(headViewERSJ)
        Glide.with(this).load(headImageXCMD).into(headViewXCMD)
        Glide.with(this).load(headImageRaftern).into(headViewRaftern)
        val textBiliERSJ = findViewById<TextView>(R.id.html_bilibili_ersj)
        val textBiliXCMD = findViewById<TextView>(R.id.html_bilibili_xcmd)
        val textGithubRaftern = findViewById<TextView>(R.id.html_github_Raftern)

        // 二人三角页面
        textBiliERSJ.setOnClickListener {
            // https://space.bilibili.com/12917066/?spm_id_from=333.999.0.0
            val intent = Intent(MyApplication.context, WebActivity::class.java)
            intent.putExtra("webUrl=","https://space.bilibili.com/12917066/?spm_id_from=333.999.0.0")
            startActivity(intent)
        }

        // 星川麻冬页面
        textBiliXCMD.setOnClickListener {
            val intent = Intent(MyApplication.context, WebActivity::class.java)
            intent.putExtra("webUrl=","https://space.bilibili.com/22638206")
            startActivity(intent)
        }

        // Raftern页面
        textGithubRaftern.setOnClickListener {
            val intent = Intent(MyApplication.context, WebActivity::class.java)
            intent.putExtra("webUrl=","https://github.com/Raftern")
            startActivity(intent)
        }

        val reviseBack = findViewById<ImageView>(R.id.revise_icon_back)
        reviseBack.setOnClickListener {
            finish()
        }
    }

}