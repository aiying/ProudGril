package com.wang.girl;

import org.cocos2d.nodes.CCSprite;

import android.graphics.Bitmap;

public class FreeSprite {
	public int freeLift;
	private CCSprite free;
	public int getFreeLift() {
		return freeLift;
	}
	public void setFreeLift(int freeLift) {
		this.freeLift = freeLift;
	}
	public CCSprite getFree() {
		return free;
	}
	@SuppressWarnings("deprecation")
	public void setFree(Bitmap bmp) {
		this.free = CCSprite.sprite(bmp);
	}
	
}
