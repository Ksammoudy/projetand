package fr.isimed.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import android.content.Intent;  // N'oubliez pas d'importer Intent


public class AuthActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private LinearLayout loginSection, registerSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);


        // Initialiser FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Sections UI
        loginSection = findViewById(R.id.loginSection);
        registerSection = findViewById(R.id.registerSection);

        // Boutons et champs de connexion
        EditText emailInputLogin = findViewById(R.id.emailInputLogin);
        EditText passwordInputLogin = findViewById(R.id.passwordInputLogin);
        Button loginButton = findViewById(R.id.loginButton);
        TextView switchToRegister = findViewById(R.id.switchToRegister);

        // Boutons et champs d'inscription
        EditText emailInputRegister = findViewById(R.id.emailInputRegister);
        EditText passwordInputRegister = findViewById(R.id.passwordInputRegister);
        Button registerButton = findViewById(R.id.registerButton);
        TextView switchToLogin = findViewById(R.id.switchToLogin);

        // Gestion de la connexion
        loginButton.setOnClickListener(v -> {
            String email = emailInputLogin.getText().toString();
            String password = passwordInputLogin.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Connexion réussie", Toast.LENGTH_SHORT).show();
                            // Redirection après connexion
                            // startActivity(new Intent(this, MainActivity.class));
                            navigateToAddPortfolio();
                        } else {
                            Toast.makeText(this, "Erreur : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Gestion de l'inscription
        registerButton.setOnClickListener(v -> {
            String email = emailInputRegister.getText().toString();
            String password = passwordInputRegister.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                            // Retour à l'écran de connexion
                            switchToLogin();
                        } else {
                            Toast.makeText(this, "Erreur : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Bouton pour basculer vers la section d'inscription
        switchToRegister.setOnClickListener(v -> switchToRegister());

        // Bouton pour basculer vers la section de connexion
        switchToLogin.setOnClickListener(v -> switchToLogin());
    }

    private void switchToRegister() {
        loginSection.setVisibility(View.GONE);
        registerSection.setVisibility(View.VISIBLE);
    }

    private void switchToLogin() {
        registerSection.setVisibility(View.GONE);
        loginSection.setVisibility(View.VISIBLE);
    }
    private void navigateToAddPortfolio() {
        // Rediriger vers AddPortfolioActivity
        Intent intent = new Intent(AuthActivity.this, CreatePortfolioActivity.class);
        startActivity(intent);
        finish();


    }

}
