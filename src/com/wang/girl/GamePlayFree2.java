package com.wang.girl;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.cocos2d.actions.base.CCFiniteTimeAction;
import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.nodes.CCSpriteSheet;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class GamePlayFree2 extends CCColorLayer {		
	private static final int overScore = 4000;
	
	// 自定义动画组
		private Bitmap bitmap;
		private Bitmap bitmapBig;
		private List<FreeSprite> myFreeList = new CopyOnWriteArrayList<FreeSprite>();
		private int myFreeDownSpeed = 8; // 下降的速度
		private int myFreeLift=3;
		private int count = 0;
		
	// 暂停按钮
	private CCSprite pause;
	private CCSprite play;
	private String playPath = "images/play.png";
	private String pausePath = "images/pause.png";
	private boolean isPause = false;
	
	//暂停按钮点击声音
	private  SoundPool soundPool;//声明一个SoundPool
	 HashMap<Integer, Integer> musicId = new HashMap<Integer, Integer>();;//定义一个整型用load（）；来设置suondID
	 
	// 创建一个分数标记
	private CCLabel scoreLabel; // 分数标签
	private int score;
	private String scorePath = "images/Cookies.ttf";

	// 敌人的爱心
	private List<GameSprite> smallLoves = new CopyOnWriteArrayList<GameSprite>();
	private String smallLovePath = "images/smallLove.png";
	private String smallLoveSeqPath = "images/smallLove_seq.png";
	private int smallLoveLife = 1;
	private int smallLoveDownSpeed = 8; // 下降的速度
	


	// 小女孩
	private CCSprite girl;
	private String girlBeforePath = "images/second_girl.png";
	private String girlAfterPath = "images/second_girl_l.png";
	private float girlChange = 1;
	private int girlLife = 5;
	
	//游戏结束动画素材
	private CCSprite girlOver;
	private String girlOverPath = "images/girl_over.png";
	private String girlOverPath2 = "images/girl_over2.png";
	private String girlOverPath3 = "images/girl_over3.png";
	private String girlOverPath4 = "images/girl_over4.png";
	private int overInt =0;
	
	private CCSprite fishOver;
	private String fishOverPath = "images/fish_over.png";
	private String fishOverPath2 = "images/fish_over2.png";
	private int fishOverInt =0;
	

	// 创建一个女孩生命标记
	private CCSprite girlLifeHeart;
	private String girLifeHeartPath1 = "images/girl_life_heart.png";
	private String girLifeHeartPath2 = "images/girl_life_heart2.png";
	private String girLifeHeartPath3 = "images/girl_life_heart3.png";
	private String girLifeHeartPath4 = "images/girl_life_heart4.png";
	private String girLifeHeartPath5 = "images/girl_life_heart5.png";

	// 小女孩发射的气泡
	private List<CCSprite> qipaos = new CopyOnWriteArrayList<CCSprite>();
	private String qipaoPath = "images/qipao.png";
	private float qipaoSpeed = 0.5f; // 气泡的速度

	// 触屏的位置
	private CGPoint touchPosition;
	private boolean canMove = false; // 小女孩是否移动

	// 背景图片路径
	private String backgroungPath = "images/run_ground.jpg";

	//系统属性
	private CGSize WinSize; // 屏幕大小
	private SharedPreferences sp_wel; // 配置文件
	private int[] sp_score = new int[]{0,0,0,0,0};
	private Activity sceneActivity;
	private MediaPlayer music;
	
	//结束时显示分数的对话框
	private Dialog myDialog;
	private Dialog lastDialog;
	
	protected GamePlayFree2(ccColor4B color) throws FileNotFoundException {
		super(color);
		// TODO Auto-generated constructor stub
		Init();
	}

	@SuppressWarnings("deprecation")
	public void Init() throws FileNotFoundException {
		music = WelcomeActivity.getMusic();
		sceneActivity = CCDirector.sharedDirector().getActivity();
		sp_wel = sceneActivity.getSharedPreferences("sharedPrefence",
				Context.MODE_PRIVATE);
		WinSize = CCDirector.sharedDirector().displaySize();
		setIsTouchEnabled(true);
		
		soundPool= new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
		musicId .put(1, soundPool .load( sceneActivity , R.raw. button_sound , 1));
	       musicId .put(2, soundPool .load( sceneActivity , R.raw.love , 1)); 
	       musicId .put(3, soundPool .load( sceneActivity , R.raw.girl_sound , 1)); 
	       musicId .put(4, soundPool .load( sceneActivity , R.raw.tixing , 1)); 
	       musicId .put(5, soundPool .load( sceneActivity , R.raw.girl_sound_l , 1)); 

		// 添加背景
		CCSprite background = CCSprite.sprite(backgroungPath);
		background.setScaleX(WinSize.width / background.getTexture().getWidth());
		background.setScaleY(WinSize.height / background.getTexture().getHeight());
		background.setPosition(CGPoint.make(WinSize.width / 2, WinSize.height / 2));
		addChild(background);

		// 初始化显示暂停按钮和分数（此时分数为0）
		play = CCSprite.sprite(playPath);
		float length = play.getContentSize().width / 2 + 15;
		play.setPosition(length, length);
		pause = CCSprite.sprite(pausePath);
		pause.setPosition(length, length);
		pause.setVisible(false);
		this.addChild(play);
		this.addChild(pause);
		ChangeScore(0);

		 CreateFree();
		
		AddHeart(girlLife);   
		// 每隔一段时间执行一次，添加敌方的爱心
		this.schedule("AddHeart", 0.2f);    //生命值
		this.schedule("GameSmallLove", 1f);
	
		this.schedule("AddQipao", 0.2f);
		GameGirl(); // 最开始创建一个女孩
		this.schedule("AddGirl", 0.2f);// 1秒执行一次，女孩会动了
		this.schedule("Detection", 0f);
		// 给myFree添加动画
		this.schedule("AddFree", 3f);

	}
	public void AddFree(float f) throws FileNotFoundException {
		FreeSprite myFree = new FreeSprite();
		myFree.setFree(bitmapBig);
		myFree.setFreeLift(myFreeLift);
		MyFreeDown(myFree, count);
		count++;
		if (count >= 4) {
			count = 0;
		}
	}

	public void MyFreeDown(FreeSprite free, int count) {		
		//下降的速度
		Random rand = new Random();
		int minDuration = myFreeDownSpeed - 4; // 爱心下降的速度
		int maxDuration = myFreeDownSpeed;
		int rangeDuration = maxDuration - minDuration;// 爱心下降的速度范围
		int actualDuration = rand.nextInt(rangeDuration) + minDuration;
		if (actualDuration < 0) {
			actualDuration = rand.nextInt(3) + 3;
		}
		
		//起始位置

		float x=free.getFree().getContentSize().width/2;
		float y = free.getFree().getContentSize().height/2;
		float xW = WinSize.width / 2;
		float yW = WinSize.height / 2;

		float[][] pos = { { -x,  yW}, {x+2*xW,yW+y},{xW-x,y+2*yW}, {xW+x,-y} };
		float[][] offsetPos = {{xW+x,y+2*yW},{xW-x,-y},{x+2*xW,yW-y},{-x,yW+y}};
				
		free.getFree().setPosition(CGPoint.ccp(pos[count][0], pos[count][1]));
		addChild(free.getFree());
		myFreeList.add(free);

		// 往上的爱心，在规定时间内动作，在时间内移动到目的地
		CCFiniteTimeAction fs_timeAction = CCMoveTo.action(actualDuration, CGPoint.ccp(offsetPos[count][0],offsetPos[count][1]));// CCMoveBy是向量，相当于从当前点开始加上你的点的大小就是移动过后的位置
		CCCallFuncN treeLoveOver = CCCallFuncN.action(this, "FreeOver");
		CCSequence fs_actions = CCSequence.actions(fs_timeAction, treeLoveOver);// 循环运行
		free.getFree().runAction(fs_actions);
		
	}
	public void FreeOver(Object obj) {
		CCSprite sprite = (CCSprite) obj;
		myFreeList.remove(sprite);
		sprite.removeSelf();
	}
	
	public void CreateFree() throws FileNotFoundException {
		String uriStr = sp_wel.getString("uri", "");
		Uri uri = Uri.parse(uriStr);

		// inJustDecodeBounds该值设为true,将不返回实际的bitmap,不给其分配内存空间
		// 只是得到图片大小信息，获取到outHeight(图片原始高度)和
		// outWidth(图片的原始宽度)，然后计算一个inSampleSize(缩放值)
		BitmapFactory.Options options = new BitmapFactory.Options();
		/*options.inJustDecodeBounds = true;
		// 用uri来获取图片， 获取这个图片的宽和高，此时bitmap为空
		bitmap = BitmapFactory.decodeStream(sceneActivity.getContentResolver().openInputStream(uri), null, options);*/
		// 重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false
		options.inJustDecodeBounds = false;
		int biH = options.outHeight / 200;
		int biW = options.outWidth / 200;
		int bi = 0;
		if (biH > biW) {
			bi = biH;
		} else {
			bi = biW;
		}
		if (bi <= 0) {
			bi = 1;
		}
		options.inSampleSize = bi;// 图片宽高都为原来bi分之一
		// 此时bitmap不为空，为缩放后的图片大小
		bitmapBig = BitmapFactory.decodeStream(sceneActivity.getContentResolver().openInputStream(uri), null, options);
	}

	private void AddHeart(int num) {
		// 添加女孩生命值
		if (girlLifeHeart != null||num<=0) {
			girlLifeHeart.removeSelf();
		}
		if(num>0){
			switch (num) {
			case 5:
				girlLifeHeart = CCSprite.sprite(girLifeHeartPath5);
				break;
			case 4:
				girlLifeHeart = CCSprite.sprite(girLifeHeartPath4);
				break;
			case 3:
				girlLifeHeart = CCSprite.sprite(girLifeHeartPath3);
				break;
			case 2:
				girlLifeHeart = CCSprite.sprite(girLifeHeartPath2);
				break;
			case 1:
				girlLifeHeart = CCSprite.sprite(girLifeHeartPath1);
				break;

			}
			float x = WinSize.width - girlLifeHeart.getContentSize().width;
			float y = WinSize.height - girlLifeHeart.getContentSize().height;
			girlLifeHeart.setPosition(x, y);
			this.addChild(girlLifeHeart);
		}
		
	}

	// 添加碰撞监听器
	public void Detection(float t) {
		// 拿每一个气泡与爱心匹配，装上了就吸收爱心
		for (int i = 0; i < qipaos.size(); i++) {
			CCSprite qipao = qipaos.get(i);
			CGRect rectQipao = qipao.getBoundingBox();
			for (int j = 0; j < smallLoves.size(); j++) {
				GameSprite small = smallLoves.get(j);
				CGRect rectSmall = small.getCCSprite().getBoundingBox();
				if (CGRect.intersects(rectQipao, rectSmall)) {
					// 气泡和爱心碰撞在一起，爱心和气泡都消失	
					soundPool.play(musicId.get(2), 1, 1, 0, 0, 1);
					small.Life -= 1;
					qipaos.remove(qipao);
					qipao.removeSelf();
					if (small.Life <= 0) {
						// 小爱心消失，获得分数
						ChangeScore(100);
						// 碰到爱心之后需要分割
						
						AddSpriteAnimal(small.getCCSprite().getPosition(), smallLoveSeqPath,
								small.getCCSprite().getContentSize().width, small.getCCSprite().getContentSize().height,
								2,0.1f);
						smallLoves.remove(small);
						small.getCCSprite().removeSelf();
					}
					
				}
			}
			
		}
		
		
		// 拿每一个气泡与自定义图片匹配，装上了就吸收爱心
				for (int i = 0; i < qipaos.size(); i++) {
					CCSprite qipao = qipaos.get(i);
					CGRect rectQipao = qipao.getBoundingBox();
					for (int j = 0; j < myFreeList.size(); j++) {
						FreeSprite small = myFreeList.get(j);
						CGRect rectSmall = small.getFree().getBoundingBox();
						if (CGRect.intersects(rectQipao, rectSmall)) {
							// 气泡和爱心碰撞在一起，爱心和气泡都消失	
							soundPool.play(musicId.get(2), 1, 1, 0, 0, 1);
							small.freeLift -= 1;
							qipaos.remove(qipao);
							qipao.removeSelf();
						
							if (small.freeLift <= 0) {
								// 小爱心消失，获得分数
								ChangeScore(300);
								// 碰到爱心之后需要分割							
								myFreeList.remove(small);
								small.getFree().removeSelf();
							}
							
						}
					}
					
				}
		
		// 爱心与女孩碰撞，女孩的生命值减1，这个爱心也消失
		CGRect rectGirl = girl.getBoundingBox();
		for (int i = 0; i < smallLoves.size(); i++) {
			GameSprite small = smallLoves.get(i);
			CGRect rectSmall = small.getCCSprite().getBoundingBox();
			if (CGRect.intersects(rectSmall, rectGirl)) {
			 soundPool.play(musicId.get(4), 1, 1, 0, 0, 1);
				smallLoves.remove(small);
				small.getCCSprite().removeSelf();
				girlLife -= 1;
				disappearGirl(girlLife);
				AddHeart(girlLife);			
			}
		}
		//自定义的图片与小女孩碰撞，小女孩生命值减2
		for (int i = 0; i < myFreeList.size(); i++) {
			FreeSprite small = myFreeList.get(i);
			CGRect rectSmall = small.getFree().getBoundingBox();
			if (CGRect.intersects(rectSmall, rectGirl)) {
			 soundPool.play(musicId.get(4), 1, 1, 0, 0, 1);
			 myFreeList.remove(small);
				small.getFree().removeSelf();
				girlLife -= 2;
				disappearGirl(girlLife);
				AddHeart(girlLife);			
			}
		}
	}
	
	public void disappearGirl(int girlLife){
		if (girlLife < 1) {
			// 弹出分数对话框，结束本次游戏
			
			StopSchedule();																	
			girlOver.setVisible(true);		
			fishOver.setVisible(true);
			
			//显示出来再刷新
			this.schedule("ShowGirlOverScence",0.5f);
			this.schedule("ShowFishOverScence",1f);
			//把分数放到sharedPrefence配置文件中
			RecordScore(score);	
			ShowDialog();
		}
	}
	
	//把分数放到sharedPrefence配置文件中
	public void RecordScore(int score){

		/**
		 * 如果用户不是第一次使用则直接调转到显示界面,否则调转到引导界面
		 */
		String scoreArray = sp_wel.getString("scoreArray", "0;0;0;0;0");
		int count = sp_wel.getInt("count", 0);
		if (scoreArray!=null) {
			String[] array = scoreArray.split(";");
			for(int i=0;i<5;i++){
				sp_score[i] = Integer.parseInt(array[i]);
			}
			if(count!=0){
				count += score;
			}
			if(count>sp_score[4]){
				sp_score[4] = count;
			}
			
			for(int i=0;i<4;i++){		
				for(int j=i+1;j<5;j++){
					if(sp_score[i]<sp_score[j]){
						int temp = sp_score[i];
						sp_score[i] = sp_score[j];
						sp_score[j] = temp;
					}				
				}						
			}
		}
		
		// 存入数据并提交	
		String s = sp_score[0]+";"+sp_score[1]+";"+sp_score[2]+";"+sp_score[3]+";"+sp_score[4];
		sp_wel.edit().putString("scoreArray", s).commit();
		sp_wel.edit().putInt("count", 0).commit();
		
	}
	
	//结束之后显示分数的对话框
	public void ShowDialog(){
		CCDirector.sharedDirector().getActivity().runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				 soundPool.play(musicId.get(3), 1, 1, 0, 0, 1);
				// TODO Auto-generated method stub
				LayoutInflater inflater = LayoutInflater.from(CCDirector.sharedDirector().getActivity());
				View v =inflater.inflate(R.layout.score_dialog, null);
				myDialog = new Dialog(CCDirector.sharedDirector().getActivity(),R.style.Dialog_Style);
			myDialog.setCancelable(false);//按返回键是否取消
				myDialog.setCanceledOnTouchOutside(false);//按对话框外面，是否取消
				myDialog.setContentView(v);
				//设置对话框位置
				Window dialogWindow = myDialog.getWindow();
				WindowManager.LayoutParams lp = dialogWindow.getAttributes();
				dialogWindow.setGravity(Gravity.RIGHT | Gravity.TOP);
				lp.x = 65; // 新位置X坐标
				lp.y = 400;
		        dialogWindow.setAttributes(lp);
				myDialog.show();
				
				TextView tv_score = (TextView) v.findViewById(R.id.tv_score);
				tv_score.setText(score+"");   //不写字符串，它会按里面的int  id查找
				
				ImageView iv_back = (ImageView) v.findViewById(R.id.iv_back);
				iv_back.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						 soundPool.play(musicId.get(1), 1, 1, 0, 0, 1);
						IntentToBack(MenuActivity.class);					
						myDialog.dismiss();
					}
				});
				
				
			}
		});
	}
	
	private void IntentToBack(Class<MenuActivity> name){
		Intent intent = new Intent(sceneActivity,name);
		sceneActivity.startActivity(intent);
		sceneActivity.finish();
	}
	
	//显示结束后的场景动画
	public void ShowGirlOverScence(float t){
		overInt++;
		//转眼睛
		if(overInt>3){
			overInt =0;
		}
		if (girlOver != null) {
			girlOver.removeSelf();
		}
		switch (overInt){
		case 0:
			girlOver = CCSprite.sprite(girlOverPath);
			break;
		case 1:
			girlOver = CCSprite.sprite(girlOverPath2);
			break;
		case 2:
			girlOver = CCSprite.sprite(girlOverPath3);
			break;
		case 3:
			girlOver = CCSprite.sprite(girlOverPath4);
			break;
		}
		girlOver.setPosition(3*WinSize.width/4, WinSize.height/5);
		this.addChild(girlOver);
		
		
	}
	
	public void ShowFishOverScence(float t){
		fishOverInt++;
		//转眼睛
		if(fishOverInt>1){
			fishOverInt =0;
		}
		if (fishOver != null) {
			fishOver.removeSelf();
		}
		switch (fishOverInt){
		case 0:
			fishOver = CCSprite.sprite(fishOverPath);
			break;
		case 1:
			fishOver = CCSprite.sprite(fishOverPath2);
			break;
		}
		fishOver.setPosition(WinSize.width/3, 7*WinSize.height/10);
		this.addChild(fishOver);
	}

	// 改变分数
	public void ChangeScore(int i) {
		score += i;
		ShowScore(score);
		
		if(score>overScore){
			StopSchedule();
			RecordScore(score);	
			 soundPool.play(musicId.get(5), 1, 1, 0, 0, 1);
			//当分数大于2000分时，先填出一个对话框，恭喜通关
		ShowLastDialog();
		}
		
	}
	
	//显示进入第二关的对话框
	public void ShowLastDialog(){
		sceneActivity.runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				soundPool.play(musicId.get(5), 1, 1, 0, 0, 1);
				View view = sceneActivity.getLayoutInflater().inflate(R.layout.last_dialog, null);
				lastDialog = new Dialog (sceneActivity, R.style.Dialog_Style);
				lastDialog.setCanceledOnTouchOutside(false);
				lastDialog.setContentView(view);
				lastDialog.show();
				
				ImageView iv_back = (ImageView) view.findViewById(R.id.iv_back);
				iv_back.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						soundPool.play(musicId.get(1), 1, 1, 0, 0, 1);
						//IntentToBack(SecondActivity.class);	
						IntentToBack(MenuActivity.class);				
						lastDialog.dismiss();
					}
				});
			}
		});
	}
	
	//进入第二关游戏场景
	public void GoSeconed(){
	//	Intent intent = new Intent(sceneActivity,SecondActivity.class);
		
	}

	// 显示分数
	public void ShowScore(int score) {
		// 分数标签存在，就移除；重新创建，相当于刷新
		if (scoreLabel != null) {
			scoreLabel.removeSelf();
		}
		scoreLabel = CCLabel.makeLabel("Score:", scorePath, 40);
		scoreLabel.setString("Score:" + score);
		scoreLabel.setColor(ccColor3B.ccBLACK);
		// 标签放置的位置，距离左下角50dip
		scoreLabel.setPosition(play.getContentSize().width + scoreLabel.getContentSize().width / 2 + 20,
				play.getContentSize().height / 2 + 15);
		this.addChild(scoreLabel);
	}

	// 添加爱心消失动画
	public void AddSpriteAnimal(CGPoint pos, String path, float cutX, float cutY, int num,float disSpeed) {
		// 连续动画
		CCSpriteSheet boomSheet = CCSpriteSheet.spriteSheet(path);
		this.addChild(boomSheet);

		// 分割图片
		CCSprite sprite = CCSprite.sprite(boomSheet.getTexture(), CGRect.make(0, 0, cutX, cutY));
		boomSheet.addChild(sprite);
		sprite.setPosition(pos.x, pos.y);

		int frameCount = 0;
		ArrayList<CCSpriteFrame> boomAnimFrames = new ArrayList<CCSpriteFrame>();

		//
		for (int y = 0; y < 1; y++) {
			for (int x = 0; x < num; x++) {
				CCSpriteFrame frame = CCSpriteFrame.frame(boomSheet.getTexture(),
						CGRect.make(x * cutX, y * cutY, cutX, cutY), CGPoint.ccp(0, 0)); // cpp(0,0)是偏移量
				boomAnimFrames.add(frame);
				frameCount++;
				if (frameCount == num) {
					break;
				}
			}
		}

		// 创建一个CCAnimation对象，并且指定动画播放的速度。我们使用disSpeed来指定每个动画帧之间的时间间隔
		CCAnimation boomAnimation = CCAnimation.animation("",disSpeed, boomAnimFrames);
		CCAnimate boomAction = CCAnimate.action(boomAnimation);
		CCCallFuncN actionAnimateDone = CCCallFuncN.action(this, "SpriteAnimationFinished");
		CCSequence actions = CCSequence.actions(boomAction, actionAnimateDone);

		sprite.runAction(actions);
	}

	// 爱心消失动画
	public void SpriteAnimationFinished(Object obj) {
		CCSprite sprite = (CCSprite) obj;
		sprite.removeSelf();
	}

	// 添加小女孩发射的气泡
	public void AddQipao(float t) {
		CCSprite qipao = CCSprite.sprite(qipaoPath);
		float localX = 0, localY = 0;

		if (touchPosition == null) {
			localX = WinSize.width / 2;
			localY = girl.getContentSize().height  + qipao.getContentSize().height / 2;
		} else {
			localX = touchPosition.x;
			localY = touchPosition.y + girl.getContentSize().height + qipao.getContentSize().height / 2;
		}
		qipao.setPosition(localX, localY);
		addChild(qipao);
		qipaos.add(qipao);

		// 在规定时间内动作，在时间内移动到目的地
		CCFiniteTimeAction fs_timeAction = CCMoveBy.action(qipaoSpeed,
				CGPoint.ccp(0, qipao.getContentSize().height / 2 + WinSize.height));// CCMoveBy是向量，相当于从当前点开始加上你的点的大小就是移动过后的位置
		CCCallFuncN qipaoOver = null;
		qipaoOver = CCCallFuncN.action(this, "qipaoOver");

		CCSequence fs_actions = CCSequence.actions(fs_timeAction, qipaoOver);// 循环运行
		qipao.runAction(fs_actions);
	}

	// 当子弹移除屏幕时就清除
	public void qipaoOver(Object qipao) {
		CCSprite qipaoOver = (CCSprite) qipao;
		qipaos.remove(qipaoOver);
		qipaoOver.removeSelf();
	}

	public void GameGirl() {
		AddGirl();
	}

	// 添加女孩
	public void AddGirl() {
		girl = CCSprite.sprite(girlBeforePath);
		girl.setPosition(WinSize.width / 2, girl.getContentSize().height / 2);
		addChild(girl);
		
		girlOver =CCSprite.sprite(girlOverPath);
		float x= WinSize.width / 4;
		float y = WinSize.height / 5;
		girlOver.setVisible(false);
		girlOver.setPosition(3*x,y);
		addChild(girlOver);
		
		fishOver =CCSprite.sprite(fishOverPath);
		fishOver.setVisible(false);
		fishOver.setPosition(WinSize.width/3, 7*WinSize.height/10);
		addChild(fishOver);

				
	}

	// 改变女孩的状态
	public void AddGirl(float t) {
		girlChange = -girlChange;
		if (girl != null) {
			girl.removeSelf();
		}
		if (girlChange == 1) {
			girl = CCSprite.sprite(girlBeforePath);
		} else {
			girl = CCSprite.sprite(girlAfterPath);
		}
		// 触屏控制，转换坐标系
		if (touchPosition == null) {
			touchPosition = CCDirector.sharedDirector()
					.convertToGL(CGPoint.ccp(WinSize.width / 2, WinSize.height - girl.getContentSize().height / 2));
		}
		girl.setPosition(touchPosition);
		addChild(girl);

	}

	// 根据手指触屏的移动
	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		// TODO Auto-generated method stub
		// 获取点击位置
		CGPoint location = CCDirector.sharedDirector().convertToGL(CGPoint.ccp(event.getX(), event.getY()));
		// 小女孩的碰撞盒，暂时没有考虑暂停，相互碰撞的情况
		CGRect rectGirl = girl.getBoundingBox();
		if (CGRect.containsPoint(rectGirl, location)) {
			canMove = true;
		} else {
			canMove = false;
		}

		// 判断是否点击到暂停按钮
		CGRect rectPlay = play.getBoundingBox();
		CGRect rectPause = pause.getBoundingBox();
		if (CGRect.containsPoint(rectPause, location) || CGRect.containsPoint(rectPlay, location)) {
			soundPool.play(musicId.get(1), 1, 1, 0, 0, 1);
			if (isPause) { // 暂停
				play.setVisible(true);
				pause.setVisible(false);
				music.start();
				CCDirector.sharedDirector().resume();
			} else {// 不暂停
				play.setVisible(false);
				pause.setVisible(true);
				music.pause();
				CCDirector.sharedDirector().pause();
			}
			isPause = !isPause;
		}

		// 一直没搞清楚过，什么时候父类的调用要放前面，什么时候不用？？？？
		return super.ccTouchesBegan(event);
	}

	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.ccTouchesEnded(event);
	}

	@Override
	public boolean ccTouchesMoved(MotionEvent event) {
		// TODO Auto-generated method stub

		// 如果可以移动，就把小女孩的位置移动
		if (canMove) {
			touchPosition = CCDirector.sharedDirector().convertToGL(CGPoint.ccp(event.getX(), event.getY()));
			girl.setPosition(touchPosition);
		}
		return super.ccTouchesMoved(event);
	}

	public void GameSmallLove(float f) {
		AddSmallLove();
	}
	


	// 添加敌方
	public void AddSmallLove() {	
		GameSprite Love = new GameSprite();
			 Love.setClicked_Or(false);
			 Love.setLife(smallLoveLife);
			 Love.setMaxLife(smallLoveLife);
			 Love.setCCSprite(smallLovePath);
				SmallLoveDown(Love, "SmallLove",smallLoveDownSpeed);
		 

	}

	// 添加敌方的爱心降落
	private void SmallLoveDown(GameSprite foe, String  name,int speed) {
		Random rand = new Random();
		int minX = (int) (foe.getCCSprite().getContentSize().width / 2.0f);
		int maxX = (int) (WinSize.width - foe.getCCSprite().getContentSize().width / 2.0f);
		int rangeX = maxX - minX; // 爱心移动的范围
		int actualX = rand.nextInt(rangeX) + minX;
		int minDuration = speed - 4; // 爱心下降的速度
		int maxDuration = speed;

		int rangeDuration = maxDuration - minDuration;// 爱心下降的速度范围
		int actualDuration = rand.nextInt(rangeDuration) + minDuration;
		if (actualDuration < 0) {
			actualDuration = rand.nextInt(2) + 2;
		}

		// 设置爱心开始的位置和下降速度（在变化范围中随机取一个加上最小值）
		foe.setInitX(actualX);
		foe.setInitDuration(actualDuration);
		foe.setInitY(foe.getCCSprite().getContentSize().height / 2.0f + WinSize.height);

		foe.getCCSprite().setPosition(actualX, foe.getCCSprite().getContentSize().height / 2.0f + WinSize.height);
		addChild(foe.getCCSprite());

		foe.getCCSprite().setTag(0);//?????
		
		CCFiniteTimeAction fs_timeAction = CCMoveTo.action(actualDuration,
				CGPoint.ccp(actualX, -(foe.getCCSprite().getContentSize().height / 2)));// 时间内移动
		CCCallFuncN fs_Over = null;
		
		if("SmallLove".equals(name)){
			smallLoves.add(foe);
			fs_Over = CCCallFuncN.action(this, "SmallLoveOverF");
		}	
		CCSequence fs_actions = CCSequence.actions(fs_timeAction, fs_Over);
		foe.getCCSprite().runAction(fs_actions);
	}
	
	//爱心鲸鱼在屏幕外面就移除
	public void SmallLoveOverF(Object smallLove) {
		CCSprite smallLoveOver = (CCSprite) smallLove;
		smallLoveOver.removeSelf();
		for (int i = 0; i <smallLoves.size(); i++) {
			GameSprite foe =smallLoves.get(i);
			if (foe.getCCSprite() == smallLoveOver) {
				smallLoves.remove(i);
				break;
			}
		}
	}

	/**
	 * 停止持续的方法
	 */
	private void StopSchedule() {
		girl.removeSelf();
		this.unschedule("GameSmallLove");
		this.unschedule("AddGirl");
		this.unschedule("AddQipao");
		this.unschedule("AddFree");
	}


}
