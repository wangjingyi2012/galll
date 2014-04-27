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

	private int screenWidth = 10;// ��Ļ���
	private int screenHeight = 10;// ��Ļ�߶�

	public boolean isChangeView = false;

	public boolean isMainMenu = true;// �Ƿ��������Ļ
	public boolean isGameView = false;// �Ƿ������Ϸ����
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

		// ������Ļ���ر�
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		// ������Ϣ
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
	 * ���˵��߳�
	 */
	class MainMenuThread implements Runnable {

		@Override
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				Message message = new Message();
				if (isMainMenu == true) {// ��Ϸ�˵�
					message.what = GalUtil.GAME_MENU;
				}
				if (isGameView == true) {// ��Ϸ����
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
