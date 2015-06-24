package slm2015.hey.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class LocalPreference {
    private static final String KEY_PRIVACY_MODE_HINT = "PrivacyModeHint";
    private static final String KEY_MOVE_TO_TOP_ISSUE_ID = "TOP_ISSUE_ID";
    private static String KEY_USER_TOKEN = "UserToken";
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
        return getUserToken() != null;
    }

    public String getUserToken() {
        return this.sharedPreferences.getString(LocalPreference.KEY_USER_TOKEN, null);
    }

    public void setUserToken(String token) {
        this.editor.putString(LocalPreference.KEY_USER_TOKEN, token).commit();
    }

    public String getMoveToTopIssueId() {
        return  this.sharedPreferences.getString(LocalPreference.KEY_MOVE_TO_TOP_ISSUE_ID, null);
    }

    public void setMoveToTopIssueId(String id) {
        this.editor.putString(LocalPreference.KEY_MOVE_TO_TOP_ISSUE_ID, id).commit();
    }
}
