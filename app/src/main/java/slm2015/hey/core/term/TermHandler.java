package slm2015.hey.core.term;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import slm2015.hey.api.user.PullRecommendsAPI;
import slm2015.hey.core.BaseAPIHandler;
import slm2015.hey.entity.Subject;
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
                List<Subject> subjects = parseSubjects(jsonObject.getJSONArray("subjects"));
                List<Term> descriptions = parseDescriptions(jsonObject.getJSONArray("descriptions"));
                List<Term> places = parsePlaces(jsonObject.getJSONArray("places"));

                callback.onReceiveRecommends(subjects, descriptions, places);
            }

            private List<Subject> parseSubjects(JSONArray array) throws JSONException {
                List<Subject> list = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    Subject subject = new Subject(object.getString("content"));
                    subject.getDescriptionList().addAll(this.parseDescriptions(object.getJSONArray("descriptions")));
                    subject.getPlaceList().addAll(this.parsePlaces(object.getJSONArray("places")));
                    list.add(subject);
                }
                return list;
            }

            private List<Term> parseDescriptions(JSONArray array) throws JSONException {
                List<Term> list = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    list.add(new Term(array.getString(i)));
                }
                return list;
            }

            private List<Term> parsePlaces(JSONArray array) throws JSONException {
                List<Term> list = new ArrayList<>();
                list.add(new Term("無"));
                for (int i = 0; i < array.length(); i++) {
                    list.add(new Term(array.getString(i)));
                }
                return list;
            }
        });
    }

    public interface TermHandlerCallback {
        void onReceiveRecommends(List<Subject> subjects, List<Term> descriptions, List<Term> places);
    }
}
