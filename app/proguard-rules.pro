# Add project specific ProGuard rules here.
-keep class com.fantasykingdom.app.data.model.** { *; }
-keepattributes *Annotation*
-keepclassmembers class * {
    @com.google.firebase.firestore.PropertyName <fields>;
}
-dontwarn com.google.zxing.**
