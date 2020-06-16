package com.grocery.gtohome.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.grocery.gtohome.R;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.model.SimpleResultModel;
import com.grocery.gtohome.session.SessionManager;
import com.grocery.gtohome.utils.Utilities;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

import static com.grocery.gtohome.api_client.Base_Url.BaseUrl;

@SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled", "JavascriptInterface"})
public class PayUMoneyActivity extends AppCompatActivity {
    WebView webView;
    private Utilities utilities;
    private SessionManager sessionManager;
    Handler mHandler = new Handler();
    //String base_url = "https://test.payu.in";
    String base_url = " https://secure.payu.in";
    String action1 = "";
    String productInfo = "";
    private String hash = "";
    //******************************************
    String TAG ="PayuMoney_Activity";
    String txnid ="", amount ="", phone ="",
            prodname ="Grocery", firstname ="", email ="",
            merchantId ="5733850", merchantkey="HBzjus8Q", salt="4YxPZIqeAl";
    String SUCCESS_URL="https://www.payumoney.com/mobileapp/payumoney/success.php";
    String Failer_URL="https://www.payumoney.com/mobileapp/payumoney/failure.php";
    private String OrderId, Order_status_id,Customer_Id;
    private String PaymentType,WalletDeductAmount,wk_wallet_payment;


