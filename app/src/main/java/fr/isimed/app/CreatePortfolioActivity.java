package fr.isimed.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class CreatePortfolioActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription;
    private Button buttonCreatePortfolio;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_portfolio);

        // Initialisation des composants
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonCreatePortfolio = findViewById(R.id.buttonCreatePortfolio);

        // Référence Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("portfolios");

        buttonCreatePortfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPortfolio();
            }
        });
    }

    private void createPortfolio() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String userId = "currentUserId"; // Remplacer par l'ID de l'utilisateur connecté

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Génération d'un ID unique
        String id = UUID.randomUUID().toString();

        // Création de l'objet Portfolio
        Portfolio portfolio = new Portfolio(id, title, description, userId);

        // Sauvegarde dans Firebase
        databaseReference.child(id).setValue(portfolio)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(CreatePortfolioActivity.this, "Portfolio créé avec succès mr khalil", Toast.LENGTH_SHORT).show();
                     // Ferme l'activité
                    navigate();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CreatePortfolioActivity.this, "Erreur lors de la création", Toast.LENGTH_SHORT).show();
                });
    }
    private void navigate() {
        // Rediriger vers AddPortfolioActivity
        Intent intent = new Intent(CreatePortfolioActivity.this, PortfolioListActivity.class);
        startActivity(intent);


    }
}
