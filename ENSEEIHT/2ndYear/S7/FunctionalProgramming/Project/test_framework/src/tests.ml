open Main

let rec pgcd a b=
  if a >= b then
    if b = 0 then
      a
    else
      pgcd b (a mod b)
  else
    pgcd b a
;;

let premiers_entre_eux a b  =
  if pgcd a b = 1 then
    true
  else
    false;;

let premier n =
  let rec test_local(t,k) =
    if t*t > k then
      true
    else
      if k mod t = 0 then
        false
      else
        test_local(t+1,k)
  in test_local(2,n)
;;

let %test _ =
  let values = Flux.unfold (fun cpt -> if cpt > 20 then None else Some (cpt, cpt+1)) 1 in
  check (fun () ->
  let a = forall values in
  let b = forall values in
  assumption (fun () -> premiers_entre_eux a b );
  let r = pgcd a b in
  assertion (fun () -> r = 1))
;;

let %test _ =
  let values = Flux.unfold (fun cpt -> if cpt > 50 then None else Some (cpt, cpt+1)) 2 in
  check (fun () ->
  let a = forall values in
  let b = forsome values in
  assumption (fun () -> a <> b);
  let r = premier a in
  assertion (fun () -> r || (a mod b = 0)))
;;

let pred x y z = x = 5 * y + 7 * z ;;
let %test _ =
  let interval a b = Flux. unfold (fun cpt ->  if cpt > b then None else Some (cpt, cpt+1)) a in
  check (fun () -> 
    begin
      let x = forall ( interval 10 50) in
      assumption (fun () ->  x mod 2 = 0);
      let y = forsome ( interval 0 20) in
      let z = forsome ( interval 0 20) in
      assertion (fun () ->  pred x y z)
    end
  )
;;

(*∀ a. [|1 , 10|], ∀ b. [|11, 20|] a <> b *)
let %test _ = 
  let values1 = Flux.unfold (fun cpt -> if cpt > 10 then None else Some (cpt, cpt + 1)) 1 in
  let values2 = Flux.unfold (fun cpt -> if cpt > 20 then None else Some (cpt, cpt + 1)) 11 in
  check(fun() -> 
    let a = forall values1 in
    let b = forall values2 in
    assumption (fun () -> a <> b);
    assertion (fun() -> a <> b)
  )
  ;;

(*∀ a,b. [|1 , 50|], a < b => a+1 < b+1 *)
let %test _ =
  let values = Flux.unfold (fun cpt -> if cpt > 50 then None else Some (cpt, cpt + 1)) 1 in
  check(fun() -> 
    let a = forall values in
    let b = forall values in
    assumption (fun () -> a < b);
    assertion (fun() -> (a+1) < (b+1))
  )
  ;;

(*∀ a,. [|1 , 50|], a mod 2 = 0 => a+1 mod 2 = 1 *)
let %test _ =
  let values = Flux.unfold (fun cpt -> if cpt > 50 then None else Some (cpt, cpt + 1)) 1 in
  check(fun() -> 
    let a = forall values in
    assumption (fun () -> (a mod 2) = 0);
    assertion (fun() -> (a + 1 mod 2 = 1))
  )
  ;;

let primeFactors n =
    let rec aux d n =
      if n = 1 then [] else
        if n mod d = 0 then d :: aux d (n / d) else aux (d+1) n
    in aux 2 n;;

(*∀ a,. [|2 , 100|], a not premier => primeFactors(a).length >= 1 *)
let %test _ =
  let values = Flux.unfold (fun cpt -> if cpt > 100 then None else Some (cpt, cpt + 1)) 2 in
  check(fun() -> 
    let a = forall values in
    assumption (fun () -> not(premier a));
    let facteursPremiers = primeFactors a in
    assertion (fun() -> List.length facteursPremiers > 1)
  )
  ;;

(*∀ a,. [|1 , 50|], pgcd a b = 1 => premier_entre_eux a b *)
let %test _ =
  let values = Flux.unfold (fun cpt -> if cpt > 50 then None else Some (cpt, cpt + 1)) 1 in
  check(fun() -> 
    let a = forall values in
    let b = forall values in
    assumption (fun () -> pgcd a b = 1);
    assertion (fun() -> premiers_entre_eux a b)
  )
  ;;

(* Merge Sort *)
let rec merge = function
  | list, []
  | [], list -> list
  | h1::t1, h2::t2 ->
      if h1 <= h2 then
        h1 :: merge (t1, h2::t2)
      else
        h2 :: merge (h1::t1, t2)
and halve = function
  | []
  | [_] as t1 -> t1, []
  | h::t ->
      let t1, t2 = halve t in
        h::t2, t1
and mergesort = function
  | []
  | [_] as list -> list
  | list ->
      let l1, l2 = halve list in
        merge (mergesort l1, mergesort l2)
;;

let %test _=
  let lengths = Flux. unfold (fun cpt ->  if cpt > 5 then None else Some (cpt, cpt+1)) 0 in
  let values = Flux. unfold (fun cpt ->  if cpt > 20 then None else Some (cpt, cpt+1)) 1 in
  check (fun () -> 
  let l = forall_length lengths (fun () ->  forall values ) in
  assertion (fun () ->  List.sort Pervasives.compare l = mergesort l ))
;;

let %test _ =
  let lengths = Flux.unfold (fun cpt -> if cpt > 5 then None else Some (cpt, cpt + 1)) 0 in
  let values = Flux.unfold (fun cpt -> if cpt > 10 then None else Some (cpt, cpt + 1)) 1 in
  check (fun () -> 
  let l = forall_length lengths (fun () ->  forall values ) in
  let x = forall values in
  let l1 = forsome_length lengths (fun () ->  forsome values) in
  let l2 = forsome_length lengths (fun () ->  forsome values) in
  let r = List . mem x l in
  assertion (fun () ->  r = ( l = l1@(x::l2 )))
  );;

