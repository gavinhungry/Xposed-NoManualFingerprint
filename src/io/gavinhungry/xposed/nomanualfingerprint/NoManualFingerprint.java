package io.gavinhungry.xposed.nomanualfingerprint;

import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.XC_MethodReplacement;

public class NoManualFingerprint implements IXposedHookLoadPackage {

  @Override
  public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
    if (!lpparam.packageName.equals("com.android.systemui")) {
      return;
    }

    XposedHelpers.findAndHookMethod("com.android.internal.widget.LockPatternUtils$StrongAuthTracker", lpparam.classLoader,
      "isFingerprintAllowedForUser", int.class, new XC_MethodReplacement() {

      @Override
      protected Object replaceHookedMethod(final MethodHookParam param) throws Throwable {
        int userId = (Integer) param.args[0];
        int strongAuthForUser = (Integer) XposedHelpers.callMethod(param.thisObject, "getStrongAuthForUser", userId);

        int STRONG_AUTH_NOT_REQUIRED = XposedHelpers.getIntField(param.thisObject, "STRONG_AUTH_NOT_REQUIRED");
        return (strongAuthForUser & ~STRONG_AUTH_NOT_REQUIRED) == 0;
      }
    });
  }
}
