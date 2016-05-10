package com.wang.girl;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.types.ccColor4B;
import android.os.Bundle;

public class MainActivity extends BaseActivity {
	private CCGLSurfaceView surface;
	public static CCScene scene;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		
		//cocos2d引擎会把图形加载该view对象上
		surface = new CCGLSurfaceView(this);
		setContentView(surface);
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//而CCDirector就是这个场景对象实例
		//sharedDirector获取对窗口进行操作的句柄
		CCDirector.sharedDirector().end();
		//返回纹理缓存的全局单例
		CCTextureCache.sharedTextureCache().removeAllTextures();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		CCDirector.sharedDirector().pause();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		CCDirector.sharedDirector().resume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		CCDirector.sharedDirector().end();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// 把cocos2d绑定在GLSurfaceView这个载体上
		//得到场景层级的父对象
		CCDirector.sharedDirector().attachInView(surface);
		scene = CCScene.node();
		
		//创建一个玩家
		GamePlay gamePlay = new GamePlay(ccColor4B.ccc4(255, 255, 255, 255));
		//在场景中添加玩家
		scene.addChild(gamePlay);
		
		CCDirector.sharedDirector().runWithScene(scene);
		
		//为什么要暂停？？？？?
		//CCDirector.sharedDirector().pause();
	}

}
