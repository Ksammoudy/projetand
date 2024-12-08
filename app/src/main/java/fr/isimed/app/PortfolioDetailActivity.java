package fr.isimed.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import android.widget.Button;


public class PortfolioDetailActivity extends AppCompatActivity implements ProjectAdapter.OnProjectClickListener {
    private RecyclerView recyclerViewProjects;
    private ProjectAdapter projectAdapter;
    private List<Project> projectList;
    private String portfolioId;
    private Button btnAddProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_detail);

        // Récupérer l'ID du portfolio
        portfolioId = getIntent().getStringExtra("portfolioId");

        // Initialiser la liste des projets (Exemple, en attendant les données Firebase)
        projectList = new ArrayList<>();

        // RecyclerView et LayoutManager
        recyclerViewProjects = findViewById(R.id.recyclerViewProjects);
        recyclerViewProjects.setLayoutManager(new LinearLayoutManager(this));

        // Initialiser l'adaptateur
        projectAdapter = new ProjectAdapter(this, projectList, this);
        recyclerViewProjects.setAdapter(projectAdapter);

        // Bouton pour ajouter un projet
        btnAddProject = findViewById(R.id.btnAddProject);
        btnAddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Naviguer vers l'Activity d'ajout de projet
                Intent intent = new Intent(PortfolioDetailActivity.this, AddProjectActivity.class);
                intent.putExtra("portfolioId", portfolioId);
                startActivityForResult(intent, 100); // Utilisation de startActivityForResult pour récupérer le nouveau projet
            }
        });
    }

    @Override
    public void onEditClick(Project project) {
        // Rediriger vers l'Activity de mise à jour
        Intent intent = new Intent(this, UpdateProjectActivity.class);
        intent.putExtra("projectId", project.getId());
        intent.putExtra("portfolioId", portfolioId);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Project project) {
        // Supprimer un projet de la liste
        projectList.remove(project);
        projectAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            // Récupérer le projet ajouté
            Project newProject = (Project) data.getSerializableExtra("newProject");
            projectList.add(newProject);
            projectAdapter.notifyDataSetChanged();
        }
    }
}
