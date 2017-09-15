/*
 * ia d'abigail
 */
package stardewvalleyautomaton.Model.Personnages.IA;

import stardewvalleyautomaton.Graphe.Graphe;
import java.util.ArrayList;
import java.util.Random;
import stardewvalleyautomaton.IHM.Moment_Journée;
import static stardewvalleyautomaton.IHM.Moment_Journée.Après_midi;
import static stardewvalleyautomaton.IHM.Moment_Journée.Soir;
import stardewvalleyautomaton.IHM.TimerIAHandler;
import stardewvalleyautomaton.Model.Carte;
import static stardewvalleyautomaton.Model.Cases.Enum_Case.*;
import stardewvalleyautomaton.Model.Gestionnaires.GestionnaireDesObjets;
import stardewvalleyautomaton.Model.Gestionnaires.GestionnaireDesPersonnages;
import stardewvalleyautomaton.Model.Objets.*;
import static stardewvalleyautomaton.Model.Objets.Enum_Objet.*;
import static stardewvalleyautomaton.Model.Personnages.IA.Enum_Action.*;
import stardewvalleyautomaton.Model.Personnages.*;
import stardewvalleyautomaton.Model.Personnages.Automate.*;
import static stardewvalleyautomaton.Model.Personnages.Enum_Personnage.*;

/**
 *
 * @author simonetma
 * @author Geoffrey Delval
 */
public class IA_Abigail extends IA {
    private ArrayList<Enum_Action> listeActions;
    private Abigail abigail;
    private Etat etat;
    
    public IA_Abigail() {
        super();
        listeActions = new ArrayList<Enum_Action>();
    }
    
    @Override
    protected void setActionValide() {
        this.addActionValide(attendre);
        this.addActionValide(moveLeft);
        this.addActionValide(moveRight);
        this.addActionValide(moveTop);
        this.addActionValide(moveBottom);
        this.addActionValide(traire);
        this.addActionValide(produireFromage);
        this.addActionValide(collecterOeuf);
        this.addActionValide(mangerFromage);
        this.addActionValide(boireLait);
    }
    
    //IA D'ABIGAIL (A IMPLEMENTER) ---------------------------------------------
    @Override
    public Enum_Action action() {
        if(this.abigail==null) {
            ArrayList<Personnage> perso = GestionnaireDesPersonnages.getListeDesPersonnages();
            Abigail abi = null;
            for(Personnage p : perso) {
                if(p.getType() == Abigail) {
                    abi = (Abigail)p;
                }
            }
            this.abigail = abi;
        }
        if(this.etat==null) {
            etat = new Attendre(this);
        }
        
        this.abigail.setPositionCourante(Graphe.coordonnees2Sommet(this.personnage().getCase().getLigne(), 
                this.personnage().getCase().getColonne()));
        Enum_Action resultat=attendre;
        
////////////////////////////////////////////////////////////////////////////////
        if(listeActions.isEmpty()) {
            etat.action();
            etat = etat.etatSuivant();
        }
        if(!this.listeActions.isEmpty()) {
            resultat = gestionBesoinsAction(this.listeActions.get(0));
            this.listeActions.remove(0);
        }
        return resultat;
    }
    
    //méthode qui ajoute à l'attribuy listeAction les prochains déplacement
    //en fonction du parcours passé en paramètre
    public void deplacement(ArrayList<Integer> parcours) {
        int positionAbigail = abigail.getPositionCourante();
        for(Integer i : parcours) {
            
           if(i==(positionAbigail+1)) {
               this.listeActions.add(moveRight);
               positionAbigail += 1;
           } else if(i==(positionAbigail-1)) {
               this.listeActions.add(moveLeft);
               positionAbigail -= 1;
           } else if(i==(positionAbigail-Carte.get().taille())) {
               this.listeActions.add(moveTop);
               positionAbigail -= Carte.get().taille();
           } else if(i==(positionAbigail+Carte.get().taille())) {
               this.listeActions.add(moveBottom);
               positionAbigail += Carte.get().taille();
           }   
        }
    }
    
    public Vache recupererVache() {
        ArrayList<Personnage> perso = GestionnaireDesPersonnages.getListeDesPersonnages();
        Vache v = null;
        for(Personnage p : perso) {
            if(p.getType() == Vache) {
                v = (Vache)p;
            }
        } 
        return v;
    }
    
