with Ada.Text_IO; use Ada.Text_IO;
with Ada.Exceptions;

-- Lecteurs concurrents, approche FIFO
package body LR.Synchro.FIFO is
   
   function Nom_Strategie return String is
   begin
      return "Stratégie FIFO";
   end Nom_Strategie;
   
   task LectRedTask is
      entry Demander(wantsToRead : boolean);
      entry Terminer_Lecture;
      entry Terminer_Ecriture;
   end LectRedTask;

   task body LectRedTask is
      isWriting : boolean := false;
      nbrCurrentReaders : Integer := 0;

   begin
      loop
        select
            when not isWriting =>
                accept Demander(wantsToRead : boolean) do
                    if wantsToRead then
                        nbrCurrentReaders := nbrCurrentReaders + 1;
                    else 
                        while nbrCurrentReaders > 0 loop
                            accept Terminer_Lecture;
                            nbrCurrentReaders := nbrCurrentReaders - 1;
                        end loop;
                        isWriting := true;
                    end if;
                end Demander;
                
            or when isWriting =>
                accept Terminer_Ecriture;
                isWriting := false;

            or when nbrCurrentReaders > 0 =>
                accept Terminer_Lecture;
                nbrCurrentReaders := nbrCurrentReaders - 1;

            or
                terminate;
         end select;
      end loop;
   exception
      when Error: others =>
         Put_Line("**** LectRedTask: exception: " & Ada.Exceptions.Exception_Information(Error));
   end LectRedTask;

   procedure Demander_Lecture is
   begin
      LectRedTask.Demander(true);
   end Demander_Lecture;

   procedure Demander_Ecriture is
   begin
      LectRedTask.Demander(false);
   end Demander_Ecriture;

   procedure Terminer_Lecture is
   begin
      LectRedTask.Terminer_Lecture;
   end Terminer_Lecture;

   procedure Terminer_Ecriture is
   begin
      LectRedTask.Terminer_Ecriture;
   end Terminer_Ecriture;

end LR.Synchro.FIFO;
