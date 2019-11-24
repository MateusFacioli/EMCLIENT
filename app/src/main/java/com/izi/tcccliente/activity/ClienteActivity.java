package com.izi.tcccliente.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.izi.tcccliente.R;
import com.izi.tcccliente.adapter.AdapterEmpresa;
import com.izi.tcccliente.config.ConfiguracaoFirebase;
import com.izi.tcccliente.listener.RecyclerItemClickListener;
import com.izi.tcccliente.model.ComercianteRecicleView;

import java.util.ArrayList;
import java.util.List;

public class ClienteActivity extends AppCompatActivity {

    private GoogleMap mMap;
    private RecyclerView recycleRestaurante;
    private AlertDialog alerta;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private FloatingActionMenu btn_menu;
    private LatLng localCliente;

    private AdapterEmpresa adapterRestaurante;
    private List<ComercianteRecicleView> restaurantes = new ArrayList<>();

    private static final int ANIMATION_DURATION = 300;
    private static final float ROTATION_ANGLE = 90f;
    private AnimatorSet mOpenAnimatorSet;
    private AnimatorSet mCloseAnimatorSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);
        inicializarComponentes();
        configuraRecicleView();
        //recuperarRestaurantesFirebase();
        verificarDistancia();

        /**
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
         **/

        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //NavigationView ic_menu;
                // abrir o ic_menu aqui e em todas as telas

                //DawerLayout drawer = findViewById(R.id.drawer_layout);
                //drawer.

            }
        });

        recycleRestaurante.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recycleRestaurante,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent loja = new Intent(ClienteActivity.this, LojaActivity.class);
                                ComercianteRecicleView comercianteSelecionado = restaurantes.get(position);

                                String uidComerciante = comercianteSelecionado.getUid();
                                loja.putExtra("idComerciante", uidComerciante);// dado do comerciante
                                startActivity(loja);



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




    private void verificarDistancia( ){

        localCliente = new LatLng(
                Double.parseDouble("-22.831938"),
                Double.parseDouble("-47.050647")
        );

        //Inicializar GeoFire
        DatabaseReference localUsuario = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("local_comerciante");
        GeoFire geoFire = new GeoFire(localUsuario);


        final GeoQuery geoQuery = geoFire.queryAtLocation(
                new GeoLocation(localCliente.latitude, localCliente.longitude),
                1//em km (0.05 50 metros)
        );

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {



                recuperarRestaurantesFirebase(key);
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });


    }

    private void recuperarRestaurantesFirebase(String id){

         DatabaseReference retaurantesRef = mDatabase.child("comerciante");
        Query restaurantesQ = retaurantesRef.orderByChild("uid").equalTo(id);
          restaurantesQ.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  restaurantes.clear();
                  for (DataSnapshot ds: dataSnapshot.getChildren()){
                      if(/*ds.getValue(ComercianteRecicleView.class).getLocalizacao() != null && */
                              ds.getValue(ComercianteRecicleView.class).getNome() != null) {
                          restaurantes.add(ds.getValue(ComercianteRecicleView.class));
                      }
                  }

                  adapterRestaurante.notifyDataSetChanged();

              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    

    public void fechartudo (View view)
    {
        Msg_alertas("Deseja sair do sistema ?",4);
    }

    public void vaiscan(View view)
    {
        Msg_alertas("Desesja ver seus comprovantes ?",6);
    }

    public void vaipedidos(View view)
    {
        Msg_alertas("Desesja ver seus pedidos atuais ?",3);
    }

    public void vaicarrinho(View view)
    {
        Msg_alertas("Deseja verificar o carrinho?",5);
    }

    private void Msg_alertas(String texto, int n) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("AVISO");
        builder.setMessage(texto);

        switch (n)
        {

            case 3://pedidos
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent pedidos = new Intent(ClienteActivity.this, AcompanharPedidoActivity.class);
                        startActivity(pedidos);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                break;
            case 4://sair
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.getInstance().signOut();
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                break;
            case 5://carrinho
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent carrinho = new Intent(ClienteActivity.this, CarrinhoActivity.class);
                        startActivity(carrinho);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                break;
            case 6://scan
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent qrcode = new Intent(ClienteActivity.this, Qr_codeGeneratorActivity.class);
                        startActivity(qrcode);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                break;
        }


        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }

    private void animation()
    {
        mOpenAnimatorSet = new AnimatorSet();
        mCloseAnimatorSet = new AnimatorSet();

        ObjectAnimator collapseAnimator =  ObjectAnimator.ofFloat(btn_menu.getMenuIconView(),
                "rotation",
                - 270f  +  ROTATION_ANGLE , 0f );
        ObjectAnimator expandAnimator = ObjectAnimator.ofFloat(btn_menu.getMenuIconView(),
                "rotation",
                0f , - 270f  +  ROTATION_ANGLE );
        //menu fica 45 graus nao consegui arrumar

        final Drawable plusDrawable = ContextCompat.getDrawable(this,
                R.drawable.ic_menu);
        expandAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                btn_menu.getMenuIconView().setImageDrawable(plusDrawable);
                btn_menu.setIconToggleAnimatorSet(mCloseAnimatorSet);
            }
        });

        final Drawable mapDrawable = ContextCompat.getDrawable(this,
                R.drawable.ic_menu);
        collapseAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                btn_menu.getMenuIconView().setImageDrawable(mapDrawable);
                btn_menu.setIconToggleAnimatorSet(mOpenAnimatorSet);
            }
        });

        mOpenAnimatorSet.play(expandAnimator);
        mCloseAnimatorSet.play(collapseAnimator);

        mOpenAnimatorSet.setDuration(ANIMATION_DURATION);
        mCloseAnimatorSet.setDuration(ANIMATION_DURATION);

        btn_menu.setIconToggleAnimatorSet(mOpenAnimatorSet);
    }

    private void configuraRecicleView(){
        recycleRestaurante.setLayoutManager(new LinearLayoutManager(this));
        recycleRestaurante.setHasFixedSize(true);
        adapterRestaurante = new AdapterEmpresa(restaurantes);
        recycleRestaurante.setAdapter(adapterRestaurante);
    }


    private void inicializarComponentes(){
        recycleRestaurante = findViewById(R.id.recicleRestaurante);
        mDatabase = ConfiguracaoFirebase.getFirebase();
        mAuth = ConfiguracaoFirebase.getFirebaseInstance();
        drawer = findViewById(R.id.drawer_layout);
        //navigationView = findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);
        btn_menu=findViewById(R.id.menu_principal);
        final Drawable originalImage = btn_menu.getMenuIconView().getDrawable();
        btn_menu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btn_menu.isOpened()) {
                    // We will change the icon when the menu opens, here we want to change to the previous icon
                    animation();
                    btn_menu.close(true);
                    btn_menu.getMenuIconView().setImageDrawable(originalImage);
                    //btn_menu.setIconAnimated(false);
                  //  btn_menu.setAnimation();
                } else {
                    // Since it is closed, let's set our new icon and then open the menu
                   // btn_menu.setIconAnimated(true);
                    btn_menu.getMenuIconView();
                    btn_menu.getMenuIconView().setImageDrawable(getResources().getDrawable(R.drawable.ic_menu));
                    btn_menu.open(true);
                }
            }
        });



    }

}
