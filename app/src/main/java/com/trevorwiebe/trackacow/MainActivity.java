package com.trevorwiebe.trackacow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trevorwiebe.trackacow.adapters.PenRecyclerViewAdapter;
import com.trevorwiebe.trackacow.db.AppDatabase;
import com.trevorwiebe.trackacow.db.entities.CowEntity;
import com.trevorwiebe.trackacow.db.entities.DrugEntity;
import com.trevorwiebe.trackacow.db.entities.DrugsGivenEntity;
import com.trevorwiebe.trackacow.db.entities.PenEntity;
import com.trevorwiebe.trackacow.objects.CowObject;
import com.trevorwiebe.trackacow.objects.DrugObject;
import com.trevorwiebe.trackacow.objects.DrugsGivenObject;
import com.trevorwiebe.trackacow.objects.PenObject;
import com.trevorwiebe.trackacow.utils.ItemClickListener;
import com.trevorwiebe.trackacow.utils.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 132;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference mPenRef;
    private ValueEventListener mPenListener;
    private PenRecyclerViewAdapter mPenRecyclerViewAdapter;
    private ArrayList<PenObject> mPenList = new ArrayList<>();

    private ArrayList<CowEntity> mCowEntityUpdateList = new ArrayList<>();
    private ArrayList<DrugEntity> mDrugEntityUpdateList = new ArrayList<>();
    private ArrayList<DrugsGivenEntity> mDrugsGivenEntityUpdateList = new ArrayList<>();
    private ArrayList<PenEntity> mPenEntityUpdateList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final TextView noPensTv = findViewById(R.id.no_pens_tv);
        final ProgressBar loadingMain = findViewById(R.id.loading_main);
        RecyclerView mainRv = findViewById(R.id.main_rv);
        mainRv.setLayoutManager(new LinearLayoutManager(this));
        mPenRecyclerViewAdapter = new PenRecyclerViewAdapter(mPenList, false, this);
        mainRv.setAdapter(mPenRecyclerViewAdapter);

        mainRv.addOnItemTouchListener(new ItemClickListener(this, mainRv,
                new ItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent trackCowIntent = new Intent(MainActivity.this, MedicatedCowsActivity.class);
                trackCowIntent.putExtra("penObject", mPenList.get(position));
                startActivity(trackCowIntent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mFirebaseAuth = firebaseAuth;
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null){

                    onSignedOutCleanUp();

                    // Choose authentication providers
                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build());

                    // Create and launch sign-in intent
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .build(),
                            RC_SIGN_IN);
                }else{
                    onSignedInInitialized(user);
                }
            }
        };

        mPenListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPenList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    PenObject penObject = snapshot.getValue(PenObject.class);
                    if(penObject != null){
                        mPenList.add(penObject);
                    }
                }
                loadingMain.setVisibility(View.INVISIBLE);
                if(mPenList.size() == 0) {
                    noPensTv.setVisibility(View.VISIBLE);
                }else {
                    noPensTv.setVisibility(View.INVISIBLE);
                }
                mPenRecyclerViewAdapter.swapData(mPenList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onPause() {
        mFirebaseAuth.removeAuthStateListener(mAuthListener);
        if(mPenRef != null) {
            mPenRef.removeEventListener(mPenListener);
        }
        super.onPause();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_manage_drugs:
                Intent manageDrugsIntent = new Intent(MainActivity.this, ManageDrugsActivity.class);
                startActivity(manageDrugsIntent);
                break;
            case R.id.nav_manage_pens:
                Intent managePensIntent = new Intent(MainActivity.this, ManagePensActivity.class);
                startActivity(managePensIntent);
                break;
            case R.id.nav_sign_out:
                AuthUI.getInstance().signOut(this);
                break;
            default:
                Log.e(TAG, "onNavigationItemSelected: unknown menu id");
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void onSignedInInitialized(FirebaseUser user){

        mPenRef = FirebaseDatabase.getInstance().getReference("users").child(mFirebaseAuth.getCurrentUser().getUid()).child(PenObject.PEN_OBJECT);

        mPenRef.addValueEventListener(mPenListener);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.nav_userName);
        TextView userEmail = headerView.findViewById(R.id.nav_userEmail);

        userName.setText(user.getDisplayName());
        userEmail.setText(user.getEmail());

        if(Utility.haveNetworkConnection(this)) {
            DatabaseReference baseRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            baseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key = snapshot.getKey();
                        if (key != null) {
                            switch (key) {
                                case "cows":
                                    for (DataSnapshot cowSnapshot : snapshot.getChildren()) {
                                        CowObject cowObject = cowSnapshot.getValue(CowObject.class);
                                        if (cowObject != null) {
                                            CowEntity cowEntity = new CowEntity();
                                            cowEntity.setAlive(cowObject.isAlive());
                                            cowEntity.setCowId(cowObject.getCowId());
                                            cowEntity.setDate(cowObject.getDate());
                                            cowEntity.setNotes(cowObject.getNotes());
                                            cowEntity.setPenId(cowObject.getPenId());
                                            cowEntity.setTagNumber(cowObject.getCowNumber());
                                            mCowEntityUpdateList.add(cowEntity);

                                            DrugsGivenEntity drugsGivenEntity = new DrugsGivenEntity();
                                            ArrayList<DrugsGivenObject> drugsGivenObjects = cowObject.getmDrugList();
                                            for (int q = 0; q < drugsGivenObjects.size(); q++) {
                                                DrugsGivenObject drugsGivenObject = drugsGivenObjects.get(q);

                                                drugsGivenEntity.setAmountGiven(drugsGivenObject.getAmountGiven());
                                                drugsGivenEntity.setCowId(cowEntity.getCowId());
                                                drugsGivenEntity.setDate(drugsGivenObject.getDate());
                                                mDrugsGivenEntityUpdateList.add(drugsGivenEntity);
                                            }
                                        }
                                    }
                                    break;
                                case "drugs":
                                    for (DataSnapshot drugsSnapshot : snapshot.getChildren()) {
                                        DrugObject drugObject = drugsSnapshot.getValue(DrugObject.class);
                                        if (drugObject != null) {
                                            DrugEntity drugEntity = new DrugEntity();
                                            drugEntity.setDrugName(drugObject.getDrugName());
                                            drugEntity.setDrugId(drugObject.getDrugId());
                                            drugEntity.setDefaultAmount(drugObject.getDefaultAmount());
                                            mDrugEntityUpdateList.add(drugEntity);
                                        }
                                    }
                                    break;
                                case "pens":
                                    for (DataSnapshot penSnapshot : snapshot.getChildren()) {
                                        PenObject penObject = penSnapshot.getValue(PenObject.class);
                                        if (penObject != null) {
                                            PenEntity penEntity = new PenEntity();
                                            penEntity.setCustomerName(penObject.getCustomerName());
                                            penEntity.setNotes(penObject.getNotes());
                                            penEntity.setPenDatabaseId(penObject.getPenId());
                                            penEntity.setPenName(penObject.getPenName());
                                            penEntity.setTotalHead(penObject.getTotalHead());

                                            mPenEntityUpdateList.add(penEntity);
                                        }
                                    }
                                    break;
                                default:
                                    Log.e(TAG, "onDataChange: unknown snapshot key");
                            }
                        }
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());

                            db.cowDao().deleteCowTable();
                            db.drugDao().deleteDrugTable();
                            db.drugsGivenDao().deleteDrugsGivenTable();
                            db.penDao().deletePenTable();

                            db.cowDao().insertCowList(mCowEntityUpdateList);
                            db.drugDao().insertListDrug(mDrugEntityUpdateList);
                            db.drugsGivenDao().insertDrugsGivenList(mDrugsGivenEntityUpdateList);
                            db.penDao().insertPenList(mPenEntityUpdateList);

                        }
                    }).start();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void onSignedOutCleanUp(){

        if(mPenRef != null) {
            mPenRef.removeEventListener(mPenListener);
        }

        mPenList.clear();
        mPenRecyclerViewAdapter.swapData(mPenList);

        mPenRef = null;
    }
}
