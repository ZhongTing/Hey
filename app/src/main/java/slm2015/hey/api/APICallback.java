package slm2015.hey.api;
import org.json.JSONException;

public interface APICallback {
    public void requestCallback(boolean isValid, Object result) throws JSONException;
}
