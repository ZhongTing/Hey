package slm2015.hey.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Converter {
    static public Date convertToDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.TAIWAN);
        format.setTimeZone(TimeZone.getTimeZone("IST"));
        Date result = null;
        try {
            result = format.parse(date);
            Log.e("Converter", result.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
