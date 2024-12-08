package fr.isimed.app;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.widget.Button;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import android.content.Intent;
import java.util.List;
import java.util.ArrayList;
import java.util.List;
public class PortfolioAdapter extends RecyclerView.Adapter<PortfolioAdapter.PortfolioViewHolder> {

    private OnItemClickListener listener;
    private Context context;
    private List<Portfolio> portfolioList;

    // Constructeur pour passer la liste des portfolios
    public PortfolioAdapter(Context context, List<Portfolio> portfolioList, OnItemClickListener listener) {
        this.context = context;
        this.portfolioList = portfolioList;
        this.listener = listener;
    }
    public PortfolioAdapter( List<Portfolio> portfolioList){
        this.portfolioList = portfolioList;

    }

    // Interface pour gérer les clics sur les éléments
    public interface OnItemClickListener {
        void onItemClick(Portfolio portfolio);
        void onEditClick(Portfolio portfolio);  // Méthode pour l'édition
        void onDeleteClick(Portfolio portfolio);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Créez une nouvelle vue
    @NonNull
    @Override
    public PortfolioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.portfolio_item, parent, false);
        return new PortfolioViewHolder(view);
    }

    // Remplir l'élément de la vue avec les données du portfolio
    @Override
    public void onBindViewHolder(@NonNull PortfolioViewHolder holder, int position) {
        Portfolio portfolio = portfolioList.get(position);
        if (portfolio != null) {
            holder.titleTextView.setText(portfolio.getTitle());
            holder.descriptionTextView.setText(portfolio.getDescription());
            holder.buttonEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(portfolio);
                }
            });

            // Bouton "Supprimer"
            holder.buttonDelete.setOnClickListener(v -> {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("portfolios");
                databaseReference.child(portfolio.getId()).removeValue();
            });
        }
        }



    public int getItemCount() {
        return portfolioList.size();
    }

    // Classe interne pour le ViewHolder
    public class PortfolioViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        Button buttonEdit, buttonDelete;
        public PortfolioViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.portfolio_title);
            descriptionTextView = itemView.findViewById(R.id.portfolio_description);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(portfolioList.get(position));
                }
            });
        }
    }

    // Méthode pour mettre à jour la liste des portfolios
    public void updatePortfolioList(List<DocumentSnapshot> newPortfolioList) {
        // Convertir les DocumentSnapshot en Portfolio
        List<Portfolio> portfolios = new ArrayList<>();
        for (DocumentSnapshot snapshot : newPortfolioList) {
            Portfolio portfolio = snapshot.toObject(Portfolio.class); // Utilisez Firestore pour la conversion
            if (portfolio != null) {
                portfolios.add(portfolio);
            }
        }
        this.portfolioList = portfolios; // Assigner la liste convertie
        notifyDataSetChanged();
    }
}
