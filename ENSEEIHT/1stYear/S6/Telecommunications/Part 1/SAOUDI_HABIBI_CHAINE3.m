% ENSEEIHT - 1SN - Télécommunications
% Etudes de chaines de transmission en BdB
% Auteurs : Younes SAOUDI & Issam HABIBI

clear;
close all;
clc;


%III- Choix du Filtre de MEF - Canal de Propagation


%Les données

bits_nbr = 10000; %Nombre de bits générés
Ns = 4;             %Condition de shannon Fe>=2.Fmax et Fmax = 4/Ts
Fe = 12000;             % Fréquence d'échantillonnage
Te = 1/Fe;             %Durée d'echatillonnage
Rythm = 3000;       %Rythm Symbol
alpha = 0.5;    % Roll-off
span = 10;


%2-b
bits = randi([0,1], 1, bits_nbr);                    %Génération de l'information binaire
symboles = 2*bits - 1;                              %Mapping binaire (moyenne nulle)
h = rcosdesign(alpha, span,Ns); %Fonction Cosinus Surélevée
hr = fliplr(h);
delay = span * Ns/2;
g = conv(hr, h, 'same');

figure
plot(g);

Diracs = kron(symboles, [1 zeros( 1, Ns - 1)]); %Génération de la suite de Diracs pondérés par les symboles
x = filter(h, 1, [ Diracs zeros(1, delay) ]); %Filtrage de MEF
x = x(delay + 1 : end);

plot(x);
axis([0 length(x)- 1 -2 2]);

y = filter(hr,1,[x zeros(1,delay)]);%Signal en Sortie du Filtre de Réception
y = y(delay + 1 : end);

figure
plot(y)
axis([0 length(y) - 1 -2 2]);
title('Figure 1 : Signal en Sortie du Filtre de Réception');

% Calcul de la DSP du signal
DSP_x = (1 / length(x)) * abs(fft(x, 2 ^ nextpow2(length(x)))) .^ 2;

figure;
plot(linspace(-Fe, Fe, length(DSP_x)), fftshift(DSP_x));
title('Figure 2 : DSP du Signal Généré');
xlabel('Fréquence en Hz');
ylabel('DSP');

%2.c-
oeil = reshape(y, Ns, length(y) / Ns); % Diagramme de l'oeil
figure
plot(oeil);
axis([1 Ns -Ns-1 (Ns + 1)]);
title("Figure 3 : Diagramme de l'oeil");
xlabel('Temps en Secondes');



%2.d-
%TEB
signal_echantillone = y( 1 : Ns : end);
symboles_decides = sign(signal_echantillone);
%Taux d'erreur symbole
TES_1 = length( find(symboles_decides ~= symboles) ) / length(symboles)
bits_decides = (1 + symboles_decides) / 2;
%Taux d'erreur binaire
TEB_1 = length( find(bits_decides ~= bits) ) / length(bits)



%3-
%Chaine avec bruît
F = mean(abs(x).^2); %Puisssance
Eb_sur_N0_db = 10; %Calcul de bruit
sigma_n_carre = (F * Ns) / ( 2 * log2(2) * 10^(Eb_sur_N0_db/10) );
bruit = sqrt(sigma_n_carre) * randn(1, length(x));
z = x + bruit; % Signal avec bruît

% Filtrage de Réception
z_1 = filter(hr, 1, [z zeros(1, delay)]);
z_1 = z_1(delay + 1 : end);
oeil_1 = reshape(z_1, Ns, length(z_1) / Ns);
figure;
plot(oeil_1);
axis([1 Ns -Ns-1 Ns+1]);
signal_echantillone_1 = z_1(1 : Ns : end);

%TES
symboles_decides_1 = sign(signal_echantillone_1);
TES2 = length( find(symboles_decides_1 ~= symboles) ) / length(symboles)
bits_decides_1 = (1 + symboles_decides_1) / 2;
%TEB
TEB2 = length( find(bits_decides_1 ~= bits) ) / length(bits)



%4-
%TEB Expérimental et TEB Théorique
TEB = zeros(0, 6);

for Eb_sur_N0_dB = 0 : 6
    sigma_n_carre = (F * Ns) / (2 * log2(2) * 10^(Eb_sur_N0_dB/10));
    bruit_gaussien = sqrt(sigma_n_carre) * randn(1, length(x));
    y_2 = x + bruit_gaussien;
    
    z_2 = filter(hr, 1, [y_2 zeros(1, delay)]);% Filtrage de Réception
    z_2 = z_2(delay + 1 : end);
    z_echantillonne = z_2(1: Ns : end);% Echantillonnage du signal
    symboles_decides = sign(z_echantillonne);
    
    TES = length( find(symboles_decides ~= symboles) ) / length(symboles);
    bits_decides = (symboles_decides + 1) / 2; %Demapping
    TEB(Eb_sur_N0_dB + 1) = length( find(bits_decides ~= bits) ) / length(bits);
end

figure;
semilogy(0 : 6, TEB, 'r-');
hold on
semilogy([0 : 6], qfunc(sqrt((2 * 10 .^ ([0 : 6] / 10)))),'b-');
grid
title('Figure 7 : Comparaison des TEB');
legend('TEB Expérimental','TEB Théorique')
ylabel('TEB');