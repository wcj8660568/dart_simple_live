# ==========================================
# Flutter 核心引擎与插件保活规则
# ==========================================
-keep class io.flutter.app.** { *; }
-keep class io.flutter.plugin.** { *; }
-keep class io.flutter.util.** { *; }
-keep class io.flutter.view.** { *; }
-keep class io.flutter.embedding.** { *; }
-keep class io.flutter.** { *; }

# 保持 Flutter 原生通信接口（MethodChannel）
-keepclassmembers class * {
    @io.flutter.plugin.common.MethodChannel$MethodCallHandler *;
    @io.flutter.plugin.common.EventChannel$StreamHandler *;
}

# ==========================================
# 基础序列化与反射支持
# ==========================================
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# 保持枚举类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保持 Parcelable 实现类
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# 保持 Serializable 实现类
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# ==========================================
# 常用第三方库保活规则（可选，视你使用的插件而定）
# ==========================================
# 如果使用了 video_player, camera 等官方插件
-keep class io.flutter.plugins.** { *; }
-keep class com.baseflow.** { *; }

# 如果使用了 webview_flutter
-keep class com.flutter_webview_plugin.** { *; }