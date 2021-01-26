module type Iter =
sig
	type 'a t
	val vide : 'a t
	val cons : 'a -> 'a t -> 'a t
	val uncons : 'a t -> ('a * 'a t) option
	val unfold : ('s -> ('a * 's) option) -> 's -> 'a t
	val filter : ('a -> bool) -> 'a t -> 'a t
	val append : 'a t -> 'a t -> 'a t                        
	val constant : 'a -> 'a t
	val map : ('a -> 'b) -> 'a t -> 'b t
	val map2 : ('a -> 'b -> 'c) -> 'a t -> 'b t -> 'c t
	val apply : ('a -> 'b) t -> 'a t -> 'b t
	val isEmpty : 'a t -> bool
end

type 'a flux = Tick of ('a * 'a flux) option Lazy.t;;

module Flux : Iter with type 'a t = 'a flux =
	struct
		type 'a t = 'a flux = Tick of ('a * 'a t) option Lazy.t;;

		let vide = Tick (lazy None);;

		let cons t q = Tick (lazy (Some (t, q)));;

		let uncons (Tick flux) = Lazy.force flux;;
 
		let rec apply f x =
			Tick (lazy (
			match uncons f, uncons x with
			| None         , _             -> None
			| _            , None          -> None
			| Some (tf, qf), Some (tx, qx) -> Some (tf tx, apply qf qx)));;

		let rec unfold f e =
			Tick (lazy (
			match f e with
			| None         -> None
			| Some (t, e') -> Some (t, unfold f e')));;

		let rec filter p flux =
			Tick (lazy (
			match uncons flux with
			| None        -> None
			| Some (t, q) -> if p t then Some (t, filter p q)
											 else uncons (filter p q)));;
		
		let rec append flux1 flux2 =
			Tick (lazy (
			match uncons flux1 with
			| None          -> uncons flux2
			| Some (t1, q1) -> Some (t1, append q1 flux2)));;
		
		let constant c = unfold (fun () -> Some (c, ())) ();;
		(* implantation rapide mais inefficace de map *)
		let map f i = apply (constant f) i;;

		let map2 f i1 i2 = apply (apply (constant f) i1) i2;;

		let isEmpty stream =
			match (uncons stream) with
				| None -> true
				| _ -> false;;
	end



let prompt0 = Delimcc.new_prompt();;
type result = | Invalid | Valid ;;

(*
	Interrupt the execution and makes it valid.
	Signature     : miracle : unit −> 'a = <fun>.
	Parameter(s)  : None.
	Precondtion   : None.
	Postcondtion  : None.
	Result        : None.
*)
let miracle () = Delimcc.shift prompt0 (fun x -> x Valid);;

(*
	Interrupts the execution and makes it invalid.
	Signature     : failure : unit −> 'a = <fun>.
	Parameter(s)  : None.
	Precondtion   : None.
	Postcondtion  : None.
	Result        : None.
*)
let failure () = Delimcc.shift prompt0 (fun x -> x Invalid);;

(*
	Continues the execution 
	Signature     : continue : unit −> 'a = <fun>.
	Parameter(s)  : None.
	Precondtion   : None.
	Postcondtion  : None.
	Result        : None.
*)
let continue () = Delimcc.shift prompt0 (fun x -> x ());;

(*
	Filters and continues just the executions that check the given predicate in
	parameter. The other executions should be stopped and declared valid.
	Signature     : assumption : (unit −> bool) −> unit = <fun>.
	Parameter(s)  : The predicate to check.
	Precondtion   : Check if the predicate is valid.
	Postcondtion  : None.
	Result        : None.
*)
let assumption f = match f() with
	| false -> let _ = miracle in ()
	| true ->  continue();;


(*
	Filters and continues just the executions that check the given predicate in
	parameter. The other executions should be stopped and declared invalid.
	Signature     : assertion : (unit −> bool) −> unit = <fun>.
	Parameter(s)  : The predicate to check.
	Precondtion   : None.
	Postcondtion  : Check if the predicate is valid.
	Result        : None.
*)
let assertion f =  match f() with 
	| false -> let _ = failure in ()
	| true ->  continue();;


(*
	Forks the execution to 2 versions. In each of these versions, the function
	returns a different boolean. The parent execution is valid if only the child
	executions are valid.
	Signature     : forall_bool : unit −> bool = <fun>.
	Parameter(s)  : None.
	Precondtion   : None.
	Postcondtion  : None.
	Result        : Return true if the child executions are valid.
*)

let forall_bool () = Delimcc.shift prompt0 (fun x -> (x false) && (x true))
;;


(*
	Forks the execution to 2 versions. In each of these versions, the function
	returns a different boolean. The parent execution is valid if at least one of
	the child executions is valid.
	Signature     : forsome_bool : unit −> bool = <fun>.
	Parameter(s)  : None.
	Precondtion   : None.
	Postcondtion  : None.
	Result        : Returns true if at least one of the child executions is valid.
*)
let forsome_bool () = Delimcc.shift prompt0 (fun x -> (x false) || (x true))
;;

(*
	Generalizes forall_bool and forks the current execution in as many versions
	as there are elements in the stream.
	Signature     : forall : ’a Flux. t −> ’a = <fun>.
	Parameter(s)  : ’a Flux. t.
	Precondtion   : None.
	Postcondtion  : None.
	Result        : return 'a.
*)
let rec forall_subFunction stream_2 = 
		match (Flux.uncons stream_2) with
			| None -> 
				failwith "Empty Stream"
			| Some(h, t) when Flux.isEmpty t ->
					(h, forall_bool())
			| Some(h, t) when  forall_bool() && snd(forall_subFunction t) ->
					(h, true)
			| Some(h, _) ->
					(h, false);;

let rec forall stream = 
	let (h, bool) = forall_subFunction stream in
		if bool = true then
				h
		else 
			match (Flux.uncons stream) with
				| None -> failwith "Empty Stream"
				| Some(h, t) when Flux.isEmpty t -> h
				| Some(h, t) -> forall t				
;;

(*
	Generalizes forsome_bool and forks the current execution in as many versions
	as there are elements in the stream.
	Signature     : forsome : ’a Flux. t −> ’a = <fun>.
	Parameter(s)  : ’a Flux. t.
	Precondtion   : None.
	Postcondtion  : None.
	Result        : return 'a.
*)
let rec forsome_subFunction stream2 = 
		match (Flux.uncons stream2) with
			| None -> 
				failwith "Empty Stream"
			| Some(h, t) when Flux.isEmpty t ->
					(h, forall_bool())
			| Some(h, t) when  snd(forall_subFunction t) && forall_bool() ->
					(h, true)
			| Some(h, _) ->
					(h, false);;

let rec forsome stream =
	let (h, bool) = forsome_subFunction stream in
		if bool = true then 
			h
		else 
			match (Flux.uncons stream) with
				| None -> failwith "Empty Stream"
				| Some(h, t) when Flux.isEmpty t -> h
				| Some(h, t) -> forsome t
;;
(*
	Forks the current execution in as many versions as there are elements in the
	stream. The parent execution is valid if at least n child executions are
	valid. We have forsome = foratleast 1.
	Signature     : foratleast : int −> ’a Flux. t −> ’a = <fun>.
	Parameter(s)  : int −> ’a Flux. t.
	Precondtion   : None.
	Postcondtion  : None.
	Result        : return 'a.
*)

let rec foratleast n stream = 
	match (n, Flux.uncons stream) with
		| (_, None) | (0, _) ->
			failwith "Incorrect foratleast arguments"
		| (1, _) -> 
			forsome stream 
		| (_, Some(h, t)) ->
			if forsome_bool() then
				foratleast (n - 1) t
			else
				foratleast n t
			
(*
	Executes a program instrumented with the primitives above.
	The result is a boolean representing the validity of the execution and can be used with let %test
	Signature     : check : ( unit −> unit) −> bool.
	Parameter(s)  : function f : unit -> unit.
	Precondtion   : None.
	Postcondtion  : None.
	Result        : Return true if the execution is valid, otherwise false.
*)
let check func = Delimcc.push_prompt prompt0 (fun () -> forall_bool( func() ));;

(* ------------------------------------------EXTENSION------------------------------------------ *)

(*forall_length : int Flux. t −> (unit −> ’a) −> ’a list *)
let rec forall_length lengths f =
	match Flux.uncons lengths with
	| None ->failwith "Empty forall_length stream" 
	| Some(h, t) when h = 0 -> forall_length t f
	| Some(h, t) when Flux.isEmpty t -> 
		if forall_bool() then 
			[f()]
		else
			[]
	| Some(h, t) -> 
		if forall_bool() then
			f()::forall_length t f
		else 
			[]
;;


(*forsome_length : int Flux. t −> (unit −> ’a) −> ’a list *)
let rec forsome_length lengths f =
	match Flux.uncons lengths with
	| None -> failwith "Empty forsome_length stream" 
	| Some(h, t) when h = 0 -> forsome_length t f
	| Some(h, t) when Flux.isEmpty t -> if forsome_bool() then [f()] else []
	| Some(h, t) -> 
		if forsome_bool() then
			f()::forsome_length t f
		else 
			forsome_length t f
;;


(*foratleast_length : int −> int Flux. t −> (unit −> ’a) −> ’a list *)
let rec foratleast_length n stream = 
	match (n, Flux.uncons stream) with
		| (_, None) | (0, _) ->
			failwith "Incorrect foratleast_length arguments"
		| (1, _) -> 
			forsome_length stream 
		| (_, Some(h, t)) ->
			if forsome_bool() then
				foratleast_length (n - 1) t
			else
				foratleast_length n t
;;