package com.wjy.galeng;

import net.tsz.afinal.FinalActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.wjy.gal.util.GalApp;
import com.wjy.gal.util.GalUtil;
import com.wjy.galView.GameView;
import com.wjy.galView.MainMenuView;

public class MainActivity extends FinalActivity {

	private int screenWidth = 10;// 屏幕宽度
	private int screenHeight = 10;// 屏幕高度

	public boolean isChangeView = false;

	public boolean isMainMenu = true;// 是否绘制主屏幕
	public boolean isGameView = false;// 是否绘制游戏界面
	public boolean isGameMenu = false;
	public boolean isGameStart = false;
	public boolean isGmaeExit = false;
	public boolean isGameInterrupt = false;

	GalApp app;

	private MainMenuView mainMenuView;
	private GameView gameView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();
	}

	public void initView() {

		// 设置屏幕不关闭
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		// 公用信息
		app = (GalApp) getApplication();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		this.screenWidth = dm.widthPixels;
		this.screenHeight = dm.heightPixels;
		app.setScreenWidth(screenWidth);
		app.setScreenHeight(screenHeight);

		this.mainMenuView = new MainMenuView(this);
		this.gameView = new GameView(this);
		setContentView(mainMenuView);
		new Thread(new MainMenuThread()).start();

	}

	/**
	 * Handler
	 */
	@SuppressLint("HandlerLeak")
	Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case GalUtil.GAME_MENU:
				if (isChangeView == true) {
					setContentView(mainMenuView);
					isChangeView = false;
				}
				mainMenuView.invalidate();
				break;
			case GalUtil.GAME_VIEW:
				if (isChangeView == true) {
					setContentView(gameView);
					isChangeView = false;
				}
				gameView.invalidate();
				break;
			case GalUtil.GAME_EXIT:
				System.exit(0);
				break;
			}
		}

	};

	/**
	 * 主菜单线程
	 */
	class MainMenuThread implements Runnable {

		@Override
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				Message message = new Message();
				if (isMainMenu == true) {// 游戏菜单
					message.what = GalUtil.GAME_MENU;
				}
				if (isGameView == true) {// 游戏界面
					message.what = GalUtil.GAME_VIEW;
				}
				if (isGmaeExit == true) {
					message.what = GalUtil.GAME_EXIT;
				}
				MainActivity.this.myHandler.sendMessage(message);
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					Thread.currentThread().interrupt();
				}
			}

		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

}
