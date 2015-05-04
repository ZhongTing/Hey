package slm2015.hey.core.issue;

import android.content.Context;

import org.json.JSONException;

import slm2015.hey.api.user.PullRecommendsAPI;
import slm2015.hey.core.BaseAPIHandler;
import slm2015.hey.entity.Issue;

public class IssueHandler extends BaseAPIHandler{
    //Todo implement catch, pass, raise core here

    public IssueHandler(Context context) {
        super(context);
    }

    public void raise(Issue issue) {
        this.runAPI(new PullRecommendsAPI(), new Callback() {
            @Override
            protected void onSuccess(Object jsonObject) throws JSONException {

            }
        });
    }
}
