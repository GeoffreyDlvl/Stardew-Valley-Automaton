/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stardewvalleyautomaton.Model.Personnages.Automate;

import java.util.ArrayList;
import stardewvalleyautomaton.Graphe.Graphe;
import static stardewvalleyautomaton.IHM.Moment_Journée.Soir;
import stardewvalleyautomaton.IHM.TimerIAHandler;
import stardewvalleyautomaton.Model.Carte;
import stardewvalleyautomaton.Model.Gestionnaires.GestionnaireDesPersonnages;
import static stardewvalleyautomaton.Model.Personnages.Enum_Personnage.Vache;
import static stardewvalleyautomaton.Model.Personnages.IA.Enum_Action.traire;
import stardewvalleyautomaton.Model.Personnages.IA.IA_Abigail;
import stardewvalleyautomaton.Model.Personnages.Personnage;

/**
 *
 * @author Geoffrey
 */
public class Trait extends Etat {

    public Trait(IA_Abigail ia) {
        super(ia);
    }

    @Override
    public Etat etatSuivant() {
        Etat etat;
        
        if(TimerIAHandler.getPeriode() == Soir)
            etat = new ReposMaison(ia);
        else if(ia.aSoif())
            etat = new Boit(ia);
        else
            etat = new FabriqueFromage(ia);
        
        return etat;
    }

    @Override
    public void action() {
        System.out.println("Abigail va traire la vache.");
        Carte carte = Carte.get();
        Graphe monGraphe = carte.getGraphe();
        ArrayList<Personnage> perso = GestionnaireDesPersonnages.getListeDesPersonnages();
        
        for(Personnage p : perso) {
            if(p.getType() == Vache) {
                int positionVache = Graphe.coordonnees2Sommet(p.getCase().getLigne(), 
                        p.getCase().getColonne());
                int dist = monGraphe.distanceDijkstra(ia.getAbigail().getPositionCourante(), positionVache);
                ArrayList<Integer> parcours = monGraphe.getParcours();
                parcours.remove(parcours.size()-1);
                ia.deplacement(parcours);
                break;
            }
        }
           
        int positionVache = Graphe.coordonnees2Sommet(ia.recupererVache().getCase().getColonne(), 
                ia.recupererVache().getCase().getLigne());
        ia.getListeAction().add(traire);   
    }
}
