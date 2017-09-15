/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stardewvalleyautomaton.Model.Personnages.Automate;

import static stardewvalleyautomaton.IHM.Moment_Journée.Soir;
import stardewvalleyautomaton.IHM.TimerIAHandler;
import static stardewvalleyautomaton.Model.Personnages.IA.Enum_Action.attendre;
import stardewvalleyautomaton.Model.Personnages.IA.IA_Abigail;

/**
 *
 * @author Geoffrey
 */
public class Attendre extends Etat {

    public Attendre(IA_Abigail ia) {
        super(ia);
    }

    @Override
    public Etat etatSuivant() {
        Etat etat;
        
        if(TimerIAHandler.getPeriode() == Soir)
            etat = new ReposMaison(ia);
        else if(ia.oeufPresent()) {
            etat = new CollecteOeuf(ia);
        } else
            etat = new Attendre(ia);
        
        return etat;
    }

    @Override
    public void action() {
        System.out.println("Abigail attends.");
        ia.getListeAction().add(attendre);
    }
    
    
}
