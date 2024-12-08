package fr.isimed.app;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError; // Import pour DatabaseError
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProjectManager {
    private DatabaseReference databaseReference;

    // Constructeur
    public ProjectManager() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Projects");
    }

    // Récupérer tous les projets
    public void getAllProjects(OnProjectsReceivedListener listener) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Project> projects = new ArrayList<>();
                for (DataSnapshot projectSnapshot : snapshot.getChildren()) {
                    Project project = projectSnapshot.getValue(Project.class);
                    if (project != null) {
                        projects.add(project);
                    }
                }
                listener.onProjectsReceived(projects);
            }
            public void onCancelled(DatabaseError error) {
                listener.onError(error.toException());
            }
        });
    }

    // Interface pour les callbacks
    public interface OnProjectsReceivedListener {
        void onProjectsReceived(List<Project> projects);
        void onError(Exception exception);
    }
}
