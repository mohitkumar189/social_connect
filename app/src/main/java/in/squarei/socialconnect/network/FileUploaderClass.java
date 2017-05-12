package in.squarei.socialconnect.network;

import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

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

    private void uploadFile(File file, Map<String, String> map, String url) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .build();

        RequestBody fileBody = RequestBody.create(MediaType.parse(content_type), file);


        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("type", content_type)
                .addFormDataPart("file", filePath.substring(filePath.lastIndexOf("/") + 1), fileBody)
                .addFormDataPart("userId", "")
                .build();


        Request request = new Request.Builder()
                .url("http://squarei.in/api/v1/upload.php")
                .post(requestBody)
                .build();

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