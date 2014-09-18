package com.bitpay.androidsdk.android;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.bitpay.androidsdk.R;
import com.bitpay.androidsdk.model.Invoice;

public class InvoiceActivity extends Activity {

    public static final String INVOICE = "invoice";
    public static final String PRIVATE_KEY = "privateKey";

    private String mEcKey = null;
    private Invoice mInvoice = null;
    private BitPayAndroid.GetClientTask clientTask;
    private BitPayAndroid.FollowInvoiceStatusTask invoiceTask;
    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(INVOICE, mInvoice);
        outState.putString(PRIVATE_KEY, mEcKey);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        if (savedInstanceState != null) {
            mInvoice = savedInstanceState.getParcelable(INVOICE);
            mEcKey = savedInstanceState.getString(PRIVATE_KEY);
        } else {
            mInvoice = getIntent().getParcelableExtra(INVOICE);
            mEcKey = getIntent().getStringExtra(PRIVATE_KEY);
        }
        webView = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100 && progressBar.getVisibility() == ProgressBar.GONE) {
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                }
                progressBar.setProgress(progress);
                if (progress == 100) {
                    progressBar.setVisibility(ProgressBar.GONE);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (clientTask != null) {
            clientTask.cancel(true);
        }
        if (invoiceTask != null) {
            invoiceTask.cancel(true);
        }
        webView.stopLoading();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (webView.getUrl() == null || webView.getProgress() != 100) {
            webView.loadUrl(mInvoice.getUrl());
        }
        clientTask = new BitPayAndroid.GetClientTask() {
            @Override
            protected void onPostExecute(final BitPayAndroid bitPay) {
                clientTask = null;
                if (bitPay != null) {

                    invoiceTask = new BitPayAndroid.FollowInvoiceStatusTask(bitPay) {

                        @Override
                        public void onStatePaid() {
                            finish();
                        }

                        @Override
                        public void onStateConfirmed() {
                            finish();
                        }

                        @Override
                        public void onStateComplete() {
                            finish();
                        }

                        @Override
                        public void onStateExpired() {
                            finish();
                        }
                        @Override
                        public void onStateInvalid() {
                            finish();
                        }

                    };
                    invoiceTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mInvoice.getId());
                }
            }
        };
        clientTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mEcKey);
    }

}
