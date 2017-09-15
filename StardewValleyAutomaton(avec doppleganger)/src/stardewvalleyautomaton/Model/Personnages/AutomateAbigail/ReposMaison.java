/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stardewvalleyautomaton.Model.Personnages.AutomateAbigail;

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

/**
 *
 * @author Geoffrey
 */
public class ReposMaison extends Etat {

    public ReposMaison(IA_Abigail ia) {
        super(ia);
    }

    @Override
    public Etat etatSuivant() {
        Etat etat;
        
        if(TimerIAHandler.getPeriode() == Soir)
            etat = new ReposMaison(ia);
        else if(ia.aSoif() && ia.getAbigail().getLait())
            etat = new Boit(ia);
        else if(ia.aSoif() && ia.peutTraire())
            etat = new Trait(ia);
        else if(ia.aFaim() && ia.getAbigail().getFromage())
            etat = new Mange(ia);
        else if(ia.aFaim() && ia.getAbigail().getLait())
            etat = new FabriqueFromage(ia);
        else if(ia.aFaim() && ia.peutTraire())
            etat = new Trait(ia);
        else if(ia.oeufPresent())
            etat = new CollecteOeuf(ia);
        else
            etat = new Attendre(ia);
        
        return etat;
    }

    @Override
    public void action() {
        if(ia.estProcheMaison()) {
            ia.getListeAction().add(attendre);
        } else {
            System.out.println("Abigail rentre chez elle.");
            ia.getListeAction().clear();
            Carte carte = Carte.get();
            Graphe graphe = carte.getGraphe();
            int posAbigail = ia.getAbigail().getPositionCourante();
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
