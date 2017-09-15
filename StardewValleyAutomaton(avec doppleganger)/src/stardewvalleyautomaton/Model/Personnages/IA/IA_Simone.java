/*
 * ia d'simone
 */
package stardewvalleyautomaton.Model.Personnages.IA;

import stardewvalleyautomaton.Model.Personnages.AutomateSimone.AttendreS;
import stardewvalleyautomaton.Model.Personnages.AutomateSimone.EtatS;
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
import static stardewvalleyautomaton.Model.Personnages.Enum_Personnage.*;

/**
 *
 * @author Geoffrey Delval
 */
public class IA_Simone extends IA {
    protected ArrayList<Enum_Action> listeActions;
    protected Simone simone;
    protected EtatS etat;
    
    public IA_Simone() {
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
        if(this.simone==null) {
            ArrayList<Personnage> perso = GestionnaireDesPersonnages.getListeDesPersonnages();
            Simone sim = null;
            for(Personnage p : perso) {
                if(p.getType() == Simone) {
                    sim = (Simone)p;
                }
            }
            this.simone = sim;
        }
        if(this.etat==null) {
            etat = new AttendreS(this);
        }
        
        this.simone.setPositionCourante(Graphe.coordonnees2Sommet(this.personnage().getCase().getLigne(), 
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
        int positionSimone = simone.getPositionCourante();
        for(Integer i : parcours) {
            
           if(i==(positionSimone+1)) {
               this.listeActions.add(moveRight);
               positionSimone += 1;
           } else if(i==(positionSimone-1)) {
               this.listeActions.add(moveLeft);
               positionSimone -= 1;
           } else if(i==(positionSimone-Carte.get().taille())) {
               this.listeActions.add(moveTop);
               positionSimone -= Carte.get().taille();
           } else if(i==(positionSimone+Carte.get().taille())) {
               this.listeActions.add(moveBottom);
               positionSimone += Carte.get().taille();
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
    
    //méthode qui gère la gestion des besoin d'Simone en fonction des prochaines
    //actions à effectuer
    public Enum_Action gestionBesoinsAction(Enum_Action action) {
        Enum_Action res = action;
        Moment_Journée periode = TimerIAHandler.getPeriode();
        int indiceJournee = 1;
        if(periode == Après_midi) { indiceJournee = 2; }
        int fatigue = simone.getFatigue();
        int faim = simone.getFaim();
        int soif = simone.getSoif();
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
        simone.setFatigue(fatigue);
        simone.setFaim(faim);
        simone.setSoif(soif);
        return res;
    }
    
    //permet de récupérer l'objet Simone
    public Simone getSimone() {
        return this.simone;
    }
    
    //getteur de l'attribut listeAction
    public ArrayList<Enum_Action> getListeAction() {
        return this.listeActions;
    }
    
    //vrai si Simone a soif, sinon faux
    public boolean aSoif() {
        return this.simone.getSoif() > 70;
    }
    
    //vrai si Simone a faim, sinon faux
    public boolean aFaim() {
        return this.simone.getFaim() > 70;
    }
    
    //vrai si Simone est fatiguée, sinon faux
    public boolean estFatigue() {
        return this.simone.getFatigue() >= 100;
    }
    
    //vrai si la vache a du lait, sinon faux
    public boolean peutTraire() {
        return this.recupererVache().lait();
    }
    
    //vrai si Simone transporte du lait mais pas de fromage, sinon faux
    public boolean peutFaireFromage() {
        return this.simone.getLait() && !this.simone.getFromage();
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
    
    //vrai si Simone transporte du lait, sinon faux
    public boolean aDuLait() {
        return simone.getLait();
    }
    //vrai si Simone est sur une case adjacente à la maison, sinon faux
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
    
    public int distance(Objet obj) {
        return Carte.get().getGraphe().distanceDijkstra(this.simone.getPositionCourante(), 
                Graphe.coordonnees2Sommet(obj.getCase().getLigne(), obj.getCase().getColonne()));
    }
}
