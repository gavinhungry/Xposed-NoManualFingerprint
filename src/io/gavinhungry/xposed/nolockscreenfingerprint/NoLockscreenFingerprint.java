package io.gavinhungry.xposed.nolockscreenfingerprint;

import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.XC_MethodReplacement;

public class NoLockscreenFingerprint implements IXposedHookLoadPackage {

  @Override
  public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
    if (!lpparam.packageName.equals("com.android.systemui")) {
      return;
    }

    XposedHelpers.findAndHookMethod("com.android.internal.widget.LockPatternUtils$StrongAuthTracker", lpparam.classLoader,
      "isFingerprintAllowedForUser", int.class, new XC_MethodReplacement() {

      @Override
      protected Object replaceHookedMethod(final MethodHookParam param) throws Throwable {
        return false;
      }
    });
  }
}
