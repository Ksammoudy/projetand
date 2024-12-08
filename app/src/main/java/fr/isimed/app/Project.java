package fr.isimed.app;


/**
 * Classe représentant un projet dans le portfolio.
 */
import java.io.Serializable;



public class Project implements Serializable {
    private String id; // ID du projet
    private String name;
    private String description;
    private String portfolioId;
    private String category;


    // Constructeur vide requis pour Firebase
    public Project() {
    }

    // Constructeur avec paramètres
    public Project(String id, String name, String description, String portfolioId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.portfolioId = portfolioId;
    }
    public Project(String id, String title, String description, String category, String portfolioId) {
        this.id = id;
        this.name = title;
        this.description = description;
        this.category = category;
        this.portfolioId = portfolioId;
    }

    // Getter et setter pour l'ID
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getters et setters pour les autres champs
    public String getTitle() {
        return name;
    }

    public void setTitle(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(String portfolioId) {
        this.portfolioId = portfolioId;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
