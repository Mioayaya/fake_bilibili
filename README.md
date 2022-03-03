# bilibiliFake App

## ip��ַ
�ֻ���wlan�� 192.168.43.44

���Կ��ȵ�ip��192.168.137.1

## ��Ŀ����:
android����ʹ��kotlin���ԣ����һ����bilibili�Ķ�����Ƶ�������
## ����˼·��
1.����recyclerview��ǩ���scrollview-gridlayout-include ���Ƕ��

2.����RoundedImageView����imageviewʵ��Բ��Ч��

3.ʹ���Լ�������viewpager���ò��ɻ���

## ����������
Q1: GridLayoutManager��contex������ô��

A1��(this,2) ��һ��������������activity �ڶ�����ʾ������ Mainactivity�������һ��contex
    ������parent.contex

    �½�һ��MyApplication��������дcontext
    ���£�

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

Q2: ��δ���ͼƬ��src

A1: ����GitHub�ϵĿ�Դ�ṹ

    Glide.with(MyApplication.context).load(anims.image).into(holder.image)

 ��Ҫ��������Լ���������Ȩ��

    implementation 'com.github.bumptech.glide:glide:4.12.0'

Q3: Kotlin �����ڲ���������õ�ǰActivity��this�÷�

A3: ����this@MainActivity�ķ���������Unresolved reference: @MainActivity �ı�����Ϣ
    MyAdapter��û�м�inner����޷�ʶ��

Q4: �޷�ͨ��tomcat��ȡurlͼƬ

A4: ��AndroidManifest.xml��<application�����   android:usesCleartextTraffic="true"


Q5: �������cardview֮������¾���

A5: ��MaterialCardView����� android:layout_marginBottom="10dp"

Q6: ��������id����ҳ���޷���ʾ�б���

A6������json��ʽ��������ӡ�,��

Q7: ���ݵ��µ�activity��û����ʾ��Ҫ���ݵ�id

A7: ���ݺ����Լ��������Ͳ�ƥ��

Q8: ����õײ�����������������һֱ����

A8: �ڿؼ������ app:labelVisibilityMode="labeled"

Q9: �����MaterialComponents���������ð�ť��ɫ

A9: ʹ��app:backgroumdTint:"color" ������ �򵥵�android:background:"color"

Q10: ����ڱ���������ʾ��ɫ����

A10: textview��������ɫ���� android:theme="@style/ThemeOverlay.MaterialComponents.Dark"

Q11: �������app״̬��

A11: ����ͨ��

this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

ʵ��ҳ��ȫ���ķ�ʽ��ʵ�֣����ǻ��кڱ�

Q12: ���ʵ�ְ�׿�ͻ��˵��ϻ��Զ����ر�����Ч��

A12: 

    <androidx.coordinatorlayout.widget.CoordinatorLayout>

        <com.google.android.material.appbar.AppBarLayout>

            <com.google.android.material.appbar.CollapsingToolbarLayout>

            ������ҳ����ͼ

            </com.google.android.material.appbar.CollapsingToolbarLayout>

            ���Ż�����ȥ�Ῠ�ڶ���

        </com.google.android.material.appbar.AppBarLayout>

        <androidx.recyclerview.widget.RecyclerView>

        �������б�����(���Ƿ���recyclerView�recyclerView���õ�����б�����ͨ��recyclerView���й���)

        </androidx.recyclerview.widget.RecyclerView>
        
    </androidx.coordinatorlayout.widget.CoordinatorLayout>

����Ҫ����������д��CollapsingToolbarLayout��AppBarLayout�м伴��

��������дסAppBarLayout��

Q13: findViewByIdѰ��include��ǩ��id����ʱ�������ĸ�����?

A13: ����ʹ��View����

private lateinit var progress:View

Q14: findViewByIdѰ�� ��ťĿ¼<item id����ʱ�������ĸ�����?

A14: �����´���

        toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.tool_us -> {val intent = Intent(MyApplication.context, ReviseActivity::class.java)
                    startActivity(intent) }
                R.id.tool_add -> {val intent = Intent(MyApplication.context,SignActivity::class.java)
                    startActivity(intent)}
            }
            false
        }

Q15: �������editText�ĺ�ɫ�»���?

A15: ������theme.xml�����style����

    <style name="MyEditText" parent="Theme.AppCompat.Light">
        <item name="colorControlNormal">@color/white</item>   
        <item name="colorControlActivated">@color/pink</item>
    </style>

    colorControlNormalδ���ʱ�»�����ɫ  colorControlActivated���ʱ�»�����ɫ

    �����EditText��������� 
    
    android:theme="@style/MyEditText"

Q16: ��ȡMySQL���ݺ�gson������õ����ݣ�������Ҳ���������ݣ�������ʾ�Ļ��ǳ�ʼ��������

A16: ������û����ǰ�������ҳ�����ʾ(�õĻ���֮ǰ������)�������ڿؼ��ı�����֮ǰҪ��һ��sleep���߳� ��

    Thread.sleep(500)

Q17: fragment���ݸ��º�ҳ�����ݲ���ı�

A17: ʹ��ˢ�º����ֶ�ˢ��

refresh.setOnRefreshListener {
            thread {
                Thread.sleep(700)
                activity?.runOnUiThread{
                    SetUserInfor()
                    Thread.sleep(500)
                    /*
                    code: �����û�����
                    */
                    refresh.isRefreshing = false
                }
            }
        }

Q18: editText���

A18: 

    android:numeric="integer"           ����ֻ����������
    android:maxLength="1"               �����������
    android:singleLine="true"           ���õ�������
    android:imeOptions="actionSearch"   ���س������������
    android:cursorVisible="true         ���ù��ɼ�

## BUG��¼
Q1��E/MemoryLeakMonitorManager: MemoryLeakMonitor.jar is not exist!

    E/AwareLog: AtomicFileUtils: readFileLines file not exist: android.util.AtomicFile@14b94f8
A1: if���û�м�else �����ǣ��Ҽ���else֮��ͳɹ���

Q2: com.google.android.material.tabs.TabLayout cannot be cast to android.widget.TableLayout

A2: findViewById<TabLayout>(R.id.anim_tab_layout)��TabLayout��д�� Tabelayout ���¿ؼ����Ͳ�ƥ��

Q3: ������תactivityʱ����

A3: application��û��ע��activity


## ���ݿ⽨��

|����| ����|
|:-----:|:-----:|
|user|id �˺� ���� ���� ����ǩ�� �ۿ����� Ӳ������ B������ ��¼״̬|