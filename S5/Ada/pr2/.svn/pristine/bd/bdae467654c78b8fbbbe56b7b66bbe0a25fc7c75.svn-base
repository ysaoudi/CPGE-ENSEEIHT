with Ada.Text_IO;  use Ada.Text_IO;


package body Ensembles_Tableau is
	
	
	procedure Init(Ensemble : in out T_Ensemble) is
	    begin
	        Ensemble.Taille:=0;
	    end Init;
	
	
	procedure Destruct(Ensemble : in out T_Ensemble) is
	    begin
	        Null;
	    end Destruct;
	
	
	function Est_Vide(Ensemble: in T_Ensemble) return Boolean is
	    begin
	        return Ensemble.Taille=0;
	    end Est_Vide;
	
	
	function Taille_Ensemble(Ensemble: in T_Ensemble) return Integer is
	    begin
	        return Ensemble.Taille;
	    end Taille_Ensemble;
	
	
	function Existe_dans_Ensemble(Element: in T_Element; Ensemble: in T_Ensemble) return Boolean is
	    begin
	        if Est_Vide(Ensemble) then
	            return False;
	        else
	            Null;
	        end if;
	        for i in 1..Ensemble.Taille loop
	            if Ensemble.Tableau(i)=Element then
	                return True;
	            else
	                Null;
	            end if;
	        end loop;
	        return False;
	    end Existe_dans_Ensemble;
	
	
	procedure Add_Element(Element: in T_Element; Ensemble: in out T_Ensemble) is
	    begin
	        if not Existe_dans_Ensemble(Element,Ensemble) then
	            Ensemble.Taille := Ensemble.Taille + 1;
	            Ensemble.Tableau(Ensemble.Taille):=Element;
	        else
	            Null;
	        end if;
	    end Add_Element;
	
	
	procedure Del_Element(Element: in T_Element; Ensemble: in out T_Ensemble) is
	    j:integer;
	    tmp:T_Element;
	    begin
	        for i in 1..Ensemble.Taille loop
	            if Ensemble.Tableau(i)=Element then
	                j:=i;
	            else
	                Null;
	            end if;
	        end loop;
	        tmp:=Ensemble.Tableau(Ensemble.Taille);
	        Ensemble.Tableau(Ensemble.Taille):=Element;
	        Ensemble.Tableau(j):=tmp;
	        Ensemble.Taille:=Ensemble.Taille -1;
	    end Del_Element;
	    
	procedure Appliquer_Sur_Tous(Ensemble: in out T_Ensemble) is
	    begin
	        for i in 1..Ensemble.Taille loop
	            Operation(Ensemble.Tableau(i));
	        end loop;
	    end Appliquer_Sur_Tous;
	        
	
	
end Ensembles_Tableau;
