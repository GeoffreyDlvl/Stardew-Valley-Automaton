/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stardewvalleyautomaton.Model.Personnages.IA;

import java.util.ArrayList;
import stardewvalleyautomaton.Graphe.Graphe;
import stardewvalleyautomaton.Model.Carte;
import stardewvalleyautomaton.Model.Gestionnaires.GestionnaireDesPersonnages;
import stardewvalleyautomaton.Model.Personnages.*;
import static stardewvalleyautomaton.Model.Personnages.Enum_Personnage.*;

/**
 *
 * @author Geoffrey
 */
public class GestionIAHumain {
    private IA_Abigail abigail;
    private IA_Simone simone;
    
    public void setAbigail(IA_Abigail abigail) {
        this.abigail = abigail;
    }
    
    public void setSimone(IA_Simone simone) {
        this.simone = simone;
    }
    
    public boolean estPlusProche(Personnage p, int positionOeuf) {
        boolean res = false;
        if(p.getType() == Abigail) {
            int distanceAbigail = Carte.get().getGraphe().distanceDijkstra(abigail.getAbigail().getPositionCourante(), 
                    positionOeuf);
            int distanceSimone = Carte.get().getGraphe().distanceDijkstra(simone.getSimone().getPositionCourante(), 
                    positionOeuf); 
            if(distanceAbigail <= distanceSimone)
                res = true;
        }
        if(p.getType() == Simone) {
            int distanceAbigail = Carte.get().getGraphe().distanceDijkstra(abigail.getAbigail().getPositionCourante(), 
                    positionOeuf);
            int distanceSimone = Carte.get().getGraphe().distanceDijkstra(simone.getSimone().getPositionCourante(), 
                    positionOeuf);
            if(distanceAbigail > distanceSimone)
                res = true;
        }
        
        return res;
    }
    
    public boolean autreIAOccupee(Personnage p) {
        boolean res = false;
        if(p.getType() == Abigail) {
            res = !simone.getListeAction().isEmpty();
        }
        if(p.getType() == Simone) {
            res = !abigail.getListeAction().isEmpty();
        }
        
        return res;
    }
}
