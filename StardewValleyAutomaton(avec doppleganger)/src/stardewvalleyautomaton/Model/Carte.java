/*
 * Carte du jeu
 */
package stardewvalleyautomaton.Model;

import stardewvalleyautomaton.Graphe.Graphe;
import java.util.ArrayList;
import stardewvalleyautomaton.Model.Cases.Case;
import stardewvalleyautomaton.Model.Cases.Case_Dirt;
import stardewvalleyautomaton.Model.Cases.Case_Grass;
import stardewvalleyautomaton.Model.Cases.Case_LightGrass;
import stardewvalleyautomaton.Model.Gestionnaires.GestionnaireDesIA;
import stardewvalleyautomaton.Model.Gestionnaires.GestionnaireDesPersonnages;
import stardewvalleyautomaton.Model.Objets.Enum_Objet;
import static stardewvalleyautomaton.Model.Objets.Enum_Objet.*;
import stardewvalleyautomaton.Model.Objets.Objet;
import stardewvalleyautomaton.Model.Personnages.Abigail;
import stardewvalleyautomaton.Model.Personnages.Enum_Personnage;
import static stardewvalleyautomaton.Model.Personnages.Enum_Personnage.*;
import stardewvalleyautomaton.Model.Personnages.IA.*;
import stardewvalleyautomaton.Model.Personnages.Personnage;
import stardewvalleyautomaton.Model.Personnages.Poule;
import stardewvalleyautomaton.Model.Personnages.Simone;
import stardewvalleyautomaton.Model.Personnages.Vache;

/**
 *
 * @author simonetma
 * @author Geoffrey Delval
 */
public final class Carte {

    //DESIGN PATTERN DU SINGLETON ----------------------------------------------
    private static Carte instance;
    private static Graphe graphe;

    public static Carte get() {
        if (instance == null) {
            instance = new Carte();
        }
        return instance;

    }

    private ArrayList<Case> listeCase;                                          //liste des cartes de la carte
    private int taille = 30;                                                    //taille de la carte
    private GestionIAHumain gestionIA;
    
    //CONSTRUCTEUR -------------------------------------------------------------
    private Carte() {
        //construction du fond de la carte
        this.listeCase = new ArrayList<>();
        String map
                = "222222233333222222222222222222"
                + "222222223333332222222222222222"
                + "222222222333333222222222222222"
                + "222222222333333322222222222222"
                + "222222222223333332222222222222"
                + "111222222222333333222222222222"
                + "111112222222223333222222222222"
                + "111111222222222333222222222222"
                + "111111122222222333333333333333"
                + "111111112222222333333333333333"
                + "111111111222222233333333333333"
                + "111111111111222222222222222222"
                + "111111111111122222222222222222"
                + "111111111111122222222222222222"
                + "111111111111122222222222222222"
                + "111111111111222222222222222222"
                + "111111111111222222222222222222"
                + "111111111112222222222222222222"
                + "111111112222222222222222222222"
                + "111122222222222222222222222222"
                + "222222222222222222222222222222"
                + "222222222222222222222222222222"
                + "222222222222222222222222221111"
                + "222222222222222222222221111111"
                + "222222222222222222222111111111"
                + "222222222222222222211111111111"
                + "222222222222222222111111111111"
                + "222222222222222222111111111111"
                + "222222222222222221111111111111"
                + "222222222222222221111111111111";

        //création des Cases
        for (int i = 0; i < this.taille; i++) {
            for (int j = 0; j < this.taille; j++) {
                switch (map.charAt(i * this.taille + j)) {
                    case '1':
                        this.listeCase.add(new Case_Grass(i, j));
                        break;
                    case '2':
                        this.listeCase.add(new Case_LightGrass(i, j));
                        break;
                    case '3':
                        this.listeCase.add(new Case_Dirt(i, j));
                        break;
                    default:
                        break;
                }
            }

        }

        //ajout des arbres
        this.getCase(24, 27).setObjet(Arbre);
        this.getCase(28, 28).setObjet(Arbre);
        this.getCase(28, 21).setObjet(Arbre);
        this.getCase(26, 24).setObjet(Arbre);
        this.getCase(22, 22).setObjet(Arbre);
        this.getCase(19, 26).setObjet(Arbre);
        this.getCase(17, 23).setObjet(Arbre);
        this.getCase(16, 28).setObjet(Arbre);
        this.getCase(19, 19).setObjet(Arbre);
        this.getCase(25, 19).setObjet(Arbre);

        //ajout de l'appareil à fromage
        this.getCase(7, 21).setObjet(Machine_Fromage);
       
        //création du poulaillé
        for (int i = 12; i < 27; i++) {
            for (int j = 2; j < 15; j++) {
                if (j == 2 || j == 14 || i == 12 || i == 26) {
                    if ((i != 12 || j != 8) && (i != 26 || j != 8) && (i != 19 || j != 2) && (i != 19 || j != 14)) {
                        this.getCase(i, j).setObjet(Barriere);                      
                    }
                }
            }
        }

        //gestion de la maison
        for (int i = 5; i < 8; i++) {
            for (int j = 22; j < 29; j++) {
                this.getCase(i, j).setObjet(Maison);
            }
        }

        //ajout des personnage
        this.createPersonnage(Poule, new IA_Poule(), 13, 5);
        this.createPersonnage(Poule, new IA_Poule(), 17, 7);
        this.createPersonnage(Poule, new IA_Poule(), 22, 3);
        this.createPersonnage(Poule, new IA_Poule(), 15, 9);
        this.createPersonnage(Poule, new IA_Poule(), 23, 10);
        this.createPersonnage(Poule, new IA_Poule(), 18, 13);

        IA_Abigail iaAbigail = new IA_Abigail();
        IA_Simone iaSimone = new IA_Simone();
        this.createPersonnage(Abigail, iaAbigail, 8, 25);
        this.createPersonnage(Simone, iaSimone, 8, 29);

        this.createPersonnage(Vache, new IA_Vache(), 5, 5);
        this.initGraphe();

        //ajout de la gestion des ia
        gestionIA = new GestionIAHumain();
        gestionIA.setAbigail(iaAbigail);
        gestionIA.setSimone(iaSimone);
        
    }

