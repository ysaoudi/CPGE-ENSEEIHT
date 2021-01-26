@doc doc"""
Approximation de la solution du sous-problème ``q_k(s) = s^{t}g + (1/2)s^{t}Hs`` 
        avec ``s=-t g_k,t > 0,||s||< \delta_k ``


# Syntaxe
```julia
s1, e1 = Pas_De_Cauchy(gradient,Hessienne,delta)
```

# Entrées
 * **gradfk** : (Array{Float,1}) le gradient de la fonction f appliqué au point ``x_k``
 * **hessfk** : (Array{Float,2}) la Hessienne de la fonction f appliqué au point ``x_k``
 * **delta**  : (Float) le rayon de la région de confiance

# Sorties
 * **s** : (Array{Float,1}) une approximation de la  solution du sous-problème
 * **e** : (Integer) indice indiquant l'état de sortie:
        si g != 0
            si on ne sature pas la boule
              e <- 1
            sinon
              e <- -1
        sinon
            e <- 0

# Exemple d'appel
```julia
g1 = [0; 0]
H1 = [7 0 ; 0 2]
delta1 = 1
s1, e1 = Pas_De_Cauchy(g1,H1,delta1)
```
---------------------------------------------------------------
Etudiants: Younes Saoudi - Manal Hajji 
Filière : 2SN-L 

Projet Optimisation Numérique
Algorithme: Pas de Cauchy
---------------------------------------------------------------
"""
function Pas_De_Cauchy(g,H,delta)

    e = 0
    n = length(g)
    s = zeros(n)

    c = transpose(g) * H * g
    norme_g = norm(g)
    
    if (c > 0)
      t = norme_g^2 / c
      if(t > delta/norme_g)
          t = delta/norme_g
      end
    else
      if (norme_g == 0)
        t = 1
      else 
        t = delta/norme_g
      end
    end

    s = -t * g
    
    if g != 0
      # si on ne sature pas la boule
      if norm(s) <= delta
        e = 1
      else
        e = -1
      end
    else 
      e = 0
    end

    # decroissance = - (g' * s + 0.5 * s' * H * s)
    return s, e
end