    @SuppressLint({"WrongConstant", "JavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_pay_u_money);

        utilities = Utilities.getInstance(PayUMoneyActivity.this);
        sessionManager = new SessionManager(PayUMoneyActivity.this);
        phone = sessionManager.getUser().getTelephone();
        firstname = sessionManager.getUser().getFirstname();
        email = sessionManager.getUser().getEmail();
        Customer_Id = sessionManager.getUser().getCustomerId();
        webView=findViewById(R.id.webview);

        if (getIntent()!=null){
           amount=getIntent().getStringExtra("amount");
            OrderId=getIntent().getStringExtra("OrderId");
            Order_status_id=getIntent().getStringExtra("Order_status_id");
            PaymentType=getIntent().getStringExtra("PaymentType");
            WalletDeductAmount=getIntent().getStringExtra("walletDeductAmount");
            wk_wallet_payment=getIntent().getStringExtra("wk_wallet_payment");
        }

        Random rand = new Random();
        String rndm = Integer.toString(rand.nextInt())
                + (System.currentTimeMillis() / 1000L);
        txnid = hashCal("SHA-256", rndm).substring(0, 20);

        JSONObject productInfoObj = new JSONObject();
        JSONArray productPartsArr = new JSONArray();
        JSONObject productPartsObj1 = new JSONObject();
        JSONObject paymentIdenfierParent = new JSONObject();
        JSONArray paymentIdentifiersArr = new JSONArray();
        JSONObject paymentPartsObj1 = new JSONObject();
        JSONObject paymentPartsObj2 = new JSONObject();
        try {
            // Payment Parts
            productPartsObj1.put("name", firstname);
            productPartsObj1.put("description", "grocery product");
            productPartsObj1.put("value", "1");
            productPartsObj1.put("isRequired", "true");
            productPartsObj1.put("settlementEvent", "EmailConfirmation");
            productPartsArr.put(productPartsObj1);
            productInfoObj.put("paymentParts", productPartsArr);

            // Payment Identifiers
            paymentPartsObj1.put("field", "CompletionDate");
            paymentPartsObj1.put("value", "01/01/2020");
            paymentIdentifiersArr.put(paymentPartsObj1);

            paymentPartsObj2.put("field", "TxnId");
            paymentPartsObj2.put("value", txnid);
            paymentIdentifiersArr.put(paymentPartsObj2);

            paymentIdenfierParent.put("paymentIdentifiers",
                    paymentIdentifiersArr);
            productInfoObj.put("", paymentIdenfierParent);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        productInfo = productInfoObj.toString();

        Log.e("TAG", productInfoObj.toString());

        hash = hashCal("SHA-512", merchantkey + "|" + txnid + "|" + amount
                + "|" + productInfo + "|" + firstname + "|" + email
                + "|||||||||||" + salt);

        action1 = base_url.concat("/_payment");

        //**************************************************************
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // TODO Auto-generated method stub
                Toast.makeText(PayUMoneyActivity.this, "Oh no!"+description,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                // TODO Auto-generated method stub
               // Toast.makeText(PayUMoneyActivity.this, "SslError! " + error,
                //        Toast.LENGTH_SHORT).show();
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Toast.makeText(PayUMoneyActivity.this, "Page Started! " + url,Toast.LENGTH_SHORT).show();
                if (url.equals(SUCCESS_URL)) {
                    Toast.makeText(PayUMoneyActivity.this, "Success! " + url, Toast.LENGTH_SHORT).show();
                } else {
                  //  Toast.makeText(PayUMoneyActivity.this, "Failure! " + url, Toast.LENGTH_SHORT).show();
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

             @Override
             public void onPageFinished(WebView view, String url) {
             super.onPageFinished(view, url);

          //   Toast.makeText(PayMentGateWay.this, "" + url, Toast.LENGTH_SHORT).show();

                     if (url.equals(SUCCESS_URL)) {
                         if (PaymentType.equalsIgnoreCase("Wallet")){
                             UpdatePaymentStatus("PayU: "+txnid);
                         }else {
                             UpdateOrderStatus("PayU: "+txnid);
                         }


                     } else if (url.equals(Failer_URL)) {
                         Toast.makeText(PayUMoneyActivity.this, "Fail: " + url, Toast.LENGTH_SHORT).show();
                     }
                     super.onPageFinished(view, url);
                 }


        });

        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setCacheMode(2);
        webView.getSettings().setDomStorageEnabled(true);
        webView.clearHistory();
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setLoadWithOverviewMode(false);

        webView.addJavascriptInterface(new PayUJavaScriptInterface(PayUMoneyActivity.this),
                "PayUMoney");
        Map<String, String> mapParams = new HashMap<String, String>();
        mapParams.put("key", merchantkey);
        mapParams.put("hash", hash);
        mapParams.put("txnid", txnid);
        mapParams.put("service_provider", "payu_paisa");
        mapParams.put("amount", amount);
        mapParams.put("firstname", firstname);
        mapParams.put("email", email);
        mapParams.put("phone", phone);

        mapParams.put("productinfo", productInfo);
        mapParams.put("surl", SUCCESS_URL);
        mapParams.put("furl", Failer_URL);
        mapParams.put("lastname", "");

        mapParams.put("address1", "");
        mapParams.put("address2", "");
        mapParams.put("city", "");
        mapParams.put("state", "");

        mapParams.put("country", "");
        mapParams.put("zipcode", "");
        mapParams.put("udf1", "");
        mapParams.put("udf2", "");

        mapParams.put("udf3", "");
        mapParams.put("udf4", "");
        mapParams.put("udf5", "");
        // mapParams.put("pg", (empty(PayMentGateWay.this.params.get("pg"))) ?
        // ""
        // : PayMentGateWay.this.params.get("pg"));
        webview_ClientPost(webView, action1, mapParams.entrySet());


    }

    @SuppressLint("CheckResult")
    private void UpdatePaymentStatus(String razorpayPaymentID) {
        final ProgressDialog progressDialog = new ProgressDialog(PayUMoneyActivity.this, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        apiInterface.UpdateWalletStatus(Customer_Id,amount,razorpayPaymentID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<SimpleResultModel>() {
                    @Override
                    public void onNext(SimpleResultModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {
                                utilities.dialogOKOnBack(PayUMoneyActivity.this, getString(R.string.validation_title),
                                        response.getMsg(), getString(R.string.ok), true);

                            } else {
                                //Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
                                utilities.dialogOK(PayUMoneyActivity.this, getString(R.string.validation_title),
                                        response.getMsg(), getString(R.string.ok), false);
                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        //Handle error
                        progressDialog.dismiss();
                        Log.e("Categ_product_error", e.toString());

                        if (e instanceof HttpException) {
                            int code = ((HttpException) e).code();
                            switch (code) {
                                case 403:
                                    break;
                                case 404:
                                    //Toast.makeText(EmailSignupActivity.this, R.string.email_already_use, Toast.LENGTH_SHORT).show();
                                    break;
                                case 409:
                                    break;
                                default:
                                    // Toast.makeText(EmailSignupActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } else {
                            if (TextUtils.isEmpty(e.getMessage())) {
                                // Toast.makeText(EmailSignupActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(EmailSignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void UpdateOrderStatus(String PaymentID) {
        final ProgressDialog progressDialog = new ProgressDialog(PayUMoneyActivity.this, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        apiInterface.UpdateOrderStatus(Customer_Id,Order_status_id,OrderId,PaymentID, WalletDeductAmount, wk_wallet_payment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<SimpleResultModel>() {
                    @Override
                    public void onNext(SimpleResultModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {
                                Toast.makeText(PayUMoneyActivity.this, "Payment successfully done! " + PaymentID, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PayUMoneyActivity.this, OrderSuccess_Activity.class);
                                intent.putExtra("OrderId",OrderId);
                                startActivity(intent);
                            } else {
                                //Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
                                utilities.dialogOK(PayUMoneyActivity.this, getString(R.string.validation_title),
                                        response.getMsg(), getString(R.string.ok), false);
                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        //Handle error
                        progressDialog.dismiss();
                        Log.e("Categ_product_error", e.toString());

                        if (e instanceof HttpException) {
                            int code = ((HttpException) e).code();
                            switch (code) {
                                case 403:
                                    break;
                                case 404:
                                    //Toast.makeText(EmailSignupActivity.this, R.string.email_already_use, Toast.LENGTH_SHORT).show();
                                    break;
                                case 409:
                                    break;
                                default:
                                    // Toast.makeText(EmailSignupActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } else {
                            if (TextUtils.isEmpty(e.getMessage())) {
                                // Toast.makeText(EmailSignupActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(EmailSignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                    }
                });
    }

    public String hashCal(String type,String str){
        byte[] hashseq=str.getBytes();
        StringBuffer hexString = new StringBuffer();
        try{
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();

            for (int i=0;i<messageDigest.length;i++) {
                String hex=Integer.toHexString(0xFF & messageDigest[i]);
                if(hex.length()==1) hexString.append("0");
                hexString.append(hex);
            }

        }catch(NoSuchAlgorithmException nsae){ }

        return hexString.toString();


    }

    public class PayUJavaScriptInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        PayUJavaScriptInterface(Context c) {
            mContext = c;
        }

        public void success(long id, final String paymentId) {

            mHandler.post(new Runnable() {

                public void run() {
                    mHandler = null;
                    Toast.makeText(PayUMoneyActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PayUMoneyActivity.this, OrderSuccess_Activity.class);
                    intent.putExtra("OrderId",OrderId);
                    startActivity(intent);
                }
            });
        }
    }


    public void webview_ClientPost(WebView webView, String url,
                                   Collection<Map.Entry<String, String>> postData) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html><head></head>");
        sb.append("<body onload='form1.submit()'>");
        sb.append(String.format("<form id='form1' action='%s' method='%s'>",
                url, "post"));
        for (Map.Entry<String, String> item : postData) {
            sb.append(String.format(
                    "<input name='%s' type='hidden' value='%s' />",
                    item.getKey(), item.getValue()));
        }
        sb.append("</form></body></html>");
        Log.d(TAG, "webview_ClientPost called");
        webView.loadData(sb.toString(), "text/html", "utf-8");
    }

    public boolean empty(String s) {
        if (s == null || s.trim().equals(""))
            return true;
        else
            return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result Code is -1 send from Payumoney activity
        Log.d("MainActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {
            TransactionResponse transactionResponse = data.getParcelableExtra( PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE );

            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {

                if(transactionResponse.getTransactionStatus().equals( TransactionResponse.TransactionStatus.SUCCESSFUL )){
                    //Success Transaction
                } else{
                    //Failure Transaction
                }

                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();

                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();
            }
           // else if (resultModel != null && resultModel.getError() != null) {
            //    Log.d(TAG, "Error response : " + resultModel.getError().getTransactionResponse());
           // }
        else {
                Log.d(TAG, "Both objects are null!");
            }
        }
    }
}
