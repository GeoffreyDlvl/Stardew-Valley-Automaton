/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stardewvalleyautomaton.Graphe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import stardewvalleyautomaton.Model.Carte;
import static stardewvalleyautomaton.Model.Objets.Enum_Objet.Oeuf;
import stardewvalleyautomaton.Model.Objets.Objet;

/**
 *
 * @author simonetma
 * @author Geoffrey Delval
 */
public class Graphe {
    
    //Attributs
    private HashMap<Couple,Integer> matriceAdjacence;                           //un graphe est défini par sa matrice d'adjacence
    private int nombreDeSommets;                                                //nombre de sommet du graphe
    
    private boolean mark[];
    private int distance[];
    private int predecesseur[];
    private int infini;

    private ArrayList<Integer> parcours;
    
    //Constructeur du graphe
    public Graphe(int _nombreDeSommets) {
        this.nombreDeSommets = _nombreDeSommets;
        this.matriceAdjacence = new HashMap<>();
        this.mark = new boolean[this.nombreDeSommets];
        this.distance = new int[this.nombreDeSommets];
        this.predecesseur = new int[this.nombreDeSommets];
        this.infini = 1;

        parcours = new ArrayList<Integer>();
    }
    
    //place "valeur" en position (i,j) de la matrice 
    public void modifierMatrice(int i,int j,int valeur) {
        this.matriceAdjacence.put(new Couple(i,j), valeur);
    }
    
    //ajoute une arête de pondération 1
    public void ajouterArete(int debut, int fin) {
        this.matriceAdjacence.put(new Couple(debut,fin), 1);  
        this.matriceAdjacence.put(new Couple(fin,debut), 1); 
    }
    
    //indique le numéro de sommet aux coordonnées i,j 
    public int numeroSommet(int i, int j, int taille) {
        return ((j-1)*taille+i);
    }
    
    //construit une grille de taille passée en paramètre
    public void construireGrille(int taille) {
        for (int i = 1; i<=taille; i++){
            for (int j = 1; j<=taille; j++){
                if (i+1<=taille){
                    ajouterArete(numeroSommet(i,j,taille), numeroSommet(i+1,j,taille));
                }
                if (j+1<=taille){
                    ajouterArete(numeroSommet(i,j,taille), numeroSommet(i,j+1,taille));
                }
                if (j-1>=1){
                    ajouterArete(numeroSommet(i,j,taille), numeroSommet(i,j-1,taille));
                }
                if (i-1>=1){
                    ajouterArete(numeroSommet(i,j,taille), numeroSommet(i-1,j,taille));
                }
            }
        }
    }
    
    //modifie le graphe pour considérer un obstacle en position l,c
    public void obstacle(int l, int c, int taille) { 
        //int pos = y*taille+(x+1);
        int pos = Graphe.coordonnees2Sommet(l, c, taille);
        this.modifierMatrice(pos-1, pos, 0);
        this.modifierMatrice(pos, pos-1, 0);
        this.modifierMatrice(pos, pos+1, 0);
        this.modifierMatrice(pos+1, pos, 0);
        this.modifierMatrice(pos, pos-30, 0);
        this.modifierMatrice(pos-30, pos, 0);
        this.modifierMatrice(pos, pos+30, 0);
        this.modifierMatrice(pos+30, pos, 0);
    }
    
    //permet d’afficher dans la console une représentation du graphe s’il a été 
    //conçu comme une grille dont la taille (largeur et hauteur) est passée en paramètre
    public void afficheGraphe(int taille) {
        String res = "";
        int sommet1 = 0;
        int sommet2 = 0;
        for(int i=1; i<=taille; i++) {
            for(int j=1; j<=taille; j++) {
                sommet1++;
                res += "O";
                if(this.Matrice(sommet1, sommet1+1)!=0) {
                    res+="-";
                } else {
                    res+=" ";
                }
            }
            res+="\n";
            for(int k=1; k<=taille; k++) {
                sommet2++;
                if(this.Matrice(sommet2, sommet2+taille)!=0) {
                    res+="|";
                } else {
                    res+=" ";
                }
                res+= " ";
            }
            res+="\n";
        }

        System.out.println(res);
    }
    
