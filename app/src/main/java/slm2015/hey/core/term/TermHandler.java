package slm2015.hey.core.term;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
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
                List<Subject> subjects = parseSubjects(jsonObject.getJSONObject("subjects"));
                List<Term> descriptions = parseDescriptions(jsonObject.getJSONArray("descriptions"));
                List<Term> places = parsePlaces(jsonObject.getJSONArray("places"));

                callback.onReceiveRecommends(subjects, descriptions, places);
            }

            private List<Subject> parseSubjects(JSONObject object) throws JSONException {
                List<Subject> list = new ArrayList<>();
                Iterator<String> keys = object.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    Subject subject = new Subject(key);
                    JSONObject subjectRelativeObject = object.getJSONObject(key);
                    subject.getDescriptionList().addAll(this.parseDescriptions(subjectRelativeObject.getJSONArray("descriptions")));
                    subject.getPlaceList().addAll(this.parsePlaces(subjectRelativeObject.getJSONArray("descriptions")));
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
                for (int i = 0; i < array.length(); i++) {
                    list.add(new Term(array.getString(i)));
                }
                return list;
            }
        });
    }

    public interface TermHandlerCallback {
        public void onReceiveRecommends(List<Subject> subjects, List<Term> descriptions, List<Term> places);
    }
}
