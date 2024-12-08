package fr.isimed.app;

import android.content.Intent;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateProjectActivity extends AppCompatActivity {

    private EditText titleInput, descriptionInput, categoryInput;
    private Button updateButton;
    private Project project;
    private int position;

    private DatabaseReference projectRef;
    private String projectId, portfolioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_project);

        // Initialisation des vues
        titleInput = findViewById(R.id.titleInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        categoryInput = findViewById(R.id.categoryInput);
        updateButton = findViewById(R.id.updateButton);

        // Récupération des données du projet
        projectId = getIntent().getStringExtra("projectId");
        portfolioId = getIntent().getStringExtra("portfolioId");
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String category = getIntent().getStringExtra("category");

        // Pré-remplir les champs
        titleInput.setText(title);
        descriptionInput.setText(description);
        categoryInput.setText(category);

        // Référence Firebase pour le projet
        projectRef = FirebaseDatabase.getInstance()
                .getReference("Portfolios")
                .child(portfolioId)
                .child("projects")
                .child(projectId);

        // Mettre à jour le projet
        updateButton.setOnClickListener(v -> updateProject());
    }

    private void updateProject() {
        String updatedTitle = titleInput.getText().toString().trim();
        String updatedDescription = descriptionInput.getText().toString().trim();
        String updatedCategory = categoryInput.getText().toString().trim();

        if (updatedTitle.isEmpty() || updatedDescription.isEmpty() || updatedCategory.isEmpty()) {
            Toast.makeText(this, "Tous les champs sont obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mise à jour dans Firebase
        projectRef.child("title").setValue(updatedTitle);
        projectRef.child("description").setValue(updatedDescription);
        projectRef.child("category").setValue(updatedCategory)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Projet mis à jour avec succès", Toast.LENGTH_SHORT).show();
                        finish(); // Fermer l'activité
                    } else {
                        Toast.makeText(this, "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show();
                    }
                });
        Intent resultIntent = new Intent();
        resultIntent.putExtra("updatedProject", project);
        resultIntent.putExtra("position", position);
        setResult(RESULT_OK, resultIntent);
        finish();

    }
}
