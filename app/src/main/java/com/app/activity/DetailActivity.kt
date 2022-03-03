package com.app.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.app.R

class DetailActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)  // 返回按钮
        val name = intent.getStringExtra("name=")
        val textView = findViewById<TextView>(R.id.anim_id_text)
        textView.text = name
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId==android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}

