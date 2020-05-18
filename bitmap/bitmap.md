# Bitmap

# 计算上的大小转换

## 基本概念
- bit (位，又叫 “比特”) ：bit 的缩写是 b,是计算机中的最小数据单位,属于二进制的范畴，其实就是0或者1）
- Byte (字节) ：Byte 的缩写是 B，是计算机文件大小的基本计算单位

## 转换关系
 1 Byte = 8 bit
 1 KB = 1024 Byte
 1 Mb = 1024 KB
 1 GB  = 1024 Mb
 1 TB =  1024 GB


# 文件大小

有一个宽高为 1080 * 1920, 大小为 722 kb 的图片
将该图片放在 assets 目录下，通过 assets.open("pic.jpg") 打开并保存到手机文件中，

```
assets 中读取的大小 ： 705 kb
保存到本地的图片大小 705 kb
```

# Bitmap的占用内存大小

## 从 assets 目录中加载

```
从 assets 中加载图片：
图像宽高 ： 1080 * 1920 
占用内存大小： 8100 kb 
bitmap.density : 320 
```
 
##  从 drawable 目录加载

1. 如果图片所在的文件夹dpi刚好是手机屏幕密度所对应的文件夹(比如：手机dpi为 xhdpi,图片在 drawable-xhdpi 文件夹中)，
则该图片不会被压缩
2. 如果图片所在的文件夹dpi高于手机屏幕密度所对应的文件夹(比如：手机dpi为 xhdpi,图片在 drawable-xxhdpi 文件夹中)，
则该图片会被压缩，图片的宽和高，以及占用的内存都会变小

3. 如果图片所在的文件夹dpi低于手机屏幕密度所对应的文件夹(比如：手机dpi为 xhdpi,图片在 drawable-mhdpi 文件夹中)，
 则该图片会被放大，图片的宽和高，以及占用的内存都会变大。  注意：如果图片本身就比较大，而又放在了密度较低的文件夹中，
 加载时会导致占用内存变得非常大，导致OOM
 
 

总结：
1. 通过BitmapFactory.decodeXX() 方法加载出来得到一个bitmap
这个 bitmap 的宽高 和 原文件的宽高一致
但是该 bitmap 占用的内存并不会和原文件的大小一致，通常会比原文件还大

2. 只有在加载图片的一瞬间内存会飙升，加载完图片后内存会降下来

# Bitmap 的各种转换


# Android 开发中的各种单位的解释

- Px（Pixel像素）
  也称为图像元素，是作为图像构成的基本单元，
  单个像素的大小并不固定，跟随屏幕大小和像素数量的关系变化（屏幕越大，像素越低，单个像素越大，反之亦然）。
  所以在使用像素作为设计单位时，在不同的设备上可能会有缩放或拉伸的情况
  
- Resolution（分辨率）
  是指屏幕的垂直和水平方向的像素数量，
  如果分辨率是 1920*1080 ，那就是垂直方向有 1920 个像素，水平方向有 1080 个像素
   
- Dpi（ Dot per Inch, 像素密度）
  是指屏幕上每英寸（1英寸 = 2.54 厘米）距离中有多少个像素点
  如果屏幕为 320*240，屏幕长 2 英寸宽 1.5 英寸，Dpi = 320 / 2 = 240 / 1.5 = 160
  
- Density (密度)  
  这个是指屏幕上每平方英寸（2.54 ^ 2 平方厘米）中含有的像素点数量

- Dip/dp (Density Independent Pixel , 密度独立像素)  
  也可以叫做dp，长度单位，同一个单位在不同的设备上有不同的显示效果，具体效果根据设备的密度有关
  
  
## 计算规则
我们以一个 4.95 英寸 1920 * 1080 的 nexus5 手机设备为例：

- Dpi
 1. 计算直角边像素数量： 1920^2 + 1080^2 = 2202^2（勾股定理）
 2. 计算 DPI = 2202 / 4.95 = 445
 3. 得到这个设备的 DPI 为 445 （每英寸的距离中有 445 个像素）

- Density
  上面得到每英寸中有 440 像素，那么 density 为每平方英寸中的像素数量，
  应该为： 445^2 = 198025
  
