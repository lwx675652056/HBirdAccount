package com;

public class Bug {

//陈樱_PM(1563488083) 17:25:51
//   OK   1.覆盖安装闪退；
//   OK   2.账户A登陆后的一笔记账记录，在切换账户B之后，该记录显示在B账户，实际B账户并没有这笔记录（默认账本），刷新，杀死进程重启还是有；
//   OK   3.切换账本顶部按钮不太好点击；
//   OK   4.我的账本里面的总账本和其他账本图片和账本图片大小不一样；
//   OK   5.场景账本设置预算页面的样式不对。初始时间默认今天，下面的时间不能设置初试时间之前的时间
//   OK   6.在分析设置场景预算后的样式不对；
//   OK   7.切换到首页的日常账本，再切换到分析里面的预算时，账本显示的是日常账本，但是曲线图不是日常账本；
//   OK   8.新建账本，没有邀请入口；
//   OK   9.进入到邀请账本页面，头像都没有显示，都是默认头像；
//   OK   10.添加账本点击选择账本的页面，图片尺寸不对。


//陈樱_PM(1563488083)  19:32:03
//   OK   1、切换账号之前记的账还有，切换账本后没了；
//   OK   2、管理员邀请人前后的样式区别；
//   OK   3、覆盖安装登陆后闪退，没有出现邀请按钮，并且闪退后打不开；
//   OK   4、在分析设置场景预算后的样式不对。
//   OK   5、管理员在账本管理页删除人后页面不刷新


//陈樱 学号003  11:32:52
//   OK   1.覆盖安装后默认显示的不是多人多账本内容；
//   OK   2.记不了账；
//   OK   3.邀请的好友在小程序端记账成功，数据刷新后没有同步过来。


//陈樱 学号003 18:24:53
//   OK   1.多人记账，同伴在其他平台的收入数据无法同步；
//   OK   2.新建账本，创建名字，在弹出的键盘输入文字不显示，必须要点一下输入框，建议直接激活输入框
//   OK   3.旅行账本，后台上线的常用的不止一个，但是App上只显示了交通通勤
//   OK   4.分析 - 预算完成率有问题


//   OK   1.连续删除多个标签
//   OK   2.连续切换领票票
//   OK   3.编辑心情返回没
//   OK   4.多账本被踢之后首页显示的问题


    // 从领票票每日任务设置预算不成功
    // 领票票二级页面返回问题
    // android 9 头像问题
//   OK   首页底部按钮当前页的时候不应该点击当前按钮
//   OK   晒成就底部应该有直角


//   OK   1.首页记账收入和支出的标签底色不对；
//   OK   2.邀请记账默认收起；
//   OK   3.顶部先灰再白再灰很乱，应该是白的；
//   OK   4.底部icon没换；
//   OK   5.首页下拉刷新样式没有做；
//   OK   6.丰丰通知空白状态没有做；
//   OK   7.二级页面的顶部都应该是白色；
//   OK   8.邀请记账4个字变形；
//   OK   9.进入记账详情页后的编辑页不是新版的，无法切换资产钱包；
//   OK   10.为什么记账的默认时间是2018年12月10日，不是今天？
//   OK   11.设置资产金额，记账之后余额不对，余额应该是原始总金额减去记账的总金额；（后台大佬改，但是这里要看界面刷新）
//   OK   12.图表顶部和我的顶部没有做成红色渐变的样式；
//   OK   13.我的页面，中间的细线应该和字的底部对齐。

    // 记账明细 - 编辑 - 还不能修改类别
    // 资产详情，不应该出现别人的数据，不显示记录人的小头像


//    陈樱_PM(1563488083) 15:09:45
//   OK   1.输入支出金额时，数字那里有重影；
//   OK   2.如果上个月没有数据，饼图那里“相比上个月支出”这块去掉，但是当月的饼图数据还是要展示的；
//   OK   3.相同1月支出385，去年12月支出52，算的是支出增加6.4%，这个算的不对；
//   OK   4.统计中，没有记录我记账的数据，1月记账数据是空的，在切换顶部日、周、月之后才展示；
//   OK   5.修改之前记账的记账心情后，切换到统计页面，消费心情这块没有同步数据。

    // 登录卡主不跳转
    // 首页 场景账本设置预算后设置时间
    // 记账奔溃
    // 改预算的通知一直都在

    //    OK   记账WIFI图标不对
    //    OK   选择账本返回箭头太小
    //    OK   首页顶部两个图标有点大
    //    OK   首页编辑ICON有点大，和字差不多大
    //    OK   首页饼图总支出颜色灰、黑
    //    OK   统计-饼图字数去掉“消费”
    //    OK   首页“设置”按钮改一下
    //    OK   账本人员头像描边粗一点
    //    OK   编辑账户默认为hint
    //    OK   编辑完账户回去关闭键盘
    //    OK   记账页面记账ICON稍微大一点
    //    OK   记账页面备注字体颜色
    //    OK   记账自定义键盘间隙
    // 选择时间的dialog不好看
    // 日历 今天 不一样
    //    OK   成员管理页面各边距
    //    OK   成员管理“添加成员”不是圆角
    //    OK   邀请记账页面 按钮不对
    //    OK   登录页切页问题
    //    OK   明细页“编辑”“删除”图标不对，应该和字一样的颜色
    //    OK   首页 查看全部高一点
    //    OK   首页描边黑一点
    //    OK   成员头像边框、大小大一点
    //    OK   饼图太小的点不到
    //    OK   首页饼图标题太大了
    //    OK   首页查看全部 刷新的时候闪烁
    //    OK   统计我的数据和全部数据去返
    //    OK   统计标题 图标短粗 距离近一点
    //    OK   资产“设置同步时间”圆角统一一下
    //    OK   资产说明渐变
    //    OK   “资产设置”图标短粗、渐变
    //    OK   分析 短粗
    //    OK   分析月份弹框不圆滑
    //    OK   分析切换数据反一下
    //    OK   分析选择账本圆角和高度
    //    OK   丰丰通知“全部已读”颜色不对
    //    OK   丰丰通知全部已读后返回要刷新
    //    OK   记账提醒关闭的时候返回样式
    //    OK   意见反馈联系方式不能输入中文
    //    OK   账户安全标题统一
    //    OK   手势密码字体小
    // Button里面的字体大小统一一下

}
