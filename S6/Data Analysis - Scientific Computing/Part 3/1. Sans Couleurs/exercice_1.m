clear;
close all;

taille_ecran = get(0,'ScreenSize');
L = taille_ecran(3);
H = taille_ecran(4);
load donnees;
figure('Name','Individu moyen et eigenfaces','Position',[0,0,0.67*L,0.67*H]);

% Calcul de l'individu moyen :
individu_moyen = mean(X,1);

% Centrage des donnees :
X_c = X - individu_moyen;

% Calcul de la matrice Sigma_2 (de taille n x n) [voir Annexe 1 pour la nature de Sigma_2] :
Sigma_2 = X_c * X_c' * 1/size(X_c,1);

% Calcul des vecteurs/valeurs propres de la matrice Sigma_2 :
[W_2, D_2] = eig(Sigma_2);

% Tri par ordre decroissant des valeurs propres de Sigma_2 :
[D_2_sorted, indexes] = sort(diag(D_2), 'descend');

% Tri des vecteurs propres de Sigma_2 dans le meme ordre :
W_2_sorted = W_2(:, indexes);

% Elimination du dernier vecteur propre de Sigma_2 :
W_2_sorted = W_2_sorted( : , 1 : end - 1);

% Vecteurs propres de Sigma (deduits de ceux de Sigma_2) :
W = transpose(X_c) * W_2_sorted;
    
% Normalisation des vecteurs propres de Sigma
% [les vecteurs propres de Sigma_2 sont normalisés
% mais le calcul qui donne W, les vecteurs propres de Sigma,
% leur fait perdre cette propriété] :
for k =1: n-1
    W(:,k) = W(:,k)/norm(W(:,k));
end

%Affichage de l'individu moyen et des eigenfaces sous forme d'images :
colormap gray;
img = reshape(individu_moyen,nb_lignes,nb_colonnes);
subplot(min(8, nb_individus), nb_postures,1);
imagesc(img);
axis image;
axis off;
title('Individu moyen','FontSize',15);
for k = 1:min(15, n-1)
	img = reshape(W( : , k), nb_lignes, nb_colonnes);
	subplot(min(8,nb_individus), nb_postures, k+1);
	imagesc(img);
	axis image;
	axis off;
	title(['Eigenface ',num2str(k)],'FontSize',15);
end

save exercice_1;
