package slm2015.hey.api.user;

import slm2015.hey.api.APIManager;
import slm2015.hey.api.PostBase;

public class RegisterGcmAPI extends PostBase {
    public RegisterGcmAPI(String gcmToken) {
        super(APIManager.HEY_SERVER_BASE_URL, "/api/user/register/android/notification");
        this.setHeader("Authorization", APIManager.getInstance().getAccessToken());

        this.setParam("gcmToken", gcmToken);
        this.TAG = "RegisterGcmAPI";
    }
}
