package com.example.project.network

import com.example.project.model.Account
import com.example.project.model.Vendor
import com.example.project.model.Customer
import com.example.project.model.RegisterRequest
import com.example.project.model.VendorUpdateRequest
import com.example.project.model.VendorPhonesUpdateRequest
import com.example.project.model.CustomerUpdateRequest
import com.example.project.model.CustomerPhonesUpdateRequest
import com.example.project.model.VendorOrders
import com.example.project.model.VendorProducts
import com.example.project.model.Orderdetails
import com.example.project.model.CustomerProducts
import com.example.project.model.AddProductRequest
import com.example.project.model.AddToCartRequest
import com.example.project.model.CartItem
import com.example.project.model.CustomerOrders
import com.example.project.model.ProductItemDetails
import com.example.project.model.UpdateProductRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("login.php")
    suspend fun login(@Body account: Account): Response<Account>

    @POST("register.php")
    suspend fun register(@Body request: RegisterRequest): Response<Account>

    @GET("get_vendor.php")
    suspend fun getVendor(@Query("vendor_id") vendorId: Int): Response<Vendor>

    @GET("get_customer.php")
    suspend fun getCustomer(@Query("customer_id") customerId: Int): Response<Customer>

    @POST("update_vendor.php")
    suspend fun updateVendor(@Body request: VendorUpdateRequest): Response<Vendor>

    @POST("update_vendor_phones.php")
    suspend fun updateVendorPhones(@Body request: VendorPhonesUpdateRequest): Response<Vendor>

    @POST("update_customer.php")
    suspend fun updateCustomer(@Body request: CustomerUpdateRequest): Response<Customer>

    @POST("update_customer_phones.php")
    suspend fun updateCustomerPhones(@Body request: CustomerPhonesUpdateRequest): Response<Customer>

    @GET("get_vendor_orders.php")
    suspend fun getVendorOrders(@Query("vendor_id") vendorId: Int): Response<VendorOrders>

    @GET("get_customer_orders.php")
    suspend fun getCustomerOrders(@Query("customer_id") customerId: Int): Response<CustomerOrders>

    // Flat JSON array for vendor products
    @GET("get_vendor_products.php")
    suspend fun getVendorProducts(@Query("vendor_id") vendorId: Int): Response<List<ProductItemDetails>>

    // Flat JSON array for customer products
    @GET("get_all_products.php")
    suspend fun getAllProducts(@Query("category") category: String): Response<List<ProductItemDetails>>

    // Single product
    @GET("get_product_details.php")
    suspend fun getProductDetails(@Query("product_id") productId: Int): Response<ProductItemDetails>

    @GET("get_order_details.php")
    suspend fun getOrderDetails(@Query("order_no") orderNo: String?, @Query("vendor_id") vendorId: Int): Response<Orderdetails>

    @GET("get_customer_order_details.php")
    suspend fun getCustomerOrderDetails(@Query("order_no") orderNo: String?): Response<Orderdetails>

    @GET("delete_vendor_orders.php")
    suspend fun deleteVendorOrders(@Query("order_no") orderNo: String?, @Query("vendor_id") vendorId: Int): Response<String>

    @GET("delete_customer_orders.php")
    suspend fun deleteCustomerOrders(@Query("order_no") orderNo: String?, @Query("customer_id") customerId: Int): Response<String>

    @POST("add_product.php")
    suspend fun addProduct(@Body request: AddProductRequest): Response<String>

    @POST("update_product.php")
    suspend fun updateProduct(@Body request: UpdateProductRequest): Response<String>

    @GET("delete_vendor_products.php")
    suspend fun deleteVendorProduct(@Query("product_id") productId: Int): Response<String>

    @POST("add_to_cart.php")
    suspend fun addToCart(@Body request: AddToCartRequest): Response<String>

    @GET("get_cart.php")
    suspend fun getCart(@Query("customer_id") customerId: Int): Response<List<CartItem>>

    @POST("complete_order.php")
    suspend fun completeOrder(@Body request: Map<String, Int>): Response<String>

    @GET("increment_views.php")
    suspend fun incrementViews(@Query("product_id") productId: Int): Response<String>
}
