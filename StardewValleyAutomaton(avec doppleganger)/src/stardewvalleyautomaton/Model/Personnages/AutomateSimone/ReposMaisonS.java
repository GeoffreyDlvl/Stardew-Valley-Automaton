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
import stardewvalleyautomaton.Model.Gestionnaires.GestionnaireDesObjets;
import static stardewvalleyautomaton.Model.Objets.Enum_Objet.Maison;
import stardewvalleyautomaton.Model.Objets.Objet;
import static stardewvalleyautomaton.Model.Personnages.IA.Enum_Action.attendre;
import stardewvalleyautomaton.Model.Personnages.IA.IA_Abigail;
import stardewvalleyautomaton.Model.Personnages.IA.IA_Simone;

/**
 *
 * @author Geoffrey
 */
public class ReposMaisonS extends EtatS {

    public ReposMaisonS(IA_Simone ia) {
        super(ia);
    }

    @Override
    public EtatS etatSuivant() {
        EtatS etat;
        
        if(TimerIAHandler.getPeriode() == Soir)
            etat = new ReposMaisonS(ia);
        else if(ia.aSoif() && ia.getSimone().getLait())
            etat = new BoitS(ia);
        else if(ia.aSoif() && ia.peutTraire())
            etat = new TraitS(ia);
        else if(ia.aFaim() && ia.getSimone().getFromage())
            etat = new MangeS(ia);
        else if(ia.aFaim() && ia.getSimone().getLait())
            etat = new FabriqueFromageS(ia);
        else if(ia.aFaim() && ia.peutTraire())
            etat = new TraitS(ia);
        else if(ia.oeufPresent())
            etat = new CollecteOeufS(ia);
        else
            etat = new AttendreS(ia);
        
        return etat;
    }

    @Override
    public void action() {
        if(ia.estProcheMaison()) {
            ia.getListeAction().add(attendre);
        } else {
            System.out.println("Simone rentre chez elle.");
            ia.getListeAction().clear();
            Carte carte = Carte.get();
            Graphe graphe = carte.getGraphe();
            int posAbigail = ia.getSimone().getPositionCourante();
            ArrayList<Objet> listeObjets = GestionnaireDesObjets.getListeDesObjets();
            ArrayList<Integer> parcours = new ArrayList<Integer>();
            int distanceMin = graphe.getInfini();
            int posDistanceMin=0;
            int distance;
            for(Objet obj : listeObjets) {
                if(obj.getType() == Maison) {
                    int ligne = obj.getCase().getLigne();
                    int colonne = obj.getCase().getColonne();
                    if(carte.getCase(ligne-1, colonne).estLibre()) {
                        distance = graphe.distanceDijkstra(posAbigail, graphe.coordonnees2Sommet(ligne-1, colonne));
                        if(distance < distanceMin) { 
                            distanceMin = distance; 
                            posDistanceMin = graphe.coordonnees2Sommet(ligne-1, colonne);
                        }
                    }
                    if(carte.getCase(ligne+1, colonne).estLibre()) {
                        distance = graphe.distanceDijkstra(posAbigail, graphe.coordonnees2Sommet(ligne+1, colonne));
                        if(distance < distanceMin) { 
                            distanceMin = distance;
                            posDistanceMin = graphe.coordonnees2Sommet(ligne+1, colonne);
                        }
                    }
                    if(carte.getCase(ligne, colonne-1).estLibre()) {
                        distance = graphe.distanceDijkstra(posAbigail, graphe.coordonnees2Sommet(ligne, colonne-1));
                        if(distance < distanceMin) { 
                            distanceMin = distance;
                            posDistanceMin = graphe.coordonnees2Sommet(ligne, colonne-1); 
                        }
                    }
                    if(carte.getCase(ligne, colonne+1).estLibre()) {
                        distance = graphe.distanceDijkstra(posAbigail, graphe.coordonnees2Sommet(ligne, colonne+1));
                        if(distance < distanceMin) { 
                            distanceMin = distance; 
                            posDistanceMin = graphe.coordonnees2Sommet(ligne, colonne+1);
                        }
                    }                
                }
            }
            graphe.distanceDijkstra(posAbigail, posDistanceMin);
            parcours = graphe.getParcours();
            ia.deplacement(parcours);
        }
    }
    
    
}
