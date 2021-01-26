with Ada.Text_IO;
use Ada.Text_IO;
with Ada.Integer_Text_IO;
use Ada.Integer_Text_IO;

-- Objectif : Afficher un tableau trié suivant le principe du tri par sélection.

procedure Tri_Selection is

    CAPACITE: constant Integer := 10;   -- la capacité du tableau

    type Tableau_Entier is array (1..CAPACITE) of Integer;

    type Tableau is
        record
            Elements : Tableau_Entier;
            Taille   : Integer;         --{ Taille in [0..CAPACITE] }
        end record;


    -- Objectif : Afficher le tableau Tab.
    -- Paramètres :
    --     Tab : le tableau à afficher
    -- Nécessite : ---
    -- Assure : Le tableau est affiché.
    procedure Afficher (Tab : in Tableau) is
    begin
        Put ("[");
        if Tab.Taille > 0 then
            -- Afficher le premier élément
            Put (Tab.Elements (1), 1);

            -- Afficher les autres éléments
            for Indice in 2..Tab.Taille loop
                Put (", ");
                Put (Tab.Elements (Indice), 1);
            end loop;
        end if;
        Put ("]");
    end Afficher;
    --R0: Trier un vecteur de N entiers relatifs par sélection
    --Tests: [8,2,9,5,1,7] => [1,2,5,7,8,9]
    --      [-1,2,3,4] => [-1,2,3,4]
    --      [9,7,2,1,-5] => [-5,1,2,7,9]

    --R1: Comment R0?  
    --      idx_min <- 1  
    --      Pour i de 1 à N-1 faire
    --          min <- Tab.Elements(i)
    --          bool <- False {si la valeur de min a changé pendant la 2ème boucle}
    --          Prendre le plus petit élément du tableau Tab.Elements(i..N)
    --          Le permuter avec l'élément Tab.Elements(i)
    --      fin Pour
    
    --R2: Comment prendre le plus petit élément du tableau Tab.Elements(i..N)?
    --          Pour j de i à N-1 faire
    --              if min>Tab.Elements(j+1) alors
    --                  min <- Tab.Elements(j+1)
    --                  idx_min <- j+1
    --                  bool <- True  {la valeur de min a effectivement été modifiée} 
    --              sinon
    --                  Rien
    --              fin si
    --          fin Pour
    
    --R3: Comment permuter le plus petit éléement de Tab.Elements(i..N) avec l'élément Tab.Elements(i)?
    --          Si bool alors {Si la valeur de min a été modifiée}
    --              tmp <- Tab.Elements(i)
    --              Tab.Elements(i) <- min
    --              Tab.Elements(idx_min) <- tmp
    --           Sinon
    --              Rien
    
    --R4: Programme final     
    procedure Tri_Select(Tab: in out Tableau) with
        Post => Tab.Taille=Tab.Taille'Old
    is        
        min : Integer;      --l'élement le plus petit des sous-tableaux
        idx_min: Integer;   --l'indice de cet élément
        tmp: Integer;       --valeur temporaire pour permuter deux éléments
        bool: Boolean;      --si la valeur de min a changé pendant la 2ème boucle
    begin
        idx_min:=1;                
        for i in 1..Tab.Taille-1 loop
            min:= Tab.Elements(i);
            bool:= False;
            for j in i..Tab.Taille-1 loop --Recherche de l'élément le plus petit dans Tab.Elements(i..N-1)
                if min>Tab.Elements(j+1) then
                    min := Tab.Elements(j+1);
                    idx_min := j+1;
                    bool := True; --La valeur de min a été modifiée
                else
                    Null;
                end if;
            end loop;
            if bool then            -- Si la valeur de min a été modifiée 
                tmp := Tab.Elements(i);
                Tab.Elements(i) := min;
                Tab.Elements(idx_min) := tmp;
                --PERMUTATION DES DEUX ELEMENTS
            else
                Null;
            end if;
        end loop;
    end Tri_Select;
    
    --Sous-programme qui calculera le nombre d'occurences d'un entier dans un tableau    
    function Occurence(int: in integer; Tab: in Tableau) return Integer is
    occ:Integer:=0;  
    begin
        for i in 1..Tab.Taille loop
            if Tab.Elements(i)=int then --si on trouve une occurence
                occ:= occ +1;
            else
                Null;
            end if;
        end loop;
        return occ;
    end Occurence;
    

    --Sous-programme qui vérifiera que le tri par sélection est correct
    function Verification_Tri_Select(Tab: in out Tableau) return Boolean is
    Tab_ini: Tableau := Tab;    
    begin
        Tri_Select(Tab);
        if Tab_ini.Taille/=Tab.Taille then --Si la taille du tableau initial et celle du tableau est triée sont différentes
            return False;
        else
            Null;
        end if;               
        for i in 1..Tab.Taille-1 loop    
            if Occurence(Tab.Elements(i),Tab)/=Occurence(Tab.Elements(i), Tab_ini) then --Si le nombre d'occurences de chaque élément du tableau trié est différent de celui du même élément dans le tableau initial
                return False;             
            elsif Tab.Elements(i)>Tab.Elements(i+1) then --Si le tableau n'est pas trié dans l'ordre croissant
                return False;
            else
                Null;
            end if;
        end loop;
        return True; --Ne sera retourné que si toutes les conditions précédentes sont fausse, i.e., si le tableau est bien trié.
    end Verification_Tri_Select;

    --Test du tri par sélection en utilisant les exemples de l'exercice 3
    procedure Test_Tri_Select is
    tab1: Tableau := ( (1,3,4,2,others=>0),4);
    tab2: Tableau := ( (4,3,2,1,others=>0),4);
    tab3: Tableau := ( (-5,3,8,1,-25,0,8,1,1,1),10);
    begin 
        pragma Assert(Verification_Tri_Select(tab1));
        pragma Assert(Verification_Tri_Select(tab2));
        pragma Assert(Verification_Tri_Select(tab3));
    end Test_Tri_Select;



Tab1 : Tableau; 
begin
    -- Initialiser le tableau
    Tab1 := ( (1, 3, 4, 2, others => 0), 4);
    -- Trier le tableau
    Tri_Select(Tab1); --Verification du tri des tableaux donnés en exercice 3
    -- Afficher le tableau
    Afficher (Tab1);
    New_Line;
    Test_Tri_Select;
end Tri_Selection;
