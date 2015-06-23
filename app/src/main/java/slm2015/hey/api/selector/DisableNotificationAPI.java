package slm2015.hey.api.selector;

import slm2015.hey.api.APIManager;
import slm2015.hey.api.PostBase;

public class DisableNotificationAPI extends PostBase{
    public DisableNotificationAPI(int selectorId) {
        super(APIManager.HEY_SERVER_BASE_URL, "/api/filter/notification/disable");

        this.setHeader("Authorization", APIManager.getInstance().getAccessToken());

        this.setParam("filterId", String.valueOf(selectorId));

        this.TAG = "EnableNotificationAPI";
    }
}