    //méthode qui gère la gestion des besoin d'Abigail en fonction des prochaines
    //actions à effectuer
    public Enum_Action gestionBesoinsAction(Enum_Action action) {
        Enum_Action res = action;
        Moment_Journée periode = TimerIAHandler.getPeriode();
        int indiceJournee = 1;
        if(periode == Après_midi) { indiceJournee = 2; }
        int fatigue = abigail.getFatigue();
        int faim = abigail.getFaim();
        int soif = abigail.getSoif();
        boolean deplacement = 
                action == moveLeft ||
                action == moveRight ||
                action == moveTop ||
                action == moveBottom;
        if(deplacement) {
            if(fatigue<50) {
                fatigue = fatigue+(1*indiceJournee);
                soif++;
            } else {
                if(this.personnage().getCase().getType() == grass) {
                    if(fatigue+(3*indiceJournee) < 100) {
                        fatigue = fatigue+(3*indiceJournee);
                    } else {
                        res = attendre;
                        this.listeActions.add(0, res);
                    }
                } else if (this.personnage().getCase().getType() == lightgrass) {
                    if(fatigue+(2*indiceJournee) < 100) {
                        fatigue = fatigue+(2*indiceJournee);
                    } else {
                        res = attendre;
                        this.listeActions.add(0, res);
                    }
                } else if (this.personnage().getCase().getType() == dirt) {
                    if(fatigue+(1*indiceJournee) < 100) {
                        fatigue = fatigue+(1*indiceJournee);
                    } else {
                        res = attendre;
                        this.listeActions.add(0, res);
                    }
                }
                soif++;
            }
        }
        if(action == collecterOeuf) {
            faim = faim+5;
        }
        abigail.setFatigue(fatigue);
        abigail.setFaim(faim);
        abigail.setSoif(soif);
        return res;
    }
    
    //permet de récupérer l'objet Abigail
    public Abigail getAbigail() {
        return this.abigail;
    }
    
    //getteur de l'attribut listeAction
    public ArrayList<Enum_Action> getListeAction() {
        return this.listeActions;
    }
    
    //vrai si Abigail a soif, sinon faux
    public boolean aSoif() {
        return this.abigail.getSoif() > 70;
    }
    
    //vrai si Abigail a faim, sinon faux
    public boolean aFaim() {
        return this.abigail.getFaim() > 70;
    }
    
    //vrai si Abigail est fatiguée, sinon faux
    public boolean estFatigue() {
        return this.abigail.getFatigue() >= 100;
    }
    
    //vrai si la vache a du lait, sinon faux
    public boolean peutTraire() {
        return this.recupererVache().lait();
    }
    
    //vrai si Abigail transporte du lait mais pas de fromage, sinon faux
    public boolean peutFaireFromage() {
        return this.abigail.getLait() && !this.abigail.getFromage();
    }
    
    //vrai si un oeuf est présent sur la carte, sinon faux
    public boolean oeufPresent() {
        boolean res = false;
        
        ArrayList<Objet> listeObjets = GestionnaireDesObjets.getListeDesObjets();
        for(Objet o : listeObjets)
            if(o.getType() == Oeuf)
                res = true;
        
        return res;
    }
    
    //vrai si Abigail transporte du lait, sinon faux
    public boolean aDuLait() {
        return abigail.getLait();
    }
    //vrai si Abigail est sur une case adjacente à la maison, sinon faux
    public boolean estProcheMaison() {
        int ligne = this.personnage().getCase().getLigne();
        int colonne = this.personnage().getCase().getColonne();
        boolean res = false;
        
        if((Carte.get().getCase(ligne, colonne+1).getObjet() != null && 
                Carte.get().getCase(ligne, colonne+1).getObjet().getType() == Maison) ||
                (Carte.get().getCase(ligne, colonne-1).getObjet() != null && 
                Carte.get().getCase(ligne, colonne-1).getObjet().getType() == Maison) ||
                (Carte.get().getCase(ligne+1, colonne).getObjet() != null && 
                Carte.get().getCase(ligne+1, colonne).getObjet().getType() == Maison) ||
                (Carte.get().getCase(ligne-1, colonne).getObjet() != null && 
                Carte.get().getCase(ligne-1, colonne).getObjet().getType() == Maison))
            res = true;
        
        return res;
    }
}
