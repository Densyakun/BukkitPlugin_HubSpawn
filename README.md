# BukkitPlugin_HubSpawn
スポーンコマンド追加プラグイン。ホーム登録機能、ベッド移動など便利な機能もある

- [Download](https://github.com/Densyakun/BukkitPlugin_HubSpawn/releases)

## バージョン
|バージョン|概要|公開日|
|:-:|:-:|:-:|
|1.1||2016/01/02|
|1.2|更新|2016/01/04|
|1.3|バグ修正|2016/01/04|
|1.4|バグ修正<br>setspawnコマンドを追加<br>configでワールドのスポーン地点を変更するか設定できるようになった|2016/01/07|
|1.5|バグ修正|2016/01/13|
|1.5.1|バグ修正|2016/01/14|
|1.5.2|バグ修正|2016/01/14|
|1.6|バグ修正<br>/bedコマンドを追加、config.ymlにリスポーン時にベッドにスポーンするかどうか、ベッドに直接移動できるか設定できるようになった|2016/01/17|
|[1.8](https://github.com/Densyakun/BukkitPlugin_HubSpawn/releases/tag/v1.8)|ホーム機能を追加<br>iConomyと連動し様々な設定ができるようになった<br>/sethome・/home・/removehome・/homelistコマンドを追加<br>プラグイン開発者向けにHubSpawnを前提プラグインとして使えるように改良|2016/02/27|

## コマンド
- /spawn スポーン地点に移動する
- /spawn reload (op必須) configをリロード
- /hub スポーン地点に移動する
- /setspawn 1.4~ スポーン地点を現在地に設定
- /setspawn 1.4~ [x] [y] [z] スポーン地点を指定した場所に設定
- /setspawn 1.4~ [world] [x] [y] [z] スポーン地点を指定した場所に設定
- /setspawn 1.4~ [x] [y] [z] [yaw] [pitch] スポーン地点を指定した場所に設定
- /setspawn 1.4~ [world] [x] [y] [z] [yaw] [pitch] スポーン地点を指定した場所に設定
- /bed 1.6~ ベッドに移動する
- /sethome [家の名前] 1.8 現在地を家に設定([家の名前]は書かなくても良い)
- /home [家の名前] 1.8 家に移動([家の名前]がない場合は書かなくても良い)
- /homelist [ページ数] 1.8 家の一覧を表示(ページ数には1～2³¹-1(2147483647)までの整数を入力。ページ数がない場合は1ページになる)
- /removehome [家の名前] 1.8 指定した家を削除([家の名前]がない場合は書かなくても良い)

## config.yml
- world スポーン地点のワールド名
- x 1.2~ スポーン地点のX座標
- y 1.2~ スポーン地点のY座標
- z 1.2~ スポーン地点のZ座標
- joinspawn 1.2~ ログイン時にスポーンさせるか
- firstonly 1.2~ 初回ログイン時にのみスポーンさせる
- respawn 1.2~ リスポーン時にスポーン地点を変更する
- worldsetspawn 1.4~ ワールドのスポーン地点を変更するか
- respawntobed 1.6~ リスポーン時にベッドにスポーンするか
- bedteleported 1.6~ ベッドに直接移動できるか
- spawned: 1.8 /spawn及び/hubコマンドが使用できるか
- spawncost: 1.8 iConomy必須 /spawn及び/hubコマンドの使用料
- bedcost: 1.8 iConomy必須 /bedコマンドの使用料
- homecost: 1.8 iConomy必須 /homeコマンドの使用料
- sethomecost: 1.8 iConomy必須 /sethomeコマンドの使用料
- home: 1.8 ホーム機能が使用できるか
- homemax: 1.8 ホームの最大数(0以下にした場合は無限)
