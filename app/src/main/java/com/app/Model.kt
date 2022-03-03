package com.app

import java.sql.Time

// 标题 、描述、图片链接
data class Animations(val id:String,val name:String, val description:String, val image:String)
// history类
data class AnimHistory(val id:String,val name:String, val description:String, val image:String,val time:String)
// Cartoon类
data class AnimCart(val id:String,val name:String, val description:String, val image:String,val pinYing:String)
// 登录类
data class Login(val login:String ,val register:String)
// 用户类
data class UserData(var name:String, var sign:String, var doinb:String, var binb:String,
                    var image: String, var watch:String, var grade:String,
                    var follow: String,var fans:String)
// 修改成功类
data class ReviseFlag(val flag:String)
// 接受用户类
data class UserInfor(val name:String,val sign:String,val grade:String,
                     val doinb:Int,val binb:Int,val watch:Int,
                        val follow: String,val fans: String,val image: String)
// 用户头像类
data class Headimage(val image:String)

// 视频集数类
data class VideoJS(val js:String,val mc:String,val id:String)

// 评论用户类?
data class UserInfo (val Username:String,val UserTime:String,val UserMessaged:String,val UserImage:String)
// 视频
data class Video(val id:String,val nameAddress:String,val videoName:String)
// 弹幕库
data class Danmaku(val DUsername:String,val DTime:String,val DMessage:String,val DVideo:String)
// 关注类
data class Follow(val isME:String,val follow:String)
// 初始化foolw
data class initFollow(val follow:String)
// 关注 粉丝 列表类
data class FollowFans(val name:String,val grade:String,val sign: String,val image: String)
// 封面视频类
data class JJVideo(var image:String,var jj:String,var part1:String,var part2:String,var part3:String)
