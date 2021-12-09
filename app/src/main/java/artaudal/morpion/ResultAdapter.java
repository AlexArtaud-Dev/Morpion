package artaudal.morpion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



import java.util.List;

import artaudal.morpion.database.Joueur;

public class ResultAdapter extends ArrayAdapter<Joueur> {

    public ResultAdapter(Context mCtx, List<Joueur> joueurList) {
        super(mCtx, R.layout.template_joueurs, joueurList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Récupération de la multiplication
        final Joueur joueur = getItem(position);

        // Charge le template XML
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.template_joueurs, parent, false);

        // Récupération des objets graphiques dans le template
        TextView textViewPseudo = (TextView) rowView.findViewById(R.id.username);
        TextView textViewVictory = (TextView) rowView.findViewById(R.id.user_victory);
        TextView textViewDraw = (TextView) rowView.findViewById(R.id.user_draw);
        TextView textViewDefeat = (TextView) rowView.findViewById(R.id.user_defeat);

        //
        textViewPseudo.setText(joueur.getPseudo());
        textViewVictory.setText(String.valueOf(joueur.getVictory()));
        textViewDraw.setText(String.valueOf(joueur.getDraw()));
        textViewDefeat.setText(String.valueOf(joueur.getDefeat()));

        //
        return rowView;
    }
}
