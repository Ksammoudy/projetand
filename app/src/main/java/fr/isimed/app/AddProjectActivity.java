package fr.isimed.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddProjectActivity extends AppCompatActivity {
    private EditText titleInput, descriptionInput, categoryInput;
    private Button saveButton;
    private DatabaseReference projectRef;
    private String portfolioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        portfolioId = getIntent().getStringExtra("portfolioId");

        titleInput = findViewById(R.id.titleInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        categoryInput = findViewById(R.id.categoryInput);
        saveButton = findViewById(R.id.saveButton);

        projectRef = FirebaseDatabase.getInstance().getReference("portfolios").child(portfolioId).child("projects");

        saveButton.setOnClickListener(v -> saveProject());
    }

    private void saveProject() {
        String title = titleInput.getText().toString();
        String description = descriptionInput.getText().toString();
        String category = categoryInput.getText().toString();

        if (title.isEmpty() || description.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        String projectId = projectRef.push().getKey();
        Project project = new Project(projectId, title, description, category);

        projectRef.child(projectId).setValue(project)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Projet ajouté", Toast.LENGTH_SHORT).show();
                    navigate();

                })
                .addOnFailureListener(e -> Toast.makeText(this, "Erreur", Toast.LENGTH_SHORT).show());
    }
    private void navigate() {
        // Rediriger vers AddPortfolioActivity
        Intent intent = new Intent(AddProjectActivity.this, ProjectListActivity.class);
        startActivity(intent);
        finish();


    }

}