    public int getInfini() {
        return this.infini;
    }
    
    //retourne les coordonnées en fonction du sommet d'une grille
    public static Couple sommet2Coordonnees(int s) {
        Couple res = new Couple(0,0);
        int taille = Carte.get().taille();
        int X = s / taille;
        int Y = (s % taille) - 1;
        res.setX(X);
        res.setY(Y);
        return res;
    }
    
    //retourne le numéro de sommet du graphe
    public static int coordonnees2Sommet(int l, int c) {
        int taille = Carte.get().taille();
        return l*taille+c+1;
    }
    
    //retourne le numéro de sommet du graphe
    public static int coordonnees2Sommet(int l, int c, int taille) {
        //int taille = Carte.get().taille();
        return l*taille+c+1;
    }
    
    public void parcoursLargeur(int numS) {
        int s = numS;
        Stack file = new Stack();
        boolean mark[] = {};
        mark = new boolean[this.nombreDeSommets + 1];
        String str = new String();

        for (int i = 1; i <= this.NombreSommet(); i++) {
            mark[i] = false;
        }

        mark[s] = true;
        file.add(s);

        while (!file.empty()) {
            int x = (int) file.firstElement();
            file.remove(0);
            str += x + "-";
            for (int i = 1; i <= this.NombreSommet(); i++) {
                if ((!mark[i]) && (this.Matrice(i, x) != 0)) {
                    file.add(i);
                    mark[i] = true;
                }
            }
        }

        System.out.println(str);
    }
    
    //simule une valeur infinie pour le graphe
    private void calculInfini() {
        this.infini += this.nombreDeSommets * this.nombreDeSommets;
    }
    
    //relâchement de Dijkstra
    private void relachement(int a, int i) {
        if (this.Matrice(a, i) != 0) {
            if (distance[i] > (distance[a] + this.Matrice(a, i))) {
                distance[i] = distance[a] + this.Matrice(a, i);
                predecesseur[i] = a;

            }
        }
    }

    //selectionSommet de Dijktra
    private int selectionSommet() {
        int indice = 0;
        int min = this.infini;
        for (int i = 0; i <= this.nombreDeSommets - 1; i++) {
            if (distance[i] < min && !mark[i]) {
                indice = i;
                min = distance[i];
            }
        }

        return indice;

    }

    //retourne vrai s'il existe un sommet non marqué (Dijkstra)
    private boolean existeUnSommetNonMarque() {
        boolean resultat = false;
        for (int i = 0; i <= this.nombreDeSommets - 1; i++) {
            if (!mark[i]) {
                resultat = true;
            }
        }
        return resultat;
    }

    //initialisation de Dijktra
    private void initialisation(int s) {
        this.calculInfini();
        for (int i = 0; i <= this.nombreDeSommets - 1; i++) {
            mark[i] = false;
            distance[i] = this.infini;
            predecesseur[i] = -1;
        }

        distance[s] = 0;
    }

