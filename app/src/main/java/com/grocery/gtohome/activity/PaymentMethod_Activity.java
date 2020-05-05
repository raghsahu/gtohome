package com.grocery.gtohome.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.grocery.gtohome.R;
import com.grocery.gtohome.adapter.ShippingMethodAdapter;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.databinding.ActivityPymentMethodBinding;
import com.grocery.gtohome.model.create_order.CreateOrderModel;
import com.grocery.gtohome.model.shipping_method.ShippingMethod;
import com.grocery.gtohome.session.SessionManager;
import com.grocery.gtohome.utils.Connectivity;
import com.grocery.gtohome.utils.Utilities;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

import static com.grocery.gtohome.api_client.Base_Url.BaseUrl;

public class PaymentMethod_Activity extends AppCompatActivity implements ShippingMethodAdapter.AdapterCallback{
    ActivityPymentMethodBinding binding;
    private String TotalPrice;
    private Utilities utilities;
    private SessionManager sessionManager;
    private String Customer_Id,CountryId,ZoneId;
    private String payment_code="", payment_title="", AddressId;
    ShippingMethodAdapter friendsAdapter;
    private String SubTitle="",SubCode="";
    //******************************************
    PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();
    //declare paymentParam object
    PayUmoneySdkInitializer.PaymentParam paymentParam = null;
    String TAG ="Payment_Activity";
    String txnid ="", amount ="", phone ="",
            prodname ="Grocery", firstname ="", email ="",
            merchantId ="5733850", merchantkey="HBzjus8Q", salt="4YxPZIqeAl";
    String udf1="",udf2="",udf3="",udf4="",udf5="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_pyment_method);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pyment_method);
        utilities = Utilities.getInstance(PaymentMethod_Activity.this);
        sessionManager = new SessionManager(PaymentMethod_Activity.this);
        Customer_Id = sessionManager.getUser().getCustomerId();
        binding.toolbar.tvToolbar.setText("Payment Method");

        phone=sessionManager.getUser().getTelephone();
        firstname=sessionManager.getUser().getFirstname();
        email=sessionManager.getUser().getEmail();
        phone=sessionManager.getUser().getTelephone();

        binding.toolbar.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (getIntent()!=null){
            TotalPrice=getIntent().getStringExtra("TotalPrice");
            CountryId=getIntent().getStringExtra("CountryId");
            ZoneId=getIntent().getStringExtra("ZoneId");
            AddressId=getIntent().getStringExtra("AddressId");
            amount=TotalPrice.substring(1);
        }

        if (Connectivity.isConnected(PaymentMethod_Activity.this)){
            getShippingMethod();
        }else {
            utilities.dialogOK(PaymentMethod_Activity.this, getString(R.string.validation_title), getString(R.string.please_check_internet),
                    getString(R.string.ok), false);
        }

