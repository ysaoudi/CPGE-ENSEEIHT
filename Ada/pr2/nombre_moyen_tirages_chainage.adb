with Ensembles_Chainage;
with Alea;
with Ada.Text_IO; use Ada.Text_IO;
with Ada.Float_Text_IO; use Ada.Float_Text_IO;

procedure Nombre_Moyen_Tirage_Chainage is
    package Intervalle_Entier is
        new Alea (1,100);
    use Intervalle_Entier;
    package Ensemble_Chainage is
        new Ensembles_Chainage (T_Element => Integer);
    use Ensemble_Chainage;

    function Tirage return Float is        
        Nbr: Integer;
        Ens: T_Ensemble;
        i:float:=0.0;
        begin
	        Init(Ens);
	        loop
	            Get_Random_Number(Nbr);
	            Add_Element(Nbr,Ens);
	            i:=i+1.0;
	        exit when Taille_Ensemble(Ens)=100; --Puisqu'un élément n'est ajouté que s'il n'existe pas, la taille sera 100 quand tous les éléments de l'intervalle
	        end loop;                           --seront ajoutés à l'ensemble.
	        return i;
	    end Tirage;
s:Float;
begin
    s:=0.0;
    for i in 1..100 loop
        s:= s + Tirage;	 
    end loop;
    s:= s/100.0;
    Put_Line("Le nombre moyen de tirages qu’il faut faire pour obtenir tous les nombres de l'intervalle est: " & Float'Image(s));
end Nombre_Moyen_Tirage_Chainage;

