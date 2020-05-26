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
N = 50;

fp = 2000;
fc = 1500;
alpha = 0.5;
span = 8;


bits = randi([0, 1], 1, bits_nbr); % Génération de l’information binaire


Symboles = (2 * bi2de(reshape(bits, 2, length(bits)/2).') - 3).';% Mapping de Gray
Diracs = kron(Symboles, [1 zeros(1, Ns - 1)]);%Génération de la suite de Diracs pondérés par les symboles
h = rcosdesign(alpha, 8, Ns, 'sqrt');%Réponse impusionnelle du filtre de mise en forme
retard = (span * Ns) / 2;

x = filter(h, 1, [Diracs zeros(1, retard)]);% Filtrage de MEF
x = x(retard + 1 : end);

% Affichage du signal transmis après MEF
figure;
plot(x);
axis([0 length(x) - 1 -3 3]);
title('Figure 1 : Signal transmis après MEF');
xlabel('Temps en Secondes');

% Calcul de la DSP
DSP_x = (1 / length(x)) * abs(fft(x, 2 ^ nextpow2(length(x)))) .^ 2;
figure;
plot(linspace(-Fe / 2, Fe / 2, length(DSP_x)), fftshift(DSP_x));
title('Figure 2 : DSP du signal transmis');
xlabel('Fréquence en Hz');
ylabel('DSP)');

% Filtrage de réception
h_r = h;
z = filter(h_r, 1, [x zeros(1,retard)]);
z = z(retard + 1 : end);
figure ;
plot(z);
axis([0 bits_nbr - 1 -3 3]);
title('Figure 3 : Signal Reçu')
xlabel('Temps en Secondes');
ylabel('z');

% Diagramme de l'oeil
oeil = reshape(z, Ns, length(z) / Ns);
figure;
plot(oeil);
title("Figure 4 : Diagramme de l'oeil");
xlabel('Temps en Secondes');


%Ajout du bruit
TES = zeros(1,7);
TEB = zeros(1,7);

for i = 0 : 6
    Puissance_signal = mean(abs(x) .^ 2);
    Puissance_bruit = Puissance_signal * Ns  / (2 * log2(4) * 10 .^ (i / 10));
    bruit_gaussien = (sqrt(Puissance_bruit) * randn(1, length(x))) + 1i * (sqrt(Puissance_bruit) * randn(1, length(x)));
    y = x + bruit_gaussien;
    
    
    z = filter(h_r, 1, [y zeros(1,retard)]);% Filtrage de réception
    z = z(retard + 1 : end);
    z_echantillonne = z(1 : Ns : end);    % Echantillonnage du signal
    
    symboles_decides = zeros(1, length(z_echantillonne));
    
    for j = 1 : length(z_echantillonne)
        if (real(z_echantillonne(j)) > 2)
            symboles_decides(j) = 3;
        elseif (real(z_echantillonne(j)) >= 0)
            symboles_decides(j) = 1;
        elseif (real(z_echantillonne(j)) < -2)
            symboles_decides(j) = -3;
        else
            symboles_decides(j) = -1;
        end
    end
    
    % TES et TEB
    TES(i + 1) = length(find(Symboles ~= symboles_decides)) / length(Symboles);
    TEB(i + 1) = TES(i + 1) / 2;
end

figure;
plot(real(z_echantillonne), 0, 'r+');
xlim([-4 4])
hold on;
plot(Symboles, 0, 'b+');
legend('Les constellations en sortie du mapping','Les constellations en sortie de l’échantillonneur')
xlabel('I');
ylabel('Q');


% TEB Théorique et Estimé
figure;
semilogy([0 : 6], TEB);
hold on
semilogy([0 : 6], (3 / 4) * qfunc(sqrt((4 / 5) * 10 .^ ([0 : 6] / 10))));
title('Comparaison des les TEB');
legend('TEB Estimé','TEB Théorique')
ylabel('TEB');