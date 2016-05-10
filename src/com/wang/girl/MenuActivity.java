package com.wang.girl;

import java.util.Timer;
import java.util.TimerTask;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MenuActivity extends BaseActivity{

	private  int isLeft = 0;
	public  SharedPreferences sp;

	private Button newGame,scores,exit;
	private ImageView girl, iv_this,iv_free,iv_music,iv_music_no;
	private LinearLayout linear_options;
	
	private Timer timer = new Timer();
	
	//按钮点击声音
	public  SoundPool soundPool;//声明一个SoundPool
    public int musicId;//定义一个整型用load（）；来设置suondID
	
	//背景音乐播放器
	private MediaPlayer music;
	
private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(isLeft>1){
				isLeft=0;
			}
			switch (msg.what){
			case 0:
				girl.setBackgroundResource(R.drawable.wel_girl_l);
				break;
			case 1:
				girl.setBackgroundResource(R.drawable.wel_girl_r);
				break;
			}
			girl.invalidate();  
		};
	};
	  

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_menu);
		sp = getSharedPreferences("sharedPrefence", Context.MODE_PRIVATE);
		
		music = WelcomeActivity.getMusic();
		
		soundPool= new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        musicId = soundPool.load(this, R.raw.button_sound, 1); 
        //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级

		newGame = (Button) findViewById(R.id.newGame);
		newGame.setOnClickListener(new MyListener());
		
		scores = (Button) findViewById(R.id.scores);
		scores.setOnClickListener(new MyListener());
		
		exit = (Button) findViewById(R.id.exit);
		exit.setOnClickListener(new MyListener());
		
		iv_music = (ImageView) findViewById(R.id.iv_music);
		iv_music.setOnClickListener(new MyListener());
		iv_music_no = (ImageView) findViewById(R.id.iv_music_no);
		iv_music_no.setOnClickListener(new MyListener());
		
		girl = (ImageView) findViewById(R.id.girl);

		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = new Message();
				msg.what= isLeft++;
				handler.sendEmptyMessage(msg.what);
				
			}
		}, 0, 300);
		
		linear_options = (LinearLayout) findViewById(R.id.linear_options);
		iv_this = (ImageView) findViewById(R.id.iv_this);
		iv_this.setOnClickListener(new MyListener());
		
		iv_free = (ImageView) findViewById(R.id.iv_free);
		iv_free.setOnClickListener(new MyListener());
	}
	
	private class MyListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()){
			case R.id.newGame:
				soundPool.play(musicId, 1, 1, 0, 0, 1);
				//第一个参数指定播放哪个声音； leftVolume 、 rightVolume 指定左、右的音量： priority 指定播放声音的优先级，数值越大，优先级越高； loop 指定是否循环， 0 为不循环， -1 为循环； rate 指定播放的比率，数值可从 0.5 到 2 ， 1 为正常比率。
				newGame.setVisibility(View.GONE);
				linear_options.setVisibility(View.VISIBLE);
				
				break;
			case R.id.scores:
				soundPool.play(musicId, 1, 1, 0, 0, 1);
				startActivity(new Intent(MenuActivity.this,ScoreActivity.class));
				break;
			case R.id.exit:
				soundPool.play(musicId, 1, 1, 0, 0, 1);
				music.stop();
				finish();
				break;
			case R.id.iv_this:
				soundPool.play(musicId, 1, 1, 0, 0, 1);
				//music.pause();
				startActivity(new Intent(MenuActivity.this,MainActivity.class));
				break;
			case R.id.iv_free:
				soundPool.play(musicId, 1, 1, 0, 0, 1);
				music.pause();
				//进入本地图库	
				Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				intent.setType("image/*");//可选择图片视频
				//修改为以下两句代码
				intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//使用以上这种模式，并添加以上两句
				startActivityForResult(intent, 1);

				break;
			case R.id.iv_music:
				soundPool.play(musicId, 1, 1, 0, 0, 1);
					music.pause();
				iv_music.setVisibility(View.GONE);
				iv_music_no.setVisibility(View.VISIBLE);
				
				break;
			case R.id.iv_music_no:
				soundPool.play(musicId, 1, 1, 0, 0, 1);
				music.start();
			iv_music_no.setVisibility(View.GONE);
			iv_music.setVisibility(View.VISIBLE);
			
			break;
			}
			
		}	
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(data!=null){
			Uri uri = data.getData();
			sp.edit().putString("uri", uri.toString()).commit();
			System.out.println(uri.toString());
			startActivity(new Intent(MenuActivity.this,FreeActivity.class));
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		music.stop();
		music.release();
		timer.cancel();
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		music.start();
		super.onStart();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		music.start();
		super.onRestart();
	}

	

	
	


}
