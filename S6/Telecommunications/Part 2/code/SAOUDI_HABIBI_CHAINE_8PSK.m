% ENSEEIHT - 1SN - Télécommunications
% Etudes de chaines de transmission sur Fréquence Porteuse
% Auteurs : Younes SAOUDI & Issam HABIBI

clear;
close all;
clc;

% Les données
bits_nbr = 10000;
Fe = 10000;
Rs = 2000;
Ns = floor(Fe / Rs);
Te = 1 / Fe;
M = 8;
fp = 2000;
fc = 1500;
alpha = 0.5;
span = 8;

bits = randi([0, M - 1], 1, bits_nbr); %Génération de l'information binaire

symboles_dk = pskmod(bits, M, pi / M); % Mappinp
symboles_ak = real(symboles_dk);
symboles_bk = imag(symboles_dk);
Diracs1 = kron(symboles_ak, [1 zeros(1, Ns - 1)]); %Génération de la suite de Diracs pondérés par les symboles
Diracs2 = kron(symboles_bk, [1 zeros(1, Ns - 1)]);
h = rcosdesign(alpha, 8, Ns, 'sqrt');%Réponse impusionnelle du filtre de mise en forme
retard = (span * Ns) / 2;

I = filter(h, 1, [Diracs1 zeros(1, retard)]);
Q = filter(h, 1, [Diracs2 zeros(1, retard)]);
I = I(retard + 1 : end);
Q = Q(retard + 1 : end);

x =  I + 1i * Q;

% Affichage du signal génerée sur la voie I
figure ;
plot(I);
axis([0 length(I) - 1 -1 1]);
title('Figure 1 : Signal généré sur la voie I');
xlabel('Temps en Secondes');
ylabel('I');

% Affichage du signal généré sur la voie Q
figure ;
plot(Q);
axis([0 length(Q) - 1 -1 1]);
title('Figure 2 : Signal généré sur la voie Q');
xlabel('Temps en Secondes');
ylabel('Q');


% Calcul de la DSP 
DSP_x = (1 / length(x)) * abs(fft(x, 2 ^ nextpow2(length(x)))) .^ 2;
figure;
plot(linspace(-Fe / 2, Fe / 2, length(DSP_x)), fftshift(DSP_x));
title('Figure 3 : DSP du signal transmis');
xlabel('Fréquence en Hz');
ylabel('DSP)');

%Filtrage de réception
h_r = h;
z = filter(h_r, 1, [x zeros(1,retard)]);
z = z(retard + 1 : end);
figure ;
plot(real(z));
axis([0 bits_nbr - 1 -1 1]);
title('Figure 3 : Signal Reçu')
xlabel('Temps en Secondes');
ylabel('z');

% Diagramme de l'oeil
oeil = reshape(real(z), Ns, length(real(z)) / Ns);
figure;
plot(oeil);
title("Figure 4 : Diagramme de l'oeil");
xlabel('Temps en Secondes');

%Ajout du brtui
TES = zeros(1,7);
TEB = zeros(1,7);

for i = 0 : 6
    Puissance_signal = mean(abs(x) .^ 2);
    Puissance_bruit = Puissance_signal * Ns  / (2 * log2(M) * 10 .^ (i / 10));
    bruit_gaussien = (sqrt(Puissance_bruit) * randn(1, length(x))) + 1i * (sqrt(Puissance_bruit) * randn(1, length(x)));
    y = x + bruit_gaussien;
    
     
    z = filter(h_r, 1, [y zeros(1,retard)]);% Filtrage de réception
    z = z(retard + 1 : end);
    z_echantillonne = z(1 : Ns : end);    % Echantillonnage du signal
    
    
    figure();
    plot(real(z_echantillonne), imag(z_echantillonne), 'r+');
    hold on;
    plot(symboles_ak, symboles_bk, 'b+');
    legend('Les constellations en sortie du mapping','Les constellations en sortie de l’échantillonneur')
    xlabel('I');
    ylabel('Q');
    
    
    bits_decides = pskdemod(z_echantillonne, M, pi / M);
    
    %TES et TEB
    TES(i + 1) = length(find(bits_decides ~= bits)) / length(bits);
    TEB(i + 1) = TES(i + 1) / log2(M);
end

% TEB Théorique et Estimé
figure;
semilogy([0 : 6], TEB);
hold on
semilogy([0 : 6], (2 / log2(M)) * qfunc(sqrt(2 * log2(M) * 10 .^ ([0 : 6] / 10)) * sin(pi / M)));
title('Figure 8 : Comparaison des les TEB');
legend('TEB Estimé','TEB Théorique')
ylabel('TEB');