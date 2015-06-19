package slm2015.hey.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class LocalPreference {
    private static final String KEY_IS_LOGIN_IN = "IsLoging";
    private static final String KEY_PRIVACY_MODE_HINT = "PrivacyModeHint";
    static LocalPreference localPreference = null;
    static Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private LocalPreference(Context context) {
        this.sharedPreferences = context.getSharedPreferences("Hey", Context.MODE_PRIVATE);
        this.editor = this.sharedPreferences.edit();
    }

    public static LocalPreference instance() {
        if (localPreference == null) {
            localPreference = new LocalPreference(context);
        }
        return localPreference;
    }

    static public void init(Context context) {
        LocalPreference.context = context;
    }

    public void cleanAllCache() {
        Log.d("LocalPreference", "Clean All Cache");
        this.sharedPreferences.edit().clear().apply();
    }

    public void setPrivacyModeHintEnabled(boolean enabled) {
        this.editor.putBoolean(LocalPreference.KEY_PRIVACY_MODE_HINT, enabled).commit();
    }

    public boolean getPrivacyModeHintEnabled() {
        return this.sharedPreferences.getBoolean(LocalPreference.KEY_PRIVACY_MODE_HINT, true);
    }

    public boolean isLogin() {
        return false;
        //return this.sharedPreferences.getBoolean(LocalPreference.KEY_IS_LOGIN_IN, false);
    }

    public void setIsLogin(boolean isLogin) {
        this.editor.putBoolean(LocalPreference.KEY_IS_LOGIN_IN, isLogin).commit();
    }
}
