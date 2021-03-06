with Ada.Text_IO;       use Ada.Text_IO;
with Vecteurs_Creux;    use Vecteurs_Creux;

-- Exemple d'utilisation des vecteurs creux.
procedure Exemple_Vecteurs_Creux is

	V1 : T_Vecteur_Creux;
	V2 : T_Vecteur_Creux;
	VC1 : T_Vecteur_Creux;
begin
	Put_Line ("Début du scénario");
    Initialiser(V1);
    Put_Line("Affichage du premier Vecteur:");    
    Afficher(V1);
    New_Line;
    Put_Line("Le vecteur est vide? : " & Boolean'Image(Est_Nul(V1)));
    New_Line;
    Modifier(V1,1,69.0);
    Modifier(V1,2,5.0);
    Modifier(V1,3,47.75);
    Modifier(V1,18,0.0);
    Put_Line("Affichage du premier Vecteur:");   
    Afficher(V1);
    New_Line;
    New_Line;
    Put_Line("La 18ème composante du vecteur est: " & Float'Image(Composante_Recursif(V1,18)) & "  (Version Récursive)");
    Put_Line("La 2ème composante du vecteur est: " & Float'Image(Composante_Iteratif(V1,2)) & "  (Version Itérative)");
    Initialiser(V2);
    Modifier(V2,1,-69.0);
    Modifier(V2,2,-5.0);
    Modifier(V2,3,-47.75);   
    Modifier(V2,4,6003.0);
    Put_Line("Affichage du deuxième Vecteur:");
    Afficher(V2);
    New_Line;
    New_Line;
    Put_Line("Les deux vecteurs sont-il égaux? : " & Boolean'Image(Sont_Egaux_Recursif(V1,V2)) & "  (Version Récursive)" );
    Put_Line("Les deux vecteurs sont-il égaux? : " & Boolean'Image(Sont_Egaux_Iteratif(V1,V2)) & "  (Version Itérative)" );   
    New_Line;
    Put_Line("Affichage du premier Vecteur:");   
    Afficher(V1);
    New_Line;
    Put_Line("Affichage du deuxième Vecteur:");
    Afficher(V2);
    New_Line;
    New_Line;
    Additionner(V1,V2);
    Put_Line("V1 et V2 additionnés dans V1");
    Put_Line("Affichage du premier Vecteur:");      
    Afficher(V1);
    New_Line;
    New_Line;
    Detruire(V1);
    Initialiser(V1);
    Modifier(V1,1,69.0);
    Modifier(V1,2,5.0);
    Modifier(V1,3,47.75);
    Modifier(V1,18,0.0);
    Put_Line("Affichage du premier Vecteur:");   
    Afficher(V1);
    New_Line; 
    Put_Line("Affichage du deuxième Vecteur:");
    Afficher(V2);
    New_Line;
    New_Line;
    Put_Line("La norme au carré de V1 est: " & Float'Image(Norme2(V1)));
    Put_Line("Affichage du premier Vecteur:");   
    Afficher(V1);
    New_Line; 
    Put_Line("Affichage du deuxième Vecteur:");
    New_Line;
    Put_Line("Le produit scalaire de V1 est V2 est:" & Float'Image(Produit_Scalaire(V1,V2)));
    Put_Line("Affichage du premier Vecteur:");   
    Afficher(V1);
    New_Line; 
    Put_Line("Affichage du deuxième Vecteur:");
    Afficher(V2);
    New_Line;
    Detruire(V1);
    Detruire(V2);
    New_Line;
    Put_Line("Affichage du premier Vecteur:");   
    Afficher(V1);
    New_Line; 
    Put_Line("Affichage du deuxième Vecteur:");
    Afficher(V2);
    New_Line;
    Put_Line("Vecteurs détruits");
	Put_Line ("Fin du scénario");
end Exemple_Vecteurs_Creux;
