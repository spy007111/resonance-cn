import os
import glob
import re

TRANSLATIONS = {
    # 底部导航 / 顶层路由
    '"Home"': '"首页"',
    '"Search"': '"搜索"',
    '"Library"': '"媒体库"',
    '"Downloads"': '"下载"',
    '"Settings"': '"设置"',
    '"Now Playing"': '"正在播放"',
    '"Queue"': '"播放队列"',
    '"Lyrics"': '"歌词"',

    # 登录页
    '"Connect to your Navidrome server"': '"连接到你的 Navidrome 音乐服务器"',
    '"Server URL"': '"服务器地址"',
    '"Username"': '"用户名"',
    '"Password"': '"密码"',
    '"Hide password"': '"隐藏密码"',
    '"Show password"': '"显示密码"',
    '"Connect"': '"连接服务器"',

    # 操作菜单
    '"Play"': '"播放"',
    '"Play all"': '"全部播放"',
    '"Play next"': '"下一首播放"',
    '"Add to queue"': '"加入队列"',
    '"Go to album"': '"查看专辑"',
    '"Go to artist"': '"查看歌手"',
    '"Download"': '"下载"',
    '"Shuffle"': '"随机播放"',
    '"Shuffle all"': '"全部随机"',
    '"See all"': '"查看全部"',
    '"Retry"': '"重试"',
    '"Cancel"': '"取消"',
    '"Remove"': '"删除"',
    '"Remove download?"': '"删除此下载？"',

    # 媒体库与分类
    '"Artists"': '"歌手"',
    '"Albums"': '"专辑"',
    '"Tracks"': '"曲目"',
    '"Songs"': '"歌曲"',
    '"Playlists"': '"歌单"',
    '"Genres"': '"流派"',

    # 提示与空状态
    '"Search your music library"': '"搜索你的音乐库"',
    '"Search artists, albums, songs..."': '"搜索歌手、专辑、曲目..."',
    '"Queue is empty"': '"队列为空"',
    '"Play some music to fill the queue"': '"挑选一首歌开始播放吧"',
    '"No lyrics available"': '"暂无歌词"',
    '"No albums in this genre"': '"该流派下暂无专辑"',
    '"Failed to load"': '"加载失败"',
    '"Could not find music. Open Downloads to listen offline."': '"无法获取音乐，可前往下载页面离线收听"',

    # 设置页
    '"Account"': '"账户"',
    '"Server"': '"服务器"',
    '"Theme"': '"外观主题"',
    '"Playback"': '"播放设置"',
    '"Disconnect from server"': '"断开与服务器的连接"',
    '"Log out"': '"退出登录"',
    '"Scrobble plays"': '"记录听歌历史 (Scrobble)"',
    '"Report played songs to server"': '"将播放记录同步至 Navidrome 服务器"',
    '"Gapless playback"': '"无缝播放 (Gapless)"',
    '"Seamless transition between tracks"': '"曲目切换时平滑衔接，消除间隙"',
    '"Open source Navidrome client"': '"完全开源免费的 Navidrome 客户端"',
    '"Download favorite songs"': '"下载喜爱的曲目"',
}

kt_files = glob.glob('/Users/lgs/workspace/resonance_src/app/src/main/java/com/resonance/music/**/*.kt', recursive=True)

total_replaced = 0
for fp in kt_files:
    with open(fp, 'r', encoding='utf-8') as f:
        content = f.read()

    new_content = content
    for eng, chn in TRANSLATIONS.items():
        if eng in new_content:
            new_content = new_content.replace(eng, chn)
            total_replaced += 1

    if new_content != content:
        with open(fp, 'w', encoding='utf-8') as f:
            f.write(new_content)
        print(f"Patched: {os.path.basename(fp)}")

print(f"=== 汉化替换完成，共替换 {total_replaced} 处文本 ===")