    //calcul la distance entre 2 sommets (valeur de retour) et stocke le parcours dans l'attribut parcours
    public int distanceDijkstra(int depart, int arrivee) {
        this.initialisation(depart);
        this.calculInfini();
        int a;
        while (this.existeUnSommetNonMarque()) {
            a = this.selectionSommet();
            mark[a] = true;
            for (int i = 0; i <= this.nombreDeSommets - 1; i++) {
                this.relachement(a, i);
            }
        }

        // récupération du chemins de sommets entre départ et arrivée
        parcours.clear();

        int arr = arrivee;                                      //Récopie du sommet de d'arrivéé pour manipulation(éventuel changement de sa valeur à venir)
        //contient les sommets parcouru dans l'ordre arrivée ---> départ
        boolean sortie = false;
        while (sortie == false) {                                                    // on retrace les sommets parcouru en partant du sommet d'arrivée pour atteindre le sommet départ
            parcours.add(arr);
            arr = this.predecesseur[arr];
            if (arr == -1) {
                sortie = true;
            }

        }                                  //pas besoin d'ajouter le prédécesseur du sommet de départ

        //System.out.print("sommets parcourus :");
        //for (int j = parcours.size() - 1; j >= 0; j--) { //affichage du sommet de départ au sommet d'arrivée(le dernier sommet ajouté étant le sommet de départ)
        //    System.out.print(" --> " + parcours.get(j));
        //}
        //System.out.println(" ");
        
        ArrayList<Integer> parcoursInverse = this.parcours;
        ArrayList<Integer> parcours = new ArrayList<Integer>();
        for(int i=this.parcours.size()-1; i>=0; i--) {
            parcours.add(parcoursInverse.get(i));
        }
        this.parcours = parcours;
        
        return distance[arrivee];
    }

    public ArrayList<Integer> getParcours() {     // permet de récupérer l'itinéraire entre départ et arrivée du calcul de distance

        return parcours;
    }
    
    public Objet oeufPlusProche(int s) {
        Objet oeuf = null;
        Carte carte = Carte.get();
        Couple couple = null;
        ArrayList<Boolean> mark = new ArrayList();
        ArrayList<Integer> aTraiter = new ArrayList();
        int x;
        int nombre = 0;
        for(int i=1; i<=this.nombreDeSommets; i++) {
            mark.add(false);
        }
        mark.set(s-1, true);
        aTraiter.add(s);
        while(!aTraiter.isEmpty()) {
            x = aTraiter.get(0);
            aTraiter.remove(0);
            nombre++;
            
            //traduction du sommet en coordonnees
            couple = sommet2Coordonnees(x);
            
            if(carte.getObjet(couple.getY(), couple.getX()) != null) {
                if(carte.getCase(couple.getY(), couple.getX()).getObjet().getType() == Oeuf) {
                    System.out.println("l'oeuf le plus proche est aux coordonnées "+couple.getX()+
                            ", "+ couple.getY()); 
                    oeuf = Carte.get().getObjet(couple.getY(),couple.getX());
                }
            }
            
            //System.out.println("Sommet "+x+" est le numéro "+nombre);
            //System.out.println(aTraiter.size());
            for(int i=1; i<=this.nombreDeSommets; i++) {
                if((!mark.get(i-1)) && (estVoisinDe(i,x))) {
                    aTraiter.add(i);
                    mark.set(i-1, true);
                }
            }
        }
        return oeuf;
    }
  
    //retourne les coordonnées
    public boolean estVoisinDe(int i, int x) {
        boolean res = false;
        if(this.Matrice(i, x)>0) {
            res = true;
        }
        return res;
    }
       
      
    //renvoie la valeur de la matrice en position (i,j)
    public int Matrice(int i,int j) {
        //valeur par défaut
        int res = 0;
        Couple c = new Couple(i,j);
        //si (i,j) est bien présent dans la matrice
        if(this.matriceAdjacence.containsKey(c)) {
            res = this.matriceAdjacence.get(c);
        }
        return res;
    }
    
    //renvoie le nombre de sommet du graphe
    public int NombreSommet() {
        return this.nombreDeSommets;
    }
    
    //renvoie la matrice d'adjacence
    @Override
    public String toString() {
        String res = "";
        for(int i=1;i<=this.nombreDeSommets;i++) {
            for(int j=1;j<=this.nombreDeSommets;j++) {
                res += this.Matrice(i, j);
                if(j!= this.nombreDeSommets) {
                    res += " / ";
                }
            }
            if(i!= this.nombreDeSommets) {
                res += "\n";
            }
        }
        return res;
    }
}
