# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


#------getui start####

-dontwarn com.getui.**
-keep class com.getui.**{*;}
-dontwarn com.igexin.**
-keep class com.igexin.** { *; }
-keep class org.json.** { *; }
-keep class android.support.v4.app.NotificationCompat { *; }
-keep class android.support.v4.app.NotificationCompat$Builder { *; }


#oppo
-keep class com.coloros.mcssdk.** { *; }
-dontwarn com.coloros.mcssdk.**

#huawei
-ignorewarning

-keepattributes *Annotation*

-keepattributes Exceptions

-keepattributes InnerClasses

-keepattributes Signature

-keepattributes SourceFile,LineNumberTable

-dontwarn com.huawei.**

-dontwarn com.hianalytics.android.**

-keep class com.hianalytics.android.**{*;}

-keep class com.huawei.updatesdk.**{*;}

-keep class com.huawei.hms.**{*;}

-keep class com.huawei.android.** { *; }

-keep interface com.huawei.android.hms.agent.common.INoProguard {*;}

-keep class * extends com.huawei.android.hms.agent.common.INoProguard {*;}


#vivo
-keep class com.vivo.push.** { *; }

-dontwarn com.vivo.push.**

#xiaomi
-dontwarn com.xiaomi.**

-keep class com.xiaomi.** { *; }

-keep class com.xiaomi.push.**

-keep class com.xiaomi.push.** {*;}

-keep class org.apache.thrift.** { *; }

#meizu
-keep class com.meizu.** { *; }

-dontwarn com.meizu.**


##---getui end