-  Dip
1. 先明白一个概念，所有显示到屏幕上的图像都是以 px 为单位
2. Dip 是我们开发中使用的长度单位，最后他也需要转换成 px
3. 计算这个设备上 1dip 等于多少 px：
   px = dip x dpi / 160
   px = 1 x 445 / 160 = 2.78
4. 通过上面的计算可以看出在此设备上 1dip = 2.78px

# Android 中内置了几个默认的 Dpi 

同一个分辨率在不同的屏幕尺寸上 Dpi 也不相同。
为了解决这个问题， Android 中内置了几个默认的 Dpi ，
在特定的分辨率下自动调用，也可以手动在配置文件中修改

|DPI|分辨率|系统dpi|基准比例	|
|:-:|:-:|:-:|:-:|
|ldpi| 240x320 | 120 | 0.75 |
|mdpi| 320x480 | 160 |1 |
|hdpi| 480x800 | 240 | 1.5 |
|xhdpi| 720x1280 | 320 |2 |
|xxhdpi| 1080x1920 | 480 | 3 |
|xxxhdpi|  |  | |

这是内置的 Dpi ，啥意思？ 在 1920*1080 分辨率的手机上 默认就使用 480 的 dpi ，
不管的你的尺寸是多大都是这样，除非厂家手动修改了配置文件

1. dpi（每英寸像素数）是有预设值的！120-160-240-320-480。对应不同的分辨率。
2. 基准比例 = dpi（每英寸像素数） / 160
3. px = dp x 基准比例
  
  
## Drawable 文件夹的适配

### Android 中的 drawable 文件夹

-  drawable-ldpi(低密度)  
-  drawable-mdpi(中等密度)     
-  drawable-hdpi(高密度)
-  drawable-xhdpi(超高密度)    
-  drawable-xxhdpi(超超高密度)
-  drawable-xxxhdpi(超超超高密度) 
-  drawable-nohdpi(无缩放)
-  drawable

###  选用规则

1. 比如在一个中等分辨率的手机上，Android就会选择drawable-mdpi文件夹下的图片，
文件夹下有这张图就会优先被使用，在这种情况下，图片是不会被缩放的

2. 但是如果没有在drawable-mdpi的文件夹下找到相应图片的话，
Android系统会首先从更高一级的drawable-hdpi文件夹中查找，
如果找到图片资源就进行缩放处理，显示在屏幕上

3. 如果drawable-hdpi文件夹下也没有的话，就依次往drawable-xhdpi文件夹、drawable-xxhdpi文件夹、
drawable-xxxhdpi文件夹、drawable-nodpi

4. 如果更高密度的文件夹里都没有找到，就往更低密度的文件夹里寻找，drawable-ldpi文件夹下查找；

5. 如果都没找到，最终会在默认的drawable文件夹中寻找，如果默认的drawable文件夹中也没有那就会报错啦。
（前提是把一张图片做成很多不同的分辨率放在各个对应密度的drawable文件夹下）

### 总结
1. 如果图片所在目录dpi低于匹配目录，那么该图片被认为是为低密度设备需要的，现在要显示在高密度设备上，图片会被放大。
2. 如果图片所在目录dpi高于匹配目录，那么该图片被认为是为高密度设备需要的，现在要显示在低密度设备上，图片会被缩小。
3. 如果图片所在目录为drawable-nodpi，则无论设备dpi为多少，保留原图片大小，不进行缩放。
   
   
   
   
   https://www.jianshu.com/p/3950665e93e6
   https://mp.weixin.qq.com/s?__biz=MzA3NTYzODYzMg==&mid=403263974&idx=1&sn=b0315addbc47f3c38e65d9c633a12cd6&scene=0&key=41ecb04b05111003b09e701282b25d11bbd44a566b396065f768bb25e8cfbf2d4fa01e0e0910b5b879e530fbf4d3d5b3&ascene=0&uin=MjAyNzY1NTU%3D&devicetype=iMac+MacBookPro12%2C1+OSX+OSX+10.11.2+build(15C50)&version=11020201&pass_ticket=ShKQF7m%2BOe6vnikUYp2pOZvgf9UcuGsGK%2F64sfJ%2BVu4%3D
   

