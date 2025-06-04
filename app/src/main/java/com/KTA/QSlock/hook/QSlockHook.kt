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
        }.hookAfter {
            val context: Context = AndroidAppHelper.currentApplication().applicationContext
            val statusBarManager = context.getSystemService("statusbar")

            // Kiểm tra tham số truyền vào updateDisplays(showing: Boolean)
            val isShowingKeyguard = it.args.getOrNull(0) as? Boolean ?: false

            try {
                val disable2Method = statusBarManager?.javaClass?.getMethod("disable2", Int::class.javaPrimitiveType)
                val value = if (isShowingKeyguard) DISABLE2_QUICK_SETTINGS else DISABLE2_NONE
                disable2Method?.invoke(statusBarManager, value)
                Log.i(TAG, "Called disable2($value), keyguard=$isShowingKeyguard")
            } catch (t: Throwable) {
                Log.e(TAG, "disable2 failed", t)
            }
        }
    }
}
