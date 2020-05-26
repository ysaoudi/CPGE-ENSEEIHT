with Ada.Text_IO;  use Ada.Text_IO;


package body Ensembles_Chainage is

procedure Init(Ensemble : in out T_Ensemble) is
	    begin
	        Ensemble.Suivant:=Null;
	    end Init;
	
	
	procedure Destruct(Ensemble : in out T_Ensemble) is
	    begin
	        Ensemble.Suivant:=Null;
	    end Destruct;
	
	
	function Est_Vide(Ensemble: in T_Ensemble) return Boolean is
        begin
            return Ensemble.Suivant=Null;            
	    end Est_Vide;
	
	
	function Taille_Ensemble(Ensemble: in T_Ensemble) return Integer is
	    Parcours: T_Acc;
	    j:integer;
	    begin
	        j:=0;
	        Parcours:=Ensemble.Suivant;
	        while Parcours/=Null loop
	            j:=j+1;
	            Parcours:=Parcours.all.Suivant;
	        end loop;
	        return j;
	    end Taille_Ensemble;
	
	
	function Existe_dans_Ensemble(Element: in T_Element; Ensemble: in T_Ensemble) return Boolean is
	    Parcours: T_Acc;
	    begin
	        if Est_Vide(Ensemble) then
	            return False;
	        else
	            Null;
	        end if;
            Parcours:=Ensemble.suivant;
            while Parcours/=Null and then Parcours.all.Element/=Element loop
                Parcours:=Parcours.all.Suivant;
            end loop;
	        return Not(Parcours=Null);
	    end Existe_dans_Ensemble;
	
	
	procedure Add_Element(Element: in T_Element; Ensemble: in out T_Ensemble) is
	    Parcours:T_Acc;
	    begin
	        if not Existe_dans_Ensemble(Element,Ensemble) then
                Parcours:= New T_Ensemble;
                Parcours.all.Element:=Element;
                Parcours.all.Suivant:=Ensemble.Suivant;
                Ensemble.Suivant:=Parcours;
	        else
	            Null;
	        end if;
	    end Add_Element;
	
	
	procedure Del_Element(Element: in T_Element; Ensemble: in out T_Ensemble) is
	    begin
            if Ensemble.Suivant.all.Element=Element then
                Ensemble.Suivant:=Ensemble.Suivant.all.Suivant;
            else
               Del_Element(Element,Ensemble.Suivant.all);
            end if;
	    end Del_Element;
	    
	procedure Appliquer_Sur_Tous(Ensemble: in out T_Ensemble) is
	    Parcours : T_Acc;
	    begin
	        Parcours:=Ensemble.Suivant;
	        while Parcours/=Null loop
	           Operation(Parcours.all.Element);
	            Parcours:=Parcours.all.Suivant;
	        end loop;
	    end Appliquer_Sur_Tous;
	        
	
	
end Ensembles_Chainage;
