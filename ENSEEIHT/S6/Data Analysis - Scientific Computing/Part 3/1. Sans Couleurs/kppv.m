%--------------------------------------------------------------------------
% ENSEEIHT - 1SN - Analyse de donnees
% PROJET Analyse de Données - Calcul Scientifique
% Auteurs : Younes SAOUDI, Issam HABIBI et Hamza MOUDDENE
% fonction kppv.m
%--------------------------------------------------------------------------
function [Partition, voisins, distances] = kppv(DataA,DataT,labelA,labelT,K,ListeClass, Nt_test)

    [Na,~] = size(DataA);
    [Nt,~] = size(DataT);
    Partition = zeros(Nt_test,1);

    for i = 1:Nt_test
        
        distances = vecnorm( ((ones(Na, 1) * DataT(i, :)) - DataA)' )';
        [~, indices] = sort(distances);
        voisins = indices(1:K);
        labels = labelA(voisins);
        compte = histc(labels, ListeClass);
        [valeurMax, indiceClasse] = max(compte);
        if length(find(compte == valeurMax)) > 1
            classe = labelA(voisins(1));
        else
            classe = ListeClass(indiceClasse);
        end
        Partition(i) = classe;

    end
end