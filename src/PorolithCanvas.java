//
// Porolith for i-appli (505i version)
//
// Copyright(C)2004 by Y.Katakura
//
import com.nttdocomo.io.*;
import com.nttdocomo.ui.*;
import com.nttdocomo.net.*;
import javax.microedition.io.*;
import java.util.Random;
import java.io.*;
import java.util.Calendar;

final class PorolithCanvas extends Canvas implements MediaListener{
	private String appVer = "1.7";

	private int NULL = 0;
	private int TITLE = 1;
	private int STARTUP = 2;
	private int DOWN = 3;
	private int GAME = 4;
	private int IDLE = 5;
	private int PAUSE = 6;
	private int RANK = 7;

	private static int event;//イベント
	private static Image[] image=new Image[19];//イメージ
	private static Graphics g;
	private int map[][] = new int[25][25];
	private int block[][] = new int[5][5];
	private int nextBlock[][] = null;
	private int workBlock[][] = null;
	private static Random rnd = new Random();
	private int score;
	private int level_pt;
	private int special_pt;
	private int hiscore=0;
	private volatile int scr = NULL;
	private int x=0,y=0;
	private int dx[] = {1, -1, 0, 0};
	private int dy[] = {0, 0, -1, 1};
	private AudioPresenter ap[] = new AudioPresenter[6];
	private int sg_lvl = 2000;
	private int sg_special = 5000;
	private int sg_sp = 100;
	private int sg_height = 6;
	private int sg_height_rand = 16;
	private int sg_max_num = 23;
	private boolean psound;
	private boolean esound;
	private boolean bmode = true;
	private boolean bmodework = false;

	private int max_num;
	private int drop_left, drop_right;
	private int level;

	private boolean sg_net = true;
	private String neturl = "http://www.tsuchinoko.net/yotan/cgibin/porolith/rank/rank.cgi";
	private String homepage = "http://www.tsuchinoko.net/yotan/porolith/porolith/";
	private int r_score[] = new int[5];
	private String r_name[] = new String[5];
	private int r_rank;
	private int r_max;
	private String r_myname = "";
	private String target;
	private String hosturl;
	private String passcode;
	private String ID;
	private int presscount_r = 0;
	private int presscount_l = 0;
	private String spstr = "scratchpad:///0;pos=";

	// 面積計算用
	private int paint_num;
	private int point;

	private boolean waitflag;
	private volatile int nextsound = -1;
	private volatile int nowsound = -1;
	private volatile boolean soundflag = false;

	private boolean debug = false;

	// スペシャルブロックパターン
	private static final int[][] spPattern = {
		{0, 0, 0, 0, 0},
		{0, 0, 0, 0, 0},
		{0, 0, 8, 0, 0},
		{0, 0, 0, 0, 0},
		{0, 0, 0, 0, 0},
	};

	// 通常のブロックパターン
	private static final int[][][] orgPattern =
	{
		{	//0
			{0, 0, 0, 0, 0},
			{0, 0, 1, 0, 0},
			{0, 1, 1, 0, 0},
			{0, 0, 1, 0, 0},
			{0, 0, 0, 0, 0},
		},
		{	//1
			{0, 0, 0, 0, 0},
			{0, 1, 0, 0, 0},
			{0, 1, 1, 0, 0},
			{0, 1, 1, 1, 0},
			{0, 0, 0, 0, 0},
		},
		{	//2
			{0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0},
			{2, 2, 2, 2, 2},
			{0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0},
		},
		{	//3
			{0, 0, 0, 0, 0},
			{0, 0, 2, 0, 0},
			{0, 2, 2, 2, 0},
			{0, 0, 2, 0, 0},
			{0, 0, 0, 0, 0},
		},
		{	//4
			{0, 0, 0, 0, 0},
			{0, 3, 3, 0, 0},
			{0, 0, 3, 0, 0},
			{0, 3, 3, 0, 0},
			{0, 0, 0, 0, 0},
		},
		{	//5
			{0, 0, 0, 0, 0},
			{0, 0, 3, 0, 0},
			{0, 0, 3, 0, 0},
			{0, 0, 3, 0, 0},
			{0, 0, 0, 0, 0},
		},
		{	//6
			{0, 0, 0, 0, 0},
			{0, 1, 1, 1, 0},
			{0, 0, 0, 0, 0},
			{0, 3, 3, 3, 0},
			{0, 0, 0, 0, 0},
		},
		{	//7
			{0, 0, 0, 0, 0},
			{0, 2, 2, 0, 0},
			{0, 2, 3, 3, 0},
			{0, 0, 3, 3, 0},
			{0, 0, 0, 0, 0},
		},
		{	//8
			{0, 0, 0, 0, 0},
			{0, 1, 1, 1, 0},
			{0, 1, 1, 2, 0},
			{0, 2, 2, 2, 0},
			{0, 0, 0, 0, 0},
		},
		{	//9
			{0, 0, 0, 0, 0},
			{0, 4, 4, 4, 0},
			{0, 2, 4, 2, 0},
			{0, 4, 4, 4, 0},
			{0, 0, 0, 0, 0},
		},
		{	//10
			{0, 0, 0, 0, 0},
			{0, 4, 4, 4, 0},
			{0, 4, 1, 4, 0},
			{0, 4, 4, 4, 0},
			{0, 0, 0, 0, 0},
		},
		{	//11
			{0, 0, 0, 0, 0},
			{0, 5, 0, 5, 0},
			{0, 5, 0, 5, 0},
			{0, 5, 0, 5, 0},
			{0, 0, 0, 0, 0},
		},
		{	//12
			{5, 5, 5, 5, 5},
			{0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0},
			{4, 4, 4, 4, 4},
		},
		{	//13
			{0, 0, 0, 0, 0},
			{0, 5, 5, 0, 0},
			{0, 5, 5, 4, 0},
			{0, 0, 4, 4, 0},
			{0, 0, 0, 0, 0},
		},
		{	//14
			{0, 0, 0, 0, 0},
			{0, 1, 0, 0, 0},
			{0, 1, 0, 5, 0},
			{0, 0, 0, 5, 0},
			{0, 0, 0, 0, 0},
		},
		{	//15
			{0, 0, 0, 0, 0},
			{0, 0, 3, 3, 0},
			{0, 3, 3, 3, 0},
			{0, 3, 3, 0, 0},
			{0, 0, 0, 0, 0},
		},
		{	//16
			{0, 0, 0, 0, 0},
			{0, 3, 0, 3, 0},
			{0, 3, 0, 3, 0},
			{0, 3, 0, 3, 0},
			{0, 0, 0, 0, 0},
		},
		{	//17
			{0, 0, 0, 0, 0},
			{0, 4, 4, 0, 0},
			{0, 4, 4, 4, 0},
			{0, 0, 4, 4, 0},
			{0, 0, 0, 0, 0},
		},
		{	//18
			{0, 0, 0, 0, 0},
			{0, 5, 5, 5, 0},
			{0, 5, 0, 0, 0},
			{0, 5, 5, 5, 0},
			{0, 0, 0, 0, 0},
		},
		{	//19
			{1, 0, 0, 0, 5},
			{1, 0, 0, 0, 5},
			{1, 0, 0, 0, 5},
			{1, 0, 0, 0, 5},
			{1, 0, 0, 0, 5},
		},
		{	//20
			{0, 0, 0, 0, 0},
			{0, 1, 0, 3, 0},
			{0, 1, 0, 3, 0},
			{0, 1, 0, 3, 0},
			{0, 0, 0, 0, 0},
		},
		{	//21
			{0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0},
			{5, 5, 5, 5, 5},
			{0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0},
		},
		{	//22
			{3, 3, 3, 3, 3},
			{0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0},
			{4, 4, 4, 4, 4},
		},
		{	//23
			{0, 0, 0, 0, 0},
			{0, 2, 2, 0, 0},
			{0, 2, 2, 3, 0},
			{0, 0, 3, 3, 0},
			{0, 0, 0, 0, 0},
		},
		{	//24
			{0, 0, 0, 0, 0},
			{0, 1, 1, 0, 0},
			{0, 1, 1, 4, 0},
			{0, 0, 4, 4, 0},
			{0, 0, 0, 0, 0},
		},
		{	//25
			{0, 0, 0, 0, 0},
			{0, 3, 3, 0, 0},
			{0, 3, 3, 5, 0},
			{0, 0, 5, 5, 0},
			{0, 0, 0, 0, 0},
		},
		{	//26
			{0, 0, 0, 0, 0},
			{0, 0, 5, 0, 0},
			{0, 5, 5, 0, 0},
			{0, 0, 5, 0, 0},
			{0, 0, 0, 0, 0},
		},
		{	//27
			{0, 0, 0, 0, 0},
			{0, 4, 0, 0, 0},
			{0, 4, 4, 0, 0},
			{0, 4, 4, 4, 0},
			{0, 0, 0, 0, 0},
		},
		{	//28
			{0, 0, 5, 0, 0},
			{0, 0, 5, 0, 0},
			{0, 0, 5, 0, 0},
			{0, 0, 5, 0, 0},
			{0, 0, 5, 0, 0},
		},
		{	//29
			{0, 0, 3, 0, 0},
			{0, 0, 3, 0, 0},
			{0, 0, 3, 0, 0},
			{0, 0, 3, 0, 0},
			{0, 0, 3, 0, 0},
		},
		{	//30
			{0, 0, 0, 0, 0},
			{0, 0, 1, 0, 0},
			{0, 1, 4, 1, 0},
			{0, 0, 1, 0, 0},
			{0, 0, 0, 0, 0},
		},
	};

