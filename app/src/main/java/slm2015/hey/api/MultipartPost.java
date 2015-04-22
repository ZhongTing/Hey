package slm2015.hey.api;

import android.graphics.Bitmap;

import org.apache.http.Consts;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class MultipartPost extends APIBase {
    protected MultipartEntityBuilder postParams;

    public MultipartPost(String baseUrl, String action, Callback callback) {
        super(baseUrl, action, callback);

        this.httpRequest = new HttpPost(this.requestUrl);

        this.postParams = MultipartEntityBuilder.create();
        this.postParams.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
    }

    protected void setParam(String key, String value) {
        this.postParams.addPart(key, new StringBody(value, ContentType.MULTIPART_FORM_DATA.withCharset(Charset.forName(Consts.UTF_8.name()))));

    }

    protected void setFileParam(String key, Bitmap bitmap) {
        this.postParams.addBinaryBody(key, this.convertBitmapToBytes(bitmap), ContentType.MULTIPART_FORM_DATA, "image.jpg");
    }

    private byte[] convertBitmapToBytes(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    protected void doInit() throws UnsupportedEncodingException {
        ((HttpPost) this.httpRequest).setEntity(this.postParams.build());
    }
}