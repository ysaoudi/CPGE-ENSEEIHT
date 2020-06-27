Generic
    Capacite : Integer;
    type T_Element is private;

package Ensembles_Tableau is

	type T_Ensemble is limited private;
	
	--Intialiser l'ensemble (il est vide).
	procedure Init(Ensemble : in out T_Ensemble) with
	    Post => Est_Vide(Ensemble);
	
	--Detruire l'ensemble.
	procedure Destruct(Ensemble : in out T_Ensemble);
	
	--Est-ce que l'ensemble est vide?
	function Est_Vide(Ensemble: in T_Ensemble) return Boolean;
	
	--Quelle est la taille de l'ensemble?
	function Taille_Ensemble(Ensemble: in T_Ensemble) return Integer;
	
	--Est-ce qu'un élément existe dans l'ensemble?
	function Existe_dans_Ensemble(Element: in T_Element; Ensemble: in T_Ensemble) return Boolean;
	
	--Ajouter un élément à l'ensemble.
	procedure Add_Element(Element: in T_Element; Ensemble: in out T_Ensemble);
	
	--Supprimer un élément d'un ensemble.
	procedure Del_Element(Element: in T_Element; Ensemble: in out T_Ensemble) with
	    Pre => Existe_dans_Ensemble(Element,Ensemble);
	
	
	--Appliquer une opération sur tous les éléments d'un tableau (d'où la nécéssité des in out)
    generic
        with procedure Operation (Element: in out T_Element);
    procedure Appliquer_Sur_Tous (Ensemble : in out T_Ensemble);   


private
    type T_Tableau is array (1..Capacite) of T_Element;
    
    type T_Ensemble is
        record
            Tableau: T_Tableau;
            Taille: integer;
        end record;
            
        
end Ensembles_Tableau;
