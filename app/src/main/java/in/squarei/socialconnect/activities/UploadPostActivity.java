package in.squarei.socialconnect.activities;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import eu.janmuller.android.simplecropimage.CropImage;
import eu.janmuller.android.simplecropimage.Util;
import in.squarei.socialconnect.R;
import in.squarei.socialconnect.network.ApiURLS;
import in.squarei.socialconnect.utils.CommonUtils;
import in.squarei.socialconnect.utils.Logger;
import in.squarei.socialconnect.utils.SharedPreferenceUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static in.squarei.socialconnect.interfaces.AppConstants.API_KEY;
import static in.squarei.socialconnect.network.ApiURLS.BASE_URL;
import static in.squarei.socialconnect.network.ApiURLS.UPLOAD_POST;

public class UploadPostActivity extends SocialConnectBaseActivity {

    public static final String TAG = "UploadPostActivity";
    public static final int REQUEST_CODE_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x3;
    public static String TEMP_PHOTO_FILE_NAME;
    String path = "";
    private LinearLayout linearUploadImage;
    private File mFileTemp, selectedFilePath;
    private Bitmap bitmap = null;
    private TextView tvPost;
    private EditText editPost, editPostTitle;
    private String clientId;
    private Response response = null;
    private ImageView ivImageToBeUpload;


    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_post);
        String states = Environment.getExternalStorageState();
        clientId = SharedPreferenceUtils.getInstance(context).getString(API_KEY);
        TEMP_PHOTO_FILE_NAME = System.currentTimeMillis() + clientId + ".jpg";
        if (Environment.MEDIA_MOUNTED.equals(states)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);
        }
        settingTitle(getResources().getString(R.string.post_upload_activity));
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(final String hostname, final SSLSession session) {
                return hostname.equalsIgnoreCase(ApiURLS.BASE_URL);
            }
        });
    }

    @Override
    protected void initViews() {
        linearUploadImage = (LinearLayout) findViewById(R.id.linearUploadImage);
        ivImageToBeUpload = (ImageView) findViewById(R.id.ivImageToBeUpload);
        editPost = (EditText) findViewById(R.id.editPost);
        editPostTitle = (EditText) findViewById(R.id.editPostTitle);
        tvPost = (TextView) findViewById(R.id.tvPost);
    }

    @Override
    protected void initContext() {
        context = UploadPostActivity.this;
        // currentActivity = UploadPostActivity.this;
        Logger.info(TAG, "==============initContext=======" + context);
    }

    @Override
    protected void initListners() {
        linearUploadImage.setOnClickListener(this);
        tvPost.setOnClickListener(this);
    }

    private void openGallery() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
    }

    private void takePicture() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            Uri mImageCaptureUri = null;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                mImageCaptureUri = Uri.fromFile(mFileTemp);
            } else {
                    /*
                     * The solution is taken from here: http://stackoverflow.com/questions/10042695/how-to-get-camera-result-as-a-uri-in-data-folder
            */
                mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
        } catch (ActivityNotFoundException e) {

            Log.d(TAG, "cannot take picture", e);
        }
    }

    private void startCropImage() {

        Intent intent = new Intent(context, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
        intent.putExtra(CropImage.SCALE, true);

        intent.putExtra(CropImage.ASPECT_X, 0);
        intent.putExtra(CropImage.ASPECT_Y, 0);

        startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("color upload 88888", +requestCode + "");
        switch (requestCode) {

            case REQUEST_CODE_TAKE_PICTURE:

                path = mFileTemp.getPath();
                bitmap = BitmapFactory.decodeFile(path);
                //      selectedFilePath = new File(path);
                Log.e("filepath", "**" + path);

                bitmap = Util.rotateImage(bitmap, -90);
                //  RotateBitmap rotateBitmap = new RotateBitmap(bitmap);
                // mImageView.setImageRotateBitmapResetBase(rotateBitmap, true);

                // image_view.setImageBitmap(bitmap);
                startCropImage();
                break;

            case REQUEST_CODE_GALLERY:
                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();

                    startCropImage();

                } catch (Exception e) {
                    Logger.info(TAG, "Error while creating temp file" + e);
                }
                //  upload_image.setText("Image upload successfully");
                break;

            case REQUEST_CODE_CROP_IMAGE:
                try {
                    path = data.getStringExtra(CropImage.IMAGE_PATH);
                    if (path == null) {
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
                selectedFilePath = new File(path);
                Logger.info("filepath", "**" + selectedFilePath);
                ivImageToBeUpload.setImageBitmap(bitmap);
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(
                context);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {

                    takePicture();

                } else if (items[item].equals("Choose from Library")) {

                    openGallery();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected boolean isActionBar() {
        return true;
    }

    @Override
    protected boolean isHomeButton() {
        return true;
    }

    @Override
    protected boolean isNavigationView() {
        return false;
    }

    @Override
    protected boolean isTabs() {
        return false;
    }

    @Override
    protected boolean isFab() {
        return false;
    }

    @Override
    protected boolean isDrawerListener() {
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearUploadImage:
                selectImage();
                break;
            case R.id.tvPost:
                //  new UploadFilesToServer().execute();
                uploadPost();
                break;
        }
    }

    private void uploadPost1() {

        Logger.info(TAG, "================file path====" + selectedFilePath);

        HashMap<String, Object> hm = new HashMap<>();
        hm.put("image", selectedFilePath);

        String url = BASE_URL + "upload.php";
        //   new CommonAsyncTaskAquery(1, context, this).getquery(url, hm);
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

    private void uploadPost() {

        Logger.info(TAG, "===========Client Id=========" + clientId);
        Logger.info(TAG, "===========File path=========" + selectedFilePath);
        String postText = editPost.getText().toString();
        String postTitle = editPostTitle.getText().toString();
        String filePath;
        final String content_type;
        // File file=new File(selectedFilePath);
        if (selectedFilePath != null) {
            content_type = getMimeType(selectedFilePath.getPath());
            filePath = selectedFilePath.getAbsolutePath();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
            RequestBody fileBody = RequestBody.create(MediaType.parse(content_type), selectedFilePath);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("post_type", content_type)
                    .addFormDataPart("title", postTitle)
                    .addFormDataPart("client-id", clientId)
                    // .addFormDataPart("posturl", "")
                    .addFormDataPart("description", postText)
                    .addFormDataPart("uploads", filePath.substring(filePath.lastIndexOf("/") + 1), fileBody)
                    .build();


/*            MultipartBody.Builder multipartBody=new MultipartBody.Builder().setType(MultipartBody.FORM);

            multipartBody.addFormDataPart();

            RequestBody requestBody1 =multipartBody.build();*/


            Logger.info(TAG, "===========requestBody=====" + requestBody);
            Request request = new Request.Builder()
                    .url(UPLOAD_POST)//UPLOAD_POST"http://squarei.in/api/v1/upload.php"
                    .post(requestBody)
                    .build();
            //  System.out.println("request url : " + request.url());
            if (context != null)
                CommonUtils.showprogressDialog(context, "Please wait", "Uploading...", false, false);
            //client.newCall(request).execute();
            tvPost.setEnabled(false);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    CommonUtils.cancelProgressDialog();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvPost.setEnabled(true);
                        }
                    });
                    tvPost.setEnabled(true);
                    Logger.info(TAG, "==============onFailure=========" + call + "====exception===" + e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    CommonUtils.cancelProgressDialog();
                    Logger.info(TAG, "==============response=========" + response);
                    String responseBody = response.body().string();
                    Logger.info(TAG, "==============response body=========" + responseBody);
                    try {
                        JSONObject jsonObject = new JSONObject(responseBody);
                        boolean error = jsonObject.getBoolean("error");

                        if (!error) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("commandResult");
                            final String message = jsonObject1.getString("message");
                            int success = jsonObject1.getInt("success");
                            if (success == 1) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                        setResult(RESULT_OK);
                                        finish();
                                    }
                                });
                                // toast(message, false);

                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvPost.setEnabled(true);
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.info(TAG, "===========onDestroy=====");
        CommonUtils.cancelProgressDialog();
    }

    private class UploadFilesToServer extends AsyncTask<String, Void, Void> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("uploading");
            progressDialog.setMessage("please wait...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            uploadPost();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }

    /*
    private class UploadFilesToServer1 extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("uploading");
            progressDialog.setMessage("please wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            uploadPost();
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String responseBody = null;
            try {
                if (response != null) {
                    responseBody = response.body().string();
                }

                System.out.println("response from server " + response.networkResponse());

            } catch (IOException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }
    }
*/
}
