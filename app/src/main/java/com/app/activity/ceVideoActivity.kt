package com.app.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.R
import com.app.databinding.ActivityCeVideoBinding
import com.dou361.ijkplayer.widget.PlayStateParams
import com.dou361.ijkplayer.widget.PlayerView

class ceVideoActivity : AppCompatActivity() {

    //private lateinit var binding: ActivityCeVideoBinding
    private lateinit var binding:ActivityCeVideoBinding

    var nameAddress = ""
    private var video_name = ""
    private var video_id = ""
    private var username =""
    var time = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        initData()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ce_video)

        binding = ActivityCeVideoBinding.inflate(layoutInflater) //初始化
        setContentView(binding.root)
        supportActionBar?.hide()//隐藏标题状态栏

        //视频
        val play: PlayerView = PlayerView(this)
            .setTitle(video_name)
            .setScaleType(PlayStateParams.fitparent)
            .hideMenu(true)
            .forbidTouch(false)
            .setPlaySource(nameAddress)
            //.hideBack(true)
        //image.setImageBitmap(play.fullScreenView)

        play.startPlay()
        play.setAutoReConnect(true,10)
        //play.setShowSpeed(true)
        //play.hideFullscreen(true)
        //play.setForbidDoulbeUp(true)
        play.seekTo(time)
        play.setOnlyFullScreen(true)


    }

    override fun onPause() {
        super.onPause()
        val video = PlayerView(this)
        video.onDestroy()


    }


    override fun onDestroy() {
        super.onDestroy()

        val video = PlayerView(this)

        video.onDestroy()
    }


    private fun initData() {

        username = intent.getStringExtra("username").toString()
        video_id = intent.getStringExtra("video_id").toString()
        nameAddress = intent.getStringExtra("address").toString()
        video_name =intent.getStringExtra("video_name").toString()
        time = intent.getIntExtra("time",0)

    }

}

