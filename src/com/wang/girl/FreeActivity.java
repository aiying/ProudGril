package com.wang.girl;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.types.ccColor4B;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

public class FreeActivity extends BaseActivity {
	public  SharedPreferences sp;
	public static  Bitmap bmp;
	private CCGLSurfaceView surface;
	public static CCScene scene;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		surface = new CCGLSurfaceView(this);
		setContentView(surface);	
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();		
	        CCDirector.sharedDirector().attachInView(surface);
	        scene = CCScene.node();
	      //创建一个玩家
			GamePlayFree2 gamePlay;
			try {
				gamePlay = new GamePlayFree2(ccColor4B.ccc4(255, 255,255,255));
				//在场景中添加玩家
				scene.addChild(gamePlay);		
				CCDirector.sharedDirector().runWithScene(scene);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}  
	
}
