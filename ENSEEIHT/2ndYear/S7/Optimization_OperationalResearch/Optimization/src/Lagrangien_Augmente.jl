@doc doc"""
Résolution des problèmes de minimisation sous contraintes d'égalités

# Syntaxe
```julia
Lagrangien_Augmente(algo,fonc,contrainte,gradfonc,hessfonc,grad_contrainte,
			hess_contrainte,x0,options)
```

# Entrées
  * **algo** 		   : (String) l'algorithme sans contraintes à utiliser:
    - **"newton"**  : pour l'algorithme de Newton
    - **"cauchy"**  : pour le pas de Cauchy
    - **"gct"**     : pour le gradient conjugué tronqué
  * **fonc** 		   : (Function) la fonction à minimiser
  * **contrainte**	   : (Function) la contrainte [x est dans le domaine des contraintes ssi ``c(x)=0``]
  * **gradfonc**       : (Function) le gradient de la fonction
  * **hessfonc** 	   : (Function) la hessienne de la fonction
  * **grad_contrainte** : (Function) le gradient de la contrainte
  * **hess_contrainte** : (Function) la hessienne de la contrainte
  * **x0** 			   : (Array{Float,1}) la première composante du point de départ du Lagrangien
  * **options**		   : (Array{Float,1})
    1. **epsilon** 	   : utilisé dans les critères d'arrêt
    2. **tol**         : la tolérance utilisée dans les critères d'arrêt
    3. **itermax** 	   : nombre maximal d'itération dans la boucle principale
    4. **lambda0**	   : la deuxième composante du point de départ du Lagrangien
    5. **mu0,tho** 	   : valeurs initiales des variables de l'algorithme

# Sorties
* **xmin**		   : (Array{Float,1}) une approximation de la solution du problème avec contraintes
* **fxmin** 	   : (Float) ``f(x_{min})``
* **flag**		   : (Integer) indicateur du déroulement de l'algorithme
   - **0**    : convergence
   - **1**    : nombre maximal d'itération atteint
   - **(-1)** : une erreur s'est produite
* **niters** 	   : (Integer) nombre d'itérations réalisées

# Exemple d'appel
```julia
using LinearAlgebra
f(x)=100*(x[2]-x[1]^2)^2+(1-x[1])^2
gradf(x)=[-400*x[1]*(x[2]-x[1]^2)-2*(1-x[1]) ; 200*(x[2]-x[1]^2)]
hessf(x)=[-400*(x[2]-3*x[1]^2)+2  -400*x[1];-400*x[1]  200]
algo = "gct" # ou newton|gct
x0 = [1; 0]
options = []
contrainte(x) =  (x[1]^2) + (x[2]^2) -1.5
grad_contrainte(x) = [2*x[1] ;2*x[2]]
hess_contrainte(x) = [2 0;0 2]
output = Lagrangien_Augmente(algo,f,contrainte,gradf,hessf,grad_contrainte,hess_contrainte,x0,options)
```
---------------------------------------------------------------
Etudiants: Younes Saoudi - Manal Hajji 
Filière : 2SN-L 

Projet Optimisation Numérique
Algorithme: Lagrangien Augmenté
---------------------------------------------------------------
"""

include("Algorithme_De_Newton.jl")
include("Regions_De_Confiance.jl")
include("Pas_De_Cauchy.jl")
include("Gradient_Conjugue_Tronque.jl")

function Lagrangien_Augmente(algo,fonc::Function,contrainte::Function,gradfonc::Function,
	hessfonc::Function,grad_contrainte::Function,hess_contrainte::Function,x0,options)

	if options == []
		epsilon = 1e-8
		tol = 1e-5
		itermax = 1000
		lambda0 = 2
		mu0 = 100
    tho = 5
    
	else
		epsilon = options[1]
		tol = options[2]
		itermax = options[3]
		lambda0 = options[4]
		mu0 = options[5]
		tho = options[6]
	end

  n = length(x0)
  xmin = zeros(n)
	fxmin = 0
	flag = 0
  nb_iters = 0
  
  eta0_chapeau = 0.1258925
  alpha = 0.1
  beta = 0.9
  epsilon0 = 1/mu0
  eta0 = eta0_chapeau / (mu0 ^ alpha)

  lambda_k = lambda0
  mu_k = mu0
  eta_k = eta0
  epsilon_k = epsilon0
  
  x_new = x0
  grad_LA0 = gradfonc(x0) + lambda0' * grad_contrainte(x0) + mu0 * grad_contrainte(x0) * contrainte(x0)
  
  while  ( (norm(gradfonc(x_new)) > tol*(norm(gradfonc(x0)) + epsilon)) || (norm(contrainte(x_new)) > (norm(contrainte(x0)) * tol + epsilon)) )
    nb_iters = nb_iters + 1

    ## Calcul approximation un minimiseur x_new du problème sans contrainte suivant 
    # La fonction Lagrangien
    LA(x) = fonc(x) + lambda_k' * contrainte(x) + 0.5 * mu_k * norm(contrainte(x)) ^ 2

    # Le gradient du Lagrangien
    grad_LA(x) = gradfonc(x) + grad_contrainte(x) * lambda_k  + mu_k * grad_contrainte(x) * contrainte(x) 
    #grad_LA(x) = ForwardDiff.gradient(LA, x)

    # La hessienne du Lagrangien
    hess_LA(x) = hessfonc(x) + lambda_k' * hess_contrainte(x) + mu_k * grad_contrainte(x) * grad_contrainte(x)'

    # Choix de l'algorithme à utiliser
    if algo == "newton"
      x_new, ~ = Algorithme_De_Newton(LA, grad_LA, hess_LA, xmin, [])
    elseif algo == "cauchy"
      x_new, ~ = Regions_De_Confiance("cauchy", LA, grad_LA, hess_LA, xmin, [])
    elseif algo == "gct"
      x_new, ~ = Regions_De_Confiance("gct", LA, grad_LA, hess_LA, xmin, [])
    else
      error("Muavais nom d'algroithme!")
    end
    
    # test de convergence
    if (norm(grad_LA(x_new)) <= tol * (norm(grad_LA0) + epsilon)) && (norm(contrainte(x_new)) <= tol * (norm(contrainte(x0))  +  epsilon))
			xmin = x_new
      break
    else
      # Mise à jour des multiplicateurs (entre autres)
      if norm(contrainte(x_new)) <= eta_k
        lambda_k = lambda_k + mu_k * contrainte(x_new)
        epsilon_k = epsilon_k / mu_k
        eta_k = eta_k / (mu_k ^ beta)
      else
        # Mise à jour du paramètre de pénalité (entre autres)
        mu_k = tho * mu_k
        epsilon_k = epsilon0 / mu_k
        eta_k = eta0_chapeau / (mu_k ^ alpha)
      end
    end

    # dépassement du nombre d'itérations permis
    if nb_iters == itermax
			flag = 1
			break
		end

  end
  
  xmin = x_new
  fxmin = fonc(xmin)

  return xmin,fxmin,flag,nb_iters
end