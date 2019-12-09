package com.izi.tcccliente.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toolbar;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.izi.tcccliente.R;
import com.izi.tcccliente.adapter.AdapterLoja;
import com.izi.tcccliente.config.ConfiguracaoFirebase;
import com.izi.tcccliente.helper.UsuarioFirebase;
import com.izi.tcccliente.listener.RecyclerItemClickListener;
import com.izi.tcccliente.model.Carrinho;
import com.izi.tcccliente.model.Cliente;
import com.izi.tcccliente.model.ComercianteRecicleView;
import com.izi.tcccliente.model.Localizacao;
import com.izi.tcccliente.model.LojaRecicleView;

import java.util.ArrayList;
import java.util.List;

public class LojaActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private RecyclerView recycleLoja;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LatLng latLng;


    private DatabaseReference mDatabase;

    private AdapterLoja adapterLoja;
    private List<LojaRecicleView> loja = new ArrayList<>();

    private Intent iLoja;
    private Toolbar toolbar;

    private List<LojaRecicleView> lojaCarrinho = new ArrayList<>();
    private ComercianteRecicleView comeciante = new ComercianteRecicleView();
    private List<Cliente> usuario = new ArrayList<>();
    private Carrinho carrinho = new Carrinho();
    private Localizacao localizacao = new Localizacao();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loja);
        inicializarComponentes();
        configuraComponentes();
        recuperarProdutos();
        recuperarUsuario();
       // getSupportActionBar().hide();

        //String urlTopass = makeURL(latLngOld.latitude, latLngOld.longitude, latLng.latitude, latLng.longitude); // lat origem, lon origem, lat destino, lon destino

       // new connectAsyncTask(urlTopass).execute();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        recycleLoja.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recycleLoja,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent lojaIntent = new Intent(LojaActivity.this, CarrinhoActivity.class);
                                LojaRecicleView produtoSelecionado = loja.get(position);

                                String uidProduto = produtoSelecionado.getIdProduto();
                                lojaIntent.putExtra("uidProduto", uidProduto);
                                lojaIntent.putExtra("idComerciante", comeciante.getUid());
                                carrinho.setProduto(loja.get(position));
                                carrinho.setStatus("andamento");
                                carrinho.salvar();
                                startActivity(lojaIntent);

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            if(mMap != null){
                mMap.clear();
                List<Marker> markersList = new ArrayList<Marker>();
                latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                final LatLng latLngComerciante = new LatLng(
                        carrinho.getComerciante().getLocalizacao().getLatitude(),
                        carrinho.getComerciante().getLocalizacao().getLongitude());

               Marker minhaPosicao = mMap.addMarker(
                       new MarkerOptions()
                               .position(latLng)
                               .title("Minha Posição"));

               Marker comercianteLocalizacao = mMap.addMarker(
                       new MarkerOptions()
                               .position(latLngComerciante)
                               .title("Localização Comerciante"));
               markersList.add(minhaPosicao);
               markersList.add(comercianteLocalizacao);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for(Marker marker: markersList){
                    builder.include(marker.getPosition());
                }

                int padding = 80;
                /**create the bounds from latlngBuilder to set into map camera*/
                LatLngBounds bounds = builder.build();
                /**create the camera with bounds and padding to set into map*/
               final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                /**call the map call back to know map is loaded or not*/
                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        /**set animated zoom camera into map*/
                        mMap.animateCamera(cu);
                        localizacao.setLatitude(latLngComerciante.latitude);
                        localizacao.setLongitude(latLngComerciante.longitude);
                        localizacao.salvar();

                    }
                    // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));

                });
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }





    private void recuperarUsuario(){
        Query usuarioRef = mDatabase
                .child("cliente")
                .orderByChild("uid").equalTo(UsuarioFirebase.getDadosUsuarioLogado().getUid());
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuario.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    usuario.add(ds.getValue(Cliente.class));
                }
                carrinho.setCliente(usuario.get(0));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void recuperarProdutos(){
        DatabaseReference produtosRef = mDatabase
                .child("cardapio")
                .child(comeciante.getUid());

        produtosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loja.clear();

                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    loja.add(ds.getValue(LojaRecicleView.class));
                }
                adapterLoja.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void configuraComponentes(){
        recycleLoja.setLayoutManager(new LinearLayoutManager(this));
        recycleLoja.setHasFixedSize(true);
        adapterLoja = new AdapterLoja(loja);
        recycleLoja.setAdapter(adapterLoja);
        comeciante = (ComercianteRecicleView) iLoja.getSerializableExtra("comerciante");
        carrinho.setComerciante(comeciante);




    }
    private void inicializarComponentes(){
        recycleLoja = findViewById(R.id.recyclerLoja);
        mDatabase = ConfiguracaoFirebase.getFirebase();
        //toolbar = findViewById(R.id.tb_dado_com);

        // o que é isso?
        iLoja = getIntent();

    }
}
