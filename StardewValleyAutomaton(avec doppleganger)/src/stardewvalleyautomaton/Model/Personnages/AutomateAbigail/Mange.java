/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stardewvalleyautomaton.Model.Personnages.AutomateAbigail;

import static stardewvalleyautomaton.IHM.Moment_Journée.Soir;
import stardewvalleyautomaton.IHM.TimerIAHandler;
import static stardewvalleyautomaton.Model.Personnages.IA.Enum_Action.mangerFromage;
import stardewvalleyautomaton.Model.Personnages.IA.IA_Abigail;

/**
 *
 * @author Geoffrey
 */
public class Mange extends Etat {

    public Mange(IA_Abigail ia) {
        super(ia);
    }

    @Override
    public Etat etatSuivant() {
        Etat etat;
        
        if(TimerIAHandler.getPeriode() == Soir)
            etat = new ReposMaison(ia);
        else if(ia.aSoif() && ia.peutTraire())
            etat = new Trait(ia);
        else if (ia.oeufPresent())
            etat = new CollecteOeuf(ia);
        else
            etat = new Attendre(ia);
        
        return etat;
    }

    @Override
    public void action() {
        System.out.println("Abigail mange.");
        ia.getListeAction().add(mangerFromage);
    }
    
}
