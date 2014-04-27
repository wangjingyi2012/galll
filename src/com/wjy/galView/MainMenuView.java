package com.wjy.galView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.wjy.gal.util.GalApp;
import com.wjy.gal.util.GalUtil;
import com.wjy.galeng.MainActivity;
import com.wjy.galeng.R;

@SuppressLint("ViewConstructor")
public class MainMenuView extends SurfaceView implements
		SurfaceHolder.Callback, Runnable {

	public boolean mbLoop = false;// ����ѭ��

	public MainActivity context;

	SurfaceHolder mSurfaceHolder = null;

	public GalApp app;
	public int screenWidth = 10;
	public int screenHeight = 10;
	public int textSize = 22;

	private Paint mPaint = null;
	public Bitmap mainMenuBg = null;
	public Bitmap gameTitle;
	public Bitmap selectIcon;

	// ��׼����
	public int distanceX = 0;
	public int distanceY = 0;

	// �Ҳ�˵�
	public Bitmap[] rightMenu;
	public String[] rightMenuText = GalUtil.rightMenuText;
	public String[] rightMenuTextTips = GalUtil.rightMenuTextTips;
	public int rightMenuWidth = 0;
	public int rightMenuHeight = 0;
	public int rightMenuDistance = 0;
	public int rightMenuX = 0;
	public int rightMenuY = 0;
	public int rightMenuTextSize = 22;
	public Rect rightMenuRect[];
	public Rect select[];

	public boolean isDrawExit = false;
	public Bitmap exitBg;
	public String exitTip = ">_< ȷ���˳��� ";
	public String exitOk = "��", exitNo = "�ֻ�";
	public Rect[] exitRect;

	public Bitmap[] snows;
	public int snowX[], snowY[];
	public int snowCount = 10;

	// ����
	Typeface faceName;
	Typeface faceText;

	/**
	 * ���췽��
	 * 
	 * @param context
	 */
	public MainMenuView(MainActivity context) {
		super(context);
		this.context = context;
		mSurfaceHolder = this.getHolder();
		mSurfaceHolder.addCallback(this);
		this.setFocusable(true);

		app = (GalApp) context.getApplicationContext();
		this.screenWidth = app.getScreenWidth();
		this.screenHeight = app.getScreenHeight();
		distanceX = screenWidth / 16;
		distanceY = screenHeight / 8;
		mPaint = new Paint();
		initView();
		mbLoop = true;
	}

	/**
	 * ��ʼ����Դ
	 */
	@SuppressWarnings("static-access")
	public void initView() {

		// �õ�����ͼƬ������
		mainMenuBg = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.main_menu_bg)).getBitmap();
		mainMenuBg = Bitmap.createScaledBitmap(mainMenuBg, screenWidth,
				screenHeight, false);

		textSize = screenHeight / 14;

		// �Ҳ�˵�������
		rightMenuWidth = screenWidth / GalUtil.rightMenuWidthCount;
		rightMenuHeight = screenHeight / GalUtil.rightMenuHeightCount;
		rightMenuDistance = screenHeight / GalUtil.rightMenuDistance;
		rightMenuX = distanceX * 11 + distanceX / 2;
		rightMenuY = distanceY * 3;
		rightMenuTextSize = rightMenuHeight / GalUtil.rightMenuTextSizeCount;
		rightMenu = new Bitmap[4];
		rightMenuRect = new Rect[4];
		for (int i = 0; i < 4; i++) {
			rightMenu[i] = ((BitmapDrawable) getResources().getDrawable(
					R.drawable.right_menu_bg)).getBitmap();
			rightMenu[i] = rightMenu[i].createScaledBitmap(rightMenu[i],
					rightMenuWidth, rightMenuHeight, false);
			rightMenuRect[i] = new Rect(rightMenuX + i * 20, rightMenuY + i
					* rightMenuDistance, rightMenuX + i * 20 + rightMenuWidth,
					rightMenuY + i * rightMenuDistance + rightMenuHeight);
		}

		// �����˵�
		gameTitle = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.gametitle)).getBitmap();
		gameTitle = Bitmap.createScaledBitmap(gameTitle, distanceX * 10,
				distanceY * 4, false);
		selectIcon = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.right_menu_bg)).getBitmap();
		selectIcon = Bitmap.createScaledBitmap(selectIcon, distanceX * 2,
				distanceY, false);
		select = new Rect[3];
		select[0] = new Rect(distanceX, screenHeight - distanceY * 2,
				distanceX * 2, screenHeight - distanceY);
		select[1] = new Rect();
		select[2] = new Rect();

		// װ����
		snows = new Bitmap[snowCount];
		snowX = new int[] { distanceX, distanceX * 2, distanceX * 4,
				distanceX * 6, distanceX * 8, distanceX * 10, distanceX * 12,
				distanceX * 5, distanceX * 9, distanceX * 14 };
		snowY = new int[] { screenHeight, screenHeight - distanceY * 5,
				screenHeight - distanceY * 2, screenHeight - distanceY * 4,
				screenHeight - distanceY * 6, screenHeight - distanceY,
				screenHeight - distanceY * 3, screenHeight - distanceY * 4,
				screenHeight - distanceY * 5, screenHeight - distanceY * 2 };
		for (int i = 0; i < snowCount; i++) {
			snows[i] = ((BitmapDrawable) getResources().getDrawable(
					R.drawable.snow)).getBitmap();
			snows[i] = Bitmap.createScaledBitmap(snows[i], distanceX / 2,
					distanceY / 2, false);
		}

		// �˳�
		exitBg = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.exit_bg)).getBitmap();
		exitBg = Bitmap.createScaledBitmap(exitBg, screenWidth * 2 / 3,
				screenHeight * 3 / 5, false);
		exitRect = new Rect[2];
		exitRect[0] = new Rect(screenWidth / 6 + rightMenuDistance * 3 / 2
				- screenHeight / 10, screenHeight / 7 + rightMenuDistance * 3
				- screenHeight / 8, screenWidth / 6 + rightMenuDistance * 3 / 2
				+ screenHeight / 7, screenHeight / 7 + rightMenuDistance * 3
				+ screenHeight / 16);
		exitRect[1] = new Rect(screenWidth / 6 + rightMenuDistance * 4
				- screenHeight / 32, screenHeight / 7 + rightMenuDistance * 3
				- screenHeight / 8, screenWidth / 6 + rightMenuDistance * 4
				+ screenHeight * 7 / 2, screenHeight / 7 + rightMenuDistance
				* 3 + screenHeight / 16);

		// ����
		faceName = Typeface.createFromAsset(context.getAssets(),
				"fonts/STXINGKA.TTF");// �����п�

	}

	@Override
	public void run() {
		while (mbLoop) {
			try {
				for (int i = 0; i < snowCount; i++) {
					snowY[i] = snowY[i] - 10;
					if (snowY[i] < 0) {
						snowY[i] = screenHeight;
					}
				}
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
			synchronized (mSurfaceHolder) {
				Draw();
			}
		}

	}

	/**
	 * ��ͼ����
	 */
	public void Draw() {
		Canvas canvas = mSurfaceHolder.lockCanvas();// �õ�����������
		if (mSurfaceHolder == null || canvas == null) {
			return;
		}

		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.BLACK);

		// ����
		canvas.drawColor(Color.BLACK);

		mPaint.setColor(Color.WHITE);
		textSize = screenHeight / 16;
		mPaint.setTextSize(textSize);
		mPaint.setAlpha(180);

		mPaint.setTypeface(faceName);

		canvas.drawBitmap(mainMenuBg, 0, 0, null);
		/**
		 * ����ѩ����Ч
		 */
		// for (int i = 0; i < snowCount; i++) {
		// canvas.drawBitmap(snows[i], snowX[i], snowY[i], mPaint);
		// }

		// ���Ʊ���
		Paint titlePaint = new Paint();
		titlePaint.setAntiAlias(true);
		titlePaint.setColor(Color.WHITE);
		titlePaint.setTypeface(faceName);
		titlePaint.setTextSize(screenHeight / 13);
		canvas.drawText("��ӣ֮��-����", screenWidth / 2, distanceY, titlePaint);
		titlePaint.setColor(Color.GRAY);
		canvas.drawText("��ӣ֮��-����", screenWidth / 2 + 2, distanceY + 2,
				titlePaint);

		canvas.drawText(screenWidth + "*" + screenHeight, distanceX, distanceY,
				mPaint);
		canvas.drawBitmap(gameTitle, distanceX * 7, distanceY / 2, null);

		mPaint.setColor(Color.BLACK);
		textSize = screenHeight / 13;
		mPaint.setAlpha(180);
		for (int i = 0; i < 4; i++) {
			canvas.drawBitmap(rightMenu[i], rightMenuRect[i].left,
					rightMenuRect[i].top, mPaint);
			canvas.drawText(rightMenuText[i], rightMenuRect[i].left
					+ screenHeight / 20, rightMenuRect[i].top + screenHeight
					/ 13, mPaint);
		}

		//canvas.drawBitmap(selectIcon, select[0].left, select[0].top, mPaint);

		if (isDrawExit == true) {

			Paint exitPaint = new Paint();
			exitPaint.setAntiAlias(true);
			textSize = screenHeight / 14;
			exitPaint.setTextSize(textSize);
			exitPaint.setColor(Color.BLACK);
			canvas.drawBitmap(exitBg, screenWidth / 6, screenHeight / 6, null);
			canvas.drawText(exitTip, screenWidth / 6 + rightMenuDistance * 2
					/ 3, screenHeight / 7 + rightMenuDistance * 2, exitPaint);
			canvas.drawLine(screenWidth / 6 + rightMenuDistance * 2 / 3 + 70,
					screenHeight / 7 + rightMenuDistance * 2 + 10, screenWidth
							/ 6 + rightMenuDistance * 2 / 3 + screenHeight / 2,
					screenHeight / 7 + rightMenuDistance * 2 + 10, exitPaint);
			textSize = screenHeight / 13;
			exitPaint.setTextSize(textSize);
			canvas.drawText(exitOk, screenWidth / 6 + rightMenuDistance * 3 / 2
					- 20, screenHeight / 7 + rightMenuDistance * 3, exitPaint);
			canvas.drawText(exitNo, screenWidth / 6 + rightMenuDistance * 4,
					screenHeight / 7 + rightMenuDistance * 3, exitPaint);

		}

		mSurfaceHolder.unlockCanvasAndPost(canvas);// ������ɲ���������

	}

	/**
	 * ��surface��С�����仯ʱ����
	 */
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

	}

	/**
	 * ��surface����ʱ����
	 */
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		new Thread(this).start();

	}

	/**
	 * ��surface����ʱ����
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		mbLoop = false;

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		if (rightMenuRect[0].contains(x, y) && isDrawExit == false) {
			setMbLoop(false);
			context.setContentView(new GameView(context));
		}
		if (rightMenuRect[1].contains(x, y) && isDrawExit == false) {
			context.setContentView(new LoadView(context));// ��ȡ��Ϸ����
		}
		if (rightMenuRect[2].contains(x, y) && isDrawExit == false) {
			System.out.println("game setting!");
		}
		if (rightMenuRect[3].contains(x, y) && isDrawExit == false) {
			this.isDrawExit = true;
		}
		if (exitRect[0].contains(x, y) && isDrawExit == true) {
			context.isGmaeExit = true;
		}
		if (exitRect[1].contains(x, y) && isDrawExit == true) {
			isDrawExit = false;
		}

		return super.onTouchEvent(event);
	}

	public boolean isMbLoop() {
		return mbLoop;
	}

	public void setMbLoop(boolean mbLoop) {
		this.mbLoop = mbLoop;
	}

}