    //GETTER -------------------------------------------------------------------
    //taille de la carte
    public int taille() {
        return taille;
    }

    //renvoie la case présente à ces coordonnées
    public Case getCase(int i, int j) {
        return this.listeCase.get(i * taille + j);
    }
    
    public GestionIAHumain getGestionIAHumain() {
        return this.gestionIA;
    }

    /**
     * renvoie le numéro de sommet du graphe en fonction des coordonnées d'une
     * case.
     * @param i ligne
     * @param j colonne
     * @return numéro de sommet
     */
    public int coordonnees2Sommet(int i, int j) {
        return (i * taille + j + 1);
    }

    //renvoie le personnage présent à ces coordonnées
    public Personnage getPersonnage(int i, int j) {
        Personnage personnage = null;
        if (i >= 0 && j >= 0 && i < taille && j < taille) {
            personnage = this.getCase(i, j).getPersonnage();
        }
        return personnage;
    }

    //renvoie l'objet présent à ces coordonnées
    public Objet getObjet(int i, int j) {
        Objet objet = null;
        if (i >= 0 && j >= 0 && i < taille && j < taille) {
            objet = this.getCase(i, j).getObjet();
        }
        return objet;
    }
    
    public Graphe getGraphe() {
        return this.graphe;
    }

    //GESTION DES PERSONNAGES --------------------------------------------------
    //ajoute d'un personnage
    private void createPersonnage(Enum_Personnage type, IA ia, int ligne, int colonne) {
        //on récupère la case
        Case laCase = this.getCase(ligne, colonne);

        //on crée le personnage (son IA est enregistrée à la création)
        Personnage personnage = null;
        switch (type) {
            case Poule:
                personnage = new Poule(laCase, ia);
                break;
            case Abigail:
                personnage = new Abigail(laCase, ia);
                break;
            case Simone:
                personnage = new Simone(laCase, ia);
                break;
            case Vache:
                personnage = new Vache(laCase, ia);
                break;
        }
        //on l'enregistre dans le gestionnaire des personnages
        GestionnaireDesPersonnages.addPersonnage(personnage);

        //on enregistre son IA dans le gestionnaire des IA
        GestionnaireDesIA.addIA(ia);

        //on le place sur la ou les cases
        laCase.setPersonnage(personnage);
        if (type == Vache && colonne + 1 < this.taille) {
            this.getCase(ligne, colonne + 1).setPersonnage(personnage);
        }
        //on indique au personnage sa case
        personnage.setCase(laCase);
    }

    //déplacement d'un personnage
    public final void setPositionPersonnage(Personnage p, int ligne, int colonne) {
        //enlève de la case précédent
        p.getCase().setPersonnage(null);
        //gestion de l'ancienne case de droite pour la vache
        if (p.getType() == Vache && p.getCase().getColonne() + 1 < this.taille) {
            this.getCase(p.getCase().getLigne(), p.getCase().getColonne() + 1).setPersonnage(null);
        }

        //place dans la case d'arrivée
        this.getCase(ligne, colonne).setPersonnage(p);
        p.setCase(this.getCase(ligne, colonne));
        //gestion de la nouvelle case de droite pour la vache
        if (p.getType() == Vache && p.getCase().getColonne() + 1 < this.taille) {
            this.getCase(p.getCase().getLigne(), p.getCase().getColonne() + 1).setPersonnage(p);
        }
    }
    
    /**
     * création du graphe correspondant à la carte
     */
    public void initGraphe() {
        graphe = new Graphe(taille*taille);
        this.graphe.construireGrille(taille);
        for(Case c : listeCase) {
            if(c.getObjet() != null && c.getObjet().getType() != Oeuf) {
                this.graphe.obstacle(c.getLigne(), c.getColonne(), this.taille);
            }
        }
    
        this.getGraphe().afficheGraphe(taille);
    }

    public ArrayList<Case> getListCase() {
        return this.listeCase;
    }
}
