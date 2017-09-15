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
import static stardewvalleyautomaton.Model.Objets.Enum_Objet.Oeuf;
import stardewvalleyautomaton.Model.Objets.Objet;
import stardewvalleyautomaton.Model.Objets.Oeuf;
import static stardewvalleyautomaton.Model.Personnages.IA.Enum_Action.collecterOeuf;
import stardewvalleyautomaton.Model.Personnages.IA.GestionIAHumain;
import stardewvalleyautomaton.Model.Personnages.IA.IA;
import stardewvalleyautomaton.Model.Personnages.IA.IA_Abigail;

/**
 *
 * @author Geoffrey
 */
public class CollecteOeuf extends Etat {

    public CollecteOeuf(IA_Abigail ia) {
        super(ia);
    }

    @Override
    public Etat etatSuivant() {
        Etat etat;
        
        if(TimerIAHandler.getPeriode() == Soir)
            etat = new ReposMaison(ia);
        else if((ia.aSoif() && ia.peutTraire()) ||
                (ia.aFaim() && ia.peutTraire()))
            etat = new Trait(ia);
        else if(ia.oeufPresent())
            etat = new CollecteOeuf(ia);
        else
            etat = new Attendre(ia);
        
        return etat;
    }

    @Override
    public void action() {
        System.out.println("Abigail part collecter un oeuf.");
        ArrayList<Objet> listeObjets = GestionnaireDesObjets.getListeDesObjets();
        Carte carte = Carte.get();
        Graphe monGraphe = carte.getGraphe();
        ArrayList<Integer> parcours = new ArrayList();
        
        int distanceMini = monGraphe.getInfini();
        int distanceOeuf;
        int positionOeuf;
        boolean parcourir = false;
        Oeuf oeuf = null;
        
        for(Objet obj : listeObjets) {
            if(obj.getType() == Oeuf) {
                oeuf = (Oeuf)obj;
                positionOeuf = Graphe.coordonnees2Sommet(obj.getCase().getLigne(), 
                        obj.getCase().getColonne());
                distanceOeuf = monGraphe.distanceDijkstra(ia.getAbigail().getPositionCourante(), positionOeuf);
                if(distanceOeuf < distanceMini && !oeuf.getMarque()) { 
                    distanceMini = distanceOeuf;
                    parcourir = true;
                }
            }
        }
        
        if(parcourir) {
            int positionFinale = Graphe.coordonnees2Sommet(oeuf.getCase().getLigne(),
                    oeuf.getCase().getColonne());
            GestionIAHumain gestionIA = Carte.get().getGestionIAHumain();
            if(gestionIA.estPlusProche(ia.getAbigail(), positionFinale) ||
                    gestionIA.autreIAOccupee(ia.getAbigail())) {
                oeuf.setMarque(true);
                monGraphe.distanceDijkstra(ia.getAbigail().getPositionCourante(), positionFinale);
                parcours = monGraphe.getParcours();
                ia.deplacement(parcours);
                parcours.clear();
                ia.getListeAction().add(collecterOeuf);
            }
        }
    }
}
