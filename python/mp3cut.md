
# 安装 pydub

```
pip3 install pydub
```

# pydub需要依赖 libav 或 ffmpeg 安装libav
各系统安装

备注 ：mac brew 不能使用的下载: homebrew

终端
```
brew install libav --with-libvorbis --with-sdl --with-theora
```
配置完成.

代码实现

```python
from pydub import AudioSegment
import re, os

# 循环目录下所有MP3文件
for item in os.listdir('.'):
    f = re.findall(r'(.*?)\.mp3', item)  # 取出后缀为.mp3的文件
    if f:
        f[0] += '.mp3'
        mp3 = AudioSegment.from_mp3(f[0])  # 打开MP3文件
        # 输出为MP3文件
        if f[0] == '01.认识秩序.mp3':
            mp3[15 * 1000:290 * 1000].export(f[0], format='mp3')  # 这个截取15—~290秒
        else:
            mp3[15 * 1000:].export(f[0], format='mp3')  # 这个只把前15秒删去
        print(f[0] + ' is done!')
```