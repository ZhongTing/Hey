package slm2015.hey.api.selector;

import slm2015.hey.api.APIManager;
import slm2015.hey.api.GetBase;

public class ListSelectorAPI extends GetBase{
    public ListSelectorAPI() {
        super(APIManager.HEY_SERVER_BASE_URL, "/api/filter/list'");

        this.setHeader("Authorization", APIManager.getInstance().getAccessToken());

        this.TAG = "ListSelectorAPI";
    }
}
