package slm2015.hey.view.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.dd.CircularProgressButton;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import slm2015.hey.R;
import slm2015.hey.util.LocalPreference;

public class LoginActivity extends Activity {
    private CircularProgressButton loginBtn;
    private EditText loginCodeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.globalInit();
        if (LocalPreference.instance().isLogin()) {
            startMainActivity();
        } else {
            setContentView(R.layout.login_activity);
            this.initEditText();
            this.initLoginButton();
        }
    }

    private void globalInit() {
        ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(getBaseContext()).build());
        LocalPreference.init(this);
    }

    private void initLoginButton() {
        this.loginBtn = (CircularProgressButton) findViewById(R.id.login_button);
        this.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        this.loginBtn.setEnabled(false);
    }

    private void initEditText() {
        this.loginCodeEditText = ((EditText) findViewById(R.id.login_code));
        this.loginCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loginBtn.setEnabled(count > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void login() {
        String loginCode = this.loginCodeEditText.getText().toString();
        String imei = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        this.loginBtn.setIndeterminateProgressMode(true);
        this.loginBtn.setProgress(50);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loginSuccess();
            }
        }, 2000);
    }

    private void loginSuccess() {
        LocalPreference.instance().setIsLogin(true);
        startMainActivity();
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }
}