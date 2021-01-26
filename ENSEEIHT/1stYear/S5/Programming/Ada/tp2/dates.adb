with Ada.Text_IO;           use Ada.Text_IO;
with Ada.Integer_Text_IO;   use Ada.Integer_Text_IO;

procedure EXO11_Specifier_Implementer_Et_Tester is
    function Calcul_Jours_Mois(m: in integer;c:in Boolean) return Integer with
            Pre => m>=1 and m<=12,
            Post => Calcul_Jours_Mois'Result>=1 and Calcul_Jours_Mois'Result<=31
    is
        Days: Integer;
    begin
        if m=2 then
            if c then
                Days:=29;
                return Days;
            elsif not c then
                Days:=28;
                return Days;
            else
                Null;
            end if;
        elsif m<8 and m/=2 then
            if m mod 2=1 then
                Days:=31;
                return Days;
            elsif m mod 2=0 then
                Days:=30;
                return Days;
            else
                Null;
            end if;
        elsif m>=8 then
            if m mod 2=0 then
                Days:=31;
                return Days;
            elsif m mod 2=1 then
                Days:=30;
                return Days;
            else
                Null;
            end if;
        else
            Null;
        end if;
    end Calcul_Jours_Mois;

    procedure Tester_Calcul_Jours is
    begin
        pragma Assert(Calcul_Jours_Mois(2,False)=28);
        pragma Assert(Calcul_Jours_Mois(2,True)=29);
        pragma Assert(Calcul_Jours_Mois(11,False)=30);
        pragma Assert(Calcul_Jours_Mois(10,True)=31);
        pragma Assert(Calcul_Jours_Mois(3,False)=31);
        pragma Assert(Calcul_Jours_Mois(6,False)=30);
    end Tester_Calcul_Jours;

    function date_lendemain(j:in integer;m:in integer;a:in integer) return Integer with
            Pre=> j>=1 and j<=31 and m>=1 and m<=12 and a>=1
    is
        V_j: Integer;
        V_m: integer;
        V_a: Integer;
        E_a: Integer:=0;
        E_m: Integer:=0;
        c: boolean;
    begin
        if a mod 4 =0 and a mod 100/=0 then
            c:=True;
        else
            c:=False;
        end if;        
        if j=Calcul_Jours_Mois(m,c) then
            V_j:=1;
            if m=12 then
                V_m:=1;
                V_a:=a+1;
            else
                V_m:=m+1;
                V_a:=a;
            end if;
        else
            V_j:=j+1;
            V_m:=m;
            V_a:=a;
        end if;
        while 10**E_a<V_a loop
            E_a:=E_a +1;
        end loop;
        while 10**E_m<V_m loop
            E_m:=E_m+1;
        end loop;
        if E_m=0 then
            E_m:=1;
        else
            Null;
        end if;
        return V_a+(V_m*10**E_a)+(V_j*10**(E_a+E_m));
    end date_lendemain;
    procedure Tester_date_lendemain is
    begin
        pragma Assert(date_lendemain(18,11,1999)=19111999);
        pragma Assert(date_lendemain(05,01,25)=6125);
        pragma Assert(date_lendemain(31,12,1999)=112000);
        pragma Assert(date_lendemain(31,01,2020)=122020);
        pragma Assert(date_lendemain(28,02,2020)=132020);
        pragma Assert(date_lendemain(28,02,2020)=2922020);
    end Tester_date_lendemain;
    begin
        Tester_date_lendemain;
        Tester_Calcul_Jours;
        Put_Line("Fini.");
    end EXO11_Specifier_Implementer_Et_Tester;
