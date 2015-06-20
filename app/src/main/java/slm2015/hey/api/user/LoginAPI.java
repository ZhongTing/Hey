package slm2015.hey.api.user;

import slm2015.hey.api.APIManager;
import slm2015.hey.api.PostBase;

public class LoginAPI extends PostBase {
    public LoginAPI(String imei) {
        super(APIManager.HEY_SERVER_BASE_URL, "/api/user/login");
        this.setParam("deviceIdentity", imei);
        this.TAG = "LoginAPI";
    }
}
