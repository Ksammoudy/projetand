package fr.isimed.app;

import java.util.ArrayList;
import java.util.List;
public class Portfolio {
    private String id;          // ID unique du portfolio
    private String title;       // Titre du portfolio
    private String description; // Description du portfolio
    private String userId;      // ID de l'utilisateur propriétaire du portfolio
    private List<Project> projects; // Liste des projets

    // Constructeur par défaut (requis pour Firebase)
    public Portfolio() {
    }

    // Constructeur personnalisé
    public Portfolio(String id, String title, String description, String userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userId = userId;
    }
    public Portfolio(String id, String title, String description, String userId, List<Project> projects) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.projects = projects;
    }

    // Getters et Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getName() {
        return title; // Si vous souhaitez que 'getName()' renvoie le titre
    }
    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    // Méthode pour ajouter un projet
    public void addProject(Project project) {
        if (projects == null) {
            projects = new ArrayList<>();
        }
        projects.add(project);
    }

    // Méthode pour supprimer un projet
    public void removeProject(String projectId) {
        if (projects != null) {
            projects.removeIf(project -> project.getId().equals(projectId));
        }
    }
}
