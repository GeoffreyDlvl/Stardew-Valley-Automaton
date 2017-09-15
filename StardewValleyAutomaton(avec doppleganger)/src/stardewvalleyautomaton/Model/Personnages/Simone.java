/*
 * Simone
 */
package stardewvalleyautomaton.Model.Personnages;

import java.util.ArrayList;
import stardewvalleyautomaton.Model.Carte;
import stardewvalleyautomaton.Model.Cases.Case;
import static stardewvalleyautomaton.Model.Objets.Enum_Objet.Machine_Fromage;
import static stardewvalleyautomaton.Model.Objets.Enum_Objet.Oeuf;
import static stardewvalleyautomaton.Model.Personnages.Enum_Personnage.Simone;
import static stardewvalleyautomaton.Model.Personnages.Enum_Personnage.Vache;
import stardewvalleyautomaton.Model.Personnages.IA.Enum_Action;
import static stardewvalleyautomaton.Model.Personnages.IA.Enum_Action.attendre;
import stardewvalleyautomaton.Model.Personnages.IA.IA;

/**
 *
 * @author Geoffrey Delval
 */
public class Simone extends Personnage {

    private boolean lait;                                                       //porte-t-elle du lait ?
    private boolean fromage;                                                    //porte-t-elle du fromage ?
    private int positionCourante;
    private int fatigue = 0;
    private int faim = 0;
    private int soif = 0;
    
    //CONSTRUCTEUR -------------------------------------------------------------
    public Simone(Case _saCase,IA _ia) {
        super(_saCase,_ia);
        this.lait = false;
        this.fromage = false;
    }

    @Override
    public Enum_Personnage getType() {
        return Simone;
    }
    
    //GETTER -------------------------------------------------------------------
    public boolean getLait() {
        return lait;
    }
    public boolean getFromage() {
        return fromage;
    }
    public int getFatigue() {
        return fatigue;
    }
    public int getFaim() {
        return faim;
    }
    public int getSoif() {
        return soif;
    }
    public int getPositionCourante() {
        return positionCourante;
    }
    
    //SETTER--------------------------------------------------------------------
    public void setFatigue(int fatigue) {
        this.fatigue=fatigue;
    }
    public void setFaim(int faim) {
        this.faim=faim;
    }
    public void setSoif(int soif) {
        this.soif=soif;
    }
    public void setPositionCourante(int position) {
        this.positionCourante = position;
    }
    
    //ACTIONS POSSIBLES --------------------------------------------------------
    //traire les vaches voisines
    public void traire() {
        //récupère les vaches autour d'elle
        ArrayList<Personnage> listeDesVaches = new ArrayList<>();
        int ligne = this.getCase().getLigne();
        int colonne = this.getCase().getColonne();
        if(Carte.get().getPersonnage(ligne-1, colonne) != null) {
            if(Carte.get().getPersonnage(ligne-1, colonne).getType() == Vache) {
                listeDesVaches.add(Carte.get().getPersonnage(ligne-1, colonne));
            }
        }
        if(Carte.get().getPersonnage(ligne+1, colonne) != null) {
            if(Carte.get().getPersonnage(ligne+1, colonne).getType() == Vache) {
                listeDesVaches.add(Carte.get().getPersonnage(ligne+1, colonne));
            }
        }
        if(Carte.get().getPersonnage(ligne, colonne-1) != null) {
            if(Carte.get().getPersonnage(ligne, colonne-1).getType() == Vache) {
                listeDesVaches.add(Carte.get().getPersonnage(ligne, colonne-1));
            }
        }
        if(Carte.get().getPersonnage(ligne, colonne+1) != null) {
            if(Carte.get().getPersonnage(ligne, colonne+1).getType() == Vache) {
                listeDesVaches.add(Carte.get().getPersonnage(ligne, colonne+1));
            }
        }
        
        //s'il n'y a pas de vache autour d'Simone
        if(listeDesVaches.isEmpty()) {
            System.err.println("Il n'y a pas de vache ici !");
        }
        
        //traite des vaches voisines d'Simone
        for(Personnage vache : listeDesVaches) {
            if(lait) {
                System.out.println("Simone a déjà du lait sur elle");
            }
            else if(((Vache) vache).lait()) {
                ((Vache) vache).traire();
                System.out.println("Simone a traie la vache");
                this.lait = true;
            }
            else {
                System.out.println("La vache n'a pas de lait");
            }
        }
    }
    
    //produire du fromage
    public void produireFromage() {
        //repère si Simone est à côté d'une machine
        boolean presenceMachine = false;
        int ligne = this.getCase().getLigne();
        int colonne = this.getCase().getColonne();
        if(Carte.get().getObjet(ligne-1, colonne) != null) {
            if(Carte.get().getObjet(ligne-1, colonne).getType() == Machine_Fromage) {
                presenceMachine = true;
            }
        }
        if(Carte.get().getObjet(ligne+1, colonne) != null) {
            if(Carte.get().getObjet(ligne+1, colonne).getType() == Machine_Fromage) {
                presenceMachine = true;
            }
        }
        if(Carte.get().getObjet(ligne, colonne-1) != null) {
            if(Carte.get().getObjet(ligne, colonne-1).getType() == Machine_Fromage) {
                presenceMachine = true;
            }
        }
        if(Carte.get().getObjet(ligne, colonne+1) != null) {
            if(Carte.get().getObjet(ligne, colonne+1).getType() == Machine_Fromage) {
                presenceMachine = true;
            }
        }
        
        //Simone doit avoir du lait, pas de fromage et doit être proche d'une machine
        if(presenceMachine && this.lait && !this.fromage) {
            this.lait = false;
            this.fromage = true;
            System.out.println("Simone a fait du fromage");
        }
        else {
            if(this.fromage) {
                System.err.println("Simone a déjà du fromage sur elle !");
            }
            if(!this.lait) {
                System.err.println("Simone n'a pas de lait !");
            }
            if(!presenceMachine) {
                System.err.println("Simone n'est pas à côté de la machine !");
            }
        }
    }

    //Collecter un oeuf présent sur la case
    public void collecterOeuf() {
        boolean success = false;
        if(this.getCase().getObjet() != null) {
            if(this.getCase().getObjet().getType() == Oeuf) {
                this.getCase().removeObjet();
                success = true;
            }
        }
        
        if(success) {
            System.out.println("Simone a ramassé un oeuf");
        }
        else {
            System.err.println("Il n'y a pas d'oeuf ici !");
        }
    }
    
    public void boireLait() {
        if(this.lait) {
            this.lait = false;
            this.soif = 0;
            System.out.println("Simone boit du lait");
        } else {
            System.out.println("Simone a n'a pas de lait !");
        }
    }
    
    public void mangerFromage() {
        if(this.fromage) {
            this.fromage = false;
            this.faim = 0;
            System.out.println("Simone mange un fromage");
        } else {
            System.out.println("Simone a n'a pas de fromage !");
        }        
    }
    
    @Override
    public void actionSpeciale(Enum_Action action) {
        switch(action) {
            case traire: traire(); break;
            case produireFromage: produireFromage(); break;
            case collecterOeuf: collecterOeuf(); break;
            case boireLait: boireLait(); break;
            case mangerFromage: mangerFromage(); break;
        }
    }
    
    @Override
    public void action(Enum_Action action) {
        super.action(action);
        if(action==attendre) {
            System.out.println("Simone se sent plus reposée.");
            fatigue = fatigue - 50;
            if(fatigue < 0) {
                fatigue = 0;
            }
        }
    }
}
