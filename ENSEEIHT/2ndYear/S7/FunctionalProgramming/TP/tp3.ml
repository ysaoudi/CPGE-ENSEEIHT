(*** Combinaisons d'une liste ***)

(* CONTRAT 
combinaisons int list -> int -> int list list
Fonction qui retourne la liste des toutes les combinaisons possibles de k éléments dans une liste l
Paramètre l : int list : La liste dont on va extraire les combinaisons possibles de k éléments
Paramètre k : int : Le nombre d'éléments dans les combinaisons possibles à extraire de l
Résultat : int list list : La liste de toutes les combinaisons possibles de k éléments dans l
Préconditions: k >= 0 && k <= taille(l)
*)
let rec combinaisons l k =
    if k <= 0 
        then [[]]
    else 
        match l with
            | [] -> []
            | t :: q ->
                let combinaisonsContenantLaTete = List.map (fun l -> t :: l) (combinaisons q (k - 1)) in
                let combinaisonsSansLaTete = combinaisons q k in combinaisonsContenantLaTete@combinaisonsSansLaTete;;

(* TESTS *)
let%test _ = combinaisons [1;2;3;4] (-99) = [[]]
let%test _ = combinaisons [1;2;3;4] 0 = [[]]
let%test _ = combinaisons [1;2;3;4] 1 = [[1];[2];[3];[4]]
let%test _ = combinaisons [1;2;3;4] 2 = [[1; 2]; [1; 3]; [1; 4]; [2; 3]; [2; 4]; [3; 4]]
let%test _ = combinaisons [1;2;3;4] 3 = [[1;2;3]; [1;2;4]; [1;3;4]; [2;3;4]]
let%test _ = combinaisons [1;2;3;4] 4 = [[1;2;3;4]]
let%test _ = combinaisons [1;2;3;4] 99 = []