package slm2015.hey.api.selector;

import slm2015.hey.api.APIManager;
import slm2015.hey.api.PostBase;

public class RemoveSelectorAPI extends PostBase {
    public RemoveSelectorAPI(int selectorId) {
        super(APIManager.HEY_SERVER_BASE_URL, "/api/filter/remove");

        this.setHeader("Authorization", APIManager.getInstance().getAccessToken());

        this.setParam("filterId", String.valueOf(selectorId));

        this.TAG = "RemoveSelectorAPI";
    }
}
