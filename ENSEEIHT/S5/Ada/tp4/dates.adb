-- Implantation d'un module Dates très simplifié.

with Ada.Text_IO;
use  Ada.Text_IO;
with Ada.Integer_Text_IO;
use  Ada.Integer_Text_IO;

package body Dates is

    procedure Initialiser ( Date  : out T_Date  ;
                            Jour  : in  Integer ;
                            Mois  : in  T_Mois  ;
                            Annee : in  Integer ) is
    begin
        Date.Jour := Jour;
        Date.Mois := Mois;
        Date.Annee := Annee;
    end Initialiser;

    -- Afficher un entier sur 2 positons au moins (avec des zéros
    -- supplémentaires si nécessaires)
    --
    -- Paramètres :
    --     Nombre : le nombre à afficher
    --
    -- Nécessite :
    --     Nombre >= 0
    --
    procedure Afficher_Deux_Positions (Nombre : in Integer) with
        Pre => Nombre >= 0
    is
    begin
        Put (Nombre / 10, 1);
        Put (Nombre mod 10, 1);
    end Afficher_Deux_Positions;

    procedure Afficher (Date : in T_Date) is
    begin
        Afficher_Deux_Positions (Date.Jour);
        Put ('/');
        Afficher_Deux_Positions (T_Mois'pos (Date.Mois) + 1);
        Put ('/');
        Afficher_Deux_Positions (Date.Annee / 100);
        Afficher_Deux_Positions (Date.Annee mod 100);
    end Afficher;


    function Le_Jour (Date : in T_Date) return Integer is
    begin
        return Date.Jour;
    end Le_Jour;


    function Le_Mois(Date : in T_Date) return T_Mois is
    begin
        return Date.Mois;
    end Le_Mois;


    function L_Annee (Date : in T_Date) return Integer is
    begin
        return Date.Annee;
    end L_Annee;


end Dates;
--Exercice 1:

 --   1/ Le fichier .adb correspond au corps du module tandis que le fichier .ads correspond
     --  à son interface, i.e., les spécifications et les contrats.

   -- 2/ Ceci est dû au fait qu'ils sont déjà présents dans l'interface dates.ads.

   -- 3/ Parce que le sous-programme Afficher_Deux_Positions fait partie de ce qui est encapsulé
    --  et n'est guère présent dans l'interface dates.ads. 

   -- 4/ Un utilisateur du module Dates peut utiliser:
        --    Initialiser, Afficher, Le_Mois, Le_Jour et L_Annee

   -- 5/ Les erreurs données et les raisons du refus sont: 
            -- Afficher_Deux_Positions n'est pas définie
                    -- Ce qui est logique puisque cette procédure est dans le corps du module et 
                    --   non accessible par l'utilisateur.
            -- Une_Date a un préfixe non défini.
                    -- Ceci est dû au fait que le programme essaie d'utiliser le type T_Date alors
                    --   que ce dernier est privé donc ses attribus sont non accessibles par
                   --    l'utilisateur.
  --  6/ T_Mois ne peut pas être privé puisque l'utilisateur peut utiliser la procédure Initialiser
    --   qui a en paramètre un type T_Mois et a donc besoin d'y accéder.
    
 --   7/ Si T_Dates est transformé en type très privé, alors on ne pourra plus éxecuter des
  --     instructions du genre: Autre_Date:=Une_Date; chose qui existe dans exemple_dates_erreurs.adb
   --    (ligne 36: une affectation et ligne 41: une évaluation)


--Exercice 2: 
