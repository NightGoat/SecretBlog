package ru.nightgoat.secretblog.android.presentation.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import io.github.nightgoat.kexcore.utils.Try

object Twitter {
    private const val TWITTER_PACKAGE = "com.twitter.android"
    private const val TWITTER_CLASS_NAME = "com.twitter.android.PostActivity"
    private const val TWITTER_INTENT_TYPE = "text/*"
    private const val TWITTER_URL = "https://twitter.com/intent/tweet?text="

    fun getIntent(ctx: Context?, shareText: String): Intent {
        val shareIntent: Intent
        return if (doesPackageExist(ctx, TWITTER_PACKAGE)) {
            shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setClassName(
                TWITTER_PACKAGE,
                TWITTER_CLASS_NAME
            )
            shareIntent.type = TWITTER_INTENT_TYPE
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
            shareIntent
        } else {
            val tweetUrl = "$TWITTER_URL$shareText"
            val uri: Uri = Uri.parse(tweetUrl)
            shareIntent = Intent(Intent.ACTION_VIEW, uri)
            shareIntent
        }
    }
}

private fun doesPackageExist(ctx: Context?, targetPackage: String): Boolean {
    return Try { ctx?.getPackageInfo(targetPackage) }
        .map { true }
        .getOrDefault { false }
}

private fun Context.getPackageInfo(packageName: String): PackageInfo {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
    } else {
        packageManager.getPackageInfo(packageName, 0)
    }
}