/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stardewvalleyautomaton.Model.Personnages.AutomateSimone;

import static stardewvalleyautomaton.IHM.Moment_Journée.Soir;
import stardewvalleyautomaton.IHM.TimerIAHandler;
import static stardewvalleyautomaton.Model.Personnages.IA.Enum_Action.attendre;
import stardewvalleyautomaton.Model.Personnages.IA.IA_Simone;

/**
 *
 * @author Geoffrey
 */
public class AttendreS extends EtatS {

    public AttendreS(IA_Simone ia) {
        super(ia);
    }

    @Override
    public EtatS etatSuivant() {
        EtatS etat;
        
        if(TimerIAHandler.getPeriode() == Soir)
            etat = new ReposMaisonS(ia);
        else if(ia.oeufPresent()) {
            etat = new CollecteOeufS(ia);
        } else
            etat = new AttendreS(ia);
        
        return etat;
    }

    @Override
    public void action() {
        System.out.println("Simone attend et se repose.");
        ia.getListeAction().add(attendre);
    }
    
    
}
