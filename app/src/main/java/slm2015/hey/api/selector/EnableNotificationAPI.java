package slm2015.hey.api.selector;

import slm2015.hey.api.APIManager;
import slm2015.hey.api.PostBase;

public class EnableNotificationAPI extends PostBase {
    public EnableNotificationAPI(int selectorId) {
        super(APIManager.HEY_SERVER_BASE_URL, "/api/filter/notification/enable");

        this.setHeader("Authorization", APIManager.getInstance().getAccessToken());

        this.setParam("filterId", String.valueOf(selectorId));

        this.TAG = "EnableNotificationAPI";
    }
}
