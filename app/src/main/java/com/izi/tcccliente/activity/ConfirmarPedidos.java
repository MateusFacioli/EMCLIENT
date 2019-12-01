package com.izi.tcccliente.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.izi.tcccliente.R;
import com.izi.tcccliente.config.ConfiguracaoFirebase;
import com.izi.tcccliente.model.Carrinho;
import com.izi.tcccliente.model.Localizacao;

import java.util.ArrayList;
import java.util.List;

public class ConfirmarPedidos extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, RoutingListener {


    private Toolbar toolbar;
    private ImageView btnConfimarPedido;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Toolbar supportActionBar;
    private ProgressDialog progressDialog;
    private Button  btnRecebido;

    private ArrayList<? extends Carrinho> carrinhos = new ArrayList<>();
    private Localizacao local = new Localizacao();

    private DatabaseReference mDatabase;

    private String idLoja;

    private Bundle bConfirma;
    private Intent iConfirma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_pedidos);
        inicializarComponentes();
        polylines = new ArrayList<>();

       // iConfirma = getIntent();
     //   bConfirma = iConfirma.getExtras();

      //  if(bConfirma != null){
      //      idLoja = bConfirma.get("idLoja").toString();
//
      //  }

        mDatabase = ConfiguracaoFirebase.getFirebase();
        //recuperarEmpresas();

        // toolbar.setTitle("Confirmar Pedido");
        // setSupportActionBar(toolbar);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                    .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                    .addApi(LocationServices.API)
                    .build();
        }
        btnRecebido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent confirmar = new Intent(ConfirmarPedidos.this, Qr_codeGeneratorActivity.class);
                startActivity(confirmar);
                finish();
            }
        });



    }

    private void recuperarLocalizacao(){

        DatabaseReference lojaRef = mDatabase
                .child("empresa")
                .child(idLoja) //idStore
                .child("localizacao");

        lojaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                      local =  ds.getValue(Localizacao.class); // try to pass values to my Localizacao.java
                    }

              //  Double latitude = local.getLatitude();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void inicializarComponentes(){
        btnRecebido = findViewById(R.id.btnRecebido);
        iConfirma = getIntent();
        //carrinhos = iConfirma.getParcelableArrayListExtra("carrinhos");

    }
    private void recuperarEmpresas(){

        DatabaseReference lojaRef = mDatabase
                .child("empresa")
                .child(idLoja);

        lojaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for(DataSnapshot ds: dataSnapshot.getChildren()){
                  // Lojas lojas = ds.getValue(Lojas.class);
                  //  Toast.makeText(ConfirmarPedidos.this, lojas.getNomeEmpresa(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }


    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }


    @Override
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            if (mMap != null) {
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                final LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

                Marker marker1 =  mMap.addMarker(new MarkerOptions().position(latLng).title("Minha Posição"));
                final LatLng latLng2 = new LatLng(-3.131251, -60.021469);
                Marker marker2 =  mMap.addMarker(new MarkerOptions().position(latLng2).title("Minha Posição"));
                builder.include(marker1.getPosition());
                builder.include(marker2.getPosition());


                LatLngBounds bounds = builder.build();

                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels;
                int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

                mMap.animateCamera(cu);

                //recuperarLocalizacao();
                getRouteToMarker(latLng);


            }

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void setSupportActionBar(Toolbar supportActionBar) {
        this.supportActionBar = supportActionBar;
    }

    public Toolbar getSupportActionBar() {
        return supportActionBar;
    }


    private void getRouteToMarker(LatLng pickupLatLng) {

            if (pickupLatLng != null && mLastLocation != null) {
                Routing routing = new Routing.Builder()
                        .travelMode(AbstractRouting.TravelMode.DRIVING)
                        .withListener(this)
                        .alternativeRoutes(false)
                        .key("AIzaSyCVA5Z6ZpycRP-NPtyKjvBbUSKXJ6aiD70")
                        .waypoints(new LatLng(-3.131251, -60.021469), pickupLatLng)
                        .build();
                routing.execute();
            }
        }


    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};

    @Override
    public void onRoutingFailure(RouteException e) {
//        progressDialog.dismiss();
        if (e != null) {
           // Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
       // progressDialog.dismiss();
        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }
            polylines = new ArrayList<>();
            //add route(s) to the map.
            for (int i = 0; i < route.size(); i++) {

                //In case of more than 5 alternative routes
                int colorIndex = i % COLORS.length;

                PolylineOptions polyOptions = new PolylineOptions();
                polyOptions.color(getResources().getColor(COLORS[colorIndex]));
                polyOptions.width(10 + i * 3);
                polyOptions.addAll(route.get(i).getPoints());
                Polyline polyline = mMap.addPolyline(polyOptions);
                polylines.add(polyline);

               // Toast.makeText(getApplicationContext(), "Route " + (i + 1) + ": distance - " + route.get(i).getDistanceValue() + ": duration - " + route.get(i).getDurationValue(), Toast.LENGTH_SHORT).show();
            }


        }


    @Override
    public void onRoutingCancelled() {
    }

    private void erasePolylines(){
        for(Polyline line : polylines){
            line.remove();
        }
        polylines.clear();
    }
}
