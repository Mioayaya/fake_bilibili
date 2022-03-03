# bilibiliFake App

## ip地址
手机开wlan： 192.168.43.44

电脑开热点ip：192.168.137.1

## 项目描述:
android开发使用kotlin语言，完成一个仿bilibili的动漫视频播放软件
## 开发思路：
1.采用recyclerview标签替代scrollview-gridlayout-include 这个嵌套

2.采用RoundedImageView代替imageview实现圆角效果

3.使用自己创建的viewpager设置不可滑动

## 遇到的问题
Q1: GridLayoutManager的contex类型怎么填

A1：(this,2) 第一个参数代表整个activity 第二个表示有两列 Mainactivity本身就是一个contex
    或者是parent.contex

    新建一个MyApplication类里面重写context
    如下：

    class MyApplication : Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context:Context
    }
    override fun onCreate() {
        super.onCreate()
        context = baseContext
    }
}

Q2: 如何传递图片的src

A1: 采用GitHub上的开源结构

    Glide.with(MyApplication.context).load(anims.image).into(holder.image)

 需要添加依赖以及设置网络权限

    implementation 'com.github.bumptech.glide:glide:4.12.0'

Q3: Kotlin 匿名内部类对象引用当前Activity的this用法

A3: 采用this@MainActivity的方法，出现Unresolved reference: @MainActivity 的报错信息
    MyAdapter类没有加inner造成无法识别

Q4: 无法通过tomcat获取url图片

A4: 在AndroidManifest.xml的<application里添加   android:usesCleartextTraffic="true"


Q5: 如何设置cardview之间的上下距离

A5: 在MaterialCardView里添加 android:layout_marginBottom="10dp"

Q6: 新增番剧id后，主页面无法显示列表了

A6：新增json格式后，忘记添加“,”

Q7: 传递到新的activity并没有显示需要传递的id

A7: 传递函数以及参数类型不匹配

Q8: 如何让底部导航栏的文字描述一直存在

A8: 在控件中添加 app:labelVisibilityMode="labeled"

Q9: 如何在MaterialComponents属性下设置按钮颜色

A9: 使用app:backgroumdTint:"color" 而不是 简单的android:background:"color"

Q10: 如何在标题栏中显示白色字体

A10: textview中设置深色主题 android:theme="@style/ThemeOverlay.MaterialComponents.Dark"

Q11: 如何隐藏app状态栏

A11: 可以通过

this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

实现页面全屏的方式来实现，但是会有黑边

Q12: 如何实现安卓客户端的上滑自动隐藏标题栏效果

A12: 

    <androidx.coordinatorlayout.widget.CoordinatorLayout>

        <com.google.android.material.appbar.AppBarLayout>

            <com.google.android.material.appbar.CollapsingToolbarLayout>

            滑动出页面视图

            </com.google.android.material.appbar.CollapsingToolbarLayout>

            跟着滑动上去会卡在顶部

        </com.google.android.material.appbar.AppBarLayout>

        <androidx.recyclerview.widget.RecyclerView>

        滚动的列表内容(不是放在recyclerView里，recyclerView调用的这个列表内容通过recyclerView进行滚动)

        </androidx.recyclerview.widget.RecyclerView>
        
    </androidx.coordinatorlayout.widget.CoordinatorLayout>

不需要滑动的内容写在CollapsingToolbarLayout与AppBarLayout中间即可

滑动内容写住AppBarLayout里

Q13: findViewById寻找include标签的id类型时，引用哪个类型?

A13: 如下使用View即可

private lateinit var progress:View

Q14: findViewById寻找 按钮目录<item id类型时，引用哪个类型?

A14: 见如下代码

        toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.tool_us -> {val intent = Intent(MyApplication.context, ReviseActivity::class.java)
                    startActivity(intent) }
                R.id.tool_add -> {val intent = Intent(MyApplication.context,SignActivity::class.java)
                    startActivity(intent)}
            }
            false
        }

Q15: 如何消除editText的黑色下划线?

A15: 首先在theme.xml里添加style类型

    <style name="MyEditText" parent="Theme.AppCompat.Light">
        <item name="colorControlNormal">@color/white</item>   
        <item name="colorControlActivated">@color/pink</item>
    </style>

    colorControlNormal未点击时下划线颜色  colorControlActivated点击时下划线颜色

    最后在EditText里添加属性 
    
    android:theme="@style/MyEditText"

Q16: 获取MySQL数据后，gson解析后得到数据，数据类也更新了数据，但是显示的还是初始化的数据

A16: 由于在没更新前就完成了页面的显示(用的还是之前的数据)，所以在控件改变数据之前要加一个sleep的线程 如

    Thread.sleep(500)

Q17: fragment数据更新后页面数据不会改变

A17: 使用刷新函数手动刷新

refresh.setOnRefreshListener {
            thread {
                Thread.sleep(700)
                activity?.runOnUiThread{
                    SetUserInfor()
                    Thread.sleep(500)
                    /*
                    code: 更新用户数据
                    */
                    refresh.isRefreshing = false
                }
            }
        }

Q18: editText相关

A18: 

    android:numeric="integer"           设置只能输入数字
    android:maxLength="1"               设置最大数量
    android:singleLine="true"           设置单行输入
    android:imeOptions="actionSearch"   将回车键变成搜索键
    android:cursorVisible="true         设置光标可见

## BUG记录
Q1：E/MemoryLeakMonitorManager: MemoryLeakMonitor.jar is not exist!

    E/AwareLog: AtomicFileUtils: readFileLines file not exist: android.util.AtomicFile@14b94f8
A1: if语句没有加else 可能是，我加了else之后就成功了

Q2: com.google.android.material.tabs.TabLayout cannot be cast to android.widget.TableLayout

A2: findViewById<TabLayout>(R.id.anim_tab_layout)中TabLayout错写成 Tabelayout 导致控件类型不匹配

Q3: 程序跳转activity时闪退

A3: application里没有注册activity


## 数据库建立

|表名| 属性|
|:-----:|:-----:|
|user|id 账号 密码 名字 个性签名 观看数量 硬币数量 B币数量 登录状态|