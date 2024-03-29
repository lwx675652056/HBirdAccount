

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
#生成混淆后的映射关系
-verbose
#输出映射文件到mappingcd
-printmapping build/outputs/mapping/release/mapping.txt
-ignorewarnings
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keepattributes Signature
#注解不需要混淆
-keepattributes *Annotation*
#android不做预检验，去掉可以加快混淆速度
-dontpreverify
#抛出异常时保留代码行号，
-keepattributes SourceFile,LineNumberTable


#实体类开始
#-keep class com.dev.base.mvp.model.entity.** { *; }
-keep class com.hbird.base.mvp.model.entity.** { *; }
#如果你使用了Devring中的GreenOpenHelper来创建初始化DaoMaster，那就需要把GreenDao自动生成的XXXDao忽略混淆
#-keep class com.dev.base.mvp.model.db.greendao.** { *; }
-keep class com.hbird.base.mvp.model.db.greendao.** { *; }
#实体类结束


#RxBus开始
#-keep class com.dev.base.mvp.model.bus.support.ThreadMode { *; }
-keep class com.hbird.base.mvp.model.bus.support.ThreadMode { *; }
-keepclassmembers class * {
    #@com.dev.base.mvp.model.bus.support.Subscribe <methods>;
    @com.hbird.base.mvp.model.bus.support.Subscribe <methods>;
}
#RxBus结束


#fresco开始
-keep class com.facebook.fresco.** { *; }
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
     @com.facebook.common.internal.DoNotStrip *;
}
-keep class com.facebook.imagepipeline.gif.** { *; }
-keep class com.facebook.imagepipeline.webp.* { *; }
-keepclassmembers class * {
    native <methods>;
}
-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
-keep class com.facebook.imagepipeline.animated.factory.AnimatedFactoryImpl {
    public AnimatedFactoryImpl(com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory,com.facebook.imagepipeline.core.ExecutorSupplier);
}
#fresco结束


#glide开始
-keep public class * implements com.bumptech.glide.module.AppGlideModule
-keep public class * implements com.bumptech.glide.module.LibraryGlideModule
-keep class com.bumptech.glide.** { *; }
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}
#glide结束


#butterknife开始
-keep class butterknife.** { *; }
  -dontwarn butterknife.internal.**
  -keep class **$$ViewBinder { *; }
  -keepclasseswithmembernames class * { @butterknife.* <fields>;}
  -keepclasseswithmembernames class * { @butterknife.* <methods>;}
#butterknife结束




#sqlcipher数据库加密开始
-keep  class net.sqlcipher.** {*;}
-keep  class net.sqlcipher.database.** {*;}
#sqlcipher数据库加密结束


#EventBus开始
#//如果使用了EventBus processor进行加速，就必须加上这个(只要有这个注解的类和方法都不混淆，为反编译提供了便利), 如果没有用到加速，这个就不用了
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
#EventBus结束


#Rxjava&RxAndroid开始
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
#Rxjava&RxAndroid结束


#RxPermission开始
-keep class com.tbruyelle.rxpermissions2.** { *; }
-keep interface com.tbruyelle.rxpermissions2.** { *; }
#RxPermission结束


#Retrofit开始
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-dontwarn okio.**
#Retrofit结束

#以下这个方法的说明：保留native的方法名和包含这个native方法的类名  keepclasseswithmembernames：保留类名和方法名
-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public void run(...);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
  public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
#说明：保留继承于view的类中set* 成员函数名不变
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}
#说明:保留R类中静态字段的字段名不变
-keepclassmembers class **.R$* {
    public static <fields>;
}
-keep public class com.ikmak.parent.R$*{
    public static final int *;
}

-keepnames class * implements java.io.Serializable

-keepclassmembers class * implements java.io.Serializable {
   static final long serialVersionUID;
   private static final java.io.ObjectStreamField[] serialPersistentFields;
   !static !transient <fields>;
   private void writeObject(java.io.ObjectOutputStream);
   private void readObject(java.io.ObjectInputStream);
   java.lang.Object writeReplace();
   java.lang.Object readResolve();
}

-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}

-keepattributes *JavascriptInterface*
-keep class **.Webview2JsInterface { *; }  # 保持WebView对HTML页面的API不被混淆
-keepclassmembers class fqcn.of.javascript.interface.for.webview {	# 保留WebView
   public *;
}

# 微信相关
-keep class com.tencent.mm.opensdk.** {
    *;
}
-keep class com.tencent.wxop.** {
    *;
}
-keep class com.tencent.mm.sdk.** {
    *;
}

# umeng
-keep class com.umeng.** {*;}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
# bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}


#greendao3.2.0,此是针对3.2.0，如果是之前的，可能需要更换下包名
# greenDAO开始
#daoPackage 'com.dev.base.mvp.model.db.greendao'
 # daoPackage 'com.hbird.base.mvp.model.db.greendao'
-keep class org.greenrobot.greendao.**{*;}
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties
-dontwarn org.greenrobot.greendao.database.**
-dontwarn rx.**
# greenDAO结束

-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep public class com.alibaba.android.arouter.facade.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}

# 如果使用了 byType 的方式获取 Service，需添加下面规则，保护接口
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider
