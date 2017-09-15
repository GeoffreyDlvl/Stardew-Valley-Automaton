/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stardewvalleyautomaton.Model.Personnages.AutomateSimone;

import stardewvalleyautomaton.Model.Personnages.AutomateAbigail.*;
import static stardewvalleyautomaton.IHM.Moment_Journée.Soir;
import stardewvalleyautomaton.IHM.TimerIAHandler;
import static stardewvalleyautomaton.Model.Personnages.IA.Enum_Action.mangerFromage;
import stardewvalleyautomaton.Model.Personnages.IA.IA_Abigail;
import stardewvalleyautomaton.Model.Personnages.IA.IA_Simone;

/**
 *
 * @author Geoffrey
 */
public class MangeS extends EtatS {

    public MangeS(IA_Simone ia) {
        super(ia);
    }

    @Override
    public EtatS etatSuivant() {
        EtatS etat;
        
        if(TimerIAHandler.getPeriode() == Soir)
            etat = new ReposMaisonS(ia);
        else if(ia.aSoif() && ia.peutTraire())
            etat = new TraitS(ia);
        else if (ia.oeufPresent())
            etat = new CollecteOeufS(ia);
        else
            etat = new AttendreS(ia);
        
        return etat;
    }

    @Override
    public void action() {
        System.out.println("Simone mange.");
        ia.getListeAction().add(mangerFromage);
    }
    
}
