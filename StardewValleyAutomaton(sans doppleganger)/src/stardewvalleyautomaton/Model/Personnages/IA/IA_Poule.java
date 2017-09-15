/*
 * ia d'une poule
 */
package stardewvalleyautomaton.Model.Personnages.IA;

import java.util.ArrayList;
import java.util.Random;
import stardewvalleyautomaton.Model.Carte;
import stardewvalleyautomaton.Model.Cases.Case;
import static stardewvalleyautomaton.Model.Objets.Enum_Objet.Barriere;
import static stardewvalleyautomaton.Model.Objets.Enum_Objet.Oeuf;
import static stardewvalleyautomaton.Model.Personnages.IA.Enum_Action.*;

/**
 *
 * @author Matthieu
 * @author Geoffrey Delval
 */
public class IA_Poule extends IA {


    private int nbAction = 1;
    
    
    @Override
    protected void setActionValide() {
        this.addActionValide(attendre);
        this.addActionValide(moveLeft);
        this.addActionValide(moveRight);
        this.addActionValide(moveTop);
        this.addActionValide(moveBottom);
        this.addActionValide(pondre);
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
        int taille = Carte.get().taille();
        
        if(colonne-1>=0) {
            //on vérifie que la case à gauche est libre, ainsi que ses cases adjacentes (haut et bas)
            if(Carte.get().getCase(ligne, colonne-1).estLibre() &&
                    Carte.get().getCase(ligne-1, colonne-1).estLibre() &&
                    Carte.get().getCase(ligne+1, colonne-1).estLibre()) {
                actionPossible.add(moveLeft);
            }
        }
        if(colonne+1<Carte.get().taille()) {
            //on vérifie que la case à droite est libre, ainsi que ses cases adjacentes (haut et bas)
            if(Carte.get().getCase(ligne, colonne+1).estLibre() &&
                    Carte.get().getCase(ligne-1, colonne+1).estLibre() &&
                    Carte.get().getCase(ligne+1, colonne+1).estLibre()) {
                actionPossible.add(moveRight);
            }
        }
        if(ligne-1>=0) {
            //on vérifie que la case en haut est libre, ainsi que ses cases adjacentes (droite et gauche)
            if(Carte.get().getCase(ligne-1, colonne).estLibre() &&
                    Carte.get().getCase(ligne-1, colonne-1).estLibre() &&
                    Carte.get().getCase(ligne-1, colonne+1).estLibre()) {
                actionPossible.add(moveTop);
            }
        }
        if(ligne+1<Carte.get().taille()) {
            //on vérifie que la case en bas est libre, ainsi que ses cases adjacentes (droite et gauche)
            if(Carte.get().getCase(ligne+1, colonne).estLibre() &&
                    Carte.get().getCase(ligne+1, colonne-1).estLibre() &&
                    Carte.get().getCase(ligne+1, colonne+1).estLibre()) {
                actionPossible.add(moveBottom);
            }
        }
        
        //choisie une action au hasard
        Random random = new Random();
        int alea = random.nextInt(actionPossible.size());
        resultat = actionPossible.get(alea);
        
        //GESTION DE LA PONTE
        if(resultat == attendre) {
            if(random.nextInt(10)==0) {
                resultat = pondre;
            }
        }
        
        return resultat;
    }
    
}
