package com.wjy.galView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.wjy.gal.util.GalApp;
import com.wjy.gal.util.GalReader;
import com.wjy.galeng.MainActivity;
import com.wjy.galeng.R;

public class LoadView extends SurfaceView implements SurfaceHolder.Callback,
		Runnable {

	public boolean mbLoop = false;// ����ѭ��

	public MainActivity context;
	public GameView gameView;

	public boolean isNewGame = true;// �Ƿ��Ǵ��������ȡ����

	SurfaceHolder mSurfaceHolder = null;

	public GalApp app;
	public int screenWidth = 10;
	public int screenHeight = 10;
	public int textSize = 22;
	Paint mPaint = null;

	public Bitmap loadBg;
	public Bitmap loadRectBg;
	public Bitmap backIcon;
	public Bitmap toleftIcon;
	public Bitmap torightIcon;
	public String tip = "";

	// �浵����
	public File[] files;
	public String[] fileName;
	public int fileCount = 0;// �ļ�����
	public String path = "";
	public String[] text;
	public String[] time;
	public Bitmap[] bg;
	public String[] bgFile;
	public Bitmap[] actor;
	public String[] actorFile;
	public String[] music;
	public String[] stepCount;
	public String[] currentFile;
	public GalReader galReader;

	public Bitmap deleteIcon;

	public int currentPage = 0;// ��ǰҳ��
	public int totalPage = 0;// ��ҳ��
	public int currentPageCount = 0;// ��ǰҳ�ϵĴ浵��
	public int touchCount = 0;// ����Ĵ浵��
	int currentStart = 0;// ��ǰ��ʼ���ļ����

	// ����
	public int selectX = 0, selectY = 0;
	public int selectWidth = 0, selectHeight = 0;
	public int distanceX = 0, distanceY = 0;// �ؼ�֮��ľ���,ͬʱҲ�ǻ�׼����
	public Rect[] select = new Rect[4];// ѡ���
	public Rect[] delete = new Rect[4];
	public Rect toLeft, toRight;// ��������ѡ��
	public Rect back;// ����

	public LoadView(MainActivity context) {
		super(context);
		this.context = context;
		gameView = new GameView(context);

		mSurfaceHolder = this.getHolder();
		mSurfaceHolder.addCallback(this);
		this.setFocusable(true);

		app = (GalApp) context.getApplicationContext();
		this.screenWidth = app.getScreenWidth();
		this.screenHeight = app.getScreenHeight();
		mPaint = new Paint();
		galReader = new GalReader(context);

		initView();
		mbLoop = true;
	}

	public void initView() {
		distanceX = screenWidth / 16;
		distanceY = screenHeight / 8;

		loadBg = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.load_bg)).getBitmap();
		loadBg = Bitmap.createScaledBitmap(loadBg, screenWidth, screenHeight,
				false);
		loadRectBg = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.exit_bg)).getBitmap();
		loadRectBg = Bitmap.createScaledBitmap(loadRectBg, screenWidth
				- distanceX * 2, screenHeight - distanceY * 3 / 2, false);
		deleteIcon = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.delete_icon)).getBitmap();
		deleteIcon = Bitmap.createScaledBitmap(deleteIcon, distanceX / 2,
				distanceY / 2, false);
		backIcon = ((BitmapDrawable) getResources()
				.getDrawable(R.drawable.home)).getBitmap();
		backIcon = Bitmap.createScaledBitmap(backIcon, distanceX, distanceY,
				false);
		toleftIcon = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.toleft)).getBitmap();
		toleftIcon = Bitmap.createScaledBitmap(toleftIcon, distanceX,
				distanceY, false);
		torightIcon = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.toright)).getBitmap();
		torightIcon = Bitmap.createScaledBitmap(torightIcon, distanceX,
				distanceY, false);

		initFile();
		initRect();

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

	public void Draw() {

		Canvas canvas = mSurfaceHolder.lockCanvas();// �õ�����������
		if (mSurfaceHolder == null || canvas == null) {
			return;
		}

		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.BLACK);

		// ����
		canvas.drawColor(Color.BLACK);

		Paint bgPaint = new Paint();
		bgPaint.setAlpha(100);
		canvas.drawBitmap(loadBg, 0, 0, bgPaint);
		canvas.drawBitmap(loadRectBg, distanceX, distanceY / 2, null);
		bgPaint.setAlpha(180);
		canvas.drawBitmap(backIcon, back.left, back.top, bgPaint);
		canvas.drawBitmap(toleftIcon, toLeft.left, toLeft.top, bgPaint);
		canvas.drawBitmap(torightIcon, toRight.left, toRight.top, bgPaint);

		textSize = screenHeight / 20;
		mPaint.setTextSize(textSize);
		mPaint.setColor(Color.BLACK);

		for (int i = 0; i < currentPageCount; i++) {
			mPaint.setColor(Color.BLACK);
			canvas.drawBitmap(bg[currentStart + i], select[i].left,
					select[i].top, null);
			canvas.drawBitmap(actor[currentStart + i], select[i].left
					+ distanceX * 2, select[i].bottom - distanceY * 5 / 3, null);
			String tempText = text[currentStart + i];
			if (tempText.length() > 8) {
				tempText = tempText.substring(0, 8) + "...";
			}
			canvas.drawText(tempText, select[i].left + distanceX / 2,
					select[i].bottom + distanceY / 3, mPaint);
			mPaint.setColor(Color.WHITE);
			canvas.drawText(time[currentStart + i], select[i].left,
					select[i].top + distanceY / 2, mPaint);
			canvas.drawBitmap(deleteIcon, delete[i].left, delete[i].top, null);
			// canvas.drawRect(delete[i], mPaint);
		}

		mPaint.setColor(Color.GREEN);

		Paint infoPaint = new Paint();
		infoPaint.setAntiAlias(true);
		infoPaint.setTextSize(textSize);
		infoPaint.setColor(Color.GREEN);
		LinearGradient gradient = new LinearGradient(0, 0, 100, 100,
				Color.YELLOW, Color.RED, Shader.TileMode.MIRROR);
		infoPaint.setShader(gradient);
		String info = "��" + currentPage + "/" + totalPage + "ҳ����" + fileCount
				+ "���浵";
		canvas.drawText(info, distanceX, screenHeight - distanceY / 4,
				infoPaint);
		canvas.drawText(tip, screenWidth - distanceX * 4, screenHeight
				- distanceY / 4, infoPaint);

		mSurfaceHolder.unlockCanvasAndPost(canvas);// ������ɲ���������
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		new Thread(this).start();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mbLoop = false;

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		// �������ť
		if (toLeft.contains(x, y)) {
			int leftPage = currentPage - 1;
			if (leftPage > 0) {
				currentPage = leftPage;
				currentStart = (currentPage - 1) * 4;
				currentPageCount = 4;
			}
		}
		// ������Ұ�ť
		if (toRight.contains(x, y)) {
			int rightPage = currentPage + 1;
			if (rightPage < totalPage) {// �������һҳ
				currentStart = currentPage * 4;
				currentPage = rightPage;
				currentPageCount = 4;
			}
			if (rightPage == totalPage) {
				currentStart = currentPage * 4;
				currentPageCount = fileCount - 4 * currentPage;
				currentPage = rightPage;
			}
		}

		/** ---------ѡ��浵------------- **/
		if (select[0].contains(x, y)) {// ��һ���浵
			if (currentPageCount > 0) {
				touchCount = (currentPage - 1) * 4;
				int theStep = Integer.parseInt(stepCount[touchCount]);
				if (theStep != 1) {
					theStep = theStep - 1;
				}
				gameView.setCurrentView(change2BgLoaded(bg[touchCount]),
						change2ActorLoaded(actor[touchCount]),
						text[touchCount], bgFile[touchCount],
						actorFile[touchCount], music[touchCount], theStep,
						currentFile[touchCount]);
				gameView.paintBg.setAlpha(255);

				mbLoop = false;
				context.setContentView(gameView);
			}
		}

		if (select[1].contains(x, y)) {// �ڶ����浵
			if (currentPageCount > 1) {
				touchCount = (currentPage - 1) * 4 + 1;
				int theStep = Integer.parseInt(stepCount[touchCount]);
				if (theStep != 1) {
					theStep = theStep - 1;
				}
				gameView.setCurrentView(change2BgLoaded(bg[touchCount]),
						change2ActorLoaded(actor[touchCount]),
						text[touchCount], bgFile[touchCount],
						actorFile[touchCount], music[touchCount], theStep,
						currentFile[touchCount]);
				gameView.paintBg.setAlpha(255);

				mbLoop = false;
				context.setContentView(gameView);
			}
		}

		if (select[2].contains(x, y)) {// �������浵
			if (currentPageCount > 2) {
				touchCount = (currentPage - 1) * 4 + 2;
				int theStep = Integer.parseInt(stepCount[touchCount]);
				if (theStep != 1) {
					theStep = theStep - 1;
				}
				gameView.setCurrentView(change2BgLoaded(bg[touchCount]),
						change2ActorLoaded(actor[touchCount]),
						text[touchCount], bgFile[touchCount],
						actorFile[touchCount], music[touchCount], theStep,
						currentFile[touchCount]);
				gameView.paintBg.setAlpha(255);

				mbLoop = false;
				context.setContentView(gameView);
			}
		}

		if (select[3].contains(x, y)) {// ���ĸ��浵
			if (currentPageCount > 3) {
				touchCount = (currentPage - 1) * 4 + 3;
				int theStep = Integer.parseInt(stepCount[touchCount]);
				if (theStep != 1) {
					theStep = theStep - 1;
				}
				gameView.setCurrentView(change2BgLoaded(bg[touchCount]),
						change2ActorLoaded(actor[touchCount]),
						text[touchCount], bgFile[touchCount],
						actorFile[touchCount], music[touchCount], theStep,
						currentFile[touchCount]);
				gameView.paintBg.setAlpha(255);

				mbLoop = false;
				context.setContentView(gameView);
			}
		}

		/** -------------ɾ���浵----------------- **/
		if (delete[0].contains(x, y)) {// ��һ���浵
			if (currentPageCount > 0) {
				new AlertDialog.Builder(context)
						.setTitle("���Ҫɾ���浵��(��_��)?")
						.setMessage("ɾ���浵���ǲ��ɻָ��ģ�������û�д浵���ޣ�ΪʲôҪɾ���� ^_^")
						.setPositiveButton("�šѦ��",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										touchCount = (currentPage - 1) * 4;
										files[touchCount].delete();
										LoadView loadView = new LoadView(
												context);
										loadView.isNewGame = isNewGame;
										loadView.tip = "ɾ���ɹ�\\(^o^)/";
										context.setContentView(loadView);
									}
								})
						.setNegativeButton("����� >_< ",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).show();
			}
		}

		if (delete[1].contains(x, y)) {// �ڶ����浵
			if (currentPageCount > 1) {
				new AlertDialog.Builder(context)
						.setTitle("���Ҫɾ���浵��(��_��)?")
						.setMessage("ɾ���浵���ǲ��ɻָ��ģ�������û�д浵���ޣ�ΪʲôҪɾ���� ^_^")
						.setPositiveButton("�šѦ��",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										touchCount = (currentPage - 1) * 4 + 1;
										files[touchCount].delete();
										LoadView loadView = new LoadView(
												context);
										loadView.isNewGame = isNewGame;
										loadView.tip = "ɾ���ɹ�\\(^o^)/";
										context.setContentView(loadView);
									}
								})
						.setNegativeButton("����� >_< ",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).show();
			}
		}

		if (delete[2].contains(x, y)) {// �������浵
			if (currentPageCount > 2) {
				new AlertDialog.Builder(context)
						.setTitle("���Ҫɾ���浵��(��_��)?")
						.setMessage("ɾ���浵���ǲ��ɻָ��ģ�������û�д浵���ޣ�ΪʲôҪɾ���� ^_^")
						.setPositiveButton("�šѦ��",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										touchCount = (currentPage - 1) * 4 + 2;
										files[touchCount].delete();
										LoadView loadView = new LoadView(
												context);
										loadView.isNewGame = isNewGame;
										loadView.tip = "ɾ���ɹ�\\(^o^)/";
										context.setContentView(loadView);
									}
								})
						.setNegativeButton("����� >_< ",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).show();
			}
		}

		if (delete[3].contains(x, y)) {// ���ĸ��浵
			if (currentPageCount > 3) {
				new AlertDialog.Builder(context)
						.setTitle("���Ҫɾ���浵��(��_��)?")
						.setMessage("ɾ���浵���ǲ��ɻָ��ģ�������û�д浵���ޣ�ΪʲôҪɾ���� ^_^")
						.setPositiveButton("�šѦ��",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										touchCount = (currentPage - 1) * 4 + 3;
										files[touchCount].delete();
										LoadView loadView = new LoadView(
												context);
										loadView.isNewGame = isNewGame;
										loadView.tip = "ɾ���ɹ�\\(^o^)/";
										context.setContentView(loadView);
									}
								})
						.setNegativeButton("����� >_< ",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).show();
			}
		}

		/** ------------���ؼ�-------------- **/
		if (back.contains(x, y)) {
			if (isNewGame == true) {
				mbLoop = false;
				context.setContentView(new MainMenuView(context));
			} else {
				gameView.setCurrentView(app.getBg(), app.getActor(),
						app.getText(), app.getBgFile(), app.getActorFile(),
						app.getMusicFile(), app.getCurrentStepCount(),
						app.getCurrentBranchFile());
				gameView.paintBg.setAlpha(255);
				mbLoop = false;
				context.setContentView(gameView);
			}

		}

		return super.onTouchEvent(event);
	}

	public void initFile() {

		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {// ���û�д洢��
			context.setContentView(new MainMenuView(context));
		} else {
			File sdCardDir = Environment.getExternalStorageDirectory();
			path = sdCardDir.getAbsolutePath() + "/androidGal/save";
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			path = path + "/";
			fileCount = file.listFiles().length;
			if (fileCount > 0) {
				files = new File[fileCount];
				fileName = new String[fileCount];
				bg = new Bitmap[fileCount];
				bgFile = new String[fileCount];
				actor = new Bitmap[fileCount];
				actorFile = new String[fileCount];
				text = new String[fileCount];
				time = new String[fileCount];
				music = new String[fileCount];
				stepCount = new String[fileCount];
				currentFile = new String[fileCount];
				files = file.listFiles();
				if (fileCount % 4 == 0) {
					totalPage = fileCount / 4;
				} else {
					totalPage = fileCount / 4 + 1;
				}
				currentPage = 1;

				for (int i = 0; i < fileCount; i++) {
					fileName[i] = files[i].getName();
					try {
						FileInputStream is = new FileInputStream(files[i]);
						int isSize = is.available();
						byte[] buffer = new byte[isSize];
						is.read(buffer);
						is.close();
						String result = new String(buffer);
						String[] everyOne = result.split("\\n");
						text[i] = everyOne[0];
						time[i] = everyOne[1];
						bg[i] = galReader.readImageSource(everyOne[2]
								.substring(0, everyOne[2].length() - 1));
						bg[i] = change2Bg(bg[i]);
						bgFile[i] = everyOne[2].substring(0,
								everyOne[2].length() - 1);
						actor[i] = galReader.readImageSource(everyOne[3]
								.substring(0, everyOne[3].length() - 1));
						actor[i] = change2Actor(actor[i]);
						actorFile[i] = everyOne[3].substring(0,
								everyOne[3].length() - 1);
						music[i] = everyOne[4].substring(0,
								everyOne[4].length() - 1);
						stepCount[i] = everyOne[5].substring(0,
								everyOne[5].length() - 1);
						currentFile[i] = everyOne[6];
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}

		}
	}

	public void initRect() {
		selectWidth = distanceX * 2;
		selectHeight = distanceY * 2;
		selectX = distanceX * 2;
		selectY = distanceY;

		if (fileCount == 0) {
			for (int i = 0; i < 4; i++) {
				select[i] = new Rect();
				delete[i] = new Rect();
			}
		}

		if (fileCount == 1) {
			currentPageCount = 1;
			select[0] = new Rect(selectX, selectY, selectX + distanceX * 5,
					selectY + selectHeight);
			delete[0] = new Rect(select[0].left, select[0].bottom,
					select[0].left + distanceY / 2, select[0].bottom
							+ distanceY / 2);
			for (int i = 1; i < 4; i++) {
				select[i] = new Rect();
				delete[i] = new Rect();
			}
		}
		if (fileCount == 2) {
			currentPageCount = 2;
			select[0] = new Rect(selectX, selectY, selectX + distanceX * 5,
					selectY + selectHeight);
			select[1] = new Rect(selectX + distanceX * 7, selectY, selectX
					+ distanceX * 12, selectY + selectHeight);
			delete[0] = new Rect(select[0].left, select[0].bottom,
					select[0].left + distanceY / 2, select[0].bottom
							+ distanceY / 2);
			delete[1] = new Rect(select[1].left, select[1].bottom,
					select[1].left + distanceY / 2, select[1].bottom
							+ distanceY / 2);
			for (int i = 2; i < 4; i++) {
				select[i] = new Rect();
				delete[i] = new Rect();
			}
		}
		if (fileCount == 3) {
			currentPageCount = 3;
			select[0] = new Rect(selectX, selectY, selectX + distanceX * 5,
					selectY + selectHeight);
			select[1] = new Rect(selectX + distanceX * 7, selectY, selectX
					+ distanceX * 12, selectY + selectHeight);
			select[2] = new Rect(selectX, distanceY * 4, selectX + distanceX
					* 5, distanceY * 6);
			delete[0] = new Rect(select[0].left, select[0].bottom,
					select[0].left + distanceY / 2, select[0].bottom
							+ distanceY / 2);
			delete[1] = new Rect(select[1].left, select[1].bottom,
					select[1].left + distanceY / 2, select[1].bottom
							+ distanceY / 2);
			delete[2] = new Rect(select[2].left, select[2].bottom,
					select[2].left + distanceY / 2, select[2].bottom
							+ distanceY / 2);
			select[3] = new Rect();
			delete[3] = new Rect();
		}
		if (fileCount >= 4) {
			currentPageCount = 4;
			select[0] = new Rect(selectX, selectY, selectX + distanceX * 5,
					selectY + selectHeight);
			select[1] = new Rect(selectX + distanceX * 7, selectY, selectX
					+ distanceX * 12, selectY + selectHeight);
			select[2] = new Rect(selectX, distanceY * 4, selectX + distanceX
					* 5, distanceY * 6);
			select[3] = new Rect(selectX + distanceX * 7, distanceY * 4,
					selectX + distanceX * 12, distanceY * 6);
			delete[0] = new Rect(select[0].left, select[0].bottom,
					select[0].left + distanceY / 2, select[0].bottom
							+ distanceY / 2);
			delete[1] = new Rect(select[1].left, select[1].bottom,
					select[1].left + distanceY / 2, select[1].bottom
							+ distanceY / 2);
			delete[2] = new Rect(select[2].left, select[2].bottom,
					select[2].left + distanceY / 2, select[2].bottom
							+ distanceY / 2);
			delete[3] = new Rect(select[3].left, select[3].bottom,
					select[3].left + distanceY / 2, select[3].bottom
							+ distanceY / 2);
		}

		/** ---------�໬��ť---- **/
		toLeft = new Rect(0, screenHeight / 2, distanceX, screenHeight / 2
				+ distanceY);
		toRight = new Rect(screenWidth - distanceX, screenHeight / 2,
				screenWidth, screenHeight / 2 + distanceY);

		/** ----------���ذ�ť------ **/
		back = new Rect(screenWidth - distanceX, 0, screenWidth, distanceY);
	}

	/**
	 * ������ͼƬ����Ϊ�ʺϵĴ�С
	 * 
	 * @param bg
	 * @return
	 */
	public Bitmap change2Bg(Bitmap bg) {
		bg = Bitmap.createScaledBitmap(bg, distanceX * 5, distanceY * 2, false);
		return bg;
	}

	/**
	 * ��ǰ��ͼ����Ϊ���ʵĴ�С
	 * 
	 * @param actor
	 * @return
	 */
	public Bitmap change2Actor(Bitmap actor) {
		actor = Bitmap.createScaledBitmap(actor, distanceX * 2,
				distanceY * 5 / 3, false);
		return actor;
	}

	/**
	 * �����󽫱���ͼƬ����Ϊ�ʺϵĴ�С
	 * 
	 * @param bg
	 * @return
	 */
	public Bitmap change2BgLoaded(Bitmap bg) {
		bg = Bitmap.createScaledBitmap(bg, screenWidth, screenHeight, false);
		return bg;
	}

	/**
	 * ������ǰ��ͼ����Ϊ���ʵĴ�С
	 * 
	 * @param actor
	 * @return
	 */
	public Bitmap change2ActorLoaded(Bitmap actor) {
		actor = Bitmap.createScaledBitmap(actor, screenWidth / 4,
				screenHeight * 2 / 3, false);
		return actor;
	}

}
