package com.wjy.gal.util;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import com.wjy.galeng.MainActivity;

public class GalReader {

	public MainActivity context;// activity引用
	public String sourceName;// 资源名
	public AssetManager assetManager;

	public InputStream is;
	public int isSize = 1024;
	public byte[] buffer;

	public String result = "hello gal world";
	public String[] texts;
	public Bitmap bmp;

	public MediaPlayer music;// 背景音乐
	public boolean isMusicPlayed = false;
	public MediaPlayer sound;// 音效
	public AssetFileDescriptor descriptor;
	public boolean isSoundPlayed = false;

	/**
	 * @param context
	 *            activity的引用
	 * @param sourceName
	 *            资源文件名称
	 */
	public GalReader(MainActivity context) {
		this.context = context;
		bmp = null;
		texts = new String[] {};
		assetManager = context.getAssets();
		context.setVolumeControlStream(AudioManager.STREAM_MUSIC);// 音量按钮控制正确的音频流
		sound = new MediaPlayer();
		music = new MediaPlayer();
		sound.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer arg0) {
				sound.stop();
				sound.release();
			}
		});
	}

	/**
	 * 读取文本资源
	 * 
	 * @param sourceName
	 * @return
	 */
	public String[] readTextSource(String sourceName) {
		try {
			this.is = assetManager.open(sourceName);
			isSize = is.available();
			buffer = new byte[isSize];
			is.read(buffer);
			is.close();
			result = new String(buffer);
			this.texts = result.split("\\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return texts;
	}

	/**
	 * 读取图片资源
	 * 
	 * @param sourceName
	 * @return
	 */
	public Bitmap readImageSource(String sourceName) {
		try {
			is = assetManager.open(sourceName);
			bmp = BitmapFactory.decodeStream(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bmp;
	}

	/**
	 * 读取音效资源
	 * 
	 * @param sourceName
	 */
	public void readSoundSource(String sourceName) {
		try {
			sound = new MediaPlayer();
			descriptor = assetManager.openFd(sourceName);
			sound.setDataSource(descriptor.getFileDescriptor(),
					descriptor.getStartOffset(), descriptor.getLength());

			sound.setVolume(0.5f, 0.5f);
			sound.prepare();
			sound.setLooping(false);
			sound.start();
			isSoundPlayed = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取背景音乐资源
	 * 
	 * @param sourceName
	 */
	public void readMusicSource(String sourceName) {
		try {
			music = new MediaPlayer();
			descriptor = assetManager.openFd(sourceName);
			music.setDataSource(descriptor.getFileDescriptor(),
					descriptor.getStartOffset(), descriptor.getLength());
			music.setVolume(0.3f, 0.3f);
			music.prepare();
			music.setLooping(true);
			music.start();
			isMusicPlayed = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 停止并释放音效资源
	 */
	public void stopSound() {
		if (isSoundPlayed == true) {
			sound.stop();
			sound.release();
		}
	}

	/**
	 * 停止并释放音乐资源
	 */
	public void stopMusic() {
		if (isMusicPlayed == true) {
			music.stop();
			music.release();
		}
	}

	public String getResult() {
		return result;
	}

	public String[] getTexts() {
		return texts;
	}

}
