with Ada.Text_IO;            use Ada.Text_IO;
with Ada.Integer_Text_IO;    use Ada.Integer_Text_IO;
with Ada.Unchecked_Deallocation;

package body ABR is

	procedure Free is
		new Ada.Unchecked_Deallocation (Object => T_Noeud, Name => T_ABR);


	procedure Initialiser(Abr: out T_ABR) is
	begin
		Abr:=Null;
	end Initialiser;


	function Est_Vide (Abr : T_Abr) return Boolean is
	begin
		return Abr=Null;	
	end;


	function Taille (Abr : in T_ABR) return Integer is
        begin
            if not Est_Vide(Abr) then
                return 1+ Taille(Abr.all.Sous_Arbre_Gauche)+Taille(Abr.all.Sous_Arbre_Droit);
            else
                return 0;
            end if;
        end Taille;


	procedure Inserer (Abr : in out T_ABR ; Cle : in Character ; Donnee : in Integer) is
	    begin
	        if Abr=Null then
	            Abr:= New T_Noeud'(Cle, Donnee, Null, Null);
	        else
	            if Cle>Abr.all.Cle then
	                Inserer(Abr.all.Sous_Arbre_Droit,Cle, Donnee);
	            elsif Cle<Arbre.all.Cle then
	                Inserer(Abr.all.Sous_Arbre_Gauche,Cle, Donnee);
	            elsif Cle=Arbre.all.Cle then
	                raise Cle_Presente_Exception;
	            else
	                Null;
	            end if;
	        end if;
	        exception
	            when Cle_Presente_Exception =>
	                Put_Line("La clé existe déjà!");	                
	    end Inserer;


	procedure Modifier (Abr : in out T_ABR ; Cle : in Character ; Donnee : in Integer) is
	begin
	    if not Est_Vide(Abr) then
	        if Cle>Abr.all.Cle then
	            Modifier(Abr.all.Sous_Arbre_Droit,Cle);
	        elsif Cle<Abr.all.Cle then
	            Modifier(Abr.all.Sous_Arbre_Gauche,Cle);
	        elsif Cle=Abr.all.Cle then
	            Abr.all.Donnee:=Donnee;
	        else
	            Null;
	        end if;
	    else
	        raise Cle_Absente_Exception;
	    end if;
	    exception
	        when Cle_Absente_Exception =>
	            Put_Line("La clé n'existe pas!");
	end Modifier;


	function La_Donnee (Abr : in T_ABR ; Cle : in Character) return Integer is
	begin
		if not Est_Vide(Abr) then
	        if Cle>Abr.all.Cle then
	            La_Donnee(Abr.all.Sous_Arbre_Droit,Cle);
	        elsif Cle<Abr.all.Cle then
	            La_Donnee(Abr.all.Sous_Arbre_Gauche,Cle);
	        elsif Cle=Abr.all.Cle then
	            return Abr.all.Donnee;
	        end if;
	    else
	        raise Cle_Absente_Exception;
	    end if;
	    --exception
	      --  when Cle_Absente_Exception =>
	        --    Put_Line("La clé n'existe pas!");
	end La_Donnee;


	procedure Supprimer (Abr : in out T_ABR ; Cle : in Character) is
	    procedure Supprimer_Cle_Sterile(Abr:in out T_ABR; Cle:in Character) is
	        begin
	            if Est_Vide(Abr) then
	                Null;
	            else
	                if Cle>Abr.all.Cle then
	                    Supprimer_Cle_Sterile(Abr.all.Sous_Arbre_Droit,Cle);
	                elsif Cle<Abr.all.Cle then 
	                    Supprimer_Cle_Sterile(Abr.all.Sous_Abre_Gauche,Cle);
	                elsif Cle=Abr.all.Cle then
	                    Abr:=Null;
	                end if;
	            end if;
	        end Supprimer_Cle_Sterile;
	    procedure Supprimer_Cle_Un_Fils(Abr: in out T_Abr;Cle: in Character) is
	        begin
	            if Est_Vide(Abr) then
	                Null;
	            else
	                if Cle>Abr.all.Cle then
	                    Supprimer_Cle_Un_Fils(Abr.all.Sous_Arbre_Droit,Cle);
	                elsif Cle<Abr.all.Cle then 
	                    Supprimer_Cle_Un_Fils(Abr.all.Sous_Abre_Gauche,Cle);
	                elsif Cle=Abr.all.Cle then
	                    if not Est_Vide(Abr.all.Sous_Arbre_Gauche) then
	                        Abr.all.Cle:=Abr.all.Sous_Arbre_Gauche.all.Cle;
	                        Abr.all.Donnee:=Abr.all.Sous_Arbre_Gauche.all.Donnee;
	                        Abr.all.Sous_Arbre_Gauche:=Null;
	                    elsif not Est_Vide(Abr.all.Sous_Arbre_Droit) then
	                        Abr.all.Cle:=Abr.all.Sous_Arbre_Droit.all.Cle;
	                        Abr.all.Donnee:=Abr.all.Sous_Arbre_Droit.all.Donnee;
	                        Abr.all.Sous_Arbre_Droit:=Null;
	                    end if;
	                end if;
	            end if;
	        end Supprimer_Cle_Un_Fils;
	    procedure Supprimer_Cle_Feconde(Abr: in out T_Abr;Cle: in Character) is
	        begin
	            if Est_Vide(Abr) then
	                Null;
	            else
	                if Cle>Abr.all.Cle then
	                    if (not Est_Vide(Abr.all.Sous_Arbre_Droit)) and then Abr.all.Sous_Arbre_Droit.all.Cle=Cle then
	                        FilsG_CleFeconde:=Abr.all.Sous_Arbre_Droit.all.Sous_Arbre_Gauche;
	                        FilsD_CleFeconde:=Abr.all.Sous_Arbre_Droit.all.Sous_Arbre_Droit;
	                        -- chercher Plus_Grande_Cle_FilsG
	                        Plus_Grande_Cle_FilsG:=FilsG_CleFeconde;
	                        while not Est_Vide(Plus_Grande_Cle_FilsG.all.Sous_Arbre_Droit) loop
	                            Plus_Grande_Cle_FilsG:=Plus_Grande_Cle_FilsG.all.Sous_Arbre_Droit;
	                        end loop;
	                        Plus_Grande_Cle_FilsG.all.Sous_Arbre_Droit:=FilsD_CleFeconde;
	                        Vider(Arbre.all.Sous_Arbre_Droit.all.Sous_Arbre_Droit);
	                        Abr.all.Sous_Arbre_Droit:=FilsG_CleFeconde;	                        
	                    elsif (not Est_Vide(Abr.all.Sous_Arbre_Droit)) and then Abr.all.Sous_Arbre_Droit.all.Cle/=Cle then 
	                        Supprimer_Cle_Feconde(Abr.all.Sous_Arbre_Droit,Cle);
	                    end if;
	                elsif Cle<Abr.all.Cle then
	                    if (not Est_Vide(Abr.all.Sous_Arbre_Gauche)) and then Abr.all.Sous_Arbre_Gauche.all.Cle=Cle then
	                        FilsG_CleFeconde:=Abr.all.Sous_Arbre_Gauche.all.Sous_Arbre_Gauche;
	                        FilsD_CleFeconde:=Abr.all.Sous_Arbre_Gauche.all.Sous_Arbre_Droit;
	                        -- chercher Plus_Grande_Cle_FilsG
	                        Plus_Grande_Cle_FilsG:=FilsG_CleFeconde;
	                        while not Est_Vide(Plus_Grande_Cle_FilsG.all.Sous_Arbre_Droit) loop
	                            Plus_Grande_Cle_FilsG:=Plus_Grande_Cle_FilsG.all.Sous_Arbre_Droit;
	                        end loop;
	                        Plus_Grande_Cle_FilsG.all.Sous_Arbre_Droit:=FilsD_CleFeconde;
	                        Vider(Arbre.all.Sous_Arbre_Gauche.all.Sous_Arbre_Droit);
	                        Abr.all.Sous_Arbre_Gauche:=FilsG_CleFeconde;	                        
	                    elsif (not Est_Vide(Abr.all.Sous_Arbre_Droit)) a
	                    elsif (not Est_Vide(Abr.all.Sous_Arbre_Gauche)) and then Abr.all.Sous_Arbre_Gauche.all.Cle/=Cle then 
	                        Supprimer_Cle_Feconde(Abr.all.Sous_Arbre_Gauche,Cle);
	                    end if;
	                end if;
	        end Supprimer_Cle_Feconde;
	    begin
		    if Est_Vide(Abr) then
		        Null;
		    --tester les cas de la clé et utiliser la sous-procédure convenable!
	    end Supprimer;


	procedure Vider (Abr : in out T_ABR) is
	    procedure Supprimer_Fils (Abr: in out T_ABR) is
            begin
                if not Est_Vide(Abr) then
                    Supprimer_Fils(Abr.all.Sous_Arbre_Gauche);
                    Supprimer_Fils(Abr.all.Sous_Arbre_Droit);
                    Abr:=Null;
                end if;
	        end Supprimer_Fils;
	    begin
		    Supprimer_Fils(Abr);
		    Abr:=Null;
	    end Vider;


	procedure Afficher (Abr : in T_Abr) is
	    Parcours:T_Abr;
	    begin
		    if not Est_Vide(Abr) then
		        if not Est_Vide(Abr.all.Sous_Arbre_Gauche) then
		            Afficher(Abr.all.Sous_Arbre_Gauche);
		        else
		            Put_Line(Integer'Image(Abr.all.Donnee) & ":" & Character'Image(Abr.all.Cle) & "/");
		            if not Est_Vide(Abr.all.Sous_Arbre_Droit) then
		                Afficher(Abr.all.Sous_Arbre_Droit);
		            else
		                Null;
		            end if;
		        end if;
		    end if;
	    end Afficher;


	procedure Afficher_Debug (Abr : in T_Abr) is
        Original: constant T_Abr:=Abr;
        function Gen(Cle: in Integer; Abr:in T_Abr) return Integer is
            begin
                if Est_Vide(Abr) then
                    return 0;
                else
                    if Abr.all.Cle=Cle then
                        return 0;
                    elsif not Est_Vide(Abr.all.Sous_Arbre_Gauche) and then Cle<Abr.all.Cle then
                        return 1 + Gen(Cle, Abr.all.Sous_Arbre_Gauche);
                    elsif not Est_Vide(Abr.all.Sous_Arbre_Droit) and then Cle>Abr.all.Cle then
                        return 1 + Gen(Cle,Abr.all.Sous_Arbre_Droit);
                    else
                        return 0;
                    end if;
               end if;
            end Gen;
        procedure Afficher_Fils(Arbre: in T_Abr) is
            begin
                if Est_Vide(Abr) then
                    Null;
                else
                    if not Est_Vide(Abr.all.Sous_Arbre_Gauche) then
                        for i in 1..Gen(Abr.all.Sous_Arbre_Gauche.all.Cle,Original) loop
                            Put("    ");
                        end loop;
                        Put(">");
                        Put(Integer'Image(Abr.all.Sous_Arbre_Gauche.all.Donnee));
                        Put(" :");
                        Put(Character'Image(Abr.all.Sous_Arbre_Gauche.all.Cle));
                        New_Line;
                        Afficher_Fils(Abr.all.Sous_Arbre_Gauche);
                    else 
                        Null;
                    end if;
                    if not Est_Vide(Abr.all.Sous_Arbre_Droit) then
                        for i in 1..Gen(Abr.all.Sous_Arbre_Droit.all.Cle,Original) loop
                            Put("    ");
                        end loop;
                        Put("<");
                        Put(Integer'Image(Abr.all.Sous_Arbre_Droit.all.Donnee));
                        Put(" :");
                        Put(Character'Image(Abr.all.Sous_Arbre_Droit.all.Cle));
                        New_Line;
                        Afficher_Fils(Abr.all.Sous_Arbre_Droit);
                    else
                        Null;
                    end if;
                end if;
            end Afficher_Fils;
                   
        begin
            if Abr=Null then
                New_Line;
            else
                Put_Line("/" & Character'Image(Abr.all.Cle));
                Afficher_Fils(Abr);
                
            end if;
        end Afficher_ABR; 
	end Afficher_Debug;

end ABR;
