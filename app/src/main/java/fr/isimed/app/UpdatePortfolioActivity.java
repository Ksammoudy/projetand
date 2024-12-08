package fr.isimed.app;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdatePortfolioActivity extends AppCompatActivity {

    private EditText editTextUpdateTitle, editTextUpdateDescription;
    private Button buttonUpdate;
    private DatabaseReference databaseReference;

    private String portfolioId, portfolioTitle, portfolioDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_portfolio);

        editTextUpdateTitle = findViewById(R.id.editTextUpdateTitle);
        editTextUpdateDescription = findViewById(R.id.editTextUpdateDescription);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        // Récupérer les données transmises
        portfolioId = getIntent().getStringExtra("portfolioId");
        portfolioTitle = getIntent().getStringExtra("portfolioTitle");
        portfolioDescription = getIntent().getStringExtra("portfolioDescription");

        // Pré-remplir les champs
        editTextUpdateTitle.setText(portfolioTitle);
        editTextUpdateDescription.setText(portfolioDescription);

        databaseReference = FirebaseDatabase.getInstance().getReference("portfolios");

        buttonUpdate.setOnClickListener(v -> updatePortfolio());
    }

    private void updatePortfolio() {
        String updatedTitle = editTextUpdateTitle.getText().toString().trim();
        String updatedDescription = editTextUpdateDescription.getText().toString().trim();

        if (TextUtils.isEmpty(updatedTitle) || TextUtils.isEmpty(updatedDescription)) {
            Toast.makeText(this, "Tous les champs sont obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        Portfolio updatedPortfolio = new Portfolio(portfolioId, updatedTitle, updatedDescription, "currentUserId");
        databaseReference.child(portfolioId).setValue(updatedPortfolio)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(UpdatePortfolioActivity.this, "Portfolio mis à jour", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(UpdatePortfolioActivity.this, "Erreur : " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
