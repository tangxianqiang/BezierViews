# BezierViews
## preview
![1、粘性气泡效果](https://github.com/tangxianqiang/BezierViews/blob/master/gifs/drag_foam_view.gif)
![2、直播点赞桃心漂浮效果](https://github.com/tangxianqiang/BezierViews/blob/master/gifs/float_love_view.gif)
## description
<br>1、粘性气泡
<br> >>通过画贝塞尔曲线，实现了两点之前粘性的效果，两点之间的中点为控制点，调用quadTo方法绘制曲线，绘制过程中没有难点，主要是去计算起始点、终点和控制点；
<br> >>回弹过程中，使用到了估值器，该估值器返回了回弹时的坐标，这样就可以实现指定轨迹的动画
<br>2、漂浮桃心
<br> >>利用动画估值器实现的指定轨迹的动画，该轨迹是通过连个控制点实现的三阶贝塞尔轨迹
## notice
<br>1、粘性气泡
<br> >>通过设置父布局的``` clipChildren="false" ```可以让子布局的位置越过父布局，这就是qq聊天中气泡能够拉出列表的原理；
<br> >>在计算贝塞尔曲线的起点、控制点、终点的时候最好使用三角函数计算（该view算法应该没有问题，但是总有点瑕疵）
<br>2、漂浮桃心
<br> >>应当注意轨迹的起始点和终点的位置