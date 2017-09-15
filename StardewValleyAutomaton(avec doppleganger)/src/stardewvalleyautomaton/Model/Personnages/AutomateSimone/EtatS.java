/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stardewvalleyautomaton.Model.Personnages.AutomateSimone;

import stardewvalleyautomaton.Model.Personnages.AutomateSimone.*;
import java.util.ArrayList;
import stardewvalleyautomaton.Model.Gestionnaires.GestionnaireDesPersonnages;
import stardewvalleyautomaton.Model.Personnages.Abigail;
import stardewvalleyautomaton.Model.Personnages.IA.IA;
import stardewvalleyautomaton.Model.Personnages.IA.IA_Simone;
import stardewvalleyautomaton.Model.Personnages.Personnage;

/**
 *
 * @author Geoffrey
 */
public abstract class EtatS {
    protected IA_Simone ia;
    
    public EtatS(IA_Simone ia) {
       this.ia = ia;
    }
    
    public abstract EtatS etatSuivant();
    
    public abstract void action();
}
