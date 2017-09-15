/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stardewvalleyautomaton.Model.Personnages.AutomateSimone;

import stardewvalleyautomaton.Model.Personnages.AutomateAbigail.*;
import java.util.ArrayList;
import stardewvalleyautomaton.Graphe.Graphe;
import static stardewvalleyautomaton.IHM.Moment_Journée.Soir;
import stardewvalleyautomaton.IHM.TimerIAHandler;
import stardewvalleyautomaton.Model.Carte;
import stardewvalleyautomaton.Model.Gestionnaires.GestionnaireDesPersonnages;
import static stardewvalleyautomaton.Model.Personnages.Enum_Personnage.Vache;
import static stardewvalleyautomaton.Model.Personnages.IA.Enum_Action.traire;
import stardewvalleyautomaton.Model.Personnages.IA.IA_Abigail;
import stardewvalleyautomaton.Model.Personnages.IA.IA_Simone;
import stardewvalleyautomaton.Model.Personnages.Personnage;

/**
 *
 * @author Geoffrey
 */
public class TraitS extends EtatS {

    public TraitS(IA_Simone ia) {
        super(ia);
    }

    @Override
    public EtatS etatSuivant() {
        EtatS etat;
        
        if(TimerIAHandler.getPeriode() == Soir)
            etat = new ReposMaisonS(ia);
        else if(ia.aSoif())
            etat = new BoitS(ia);
        else
            etat = new FabriqueFromageS(ia);
        
        return etat;
    }

    @Override
    public void action() {
        System.out.println("Simone va traire la vache.");
        Carte carte = Carte.get();
        Graphe monGraphe = carte.getGraphe();
        ArrayList<Personnage> perso = GestionnaireDesPersonnages.getListeDesPersonnages();
        
        for(Personnage p : perso) {
            if(p.getType() == Vache) {
                int positionVache = Graphe.coordonnees2Sommet(p.getCase().getLigne(), 
                        p.getCase().getColonne());
                int dist = monGraphe.distanceDijkstra(ia.getSimone().getPositionCourante(), positionVache);
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
