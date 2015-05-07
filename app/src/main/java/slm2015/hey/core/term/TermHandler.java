package slm2015.hey.core.term;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import slm2015.hey.api.user.PullRecommendsAPI;
import slm2015.hey.core.BaseAPIHandler;
import slm2015.hey.entity.Term;

public class TermHandler extends BaseAPIHandler {
    private TermHandlerCallback callback;

    public TermHandler(Context context, TermHandlerCallback callback) {
        super(context);
        this.callback = callback;
    }

    public void loadRecommends() {
        this.runAPI(new PullRecommendsAPI(), new Callback() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                List<Term> subjects = parseTermArray(jsonObject.getJSONArray("subject"));
                List<Term> descriptions = parseTermArray(jsonObject.getJSONArray("description"));
                List<Term> places = parseTermArray(jsonObject.getJSONArray("place"));

                callback.onReceiveRecommends(subjects, descriptions, places);
            }

            private List<Term> parseTermArray(JSONArray array) throws JSONException {
                List<Term> list = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    list.add(new Term(array.getString(i)));
                }
                return list;
            }
        });
    }

    public interface TermHandlerCallback {
        public void onReceiveRecommends(List<Term> subjects, List<Term> descriptions, List<Term> places);
    }
}
