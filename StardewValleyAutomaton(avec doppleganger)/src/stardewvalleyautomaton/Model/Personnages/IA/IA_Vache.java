/*
 * IA d'une vache
 */
package stardewvalleyautomaton.Model.Personnages.IA;

import java.util.ArrayList;
import java.util.Random;
import stardewvalleyautomaton.Graphe.Graphe;
import stardewvalleyautomaton.Model.Carte;
import stardewvalleyautomaton.Model.Cases.Case;
import stardewvalleyautomaton.Model.Gestionnaires.GestionnaireDesPersonnages;
import stardewvalleyautomaton.Model.Personnages.Abigail;
import static stardewvalleyautomaton.Model.Personnages.Enum_Personnage.*;
import static stardewvalleyautomaton.Model.Personnages.IA.Enum_Action.*;
import stardewvalleyautomaton.Model.Personnages.Personnage;
import stardewvalleyautomaton.Model.Personnages.Vache;

/**
 *
 * @author Matthieu
 */
public class IA_Vache extends IA {

    
    @Override
    protected void setActionValide() {
        this.addActionValide(attendre);
        this.addActionValide(moveLeft);
        this.addActionValide(moveRight);
        this.addActionValide(moveTop);
        this.addActionValide(moveBottom);
        this.addActionValide(produireLait);
    }
    
    @Override
    public Enum_Action action() {
        Enum_Action resultat;
        
        //liste toutes les actions que la poule peut faire
        ArrayList<Enum_Action> actionPossible = new ArrayList<>();
        actionPossible.add(attendre);
        
        Case positionActuelle = this.personnage().getCase();
        int ligne = positionActuelle.getLigne();
        int colonne = positionActuelle.getColonne();
        
        if(colonne-1>=0) {
            if(Carte.get().getCase(ligne, colonne-1).estLibre()) {
                actionPossible.add(moveLeft);
            }
        }
        if(colonne+2<Carte.get().taille()) {
            if(Carte.get().getCase(ligne, colonne+2).estLibre()) {
                actionPossible.add(moveRight);
            }
        }
        if(ligne-1>=0 && colonne+1<Carte.get().taille()) {
            if(Carte.get().getCase(ligne-1, colonne).estLibre() && Carte.get().getCase(ligne-1, colonne+1).estLibre()) {
                actionPossible.add(moveTop);
            }
        }
        if(ligne+1<Carte.get().taille() && colonne+1<Carte.get().taille()) {
            if(Carte.get().getCase(ligne+1, colonne).estLibre() && Carte.get().getCase(ligne+1, colonne+1).estLibre()) {
                actionPossible.add(moveBottom);
            }
        }
        
        //choisie une action au hasard
        Random random = new Random();
        int alea = random.nextInt(actionPossible.size());
        
        resultat = actionPossible.get(alea);
        
        //gestion de la production de lait
        if(resultat == attendre) {
            if(random.nextInt(15) == 0) {
                resultat = produireLait;
            }
        }
        /*
        if(recupererVache().lait()) {
            int positionVache = Graphe.coordonnees2Sommet(this.personnage().getCase().getLigne(), 
                    this.personnage().getCase().getColonne()-1);
            Abigail a = recupererAbigail();
            int positionAbigail = Graphe.coordonnees2Sommet(a.getCase().getColonne(), 
                    a.getCase().getLigne()-1);
            int distance = Carte.get().getGraphe().distanceDijkstra(positionVache, positionAbigail);
            if(distance < 5) {
                resultat = attendre;
            }
        }*/
        
        if(recupererVache().lait()) {
            resultat = attendre;
        }
                
        return resultat;
    }
    
    public Vache recupererVache() {
        ArrayList<Personnage> perso = GestionnaireDesPersonnages.getListeDesPersonnages();
        Vache v = null;
        for(Personnage p : perso) {
            if(p.getType() == Vache) {
                v = (Vache)p;
            }
        } 
        return v;
    }
    
    public Abigail recupererAbigail() {
        ArrayList<Personnage> perso = GestionnaireDesPersonnages.getListeDesPersonnages();
        Abigail abi = null;
        for(Personnage p : perso) {
            if(p.getType() == Abigail) {
                abi = (Abigail)p;
            }
        } 
        return abi;
    }
    
}
