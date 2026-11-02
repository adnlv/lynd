# Proguard Rules for Lynd

# Kotlinx Serialization
-keepattributes *Annotation*,InnerClasses
-dontnote kotlinx.serialization.SerializationKt
-keepclassmembers class * {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.adnlv.lynd.data.network.** { *; }

# Room Database
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Retain Coroutine stack frames for debugging
-keepclassmembernames class kotlinx.coroutines.internal.DispatchedContinuation {
    java.lang.Object _reusableCancellableContinuation;
}
