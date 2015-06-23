package slm2015.hey.api.issue;

import slm2015.hey.api.APIManager;
import slm2015.hey.api.PostBase;

public class AddSelectorAPI extends PostBase {
    public AddSelectorAPI(String selector) {
        super(APIManager.HEY_SERVER_BASE_URL, "/api/filter/add");

        this.setHeader("Authorization", APIManager.getInstance().getAccessToken());

        this.setParam("subject", selector);

        this.TAG = "AddSelectorAPI";
    }
}
