package com.github.tonytangandroid.client.certificate.demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.chariotsolutions.example.R;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends Activity implements View.OnClickListener {
    TextView tvResultCodeWithContent;
    private ApacheHttpClientApi apacheHttpClientApi;
    private OkHttpClient okHttpClient;
    private Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        tvResultCodeWithContent = findViewById(R.id.tv_result_code_with_response);
        findViewById(R.id.btn_apache_2_requests).setOnClickListener(this);
        findViewById(R.id.btn_apache_1_request).setOnClickListener(this);
        findViewById(R.id.btn_okhttp_1_request).setOnClickListener(this);
        findViewById(R.id.btn_okhttp_2_requests).setOnClickListener(this);
    }


    @SuppressLint("SetTextI18n")
    private void updateOutput(String text) {
        tvResultCodeWithContent.setText(tvResultCodeWithContent.getText() + "\n\n" + text);
    }


    private void doTheWorkUsingApache() {
        String result = apacheHttpClientApi.doGet(Constants.SERVER_URL);
        int responseCode = apacheHttpClientApi.getLastResponseCode();
        handler.post(() -> appendingText(responseCode, result));
    }

    private void doTheWorkUsingOkhttp() {

        try {
            Request request = new Request.Builder().url(Constants.SERVER_URL).build();
            Response response = okHttpClient.newCall(request).execute();
            int code = response.code();
            ResponseBody body = response.body();
            String result = body == null ? null : body.string();
            handler.post(() -> appendingText(code, result));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void appendingText(int responseCode, String result) {
        updateOutput(responseCode + ":" + result);
    }


    @Override
    public void onClick(View v) {
        tvResultCodeWithContent.setText("");
        switch (v.getId()) {
            case R.id.btn_apache_2_requests:
                runApache(2);
                break;
            case R.id.btn_apache_1_request:
                runApache(1);
                break;
            case R.id.btn_okhttp_2_requests:
                runOkHttp(2);
                break;
            case R.id.btn_okhttp_1_request:
                runOkHttp(1);
                break;
        }

    }

    private void runApache(int count) {

        initApacheClient();
        Thread thread = new Thread(() -> execute(count));
        thread.start();
    }

    private void initApacheClient() {
        apacheHttpClientApi = new ApacheHttpClientApi();
    }

    private void execute(int count) {
        for (int i = 0; i < count; i++) {
            doTheWorkUsingApache();
        }
    }

    private void runOkHttp(int count) {
        initOkHttpClient();
        Thread thread = new Thread(() -> executeOkhttp(count));
        thread.start();
    }

    /**
     * If you create SSLSocketFactory there, only the first request will succeed.
     * However, if you create your SSLSocketFactory in the forloop, which create a new
     * sSLSocketFactory every time. It will always work.
     */
    private void initOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        X509TrustManager trustManager = ClientCertificateUtil.provideX509TrustManager();
        SSLSocketFactory sslSocketFactory = ClientCertificateUtil.provideSSLSocketFactory();
        builder.sslSocketFactory(sslSocketFactory, trustManager);
        okHttpClient = builder.build();
    }

    private void executeOkhttp(int count) {
        for (int i = 0; i < count; i++) {
            doTheWorkUsingOkhttp();
        }
    }
}
