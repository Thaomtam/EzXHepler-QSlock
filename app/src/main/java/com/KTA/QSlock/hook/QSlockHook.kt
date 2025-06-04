package com.KTA.QSlock.hook

import com.github.kyuubiran.ezxhelper.utils.*

// Example hook
object QSlockHook : BaseHook() {
    override fun init() {
        // Example for findMethod
        findMethod("android.widget.Toast") {
            name == "show"
        }.hookBefore {
            Log.i("Hooked before Toast.show()")
        }

        // Example for getMethodByDesc
        getMethodByDesc("Landroid/widget/Toast;->show()V").hookAfter {
            Log.i("Hooked after Toast.show()")
        }
    }
}
