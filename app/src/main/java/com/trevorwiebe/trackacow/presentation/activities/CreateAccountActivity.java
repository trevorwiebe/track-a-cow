package com.trevorwiebe.trackacow.presentation.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.trevorwiebe.trackacow.R;

public class CreateAccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private GoogleSignInClient mGoogleSignInClient;

    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;
    private ProgressBar mCreatingAccount;
    private ProgressBar mCreatingAccountWithGoogle;
    private Button mCreateAccountBtn;
    private Button mCreateAccountWithGoogleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccentDark));

        mName = findViewById(R.id.create_account_name);
        mEmail = findViewById(R.id.create_account_email);
        mPassword = findViewById(R.id.create_account_password);
        mCreatingAccount = findViewById(R.id.creating_account);
        mCreatingAccountWithGoogle = findViewById(R.id.creating_account_with_google);
        mCreateAccountBtn = findViewById(R.id.create_account_btn);
        mCreateAccountWithGoogleBtn = findViewById(R.id.create_account_with_google);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        mCreateAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mName.length() == 0) {
                    mName.requestFocus();
                    mName.setError("Please fill in name");
                } else if (mEmail.length() == 0) {
                    mEmail.requestFocus();
                    mEmail.setError("Please fill in email");
                } else if (mPassword.length() == 0) {
                    mPassword.requestFocus();
                    mPassword.setError("Please fill in password");
                } else {

                    mCreatingAccount.setVisibility(View.VISIBLE);
                    final String name = mName.getText().toString();
                    final String email = mEmail.getText().toString();
                    String password = mPassword.getText().toString();

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        final FirebaseUser user = task.getResult().getUser();

                                        if (user != null) {
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(name)
                                                    .build();

                                            user.updateProfile(profileUpdates)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
                                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                startActivity(intent);
                                                            } else {
                                                                String errorMessage = task.getException().getLocalizedMessage();
                                                                showMessage("Could not set name to account", errorMessage);
                                                            }
                                                        }
                                                    });
                                        } else {
                                            showMessage("User was null", "");
                                        }
                                    } else {
                                        String errorMessage = task.getException().getLocalizedMessage();
                                        showMessage("There was an error", errorMessage);
                                    }
                                }
                            });

                }
            }
        });

        mCreateAccountWithGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGoogleSignInForResult();
            }
        });

    }

    ActivityResultLauncher<Intent> signInActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        try {
                            // Google Sign In was successful, authenticate with Firebase
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            fireBaseAuthWithGoogle(account);
                        } catch (ApiException e) {
                            // Google Sign In failed, update UI appropriately
                            String errorMessage = e.getLocalizedMessage();
                            showMessage("Error creating account", errorMessage);
                        }
                    }
                }
            });

    private void openGoogleSignInForResult(){
        mCreatingAccountWithGoogle.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        signInActivity.launch(signInIntent);
    }


    private void fireBaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        final String name = acct.getDisplayName();
        final String email = acct.getEmail();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            String errorMessage = task.getException().getLocalizedMessage();
                            showMessage("Error creating account", errorMessage);
                        }
                    }
                });
    }

    private void showMessage(String title, String errorMessage) {
        AlertDialog.Builder signInError = new AlertDialog.Builder(CreateAccountActivity.this);
        signInError.setTitle(title);
        signInError.setMessage(errorMessage);
        signInError.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        signInError.show();

        mCreatingAccountWithGoogle.setVisibility(View.INVISIBLE);

        mCreatingAccount.setVisibility(View.INVISIBLE);
    }

}
