# Keep PermissionX classes
-keep class com.permissionx.guolindev.** { *; }

# Keep your application classes
-keep class com.datarakshak.app.** { *; }

# Keep enum classes
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Add project specific ProGuard rules here.
-keepattributes SourceFile,LineNumberTable
-keep class com.datarakshak.app.** { *; } 