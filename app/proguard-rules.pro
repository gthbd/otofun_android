# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/hung/Documents/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-dontskipnonpubliclibraryclasses
#-dontobfuscate
-forceprocessing
-optimizationpasses 5

-dontwarn com.viewpagerindicator.**
-dontwarn it.sephiroth.android.library.**
-dontwarn com.squareup.okhttp.**

-dontwarn com.rey.material.**
-dontwarn io.realm.**

-optimizations !field/removal/writeonly,!field/marking/private,!class/merging/*,!code/allocation/variable

-keepclasseswithmembernames class android.support.v4.widget.** { *; }
-keepclassmembers class * implements android.os.Parcelable { static ** CREATOR; }
-keep class * extends android.app.Activity
-keep class net.sourceforge.zbar.** { *; }
-assumenosideeffects class android.util.Log { *; }
-assumenosideeffects class java.io.PrintStream {
     public void println(%);
     public void println(**);
}