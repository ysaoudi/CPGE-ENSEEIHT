-------------------------------------------------------------------------------------------------
--  Auteur   : SAOUDI Younes
--  Objectif : Programme qui aide l'utilisateur à réviser les tables de multiplication de 1 à 10.
-------------------------------------------------------------------------------------------------

with Ada.Text_IO;           use Ada.Text_IO;
with Ada.Integer_Text_IO;   use Ada.Integer_Text_IO;
with Ada.Calendar;          use Ada.Calendar;
With Alea;

procedure Multiplications is
    package Mon_Alea is
		    new Alea (1, 10);  -- générateur de nombre dans l'intervalle [1, 10]
	    use Mon_Alea;
    choix: String:= "Oui";   --Variable qui laissera l'utilisateur quitter l'application s'il répond par non
    table: integer; --La table que choisit l'utilisateur
    rhs: integer; --Right hand side/Nombre du côté droit de l'opération
    result: integer; --Résultat
    false_nbr: integer:=0; --Nombre de réponses fausses
    Delay_time: Duration; --Le temps pris pour que l'utilisateur réponde
    Op_Start: Time; --La date où l'utilisateur commence à réfléchir à l'opération
    Op_End: Time; --La date où il répond à l'opération
    Max_Delay: Duration;--La durée maximale que l'utilisateur a pris pour répondre à une certaine opération pour une table spécifique
    Avg_Delay: Duration;--Le temps moyen de réponse pour une table spécifique
    Sum_Delay: Duration;--La somme des temps de réponse pour une table spécifique
    Delayed_Rhs: integer;--Le nombre du côté droit pour lequel l'utilisateur a pris le plus de temps
begin
    loop
        --Initialisation des variables mais surtout leur REinitialisation puisqu'ils seront en général différents pour chaque table        
        false_nbr:=0;                
        Max_Delay:=0.0;
        Sum_Delay:=0.0;
        Put("Réviser la table de : ");
        Get(table);
        New_line;
        while table<1 or table >10 loop --Redemander à l'utilisateur une table correcte
            Put("Saisissez une table de multiplication entre 1 et 10 : ");
            Get(table);
            New_line;
        end loop;        
        for i in 1..10 loop            --10 opérations à faire
            Get_Random_Number(rhs);    --Génération du nombre du côté droit aléatoirement
            Put("(Multip. " & integer'image(i) & ") " & Integer'Image(table) & " * " & Integer'Image(rhs) & " : ? "); --Affichage de l'opération complète
            Op_Start:=Clock; --Enregistrement de la date
            Get(result);
            Op_End:=Clock; --Enregistrement de la date
            Delay_time:= Op_End - Op_Start; --Le temps que l'utilisateur a pris pour faire son calcul
            Sum_Delay:= Sum_Delay + Delay_time;            
            if Delay_time>Max_Delay then --Si le temps pris pour cette opération est plus grand que le délai maximum
                Max_Delay:=Delay_time;
                Delayed_Rhs:=rhs; --Mémorisation du nombre de côté droit pour lequel l'utilisateur a pris le plus de temps
            else
                Null;
            end if;
            if result=table*rhs then --Si l'utilisateur a bien répondu
                Put_Line("Bravo! Réponse correcte.");
            else
                Put_Line("Mauvaise réponse.");
                false_nbr:=false_nbr +1;
            end if;
            New_line;
        end loop;
        case false_nbr is --Selon le nombre de réponses fausses
            when 0 => 
                Put_Line("Excellent! Aucune erreur.");
            when 1 => 
                Put_Line("Une seule erreur. Très Bien.");
            when 10 => 
                Put_Line("Tout est faux. Volontaire?");
            when 5..9 => 
                Put_Line("Seulement " & Integer'Image(10-false_nbr) & " bonnes réponses. Il faut apprendre la table de " & Integer'Image(table) & " !"); 
                -- 10 - false_nbr = le nombre de bonnes réponses
            when 2..4 => 
                Put_Line(Integer'Image(false_nbr) & " erreurs. Il faut travailler la table de " &  Integer'Image(table));
            when others => 
                Null;
        end case;
        New_line;
        Avg_Delay:=Sum_Delay/10.0; --Calcul du temps moyen de réponse pour la table courante
        if Max_Delay>Avg_Delay+1.0 then ----Si supérieur au temps moyen spécifique à la table courante + 1 seconde
            Put_Line("Des hésitations sur la table de " & Integer'Image(Delayed_Rhs) & " : " & Duration'Image(Max_Delay) & " secondes contre " & Duration'Image(Avg_Delay) & " en moyenne. Il faut certainement la réviser.");
        else
            Null;
        end if;
        New_line;
        Put("Voulez-vous réviser une autre table? (Oui/Non): ");
        Get(choix);
        New_line;
        exit when choix="Non" or choix="non" or choix="NON";    --Sortira automatiquement si l'utilisateur répond par non
    end loop;
    New_line;    
    Put_Line("Vous avez choisi de quitter l'application."); --Instruction après la boucle et sera donc exécutée automatiquement après que l'utilisateur choisisse de quitter
end Multiplications;
