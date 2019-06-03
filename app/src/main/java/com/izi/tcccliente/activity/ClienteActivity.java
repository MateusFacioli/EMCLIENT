package com.izi.tcccliente.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.izi.tcccliente.R;
import com.izi.tcccliente.adapter.AdapterEmpresa;
import com.izi.tcccliente.config.ConfiguracaoFirebase;
import com.izi.tcccliente.listener.RecyclerItemClickListener;
import com.izi.tcccliente.model.ComercianteRecicleView;

import java.util.ArrayList;
import java.util.List;

public class ClienteActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recycleRestaurante;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private AdapterEmpresa adapterRestaurante;
    private List<ComercianteRecicleView> restaurantes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);
        inicializarComponentes();
        configuraRecicleView();
        recuperarRestaurantesFirebase();

        /**
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
         **/

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
                                loja.putExtra("idComerciante", uidComerciante);
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



    private void recuperarRestaurantesFirebase(){



         DatabaseReference retaurantesRef = mDatabase.child("comerciante");
          retaurantesRef.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  restaurantes.clear();
                  for (DataSnapshot ds: dataSnapshot.getChildren()){
                      restaurantes.add( ds.getValue(ComercianteRecicleView.class) );
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cliente, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_carrinho) {
            Intent carrinho = new Intent(ClienteActivity.this, CarrinhoActivity.class);
            startActivity(carrinho);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_sair) {

            mAuth.getInstance().signOut();
            finish();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

}
