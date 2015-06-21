package slm2015.hey.api.user;

import slm2015.hey.api.APIManager;
import slm2015.hey.api.PostBase;

public class RegisterAPI extends PostBase {
    public RegisterAPI(String imei, String registerCode) {
        super(APIManager.HEY_SERVER_BASE_URL, "/api/user/register");
        this.setParam("deviceIdentity", imei);
        this.setParam("coupon", registerCode);
        this.TAG = "LoginAPI";
    }
}
