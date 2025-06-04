package com.KTA.QSlock.hook

import android.app.AndroidAppHelper
import android.content.Context
import android.util.Log
import com.github.kyuubiran.ezxhelper.utils.*
import de.robv.android.xposed.XC_MethodHook

object QSlockHook : BaseHook() {
    private const val TAG = "QSlock"

    private const val DISABLE2_NONE = 0
    private const val DISABLE2_QUICK_SETTINGS = 1

    override fun init() {
        findMethod("com.android.keyguard.KeyguardDisplayManager") {
            name == "updateDisplays"
        }.hookBefore {
            val context: Context = AndroidAppHelper.currentApplication().applicationContext
            val statusBarManager = context.getSystemService("statusbar")
            try {
                val disable2Method = statusBarManager?.javaClass?.getMethod("disable2", Int::class.javaPrimitiveType)
                disable2Method?.invoke(statusBarManager, DISABLE2_QUICK_SETTINGS)
                Log.i(TAG, "Called disable2(DISABLE2_QUICK_SETTINGS)")
            } catch (t: Throwable) {
                Log.e(TAG, "disable2 failed", t)
            }
        }
    }
}
