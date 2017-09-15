/*
 * Timer gérant les IAs et l'IHM
 */
package stardewvalleyautomaton.IHM;

import java.util.ArrayList;
import javafx.event.Event;
import javafx.event.EventHandler;
import static stardewvalleyautomaton.IHM.Moment_Journée.*;
import stardewvalleyautomaton.IHM.Scenes.Scene_Principale;
import stardewvalleyautomaton.IHM.Sprites.Personnages.IHM_Personnage;
import stardewvalleyautomaton.Model.Gestionnaires.GestionnaireDesIA;
import stardewvalleyautomaton.Model.Gestionnaires.GestionnaireDesObjets;
import stardewvalleyautomaton.Model.Gestionnaires.GestionnaireDesPersonnages;
import stardewvalleyautomaton.Model.Objets.Objet;
import stardewvalleyautomaton.Model.Personnages.IA.IA;
import stardewvalleyautomaton.Model.Personnages.Personnage;


/**
 *
 * @author simonetma
 * @author Maxime Charlet
 */
public class TimerIAHandler implements EventHandler {

    private Scene_Principale scene;                                             //scene principale
    
    // attribut compteur qui sert pour s'incrémenter
    private int compteur = 0;
    
    // attribut heure permettant de simuler les heures de la journée
    private int heure = 5; //la journée d'Abigail commence le matin à 5h
    
    // attribut permettant de savoir à quel moment on en est dans la journée
    private static Moment_Journée periodeJournée = Matin;
    
    // attribut utile pour le test du changement de période
    private Moment_Journée test = periodeJournée;
    
    //constructeur
    public TimerIAHandler(Scene_Principale _scene) {
        this.scene = _scene;
    }
    
    /**
    * Méthode permettant de récupéré le moment de la journée
    * @return periodeJournée le moment de la journée
    */
    public static Moment_Journée getPeriode(){
        return periodeJournée;
    }
    
    @Override
    //méthode appelée régulièrement
    public void handle(Event event) {
        //gestion des objets
        this.gestionDesObjets();
        
        //gestion des personnages
        this.gestionDesPersonnages();
        
        //gestion des IAs
        this.gestionDesIAs();
        
        // incrémentation du compteur
        compteur+=1;
        
        // si le compteur est divisible par 50 alors une heure vient de s'écouler
        if (compteur%100 == 0){
            heure +=1;
        }
        
        // si l'heure est égale à 24 alors on l'initialise à 0
        if (heure == 24){
            heure = 0;
        }
        
        // on passe à la période du Matin quand l'heure est entre 5 et 12 heure inclus
        if ((heure >= 5) && (heure <= 12)){
            periodeJournée = Matin;
        }
        
        // on passe à la période de l'Après-Midi quand l'heure est entre 13 et 20 heure inclus
        if ((heure >= 13) && (heure <= 20)){
            periodeJournée = Après_midi;
        }
        
        // on passe à la période du Soir quand l'heure est entre 21 et 4 heure inclus
        if ((heure >= 21) || (heure <= 4)){
            periodeJournée = Soir;
        }
        
        // affichage de test
        //System.out.println("Compteur : "+compteur);
        //System.out.println("Heure de la journée : "+heure);
        // test du changement de moment de la journée
        if (test != periodeJournée){
            System.out.println("La période de la journée est : "+periodeJournée.toString());
            test = periodeJournée;
        }
        
    }
    
    //gestion des objets
    private void gestionDesObjets() {
        //gestion des objets à rajouter
        ArrayList<Objet> aAjouter = new ArrayList<>(GestionnaireDesObjets.getListeDesNouveauxObjets());
        ArrayList<Objet> aSupprimer = new ArrayList<>(GestionnaireDesObjets.getListeDesAnciensObjets());
        for(Objet objet : aAjouter) {
            //ajouter l'objet à l'IHM
            scene.ajouterObjet(objet);
            //Valider l'objet
            GestionnaireDesObjets.valider(objet);
        }
        //suppression des objets
        for(Objet objet : aSupprimer) {
            //supprimer l'objet de l'IHM
            scene.supprimerObjet(objet);
            //Devalider l'objet
            GestionnaireDesObjets.devalider(objet);
        }
    }
    
    //gestion des Personnages
    private void gestionDesPersonnages() {
        //gestion des personnages à rajouter
        ArrayList<Personnage> aModifier = new ArrayList<>(GestionnaireDesPersonnages.getListeDesNouveauxPersonnages());
        for(Personnage personnage : aModifier) {
            //ajouter le personnage à l'IHM
            scene.ajouterPersonnage(personnage);
            //Valider le personnage
            GestionnaireDesPersonnages.valider(personnage);
        }
    }
    
    //gestion des IAs et des sprites personnages (pour les mouvements)
    private void gestionDesIAs() {
        //liste des IAs et des sprites personnages
        ArrayList<IA> listeDesIA = GestionnaireDesIA.getListeDesIA();
        ArrayList<IHM_Personnage> listeDesPersonnages = this.scene.getListeDesPersonnages();
        
        //actionne les IAs
        for(IA ia : listeDesIA) {
            ia.activerParTimer();
        }
        
        //actionne le mouvement des sprites
        for(IHM_Personnage perso : listeDesPersonnages) {
            perso.action();
        }

    }
    
}
