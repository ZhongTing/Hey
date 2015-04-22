package slm2015.hey.api.issue;

import slm2015.hey.api.APICallback;
import slm2015.hey.api.MultipartPost;

public class RaiseIssueAPI extends MultipartPost{
    public RaiseIssueAPI(String baseUrl, String action, APICallback callback) {
        super(baseUrl, action, callback);
    }
}
