package com.wjy.galView;

import java.io.File;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.wjy.gal.util.GalApp;
import com.wjy.gal.util.GalReader;
import com.wjy.gal.util.GalUtil;
import com.wjy.galeng.MainActivity;
import com.wjy.galeng.R;

/**
 * @author 王
 * 
 */
@SuppressLint({ "ViewConstructor", "SimpleDateFormat" })
public class GameView extends SurfaceView implements Runnable,
		SurfaceHolder.Callback {

	public boolean mbLoop = false;// 控制循环

	public MainActivity context;
	public GalReader galReader;
	public String[] texts;
	public int stepCount = 1;
	public int stepTotal = 0;

	SurfaceHolder mSurfaceHolder = null;
	public Canvas canvas;

	// 存储相关数据
	public GalApp app;
	public int screenWidth = 10;
	public int screenHeight = 10;

	// 屏幕每等分及份数
	public int screenEach = 20;// 默认将屏幕横竖各分为20份
	public int screenWidthEach = 10;
	public int screenHeightEach = 10;

	public String currentFile = "branch/player.1.1.txt";

	private Paint mPaint = null;
	public Bitmap bgColor;
	public Bitmap confirm1;
	private int textSize = 22;
	String time = "201X";

	// 背景，前景，姓名框，文本框，文本及其相关设置
	public Bitmap bg;
	public Bitmap actor;
	public Bitmap textBg;
	public Bitmap nameBg;
	public String name = "";
	public String text = "~hello gal World~";
	public String bgFile;
	public String actorFile;
	public String musicFile;
	public Bitmap lastBg;

	public boolean isTextBg = false;// 是否绘制文本框
	public boolean isNameBg = false;// 是否绘制姓名框

	public int actorX = 0;
	public int actorY = 0;
	public int actorWidth = 100;
	public int actorHeight = 100;
	public int textBgX = 0;
	public int textBgY = 0;
	public int textBgWidth = 100;
	public int textBgHeight = 10;
	public int textX = 0;
	public int textY = 0;
	public int textDistanceX = 0;
	public int textDistanceY = 0;
	public int nameBgX = 0;
	public int nameBgY = 0;
	public int nameBgWidth = 100;
	public int nameBgHeight = 100;

	Paint paintBg;

	// 字体
	Typeface faceName;
	Typeface faceText;

	// 游戏中菜单
	public boolean isDrawGameMenu = false;
	public Rect gameMenuTotal;
	public int gameMenuTotalWidth = 0;
	public int gameMenuTotalHeight = 0;
	public Bitmap gameMenuBg;
	public int gameMenuWidth = 0;
	public int gameMenuHeight = 0;
	public Rect[] gameMenu;
	public String[] gameMenuText = GalUtil.gameMenuText;
	public int gameMenuTextDistanceX = 10;
	public int gameMenuTextDistanceY = 40;
	public String[] gameMenuTextTips = GalUtil.gameMenuTextTips;

	public boolean isSaveGame = false;// 是否保存游戏

	public boolean isReturnLastBranch = false;// 返回上一分支
	public boolean isDrawExit = false;// 返回主界面

	// 没有选项的场景
	public Rect gameNextRect;

	// 有选项的场景
	public boolean isDrawSelect = false;
	public boolean isSelecting = false;
	public Rect[] select;
	public Bitmap[] selectBg;
	public String[] selectText;
	public String[] selectFile;
	public int selectX = 0;
	public int selectY = 0;
	public int selectWidth = 100;
	public int selectHeight = 100;
	public int selectCount = 0;
	public int selectDistanceX = 0;
	public int selectDistanceY = 0;

	// 确认对话框
	public boolean isDrawConfirm = false;
	public String confirmTip = "你确定要这样吗";
	public String confirmOk = "你确定要这样吗";
	public String confirmCanel = "你确定要这样吗";
	public Bitmap confirmBg;
	public Rect[] confirmRect;
	public int confirmDistance = 0;

	// 提示信息
	public String operationTip = "";
	public boolean isTip = false;

	// 读取游戏
	public boolean isLoad = false;

	// 特效笔刷
	public Paint effectPaint;
	public int effectAlpha = 255;
	public boolean isEffect = false;
	/** 特效播放参数 **/
	private int s_effRange = 1;
	/** 特效播放参数 **/
	private int s_effectRangeTarget;
	/** 特效类型 **/
	private int s_effectType = 0;

	final static byte RANDOM_EFFECT_TYPE_SQUARE = 0;
	final static byte RANDOM_EFFECT_TYPE_SHADOW = 1;
	final static byte RANDOM_EFFECT_TYPE_CROSS = 2;
	final static byte RANDOM_EFFECT_TYPE_SECTOR = 3;

	int EFFECT_RANGE_PERFRAME_0 = 8;
	final static int EFFECT_RANGE_PERFRAME_1 = -24;

	final static int RANDOM_TYPE_1_RANGE1 = 15;
	final static int RANDOM_TYPE_1_RANGE2 = 10;
	final static int RANDOM_TYPE_1_RANGE3 = 5;
	final static int RANDOM_TYPE_1_SPACE1 = 8;
	final static int RANDOM_TYPE_1_SPACE2 = (RANDOM_TYPE_1_SPACE1 << 1) + 13;
	final static int RANDOM_TYPE_1_SPACE3 = RANDOM_TYPE_1_SPACE2 + 14;
	final static int RANDOM_TYPE_2_RANGE = 32;
	final static int RANDOM_TYPE_0_RANGE = 32;
	final static int RANDOM_TYPE_3_RANGE = 100;

	public GameView(MainActivity context) {
		super(context);
		this.context = context;
		mSurfaceHolder = this.getHolder();
		mSurfaceHolder.addCallback(this);
		this.setFocusable(true);

		app = (GalApp) context.getApplicationContext();
		this.screenWidth = app.getScreenWidth();
		this.screenHeight = app.getScreenHeight();
		bgFile = app.getBgFile();
		actorFile = app.getActorFile();
		musicFile = app.getMusicFile();

		galReader = new GalReader(context);
		texts = galReader.readTextSource(currentFile);
		stepTotal = texts.length;

		mPaint = new Paint();
		paintBg = new Paint();
		effectPaint = new Paint();
		effectPaint.setAntiAlias(true);
		paintBg.setAntiAlias(true);
		paintBg.setAlpha(0);
		initView();
		mbLoop = true;

	}

	/**
	 * 初始化资源
	 */
	public void initView() {

		screenWidthEach = screenWidth / screenEach;
		screenHeightEach = screenHeight / screenEach;

		bgColor = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.bg_color)).getBitmap();
		bgColor = change2Bg(bgColor);
		confirm1 = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.save_bg)).getBitmap();
		confirm1 = change2Bg(confirm1);

		bg = ((BitmapDrawable) getResources()
				.getDrawable(R.drawable.default_bg)).getBitmap();
		actor = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.default_actor)).getBitmap();
		textBg = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.text_bg)).getBitmap();
		lastBg = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.text_bg)).getBitmap();
		lastBg = this.change2Bg(lastBg);
		text = texts[1];
		app.setBg(bg);
		app.setActor(actor);
		app.setText(text);
		app.setBgFile(bgFile);
		app.setActorFile(actorFile);
		app.setMusicFile(musicFile);
		app.setLastBg(lastBg);
		// 判断是否是游戏第一个文本，若不是，则记录上一文本
		if (!texts[0].contains("$START$")) {
			app.setLastBranchFile(texts[0].substring(0, texts[0].length() - 1));
		}

		textSize = screenHeight / 16;
		gameMenuTextDistanceY = screenHeight / 8;

		actorWidth = screenWidth / 3;
		actorX = (screenWidth - actorWidth) / 2;
		// actorHeight = screenHeight * 6 / 7;
		// actorY = screenHeight / 7;
		actorHeight = screenHeight;
		actorY = 0;

		textBgWidth = screenWidth * 9 / 10;
		textBgX = (screenWidth - textBgWidth) / 2;
		textBgHeight = screenHeight / 4;
		textBgY = screenHeight * 2 / 3 + 30;

		textDistanceX = screenHeight / 8;
		textDistanceY = screenHeight / 8;
		textX = textBgX + textDistanceX;
		textY = textBgY + textDistanceY;

		textBg = Bitmap.createScaledBitmap(textBg, textBgWidth, textBgHeight,
				false);

		// 姓名框初始化
		nameBgWidth = screenWidthEach * 2;
		nameBgHeight = screenHeightEach;
		nameBg = Bitmap.createScaledBitmap(textBg, nameBgWidth, nameBgHeight,
				false);
		nameBgX = textBgX + screenWidthEach;
		nameBgY = textBgY - nameBgHeight;

		// 第一次进入游戏
		setCurrentView(bg, actor, text, "bg/black.bmp", "actor/none_actor.png",
				app.getMusicFile(), stepCount, "branch/player.1.1.txt");

		// 游戏菜单极其相关设置
		gameMenuTotalWidth = screenWidth;
		gameMenuTotalHeight = screenHeight / 6;
		gameMenuTotal = new Rect(0, 0, gameMenuTotalWidth, gameMenuTotalHeight);
		gameMenuBg = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.exit_bg)).getBitmap();
		gameMenuBg = Bitmap.createScaledBitmap(gameMenuBg, gameMenuTotalWidth,
				gameMenuTotalHeight, false);

		gameMenuWidth = gameMenuTotalWidth / 5;
		gameMenuHeight = gameMenuTotalHeight;
		gameMenu = new Rect[5];
		for (int i = 0; i < 5; i++) {
			gameMenu[i] = new Rect(i * gameMenuWidth, 0, gameMenuWidth
					+ (i * gameMenuWidth), gameMenuHeight);
		}

		// 确认对话框
		confirmDistance = screenHeight / GalUtil.rightMenuDistance;
		confirmBg = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.exit_bg)).getBitmap();
		confirmBg = Bitmap.createScaledBitmap(confirmBg, screenWidth * 2 / 3,
				screenHeight * 3 / 5, false);
		confirmRect = new Rect[2];
		confirmRect[0] = new Rect(screenWidth / 6 + confirmDistance * 3 / 2
				- screenHeight / 10, screenHeight / 7 + confirmDistance * 3
				- screenHeight / 8, screenWidth / 6 + confirmDistance * 3 / 2
				+ screenHeight / 7, screenHeight / 7 + confirmDistance * 3
				+ screenHeight / 16);
		confirmRect[1] = new Rect(screenWidth / 6 + confirmDistance * 4
				- screenHeight / 32, screenHeight / 7 + confirmDistance * 3
				- screenHeight / 8, screenWidth / 6 + confirmDistance * 4
				+ screenHeight * 7 / 2, screenHeight / 7 + confirmDistance * 3
				+ screenHeight / 16);

		// 没有选项的场景设置
		gameNextRect = new Rect(0, screenHeight / 6, screenWidth, screenHeight);

		// 选项区域及其相关设置
		select = new Rect[4];
		selectText = new String[4];
		selectFile = new String[4];
		selectBg = new Bitmap[4];
		selectX = screenWidth / 5;
		selectWidth = screenWidth / 5;
		selectY = screenHeight / 5;
		selectHeight = screenHeight / 6;
		selectDistanceX = screenHeight / 14;
		selectDistanceY = screenHeight / 16;
		for (int i = 0; i < 4; i++) {
			select[i] = new Rect(0, 0, 0, 0);
		}
		selectBg[0] = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.select1)).getBitmap();
		selectBg[1] = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.select2)).getBitmap();
		selectBg[2] = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.select3)).getBitmap();
		selectBg[3] = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.select4)).getBitmap();
		for (int i = 0; i < 4; i++) {
			selectBg[i] = Bitmap.createScaledBitmap(selectBg[i],
					selectWidth * 3 / 2, selectHeight, false);
		}

		// 字体
		faceName = Typeface.createFromAsset(context.getAssets(),
				"fonts/STXINGKA.TTF");// 华文行楷
		//faceText = Typeface.createFromAsset(context.getAssets(),
		// "fonts/FZSTK.TTF");//方正书体
		//		"fonts/SIMYOU.TTF");// 幼圆

		// 特效参数
		EFFECT_RANGE_PERFRAME_0 = screenWidth / 8;// 交错矩形移动速度
		// RANDOM_TYPE_1_SPACE2 = screenWidth / 2;// 水波纹移动速度

	}

	/**
	 * Surface改变大小时触发
	 */
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	/**
	 * Surface创建时触发
	 */
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		new Thread(this).start();
	}

	/**
	 * Surface销毁时触发
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {

		galReader.stopMusic();
		mbLoop = false;
	}

	@Override
	public void run() {
		while (mbLoop) {
			try {
				Thread.sleep(200);
			} catch (Exception e) {
				e.printStackTrace();
			}
			synchronized (mSurfaceHolder) {
				Draw();
			}
		}
	}

	/**
	 * 绘制屏幕
	 */
	public void Draw() {
		canvas = mSurfaceHolder.lockCanvas();// 得到并锁定画布
		if (mSurfaceHolder == null || canvas == null) {
			return;
		}
		// 清屏
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.BLACK);
		canvas.drawColor(Color.BLACK);

		mPaint.setColor(Color.BLACK);
		textSize = screenHeight / 22;
		//mPaint.setTypeface(faceText);
		// mPaint.setS
		mPaint.setTextSize(textSize);

		canvas.drawBitmap(bg, 0, 0, paintBg);// 绘制背景
		canvas.drawBitmap(actor, actorX, actorY, null);// 绘制前景
		canvas.drawBitmap(textBg, textBgX, textBgY, null);// 绘制对话框

		if (isNameBg) {
			Paint namePaint = new Paint();
			namePaint.setAntiAlias(true);
			namePaint.setTypeface(faceName);
			namePaint.setTextSize(screenHeight / 24);
			canvas.drawBitmap(nameBg, nameBgX, nameBgY, null);// 绘制姓名框
			canvas.drawText(name, nameBgX + 10, textBgY - 5, namePaint);
		}

		if (isTextBg) {

		}

		String tempText = text;
		int nsize = screenWidth / 40;
		if (tempText.length() > nsize) {
			canvas.drawText(tempText.substring(0, nsize), textX, textY, mPaint);
			canvas.drawText(tempText.substring(nsize, tempText.length()), textX
					- textSize, textY + textSize, mPaint);
		} else {
			canvas.drawText(tempText, textX, textY, mPaint);
		}

		// 绘制特效
		if (isEffect == true) {
			RenderEffect();
			UpdataEffectRange(EFFECT_RANGE_PERFRAME_0);
		}

		// 游戏中菜单
		if (isDrawGameMenu == true) {
			Paint menuPaint = new Paint();
			menuPaint.setAntiAlias(true);
			menuPaint.setColor(Color.BLACK);
			textSize = screenHeight / 16;
			menuPaint.setTextSize(textSize);
			canvas.drawBitmap(gameMenuBg, 0, 0, null);
			for (int i = 0; i < 5; i++) {
				canvas.drawText(gameMenuText[i], gameMenu[i].left
						+ gameMenuTextDistanceX, gameMenuTextDistanceY,
						menuPaint);
				if (i > 0) {
					canvas.drawLine(i * gameMenuWidth, 0, i * gameMenuWidth,
							gameMenuHeight, menuPaint);
				}

			}
			canvas.drawLine(0, gameMenuHeight, screenWidth, gameMenuHeight,
					menuPaint);
		}

		// 有选项的时候
		if (isDrawSelect == true) {
			Paint selectPaint = new Paint();
			selectPaint.setColor(Color.BLACK);
			textSize = screenHeight / 15;
			selectPaint.setTextSize(textSize);
			selectPaint.setAntiAlias(true);
			for (int i = 0; i < selectCount; i++) {
				canvas.drawBitmap(selectBg[i], select[i].left, select[i].top,
						null);
				canvas.drawText(selectText[i],
						select[i].left + selectDistanceX, select[i].bottom
								- selectDistanceY, selectPaint);
			}
		}

		/** ----------------绘制提示信息---------- **/
		if (isTip == true) {
			Paint timePaint = new Paint();
			timePaint.setColor(Color.BLUE);
			timePaint.setAntiAlias(true);
			timePaint.setTextSize(screenHeight / 16);
			canvas.drawText(operationTip, screenHeight / 16, screenHeight / 14,
					timePaint);
		}

		/** -------------- 绘制确认框--------------- **/
		if (isDrawConfirm == true) {
			if (isSaveGame == true) {// 保存
				canvas.drawBitmap(confirm1, 0, 0, null);
				drawConfirm(canvas, "存储为:" + text, "好的", "不好");
				Paint timePaint = new Paint();
				timePaint.setColor(Color.GREEN);
				timePaint.setAntiAlias(true);
				timePaint.setTextSize(screenHeight / 16);
				canvas.drawText(time, screenHeight / 16, screenHeight / 14,
						timePaint);
			}
			if (isLoad == true) {
				canvas.drawBitmap(confirm1, 0, 0, null);
				drawConfirm(canvas, "人生能读档是一种幸福啊...", "是的", "不了");
				Paint timePaint = new Paint();
				timePaint.setColor(Color.GREEN);
				timePaint.setAntiAlias(true);
				timePaint.setTextSize(screenHeight / 16);
				canvas.drawText(time, screenHeight / 16, screenHeight / 14,
						timePaint);
			}
			if (isReturnLastBranch == true) {// 返回上一分支
				canvas.drawBitmap(bgColor, 0, 0, null);
				drawConfirm(canvas, "确定要重新选择吗(⊙_⊙)?", "对", "不了..");
			}
			if (isDrawExit == true) {// 退出游戏
				canvas.drawBitmap(bgColor, 0, 0, null);
				drawConfirm(canvas, "退出前别忘记保存游戏哦^_^ ", "当然", "又滑了..");
			}
		}

		mSurfaceHolder.unlockCanvasAndPost(canvas);// 绘制完成并解锁画布
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();

		// 点击后取消操作提示
		operationTip = "";
		isTip = false;

		// 如果点了游戏中菜单
		if (isDrawGameMenu == true) {
			if (gameMenu[0].contains(x, y) && isDrawExit == false) {// 存档
				time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
						.format(new Date());
				isSaveGame = true;
				isDrawConfirm = true;
			}

			if (gameMenu[1].contains(x, y) && isDrawExit == false) {// 读档
				isLoad = true;
				isDrawConfirm = true;
			}

			if (gameMenu[2].contains(x, y) && isDrawExit == false) {// 上一分支
				isReturnLastBranch = true;
				isDrawConfirm = true;
			}

			if (gameMenu[3].contains(x, y) && isDrawExit == false) {// 设置
				System.out.println("gameMenu4");
				isDrawGameMenu = false;
			}

			// 返回主界面
			if (gameMenu[4].contains(x, y) && isDrawGameMenu == true) {// 主界面
				isDrawConfirm = true;
				isDrawExit = true;
			}
		}

		// 点击游戏菜单区域，显示菜单
		if (gameMenuTotal.contains(x, y) && isDrawGameMenu == false
				&& isDrawConfirm == false) {
			isDrawGameMenu = true;
		}

		/** --------------End 游戏菜单-------------------- **/

		// 没选项时，点击游戏菜单区域外，隐藏菜单,同时下一步
		if (gameNextRect.contains(x, y) && isDrawSelect == false
				&& isDrawExit == false && isDrawConfirm == false
				&& isSelecting == false) {
			isDrawGameMenu = false;
			isDrawSelect = false;
			stepCount = stepCount + 1;
			if (stepCount < stepTotal) {
				String tempFile = texts[stepCount];

				if (tempFile.contains("$background$")) {// 如果是背景
					lastBg = bg;
					app.setLastBg(bg);
					stepCount = stepCount + 1;
					String effectCategory = tempFile;
					tempFile = texts[stepCount].substring(0,
							texts[stepCount].length() - 1);
					app.setBgFile(tempFile);
					bg = galReader.readImageSource(tempFile);
					bg = change2Bg(bg);
					// new Thread(new BgThreadExpand(bg, paintBg)).start();
					stepCount = stepCount + 1;
					this.text = texts[stepCount];
					s_effRange = 1;
					// 特效
					if (effectCategory.contains("$SQUARE$")) {
						SetCurtainEffect(RANDOM_EFFECT_TYPE_SQUARE);// 百叶窗
					} else if (effectCategory.contains("$CROSS$")) {
						SetCurtainEffect(RANDOM_EFFECT_TYPE_CROSS);// 交错矩形
					} else if (effectCategory.contains("$SECTOR$")) {
						SetCurtainEffect(RANDOM_EFFECT_TYPE_SECTOR);// 扇形
					} else if (effectCategory.contains("$SHADOW$")) {
						SetCurtainEffect(RANDOM_EFFECT_TYPE_SHADOW);// 水波纹
					} else {
						new Thread(new BgThread(paintBg)).start();
					}
					//

				} else if (tempFile.contains("$actor$")) {// 如果是前景1
					stepCount = stepCount + 1;
					tempFile = texts[stepCount].substring(0,
							texts[stepCount].length() - 1);
					app.setActorFile(tempFile);
					actor = galReader.readImageSource(tempFile);
					actor = change2Actor(actor);
					stepCount = stepCount + 1;
					this.text = texts[stepCount];
				} else if (tempFile.contains("$name$")) {// 如果是姓名
					isNameBg = true;
					app.setPreName(name);
					stepCount = stepCount + 1;
					name = texts[stepCount];
					app.setCurrentName(name);
					stepCount = stepCount + 1;
					this.text = texts[stepCount];
				} else if (tempFile.contains("$nameEnd$")) {// 如果是姓名结束
					isNameBg = false;
					app.setPreName(name);
					name = "-";
					app.setCurrentName(name);
					stepCount = stepCount + 1;
					this.text = texts[stepCount];
				} else if (tempFile.contains("$music$")) {// 如果是音乐
					galReader.stopMusic();
					stepCount = stepCount + 1;
					tempFile = texts[stepCount].substring(0,
							texts[stepCount].length() - 1);
					app.setMusicFile(tempFile);
					galReader.readMusicSource(tempFile);
					stepCount = stepCount + 1;
					this.text = texts[stepCount];
				} else if (tempFile.contains("$soundEffect$")) {// 如果是音效
					galReader.stopSound();
					stepCount = stepCount + 1;
					tempFile = texts[stepCount].substring(0,
							texts[stepCount].length() - 1);
					galReader.readSoundSource(tempFile);
					stepCount = stepCount + 1;
					this.text = texts[stepCount];

				} else if (tempFile.contains("$branch$")) {// 如果是分支
					isSelecting = true;
					selectCount = (stepTotal - stepCount) / 2 - 1;
					isDrawSelect = true;

					app.setLastBranchCount(stepCount);
					app.setBg(bg);
					app.setActor(actor);
					app.setText(text);
					lastBg = bg;
					app.setLastBg(lastBg);

					if (selectCount == 1) {
						select[0] = new Rect(selectX, selectY, selectX
								+ selectWidth, selectY + selectHeight);
						selectText[0] = texts[stepCount + 1];
						selectFile[0] = texts[stepCount + 2].substring(0,
								texts[stepCount + 2].length() - 1);
					} else if (selectCount == 2) {
						select[0] = new Rect(selectX, selectY, selectX
								+ selectWidth, selectY + selectHeight);
						select[1] = new Rect(selectX * 3, selectY, selectX * 4,
								selectY + selectHeight);
						selectText[0] = texts[stepCount + 1];
						selectText[1] = texts[stepCount + 3];
						selectFile[0] = texts[stepCount + 2].substring(0,
								texts[stepCount + 2].length() - 1);
						selectFile[1] = texts[stepCount + 4].substring(0,
								texts[stepCount + 4].length() - 1);
					} else if (selectCount == 3) {
						select[0] = new Rect(selectX, selectY, selectX
								+ selectWidth, selectY + selectHeight);
						select[1] = new Rect(selectX * 3, selectY, selectX * 4,
								selectY + selectHeight);
						select[2] = new Rect(selectX, selectY * 3, selectX
								+ selectWidth, selectY * 3 + selectHeight);
						selectText[0] = texts[stepCount + 1];
						selectText[1] = texts[stepCount + 3];
						selectText[2] = texts[stepCount + 5];
						selectFile[0] = texts[stepCount + 2].substring(0,
								texts[stepCount + 2].length() - 1);
						selectFile[1] = texts[stepCount + 4].substring(0,
								texts[stepCount + 4].length() - 1);
						selectFile[2] = texts[stepCount + 6].substring(0,
								texts[stepCount + 6].length() - 1);
					} else if (selectCount == 4) {
						select[0] = new Rect(selectX, selectY, selectX
								+ selectWidth, selectY + selectHeight);
						select[1] = new Rect(selectX * 3, selectY, selectX * 4,
								selectY + selectHeight);
						select[2] = new Rect(selectX, selectY * 3, selectX
								+ selectWidth, selectY * 3 + selectHeight);
						select[3] = new Rect(selectX * 3, selectY * 3,
								selectX * 4, selectY * 3 + selectHeight);
						selectText[0] = texts[stepCount + 1];
						selectText[1] = texts[stepCount + 3];
						selectText[2] = texts[stepCount + 5];
						selectText[3] = texts[stepCount + 7];
						selectFile[0] = texts[stepCount + 2].substring(0,
								texts[stepCount + 2].length() - 1);
						selectFile[1] = texts[stepCount + 4].substring(0,
								texts[stepCount + 4].length() - 1);
						selectFile[2] = texts[stepCount + 6].substring(0,
								texts[stepCount + 6].length() - 1);
						selectFile[3] = texts[stepCount + 8].substring(0,
								texts[stepCount + 8].length() - 1);
					}
				} else {
					this.text = texts[stepCount];
				}
			}

		}

		// 有选项时
		if (isDrawSelect == true && isSelecting == true
				&& isDrawConfirm == false) {
			// 有选项时，点击游戏菜单区域外，隐藏菜单
			if (gameNextRect.contains(x, y) && isDrawConfirm == false) {
				isDrawGameMenu = false;
			}

			if (select[0].contains(x, y)) {
				isDrawSelect = false;
				isGameStartText();
				texts = galReader.readTextSource(selectFile[0]);
				initGalText();
				app.setCurrentBranchFile(selectFile[0]);// 设置当前文本
				isSelecting = false;
			}
			if (select[1].contains(x, y)) {
				isGameStartText();
				isDrawSelect = false;
				texts = galReader.readTextSource(selectFile[1]);
				initGalText();
				app.setCurrentBranchFile(selectFile[1]);// 设置当前文本
				isSelecting = false;
			}
			if (select[2].contains(x, y)) {
				isDrawSelect = false;
				isGameStartText();
				texts = galReader.readTextSource(selectFile[2]);
				initGalText();
				app.setCurrentBranchFile(selectFile[2]);// 设置当前文本
				isSelecting = false;
			}
			if (select[3].contains(x, y)) {
				isDrawSelect = false;
				isGameStartText();
				texts = galReader.readTextSource(selectFile[3]);
				initGalText();
				app.setCurrentBranchFile(selectFile[3]);// 设置当前文本
				isSelecting = false;
			}
		}

		/** --------------如果画出了确认框------------- **/
		if (isDrawConfirm == true) {
			isDrawGameMenu = false;
			if (isSaveGame == true) {// 存档
				if (confirmRect[0].contains(x, y)) {
					String content = text + "\r\n" + time + "\r\n"
							+ app.getBgFile() + "\r\n" + app.getActorFile()
							+ "\r\n" + app.getMusicFile() + "\r\n" + stepCount
							+ "\r\n" + app.getCurrentBranchFile();
					boolean result = saveGame(content);
					if (result == true) {
						isTip = true;
						operationTip = "进度保存成功^_^";
						isSaveGame = false;
						isDrawConfirm = false;
					} else {
						isTip = true;
						operationTip = "进度保存失败 >_<";
						System.out.println("save false!");
					}
				} else if (confirmRect[1].contains(x, y)) {
					isSaveGame = false;
					isDrawConfirm = false;
				}
			}
			if (isLoad == true) {// 读档
				if (confirmRect[0].contains(x, y)) {
					app.setBg(bg);
					app.setActor(actor);
					app.setBgFile(bgFile);
					app.setActorFile(actorFile);
					app.setText(text);
					app.setMusicFile(musicFile);
					app.setCurrentBranchFile(currentFile);
					app.setCurrentStepCount(stepCount);
					mbLoop = false;
					LoadView loadView = new LoadView(context);
					loadView.isNewGame = false;
					context.setContentView(loadView);
				} else if (confirmRect[1].contains(x, y)) {
					isLoad = false;
					isDrawConfirm = false;
				}
			}
			if (isReturnLastBranch == true) {// 返回上一分支
				if (!texts[0].contains("$START$")) {
					if (confirmRect[0].contains(x, y)) {
						setCurrentView(app.getBg(), app.getActor(),
								app.getText(), app.getBgFile(),
								app.getActorFile(), app.getMusicFile(),
								stepCount, app.getCurrentBranchFile());
						bg = app.getLastBg();
						isReturnLastBranch = false;
						isDrawSelect = true;
						isSelecting = true;
						isDrawConfirm = false;
					} else if (confirmRect[1].contains(x, y)) {
						isReturnLastBranch = false;
						isDrawConfirm = false;
					}
				} else {
					isReturnLastBranch = false;
					isDrawConfirm = false;
				}
			}
			if (isDrawExit == true) {// 返回主界面
				if (confirmRect[0].contains(x, y)) {
					mbLoop = false;
					context.setContentView(new MainMenuView(context));
				} else if (confirmRect[1].contains(x, y)) {
					isDrawExit = false;
					isDrawConfirm = false;
				}
			}
		}// end confirm

		return super.onTouchEvent(event);
	}

	/**
	 * 读取分之后进行初始化
	 */
	public void initGalText() {
		stepTotal = texts.length;
		text = texts[1];
		stepCount = 1;
	}

	/**
	 * 判断是否是游戏第一个文本，若不是的话，设置上一文本
	 */
	public void isGameStartText() {
		// 判断是否是游戏第一个文本，若不是，则记录上一文本
		if (!texts[0].contains("$START$")) {
			app.setLastBranchFile(texts[0].substring(0, texts[0].length() - 1));
		}
	}

	/**
	 * 绘制确认对话框
	 */
	public void drawConfirm(Canvas canvas, String tip, String ok, String canel) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		textSize = screenHeight / 14;
		paint.setTextSize(textSize);
		paint.setColor(Color.BLACK);
		if (isSaveGame == true) {
			paint.setColor(Color.WHITE);
		}
		canvas.drawBitmap(confirmBg, screenWidth / 6, screenHeight / 6, null);
		if (tip.length() > 12) {
			tip = tip.substring(0, 12) + "...";
		}
		canvas.drawText(tip, screenWidth / 6 + confirmDistance * 2 / 3,
				screenHeight / 7 + confirmDistance * 2, paint);
		canvas.drawLine(screenWidth / 6 + confirmDistance * 2 / 3 + 70,
				screenHeight / 7 + confirmDistance * 2 + 10, screenWidth / 6
						+ confirmDistance * 2 / 3 + screenHeight * 5 / 6,
				screenHeight / 7 + confirmDistance * 2 + 10, paint);
		textSize = screenHeight / 13;
		paint.setTextSize(textSize);
		canvas.drawText(ok, screenWidth / 6 + confirmDistance * 3 / 2 - 20,
				screenHeight / 7 + confirmDistance * 3, paint);
		canvas.drawText(canel, screenWidth / 6 + confirmDistance * 4,
				screenHeight / 7 + confirmDistance * 3, paint);
	}

	/**
	 * @param bg背景
	 * @param actor前景
	 * @param text文字
	 * @param bgFile背景文件
	 * @param actorFile前景文件名
	 * @param musicFile音乐文件名
	 */
	public void setCurrentView(Bitmap bg, Bitmap actor, String text,
			String bgFile, String actorFile, String musicFile, int stepCount,
			String currentFile) {
		this.bg = galReader.readImageSource(bgFile);
		this.actor = galReader.readImageSource(actorFile);
		this.bg = change2Bg(this.bg);
		this.actor = change2Actor(this.actor);
		this.text = text;
		this.bgFile = bgFile;
		this.actorFile = actorFile;
		this.musicFile = musicFile;
		this.stepCount = stepCount;
		this.currentFile = currentFile;
		this.texts = galReader.readTextSource(currentFile);
		stepTotal = texts.length;
	}

	/**
	 * 将背景图片设置为适合的大小
	 * 
	 * @param bg
	 * @return
	 */
	public Bitmap change2Bg(Bitmap bg) {
		bg = Bitmap.createScaledBitmap(bg, screenWidth, screenHeight, false);
		return bg;
	}

	/**
	 * 将前景图设置为合适的大小
	 * 
	 * @param actor
	 * @return
	 */
	public Bitmap change2Actor(Bitmap actor) {
		actor = Bitmap
				.createScaledBitmap(actor, actorWidth, actorHeight, false);
		return actor;
	}

	/**
	 * 保存游戏
	 * 
	 * @param content
	 * @return
	 */
	public boolean saveGame(String content) {
		boolean result = false;
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {// 如果没有存储卡
			return false;
		} else {
			try {
				File sdCardDir = Environment.getExternalStorageDirectory();
				String savePath = sdCardDir.getAbsolutePath()
						+ "/androidGal/save";
				UUID uuid = UUID.randomUUID();
				String filename = uuid.toString().replace("-", "") + ".txt";
				File path = new File(savePath);
				if (!path.exists()) {
					path.mkdirs();
				}
				File save = new File(path + "/" + filename);
				if (!save.exists()) {
					save.createNewFile();
				}
				RandomAccessFile raf = new RandomAccessFile(save, "rw");
				// 将文件记录指针移动最后
				raf.seek(save.length());
				// 输出文件内容
				raf.write(content.getBytes());
				raf.close();
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return result;
	}

	/**
	 * 绘制背景图渐变-改变透明度
	 * 
	 * @return
	 */
	class BgThread implements Runnable {
		Paint paintBg;
		int deadLine = 0;
		int alpha = 55;
		boolean bgLoop = false;

		public BgThread(Paint paintBg) {
			this.paintBg = paintBg;
			paintBg.setAlpha(alpha);
			bgLoop = true;
		}

		public void setBgLoop(boolean bgLoop) {
			this.bgLoop = bgLoop;
		}

		@Override
		public void run() {
			while (bgLoop == true) {
				try {
					Thread.sleep(30);
					if (alpha < 255) {
						alpha = alpha + 5;
						paintBg.setAlpha(alpha);
					}
					deadLine = deadLine + 20;
					if (deadLine > 1000) {
						bgLoop = false;
						alpha = 255;
						paintBg.setAlpha(alpha);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

	}// end class

	public boolean isMbLoop() {
		return mbLoop;
	}

	public void setMbLoop(boolean mbLoop) {
		this.mbLoop = mbLoop;
	}

	/**
	 * 背景切换特效
	 */
	/**
	 * 绘制一个矩形
	 * 
	 * @param canvas
	 * @param color
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public void drawFillRect(Canvas canvas, int color, int x, int y, int w,
			int h) {
		int backColor = effectPaint.getColor();
		effectPaint.setColor(color);
		effectPaint.setAlpha(effectAlpha);
		canvas.drawRect(x, y, x + w, y + h, effectPaint);
		effectPaint.setColor(backColor);
	}

	public void drawEffectImage4(Canvas canvas, Bitmap bmp, int x, int y,
			int w, int h) {
		bmp = Bitmap.createBitmap(bmp, x, y, w, h);
		canvas.drawBitmap(bmp, x, y, null);
	}

	/**
	 * 绘制一个扇形
	 * 
	 * @param canvas
	 * @param color
	 * @param oval
	 * @param startAngle
	 * @param sweepAngle
	 * @param useCenter
	 */
	public void drawFillCircle(Canvas canvas, int color, RectF oval,
			int startAngle, int sweepAngle, boolean useCenter) {
		int backColor = effectPaint.getColor();
		effectPaint.setColor(color);
		effectPaint.setAlpha(effectAlpha);
		canvas.drawArc(oval, startAngle, sweepAngle, useCenter, effectPaint);
		effectPaint.setColor(backColor);
	}

	/**
	 * 程序切割图片
	 * 
	 * @param bitmap
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 */
	public Bitmap BitmapClipBitmap(Bitmap bitmap, int x, int y, int w, int h) {
		return Bitmap.createBitmap(bitmap, x, y, w, h);
	}

	/** 绘制特效 **/
	public void RenderEffect() {
		switch (s_effectType) {
		case RANDOM_EFFECT_TYPE_SQUARE:
			/** 百叶窗效果利用双for循环 修改每个矩形绘制的宽度 **/
			for (int i = 0; i <= (screenWidth / RANDOM_TYPE_0_RANGE); i++) {
				for (int j = 0; j <= (screenHeight / RANDOM_TYPE_0_RANGE); j++) {
					drawFillRect(canvas, Color.LTGRAY, i * RANDOM_TYPE_0_RANGE,
							j * RANDOM_TYPE_0_RANGE, s_effRange, s_effRange);
				}
			}
			break;
		case RANDOM_EFFECT_TYPE_SHADOW:
			/** 水纹效果其实绘制了4个矩形 中间留一些缝隙 **/
			// drawFillRect(canvas, Color.GRAY, 0, 0, s_effRange, screenHeight);
			// drawFillRect(canvas, Color.GRAY, s_effRange +
			// RANDOM_TYPE_1_SPACE1,
			// 0, RANDOM_TYPE_1_RANGE1, screenHeight);
			// drawFillRect(canvas, Color.GRAY, s_effRange +
			// RANDOM_TYPE_1_SPACE2,
			// 0, RANDOM_TYPE_1_RANGE2, screenHeight);
			// drawFillRect(canvas, Color.GRAY, s_effRange +
			// RANDOM_TYPE_1_SPACE3,
			// 0, RANDOM_TYPE_1_RANGE3, screenHeight);

			// System.out.println("w:" + s_effRange);
			drawEffectImage4(canvas, lastBg, 0, 0, screenWidth, screenHeight);
			drawEffectImage4(canvas, bg, 0, 0, s_effRange, screenHeight);
			canvas.drawBitmap(textBg, textBgX, textBgY, null);
			// drawEffectImage4(canvas, lastBg, RANDOM_TYPE_1_SPACE2, 0,
			// RANDOM_TYPE_1_RANGE1, screenHeight);
			// drawEffectImage4(canvas, lastBg, 15 + RANDOM_TYPE_1_SPACE2, 0,
			// RANDOM_TYPE_1_RANGE2, screenHeight);
			// // drawEffectImage4(canvas, bg, 15 + RANDOM_TYPE_1_SPACE3,
			// // 0, s_effRange + RANDOM_TYPE_1_SPACE2, screenHeight);
			break;

		case RANDOM_EFFECT_TYPE_CROSS:
			/** 交错的实现矩形相交 **/
			drawEffectImage4(canvas, lastBg, 0, 0, screenWidth, screenHeight);
			int count = (screenHeight / RANDOM_TYPE_2_RANGE);
			for (int i = 0; i < count; i += 2) {
				// drawFillRect(canvas, Color.LTGRAY, 0, i *
				// RANDOM_TYPE_2_RANGE,
				// s_effRange, RANDOM_TYPE_2_RANGE);
				drawEffectImage4(canvas, bg, 0, i * RANDOM_TYPE_2_RANGE,
						s_effRange, RANDOM_TYPE_2_RANGE);
			}
			for (int i = 1; i < count; i += 2) {
				// drawFillRect(canvas, Color.BLACK, screenWidth - s_effRange, i
				// * RANDOM_TYPE_2_RANGE, s_effRange, RANDOM_TYPE_2_RANGE);
				drawEffectImage4(canvas, bg, screenWidth - s_effRange, i
						* RANDOM_TYPE_2_RANGE, s_effRange, RANDOM_TYPE_2_RANGE);
			}
			break;

		case RANDOM_EFFECT_TYPE_SECTOR:
			// rectf为扇形绘制区域 为了让扇形完全填充屏幕所以将它的区域扩大了100像素
			RectF rectf = new RectF(-RANDOM_TYPE_3_RANGE, -RANDOM_TYPE_3_RANGE,
					screenWidth + RANDOM_TYPE_3_RANGE, screenHeight
							+ RANDOM_TYPE_3_RANGE);
			// 将扇形绘制出来
			drawFillCircle(canvas, Color.BLACK, rectf, 0, s_effRange, true);
			break;

		}
	}

	/** 更新特效 **/
	public void UpdataEffectRange(int range) {
		if (s_effectRangeTarget == RANDOM_TYPE_0_RANGE) {
			if (s_effRange < s_effectRangeTarget) {
				s_effRange += range;
				// paintBg.setAlpha(effectAlpha);
				if (s_effRange > s_effectRangeTarget) {
					s_effRange = s_effectRangeTarget;
					// effectAlpha = 255;
					// paintBg.setAlpha(effectAlpha);
					isEffect = false;
				}
			}
		}
		if (s_effectRangeTarget == screenWidth) {// 百叶窗
			if (s_effRange < s_effectRangeTarget) {
				s_effRange += range;
				if (s_effRange > s_effectRangeTarget) {
					s_effRange = s_effectRangeTarget;
					// effectAlpha = 255;
					isEffect = false;
				}
			}
		}
		if (s_effectRangeTarget == 360) {//
			if (s_effRange < s_effectRangeTarget) {
				s_effRange += range;
				if (s_effRange > s_effectRangeTarget) {
					s_effRange = s_effectRangeTarget;
					// effectAlpha = 255;
					isEffect = false;
				}
			}
		}
		// if (s_effRange < s_effectRangeTarget) {
		// s_effRange += range;
		// if (s_effRange > s_effectRangeTarget) {
		// s_effRange = s_effectRangeTarget;
		// effectAlpha = 255;
		// System.out.println("effrange:" + s_effRange);
		// isEffect = false;
		// }
		// } else if (s_effRange > s_effectRangeTarget) {
		// s_effRange -= range;
		// if (s_effRange < s_effectRangeTarget) {
		// s_effRange = s_effectRangeTarget;
		// effectAlpha = 255;
		// isEffect = false;
		// }
		// }
	}

	/** 设置播放特效类型 **/
	public void SetCurtainEffect(int type) {
		s_effectType = type;
		switch (s_effectType) {
		case RANDOM_EFFECT_TYPE_SQUARE:
			s_effRange = 1;
			s_effectRangeTarget = RANDOM_TYPE_0_RANGE;
			break;
		case RANDOM_EFFECT_TYPE_SHADOW:
			// s_effRange = EFFECT_RANGE_PERFRAME_1;
			s_effectRangeTarget = screenWidth;
			break;
		case RANDOM_EFFECT_TYPE_CROSS:
			s_effRange = 1;
			s_effectRangeTarget = screenWidth;
			break;
		case RANDOM_EFFECT_TYPE_SECTOR:
			s_effRange = 1;
			s_effectRangeTarget = 360;
			break;
		}
		isEffect = true;
	}

}
