package slm2015.hey.core.selector;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import slm2015.hey.api.selector.AddSelectorAPI;
import slm2015.hey.api.selector.EnableNotificationAPI;
import slm2015.hey.api.selector.ListSelectorAPI;
import slm2015.hey.api.selector.RemoveSelectorAPI;
import slm2015.hey.core.BaseAPIHandler;
import slm2015.hey.entity.Selector;

public class SelectorHandler extends BaseAPIHandler {
    public SelectorHandler(Context context) {
        super(context);
    }

    public void addSelector(final String selector, final AddSelectorCallBack callBack) {
        this.runAPI(new AddSelectorAPI(selector), new Callback() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                callBack.onReceiveSelectorId(jsonObject.getInt("id"));
            }
        });
    }

    public void listSelector(final ListSelectorCallBack callback){
        this.runAPI(new ListSelectorAPI(), new Callback() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                JSONArray issueJSONArray = jsonObject.getJSONArray("filters");
                callback.onReceiveFilterList(parseSelectorArray(issueJSONArray));
            }

            private List<Selector> parseSelectorArray(JSONArray array) throws JSONException{
                List<Selector> list = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    Selector selector = new Selector(jsonObject.getString("subject"));
                    selector.setId(jsonObject.getInt("id"));
                    list.add(selector);
                }
                return list;
            }
        });
    }

    public void removeSelector(int selectorId, final RemoveSelectorCallBack callBack){
        this.runAPI(new RemoveSelectorAPI(selectorId), new Callback() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                callBack.onRemoeveSuccess();
            }
        });
    }

    public void enableNotification(int selectorId, final EnableNotificationCallBack callBack){
        this.runAPI(new EnableNotificationAPI(selectorId), new Callback() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                callBack.onEnableSuccess();
            }
        });
    }

    public void disableNotification(int selectorId, final DisableNotificationCallBack callBack){
        this.runAPI(new EnableNotificationAPI(selectorId), new Callback() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                callBack.onDisableSuccess();
            }
        });
    }

    public interface EnableNotificationCallBack {
        void onEnableSuccess();
    }

    public interface DisableNotificationCallBack {
        void onDisableSuccess();
    }

    public interface RemoveSelectorCallBack {
        void onRemoeveSuccess();
    }

    public interface AddSelectorCallBack {
        void onReceiveSelectorId(int id);
    }

    public interface ListSelectorCallBack {
        void onReceiveFilterList(List<Selector> selectorList);
    }
}
