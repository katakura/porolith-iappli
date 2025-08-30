# Porolith for i-appli

## 概要 (Overview)

**Porolith** は NTT DoCoMo の i-appli プラットフォーム向けに開発された落ち物パズルゲームです。505i 端末向けに設計されており、テトリス風のゲームプレイに独自のカラーマッチング要素を組み合わせたパズルゲームです。

**Porolith** is a falling block puzzle game developed for NTT DoCoMo's i-appli platform. Designed for 505i series mobile phones, it combines Tetris-style gameplay with unique color-matching puzzle elements.

## ゲーム概要 (Game Description)

### 基本ルール (Basic Rules)
- さまざまな形状とカラーのブロックが上から落下してきます
- プレイヤーはブロックを左右に移動、回転させて配置します
- 同じ色のブロックが23個以上つながると消去されます
- ブロックを消去するとスコアが加算され、レベルが上がります
- 画面上部までブロックが積み上がるとゲームオーバーです

### 特殊ブロック (Special Blocks)
- スペシャルブロック（特殊ブロック）が登場することがあります
- スペシャルブロックは指定した色のブロックを全て消去できます
- 高スコア時や特定条件で出現確率が上がります

### 操作方法 (Controls)
- **左右移動**: 左右キー / 4・6キー
- **回転**: 上キー / 2・5・*・選択キー
- **高速落下**: 下キー / 8キー / 3キー（設定により異なる）
- **ポーズ**: ソフトキー1
- **終了**: ソフトキー2

## 機能 (Features)

### ゲーム機能
- **31種類のブロックパターン**: 様々な形状と色の組み合わせ
- **レベルシステム**: スコアに応じてレベルアップし、難易度が上昇
- **ハイスコア記録**: ローカルにハイスコアを保存
- **サウンド**: BGMと効果音（ON/OFF切り替え可能）
- **ブロック落下速度設定**: FAST/SLOW モード切り替え

### ネットワーク機能
- **オンラインランキング**: ハイスコアをサーバーに送信
- **ランキング表示**: トップ5の表示と自分の順位確認
- **Webサイト連携**: 公式サイトへのリンク

### その他
- **設定保存**: サウンド設定やゲーム設定の保持
- **機種別最適化**: 各メーカーの携帯電話に対応

## 技術仕様 (Technical Specifications)

### 対応プラットフォーム
- **i-appli** (DoCoMo Java アプリケーション)
- **対象機種**: 505i シリーズ、900i シリーズ他
- **Java**: DoCoMo 独自 Java API使用

### 使用API
- `com.nttdocomo.ui.*` - UI コンポーネント
- `com.nttdocomo.io.*` - 入出力
- `com.nttdocomo.net.*` - ネットワーク通信
- `javax.microedition.io.*` - 標準 MIDP API

### リソース
- **グラフィック**: 9x9ピクセル GIF画像
- **サウンド**: .mld 形式音声ファイル
- **画面サイズ**: 225x225 ピクセル

## 開発情報 (Development Info)

- **作者**: Y.Katakura (Yotan)
- **著作権**: Copyright(C)2004-2005 by Y.Katakura
- **バージョン**: 1.7
- **開発年**: 2004-2005年

### 謝辞
Special Thanks: TAKA, RYOUSHI, Chaka ...and all gamers!

## ファイル構成 (File Structure)

```
porolith-iappli/
├── src/
│   ├── Porolith.java          # メインアプリケーションクラス
│   └── PorolithCanvas.java    # ゲーム描画・ロジッククラス
├── res/
│   ├── *.gif                  # ゲーム用9x9グラフィック
│   ├── *.mld                  # サウンドファイル
│   ├── title.gif              # タイトル画面
│   └── subtitle.gif           # サブタイトル
├── bin/
│   └── Download.html          # ダウンロードページ
├── data/                      # アートワーク・素材ファイル
└── sp/
    └── Porolith.sp           # スクラッチパッド設定
```

## 歴史的背景 (Historical Context)

このゲームは2004年頃の日本の携帯電話文化を反映しており、当時のi-mode対応携帯電話で動作するJavaアプリケーション（i-appli）として開発されました。現在では歴史的価値のあるモバイルゲームアーカイブとして価値があります。

---

*このプログラムは DoCoMo i-appli 用の「Porolith」というパズルゲームです。ブロックを操作して同じ色のブロックを消していく、テトリス風のゲームとなっています。*
