package com.wang.girl;

import org.cocos2d.nodes.CCSprite;

public class GameSprite {
	public int Life=0, MaxLife=0;
	public float InitX = 0,InitY = 0,InitDuration = 0;
	public CCSprite ccSpritel;
	//是否可以点击
	public boolean Clicked_Or = false;
	
	public int getLife() {
		return Life;
	}
	public void setLife(int life) {
		Life = life;
	}
	public int getMaxLife() {
		return MaxLife;
	}
	public void setMaxLife(int maxLife) {
		MaxLife = maxLife;
	}
	public float getInitX() {
		return InitX;
	}
	public void setInitX(float initX) {
		InitX = initX;
	}
	public float getInitY() {
		return InitY;
	}
	public void setInitY(float initY) {
		InitY = initY;
	}
	public float getInitDuration() {
		return InitDuration;
	}
	public void setInitDuration(float initDuration) {
		InitDuration = initDuration;
	}
	public CCSprite getCCSprite() {
		return ccSpritel;
	}
	public void setCCSprite(String path) {
		//初始化一个精灵对象,sprite()方法会默认到assets目录下去找名为path的文件
		this.ccSpritel = CCSprite.sprite(path);
	}
	public boolean isClicked_Or() {
		return Clicked_Or;
	}
	public void setClicked_Or(boolean clicked_Or) {
		Clicked_Or = clicked_Or;
	}
	

}
