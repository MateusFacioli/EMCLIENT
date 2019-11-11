/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.izi.tcccliente.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.location.Address;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.izi.tcccliente.R;
import com.izi.tcccliente.activity.AcompanharPedidoActivity;
import com.izi.tcccliente.activity.Qr_codeGeneratorActivity;
import com.izi.tcccliente.model.Localizacao;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class CheckoutActivity extends AppCompatActivity {


  private Localizacao localizacao = new Localizacao();
  private AutocompleteSupportFragment autocompleteFragment;
  private PaymentsClient mPaymentsClient;
  private View mGooglePayButton;
  private Toolbar toolbar;
  private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;
  private TextView mGooglePayStatusText;
  private ItemInfo mBikeItem = new ItemInfo("Dipirona", 2 * 1000000, R.drawable.lojathepletefinal);
  private long mShippingCost = 90 * 1000000;
  private PlacesClient placesClient;
  private final int AUTOCOMPLETE_REQUEST_CODE = 99;
  private Intent iConfirma;
  private Bundle bConfirma;

  private TextView txtEnderecoFinalizar;
  private TextView txtNomeLoja;
  private TextView txtNomePedido;

  private String nomePedido;
  private String nomeLoja;
  private String idLoja;
  private String endereco;

  @RequiresApi(api = Build.VERSION_CODES.N)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_checkout);
    inicializarComponenetes();
    configurarComponentes();
    toolbar = findViewById(R.id.tolbarcheck2);
    toolbar.setTitle("Confirmar Pedido");
    setSupportActionBar(toolbar);


    // Set up the mock information for our item in the UI.
    //initItemUI();

    mGooglePayButton = findViewById(R.id.googlepay);
    mGooglePayStatusText = findViewById(R.id.googlestatus);

    // Initialize a Google Pay API client for an environment suitable for testing.
    // It's recommended to create the PaymentsClient object inside of the onCreate method.
    mPaymentsClient = PaymentsUtil.createPaymentsClient(this);
    possiblyShowGooglePayButton();


    mGooglePayButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            requestPayment(view);
          }
        });
  }


  @RequiresApi(api = Build.VERSION_CODES.N)
  private void possiblyShowGooglePayButton() {
    final Optional<JSONObject> isReadyToPayJson = PaymentsUtil.getIsReadyToPayRequest();
    if (!isReadyToPayJson.isPresent()) {
      return;
    }
    IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(isReadyToPayJson.get().toString());
    if (request == null) {
      return;
    }

    // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
    // OnCompleteListener to be triggered when the result of the call is known.
    Task<Boolean> task = mPaymentsClient.isReadyToPay(request);
    task.addOnCompleteListener(this,
        new OnCompleteListener<Boolean>() {
          @Override
          public void onComplete(@NonNull Task<Boolean> task) {
            if (task.isSuccessful()) {
              setGooglePayAvailable(task.getResult());
            } else {
              Log.w("isReadyToPay failed", task.getException());
            }
          }
        });
  }

  private void setGooglePayAvailable(boolean available) {
    if (available) {
      mGooglePayStatusText.setVisibility(View.GONE);
      mGooglePayButton.setVisibility(View.VISIBLE);
    } else {
      mGooglePayStatusText.setText(R.string.googlepay_status_unavailable);
    }
  }


  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
        // value passed in AutoResolveHelper
      case LOAD_PAYMENT_DATA_REQUEST_CODE:
        switch (resultCode) {
          case Activity.RESULT_OK:
         //   PaymentData paymentData = PaymentData.getFromIntent(data);
          //  handlePaymentSuccess(paymentData);

            Intent intent = new Intent(CheckoutActivity.this, AcompanharPedidoActivity.class);
            startActivity(intent);

            break;
          case Activity.RESULT_CANCELED:
            // Nothing to here normally - the user simply cancelled without selecting a
            // payment method.
            break;
          case AutoResolveHelper.RESULT_ERROR:
            Status status = AutoResolveHelper.getStatusFromIntent(data);
            handleError(status.getStatusCode());
            break;
          default:
            // Do nothing.
        }

        // Re-enables the Google Pay payment button.
        mGooglePayButton.setClickable(true);
        break;
    }
  }


  private void salvaLocal(Address address){

    localizacao.setEstado(address.getAdminArea());
    localizacao.setCidade(address.getCountryName());
    localizacao.setCep(address.getPostalCode());
    localizacao.setBairro(address.getSubLocality());
    localizacao.setRua(address.getThoroughfare());
    localizacao.setNumero(address.getFeatureName());

  }




  private void handlePaymentSuccess(PaymentData paymentData) {
    String paymentInformation = paymentData.toJson();

    // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
    if (paymentInformation == null) {
      return;
    }
    JSONObject paymentMethodData;

    try {
      paymentMethodData = new JSONObject(paymentInformation).getJSONObject("paymentMethodData");
      // If the gateway is set to "example", no payment information is returned - instead, the
      // token will only consist of "examplePaymentMethodToken".
      if (paymentMethodData
              .getJSONObject("tokenizationData")
              .getString("type")
              .equals("PAYMENT_GATEWAY")
          && paymentMethodData
              .getJSONObject("tokenizationData")
              .getString("token")
              .equals("examplePaymentMethodToken")) {
        AlertDialog alertDialog =
            new AlertDialog.Builder(this)
                .setTitle("Warning")
                .setMessage(
                    "Gateway name set to \"example\" - please modify "
                        + "Constants.java and replace it with your own gateway.")
                .setPositiveButton("OK", null)
                .create();
        alertDialog.show();
      }

      String billingName =
          paymentMethodData.getJSONObject("info").getJSONObject("billingAddress").getString("name");
      Log.d("BillingName", billingName);
     // Toast.makeText(this, getString(R.string.payments_show_name, billingName), Toast.LENGTH_LONG)
         // .show();

      // Logging token string.
      Log.d("GooglePaymentToken", paymentMethodData.getJSONObject("tokenizationData").getString("token"));
    } catch (JSONException e) {
      Log.e("handlePaymentSuccess", "Error: " + e.toString());
      return;
    }

  }


  private void handleError(int statusCode) {
    Log.w("loadPaymentData failed", String.format("Error code: %d", statusCode));
  }

  // This method is called when the Pay with Google button is clicked.
  @RequiresApi(api = Build.VERSION_CODES.N)
  public void requestPayment(View view) {
    // Disables the button to prevent multiple clicks.
    mGooglePayButton.setClickable(false);

    // The price provided to the API should include taxes and shipping.
    // This price is not displayed to the user.
    String price = PaymentsUtil.microsToString(mBikeItem.getPriceMicros() + mShippingCost);

    // TransactionInfo transaction = PaymentsUtil.createTransaction(price);
    Optional<JSONObject> paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(price);
    if (!paymentDataRequestJson.isPresent()) {
      return;
    }
    PaymentDataRequest request =
        PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString());

    // Since loadPaymentData may show the UI asking the user to select a payment method, we use
    // AutoResolveHelper to wait for the user interacting with it. Once completed,
    // onActivityResult will be called with the result.
    if (request != null) {
      AutoResolveHelper.resolveTask(
          mPaymentsClient.loadPaymentData(request), this, LOAD_PAYMENT_DATA_REQUEST_CODE);
    }
  }
  //private void initItemUI() {
    //TextView itemName = findViewById(R.id.text_item_name);
    //ImageView itemImage = findViewById(R.id.image_item_image);
   // TextView itemPrice = findViewById(R.id.text_item_price);

    //itemName.setText(mBikeItem.getName());
   // itemImage.setImageResource(mBikeItem.getImageResourceId());
    //itemPrice.setText(PaymentsUtil.microsToString(mBikeItem.getPriceMicros()));
  //}

  private void inicializarComponenetes(){


    iConfirma = getIntent();
    bConfirma = iConfirma.getExtras();

    txtNomeLoja = findViewById(R.id.txtNomeLoja);
    txtNomePedido = findViewById(R.id.txtNomePedido);
    txtNomeLoja = findViewById(R.id.txtNomeLoja);
    txtEnderecoFinalizar = findViewById(R.id.txtEnderecoFinalizar);
  }
  public void configurarComponentes(){

    if(bConfirma != null){
      nomePedido = bConfirma.get("nomePedido").toString();
      nomeLoja = bConfirma.get("nomeLoja").toString();
      idLoja = bConfirma.get("idLoja").toString();
      //endereco = bConfirma.get("endereco").toString();
      txtEnderecoFinalizar.setText(endereco);
      txtNomeLoja.setText(nomeLoja);
      txtNomePedido.setText(nomePedido);

    }

    toolbar = findViewById(R.id.tolbarcheck2);
    toolbar.setTitle("Confirmar Pedido");
    setSupportActionBar(toolbar);


  }


}
