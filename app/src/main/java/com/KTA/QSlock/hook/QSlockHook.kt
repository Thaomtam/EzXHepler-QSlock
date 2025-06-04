package com.KTA.QSlock.hook

import android.app.AndroidAppHelper
import android.content.Context
import android.util.Log
import com.github.kyuubiran.ezxhelper.utils.*
import de.robv.android.xposed.XposedHelpers

object QSlockHook : BaseHook() {
    private const val TAG = "QSlock"
    private const val DISABLE2_NONE = 0
    private const val DISABLE2_QUICK_SETTINGS = 1

    override fun init() {
        // Hook phương thức updateDisplays trong KeyguardDisplayManager
        findClass("com.android.keyguard.KeyguardDisplayManager")?.findMethod {
            name == "updateDisplays"
        }?.hookAfter {
            Log.i(TAG, "show")

            val context = AndroidAppHelper.currentApplication()
            if (context == null) {
                Log.e(TAG, "context is null")
                return@hookAfter
            }

            val statusBarManager = context.getSystemService("statusbar")
            if (statusBarManager == null) {
                Log.e(TAG, "statusBarManager is null")
                return@hookAfter
            }

            val isLockScreenActive = args.getOrNull(0)?.toString() == "true"
            val disableFlag = if (isLockScreenActive) DISABLE2_QUICK_SETTINGS else DISABLE2_NONE

            // Gọi phương thức disable2 của statusBarManager
            XposedHelpers.callMethod(
                statusBarManager,
                "disable2",
                arrayOf<Class<*>>(Integer::class.java),
                disableFlag
            )
        }
    }
}
