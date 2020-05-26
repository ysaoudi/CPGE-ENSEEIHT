with Ada.Text_IO;                 use Ada.Text_IO;
with Ada.Integer_Text_IO;         use Ada.Integer_Text_IO;
with Ada.Float_Text_IO;           use Ada.Float_Text_IO;
with Ada.Unchecked_Deallocation;

package body Vecteurs_Creux is


	procedure Free is
		new Ada.Unchecked_Deallocation (T_Cellule, T_Vecteur_Creux);


	procedure Initialiser (V : out T_Vecteur_Creux) is
	begin
		V:=Null;
	end Initialiser;


	procedure Detruire (V: in out T_Vecteur_Creux) is
	begin
		if V/=Null then
		    Detruire(V.all.Suivant);
		    Free(V);
		end if;
	end Detruire;


	function Est_Nul (V : in T_Vecteur_Creux) return Boolean is
	begin
		return V=Null;
	end Est_Nul;


	function Composante_Recursif (V : in T_Vecteur_Creux ; Indice : in Integer) return Float is
	begin
	    if V=Null then
	        return 0.0;
	    else        
	        if Indice=V.all.Indice then
	            return V.all.Valeur;
	        else
	            return Composante_Recursif(V.all.Suivant,Indice);
	        end if;
	    end if;
	end Composante_Recursif;


	function Composante_Iteratif (V : in T_Vecteur_Creux ; Indice : in Integer) return Float is
	Parcours: T_Vecteur_Creux;
	begin
		Parcours:=V;
		if V=Null then
		    return 0.0;
		else
		    for i in 1..Indice-1 loop
		        if Parcours/=Null then
		            Parcours:=Parcours.all.Suivant;
		        else
		            return 0.0;
		        end if;
		    end loop;
		end if;
		return Parcours.all.Valeur;
		
	end Composante_Iteratif;


	procedure Modifier (V : in out T_Vecteur_Creux ;
				       Indice : in Integer ;
					   Valeur : in Float ) is
	Interm:T_Vecteur_Creux;				   
	begin
	    if V=Null then
	        if Valeur /= 0.0 then
	            V:= new T_Cellule'(Indice,Valeur,V);
	        else
	            Null;
	        end if;
	    elsif V/=Null then
	        if Indice > V.all.Indice then
	            Modifier(V.all.Suivant,Indice,Valeur);     
	        elsif Indice=V.all.Indice then
	            V.all.Valeur:=Valeur;
	        elsif Indice < V.all.Indice then
	            Interm:= new T_Cellule'(Indice,Valeur,V);
	            V:=Interm;	        
	        else
	            Null;
	        end if;
	    else 
	        Null;
	    end if;
	end Modifier;


	function Sont_Egaux_Recursif (V1, V2 : in T_Vecteur_Creux) return Boolean is
	begin
	    if V1=Null and V2=Null then
	        return True;
	    elsif V1.all.Valeur/=V2.all.Valeur then
	        return False;
	    else
	        return Sont_Egaux_Recursif(V1.all.Suivant, V2.all.Suivant);
	    end if;
	end Sont_Egaux_Recursif;


	function Sont_Egaux_Iteratif (V1, V2 : in T_Vecteur_Creux) return Boolean is
	Parcours1: T_Vecteur_Creux;
	Parcours2: T_Vecteur_Creux;
	begin
		Parcours1:=V1;
		Parcours2:=V2;
		loop
		    if Parcours1.all.Valeur=Parcours2.all.Valeur then
		        Parcours1:=Parcours1.all.Suivant;
		        Parcours2:=Parcours2.all.Suivant;
		    else
		        return False;
		    end if;
		exit when Parcours1=Null and Parcours2=Null;
		end loop;
		return True;
	end Sont_Egaux_Iteratif;


	procedure Additionner (V1 : in out T_Vecteur_Creux; V2 : in T_Vecteur_Creux) is
	Parcours1: T_Vecteur_Creux;
	Parcours2: T_Vecteur_Creux;
	i: integer:=1;
	begin
		Parcours1:=V1;
		Parcours2:=V2;
		if V1/= Null and V2/= Null then
	        loop
	            if Parcours1=Null and Parcours2/=Null then
	                Modifier(V1,i,Parcours2.all.Valeur);
	                Parcours2:=Parcours2.all.Suivant;
    	        elsif Parcours1/=Null and Parcours2=Null then
    	            Modifier(V1,i,Parcours1.all.Valeur);
    	            Parcours1:=Parcours1.all.Suivant;
    	        elsif Parcours1/=Null and Parcours2/=Null then		            
    	            Modifier(V1,i,Parcours1.all.Valeur + Parcours2.all.Valeur);
    	            Parcours1:=Parcours1.all.Suivant;
    	            Parcours2:=Parcours2.all.Suivant;
    	        else 
    	            Null;
    	        end if;    
    	        i:=i+1;
	    exit when Parcours1=Null and Parcours2=Null;
	    end loop;
	    else
	        Null; 
    	end if;
	end Additionner;


	function Norme2 (V : in T_Vecteur_Creux) return Float is
	Parcours: T_Vecteur_Creux;
	Somme: Float :=0.0;
	begin
		Parcours:=V;
		if V=Null then
		    return 0.0;
		else    
		    loop
		        Somme:= Somme + (Parcours.all.Valeur)**2;
		        Parcours:=Parcours.all.Suivant;		    
		    exit when Parcours=Null;
		    end loop;
		end if;
		return Somme;
	end Norme2;


	Function Produit_Scalaire (V1, V2: in T_Vecteur_Creux) return Float is
    Parcours1: T_Vecteur_Creux;
    Parcours2: T_Vecteur_Creux;
	PScalaire: Float :=0.0;
	begin
	    Parcours1:=V1;
		Parcours2:=V2;
	    if V1/=Null or V2/=Null then
		    loop
		        PScalaire:= PScalaire + Parcours1.all.Valeur*Parcours2.all.Valeur;
		        Parcours1:=Parcours1.all.Suivant;
		        Parcours2:=Parcours2.all.Suivant;		    
		    exit when Parcours1=Null or Parcours2=Null;
		    end loop;
		else
		    return 0.0;
		end if;
		return PScalaire;
	end Produit_Scalaire;


	procedure Afficher (V : T_Vecteur_Creux) is
	begin
		if V = Null then
			Put ("--E");
		else
			Put ("-->[ ");
			Put (V.all.Indice, 0);
			Put (" | ");
			Put (V.all.Valeur, Fore => 0, Aft => 1, Exp => 0);
			Put (" ]");
			Afficher (V.all.Suivant);
		end if;
	end Afficher;


	function Nombre_Composantes_Non_Nulles (V: in T_Vecteur_Creux) return Integer is
	begin
		if V = Null then
			return 0;
		else
			return 1 + Nombre_Composantes_Non_Nulles (V.all.Suivant);
		end if;
	end Nombre_Composantes_Non_Nulles;


end Vecteurs_Creux;
