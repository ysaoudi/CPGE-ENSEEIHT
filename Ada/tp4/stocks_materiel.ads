
-- Auteur: SAOUDI Younes
-- Gérer un stock de matériel informatique.

package Stocks_Materiel is


    CAPACITE : constant Integer := 10;      -- nombre maximum de matériels dans un stock

    type T_Nature is (UNITE_CENTRALE, DISQUE, ECRAN, CLAVIER, IMPRIMANTE);


    type T_Stock is limited private;


    -- Créer un stock vide.
    --
    -- paramètres
    --     Stock : le stock à créer
    --
    -- Assure
    --     Nb_Materiels (Stock) = 0
    --
    procedure Creer (Stock : out T_Stock) with
        Post => Nb_Materiels (Stock) = 0;


    -- Obtenir le nombre de matériels dans le stock Stock
    --
    -- Paramètres
    --    Stock : le stock dont ont veut obtenir la taille
    --
    -- Nécessite
    --     Vrai
    --
    -- Assure
    --     Résultat >= 0 Et Résultat <= CAPACITE
    --
    function Nb_Materiels (Stock: in T_Stock) return Integer with
        Post => Nb_Materiels'Result >= 0 and Nb_Materiels'Result <= CAPACITE;


    -- Enregistrer un nouveau métériel dans le stock.  Il est en
    -- fonctionnement.  Le stock ne doit pas être plein.
    -- 
    -- Paramètres
    --    Stock : le stock à compléter
    --    Numero_Serie : le numéro de série du nouveau matériel
    --    Nature       : la nature du nouveau matériel
    --    Annee_Achat  : l'année d'achat du nouveau matériel
    -- 
    -- Nécessite
    --    Nb_Materiels (Stock) < CAPACITE
    -- 
    -- Assure
    --    Nouveau matériel ajouté
    --    Nb_Materiels (Stock) = Nb_Materiels (Stock)'Avant + 1
    procedure Enregistrer (
            Stock        : in out T_Stock;
            Numero_Serie : in     Integer;
            Nature       : in     T_Nature;
            Annee_Achat  : in     Integer
        ) with
            Pre => Nb_Materiels (Stock) < CAPACITE,
            Post => Nb_Materiels (Stock) = Nb_Materiels (Stock)'Old + 1;

    function Nb_Materiels_False(Stock: in T_Stock) return Integer with
        Pre => Nb_Materiels(Stock)<CAPACITE;
    function Hardware_Index(Stock:in T_Stock; N_Serie: in Integer) return Integer;
     
    procedure Update_State(Stock: in out T_Stock; N_Serie: in Integer; Etat: in Boolean);
    
    procedure Delete_Hardware(Stock:in out T_Stock; N_Serie: in Integer);
    
    procedure Show_All_Hardware(Stock: in T_Stock); 
    procedure Delete_All_False(Stock: in out T_Stock) with
    Post=> Nb_Materiels_False(Stock)=0;




private
    type T_Hardware is
        record
            Serial_N : integer;
            Nature : T_Nature;
            Purchase_Year: integer;
            State: Boolean;
                --Invariant:
                    --Purchase_Year >= 1
        end record;
    type T_Warehouse is
        array (1..CAPACITE) of T_Hardware;
    
    type T_Stock is
        record
            Warehouse: T_Warehouse;
            Size: integer;
            --Invariant:
                -- Size <= CAPACITE
        end record;                
                





end Stocks_Materiel;
