/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stardewvalleyautomaton.IHM;

/**
 *
 * @author mc668903
 * représente les différents moment de la journée
 */
public enum Moment_Journée {
    Matin("Matin"),
    Après_midi("Après-midi"),
    Soir("Soir");
    
    /**
     * attribut permettant de donné un String équivalent à la période
     * de la journée
     */
    private String nom = "";
    
    // défini le String de la période
    Moment_Journée(String s){
        this.nom = s;
    }
    
    /**
     * Méthode toString permettant d'afficher la période de la journée
     * @return le String de la période
     */
    @Override
    public String toString(){
        return this.nom;
    }
    
}
