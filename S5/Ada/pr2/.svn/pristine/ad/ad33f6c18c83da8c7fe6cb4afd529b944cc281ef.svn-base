with Ensembles_Chainage;

procedure Test_Ensembles_Chainage is

    package Ensemble_Entier is
        new Ensembles_Chainage (T_Element => Integer);
    use Ensemble_Entier;
    
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
    
    procedure Tester_Destruction is
        Ens:T_Ensemble;
        begin
            Init(Ens);
            Add_Element(69, Ens);
            Add_Element(5, Ens);
            Add_Element(-4, Ens);
            Add_Element(5, Ens);
            Destruct(Ens);
            pragma Assert(Est_Vide(Ens));
        end Tester_Destruction;
                   
begin
	Tester_Ensemble_Entier;
	Tester_Est_Vide;
	Tester_Destruction;
end Test_Ensembles_Chainage;
