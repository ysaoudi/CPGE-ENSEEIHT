%--------------------------------------------------------------------------
% ENSEEIHT - 1SN - Analyse de donnees
% PROJET Analyse de Données - Calcul Scientifique
% Auteurs : Younes SAOUDI, Issam HABIBI et Hamza MOUDDENE
%--------------------------------------------------------------------------
clear;
close all;
load donnees;
load exercice_1;

% Tirage aleatoire de nbrTests images de test :
nbrTests = 100;
individus_aleatoires = zeros(nbrTests,1);
images_test = zeros(nbrTests, size(X,2));

for i=1:nbrTests
    individu = randi(37);
    posture = randi(6);
    individus_aleatoires(i) = individu;
    chemin = './Images_Projet_2020';
    fichier = [chemin '/' num2str(individu+3) '-' num2str(posture) '.jpg'];
    Im=importdata(fichier);
    I=rgb2gray(Im);
    I=im2double(I);
    image_test=I(:)';
    images_test(i,:) = image_test;
end

% Nombre N de composantes principales a prendre en compte
% [dans un second temps, N peut etre calcule pour atteindre le pourcentage
% d'information avec N valeurs propres] :
N = 8;

% Composantes principales des donnees d'apprentissage
C = X_c * W;

% N premieres composantes principales des images d'apprentissage :
N_CP_Appr = C( : , 1:N );

% N premieres composantes principales des images de test :
images_test_c = images_test - individu_moyen;
C_test = images_test_c * W;
N_CP_test = C_test( : , 1:N );

% Determination des images d'apprentissage les plus proches (plus proches voisins) :
ListeClasse = 1:37;         %Les classes présentes dans la base d'Images

train_labels = repmat(numeros_individus, nb_postures, 1);
train_labels = train_labels(:); %Les classes d'apprentissage

%Nombre de voisins plus proches
k = 2;   
[Partition, kppv, ~] = kppv(N_CP_Appr, N_CP_test, train_labels, individus_aleatoires, k, ListeClasse, nbrTests);

individu_reconnu = Partition; % le n° des indiv reconnus sont ceux dont la classe est la plus présente

confusion_matrix = zeros(37);
for i = 1:nbrTests
    confusion_matrix(individus_aleatoires(i), individu_reconnu(i)) = confusion_matrix(individus_aleatoires(i), individu_reconnu(i))+1;
end

errorRate = (nbrTests - sum(diag(confusion_matrix))) / nbrTests * 100