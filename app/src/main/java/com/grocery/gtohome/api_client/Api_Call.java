package com.grocery.gtohome.api_client;

import com.grocery.gtohome.model.WalletUseModel;
import com.grocery.gtohome.model.blog_model.BlogModel;
import com.grocery.gtohome.model.company_info_model.Company_infoModel;
import com.grocery.gtohome.model.confirm_order_model.Confirm_Order_Model;
import com.grocery.gtohome.model.country_model.CountryModel;
import com.grocery.gtohome.model.state_model.StateModel;
import com.grocery.gtohome.model.FilterByModel;
import com.grocery.gtohome.model.SimpleResultModel;
import com.grocery.gtohome.model.address_model.AddressModel;
import com.grocery.gtohome.model.address_model.SaveAddressModel;
import com.grocery.gtohome.model.cart_model.CartModel;
import com.grocery.gtohome.model.category_model.CategoryModel;
import com.grocery.gtohome.model.category_product_model.CategoryProductModel;
import com.grocery.gtohome.model.create_order.CreateOrderModel;
import com.grocery.gtohome.model.home_slider.HomeSliderBanner;
import com.grocery.gtohome.model.login_model.LoginModel;
import com.grocery.gtohome.model.order_history.OrderHistory;
import com.grocery.gtohome.model.order_history.OrderHistoryDetails;
import com.grocery.gtohome.model.popular_brand.PopularBrandModel;
import com.grocery.gtohome.model.product_details.Product_Details_Model;
import com.grocery.gtohome.model.product_slider.Product_Slider_Model;
import com.grocery.gtohome.model.return_model.ReturnDetailsModel;
import com.grocery.gtohome.model.return_model.ReturnModel;
import com.grocery.gtohome.model.return_reason_model.ReturnReasonModel;
import com.grocery.gtohome.model.review_model.ReviewModel;
import com.grocery.gtohome.model.shipping_method.ShippingMethod;
import com.grocery.gtohome.model.slot_model.Slot_Model;
import com.grocery.gtohome.model.wallet_model.WalletModelList;
import com.grocery.gtohome.model.wishlist_model.Wishlist_Model;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static com.grocery.gtohome.api_client.Base_Url.Register;
import static com.grocery.gtohome.api_client.Base_Url.loginapi;

/**
 * Created by Raghvendra Sahu on 20-Apr-20.
 */
public interface Api_Call {

    @GET(Register)
    Observable<LoginModel> EmailRegistrationUser(
           // @Query("route") String path,
            @Query("firstname") String first_name,
            @Query("lastname") String last_name,
            @Query("password") String et_pw,
            @Query("confirm") String etConf_pw,
            @Query("telephone") String et_mobile,
            @Query("email") String et_email,
            @Query("agree") String agree,
            @Query("newsletter") String newsletter,
            @Query("customer_group_id") String customer_group_id);


    @GET(loginapi)
    Observable<LoginModel> LoginUser(
           // @Query("route") String loginapi,
            @Query("email") String et_email,
            @Query("password") String et_pw);


    @GET("index.php?")
    Observable<SimpleResultModel> ForgotUser(
            @Query("route") String forgottenapi,
            @Query("email") String et_email);

    @GET("index.php?route=restapi/category")
    Observable<CategoryModel> CategoryApi(
           );


    @POST("index.php?route=restapi/product/category_product")
    @FormUrlEncoded
    Observable<CategoryProductModel> CategoryProductApi(@FieldMap HashMap<String, String> map);


    @FormUrlEncoded
    @POST("index.php?route=restapi/product/product_detail")
    Observable<Product_Details_Model> ProductDetails(
            @Field ("product_id")String userid);


    @GET("index.php?route=restapi/filter/sortby")
    Observable<FilterByModel> FilterByApi();

    @FormUrlEncoded
    @POST("index.php?route=restapi/cart/add")
    Observable<SimpleResultModel>  AddCart(@FieldMap HashMap<String, String> map);


