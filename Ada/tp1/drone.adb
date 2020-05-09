with Ada.Text_IO; use Ada.Text_IO;
with Ada.Integer_Text_IO; use Ada.Integer_Text_IO;
procedure Drone is
    Altitude: Integer:=0; --L'altitude qui sera affichée
    Choix: Character :='d'; --Les commandes que saisira l'utilisateur
    On: Boolean :=False; --Etat du drône (allumé ou non)
begin
    Altitude:=0; --Drône sur terre
    while Choix /= 'q' loop            -- Affichage continu du Menu de commandes
        Put_Line("Altitude :" & Integer'Image(Altitude));
        Put_Line(    "Que faire ?");
        Put_Line(    "d -- Démarrer ");
        Put_Line(    "m -- Monter");
        Put_Line(    "s -- Descendre");
        Put_Line(    "q -- Quitter");
        Put_Line("Votre choix: ");
        Get(Choix);
        Skip_Line;
        case Choix is
            when 'd' =>  --Quand le drône est démarré
                On:=True;
            when 'm' =>  --Quand l'utilisateur veut que le drône monte
                if On then --Ne monte que si ON
                    Altitude:= Altitude +1; 
                    if Altitude>5 then 
                        Put_Line("Votre drône est maintenant hors de portée. Arrêt de l'application.");
                        Choix:='q';
                    end if; 
                elsif not On then --Si le drône n'est pas démarré
                    Put_Line("Veuillez démarrer votre drône");                     
                else
                    Null;                
                end if;
            when 's' => --Quand l'utilisateur veut qu'il descende
                if On then  
                    if Altitude-1<0 then --Altitude ne peut pas être négative
                        Put_Line("Vous ne pouvez pas descendre davantage");
                    elsif Altitude-1>=0 then
                        Altitude:= Altitude -1; 
                    end if;
                elsif not On then
                    Put_Line("Veuillez démarrer votre drône");
                else
                    Null;
                end if;
            when 'q' => --Quand l'utilisateur veut quitter menu
                Choix:='q';
            when others => --Autres cas
                Null;
        end case;
    end loop;
end Drone;
