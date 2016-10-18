package com.zta.uploadfiletoserverdemo;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zta.uploadfiletoserverdemo.API.ApiResponse;
import com.zta.uploadfiletoserverdemo.API.ApiService;
import com.zta.uploadfiletoserverdemo.Utility.AppConfig;
import com.zta.uploadfiletoserverdemo.Utility.Common;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE=1;
    final int CHOOSE_FILE_REQUEST=2;

    @BindView(R.id.select_file_button)
    Button selectFileButton;
    @BindView(R.id.upload_file_button)
    Button uploadFileButton;
    @BindView(R.id.select_file_image_view)
    ImageView selectedFileImageView;
    @BindView(R.id.selected_file_path_text_view)
    TextView selectedFilePathView;

    Context context;
    String filePath;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context= MainActivity.this;
        ButterKnife.bind(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                    choosefile();
                } else {
                    //permission not  granted
                    Toast.makeText(context,"Without permission cannot select file. :(", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case CHOOSE_FILE_REQUEST:
                //file selected RESULT_OK
                if(resultCode== Activity.RESULT_OK){

                    filePath= Common.getSelectedFilePath(context, data.getData());

                    if(Common.isImageFile(filePath)){
                        selectedFilePathView.setVisibility(View.GONE);
                        selectedFileImageView.setVisibility(View.VISIBLE);

                        Glide.with(context)
                                .load(new File(filePath)).into(selectedFileImageView);

                    }else {
                        selectedFileImageView.setVisibility(View.GONE);
                        selectedFilePathView.setVisibility(View.VISIBLE);
                        selectedFilePathView.setText("File Path: " +filePath);
                    }
                }
                break;
        }
    }

    @OnClick(R.id.select_file_button)
    public void onSelectFileButtonClick(){

        if(Build.VERSION.SDK_INT>=23){
                checkPermissionGranted();
            return;
        }else {
            choosefile();
        }
    }

    @OnClick(R.id.upload_file_button)
    public void onUploadFileButtonClick(){
        uploadFile();
    }

    /**
     * checks WRITE_EXTERNAL_STORAGE permission is granted
     * if FALSE: requests to get permission
     * if TRUE: shows file chooser
     */
    private void checkPermissionGranted(){
        //checking if permission granted
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                //permission not granted

                     //displaying Snackbar to get Permission
                     Snackbar.make(findViewById(android.R.id.content), "Need Permission to select file", Snackbar.LENGTH_LONG)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                //Requesting to get Permission
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                            }
                        })
                        .setActionTextColor(context.getResources().getColor(R.color.colorPrimary))
                        .show();

        }else {
            //already permission granted

            //show file selector
            choosefile();

        }
    }

    /**
     * shows file selector
     */
    private void choosefile(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, CHOOSE_FILE_REQUEST);
    }

    /**
     * uploads selected file to servers
     */
    private void uploadFile() {
        //displaying progress dialog
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        Map<String, RequestBody> map = new HashMap<>();
        File file = new File(filePath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        map.put("file\"; filename=\"" + file.getName() + "", requestBody);
        ApiService getResponse = AppConfig.getRetrofit().create(ApiService.class);


        Call<ApiResponse> call = getResponse.uploadFileService(map);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                ApiResponse serverResponse = response.body();
                progressDialog.dismiss();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(),Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Fail to Upload file",Toast.LENGTH_SHORT).show();
                    Log.v("Response", serverResponse.toString());
                }
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error Something went wrong... :(",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
