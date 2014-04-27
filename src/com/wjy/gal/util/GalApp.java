package com.wjy.gal.util;

import android.app.Application;
import android.graphics.Bitmap;

public class GalApp extends Application {

	public int screenWidth;// 屏幕宽度
	public int screenHeight;// 屏幕高度

	public String currentBranchFile = "branch/player.1.1.txt";// 当前文本
	public String lastBranchFile = "branch/player.1.1.txt";// 上一文本
	public int currentStepCount = 0;// 当前步数
	public int lastBranchCount = 0;// 上一分支的步数
	public Bitmap bg;// 当前背景
	public Bitmap actor;// 当前前景
	public String text;// 当前文本
	public Bitmap lastBg;

	public String currentName = "-";
	public String preName = "-";

	public String bgFile = "bg/black.bmp";
	public String actorFile = "actor/none_actor.png";
	public String musicFile = "music/nor.mid";
	public boolean isDrawSelect = false;
	public boolean isSelecting = false;

	/**
	 * 构造方法
	 */
	public GalApp() {

	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	public String getCurrentBranchFile() {
		return currentBranchFile;
	}

	public void setCurrentBranchFile(String currentBranchFile) {
		this.currentBranchFile = currentBranchFile;
	}

	public String getLastBranchFile() {
		return lastBranchFile;
	}

	public void setLastBranchFile(String lastBranchFile) {
		this.lastBranchFile = lastBranchFile;
	}

	public int getCurrentStepCount() {
		return currentStepCount;
	}

	public void setCurrentStepCount(int currentStepCount) {
		this.currentStepCount = currentStepCount;
	}

	public int getLastBranchCount() {
		return lastBranchCount;
	}

	public void setLastBranchCount(int lastBranchCount) {
		this.lastBranchCount = lastBranchCount;
	}

	public Bitmap getBg() {
		return bg;
	}

	public void setBg(Bitmap bg) {
		this.bg = bg;
	}

	public Bitmap getActor() {
		return actor;
	}

	public void setActor(Bitmap actor) {
		this.actor = actor;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getBgFile() {
		return bgFile;
	}

	public void setBgFile(String bgFile) {
		this.bgFile = bgFile;
	}

	public String getActorFile() {
		return actorFile;
	}

	public void setActorFile(String actorFile) {
		this.actorFile = actorFile;
	}

	public String getMusicFile() {
		return musicFile;
	}

	public void setMusicFile(String musicFile) {
		this.musicFile = musicFile;
	}

	public boolean isDrawSelect() {
		return isDrawSelect;
	}

	public void setDrawSelect(boolean isDrawSelect) {
		this.isDrawSelect = isDrawSelect;
	}

	public boolean isSelecting() {
		return isSelecting;
	}

	public void setSelecting(boolean isSelecting) {
		this.isSelecting = isSelecting;
	}

	public Bitmap getLastBg() {
		return lastBg;
	}

	public void setLastBg(Bitmap lastBg) {
		this.lastBg = lastBg;
	}

	public String getCurrentName() {
		return currentName;
	}

	public void setCurrentName(String currentName) {
		this.currentName = currentName;
	}

	public String getPreName() {
		return preName;
	}

	public void setPreName(String preName) {
		this.preName = preName;
	}

}
