package slm2015.hey.view.main;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import slm2015.hey.R;
import slm2015.hey.core.gcm.RegistrationIntentService;
import slm2015.hey.core.user.UserHandler;
import slm2015.hey.util.LocalPreference;

public class LoginActivity extends Activity implements UserHandler.LoginListener {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private static final String TAG = "LoginActivity";

    private UserHandler userHandler;
    private CircularProgressButton registerButton;
    private EditText loginCodeEditText;
    private View registerZone;
    private boolean isFinishActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.globalInit();
        setContentView(R.layout.login_activity);
        this.init();
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.login();

        //斷網也可以看local
        if (LocalPreference.instance().isLogin()) {
            startMainActivity();
        }
    }

    private void globalInit() {
        LocalPreference.init(this);
    }

    private void init() {
        this.userHandler = new UserHandler(this);
        this.initRegisterZone();
        this.initEditText();
        this.initRegisterButton();
    }

    private void initRegisterZone() {
        this.registerZone = findViewById(R.id.register_zone);
        this.registerZone.setAlpha(0);
    }

    private void initRegisterButton() {
        this.registerButton = (CircularProgressButton) findViewById(R.id.register_button);
        this.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        this.registerButton.setEnabled(false);
    }

    private void initEditText() {
        this.loginCodeEditText = ((EditText) findViewById(R.id.login_code));
        this.loginCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registerButton.setEnabled(count > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void register() {
        String imei = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        String registerCode = this.loginCodeEditText.getText().toString();
        this.registerButton.setIndeterminateProgressMode(true);
        this.registerButton.setProgress(50);
        this.userHandler.register(imei, registerCode, this);
    }

    private void login() {
        String imei = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        this.userHandler.login(imei, this);
    }

    @Override
    public void onLoginSuccess() {
        startMainActivity();
    }

    @Override
    public void onLoginFail() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this.registerZone, "alpha", this.registerZone.getAlpha(), 1);
        animator.setDuration(3000);
        animator.start();
    }

    @Override
    public void onRegisterCodeError() {
        this.registerButton.setProgress(0);
        Toast.makeText(this, "註冊碼錯誤", Toast.LENGTH_LONG).show();
    }

    private void startMainActivity() {
        if (!this.isFinishActivity) {
            this.isFinishActivity = true;
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}