# https://github.com/square/okhttp
# https://github.com/square/retrofit
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn retrofit2.Platform$Java8
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# https://github.com/google/dagger/issues/645
-dontwarn com.google.errorprone.annotations.**

# https://stackoverflow.com/questions/22582006/cannot-instantiate-class-android-support-v7-widget-searchview
-keep class android.support.v7.widget.SearchView { *; }
