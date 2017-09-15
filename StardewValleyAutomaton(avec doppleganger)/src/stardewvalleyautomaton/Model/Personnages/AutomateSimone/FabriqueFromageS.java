/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stardewvalleyautomaton.Model.Personnages.AutomateSimone;

import stardewvalleyautomaton.Model.Personnages.AutomateSimone.*;
import java.util.ArrayList;
import stardewvalleyautomaton.Graphe.Graphe;
import static stardewvalleyautomaton.IHM.Moment_Journée.Soir;
import stardewvalleyautomaton.IHM.TimerIAHandler;
import stardewvalleyautomaton.Model.Carte;
import stardewvalleyautomaton.Model.Gestionnaires.GestionnaireDesObjets;
import static stardewvalleyautomaton.Model.Objets.Enum_Objet.Machine_Fromage;
import stardewvalleyautomaton.Model.Objets.Objet;
import static stardewvalleyautomaton.Model.Personnages.IA.Enum_Action.produireFromage;
import stardewvalleyautomaton.Model.Personnages.IA.IA_Simone;

/**
 *
 * @author Geoffrey
 */
public class FabriqueFromageS extends EtatS {

    public FabriqueFromageS(IA_Simone ia) {
        super(ia);
    }

    @Override
    public EtatS etatSuivant() {
        EtatS etat;
        
        if(TimerIAHandler.getPeriode() == Soir)
            etat = new ReposMaisonS(ia);
        else
            etat = new MangeS(ia);
        
        return etat;
    }

    @Override
    public void action() {
        System.out.println("Simone va fabriquer du fromage.");
        Carte carte = Carte.get();
        Graphe graphe = carte.getGraphe();
        ArrayList<Objet> listeObjets = GestionnaireDesObjets.getListeDesObjets();
        for(Objet obj : listeObjets) {
            if(obj.getType() == Machine_Fromage) {
                int positionMachine = Graphe.coordonnees2Sommet(obj.getCase().getLigne(), 
                        obj.getCase().getColonne());
                int dist = graphe.distanceDijkstra(ia.getSimone().getPositionCourante(), positionMachine+carte.taille());
                ia.deplacement(graphe.getParcours());
                ia.getListeAction().add(produireFromage);
                break;
            }
        }    
    }
    
}
