# CircularPercentRing
一个显示百分百的圆环，可适用于计步软件，仪表盘，金额额度，收入支出比例等显示

[![](https://jitpack.io/v/MrRobotter/CircularPercentRing.svg)](https://jitpack.io/#MrRobotter/CircularPercentRing)

### 效果展示
![]( https://github.com/MrRobotter/AndroidGuide/raw/master/resource/image/ring_demo.jpg )

* 方法说明

|**方法** |**说明**|
|:----:|:----:|
|void setRoundBackgroundColor(int roundBackgroundColor)|设置圆环背景色|
|void setColors(int[] colors)|设置渐变色|
| void update(float progress, int time)|更新进度，参数：百分比，动画时间|
| void setRoundWidth(float roundWidth)|设置环的宽度|
| void setCircleWidth(int circleWidth)|设置圆环大小|
| void setLine(boolean line) | 是否为线条（待扩展） |




### How To Use

* 1. Add it in your root build.gradle at the end of repositories:
````
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

````

* 2. Add the dependency

````
	dependencies {
	        implementation 'com.github.MrRobotter:CircularPercentRing:1.1'
	}

````

### 示例代码
* xml
````
    <com.joinyon.circularpercenring.CircularPercentRing
        android:id="@+id/ring"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:layout_gravity="center_horizontal"
        android:layout_marginTop="20dp"
        app:roundProgressColor="@color/colorAccent"
        app:circleRoundWidth="30dp"
        app:circleWidth="200dp"/>

````

* activity

````
public class MainActivity extends AppCompatActivity {

    private CircularPercentRing ring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ring = findViewById(R.id.ring);
        initRing();
    }

    private void initRing() {
        ring.setRoundWidth(30);
        int[] colors = {0xFF00AAFF, 0xFF00AAFF, 0xFF00AAFF};
        ring.setColors(colors);
        ring.update(50.0f, 0);
    }

    public void small(View view) {
        ring.setCircleWidth(200);
    }

    public void big(View view) {
        ring.setCircleWidth(320);
    }

    public void larg(View view) {
        ring.setRoundWidth(30);
    }

    public void li(View view) {
        ring.setRoundWidth(20);
    }

    public void half(View view) {
        ring.update(50.0f, 0);
    }

    public void full(View view) {
        ring.update(100.0f, 0);
    }

    public void quik(View view) {
        ring.update(100.0f, 1500);
    }

    public void slow(View view) {
        ring.update(100.0f, 3000);
    }

    public void single(View view) {
        int[] colors = {0xFFED0957, 0xFFED0957, 0xFFED0957};
        ring.setColors(colors);
    }

    public void colorful(View view) {
        int[] colors = {0xFF11F020,0xFFFFDC40, 0xFFE9151F};
        ring.setColors(colors);
    }
}

````
### 感谢
* 谢谢关注本项目，您有好的建议可以与我取得联系