% ENSEEIHT - 1SN - Télécommunications
% Etudes de chaines de transmission sur Fréquence Porteuse
% Auteurs : Younes SAOUDI & Issam HABIBI

clear;
close all;
clc;


% Les données
bits_nbr = 10000;
Fe = 10000;
Rs = 1000;
Ns = Fe / Rs;
Te = 1 / Fe;
N = 50;

fp = 2000;
fc = 1500;
alpha = 0.35;
span = 8;


bits = randi([0, 1], 1, bits_nbr); %Génération de l'information binaire


symboles_ak = 2 * bits(1 : 2 : end) - 1; 
symboles_bk = 2 * bits(2 : 2 : end) - 1;
symboles_dk = symboles_ak + 1j * symboles_bk; %Mapping +/-1 +/-j
Diracs1 = kron(symboles_ak, [1 zeros(1, Ns - 1)]); %Génération de la suite de Diracs pondérés par les symboles
Diracs2 = kron(symboles_bk, [1 zeros(1, Ns - 1)]);
h = rcosdesign(alpha, 8, Ns, 'sqrt');%Réponse impusionnelle du filtre de mise en forme
retard = (span * Ns) / 2;


I = filter(h, 1, [Diracs1 zeros(1, retard)]);
Q = filter(h, 1, [Diracs2 zeros(1, retard)]);
I = I(retard + 1 : end);
Q = Q(retard + 1 : end);

T = [0 : length(I) - 1] * Te;
x_enveloppe =  I + 1i * Q;

% Affichage du signal génerée sur la voie I
figure ;
plot(I);
axis([0 length(I) - 1 -1 1]);
title('Figure 1: Signal généré sur la voie I');
xlabel('Temps en Secondes');
ylabel('I');

% Affichage du signal généré sur la voie Q
figure ;
plot(Q);
axis([0 length(Q) - 1 -1 1]);
title('Figure 2 : Signal généré sur la voie Q');
xlabel('Temps en Secondes');
ylabel('Q');

% Calcul de la DSP de x_enveloppe
DSP_x_enveloppe = (1 / length(x_enveloppe)) * abs(fft(x_enveloppe, 2 ^ nextpow2(length(x_enveloppe)))) .^ 2;
figure;
plot(linspace(-Fe / 2, Fe / 2, length(DSP_x_enveloppe)), fftshift(DSP_x_enveloppe));
title('Figure 3: DSP de l’Enveloppe');
xlabel('Fréquence en Hz');
ylabel('DSP');


% Filtrage de réception
h_r = h;
z = filter(h_r, 1, [x_enveloppe zeros(1, retard)]);
z = z(retard + 1 : end);
figure ;
plot(real(z));
axis([0 bits_nbr - 1 -1.5 1.5]);
title('Figure 3 : Signal Reçu');
xlabel('Temps en Secondes');
ylabel('z');

% Diagramme de l'oeil
oeil = reshape(real(z), Ns, length(real(z)) / Ns);
figure;
plot(oeil);
title("Figure 4 : Diagramme de l'oeil");
xlabel('Temps en Secondes');


%Ajout du bruit
TES = zeros(1,7);
TEB = zeros(1,7);

for i = 0 : 6
    Puissance_signal = mean(abs(x_enveloppe) .^ 2);
    Puissance_bruit = Puissance_signal * Ns  / (2 * log2(4) * 10 .^ (i / 10));
    bruit_gaussien = (sqrt(Puissance_bruit) * randn(1, length(x_enveloppe))) + 1i * (sqrt(Puissance_bruit) * randn(1, length(x_enveloppe)));
    y = x_enveloppe + bruit_gaussien; 
    
    z = filter(h_r, 1, [y zeros(1,retard)]);% Filtrage de réception
    z = z(retard + 1 : end);
    z_echantillonne = z(1 : Ns : end);    % Echantillonnage du signal

    figure;
    plot(real(z_echantillonne), imag(z_echantillonne), 'r+');
    hold on;
    plot(symboles_ak, symboles_bk, 'b+');
    legend('Les constellations en sortie du mapping','Les constellations en sortie de l’échantillonneur')
    xlabel('I');
    ylabel('Q');

    for j = 1 : length(z_echantillonne)
        if (real(z_echantillonne(j)) <= 0 && imag(z_echantillonne(j)) <= 0)
            symboles_decides(j) = -1 - 1i;
        elseif (real(z_echantillonne(j)) >= 0 && imag(z_echantillonne(j)) >= 0)
            symboles_decides(j) = 1 + 1i;
        elseif (real(z_echantillonne(j)) <= 0 && imag(z_echantillonne(j)) >= 0)
            symboles_decides(j) = -1 + 1i;
        elseif (real(z_echantillonne(j)) >= 0 && imag(z_echantillonne(j)) <= 0)
            symboles_decides(j) = 1 - 1i;
        end
    end
    
    % TES et TEB
    TES(i + 1) = length(find(symboles_decides ~= symboles_dk)) / (length(symboles_dk));
    TEB(i + 1) = TES(i + 1) / 2;
end

%TEB Théorique  et Estimé
figure;
semilogy([0 : 6], TEB);
hold on
semilogy([0 : 6], (4 * (1 - (1 / sqrt(4))) * qfunc(sqrt(3 * log2(4)* 10 .^ ([0 : 6] / 10) / (3)))) / log2(4));
grid
title('Figure 6 : Comparaison des les TEB');
legend('TEB Estimé','TEB Théorique')
ylabel('TEB');