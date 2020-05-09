with Ensembles_Chainage;
with Ada.Text_IO; use Ada.Text_IO;
with Ada.Integer_Text_IO; use Ada.Integer_Text_IO;

procedure Test_Ensembles_Sujet_Chainage is


	-- Instancier le paquetage Ensembles_Chainage pour avoir un Ensemble
	-- d'entiers

	-- Définir une opération Afficher qui affiche les éléments d'un ensemble
	-- d'entier en s'appuyant sur Appliquer_Sur_Tous.  L'ensemble {5, 28, 10}
	-- sera affiché :
	--           5         28         10
    package Ensemble_Chainage_Entier is
        new Ensembles_Chainage (T_Element => Integer);
    use Ensemble_Chainage_Entier;	
    procedure Afficher_Element(Element: in out Integer) is
	    begin
            Put("    " & Integer'Image(Element));
	    end Afficher_Element;
    
    procedure Afficher is new Appliquer_Sur_Tous (Operation => Afficher_Element);
	-- Définir une opération Afficher qui affiche les éléments d'un ensemble
	-- d'entier en s'appuyant sur Appliquer_Sur_Tous.  L'ensemble {5, 28, 10}
	-- sera affiché :
	--           5         28         10

	Ens1 : T_Ensemble;

begin
	-- Créer un ensemble vide Ens1
	Init(Ens1);
	-- Afficher l'ensemble
	Afficher (Ens1);New_Line;
	-- Vérifier si vide ou non, sa taille, la présence ou pas de 2, 5, 7, 10
	pragma assert(Est_Vide(Ens1));
	pragma assert(Taille_Ensemble(Ens1)=0);
    pragma assert(not Existe_dans_Ensemble(2, Ens1));
    pragma assert(not Existe_dans_Ensemble(5, Ens1));
    pragma assert(not Existe_dans_Ensemble(7, Ens1));
    pragma assert(not Existe_dans_Ensemble(10, Ens1));
	-- Ajouter 5 dans Ens1
	Add_Element(5,Ens1);
	-- Afficher l'ensemble
	Afficher (Ens1);New_Line;
	-- Vérifier si vide ou non, sa taille, la présence ou pas de 2, 5, 7, 10
	pragma assert(not Est_Vide(Ens1));
	pragma assert(Taille_Ensemble(Ens1)=1);
    pragma assert(not Existe_dans_Ensemble(2, Ens1));
    pragma assert(Existe_dans_Ensemble(5, Ens1));
    pragma assert(not Existe_dans_Ensemble(7, Ens1));
    pragma assert(not Existe_dans_Ensemble(10, Ens1));
	-- Ajouter 28 puis 10 dans Ens1
	Add_Element(28,Ens1);
	Add_Element(10,Ens1);
	-- Afficher l'ensemble
	Afficher (Ens1);New_Line;
	-- Vérifier si vide ou non, sa taille, la présence ou pas de 2, 5, 7, 10
	pragma assert(not Est_Vide(Ens1));
	pragma assert(Taille_Ensemble(Ens1)=3);
    pragma assert(not Existe_dans_Ensemble(2, Ens1));
    pragma assert(Existe_dans_Ensemble(5, Ens1));
    pragma assert(not Existe_dans_Ensemble(7, Ens1));
    pragma assert(Existe_dans_Ensemble(10, Ens1));
	-- Ajouter 7 dans Ens1
	Add_Element(7,Ens1);
	-- Afficher l'ensemble	
	Afficher (Ens1);New_Line;
	-- Vérifier si vide ou non, sa taille, la présence ou pas de 2, 5, 7, 10
	pragma assert(not Est_Vide(Ens1));
	pragma assert(Taille_Ensemble(Ens1)=4);
    pragma assert(not Existe_dans_Ensemble(2, Ens1));
    pragma assert(Existe_dans_Ensemble(5, Ens1));
    pragma assert(Existe_dans_Ensemble(7, Ens1));
    pragma assert(Existe_dans_Ensemble(10, Ens1));
	-- Supprimer 10 en Ens1
	Del_Element(10,Ens1);
	-- Afficher l'ensemble	
	Afficher (Ens1);New_Line;
	-- Vérifier si vide ou non, sa taille, la présence ou pas de 2, 5, 7, 10
	pragma assert(not Est_Vide(Ens1));
	pragma assert(Taille_Ensemble(Ens1)=3);
    pragma assert(not Existe_dans_Ensemble(2, Ens1));
    pragma assert(Existe_dans_Ensemble(5, Ens1));
    pragma assert(Existe_dans_Ensemble(7, Ens1));
    pragma assert(not Existe_dans_Ensemble(10, Ens1));
	-- Supprimer 7 en Ens1
	Del_Element(7,Ens1);
	-- Vérifier si vide ou non, sa taille, la présence ou pas de 2, 5, 7, 10
	pragma assert(not Est_Vide(Ens1));
	pragma assert(Taille_Ensemble(Ens1)=2);
    pragma assert(not Existe_dans_Ensemble(2, Ens1));
    pragma assert(Existe_dans_Ensemble(5, Ens1));
    pragma assert(not Existe_dans_Ensemble(7, Ens1));
    pragma assert(not Existe_dans_Ensemble(10, Ens1));
	-- Supprimer 5 en Ens1
	Del_Element(5,Ens1);
	-- Afficher l'ensemble	
	Afficher (Ens1);New_Line;
	-- Vérifier si vide ou non, sa taille, la présence ou pas de 2, 5, 7, 10
	pragma assert(not Est_Vide(Ens1));
	pragma assert(Taille_Ensemble(Ens1)=1);
    pragma assert(not Existe_dans_Ensemble(2, Ens1));
    pragma assert(not Existe_dans_Ensemble(5, Ens1));
    pragma assert(not Existe_dans_Ensemble(7, Ens1));
    pragma assert(not Existe_dans_Ensemble(10, Ens1));

	-- Détruire l'ensemble
	Destruct(Ens1);

end Test_Ensembles_Sujet_Chainage;
