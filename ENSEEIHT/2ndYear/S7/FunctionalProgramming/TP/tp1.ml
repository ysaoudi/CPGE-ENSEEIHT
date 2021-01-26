(* Exercice 5 *)
(* PGCD -> pgcd.ml *)
(* pgcd : int -> int -> int *)
(* renvoie le pgcd de deux entiers *)
(* x : le premier netier *)
(* y : le deuxième entier *)
(* préconditions :  x > 0 and y > 0 *)
let rec pgcd x y =
  let estNegatif a = a <= 0 in
  if estNegatif x || estNegatif y then failwith "Arguments should be stricly greater than 0!" 
  else
  let x_y = x-y and y_x = y-x in
  if x = y then x
  else if x > y then pgcd x_y y
  else pgcd x y_x

let%test _ = pgcd 123 36 = 3
let%test _ = pgcd 12 30 = 6
let%test _ = pgcd 60 36 = 12
let%test _ = pgcd 2 2 = 2