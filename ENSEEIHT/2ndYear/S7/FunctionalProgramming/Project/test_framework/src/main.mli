(* interfaces des flux utiles pour toute la séance *)

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


(* Module Flux implantant l'interface de flux Iter *)
(* a l'aide d'une structure de donnees paresseuse  *)
type 'a flux = Tick of ('a * 'a flux) option Lazy.t;;
module Flux : Iter with type 'a t = 'a flux;;
type result;;

  (*
    Filters and continues just the executions that check the given predicate in
    parameter. The other executions should be stopped and declared valid.
    Signature     : assumption : (unit −> bool) −> unit = <fun>.
    Parameter(s)  : The predicate to check.
    Precondtion   : Check if the predicate is valid.
    Postcondtion  : None.
    Result        : None.
  *)
  val assumption : (unit -> bool) -> unit

  (*
    Filters and continues just the executions that check the given predicate in
    parameter. The other executions should be stopped and declared invalid.
    Signature     : assertion : (unit −> bool) −> unit = <fun>.
    Parameter(s)  : The predicate to check.
    Precondtion   : None.
    Postcondtion  : Check if the predicate is valid.
    Result        : None.
  *)
  val assertion : (unit -> bool) -> unit

  (*
    Interrupt the execution and makes it valid.
    Signature     : miracle : unit −> 'a = <fun>.
    Parameter(s)  : None.
    Precondtion   : None.
    Postcondtion  : None.
    Result        : None.
  *)
  val miracle : unit -> result

  (*
    Interrupts the execution and makes it invalid.
    Signature     : failure : unit −> 'a = <fun>.
    Parameter(s)  : None.
    Precondtion   : None.
    Postcondtion  : None.
    Result        : None.
  *)
  val failure : unit -> result

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
  val forall_bool : unit -> bool

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
  val forsome_bool : unit -> bool

  (*
    Generalizes forall_bool and forks the current execution in as many versions
    as there are elements in the stream.
    Signature     : forall : 'a Flux. t −> 'a = <fun>.
    Parameter(s)  : 'a Flux. t.
    Precondtion   : None.
    Postcondtion  : None.
    Result        : return 'a.
  *)
  val forall : 'a Flux.t -> 'a

  (*
    Generalizes forsome_bool and forks the current execution in as many versions
    as there are elements in the stream.
    Signature     : forsome : 'a Flux. t −> 'a = <fun>.
    Parameter(s)  : 'a Flux. t.
    Precondtion   : None.
    Postcondtion  : None.
    Result        : return 'a.
  *)
  val forsome : 'a Flux.t -> 'a

  (*
    Forks the current execution in as many versions as there are elements in the
    stream. The parent execution is valid if at least n child executions are
    valid. We have forsome = foratleast 1.
    Signature     : foratleast : int −> 'a Flux. t −> 'a = <fun>.
    Parameter(s)  : int −> 'a Flux. t.
    Precondtion   : None.
    Postcondtion  : None.
    Result        : return 'a.
  *)
  val foratleast : int -> 'a Flux. t -> 'a

  (*
    Executes a program containing the functions above.
    Signature     : forsome : 'a Flux. t −> 'a = <fun>.
    Parameter(s)  : None.
    Precondtion   : None.
    Postcondtion  : None.
    Result        : Return true if the execution is valid, otherwise false.
  *)
  val check : ( unit -> unit) -> bool

  (* Extensions *)
  val forall_length: int Flux.t -> (unit -> 'a) -> 'a list
  val forsome_length : int Flux.t -> (unit -> 'a) -> 'a list
  val foratleast_length : int -> int Flux.t -> (unit -> 'a) -> 'a list