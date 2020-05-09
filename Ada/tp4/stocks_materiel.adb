with Ada.Text_IO;          use Ada.Text_IO;
with Ada.Integer_Text_IO;  use Ada.Integer_Text_IO;

-- Auteur: SAOUDI Younes
-- Gérer un stock de matériel informatique.
--
package body Stocks_Materiel is

    procedure Creer (Stock : out T_Stock) is
    begin
        Stock.Size:=0;
    end Creer;


    function Nb_Materiels (Stock: in T_Stock) return Integer is
    begin
        return Stock.Size;
    end;


    procedure Enregistrer (
            Stock        : in out T_Stock;
            Numero_Serie : in     Integer;
            Nature       : in     T_Nature;
            Annee_Achat  : in     Integer
        ) is
    begin
        Stock.Size := Stock.Size+1;
        Stock.Warehouse(Stock.Size).Serial_N:=Numero_Serie;
        Stock.Warehouse(Stock.Size).Nature:=Nature;
        Stock.Warehouse(Stock.Size).Purchase_Year:=Annee_Achat;
        Stock.Warehouse(Stock.Size).State:=True;
    end;
    
    function Hardware_Index(Stock:in T_Stock; N_Serie: in Integer) return Integer is
        j:integer;
        begin
            for i in 1..Stock.Size loop
                if Stock.Warehouse(i).Serial_N=N_Serie then
                    j:= i;
                else
                    Null;
                end if;
            end loop;
            return j;
        end Hardware_Index;
         
    function Nb_Materiels_False(Stock: in T_Stock) return Integer is
        nbr_false: integer;
        begin
            for i in 1..Stock.Size loop
                if not Stock.Warehouse(i).State then
                    nbr_false:= nbr_false + 1;
                else
                    Null;
                end if;
            end loop;
            return nbr_false; 
        end Nb_Materiels_False;
    
        
    procedure Update_State(Stock: in out T_Stock; N_Serie: in Integer; Etat: in Boolean) is
    j: integer;
    begin
        j:=Hardware_Index(Stock,N_Serie);
        Stock.Warehouse(j).State:=Etat;
    end Update_State;  
    procedure Delete_Hardware(Stock:in out T_Stock; N_Serie: in Integer) is
    j: integer;
    begin
        Stock.Size:=Stock.Size -1;
        j:=Hardware_Index(Stock,N_Serie);
        Stock.Warehouse(j).Serial_N:=0;
    end Delete_Hardware;
    
    procedure Show_All_Hardware(Stock: in T_Stock) is
    begin
        for i in 1..Stock.Size loop
           Put("Numéro de série: " & Integer'Image(Stock.Warehouse(i).Serial_N));
           Put(" / Nature du matériel: " & T_Nature'Image(Stock.Warehouse(i).Nature));
           Put(" / Etat du matériel: " & Boolean'Image(Stock.Warehouse(i).State));
           Put(" / Année d'Achat: " & Integer'Image(Stock.Warehouse(i).Purchase_Year) & " /");
           New_Line;
        end loop;
    end Show_All_Hardware;
    
    
    procedure Delete_All_False(Stock: in out T_Stock) is
    begin
        for i in 1..Stock.Size loop
            if Stock.Warehouse(i).State = False then
                Delete_Hardware(Stock,Stock.Warehouse(i).Serial_N);
            else
                Null;
            end if;
        end loop;
    end Delete_All_False;
            


end Stocks_Materiel;
