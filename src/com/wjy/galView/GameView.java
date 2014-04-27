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
 * @author ��
 * 
 */
@SuppressLint({ "ViewConstructor", "SimpleDateFormat" })
public class GameView extends SurfaceView implements Runnable,
		SurfaceHolder.Callback {

	public boolean mbLoop = false;// ����ѭ��

	public MainActivity context;
	public GalReader galReader;
	public String[] texts;
	public int stepCount = 1;
	public int stepTotal = 0;

	SurfaceHolder mSurfaceHolder = null;
	public Canvas canvas;

	// �洢�������
	public GalApp app;
	public int screenWidth = 10;
	public int screenHeight = 10;

	// ��Ļÿ�ȷּ�����
	public int screenEach = 20;// Ĭ�Ͻ���Ļ��������Ϊ20��
	public int screenWidthEach = 10;
	public int screenHeightEach = 10;

	public String currentFile = "branch/player.1.1.txt";

	private Paint mPaint = null;
	public Bitmap bgColor;
	public Bitmap confirm1;
	private int textSize = 22;
	String time = "201X";

	// ������ǰ�����������ı����ı������������
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

	public boolean isTextBg = false;// �Ƿ�����ı���
	public boolean isNameBg = false;// �Ƿ����������

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

	// ����
	Typeface faceName;
	Typeface faceText;

	// ��Ϸ�в˵�
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

	public boolean isSaveGame = false;// �Ƿ񱣴���Ϸ

	public boolean isReturnLastBranch = false;// ������һ��֧
	public boolean isDrawExit = false;// ����������

	// û��ѡ��ĳ���
	public Rect gameNextRect;

	// ��ѡ��ĳ���
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

	// ȷ�϶Ի���
	public boolean isDrawConfirm = false;
	public String confirmTip = "��ȷ��Ҫ������";
	public String confirmOk = "��ȷ��Ҫ������";
	public String confirmCanel = "��ȷ��Ҫ������";
	public Bitmap confirmBg;
	public Rect[] confirmRect;
	public int confirmDistance = 0;

	// ��ʾ��Ϣ
	public String operationTip = "";
	public boolean isTip = false;

	// ��ȡ��Ϸ
	public boolean isLoad = false;

	// ��Ч��ˢ
	public Paint effectPaint;
	public int effectAlpha = 255;
	public boolean isEffect = false;
	/** ��Ч���Ų��� **/
	private int s_effRange = 1;
	/** ��Ч���Ų��� **/
	private int s_effectRangeTarget;
	/** ��Ч���� **/
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
	 * ��ʼ����Դ
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
		// �ж��Ƿ�����Ϸ��һ���ı��������ǣ����¼��һ�ı�
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

		// �������ʼ��
		nameBgWidth = screenWidthEach * 2;
		nameBgHeight = screenHeightEach;
		nameBg = Bitmap.createScaledBitmap(textBg, nameBgWidth, nameBgHeight,
				false);
		nameBgX = textBgX + screenWidthEach;
		nameBgY = textBgY - nameBgHeight;

		// ��һ�ν�����Ϸ
		setCurrentView(bg, actor, text, "bg/black.bmp", "actor/none_actor.png",
				app.getMusicFile(), stepCount, "branch/player.1.1.txt");

		// ��Ϸ�˵������������
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

		// ȷ�϶Ի���
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

		// û��ѡ��ĳ�������
		gameNextRect = new Rect(0, screenHeight / 6, screenWidth, screenHeight);

		// ѡ���������������
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

		// ����
		faceName = Typeface.createFromAsset(context.getAssets(),
				"fonts/STXINGKA.TTF");// �����п�
		//faceText = Typeface.createFromAsset(context.getAssets(),
		// "fonts/FZSTK.TTF");//��������
		//		"fonts/SIMYOU.TTF");// ��Բ

		// ��Ч����
		EFFECT_RANGE_PERFRAME_0 = screenWidth / 8;// ��������ƶ��ٶ�
		// RANDOM_TYPE_1_SPACE2 = screenWidth / 2;// ˮ�����ƶ��ٶ�

	}

	/**
	 * Surface�ı��Сʱ����
	 */
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	/**
	 * Surface����ʱ����
	 */
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		new Thread(this).start();
	}

	/**
	 * Surface����ʱ����
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
	 * ������Ļ
	 */
	public void Draw() {
		canvas = mSurfaceHolder.lockCanvas();// �õ�����������
		if (mSurfaceHolder == null || canvas == null) {
			return;
		}
		// ����
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.BLACK);
		canvas.drawColor(Color.BLACK);

		mPaint.setColor(Color.BLACK);
		textSize = screenHeight / 22;
		//mPaint.setTypeface(faceText);
		// mPaint.setS
		mPaint.setTextSize(textSize);

		canvas.drawBitmap(bg, 0, 0, paintBg);// ���Ʊ���
		canvas.drawBitmap(actor, actorX, actorY, null);// ����ǰ��
		canvas.drawBitmap(textBg, textBgX, textBgY, null);// ���ƶԻ���

		if (isNameBg) {
			Paint namePaint = new Paint();
			namePaint.setAntiAlias(true);
			namePaint.setTypeface(faceName);
			namePaint.setTextSize(screenHeight / 24);
			canvas.drawBitmap(nameBg, nameBgX, nameBgY, null);// ����������
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

		// ������Ч
		if (isEffect == true) {
			RenderEffect();
			UpdataEffectRange(EFFECT_RANGE_PERFRAME_0);
		}

		// ��Ϸ�в˵�
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

		// ��ѡ���ʱ��
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

		/** ----------------������ʾ��Ϣ---------- **/
		if (isTip == true) {
			Paint timePaint = new Paint();
			timePaint.setColor(Color.BLUE);
			timePaint.setAntiAlias(true);
			timePaint.setTextSize(screenHeight / 16);
			canvas.drawText(operationTip, screenHeight / 16, screenHeight / 14,
					timePaint);
		}

		/** -------------- ����ȷ�Ͽ�--------------- **/
		if (isDrawConfirm == true) {
			if (isSaveGame == true) {// ����
				canvas.drawBitmap(confirm1, 0, 0, null);
				drawConfirm(canvas, "�洢Ϊ:" + text, "�õ�", "����");
				Paint timePaint = new Paint();
				timePaint.setColor(Color.GREEN);
				timePaint.setAntiAlias(true);
				timePaint.setTextSize(screenHeight / 16);
				canvas.drawText(time, screenHeight / 16, screenHeight / 14,
						timePaint);
			}
			if (isLoad == true) {
				canvas.drawBitmap(confirm1, 0, 0, null);
				drawConfirm(canvas, "�����ܶ�����һ���Ҹ���...", "�ǵ�", "����");
				Paint timePaint = new Paint();
				timePaint.setColor(Color.GREEN);
				timePaint.setAntiAlias(true);
				timePaint.setTextSize(screenHeight / 16);
				canvas.drawText(time, screenHeight / 16, screenHeight / 14,
						timePaint);
			}
			if (isReturnLastBranch == true) {// ������һ��֧
				canvas.drawBitmap(bgColor, 0, 0, null);
				drawConfirm(canvas, "ȷ��Ҫ����ѡ����(��_��)?", "��", "����..");
			}
			if (isDrawExit == true) {// �˳���Ϸ
				canvas.drawBitmap(bgColor, 0, 0, null);
				drawConfirm(canvas, "�˳�ǰ�����Ǳ�����ϷŶ^_^ ", "��Ȼ", "�ֻ���..");
			}
		}

		mSurfaceHolder.unlockCanvasAndPost(canvas);// ������ɲ���������
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();

		// �����ȡ��������ʾ
		operationTip = "";
		isTip = false;

		// ���������Ϸ�в˵�
		if (isDrawGameMenu == true) {
			if (gameMenu[0].contains(x, y) && isDrawExit == false) {// �浵
				time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
						.format(new Date());
				isSaveGame = true;
				isDrawConfirm = true;
			}

			if (gameMenu[1].contains(x, y) && isDrawExit == false) {// ����
				isLoad = true;
				isDrawConfirm = true;
			}

			if (gameMenu[2].contains(x, y) && isDrawExit == false) {// ��һ��֧
				isReturnLastBranch = true;
				isDrawConfirm = true;
			}

			if (gameMenu[3].contains(x, y) && isDrawExit == false) {// ����
				System.out.println("gameMenu4");
				isDrawGameMenu = false;
			}

			// ����������
			if (gameMenu[4].contains(x, y) && isDrawGameMenu == true) {// ������
				isDrawConfirm = true;
				isDrawExit = true;
			}
		}

		// �����Ϸ�˵�������ʾ�˵�
		if (gameMenuTotal.contains(x, y) && isDrawGameMenu == false
				&& isDrawConfirm == false) {
			isDrawGameMenu = true;
		}

		/** --------------End ��Ϸ�˵�-------------------- **/

		// ûѡ��ʱ�������Ϸ�˵������⣬���ز˵�,ͬʱ��һ��
		if (gameNextRect.contains(x, y) && isDrawSelect == false
				&& isDrawExit == false && isDrawConfirm == false
				&& isSelecting == false) {
			isDrawGameMenu = false;
			isDrawSelect = false;
			stepCount = stepCount + 1;
			if (stepCount < stepTotal) {
				String tempFile = texts[stepCount];

				if (tempFile.contains("$background$")) {// ����Ǳ���
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
					// ��Ч
					if (effectCategory.contains("$SQUARE$")) {
						SetCurtainEffect(RANDOM_EFFECT_TYPE_SQUARE);// ��Ҷ��
					} else if (effectCategory.contains("$CROSS$")) {
						SetCurtainEffect(RANDOM_EFFECT_TYPE_CROSS);// �������
					} else if (effectCategory.contains("$SECTOR$")) {
						SetCurtainEffect(RANDOM_EFFECT_TYPE_SECTOR);// ����
					} else if (effectCategory.contains("$SHADOW$")) {
						SetCurtainEffect(RANDOM_EFFECT_TYPE_SHADOW);// ˮ����
					} else {
						new Thread(new BgThread(paintBg)).start();
					}
					//

				} else if (tempFile.contains("$actor$")) {// �����ǰ��1
					stepCount = stepCount + 1;
					tempFile = texts[stepCount].substring(0,
							texts[stepCount].length() - 1);
					app.setActorFile(tempFile);
					actor = galReader.readImageSource(tempFile);
					actor = change2Actor(actor);
					stepCount = stepCount + 1;
					this.text = texts[stepCount];
				} else if (tempFile.contains("$name$")) {// ���������
					isNameBg = true;
					app.setPreName(name);
					stepCount = stepCount + 1;
					name = texts[stepCount];
					app.setCurrentName(name);
					stepCount = stepCount + 1;
					this.text = texts[stepCount];
				} else if (tempFile.contains("$nameEnd$")) {// �������������
					isNameBg = false;
					app.setPreName(name);
					name = "-";
					app.setCurrentName(name);
					stepCount = stepCount + 1;
					this.text = texts[stepCount];
				} else if (tempFile.contains("$music$")) {// ���������
					galReader.stopMusic();
					stepCount = stepCount + 1;
					tempFile = texts[stepCount].substring(0,
							texts[stepCount].length() - 1);
					app.setMusicFile(tempFile);
					galReader.readMusicSource(tempFile);
					stepCount = stepCount + 1;
					this.text = texts[stepCount];
				} else if (tempFile.contains("$soundEffect$")) {// �������Ч
					galReader.stopSound();
					stepCount = stepCount + 1;
					tempFile = texts[stepCount].substring(0,
							texts[stepCount].length() - 1);
					galReader.readSoundSource(tempFile);
					stepCount = stepCount + 1;
					this.text = texts[stepCount];

				} else if (tempFile.contains("$branch$")) {// ����Ƿ�֧
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

		// ��ѡ��ʱ
		if (isDrawSelect == true && isSelecting == true
				&& isDrawConfirm == false) {
			// ��ѡ��ʱ�������Ϸ�˵������⣬���ز˵�
			if (gameNextRect.contains(x, y) && isDrawConfirm == false) {
				isDrawGameMenu = false;
			}

			if (select[0].contains(x, y)) {
				isDrawSelect = false;
				isGameStartText();
				texts = galReader.readTextSource(selectFile[0]);
				initGalText();
				app.setCurrentBranchFile(selectFile[0]);// ���õ�ǰ�ı�
				isSelecting = false;
			}
			if (select[1].contains(x, y)) {
				isGameStartText();
				isDrawSelect = false;
				texts = galReader.readTextSource(selectFile[1]);
				initGalText();
				app.setCurrentBranchFile(selectFile[1]);// ���õ�ǰ�ı�
				isSelecting = false;
			}
			if (select[2].contains(x, y)) {
				isDrawSelect = false;
				isGameStartText();
				texts = galReader.readTextSource(selectFile[2]);
				initGalText();
				app.setCurrentBranchFile(selectFile[2]);// ���õ�ǰ�ı�
				isSelecting = false;
			}
			if (select[3].contains(x, y)) {
				isDrawSelect = false;
				isGameStartText();
				texts = galReader.readTextSource(selectFile[3]);
				initGalText();
				app.setCurrentBranchFile(selectFile[3]);// ���õ�ǰ�ı�
				isSelecting = false;
			}
		}

		/** --------------���������ȷ�Ͽ�------------- **/
		if (isDrawConfirm == true) {
			isDrawGameMenu = false;
			if (isSaveGame == true) {// �浵
				if (confirmRect[0].contains(x, y)) {
					String content = text + "\r\n" + time + "\r\n"
							+ app.getBgFile() + "\r\n" + app.getActorFile()
							+ "\r\n" + app.getMusicFile() + "\r\n" + stepCount
							+ "\r\n" + app.getCurrentBranchFile();
					boolean result = saveGame(content);
					if (result == true) {
						isTip = true;
						operationTip = "���ȱ���ɹ�^_^";
						isSaveGame = false;
						isDrawConfirm = false;
					} else {
						isTip = true;
						operationTip = "���ȱ���ʧ�� >_<";
						System.out.println("save false!");
					}
				} else if (confirmRect[1].contains(x, y)) {
					isSaveGame = false;
					isDrawConfirm = false;
				}
			}
			if (isLoad == true) {// ����
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
			if (isReturnLastBranch == true) {// ������һ��֧
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
			if (isDrawExit == true) {// ����������
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
	 * ��ȡ��֮����г�ʼ��
	 */
	public void initGalText() {
		stepTotal = texts.length;
		text = texts[1];
		stepCount = 1;
	}

	/**
	 * �ж��Ƿ�����Ϸ��һ���ı��������ǵĻ���������һ�ı�
	 */
	public void isGameStartText() {
		// �ж��Ƿ�����Ϸ��һ���ı��������ǣ����¼��һ�ı�
		if (!texts[0].contains("$START$")) {
			app.setLastBranchFile(texts[0].substring(0, texts[0].length() - 1));
		}
	}

	/**
	 * ����ȷ�϶Ի���
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
	 * @param bg����
	 * @param actorǰ��
	 * @param text����
	 * @param bgFile�����ļ�
	 * @param actorFileǰ���ļ���
	 * @param musicFile�����ļ���
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
	 * ������ͼƬ����Ϊ�ʺϵĴ�С
	 * 
	 * @param bg
	 * @return
	 */
	public Bitmap change2Bg(Bitmap bg) {
		bg = Bitmap.createScaledBitmap(bg, screenWidth, screenHeight, false);
		return bg;
	}

	/**
	 * ��ǰ��ͼ����Ϊ���ʵĴ�С
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
	 * ������Ϸ
	 * 
	 * @param content
	 * @return
	 */
	public boolean saveGame(String content) {
		boolean result = false;
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {// ���û�д洢��
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
				// ���ļ���¼ָ���ƶ����
				raf.seek(save.length());
				// ����ļ�����
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
	 * ���Ʊ���ͼ����-�ı�͸����
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
	 * �����л���Ч
	 */
	/**
	 * ����һ������
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
	 * ����һ������
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
	 * �����и�ͼƬ
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

	/** ������Ч **/
	public void RenderEffect() {
		switch (s_effectType) {
		case RANDOM_EFFECT_TYPE_SQUARE:
			/** ��Ҷ��Ч������˫forѭ�� �޸�ÿ�����λ��ƵĿ�� **/
			for (int i = 0; i <= (screenWidth / RANDOM_TYPE_0_RANGE); i++) {
				for (int j = 0; j <= (screenHeight / RANDOM_TYPE_0_RANGE); j++) {
					drawFillRect(canvas, Color.LTGRAY, i * RANDOM_TYPE_0_RANGE,
							j * RANDOM_TYPE_0_RANGE, s_effRange, s_effRange);
				}
			}
			break;
		case RANDOM_EFFECT_TYPE_SHADOW:
			/** ˮ��Ч����ʵ������4������ �м���һЩ��϶ **/
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
			/** �����ʵ�־����ཻ **/
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
			// rectfΪ���λ������� Ϊ����������ȫ�����Ļ���Խ���������������100����
			RectF rectf = new RectF(-RANDOM_TYPE_3_RANGE, -RANDOM_TYPE_3_RANGE,
					screenWidth + RANDOM_TYPE_3_RANGE, screenHeight
							+ RANDOM_TYPE_3_RANGE);
			// �����λ��Ƴ���
			drawFillCircle(canvas, Color.BLACK, rectf, 0, s_effRange, true);
			break;

		}
	}

	/** ������Ч **/
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
		if (s_effectRangeTarget == screenWidth) {// ��Ҷ��
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

	/** ���ò�����Ч���� **/
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
