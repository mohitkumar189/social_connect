package in.squarei.socialconnect.network;

import android.os.Handler;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by mohit kumar on 5/12/2017.
 */

public class FileUploaderClass {
    private static final int CONNECT_TIMEOUT = 10;
    private static final int WRITE_TIMEOUT = 30;
    private static final int READ_TIMEOUT = 30;
    private String filePath;
    private String content_type;
    private String fileName;

    private void uploadFile(File file, Map<String, String> map, String url, Handler handler) {
        getFileInfo(file);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .build();

        MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);

        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entrySet = iterator.next();
            multipartBody.addFormDataPart(entrySet.getKey(), entrySet.getValue());
        }
        RequestBody fileBody = RequestBody.create(MediaType.parse(content_type), file);
        multipartBody.addFormDataPart("file", filePath.substring(filePath.lastIndexOf("/") + 1), fileBody);
        RequestBody requestBody = multipartBody.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    private String getMimeType(String path) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(path).toLowerCase().trim();
        System.out.println("file extension : " + extension);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            return type;
        }
        return type;
    }

    private void getFileInfo(File file) {
        content_type = getMimeType(file.getPath());
        System.out.println("content type : " + content_type);
        filePath = file.getAbsolutePath();
        fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
    }

    // this is farjiwaada/// not in used anymore
    private void sendOnUrl(String url) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("v", "1.0");
        urlBuilder.addQueryParameter("user", "vogella");
        String url2 = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url2)
                .build();

        Request request2 = new Request.Builder()
                .header("Authorization", "your token")
                .url("https://api.github.com/users/vogella")
                .build();
    }
}
//   final String responseData = response.body().string();