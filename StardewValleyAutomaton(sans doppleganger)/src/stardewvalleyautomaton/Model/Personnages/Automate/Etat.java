/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stardewvalleyautomaton.Model.Personnages.Automate;

import java.util.ArrayList;
import stardewvalleyautomaton.Model.Gestionnaires.GestionnaireDesPersonnages;
import stardewvalleyautomaton.Model.Personnages.Abigail;
import stardewvalleyautomaton.Model.Personnages.IA.IA;
import stardewvalleyautomaton.Model.Personnages.IA.IA_Abigail;
import stardewvalleyautomaton.Model.Personnages.Personnage;

/**
 *
 * @author Geoffrey
 */
public abstract class Etat {
    protected IA_Abigail ia;
    
    public Etat(IA_Abigail ia) {
       this.ia = ia;
    }
    
    public abstract Etat etatSuivant();
    
    public abstract void action();
}
