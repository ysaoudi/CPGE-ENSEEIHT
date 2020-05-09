%--------------------------------------------------------------------------
% ENSEEIHT - 1SN - Analyse de donnees
% PROJET Analyse de Données - Calcul Scientifique
% Auteurs : Younes SAOUDI, Issam HABIBI et Hamza MOUDDENE
%--------------------------------------------------------------------------
clear;
close all;
load donnees;
load exercice_1;

s = 50;

%Tirage d'une image aléatoire
individu = randi(37);
posture = randi(6);
chemin = './Images_Projet_2020';
fichier = [chemin '/' num2str(individu+3) '-' num2str(posture) '.jpg'];
Im=importdata(fichier);
I=rgb2gray(Im);
I=im2double(I);
image_test=I(:)';

% Affichage de l'image de test :
colormap gray;
imagesc(I);
axis image;
axis off;

% Nombre N de composantes principales a prendre en compte
% [dans un second temps, N peut etre calcule pour atteindre le pourcentage
% d'information avec N valeurs propres] :
N = 8;

% Composantes principales des donnees d'apprentissage
C = X_c * W;

% N premieres composantes principales des images d'apprentissage :
N_CP_Appr = C( : , 1:N );

% N premieres composantes principales des images de test :
image_test_c = image_test - individu_moyen;
C_test = image_test_c * W;
N_CP_test = C_test( : , 1:N );

% Determination des images d'apprentissage les plus proches (plus proches voisins) :
ListeClasse = 1:37;         %Les classes présentes dans la base d'Images

train_labels = repmat(numeros_individus, nb_postures, 1);
train_labels = train_labels(:); %Les classes d'apprentissage

k = 2; %Nombre de voisins plus proches que l'on cherche.

[Partition, kppv, Distances] = kppv(N_CP_Appr, N_CP_test, train_labels, individu, k, ListeClasse, 1);

if(min(Distances) < s)
    individu_reconnu = Partition; % le n° de l'indiv reconnu est celui dont la classe est la plus présente
    title({['Posture numero ' num2str(posture) ' de l''individu numero ' num2str(individu+3)];...
        ['Je reconnais l''individu numero ' num2str(individu_reconnu+3)]},'FontSize',20);
else
     title({['Posture numero ' num2str(posture) ' de l''individu numero ' num2str(individu+3)];...
        'Je ne reconnais pas cet individu !'},'FontSize',20);
end

%Trouver les résultats de la requête
postures = mod(kppv, nb_postures).';
postures(postures == 0) = nb_postures;
test_labels = train_labels(kppv);
recognitionMatrix = [test_labels postures'];

%Affichage de l'image requête
figure('Name',"FIGURE - Résultat d'une requête sur une base de visages",'Position',[0.2*L,0.2*H,0.6*L,0.5*H]);
subplot(1, k + 1, 1);
colormap gray;
imagesc(I);
axis image;
title("Requête");

%Affichage du résultat de la requête
for i = 1:k
    subplot(1, k+1, i+1);
    fichier = [chemin '/' num2str(recognitionMatrix(i, 1) + 3) '-' num2str( recognitionMatrix(i, 2) ) '.jpg'];
    Im = importdata(fichier);
    I = rgb2gray(Im);
    I = im2double(I);
    imagesc(I);
    axis image;
    title("Trouvée - choix " + i);
end