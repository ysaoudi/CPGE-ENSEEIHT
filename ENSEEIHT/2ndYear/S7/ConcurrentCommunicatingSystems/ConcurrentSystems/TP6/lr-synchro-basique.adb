with Ada.Text_IO; use Ada.Text_IO;
with Ada.Exceptions;

-- Lecteurs concurrents, approche automate. Pas de politique d'accès.
package body LR.Synchro.Basique is
   
   function Nom_Strategie return String is
   begin
      return "Automate, lecteurs concurrents, sans politique d'accès";
   end Nom_Strategie;
   
   task LectRedTask is
      entry Demander_Lecture;
      entry Demander_Ecriture;
      entry Terminer_Lecture;
      entry Terminer_Ecriture;
   end LectRedTask;

   task body LectRedTask is
      type State is (Free, Reading, Writing);
      current_state : State := Free;
      nbrCurrentReaders : Integer := 0;
   begin
      loop
         case current_state is
            when Free =>
                select
                    accept Demander_Lecture;
                    nbrCurrentReaders := nbrCurrentReaders + 1;
                    current_state := Reading;
                or
                    accept Demander_Ecriture;
                    current_state := Writing;
                or
                    terminate;
                end select;    
                 
            when Reading =>
               select
                  accept Demander_Lecture;
                  nbrCurrentReaders := nbrCurrentReaders + 1;
               or 
                  accept Terminer_Lecture;
                  nbrCurrentReaders := nbrCurrentReaders - 1;
               end select;
               
               if nbrCurrentReaders = 0 then
                  current_state := Free;
               end if;

            when Writing =>
               accept Terminer_Ecriture;
               current_state := Free;
         end case;
      end loop;
   exception
      when Error: others =>
         Put_Line("**** LectRedTask: exception: " & Ada.Exceptions.Exception_Information(Error));
   end LectRedTask;

   procedure Demander_Lecture is
   begin
      LectRedTask.Demander_Lecture;
   end Demander_Lecture;

   procedure Demander_Ecriture is
   begin
      LectRedTask.Demander_Ecriture;
   end Demander_Ecriture;

   procedure Terminer_Lecture is
   begin
      LectRedTask.Terminer_Lecture;
   end Terminer_Lecture;

   procedure Terminer_Ecriture is
   begin
      LectRedTask.Terminer_Ecriture;
   end Terminer_Ecriture;

end LR.Synchro.Basique;