        //******************************************
        binding.rgPaymentMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_cod:
                       payment_code="cod";
                       payment_title="Cash On Delivery";
                        break;
                    case R.id.radio_payu:
                        payment_code="payu";
                        payment_title="PayUMoney";
                        break;
                }
            }
        });

        binding.tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AddressId.isEmpty()){
                    if (payment_code.isEmpty() && payment_code.equals("")) {
                        utilities.dialogOK(PaymentMethod_Activity.this, getString(R.string.validation_title),
                                "Please select payment method", getString(R.string.ok), false);
                    } else {
                        if (SubTitle.isEmpty() && SubTitle.equals("")) {
                            utilities.dialogOK(PaymentMethod_Activity.this, getString(R.string.validation_title),
                                    "Please select shipping method", getString(R.string.ok), false);
                        } else {
                        String Et_Comment=binding.etComments.getText().toString();
                        if (Connectivity.isConnected(PaymentMethod_Activity.this)){
                            CreateOrder(Customer_Id,Et_Comment,AddressId,payment_code,payment_title,SubCode,SubTitle,TotalPrice);
                        }else {
                            utilities.dialogOK(PaymentMethod_Activity.this, getString(R.string.validation_title), getString(R.string.please_check_internet),
                                    getString(R.string.ok), false);
                        }
                        }
                    }
                }else {
                    utilities.dialogOK(PaymentMethod_Activity.this, getString(R.string.validation_title),
                            "Address not found", getString(R.string.ok), false);
                }
            }
        });


    }

    @SuppressLint("CheckResult")
    private void CreateOrder(String customer_id, String et_comment, String addressId, final String payment_code, String payment_title,
                             String subCode, String subTitle, String totalPrice) {
        final ProgressDialog progressDialog = new ProgressDialog(PaymentMethod_Activity.this, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("customer_id", customer_id);
        map.put("comment", et_comment);
        map.put("address_id", addressId);
        map.put("payment_method[code]", payment_code);
        map.put("payment_method[title]", payment_title);
        map.put("shipping_method[code]", subCode);
        map.put("shipping_method[title]", subTitle);
        map.put("subtotal", totalPrice);

        apiInterface.CreateOrder(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<CreateOrderModel>() {
                    @Override
                    public void onNext(CreateOrderModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_order", "" + response.getStatus());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {
                                if (payment_code.equals("cod")){
                                    Intent intent = new Intent(PaymentMethod_Activity.this, OrderSuccess_Activity.class);
                                    intent.putExtra("OrderId",response.getOrderId().toString());
                                    startActivity(intent);
                                }else {

                                    Intent intent = new Intent(PaymentMethod_Activity.this, PayUMoneyActivity.class);
                                    intent.putExtra("OrderId",response.getOrderId().toString());
                                    startActivity(intent);

                              //  CallPaymentgateway();
                                }


                            } else {
                                //Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
                                utilities.dialogOK(PaymentMethod_Activity.this, getString(R.string.validation_title),
                                        "Order not submit, error", getString(R.string.ok), true);
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

    private void CallPaymentgateway() {
        Random rand = new Random();
        String rndm = Integer.toString(rand.nextInt())+(System.currentTimeMillis() / 1000L);
        txnid=hashCal("SHA-256",rndm).substring(0,20);


        builder.setAmount(amount)                          // Payment amount
                .setTxnId(txnid)                     // Transaction ID
                .setPhone("7879014631")                   // User Phone number
                .setProductName(prodname)                   // Product Name or description
                .setFirstName(firstname)                              // User First name
                .setEmail("raghsahu.786@gmail.com")              // User Email ID
                .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")     // Success URL (surl)
                .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")     //Failure URL (furl)
                .setUdf1("")
                .setUdf2("")
                .setUdf3("")
                .setUdf4("")
                .setUdf5("")
                .setUdf6("")
                .setUdf7("")
                .setUdf8("")
                .setUdf9("")
                .setUdf10("")
                .setIsDebug(true)                              // Integration environment - true (Debug)/ false(Production)
                .setKey(merchantkey)                        // Merchant key
                .setMerchantId(merchantId);

        try {
            paymentParam = builder.build();
            // generateHashFromServer(paymentParam );
            getHashkey();
        } catch (Exception e) {
            Log.e(TAG, " error s "+e.toString());
        }

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

    private void getHashkey() {
        String hashSequence = "merchantkey|txnid|amount|prodname|firstname|email|udf1|udf2|udf3|udf4|udf5||||||salt";
       String hash=hashCal("SHA-512",hashSequence);
       Log.e("Hash_checksum",hash);

        paymentParam.setMerchantHash(hash);
        // Invoke the following function to open the checkout page.
        // PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, StartPaymentActivity.this,-1, true);
        PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, PaymentMethod_Activity.this, R.style.AppTheme_default,false);

    }

    @SuppressLint("CheckResult")
    private void getShippingMethod() {
        final ProgressDialog progressDialog = new ProgressDialog(PaymentMethod_Activity.this, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        amount=TotalPrice.substring(1);

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        apiInterface.GetShippingMethod(amount,CountryId,ZoneId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ShippingMethod>() {
                    @Override
                    public void onNext(ShippingMethod response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_adderss", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {
                                friendsAdapter = new ShippingMethodAdapter(response.getShippingMethods(),PaymentMethod_Activity.this);
                                binding.setShipinglistAdapter(friendsAdapter);//set databinding adapter
                                friendsAdapter.notifyDataSetChanged();

                            } else {
                                //Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
                                utilities.dialogOK(PaymentMethod_Activity.this, getString(R.string.validation_title),
                                        response.getMsg(), getString(R.string.ok), true);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onMethodCallback(String subTitle, String subCode) {
        SubTitle=subTitle;
        SubCode=subCode;

        Log.e("shipping_method",SubCode+" "+SubTitle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
// PayUMoneySdk: Success -- payuResponse{"id":225642,"mode":"CC","status":"success","unmappedstatus":"captured","key":"9yrcMzso","txnid":"223013","transaction_fee":"20.00","amount":"20.00","cardCategory":"domestic","discount":"0.00","addedon":"2018-12-31 09:09:43","productinfo":"a2z shop","firstname":"kamal","email":"kamal.bunkar07@gmail.com","phone":"9144040888","hash":"b22172fcc0ab6dbc0a52925ebbd0297cca6793328a8dd1e61ef510b9545d9c851600fdbdc985960f803412c49e4faa56968b3e70c67fe62eaed7cecacdfdb5b3","field1":"562178","field2":"823386","field3":"2061","field4":"MC","field5":"167227964249","field6":"00","field7":"0","field8":"3DS","field9":" Verification of Secure Hash Failed: E700 -- Approved -- Transaction Successful -- Unable to be determined--E000","payment_source":"payu","PG_TYPE":"AXISPG","bank_ref_no":"562178","ibibo_code":"VISA","error_code":"E000","Error_Message":"No Error","name_on_card":"payu","card_no":"401200XXXXXX1112","is_seamless":1,"surl":"https://www.payumoney.com/sandbox/payment/postBackParam.do","furl":"https://www.payumoney.com/sandbox/payment/postBackParam.do"}
        // Result Code is -1 send from Payumoney activity
        Log.e("StartPaymentActivity", "request code " + requestCode + " resultcode " + resultCode);
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
                Log.e(TAG, "tran "+payuResponse+"---"+ merchantResponse);
            } /* else if (resultModel != null && resultModel.getError() != null) {
                Log.d(TAG, "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d(TAG, "Both objects are null!");
            }*/
        }
    }

}
