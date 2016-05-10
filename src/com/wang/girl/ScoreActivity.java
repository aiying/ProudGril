package com.wang.girl;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ScoreActivity extends BaseActivity {
	private ArrayList<HashMap<String, Object>> list;
	private SimpleAdapter adapter;
	private ListView myListView;
	private ImageView iv_back;

	private SharedPreferences sp;

	private SoundPool soundPool;
	public int musicId;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_view);

		soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		musicId = soundPool.load(this, R.raw.button_sound, 1);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				soundPool.play(musicId, 1, 1, 0, 0, 1);
				finish();
			}
		});
		list = new ArrayList<HashMap<String, Object>>();
		adapter = new SimpleAdapter(this, list, R.layout.score_item, new String[] { "score" },
				new int[] { R.id.tv_score });

		myListView = (ListView) findViewById(R.id.listView);
		myListView.setAdapter(adapter);

		sp = getSharedPreferences("sharedPrefence", Context.MODE_PRIVATE);
		String s = sp.getString("scoreArray", "0;0;0;0;0");
		String[] array = s.split(";");

		for (int i = 0; i < 5; i++) {
			AddItem("score", array[i]);
		}

	}

	private void AddItem(String name, String score) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(name, score);
		list.add(map);
		// 方法进行通知该SimpleAdapter内容已经发生改变。
		adapter.notifyDataSetChanged();
	}

}
