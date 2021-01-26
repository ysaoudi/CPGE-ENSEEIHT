% ENSEEIHT - 1SN - Télécommunications
% Etudes de chaines de transmission en BdB
% Auteurs : Younes SAOUDI & Issam HABIBI

clear;
close all;
clc;

% I-Chaine de référence

%Les données

bits_nbr = 10000;   %Nombre de bits générés
Ns = 4;             %Condition de shannon Fe>=2.Fmax et Fmax = 4/Ts
Te = 1;             %Durée d'echatillonnage
Fe = 1;             % Fréquence d'échantillonnage

%1.

bits = randi([0,1], 1, bits_nbr);                    %Génération de l'information binaire
symboles = 2*bits - 1;                              %Mapping binaire (moyenne nulle)
Diracs = kron(symboles, [1 zeros( 1, Ns - 1)]); %Génération de la suite de Diracs pondérés par les symboles
h = ones(1, Ns);                                   %Réponse impusionnelle du filtre de mise en forme

x = filter(h, 1, Diracs);
figure;
plot(x);
axis([0 bits_nbr - 1 -2 2]);
title('Figure 1 : Signal Généré');
xlabel('Temps en (s)');
ylabel('x');

% Calcul de la DSP du signal généré
DSP_x = (1 / length(x)) * abs(fft(x, 2 ^ nextpow2(length(x)))) .^ 2;

% Affichage de la DSP
figure;
plot(linspace(-Fe, Fe, length(DSP_x)), fftshift(DSP_x));
title('Figure 2 : DSP du Signal Généré');
xlabel('Fréquence en Hz');
ylabel('DSP_x');

%2-a
%Filtrage de réception
hr = fliplr(h);
y = filter(hr, 1, x);
figure;
plot(y);
axis([0 bits_nbr - 1 -5 5]);
title('Figure 3 : Signal en Sortie du Filtre de Réception');
xlabel('Temps en (s)');
ylabel('y');

%2-b
% Diagramme de l'oeil
oeil = reshape(y, Ns, length(y)/Ns);
figure
plot(oeil);
title("Figure 4 : Diagramme de l'oeil");
xlabel('Temps en secondes');


%Question 2-c
%Calcul du TEB et du TES
signal_echanntillon = y(Ns:Ns:end);
symboles_decides = sign(signal_echanntillon);

%TEB et TES
TES1 = length(find(symboles_decides ~= symboles)) / length(symboles)
bits_deci = (1+symboles_decides)/2;
%Taux d'erreur binaire
TEB1 = length(find(bits_deci ~= bits))/length(bits)


%3
%Chaine avec bruît
F = mean(abs(x).^2);    %Puissance du signal
TEB = zeros(0, 6);

for Eb_sur_N0_dB = 0 : 6
    
    sigma_n_carre = (F * Ns) / (2 * log2(2) * 10^(Eb_sur_N0_dB / 10) );
    Bruit_gaussien = sqrt(sigma_n_carre) * randn(1, length(x));
    y2 = x + Bruit_gaussien;
    
    z2 = filter(hr, 1, y2); % Filtrage de réception
    z_echant = z2(Ns : Ns : end); % Echantillonnage du signal
    
    symboles_decides = sign(z_echant); % Detecteur à seuil
    TES = length(find(symboles_decides ~= symboles) / length(symboles));
    
    bits_decides = (symboles_decides + 1) / 2; %Demapping
    TEB (Eb_sur_N0_dB + 1) = length(find(bits_decides ~= bits)) / length(bits);
end

figure;
semilogy(0 : 6, TEB, 'r*');
title('Figure 5 : TEB Théorique en Fonction de Eb/N0');
ylabel('TEB');
xlabel('Eb/N0');

%4-
%TEB Théorique et TEB Experimentale 
figure;
semilogy([0 : 6], TEB, 'r-');
hold on;
semilogy([0 : 6], qfunc(sqrt((2 * 10 .^ ([0 : 6] / 10)))),'b-');
grid
title('Figure 6 : Comparaison des les TEB');
legend('TEB Expérimental','TEB Théorique')
ylabel('TEB');