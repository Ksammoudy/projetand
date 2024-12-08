package fr.isimed.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Map;
import java.util.HashMap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProjectListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView projectRecyclerView;
    private ProjectAdapter projectAdapter;
    private List<Project> projectList;
    private DatabaseReference projectRef;
    private String portfolioId, portfolioTitle;
    private Button addProjectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        // Récupérer l'ID et le titre du portfolio depuis l'Intent
        portfolioId = getIntent().getStringExtra("portfolioId");
        portfolioTitle = getIntent().getStringExtra("portfolioTitle");

        // Configurer le titre de l'activité
        setTitle("Projets : " + portfolioTitle);

        // Initialiser les vues
        projectRecyclerView = findViewById(R.id.projectRecyclerView);
        addProjectButton = findViewById(R.id.addProjectButton);
        projectRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        projectList = new ArrayList<>();
        projectAdapter = new ProjectAdapter(this, projectList, new ProjectAdapter.OnProjectClickListener() {
            @Override
            public void onEditClick(Project project) {
                // Démarrer l'activité d'édition
                Intent intent = new Intent(ProjectListActivity.this, UpdateProjectActivity.class);
                intent.putExtra("projectId", project.getId());
                intent.putExtra("portfolioId", portfolioId);
                intent.putExtra("title", project.getTitle());
                intent.putExtra("description", project.getDescription());
                intent.putExtra("category", project.getCategory());
                startActivity(intent);
            }


            public void onDeleteClick(Project project) {
                // Supprimer le projet
                deleteProject(project.getId());
            }
        });
        projectRecyclerView.setAdapter(projectAdapter);

        // Charger les projets depuis Firebase
        projectRef = FirebaseDatabase.getInstance().getReference("portfolios").child(portfolioId).child("projects");
        loadProjects();

        // Ajouter un projet
        addProjectButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProjectListActivity.this, AddProjectActivity.class);
            intent.putExtra("portfolioId", portfolioId);
            startActivity(intent);
        });
    }

    private void loadProjects() {
        projectRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Portfolio> portfolioMap = new HashMap<>();

                // Lire les données comme une Map
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    Portfolio portfolio = snapshot.getValue(Portfolio.class);
                    portfolioMap.put(key, portfolio);
                }

                // Convertir en liste
                List<Portfolio> portfolioList = new ArrayList<>(portfolioMap.values());

                // Configurer l'Adapter avec la liste obtenue
                PortfolioAdapter adapter = new PortfolioAdapter(portfolioList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProjectListActivity.this, "Erreur lors du chargement des projets", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteProject(String projectId) {
        projectRef.child(projectId).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Projet supprimé", Toast.LENGTH_SHORT).show();
                    loadProjects();  // Recharger la liste
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                });
    }
}

