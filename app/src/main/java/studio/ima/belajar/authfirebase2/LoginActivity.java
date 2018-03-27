package studio.ima.belajar.authfirebase2;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
private static final int RC_SIGN_IN =123;
Boolean doubleBackpress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
//tambahan
    @Override
    public void onBackPressed() {
        if (doubleBackpress){

            super.onBackPressed();
            return;
        }
        this.doubleBackpress=true;
        Toast.makeText(this, "klik kembali 2 kali", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackpress=false;
            }
        },2000);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() !=null){//apakah sudah ada user

            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
            finish();
        }else{
            startActivityForResult(
                    AuthUI.getInstance()
                    .createSignInIntentBuilder().setAvailableProviders(
                            Arrays.asList(  new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build())
                    ).build(),RC_SIGN_IN
            );

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//minta code
        Toast.makeText(this, "On Activity Result ya ", Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            IdpResponse idpResponse = IdpResponse.fromResultIntent(data);

            //jika sukses masuk
            if (requestCode == RESULT_OK){

                Intent masuk = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(masuk);
                finish();
            }else{
                //gagal masuk
                if(idpResponse == null){
                    Toast.makeText(this, "gagal", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                if (idpResponse.getError().getErrorCode()== ErrorCodes.UNKNOWN_ERROR){
                    Toast.makeText(this, "eror2", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(this, "eror1", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