	//実行。画面遷移に関する制御を行う。
	void exe() {
		g=getGraphics();
		int i,j;
		MediaImage m;
		int o_width=0,o_height=0;
		Image[] title_image=new Image[2];//タイトル

		scr = TITLE;

		try {

			// 機種判別
			target = System.getProperty("microedition.platform");
			int idx = target.indexOf("505");
			if (idx < 0) {
				idx = target.indexOf("900");
			}
			String maker = "";
			for (i=0;i<idx;i++) {
				maker += target.charAt(i);
			}

			psound = true;
			esound = true;
			waitflag = true;
//			if (maker.equals("X")) {	// iαTool
//				esound = false;
//				psound = false;
//			}
			// NシリーズとP900i(中身はNEC)は100ms以下のwaitが出来ないので落下時のwaitを行わない
			if (maker.equals("N") || target.equals("P900i") ||
				target.equals("P901i") ||
				target.equals("P700i")) {	// 日本電気
				waitflag = false;
//				esound = false;
//				psound = false;
			}
//			if (maker.equals("SO")) {	// ソニー
//			}
//			if (maker.equals("D")) {	// 三菱
//			}
//			if (maker.equals("P")) {	// 松下
//			}
//			if (maker.equals("F")) {	// 富士通
//			}

			hosturl = IApplication.getCurrentApp().getSourceURL();

			// ハイスコア読み込み
			hiscore_load();

			// audio
			for (i=0;i<6;i++) {
				MediaSound sound = MediaManager.getSound("resource:///"+i+".mld");
				sound.use();
				ap[i] = AudioPresenter.getAudioPresenter();
				ap[i].setMediaListener(this);
				ap[i].setSound(sound);
//				if (i==0) { // main bgm
//					ap[i].setAttribute(ap[i].PRIORITY, AudioPresenter.NORM_PRIORITY);
//				} else {
//					ap[i].setAttribute(ap[i].PRIORITY, AudioPresenter.MAX_PRIORITY);
//				}
			}

			//画面クリア
			g.setColor(g.getColorOfName(g.BLACK));
			g.fillRect(0,0,getWidth(),getHeight());


			//画面中心設定
			o_width = (getWidth()-225)/2;
			o_height = (getHeight()-225)/2;
			g.setOrigin(o_width,o_height);


			//フォントサイズ
			g.setFont(Font.getFont(Font.SIZE_SMALL));
			//イメージ読み込み
			for (i=18;i>=0;i--) {
				m=MediaManager.getImage("resource:///"+i+".gif");
				m.use();
				image[i]=m.getImage();
			}
			m=MediaManager.getImage("resource:///"+"title.gif");
			m.use();
			title_image[0]=m.getImage();
			m=MediaManager.getImage("resource:///"+"subtitle.gif");
			m.use();
			title_image[1]=m.getImage();

			load_rank();

//			if (debug && psound && target.equals("N504i")) {
//				for (i=1;i<6;i++) {
//					g.lock();
//					g.setColor(g.getColorOfName(g.BLACK));
//					g.fillRect(0,0,225,225);
//					special_text(28,71,"【ｻｳﾝﾄﾞﾃｽﾄ】",g.getColorOfName(g.BLUE),g.getColorOfName(g.WHITE));
//					special_text(60,71+20, "("+i+")",g.getColorOfName(g.BLUE),g.getColorOfName(g.WHITE));
//					g.unlock(true);
//					playsound(i);
//					try {
//						while (true) {
//							if (!soundflag) {
//								break;
//							}
//							Thread.sleep(100);
//						}
//					} catch (Exception e) {
//					}
//				}
//			}

			usage_put();

			while(true) {
				if (scr == TITLE) {
					// audio
					if (psound) {
//						try {
//							ap[0].play();
//						} catch (Exception e) {
//							;
//						}
						playsound(0);
					}
					g.setOrigin(0,0);

					g.lock();

					g.setColor(g.getColorOfName(g.BLACK));
					g.fillRect(0,0,getWidth(),getHeight());

					o_width = (getWidth()-225)/2;
					o_height = (getHeight()-225)/2;
					g.setOrigin(o_width,o_height);

					setSoftLabel(SOFT_KEY_1,"ｽﾀｰﾄ");
					setSoftLabel(SOFT_KEY_2,"終了");

					//画面クリア
					g.setColor(g.getColorOfName(g.BLACK));
					g.fillRect(0,0,getWidth(),getHeight());

					// タイトル表示
					g.drawImage(title_image[0],0,0);

					menu_draw(psound,bmode);


					// test
//					special_text(26,71,"TEST VERSION",g.getColorOfName(g.BLUE),g.getColorOfName(g.WHITE));

					g.unlock(true);

					//入力待ち
					while(true) {
						keyWait();
						if (esound && event==Display.KEY_LEFT) { // 左カーソル
							psound = true;
//							try {
//								ap[0].play();
//							} catch (Exception e) {
//							}
							playsound(0);
							g.lock();
							menu_draw(psound,bmode);
							g.unlock(true);
						}
						if (esound && event==Display.KEY_RIGHT) { // 右カーソル
							psound = false;
//							try {
//								ap[0].stop();
//							} catch (Exception e) {
//							}
							stopsound(0);
							g.lock();
							menu_draw(psound,bmode);
							g.unlock(true);
						}
						if (event==Display.KEY_UP) {
							bmode = true;
							g.lock();
							menu_draw(psound,bmode);
							g.unlock(true);
						}
						if (event==Display.KEY_DOWN) {
							bmode = false;
							g.lock();
							menu_draw(psound,bmode);
							g.unlock(true);
						}
						if (event==Display.KEY_SOFT1) { // 左ボタン
							if (psound) {
//								try {
//									ap[0].stop();
//								} catch (Exception e) {
//								}
								stopsound(0);
							}
							scr = STARTUP;
							break;
						}
						if (event==Display.KEY_SELECT && sg_net) { // 中央
							if (psound) {
//								try {
//									ap[0].stop();
//								} catch (Exception e) {
//								}
								stopsound(0);
							}
							scr = RANK;
							break;
						}
						if (event==Display.KEY_ASTERISK && sg_net) { // *
							if (psound) {
//								try {
//									ap[0].stop();
//								} catch (Exception e) {
//								}
								stopsound(0);
							}
//							special_text(32,60,"【準備中】",g.getColorOfName(g.WHITE),g.getColorOfName(g.GREEN));
							hiscore_save();
							save_rank();
							IApplication.getCurrentApp().launch(IApplication.LAUNCH_BROWSER, new String[]{homepage});
							Thread.sleep(10);
							break;
						}
					}
				}
				// ゲーム画面
				if (scr == STARTUP) {
					max_num = sg_max_num;
					setSoftLabel(SOFT_KEY_1,"停止");
					setSoftLabel(SOFT_KEY_2,"ﾀｲﾄﾙ");
					g.lock();
					g.setColor(g.getColorOfName(g.BLACK));
					g.fillRect(0,0,225,225);
					for (i = 0; i < 25; i ++) {
						for (j=0;j<6;j++) {
							map[j][i] = 9;
							map[j+14][i] = 9;
						}
						chput(0,i,9);
						chput(14,i,9);
						for (j = 1; j < 14; j ++) {
							chput(j,i,(i != 24)? 0:9);
							map[j+5][i] = (i != 24)? 0:9;
						}
					}
					g.drawImage(title_image[1],15*9,0);


					g.setColor(g.getColorOfName(g.YELLOW));
					putstr("Next:",18,11);

					g.setColor(g.getColorOfName(g.YELLOW));
					putstr("Score:",18,3);
					score = 0;
					score_put();

					g.setColor(g.getColorOfName(g.YELLOW));
					putstr("Hi-Score:",18,6);
					hiscore_put();
					special_pt = sg_special;
					level_pt = sg_lvl;
					level = 1;
					level_put(level);

					block_select(level);
					next_copy();
					block_copy();
					block_select(level);
					next_copy();
					next_put();
					g.unlock(true);
					x = 5;
					y = -2;
					block_put(x,y);
					scr = GAME;
				}

				//ダウン
				if (scr == DOWN) {
					if (block_check(x,y+1)) {
						game_main();
						if (scr != IDLE) scr = GAME;
						continue;
					}
					// audio
					if (psound) {
//						try {
//							ap[2].play();
//						} catch (Exception e) {
//						}
						playsound(2);
					}
					while(true) {
						g.lock();
						block_hide(x,y);
						if (block_check(x,y+1)) {
							block_put(x,y);
							g.unlock(true);
							scr = GAME;
							break;
						} else {
							y++;
							block_put(x,y);
							g.unlock(true);
							if (waitflag) {
								Thread.sleep(10);
							}
						}
					}
				}
				// ゲーム画面
				if (scr == GAME) {
					delay(level);
					if (scr != GAME) continue;
					game_main();
				}
				// ゲームオーバ
				if (scr == IDLE) {
					hiscore_save();
					setSoftLabel(SOFT_KEY_1,"ｽﾀｰﾄ");
					setSoftLabel(SOFT_KEY_2,"ﾀｲﾄﾙ");
					for (j=23;j>=0;j--) {
						g.lock();
						for (i=1;i<14;i++) {
							if (map[i+5][j]!=0) {
								chput(i,j,11);
							}
						}
						g.unlock(true);
						try {
							Thread.sleep(40);
						} catch (Exception e) {
							;
						}
					}
					putclr("           ",3,5);
					g.setColor(g.getColorOfName(g.YELLOW));
					putstr(" GAME OVER ",3,5);

					while(true) {
						keyWait();
						if (event==Display.KEY_SOFT1) { // 左ボタン
							scr = STARTUP;
							break;
						}
						if (event==Display.KEY_SOFT2) { // 左ボタン
							scr = TITLE;
							break;
						}
					}
				}
				// ランキング画面
				if (scr == RANK) {
					put_rank_screen();
					while(true) {
						keyWait();
						if (event==Display.KEY_SELECT) {
							scr = TITLE;
							break;
						}
						if (event==Display.KEY_SOFT1) {
							boolean res;
							special_text(85,120,"【通信中】",g.getColorOfName(g.WHITE),g.getColorOfName(g.GREEN));
							res = get_rank();
							if (res) {
								save_rank();
							}
							g.lock();
							put_rank_screen();
							g.unlock(true);
							if (!res) {
								special_text(70,120,"ﾃﾞｰﾀ取得失敗!",g.getColorOfName(g.WHITE),g.getColorOfName(g.RED));
							}
						}
						if (event==Display.KEY_SOFT2) {
							boolean res;
							boolean can;

							can = input_name();
							Display.setCurrent(this);
							g.lock();
							put_rank_screen();
							g.unlock(true);
							if (!can) {
								if (r_myname.equals("ID変更")) {
									ID = getID();
								}
								if (r_myname.equals("ﾊｲｽｺｱｸﾘｱ")) {
									hiscore = 0;
									hiscore_save();
								}
								special_text(85,120,"【通信中】",g.getColorOfName(g.WHITE),g.getColorOfName(g.GREEN));
								res = set_rank();
								if (res) {
									save_rank();
								}
								g.lock();
								put_rank_screen();
								g.unlock(true);
								if (!res) {
									special_text(70,120,"ﾃﾞｰﾀ取得失敗!",g.getColorOfName(g.WHITE),g.getColorOfName(g.RED));
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			Dialog dialog=new Dialog(Dialog.DIALOG_ERROR,"エラー");
			dialog.setText("何らかのエラーが発生しました");
			dialog.show();
			IApplication.getCurrentApp().terminate();
		}
	}

	// ゲームメイン処理
	public synchronized void game_main() {
		int bn;
		try {
			if (!block_check(x,y+1)) {
				g.lock();
				block_hide(x,y);
				y++;
				block_put(x,y);
				g.unlock(true);
			} else {
				presscount_r = 0;
				presscount_l = 0;
				block_keep(x,y,true);
				if (psound) {
//					try {
//						ap[3].play();
//					} catch(Exception e) {
//					}
					playsound(3);
				}
				Thread.sleep(500);
				bn = 0;
				while (block_clear()) {
					bn += 500;
					if (bn != 500) {
						if (psound) {
//							try {
//								ap[5].play();
//							} catch(Exception e) {
//							}
							playsound(5);
						}
						score_add(bn);
						g.drawImage(image[17],18*9,20*9);
					}
					block_drop();
					if (bn != 500) {
						Thread.sleep(700);
						g.lock();
						next_put();
						g.unlock(true);
					}
				}
				if (score > special_pt) {
					special_pt += sg_special;
					block_select(-1);
				} else {
					if (getRandomInt(sg_sp)==0) {
						block_select(-1);
					} else {
						block_select(level);
					}
				}
				if (y < sg_height && getRandomInt(sg_height_rand) == 0) {
					block_select(-1);
				}
	
				x = 5;
				y = -2; // 12/19
				block_copy();
				next_copy();
				g.lock();
				next_put();
				block_put(x,y);
				g.unlock(true);
				if (block_check(x,y)) {
					block_keep(x,y,false); // 12/19
					scr = IDLE;
					return;
				}
			}
		} catch (Exception e) {
			;
		}
	}

	public String getID() {
		String id;
		Calendar calendar = Calendar.getInstance();
		id =	itoa(calendar.get(Calendar.YEAR),4)+
				itoa(calendar.get(Calendar.MONTH)+1,2)+
				itoa(calendar.get(Calendar.DATE),2)+"-"+
				itoa(calendar.get(Calendar.HOUR_OF_DAY),2)+
				itoa(calendar.get(Calendar.MINUTE),2)+
				itoa(calendar.get(Calendar.SECOND),2)+"-"+
				itoa(getRandomInt(10000000),8);
		return id;
	}

	public String itoa(int n, int k) {
		int i;
		Integer sc = new Integer(n);
		String wk = sc.toString();
		int len = wk.length();
		if (len < k) {
			for (i = 0; i < k-len; i ++) {
				wk = "0" + wk;
			}
		}
		sc = null;
		return wk;
	}

	public void menu_draw(boolean s, boolean b){
		g.setColor(g.getColorOfRGB(0,0,0));
		g.fillRect(0,120,200,70);
		if (esound) {
			g.drawImage(image[14],6*8,9*15);

			g.setColor(g.getColorOfRGB(255,128,128));
			putstr("SOUND:",8,9);

			if (s) {
				g.setColor(g.getColorOfRGB(255,220,220));
				putstr("ON",14,9);
			} else {
				g.setColor(g.getColorOfRGB(255,200,200));
				putstr("OFF",14,9);
			}
		}

		g.drawImage(image[13],6*8,10*15);
		g.setColor(g.getColorOfRGB(255,128,128));
		putstr("BLOCK:",8,10);
		g.setColor(g.getColorOfRGB(255,128,128));
		if (b) {
			g.setColor(g.getColorOfRGB(255,220,220));
			putstr("FAST",14,10);
		} else {
			g.setColor(g.getColorOfRGB(255,200,200));
			putstr("SLOW",14,10);
		}

		if (sg_net) {
			g.drawImage(image[15],6*8,11*15);
			g.setColor(g.getColorOfRGB(255,128,128));
			putstr("NET RANKING",8,11);

			g.drawImage(image[18],6*8,12*15);
			g.setColor(g.getColorOfRGB(255,128,128));
			putstr("to Web Site",8,12);

		}
	}

	public void delay(int lv) {
		int base = 100;
		int i;
		try {
			base += (1050-(lv*50));
			if (base < 100) base = 100;

			for (i=0;i<base;i+=100) {
				Thread.sleep(100);
				if (scr != GAME) return;
				// 連続押し処理。500ms押し続けると左右に動き続けるようにする
				if (((1<<Display.KEY_RIGHT)&getKeypadState())!=0 ||
				    ((1<<Display.KEY_6)&getKeypadState())!=0) {
					presscount_r ++;
					if (presscount_r > 5) {
						processEvent(Display.KEY_PRESSED_EVENT, Display.KEY_RIGHT);
					}
				} else {
					presscount_r = 0;
				}

				if (((1<<Display.KEY_LEFT)&getKeypadState())!=0 ||
				    ((1<<Display.KEY_4)&getKeypadState())!=0) {
					presscount_l ++;
					if (presscount_l > 5) {
						processEvent(Display.KEY_PRESSED_EVENT, Display.KEY_LEFT);
					}
				} else {
					presscount_l = 0;
				}

				if (bmode) {
					if (bmodework) {
						if (((1<<Display.KEY_3)&getKeypadState())!=0) return;
					} else {
						if (scr == DOWN) {
							return;
						}
					}
				} else {
					if (bmodework) {
						if (scr == DOWN) {
							return;
						}
					} else {
						if (((1<<Display.KEY_DOWN)&getKeypadState())!=0) return;
						if (((1<<Display.KEY_8)&getKeypadState())!=0) return;
					}
				}
			}
		} catch (Exception e) {
			;
		}
		return;
	}

	public void stopsound(int s) {
		nextsound = -1;
		soundflag = false;
		try {
			ap[s].stop();
		} catch (Exception e) {
		}
	}

	// サウンド再生中に再び再生させるとN504系でexceptionが発生してしまうのでその対処
	public void playsound(int s) {
		if (soundflag) {
			nextsound = s;
			try {
				ap[nowsound].stop();
			} catch (Exception e) {
			}
			return;
		}
		try {
			soundflag = true;
			nowsound = s;
			ap[s].play();
		} catch (Exception e) {
			soundflag = false;
		}
	}

	public void mediaAction(MediaPresenter source,int type, int param) {
		if (type == AudioPresenter.AUDIO_PLAYING) {
			soundflag = true;
		}
		if (type == AudioPresenter.AUDIO_COMPLETE || type == AudioPresenter.AUDIO_STOPPED) {
			if (nextsound >= 0) {
				try {
					nowsound = nextsound;
					ap[nextsound].play();
				} catch (Exception e) {
				}
				nextsound = -1;
			} else {
				nextsound = -1;
				soundflag = false;
			}
		}
	}

	public void level_put(int lv) {
		putclr("   ",24,9);
		g.setColor(g.getColorOfName(g.YELLOW));
		putstr("Level:",18,9);
		g.setColor(g.getColorOfName(g.WHITE));
		putstr(""+lv,24,9);
		return;
	}

	public void score_put() {
		int i;
		Integer sc = new Integer(score);
		String wk = sc.toString();
		int len = wk.length();
		if (len < 9) {
			for (i = 0; i < 8-len; i ++) {
				wk = " " + wk;
			}
		}
		g.lock();
		putclr("        ",19,4);
		g.setColor(g.getColorOfName(g.WHITE));
		putstr(wk,19,4);
		g.unlock(true);
		sc = null;
		return;
	}

	public void hiscore_put() {
		int i;
		Integer sc = new Integer(hiscore);
		String wk = sc.toString();
		int len = wk.length();
		if (len < 9) {
			for (i = 0; i < 8-len; i ++) {
				wk = " " + wk;
			}
		}
		g.lock();
		putclr("        ",19,7);
		g.setColor(g.getColorOfName(g.WHITE));
		putstr(wk,19,7);
		g.unlock(true);
		sc = null;
		return;
	}

	private static int getRandomInt(int limit) {
		return Math.abs(rnd.nextInt() % limit);
	}

	public void chput(int x, int y, int ch) {
		g.drawImage(image[ch], x*9, y*9);
		return;
	}

	//イベント
	public void keyPressed(int key) {
	}

	public synchronized void processEvent(int type, int param) {
		if (type==Display.KEY_PRESSED_EVENT) {
			event=param;

			switch(event) {
			case Display.KEY_POUND:
				if (scr == GAME && esound) {
					if (psound) {
						psound = false;
					} else {
						psound = true;
					}
				}
				break;
			case Display.KEY_SOFT2:
				if (scr == RANK || scr == IDLE) {
					return;
				}
				hiscore_save();
				if (scr == GAME || scr == PAUSE) {
					scr = TITLE;
					return;
				}
//				special_text(32,60,"【終了中】",g.getColorOfName(g.WHITE),g.getColorOfName(g.GREEN));
				save_rank();
				IApplication.getCurrentApp().terminate();
				break;
			case Display.KEY_SOFT1:
				if (scr == GAME) {
					setSoftLabel(SOFT_KEY_1,"再開");
					scr = PAUSE;
					event=-222;
				} else {
					if (scr == PAUSE) {
						setSoftLabel(SOFT_KEY_1,"停止");
						scr = GAME;
						event=-222;
					}
				}
				break;
			case Display.KEY_RIGHT:
			case Display.KEY_6:
				if (scr == GAME) {
					if (!block_check(x+1,y)) {
						g.lock();
						block_hide(x,y);
						x ++;
						block_put(x,y);
						g.unlock(true);
					}
					event=-222;
				}
				break;
			case Display.KEY_LEFT:
			case Display.KEY_4:
				if (scr == GAME) {
					if (!block_check(x-1,y)) {
						g.lock();
						block_hide(x,y);
						x --;
						block_put(x,y);
						g.unlock(true);
					}
					event=-222;
				}
				break;
			case Display.KEY_SELECT:
			case Display.KEY_UP:
			case Display.KEY_5:
			case Display.KEY_2:
			case Display.KEY_ASTERISK:
				if (scr == GAME) {
					// とりあえず回す
					block_ring_right();
					if (block_check(x,y)) {
						block_ring_left();
						event=-222;
						return;
					}

					block_ring_left();
					// audio
					if (psound) {
//						try {
//							ap[1].play();
//						} catch (Exception e) {
//						}
						playsound(1);
					}
					g.lock();
					block_hide(x,y);
					block_ring_right();
					if (!block_check(x,y)) {
						block_put(x,y);
					} else {
						block_ring_left();
						block_put(x,y);
					}
					g.unlock(true);
					event=-222;
				}
				break;
			case Display.KEY_DOWN:
			case Display.KEY_8:
				if (scr == GAME) {
					bmodework=false;
					if (bmode) {
						scr = DOWN;
						event = -222;
					} else {
						return;
					}
				}
				break;
			case Display.KEY_3:
				if (scr == GAME) {
					bmodework=true;
					if (!bmode) {
						scr = DOWN;
						event = -222;
					} else {
						return;
					}
				}
				break;
			}
		}
	}

	//キーウェイト
	void keyWait() throws Exception {
		event=-222;
		while (event<0) Thread.sleep(300);
	}

	//描画
	public void paint(Graphics g) {
	}

	public void next_copy() {
		nextBlock = workBlock;
		return;
	}

	public void block_copy() {
		for (int i = 0; i < 5; i++) {
			System.arraycopy(nextBlock[i], 0, block[i], 0, nextBlock[i].length);
		}
		return;
	}

	public void next_put() {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				chput(18 + i, 20 + j, nextBlock[i][j] != 0 ? nextBlock[i][j] : 12);
			}
		}
		return;
	}

	public void block_put(int x,int y) {
		int i,j;

		for (i=0; i<5; i++) {
			for (j=0; j<5; j++) {
				if (block[i][j]!=0) {
					// 12/19
					if (y+j < 0) continue;
					chput(x+i,y+j,block[i][j]);
				}
			}
		}
		return;
	}

	public void block_hide(int x,int y) {
		int i,j;

		for (i=0; i<5; i++) {
			for (j=0; j<5; j++) {
				// 12/19
				if (y+j < 0) continue;
				if (block[i][j]!=0) {
					chput(x+i,y+j,0);
				}
			}
		}
		return;
	}

	public void score_add(int sc) {
		score += sc;
		if (score > hiscore) {
			hiscore=score;
			hiscore_put();
		}
		score_put();
	}

	public void block_ring_left() {
		int i, j;
		int buf;
		for (j = 0; j < 2; j++) {
			for (i = j; i < 4 - j; i++) {
				buf = block[4 - j][i];
				block[4 - j][i] = block[4 - i][4 - j];
				block[4 - i][4 - j] = block[j][4 - i];
				block[j][4 - i] = block[i][j];
				block[i][j] = buf;
			}
		}
	}

	public void block_ring_right()
	{
		int i, j;
		int buf;
		for (j = 0; j < 2; j++) {
			for (i = j; i < 4 - j; i++) {
				buf = block[i][j];
				block[i][j] = block[j][4 - i];
				block[j][4 - i] = block[4 - i][4 - j];
				block[4 - i][4 - j] = block[4 - j][i];
				block[4 - j][i] = buf;
			}
		}
	}

	public boolean block_check(int x, int y) {
		int i, j;
		for (i = 0; i < 5; i++) {
			for (j = 0; j < 5; j++) {
				// 12/19
				if (block[i][j] != 0) {
					if (x + i <= 0 || x + i >= 14) return true;
				}
				if (y+j < 0) continue;

				if (block[i][j] != 0 && map[x + 5 + i][y + j] != 0) {
						return true;
				}
			}
		}
		return false;
	}

	public void block_keep(int x, int y, boolean spf) {
		int bn;
		int sp;
		int i, j;

		if (spf) { // 12/19
			if (block[2][2]==(8)) {
				block_hide(x, y);
				sp = map[x+2+5][y + 3];
				/* 消去対象となるブロック検索	*/
				for (j = 1; j < 15 - 1; j++) {
					for (i = 0; i < 25; i++) {
						if (map[j+5][i] == sp)
							map[j+5][i] |= 0x80;
					}
				}
				bn = 0;
				while (block_clear()) {
					bn += 500;

					if (bn != 500) {
						if (psound) {
//							try {
//								ap[5].play();
//							} catch (Exception e) {
//							}
							playsound(5);
						}
						score_add(bn);
						g.drawImage(image[17],18*9,20*9);
					}
					block_drop();
					if (bn != 500) {
						try {
							Thread.sleep(700);
						} catch (Exception e) {
							;
						}
						next_put();
					}
				}
				return;
			}
		}

		for (i = 0; i < 5; i++) {
			for (j = 0; j < 5; j++) {
				// 12/19
				if (y+j < 0) continue;

				if (block[i][j] != 0)
					map[x+5+i][y+j]=block[i][j];
			}
		}
		block_line(x,y);
	}

	public void block_line(int x, int y) {
		int i,j,k;
		g.lock();
		for (i = x - 1; i < x + 6; i++) {
			for (j = y - 1; j < y + 6; j++) {
				if (j >= 0 && j < 24 && i > 0 && i < 15 - 1) {
					if (map[i+5][j] != 0) {
						chput(i, j, map[i+5][j]);
						for (k = 0; k < 4; k++) {
							if (j+dy[k] < 0) {
								block_line2(i,j,k);
							} else {
								if (map[i+5][j] != map[i+dx[k]+5][j+dy[k]]) {
									block_line2(i, j, k);
								}
							}
						}
					}
				}
			}
		}
		g.unlock(true);
		return;
	}

	public void block_line2(int x, int y, int k) {
		g.setColor(g.getColorOfName(g.WHITE));
		switch(k) {
		case 0:
			g.drawLine(x*9+9-1,y*9,x*9+9-1,y*9+9-1);
			break;
		case 1:
			g.drawLine(x*9,y*9,x*9,y*9+9-1);
			break;
		case 2:
			g.drawLine(x*9,y*9,x*9+9-1,y*9);
			break;
		case 3:
			g.drawLine(x*9,y*9+9-1,x*9+9-1,y*9+9-1);
			break;
		}
		return;
	}

	public void special_text(int xx,int yy, String str, int fg, int bg) {
		int i,j;
		g.setColor(bg);
		for (i=0;i<3;i++) {
			for (j=0;j<3;j++) {
				g.drawString(str,xx+i,yy+j);
			}
		}
		g.setColor(fg);
		g.drawString(str,xx+1,yy+1);
		return;
	}

	public void put_rank_screen() {
		int o_width, o_height;

		setSoftLabel(SOFT_KEY_1,"参照");
		setSoftLabel(SOFT_KEY_2,"応募");

		g.lock();

		g.setOrigin(0,0);

		g.setColor(g.getColorOfName(g.BLACK));
		g.fillRect(0,0,getWidth(),getHeight());

		o_width = (getWidth()-225)/2;
		o_height = (getHeight()-225)/2;
		g.setOrigin(o_width,o_height);

		special_text(60,14,"Net Ranking",g.getColorOfName(g.BLUE),g.getColorOfName(g.WHITE));

		g.setColor(g.getColorOfRGB(0,0,0));
		g.drawImage(image[15],20+22,225-1-11);
		g.setColor(g.getColorOfRGB(255,128,128));
		g.drawString("RETURN TITLE",20+22+16,225-1);

		display_rank();
		g.unlock(true);
	}

	public void save_rank() {
		int i;
		int offset;
		OutputStream out;

		try {
			out = Connector.openOutputStream(spstr+"8");
			out.write((r_rank >> 24) & 0xff);
			out.write((r_rank >> 16) & 0xff);
			out.write((r_rank >> 8) & 0xff);
			out.write(r_rank & 0xff);

			out.write((r_max >> 24) & 0xff);
			out.write((r_max >> 16) & 0xff);
			out.write((r_max >> 8) & 0xff);
			out.write(r_max & 0xff);

			out.close();

			for (i=0;i<5;i++) {
				offset = 16+(i*36);
				out = Connector.openOutputStream(spstr+offset);
				out.write((r_score[i] >> 24) & 0xff);
				out.write((r_score[i] >> 16) & 0xff);
				out.write((r_score[i] >> 8) & 0xff);
				out.write(r_score[i] & 0xff);
				out.close();

				offset = 20+(i*36);
				DataOutputStream dout = Connector.openDataOutputStream(spstr+offset);
				dout.writeUTF(r_name[i]);
				dout.close();
			}

			// ID
			offset = 20+5*36;
			DataOutputStream dout = Connector.openDataOutputStream(spstr+offset);
			dout.writeUTF(ID);
			dout.close();


		} catch (IOException e) {
			;
		}

	}

	public void load_rank() {
		int i;
		int offset;
		InputStream in;

		try {
			in = Connector.openInputStream(spstr+"8");
			r_rank = in.read() << 24;
			r_rank |= (in.read() << 16);
			r_rank |= (in.read() << 8);
			r_rank |= in.read();

			r_max = in.read() << 24;
			r_max |= (in.read() << 16);
			r_max |= (in.read() << 8);
			r_max |= in.read();

			in.close();

			for (i=0;i<5;i++) {
				offset = 16+(i*36);
				in = Connector.openInputStream(spstr+offset);
				r_score[i] = in.read() << 24;
				r_score[i] |= (in.read() << 16);
				r_score[i] |= (in.read() << 8);
				r_score[i] |= in.read();
				in.close();

				offset = 20+(i*36);
				DataInputStream din = Connector.openDataInputStream(spstr+offset);
				r_name[i] = din.readUTF();
				din.close();

			}

			// ID
			offset = 20+5*36;
			DataInputStream din = Connector.openDataInputStream(spstr+offset);
			ID = din.readUTF();
			din.close();
			if (ID.equals("") || 0 > ID.indexOf("-")) {
				ID = getID();
			}
		} catch (IOException e) {
			;
		}
	}

	public boolean input_name() {
		boolean st;
		inputPanel input = new inputPanel();
		input.start();
		st = input.getStatus();
		input = null;
		return st;
	}

	public void display_rank() {
		int i,j;
		String wk;
		int len;
		int col;

		g.setColor(g.getColorOfName(g.YELLOW));
		putstr("#",1,3);

		g.setColor(g.getColorOfName(g.YELLOW));
		putstr("SCORE",5,3);

		g.setColor(g.getColorOfName(g.YELLOW));
		putstr("NAME",16,3);
		for (i=0;i<5;i++) {
			wk = String.valueOf(r_score[i]);
			len = wk.length();
			if (len < 8) {
				for (j = 0; j < 8-len; j ++) {
					wk = " " + wk;
				}
			}

			col = 255 - (i*20);
			g.setColor(g.getColorOfRGB(col,col,col));

			putstr(String.valueOf(i+1),1,i+4);
			putstr(wk,5,i+4);
			putstr(r_name[i],16,i+4);
		}

		if (r_rank != 0) {
			wk = " Rank:"+String.valueOf(r_rank)+"/"+String.valueOf(r_max);
		} else {
			wk = " YourRank:unknown";
		}
		g.setColor(g.getColorOfRGB(0,255,128));
		putstr(wk,4,11);

		wk = " Hi-Score:"+String.valueOf(hiscore);
		g.setColor(g.getColorOfRGB(0,255,128));
		putstr(wk,4,12);
	}

	public boolean get_rank() {
		HttpConnection wi_connect;
		HttpConnection wo_connect;
		String url = new String(neturl);
		String w_buffer = "";
		String sc;
		int w_char;
		try { 
			wi_connect = (HttpConnection)Connector.open(url,Connector.READ_WRITE,true);
		}
		catch(Exception e) {
			return false;
		}
		try {
			wi_connect.setRequestMethod(HttpConnection.POST);
			wi_connect.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			OutputStream wo_stream = wi_connect.openOutputStream();
			OutputStreamWriter wo_buffer = new OutputStreamWriter(wo_stream);

			wo_buffer.write("action=get&");
			wo_buffer.write("userid="+ID+"&");
			sc = String.valueOf(hiscore);
			wo_buffer.write("score="+sc);

			wo_buffer.close();
			wo_stream.close();

			wi_connect.connect();
			InputStream wi_stream = wi_connect.openInputStream();
			InputStreamReader wi_buffer = new InputStreamReader(wi_stream);
			StringBuffer w_StringBuffer = new StringBuffer();
			while ((w_char = wi_buffer.read()) != -1) {
				w_StringBuffer.append((char)w_char);
			}
			w_buffer = (String)w_StringBuffer.toString();
			wi_stream.close();
			wi_connect.close();
			if (w_buffer.length() == 0) return false;
		}
		catch(Throwable e) {
			return false;
		}

		int i,j;
		String wkbuf;
		int idx = 0;
		for (i=0;i<5;i++) {
			wkbuf = "";
			for (j=0;j<8;j++) {
				wkbuf += w_buffer.charAt(idx);
				idx ++;
			}
			r_score[i] = Integer.parseInt(wkbuf);
			idx ++; // カンマ

			wkbuf = "";
			while (true) {
				if (w_buffer.charAt(idx) == '\n') break;
				wkbuf += w_buffer.charAt(idx);
				idx ++;
			}
			r_name[i] = wkbuf;
			idx ++; // 改行
		}
		wkbuf = "";
		for (j=0;j<8;j++) {
			wkbuf += w_buffer.charAt(idx);
			idx ++;
		}
		r_rank = Integer.parseInt(wkbuf);
		idx ++; // 改行
		wkbuf = "";
		for (j=0;j<8;j++) {
			wkbuf += w_buffer.charAt(idx);
			idx ++;
		}
		r_max = Integer.parseInt(wkbuf);
		return true;
	}

	public boolean set_rank() {
		HttpConnection wi_connect;
		HttpConnection wo_connect;
		String url = new String(neturl);
		String w_buffer = "";
		String sc;
		int w_char;
		try { 
			wi_connect = (HttpConnection)Connector.open(url,Connector.READ_WRITE,true);
		}
		catch(Exception e) {
			return false;
		}
		try {
			wi_connect.setRequestMethod(HttpConnection.POST);
			wi_connect.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			OutputStream wo_stream = wi_connect.openOutputStream();
			OutputStreamWriter wo_buffer = new OutputStreamWriter(wo_stream);

			wo_buffer.write("action=set&");
			sc = String.valueOf(hiscore);
			wo_buffer.write("score="+sc+"&");
			wo_buffer.write("name="+URLEncoder.encode(r_myname)+"&");
			wo_buffer.write("userid="+ID+"&");
			wo_buffer.write("appver="+appVer+"&");
			wo_buffer.write("target="+target);
			

			wo_buffer.close();
			wo_stream.close();

			wi_connect.connect();
			InputStream wi_stream = wi_connect.openInputStream();
			InputStreamReader wi_buffer = new InputStreamReader(wi_stream);
			StringBuffer w_StringBuffer = new StringBuffer();
			while ((w_char = wi_buffer.read()) != -1) {
				w_StringBuffer.append((char)w_char);
			}
			w_buffer = (String)w_StringBuffer.toString();
			wi_stream.close();
			wi_connect.close();
			if (w_buffer.length() == 0) return false;
		}
		catch(Throwable e) {
			return false;
		}

		int i,j;
		String wkbuf;
		int idx = 0;
		for (i=0;i<5;i++) {
			wkbuf = "";
			for (j=0;j<8;j++) {
				wkbuf += w_buffer.charAt(idx);
				idx ++;
			}
			r_score[i] = Integer.parseInt(wkbuf);
			idx ++; // カンマ

			wkbuf = "";
			while (true) {
				if (w_buffer.charAt(idx) == '\n') break;
				wkbuf += w_buffer.charAt(idx);
				idx ++;
			}
			r_name[i] = wkbuf;
			idx ++; // 改行
		}
		wkbuf = "";
		for (j=0;j<8;j++) {
			wkbuf += w_buffer.charAt(idx);
			idx ++;
		}
		r_rank = Integer.parseInt(wkbuf);
		idx ++; // 改行
		wkbuf = "";
		for (j=0;j<8;j++) {
			wkbuf += w_buffer.charAt(idx);
			idx ++;
		}
		r_max = Integer.parseInt(wkbuf);
		return true;
	}

	public void usage_put() {
		g.lock();
		g.setColor(g.getColorOfRGB(0,0,60));
		g.fillRect(0,0,225-1,225-1);

		g.setColor(g.getColorOfRGB(255,255,255));

		g.drawRect(0,0,225-1,225-1);

		g.setColor(g.getColorOfRGB(255,255,0));
		putstr("      POROLITH",4,2);
//		putstr("Created by Y.Katakura",4,3);
//		g.drawString("                     ",1,16+ 2*15);
		putstr("   Special Thanks:",4,7);
		putstr(" TAKA,RYOUSHI,Chaka",4,8);
		putstr("  ...and all gamers!",4,9);
//		g.drawString("                     ",1,16+ 6*15);
		putstr("Copyright 2005 Yotan",4,12);
		putstr("   Allrights Reserved",4,13);


		g.unlock(true);
		try {
			Thread.sleep(3000);
		} catch(Exception e) {
			;
		}

		g.setColor(g.getColorOfName(g.BLACK));
		g.fillRect(0,0,225,225);
	}

	public void putstr(String str, int x, int y) {
		g.drawString(str,x*8,y*15+14);
		return;
	}

	public void putclr(String str, int x, int y) {
		int sz = str.length();
		g.setColor(g.getColorOfName(g.BLACK));
		g.fillRect(x*8,y*15,sz*8,14);
		return;
	}

	public void hiscore_save() {
		int wk;

		try {
			OutputStream out = Connector.openOutputStream(spstr+"0");
			out.write((hiscore >> 24) & 0xff);
			out.write((hiscore >> 16) & 0xff);
			out.write((hiscore >> 8) & 0xff);
			out.write(hiscore & 0xff);

			out.write(0xff);
			if (bmode) {
				wk = 0xff;
			} else {
				wk = 0x00;
			}
			out.write(wk);
			if (psound) {
				wk = 0xff;
			} else {
				wk = 0x00;
			}
			out.write(wk);
			out.close();
		} catch (IOException e) {
			;
		}
	}

	public boolean hiscore_load() {
		int wk;
		boolean rtn = false;
		try {
			InputStream in = Connector.openInputStream(spstr+"0");
			hiscore = in.read() << 24;
			hiscore |= (in.read() << 16);
			hiscore |= (in.read() << 8);
			hiscore |= in.read();

			rtn = false;
			wk = in.read();
			if (wk == 0xff) {
				wk = in.read();
				if (wk == 0xff) {
					bmode = true;
				} else {
					bmode = false;
				}
				wk = in.read();
				if (wk == 0xff) {
					psound = true;
				} else {
					psound = false;
				}
				rtn = true;
			}
			in.close();
		} catch (IOException e) {
			hiscore = 0;
		}
		return rtn;
	}

	public void block_drop() {
		boolean end_flag = false;
		int x, y;
		int k;
		/* 落ちきるまでループ	*/
		do {
			end_flag = true;
			g.lock();
			for (y = 25 - 2; y >= 0; y--) {
				for (x = drop_left; x <= drop_right; x++) {
					/* 対象ブロックの一つ下がnullなら落とす	*/
					if (map[x+5][y] != 0xff && map[x+5][y] != 0 && map[x+5][y+1] == 0x00) {
						map[x+5][y+1] = map[x+5][y];
						map[x+5][y] = 0;
						chput(x, y, 0);
						chput(x, y + 1, map[x+5][y+1]);
						end_flag = false;
					}
				}
			}
			g.unlock(true);
			try {
				Thread.sleep(40);
			} catch (Exception e) {
				;
			}

		} while (!end_flag);
		/* 落ちきったら枠を書く	*/
		g.lock();
		for (y = 0; y < 24; y++) {
			for (x = 1; x < 15; x++) {
				if (y > 0 && y < 24 && x > 0 && x < 15 - 1) {
					chput(x, y, map[x+5][y]);
					for (k = 0; k < 4; k++) {
						if (map[x+5][y] != map[x + dx[k]+5][y + dy[k]] && map[x+5][y] != 0) {
							block_line2(x, y, k);
						}
					}
				}
			}
		}
		g.unlock(true);
	}

	public boolean block_clear() {
		boolean flag, double_flag;
		int x, y;
		int pt;
		int bflag;

		flag = false;
		double_flag = false;
		bflag = 0x00;
		drop_left = 15;
		drop_right = 0;

		block_paint();
		for (x = 1; x < 15 - 1; x++) {
			for (y = 0; y < 24; y++) {
				if (map[x+5][y] != 0xff && (map[x+5][y] & 0x80) != 0x00) {
					if (drop_right <= x)
						drop_right = x;
					if (drop_left >= x)
						drop_left = x;
				}
			}
		}

		pt = 0;
		/* クリアされるブロックがあったか？	*/
		g.lock();
		for (x = 1; x < 15 - 1; x++) {
			for (y = 0; y < 25 - 1; y++) {
				if (map[x+5][y] != 0xff && (map[x+5][y] & 0x80) != 0x00) {
					if (bflag == 0x00)
						bflag = map[x+5][y];
					/* ダブルクリアのフラグ設定	*/
					if (bflag != map[x+5][y])
						double_flag = true;
					map[x+5][y] = 11;
					chput(x, y, 11);
					pt++;
					flag = true;
				}
			}
		}
		g.unlock(true);
		/* クリアされるブロックがなければおしまい	*/
		if (!flag) return (flag);

		// audio
		if (psound) {
//			try {
//				ap[4].play();
//			} catch (Exception e) {
//			}
			playsound(4);
		}

		score_add(pt*10);
		if (score > level_pt) {
			level_pt += sg_lvl;
			level++;
			level_put(level);
		}

		if (double_flag) {
			score_add(2000);
			if (psound) {
//				try {
//					ap[5].play();
//				} catch (Exception e) {
//				}
				playsound(5);
			}
			g.drawImage(image[16],18*9,20*9);
			try {
				Thread.sleep(700);
			} catch (Exception e) {
				;
			}
			g.lock();
			next_put();
			g.unlock(true);
		} else {
			try {
				Thread.sleep(400);
			} catch (Exception e) {
				;
			}
		}
		g.lock();
		for (y = 0; y < 25 - 1; y++) {
			for (x = 1; x < 15 - 1; x++) {
				if (map[x+5][y] == 11) {
					map[x+5][y] = 0x00;
					chput(x, y, 0);
					pt++;
				}
			}
		}
		g.unlock(true);
		return flag;
	}

	public void recurs_paint(int i, int j) {
		if (i >= 0 && i < 15 && j >= 0 && j < 25 && map[i+5][j] == point) {
			paint_num++;
			map[i+5][j] |= 0x80;
		} else
			return;
		recurs_paint(i - 1, j);
		recurs_paint(i + 1, j);
		recurs_paint(i, j - 1);
		recurs_paint(i, j + 1);
		return;
	}

	public void resume(int i, int j) {
		if (i >= 0 && i < 15 && j >= 0 && j < 25 && map[i+5][j] == point) {
			map[i+5][j] &= ~(0x80);
		} else
			return;
		resume(i - 1, j);
		resume(i + 1, j);
		resume(i, j - 1);
		resume(i, j + 1);
		return;
	}

	public void block_paint() {
		int i, j;
		for (i = 1; i < 15 - 1; i++) {
			for (j = 0; j < 25 - 1; j++) {
				if (0 == (map[i+5][j] & 0x80) && map[i+5][j] != 0) {
					paint_num = 0;
					point = map[i+5][j];
					recurs_paint(i, j);
					if (paint_num < max_num) {
						point |= 0x80;
						resume(i, j);
					}
				}
			}
		}
	}

	public void block_select(int lv) {
		int lvmap[] = {1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5};
		int blk;

		// lv が -1 の時はスペシャルキャラ
		if (lv < 0) {
			workBlock = spPattern;
			return;
		}
		lv = (int)(lv / 2) + 1;
		if (lv > 5) {
			lv = 5;
		}
		while (true) {
			blk = getRandomInt(31);
			if (lvmap[blk] <= lv) {
				break;
			}
		}
		workBlock = orgPattern[blk];
	}

	class inputPanel extends Panel implements ComponentListener {
		public boolean endflag;
		public boolean cancelflag;
		public Button button1;
		public Button button2;
		public TextBox text;

		public void start() {
			endflag=false;
			HTMLLayout lm = new HTMLLayout();
			setLayoutManager(lm);
			lm.begin(HTMLLayout.CENTER);
			add(new Label("☆名前をいれてね☆"));
			lm.end();
			lm.br();
			add(new Label("名前:"));
			text = new TextBox(r_myname,10,1,TextBox.DISPLAY_ANY );
			add(text);
			lm.br();
			lm.begin(HTMLLayout.CENTER);
			button1 = new Button("送信");
			add(button1);
			button2 = new Button("ｷｬﾝｾﾙ");
			add(button2);
			lm.end();
			lm.br();
			add(new Ticker("    名前は全角で5文字までだよ(半角なら10文字)"));
			setComponentListener(this);
			Display.setCurrent(this);
			while (!endflag) {
				try {
					Thread.sleep(100);
				}
				catch(Exception e) {
				}
			}
			return;
		}

		public boolean getStatus() {
			return cancelflag;
		}

		public void componentAction(Component component, int type, int param){
			if (component == button1 && type == BUTTON_PRESSED) {
				if (text.getText().equals("")) {
					text.requestFocus();
					return;
				}

				r_myname=text.getText();

				endflag = true;
				cancelflag = false;
			}
			if (component == button2 && type == BUTTON_PRESSED) {
				endflag = true;
				cancelflag = true;
			}
		}
	}
}
// EOF
