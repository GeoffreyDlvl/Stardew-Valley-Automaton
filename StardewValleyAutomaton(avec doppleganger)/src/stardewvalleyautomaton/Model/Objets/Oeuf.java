/*
 * Un oeuf
 */
package stardewvalleyautomaton.Model.Objets;

import stardewvalleyautomaton.Model.Cases.Case;

/**
 *
 * @author Matthieu
 */
public class Oeuf extends Objet {
    private boolean marque;
    
    public boolean getMarque() {
        return marque;
    }
    
    public void setMarque(boolean bool) {
        this.marque = bool;
    }
    
    public Oeuf(Case Case) {
        super(Case);
    }

    @Override
    public Enum_Objet getType() {
        return Enum_Objet.Oeuf;
    }
    
}
