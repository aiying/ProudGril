package com.wang.girl;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class WelcomeActivity extends Activity implements Runnable {
	public static final int VERSION = 2;
	public static SharedPreferences sp;
	
	private int i=0;
	private ImageView imageView1;
	
	private Timer timer = new Timer();  
	
	public static  MediaPlayer music = null;
	
    private Handler handler = new Handler()  
    {  
        @Override  
        public void handleMessage(Message msg)  
        {  
            if (i > 1)  
            {  
                i = 0;  
            }           
                switch (i)  
                {  
                case 0:  
                	imageView1.setImageResource(R.drawable.wel_girl_l);  
                    break;  
                case 1:  
                	imageView1.setImageResource(R.drawable.wel_girl_r);  
                    break;            
                default:  
                    break;  
                }  
                imageView1.invalidate();  
            
            super.handleMessage(msg);  
        }  
    };  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		music = MediaPlayer.create(this, R.raw.wel);
		music.setLooping(true);
		music.start();
		timer.scheduleAtFixedRate(new TimerTask()  
        {  
            @Override  
            public void run()  
            {  
                // TODO Auto-generated method stub  
               
                Message mesasge = new Message();  
                mesasge.what = ++i;  
                handler.sendMessage(mesasge);  
            }  
        }, 0,300);  
		// 启动一个延迟线程
		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			Thread.sleep(3000);
			
			
			// 读取SharedPreferences中需要的数据
			sp = getSharedPreferences("sharedPrefence", Context.MODE_PRIVATE);
			/**
			 * 如果用户不是第一次使用则直接调转到显示界面,否则调转到引导界面
			 */
			if (sp.getInt("VERSION", 0) != VERSION) {
				startActivity(new Intent(WelcomeActivity.this,
						GuideActivity.class));
			} else {
				startActivity(new Intent(WelcomeActivity.this,
						MenuActivity.class));
			}
			finish();

		} catch (Exception e) {
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//在用完定时器timer后，要在Activity被干掉的同时销毁定时器timer。
		timer.cancel();  
        super.onDestroy();
        
	}

	public static MediaPlayer getMusic() {
		return music;
	}

}
