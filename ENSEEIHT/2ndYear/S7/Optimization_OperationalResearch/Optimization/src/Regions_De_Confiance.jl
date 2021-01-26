@doc doc"""
Minimise une fonction en utilisant l'algorithme des régions de confiance avec
    - le pas de Cauchy
ou
    - le pas issu de l'algorithme du gradient conjugue tronqué

# Syntaxe
```julia
x_new, nb_iters, f(x_new), flag = Regions_De_Confiance(algo,f,gradf,hessf,x0,option)
```

# Entrées :

   * **algo**        : (String) string indicant la méthode à utiliser pour calculer le pas
        - **"gct"**   : pour l'algorithme du gradient conjugué tronqué
        - **"cauchy"**: pour le pas de Cauchy
   * **f**           : (Function) la fonction à minimiser
   * **gradf**       : (Function) le gradient de la fonction f
   * **hessf**       : (Function) la hessiene de la fonction à minimiser
   * **x0**          : (Array{Float,1}) point de départ
   * **options**     : (Array{Float,1})
     * **deltaMax**      : utile pour les m-à-j de la région de confiance
                      ``R_{k}=\left\{x_{k}+s ;\|s\| \leq \Delta_{k}\right\}``
     * **gamma1,gamma2** : ``0 < \gamma_{1} < 1 < \gamma_{2}`` pour les m-à-j de ``R_{k}``
     * **eta1,eta2**     : ``0 < \eta_{1} < \eta_{2} < 1`` pour les m-à-j de ``R_{k}``
     * **delta0**        : le rayon de départ de la région de confiance
     * **max_iter**      : le nombre maximale d'iterations
     * **Tol_abs**       : la tolérence absolue
     * **Tol_rel**       : la tolérence relative

# Sorties:

   * **xmin**    : (Array{Float,1}) une approximation de la solution du problème : ``min_{x \in \mathbb{R}^{n}} f(x)``
   * **fxmin**   : (Float) ``f(x_{min})``
   * **flag**    : (Integer) un entier indiquant le critère sur lequel le programme à arrêter
      - **0**    : Convergence
      - **1**    : stagnation du ``x``
      - **2**    : stagnation du ``f``
      - **3**    : nombre maximal d'itération dépassé
   * **nb_iters** : (Integer)le nombre d'iteration qu'à fait le programme

# Exemple d'appel
```julia
algo="gct"
f(x)=100*(x[2]-x[1]^2)^2+(1-x[1])^2
gradf(x)=[-400*x[1]*(x[2]-x[1]^2)-2*(1-x[1]) ; 200*(x[2]-x[1]^2)]
hessf(x)=[-400*(x[2]-3*x[1]^2)+2  -400*x[1];-400*x[1]  200]
x0 = [1; 0]
options = []
xmin, fxmin, flag,nb_iters = Regions_De_Confiance(algo,f,gradf,hessf,x0,options)
```
---------------------------------------------------------------
Etudiants: Younes Saoudi - Manal Hajji 
Filière : 2SN-L 

Projet Optimisation Numérique
Algorithme: Régions de Confiance
---------------------------------------------------------------
"""
function Regions_De_Confiance(algo, f::Function, gradf::Function, hessf::Function, x0, options)

    if options == []
        deltaMax = 10
        gamma1 = 0.5
        gamma2 = 2.00
        eta1 = 0.25
        eta2 = 0.75
        delta0 = 2
        max_iter = 1000
        Tol_abs = sqrt(eps())
        Tol_rel = 1e-15
    else
        deltaMax = options[1]
        gamma1 = options[2]
        gamma2 = options[3]
        eta1 = options[4]
        eta2 = options[5]
        delta0 = options[6]
        max_iter = options[7]
        Tol_abs = options[8]
        Tol_rel = options[9]
    end

    n = length(x0)
    xmin = zeros(n)
    fxmin = f(xmin)
    flag = 0
    nb_iters = 0
    x_new = x0
    delta_new = delta0
    
    while true
        nb_iters = nb_iters + 1
        x_old = x_new
        delta_old = delta_new
        
        # Choix de l'algorithme à appliquer et calcul de s (ou sk) solution du sous-problème
        if algo == "cauchy"
            s, ~ = Pas_De_Cauchy(gradf(x_old), hessf(x_old), delta_old)
        elseif algo == "gct"
            options_gct = [delta_old, max_iter, Tol_rel]
            s = Gradient_Conjugue_Tronque(gradf(x_old), hessf(x_old), options_gct)
        else
            error("Muavais nom d'algroithme!")
        end

        # Calcul de mk(xk + sk)
        m_xs = f(x_old) + transpose(gradf(x_old)) * s + 0.5 * transpose(s) * hessf(x_old) * s

        rho = (f(x_old) - f(x_old + s)) / (f(x_old) - m_xs)

        # Mise à jour de l'itéré courant
        if rho > eta1
            x_new = x_old + s
        end

        # Mise à jour de la région de confiance
        if rho >= eta2
            delta_new = min(gamma2 * delta_old, deltaMax)
        elseif rho >= eta1 && rho <= eta2
            delta_new = delta_old
        else
            delta_new = gamma1 * delta_old
        end


        # test de convergence
        if norm(gradf(x_new), 2) <= Tol_rel * norm(gradf(x0), 2)
            flag = 0
            break
        # stagnation de x_new
        elseif (norm(s, 2) <= Tol_rel * (norm(x_new, 2) + eps() )) && x_old != x_new
            flag = 1
            break
        # stagnation de f
        elseif (abs(f(x_old) - f(x_new)) <= Tol_rel * ( abs(f(x_new)) + eps() )) && x_old != x_new
            flag = 2
            break
        # depassement du nombre d'itérations permis
        elseif nb_iters >= max_iter
            flag = 3
            break
        end
    end

    xmin = x_new
    f_min = f(x_new)

    return xmin, fxmin, flag, nb_iters
end
