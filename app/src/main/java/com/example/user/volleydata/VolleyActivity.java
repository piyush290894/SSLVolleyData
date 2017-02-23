package com.example.user.volleydata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


public class VolleyActivity extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);
        tv= (TextView) findViewById(R.id.volleyMessage);

        String url="https://www.google.com";
        HurlStack hurlStack=new HurlStack(){
            @Override
            protected HttpURLConnection createConnection(URL url) throws IOException {
                HttpsURLConnection httpsURLConnection= (HttpsURLConnection) super.createConnection(url);
                try {
                    httpsURLConnection.setSSLSocketFactory(getSSLSocketFactory());
                    //httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
                }catch (Exception e){
                    e.printStackTrace();
                }

                return httpsURLConnection;
            }
        };
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                tv.setText("Response is: " + response.substring(0, 500));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tv.setText("That didn't work!");
            }
        });
        final RequestQueue queue= Volley.newRequestQueue(this, hurlStack);
        queue.add(stringRequest);



    }

    private TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers){
        final X509TrustManager originalTrustManager=(X509TrustManager)trustManagers[0];
        return new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                        if(certs!=null && certs.length>0){
                            certs[0].checkValidity();
                        }else{
                            originalTrustManager.checkClientTrusted(certs,authType);
                        }
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                        if(certs!=null && certs.length>0){
                            certs[0].checkValidity();
                        }
                        else {
                            originalTrustManager.checkClientTrusted(certs,authType);
                        }
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return originalTrustManager.getAcceptedIssuers();
                    }
                }
        };
    }

    private SSLSocketFactory getSSLSocketFactory() throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        CertificateFactory cf=CertificateFactory.getInstance("X.509");
        InputStream caInput=getResources().openRawResource(R.raw.selfsigned);

        Certificate ca=cf.generateCertificate(caInput);
        caInput.close();

        KeyStore keyStore=KeyStore.getInstance("JKS");
        keyStore.load(null,null);
        keyStore.setCertificateEntry("ca",ca);

        String tmfAlgorithm= TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf=TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);
        TrustManager[] wrappedTrustManager=getWrappedTrustManagers(tmf.getTrustManagers());
        SSLContext sslContext=SSLContext.getInstance("TLS");
        sslContext.init(null, wrappedTrustManager, null);

        return sslContext.getSocketFactory();

    }
}