    @FormUrlEncoded
    @POST("index.php?route=restapi/cart/view")
    Observable<CartModel>  CartListApi(@FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST("index.php?route=restapi/cart/remove")
    Observable<SimpleResultModel>  RemoveCartItemApi(@FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST("index.php?route=restapi/cart/edit")
    Observable<SimpleResultModel> UpdateCartItemApi(@FieldMap  HashMap<String, String> map);

    @GET("index.php?route=restapi/product/featured_products")
    Observable<CategoryProductModel> FeatureProductApi();


    @GET("index.php?route=restapi/customer")
    Observable<AddressModel> GetExistingAddress( @Query("customer_id") String customer_id);


    @GET("index.php?route=restapi/filter/countries")
    Observable<CountryModel> GetCountry();

    @GET("index.php?route=restapi/filter/states")
    Observable<StateModel>  GetState(@Query("country_id")String country_id);


    @GET("index.php?route=restapi/customer/save_payment_address")
    Observable<SaveAddressModel> SaveDeliveryAddress(
            @Query("customer_id") String customer_id,
            @Query("firstname")  String first_name,
            @Query("lastname") String last_name,
            @Query("company") String company_name,
            @Query("address_1") String address1,
            @Query("address_2") String address2,
            @Query("city") String city,
            @Query("postcode") String postcode,
            @Query("country_id") String country_id,
            @Query("zone_id") String zone_id);


    @FormUrlEncoded
    @POST("index.php?route=restapi/shipping/methods")
    Observable<ShippingMethod>  GetShippingMethod(
            @Field ("total") String totalPrice,
            @Field ("country_id")  String countryId,
            @Field ("zone_id") String zoneId);


    @FormUrlEncoded
    @POST("index.php?route=restapi/orders/add")
    Observable<CreateOrderModel>  CreateOrder(@FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST("index.php?route=restapi/orders")
    Observable<OrderHistory>  OrderHistoryApi(@FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST("index.php?route=restapi/orders/info")
    Observable<OrderHistoryDetails> OrderHistoryItemDetails(@FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST("index.php?route=restapi/customer/edit_account")
    Observable<LoginModel> UpdateUser(
            @Field ("firstname")  String first_name,
            @Field ("lastname")String lastname,
            @Field ("telephone") String mobile,
            @Field ("email") String email,
            @Field ("customer_id")  String customer_id);

    @GET("index.php?route=restapi/information")
    Observable<Company_infoModel> InfoCompany();

    @FormUrlEncoded
    @POST("index.php?route=restapi/cart/clear")
    Observable<SimpleResultModel>  CartRemoveApi(@FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST("index.php?route=restapi/customer/address")
    Observable<AddressModel> GetAddressApi(@FieldMap HashMap<String, String> map);


    @FormUrlEncoded
    @POST("index.php?route=restapi/customer/delete_address")
    Observable<SimpleResultModel> RemoveAddressApi(@FieldMap HashMap<String, String> map);


    @FormUrlEncoded
    @POST("index.php?route=restapi/customer/add_edit_address")
    Observable<SimpleResultModel>  SaveNewAddress(
            @Field("address_id")  String address_id,
            @Field("customer_id") String customer_id,
            @Field("firstname") String first_name,
            @Field("lastname") String last_name,
            @Field("company") String company_name,
            @Field("address_1") String address1,
            @Field("address_2") String address2,
            @Field("city") String city,
            @Field("postcode") String post_code,
            @Field("country_id") String country_id,
            @Field("zone_id") String zone_id,
            @Field("default") String default_address);


    @FormUrlEncoded
    @POST("index.php?route=restapi/wishlist/add_remove")
    Observable<SimpleResultModel> AddWishlist(@FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST("index.php?route=restapi/wishlist")
    Observable<Wishlist_Model> GetWishlist(@FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST("index.php?route=restapi/cart/count")
    Observable<SimpleResultModel>  CartCount(@FieldMap HashMap<String, String> map);

    @GET("index.php?route=restapi/deliverydate")
    Observable<Slot_Model> GetSlot(@Query("deliverydate") String date);

    @FormUrlEncoded
    @POST("index.php?route=restapi/coupon")
    Observable<SimpleResultModel>  ApplyCoopanApi(@FieldMap  HashMap<String, String> map);

    @FormUrlEncoded
    @POST("index.php?route=restapi/voucher")
    Observable<SimpleResultModel> ApplyGiftApi(@FieldMap  HashMap<String, String> map);

    @FormUrlEncoded
    @POST("index.php?route=restapi/customer/change_password")
    Observable<SimpleResultModel>  ChanePwApi(@FieldMap  HashMap<String, String> map);

    @FormUrlEncoded
    @POST("index.php?route=restapi/orders/confirm")
    Observable<Confirm_Order_Model>  ConfirmOrder(@FieldMap HashMap<String, String> map);

    @GET("index.php?route=restapi/information/blogs")
    Observable<BlogModel> BlogApi();

    @GET("index.php?route=restapi/information/blog_detail")
    Observable<BlogModel>  BlogDetailsApi(@Query("blogger_id") String blog_id);

    @FormUrlEncoded
    @POST("index.php?route=restapi/information/blog_add_comment")
    Observable<BlogModel>  AddCommentApi(
            @Field("author")  String et_author,
            @Field("email") String et_email,
            @Field("comment") String et_comment,
            @Field("blogger_id") String blog_id,
            @Field("auto_approve") String autoApprove);


    @GET("index.php?route=restapi/home/slideshow")
    Observable<HomeSliderBanner> HomeHorizontalSlideApi();


    @GET("index.php?route=restapi/home/add_newsletter")
    Observable<SimpleResultModel>  AddNews( @Query("email") String news_address);

    @GET("index.php?route=restapi/customer/newsletter_subscription")
    Observable<SimpleResultModel> UpdateNewsletter(@Query("newsletter") String newsValue,
                                                   @Query("customer_id")String customerId);

    @GET("index.php?route=restapi/home/our_brands")
    Observable<PopularBrandModel> PopularBrandApi();

    @GET("index.php?route=restapi/product/special_products")
    Observable<CategoryProductModel>  SpecialProductApi();

    @GET("index.php?route=restapi/product/search")
    Observable<CategoryProductModel>  SearchApi(
            @Query("customer_id") String customer_Id,
            @Query("category_id")  String category_id,
            @Query("sub_category")  String sub_category,
            @Query("description") String description,
            @Query("search")  String et_search);

    @GET("index.php?route=restapi/return/reasons")
    Observable<ReturnReasonModel>  ReturnReasonApi();

    @FormUrlEncoded
    @POST("index.php?route=restapi/return/add")
    Observable<SimpleResultModel>  ReturnRequestApi(@FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST("index.php?route=restapi/return")
    Observable<ReturnModel>  OrderReturnApi(@FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST("index.php?route=restapi/return/info")
    Observable<ReturnDetailsModel> ReturnItemDetails(@FieldMap  HashMap<String, String> map);

    @FormUrlEncoded
    @POST("index.php?route=restapi/orders/success")
    Observable<SimpleResultModel>  UpdateOrderStatus(
            @Field("customer_id") String customer_id,
            @Field("order_status_id") String order_status_id,
            @Field("order_id") String order_id,
            @Field("message") String razorpayPaymentID,
            @Field("wallet_deduction")  String walletDeductAmount,
            @Field("wk_wallet_payment")  String wk_wallet_payment);


    @GET("index.php?route=restapi/auth/social_login")
    Observable<LoginModel>  LoginSocialUser(
            @Query("firstname") String firstname,
            @Query("lastname") String lastname,
            @Query("email") String social_email,
            @Query("oauthid") String social_id,
            @Query("oauth_provider")  String gplus);


    @GET("index.php?route=restapi/information/send_contact")
    Observable<SimpleResultModel>  SendEnquiry(
            @Query("name")  String name,
            @Query("email")  String email,
            @Query("enquiry") String et_enquiry);

    @FormUrlEncoded
    @POST("index.php?route=restapi/wallet/add_money")
    Observable<SimpleResultModel>  UpdateWalletStatus(
            @Field("customer_id")  String customerId,
            @Field("amount")  String amount,
            @Field("description")  String razorpayPaymentID);

    @FormUrlEncoded
    @POST("index.php?route=restapi/wallet")
    Observable<WalletModelList>  GetWalletApi(@Field("customer_id") String customerId);

    @FormUrlEncoded
    @POST("index.php?route=restapi/payment/use_wallet")
    Observable<WalletUseModel>  UseWalletApi(
            @Field("customer_id")  String customer_id,
            @Field("isWallet")  String isWallet,
            @Field("wk_cart_amount") String subTotal,
            @Field("shipping_method[title]")  String subTitle,
            @Field("shipping_method[code]")  String subCode,
            @Field("subtotal")  String subTotal1,
            @Field("address_id")  String address_id);


    @GET("index.php?route=restapi/product/review_list")
    Observable<ReviewModel>  ReviewList(
            @Query("product_id")  String product_id);

    @FormUrlEncoded
    @POST("index.php?route=restapi/product/add_review")
    Observable<SimpleResultModel> RatingPost(
            @Field("name") String name,
            @Field("text") String review,
            @Field("rating") String rating_point,
            @Field("product_id") String product_id,
            @Field("customer_id") String customer_id);

    @FormUrlEncoded
    @POST("index.php?route=kbzipcodeavailability/kbzipcodeavailability")
    Observable<SimpleResultModel> CheckAvailProduct(
            @Field("zipcode") String pincode,
            @Field("product_id") String product_Id);

    @GET("index.php?route=restapi/home/banners")
    Observable<Product_Slider_Model> ProductSlideApi();
}
