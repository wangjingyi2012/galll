package com.wjy.gal.util;

public class GalUtil {

	public static final String gameName = "galworld";
	
	public static final int GAME_START = 0; // 游戏开始
	public static final int MAIN_MENU_VIEW = 1; // 游戏主界面
	public static final int GAME_VIEW = 2;// 游戏界面
	public static final int GAME_MENU = 3;// 游戏界面中的菜单
	public static final int GAME_EXIT = 10;// 游戏退出

	// 主菜单右侧按钮
	public static final String[] rightMenuText = new String[] { "新游戏", "存档",
			"设置", "离开" };
	public static final String[] rightMenuTextTips = new String[] {
			"如果人生可以重头开始的话，会怎么样呢...", "你的人生可以存档！", "投胎是个技术活", "~88~" };
	public static final int rightMenuWidthCount = 3;
	public static final int rightMenuHeightCount = 9;
	public static final int rightMenuDistance = 7;
	public static final int rightMenuTextSizeCount = 14;


	// 游戏界面菜单
	public static final String[] gameMenuText = new String[] { "存档", "读档",
			"上一分支", "设置", "主界面" };
	public static final String[] gameMenuTextTips = new String[] { "你的人生可以存档",
			"这辈子也能从新来过的话...", "既然选择了就应该继续走下去...", "系统设置", "主界面" };

}
