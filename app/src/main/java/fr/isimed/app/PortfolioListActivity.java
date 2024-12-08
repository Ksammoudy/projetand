package fr.isimed.app;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QuerySnapshot;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class PortfolioListActivity extends AppCompatActivity {
    private PortfolioManager portfolioManager;
        private RecyclerView recyclerViewPortfolios;
        private PortfolioAdapter portfolioAdapter;
        private List<Portfolio> portfolioList;
        private DatabaseReference databaseReference;
        private Button buttonAddPortfolio;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_portfolio_list);

            // Initialisation des vues
            recyclerViewPortfolios = findViewById(R.id.recyclerView);
            buttonAddPortfolio = findViewById(R.id.buttonAddPortfolio);

            // Configuration de la RecyclerView
            recyclerViewPortfolios.setLayoutManager(new LinearLayoutManager(this));
            portfolioList = new ArrayList<>();
            portfolioAdapter = new PortfolioAdapter(this, portfolioList, new PortfolioAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(Portfolio portfolio) {
                    Intent intent = new Intent(PortfolioListActivity.this, PortfolioDetailActivity.class);
                    intent.putExtra("portfolioId", portfolio.getId()); // Passer l'ID du portfolio
                    startActivity(intent);


                }

                public void onEditClick(Portfolio portfolio) {
                    // Redirige vers l'écran de modification
                    Intent intent = new Intent(PortfolioListActivity.this, UpdatePortfolioActivity.class);
                    intent.putExtra("portfolioId", portfolio.getId());
                    intent.putExtra("portfolioTitle", portfolio.getTitle());
                    intent.putExtra("portfolioDescription", portfolio.getDescription());
                    startActivity(intent);

                }


                public void onDeleteClick(Portfolio portfolio) {
                    // Supprimer le portfolio de Firebase
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("portfolios");
                    databaseReference.child(portfolio.getId()).removeValue()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(PortfolioListActivity.this, "Portfolio supprimé", Toast.LENGTH_SHORT).show();
                                portfolioList.remove(portfolio);
                                portfolioAdapter.notifyDataSetChanged();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(PortfolioListActivity.this, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                            });
                }
            });
            recyclerViewPortfolios.setAdapter(portfolioAdapter);

            // Référence Firebase
            databaseReference = FirebaseDatabase.getInstance().getReference("portfolios");

            // Charger les portfolios
            loadPortfolios();

            // Redirection vers l'écran de création
            buttonAddPortfolio.setOnClickListener(v -> {
                Intent intent = new Intent(PortfolioListActivity.this, CreatePortfolioActivity.class);
                startActivity(intent);
            });
        }

        private void loadPortfolios() {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    portfolioList.clear(); // Vider la liste pour éviter les doublons
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Portfolio portfolio = snapshot.getValue(Portfolio.class);
                        portfolioList.add(portfolio);
                    }
                    portfolioAdapter.notifyDataSetChanged(); // Notifier l'adaptateur des changements
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(PortfolioListActivity.this, "Erreur : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
