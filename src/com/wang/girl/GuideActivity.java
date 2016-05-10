package com.wang.girl;

import java.util.ArrayList;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 
 * @author YeChao
 * @功能描述：引导界面activity类
 *
 */
public class GuideActivity extends BaseActivity implements OnPageChangeListener {
	// 定义ViewPager对象
	private ViewPager viewPager;
	// 定义一个ArrayList来存放View
	private ArrayList<View> views;
	// 定义各个界面View对象
	private View view1,view4;
	// 定义开始按钮对象

	private Button btnStart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		initView();

	}

	private void initView() {

		viewPager = (ViewPager) findViewById(R.id.viewpager);
		// 实例化各个界面的布局对象
		LayoutInflater mLi = LayoutInflater.from(this);
		view1 = mLi.inflate(R.layout.guide_view1, null);
		view4 = mLi.inflate(R.layout.guide_view4, null);
	

		// 实例化ArrayList对象
		// 将要分页显示的View装入数组中
		views = new ArrayList<View>();
		views.add(view1);
		views.add(view4);

		// 设置监听
		viewPager.setOnPageChangeListener(this);
		// 设置适配器数据
		viewPager.setAdapter(new ViewPagerAdapter(views));

		// 实例化开始按钮
		btnStart = (Button) view4.findViewById(R.id.startBtn);
		// 给开始按钮设置监听

		btnStart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 存入数据并提交
				WelcomeActivity.sp.edit().putInt("VERSION", WelcomeActivity.VERSION).commit();
				startActivity(new Intent(GuideActivity.this, MenuActivity.class));
				finish();
			}

		});
	}

	/**
	 * 滑动状态改变时调用
	 */
	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	/**
	 * 当前页面滑动时调用
	 */
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	/**
	 * 新的页面被选中时调用
	 */
	@Override
	public void onPageSelected(int arg0) {
	}

}
