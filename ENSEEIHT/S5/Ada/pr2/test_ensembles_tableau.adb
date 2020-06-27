with Ensembles_Tableau;

procedure Test_Ensembles_Tableau is

    package Ensemble_Entier_20 is
        new Ensembles_Tableau (Capacite => 20, T_Element => Integer);
    use Ensemble_Entier_20;
    
    procedure Tester_Ensemble_Entier is
        Ens: T_Ensemble;
        begin
            Init(Ens);
            Add_Element(69, Ens);
            Add_Element(5, Ens);
            Add_Element(-4, Ens);
            Add_Element(5, Ens);
            pragma Assert(Existe_dans_Ensemble(69,Ens));
            pragma Assert(Taille_Ensemble(Ens)=3);
            Del_Element(5,Ens);
            pragma Assert(not Existe_dans_Ensemble(5,Ens));
            pragma Assert(Taille_Ensemble(Ens)=2);
        end Tester_Ensemble_Entier;
    
    procedure Tester_Est_Vide is
        Ens: T_Ensemble;
        begin
            Init(Ens);
            pragma Assert(Est_Vide(Ens));
            Add_Element(2,Ens);
            pragma Assert(not Est_Vide(Ens));
        end Tester_Est_Vide;
  
begin
	Tester_Ensemble_Entier;
	Tester_Est_Vide;
end Test_Ensembles_Tableau;
