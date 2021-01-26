@doc doc"""
Minimise le problème : ``min_{||s||< \delta_{k}} q_k(s) = s^{t}g + (1/2)s^{t}Hs``
                        pour la ``k^{ème}`` itération de l'algorithme des régions de confiance

# Syntaxe
```julia
sk = Gradient_Conjugue_Tronque(fk,gradfk,hessfk,option)
```

# Entrées :   
   * **gradfk**           : (Array{Float,1}) le gradient de la fonction f appliqué au point xk
   * **hessfk**           : (Array{Float,2}) la Hessienne de la fonction f appliqué au point xk
   * **options**          : (Array{Float,1})
      - **delta**    : le rayon de la région de confiance
      - **max_iter** : le nombre maximal d'iterations
      - **tol**      : la tolérance pour la condition d'arrêt sur le gradient


# Sorties:
   * **s** : (Array{Float,1}) le pas s qui approche la solution du problème : ``min_{||s||< \delta_{k}} q(s)``

# Exemple d'appel:
```julia
gradf(x)=[-400*x[1]*(x[2]-x[1]^2)-2*(1-x[1]) ; 200*(x[2]-x[1]^2)]
hessf(x)=[-400*(x[2]-3*x[1]^2)+2  -400*x[1];-400*x[1]  200]
xk = [1; 0]
options = []
s = Gradient_Conjugue_Tronque(gradf(xk),hessf(xk),options)
```
---------------------------------------------------------------
Etudiants: Younes Saoudi - Manal Hajji 
Filière : 2SN-L 

Projet Optimisation Numérique
Algorithme: Gradient Conjugué Tronqué
---------------------------------------------------------------
"""
function Gradient_Conjugue_Tronque(gradfk, hessfk, options)

    "# Si option est vide on initialise les 3 paramètres par défaut"
    if options == []
        deltak = 2
        max_iter = 100
        tol = 1e-6
    else
        deltak = options[1]
        max_iter = options[2]
        tol = options[3]
    end

    n = length(gradfk)
    s = zeros(n)
    
    g0 = gradfk
    gj = gradfk
    pj = - gradfk
    sj = zeros(n)

    # Question 2
    # for j = 1 : 1

    for j = 1 : max_iter
        kj = pj' * hessfk * pj
    
        if kj < 0
            # Déterminer sigmaj, racine de l'équation norm(sj + sigma*pj) = deltak pour laquelle q(sj + sigma*pj) est plus petite
            # Question 1
            # s = sj
            # decroissance = - (gradfk' * s + 0.5 * s' * hessfk * s) 
            # break 
            detj = sqrt((2 * sj' * pj) ^ 2 - 4 * norm(pj) ^ 2 * (norm(sj) ^ 2 - deltak ^ 2))
            sigma1 = (- (2 * sj' * pj) - detj) / (2 * norm(pj) ^ 2)
            sigma2 = (- (2 * sj' * pj) + detj) / (2 * norm(pj) ^ 2)

        
            sj_sigma1 = sj + sigma1 * pj
            q_sigma1 = gj' * sj_sigma1  +  0.5 * sj_sigma1' * hessfk * sj_sigma1
        
            sj_sigma2 = sj + sigma2 * pj
            q_sigma2 = gj' * sj_sigma2  +  0.5 * sj_sigma1' * hessfk * sj_sigma2

            if q_sigma1 < q_sigma2
                sigmaj = sigma1
            else
                sigmaj = sigma2
            end

            s = sj + sigmaj * pj
            # decroissance = - (gradfk' * s + 0.5 * s' * hessfk * s) 
            break
        end

        alphaj = gj' * gj / kj

        if norm((sj + alphaj * pj)) >= deltak
            # Question 1
            # s = sj
            # decroissance = - (gradfk' * s + 0.5 * s' * hessfk * s) 
            # break 

            # Déterminer sigmaj la racine positive de l'équation norm(sj + sigma*pj) = deltak 

            sigmaj = -norm(sj) + deltak / norm(pj)
            s = sj + sigmaj * pj
            # decroissance = - (gradfk' * s + 0.5 * s' * hessfk * s) 
            break
        end

        sj = sj + alphaj * pj
        gj_new = gj + alphaj * hessfk * pj
        pj = -gj_new + (gj_new' * gj_new / (gj' * gj)) * pj
        gj = gj_new
        
        # Condition de convergence
        if norm(gj) < tol * norm(g0)
            s = sj
            # decroissance = - (gradfk' * s + 0.5 * s' * hessfk * s) 
            break
        end
    end

    return s
end
