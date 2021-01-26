(******* TRIS ******)

(** Tri par insertion **)

(*CONTRAT
Fonction qui ajoute un élément dans une liste triée, selon un ordre donné
Type : ('a->'a->bool)->'a->'a list -> 'a list
Paramètre : ordre  ('a->'a->bool), un ordre sur les éléments de la liste
Paramètre : elt, l'élement à ajouter
Paramètre : l, la liste triée dans laquelle ajouter elt
Résultat : une liste triée avec les éléments de l, plus elt
*)
let rec insert ordre elt l = 
    if l == [] then
        [elt]
    else if (ordre elt (List.hd l)) then
        elt::l
    else
        List.hd l::insert ordre elt (List.tl l)

(* TESTS *)
let%test _ = insert (fun x y -> x<y) 3 []=[3]
let%test _ = insert (fun x y -> x<y) 3 [2;4;5]=[2;3;4;5]
let%test _ = insert (fun x y -> x > y) 6 [3;2;1]=[6;3;2;1]
let%test _ = insert (fun x y -> x<y) 2 [1;2;4;5] = [1; 2; 2; 4; 5]
let%test _ = insert (fun x y -> x<y) 1 [1;2;4;5] = [1; 1; 2; 4; 5]
let%test _ = insert (fun x y -> x<y) (-1) [1;2;4;5] = [-1; 1; 2; 4; 5]
let%test _ = insert (fun x y -> x<y) 5555 [1;2;4;5] = [1; 2; 4; 5; 555]

(*CONTRAT
Fonction qui trie une liste, selon un ordre donné
Type : ('a->'a->bool)->'a list -> 'a list
Paramètre : ordre  ('a->'a->bool), un ordre sur les éléments de la liste
Paramètre : l, la liste à trier
Résultat : une liste triée avec les éléments de l
*)
let rec tri_insertion ordre l = 
    match l with
    | [] -> []
    | t::q -> insert ordre t (tri_insertion ordre q)

(* TESTS *)
let%test _ = tri_insertion (fun x y -> x<y) [] =[]
let%test _ = tri_insertion (fun x y -> x<y) [4;2;4;3;1] =[1;2;3;4;4]
let%test _ = tri_insertion (fun x y -> x > y) [4;7;2;4;1;2;2;7]=[7;7;4;4;2;2;2;1]



(** Tri fusion **)

(* CONTRAT
Fonction qui décompose une liste en deux listes de tailles égales à plus ou moins un élément
Paramètre : l, la liste à couper en deux
Retour : deux listes
*)
let rec scinde l =  
    match l with
    | [] -> [],[]
    | x::[] -> [x],[] (*Car la taille de la première liste est toujours >= celle de la seconde, on n'arrivera jamais au cas []::y *)
    | x::y::q ->
        let (a, b) = scinde q in
            x::a, y::b
  

(* TESTS *)
(* Peuvent être modifiés selon l'algorithme choisi *)
let%test _ = scinde [1;2;3;4] = ([1;3],[2;4])
let%test _ = scinde [1;2;3] = ([1;3],[2])
let%test _ = scinde [1] = ([1],[])
let%test _ = scinde [] = ([],[])


(* Fusionne deux listes triées pour en faire une seule triée
Paramètre : ordre  ('a->'a->bool), un ordre sur les éléments de la liste
Paramètre : l1 et l2, les deux listes triées
Résultat : une liste triée avec les éléments de l1 et l2
*)
let rec fusionne ordre l1 l2 = 
    match (l1, l2) with
    | ([],[]) -> []
    | (_, []) -> l1
    | ([], _) -> l2
    | (t1::q1, t2::q2) -> if ordre t1 t2 then (t1::(fusionne ordre q1 l2)) else (t2::(fusionne ordre l1 q2))


(*TESTS*)
let%test _ = fusionne (fun x y -> x<y) [1;2;4;5;6] [3;4] = [1;2;3;4;4;5;6]
let%test _ = fusionne (fun x y -> x<y) [1;2;4] [3;4] = [1;2;3;4;4]
let%test _ = fusionne (fun x y -> x<y) [1;2;4] [3;4;8;9;10] = [1;2;3;4;4;8;9;10]
let%test _ = fusionne (fun x y -> x<y) [] [] = []
let%test _ = fusionne (fun x y -> x<y) [1] [] = [1]
let%test _ = fusionne (fun x y -> x<y) [] [1] = [1]
let%test _ = fusionne (fun x y -> x<y) [1] [2] = [1;2]
let%test _ = fusionne (fun x y -> x>y) [1] [2] = [2;1]

(* CONTRAT
Fonction qui trie une liste, selon un ordre donné
Type : ('a->'a->bool)->'a list -> 'a list
Paramètre : ordre  ('a->'a->bool), un ordre sur les éléments de la liste
Paramètre : l, la liste à trier
Résultat : une liste triée avec les éléments de l
*)
let rec tri_fusion ordre l = 
    match l with
    | [] -> l 
    | _::[] -> l
    | _::_ -> let (l1, l2) = scinde l in  fusionne ordre (tri_fusion ordre l1) (tri_fusion ordre l2)


(* TESTS *)
let%test _ = tri_fusion (fun x y -> x<y) [] =[]
let%test _ = tri_fusion (fun x y -> x<y) [4;2;4;3;1] =[1;2;3;4;4]
let%test _ = tri_fusion (fun x y -> x > y) [4;7;2;4;1;2;2;7]=[7;7;4;4;2;2;2;1]

