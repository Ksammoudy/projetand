package fr.isimed.app;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class PortfolioManager {

    private final FirebaseFirestore firestore;
    private final CollectionReference portfoliosCollection;

    public PortfolioManager() {
        firestore = FirebaseFirestore.getInstance();
        portfoliosCollection = firestore.collection("portfolios");
    }

    // Ajouter un nouveau portfolio
    public void addPortfolio(String userId, String title, String description, OnCompleteListener<Void> onCompleteListener) {
        String id = portfoliosCollection.document().getId(); // Générer un ID unique
        Portfolio portfolio = new Portfolio(id, title, description, userId);
        portfoliosCollection.document(id).set(portfolio).addOnCompleteListener(onCompleteListener);
    }

    // Récupérer les portfolios d'un utilisateur
    public void getUserPortfolios(String userId, OnCompleteListener<QuerySnapshot> onCompleteListener) {
        portfoliosCollection.whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }

    // Mettre à jour un portfolio
    public void updatePortfolio(String portfolioId, Map<String, Object> updatedData, OnCompleteListener<Void> onCompleteListener) {
        portfoliosCollection.document(portfolioId)
                .update(updatedData)
                .addOnCompleteListener(onCompleteListener);
    }


    // Supprimer un portfolio
    public void deletePortfolio(String portfolioId, OnCompleteListener<Void> onCompleteListener) {
        portfoliosCollection.document(portfolioId)
                .delete()
                .addOnCompleteListener(onCompleteListener);
    }
}
