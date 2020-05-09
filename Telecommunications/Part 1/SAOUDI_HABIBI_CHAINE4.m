% ENSEEIHT - 1SN - Télécommunications
% Etudes de chaines de transmission en BdB
% Auteurs : Younes SAOUDI & Issam HABIBI

clear;
close all;
clc;


%IV- Choix du MAPPING


%Les données

bits_nbr = 10000; %Nombre de bits générés
Ns = 8;             %Condition de shannon Fe>=2.Fmax et Fmax = 4/Ts
Fe = 1;             % Fréquence d'échantillonnage
Te = 1/Fe;             %Durée d'echatillonnage

%1.a-
bits = randi([0,1], 1, bits_nbr);                    %Génération de l'information binaire
symboles = (2 * bi2de(reshape(bits, 2, length(bits) / 2).')-3).';
Diracs = kron(symboles, [1 zeros( 1, Ns - 1)]); %Génération de la suite de Diracs pondérés par les symboles
h = ones(1, Ns);                                %Réponse impusionnelle du filtre de mise en forme
x = filter(h,1,Diracs); %Filtrage de mise en forme
figure
plot(x)
axis([0 bits_nbr - 1 -4 4]);
title("Figure 1 : Signal à la Sortie du filtre d'Emission");
xlabel('Temps en Secondes');
ylabel("Signal Généré");

% Calcul de la DSP
DSP_x = (1 / length(x)) * abs(fft(x, 2 ^ nextpow2(length(x)))) .^ 2;
figure;
plot(linspace(-Fe, Fe, length(DSP_x)), fftshift(DSP_x));
title('Figure 2 : DSP du Signal ');
xlabel('Fréquence en Hz');
ylabel('DSP');

%1.b-
Ns_1 = 8;
h_1 = ones(1, Ns_1);
hr_1 = fliplr(h_1);
Fe_1 = 1;
symboles_1 = 2*bits - 1;
Diracs_1 = kron(symboles_1, [1 zeros(1, Ns_1 - 1)]);
x1 = filter(h_1, 1, Diracs_1);
% Calcul de la DSP
DSP_x = (1 / length(x)) * abs(fft(x, 2 ^ nextpow2(length(x)))) .^ 2;
figure;
plot(linspace(-Fe, Fe, length(DSP_x)), fftshift(DSP_x));hold on;
%DSP de la Chaîne de Référence
DSP_x1 = (1 / length(x1)) * abs(fft(x1, 2 ^ nextpow2(length(x1)))) .^ 2;
plot(linspace(-Fe, Fe, length(DSP_x1)), fftshift(DSP_x1));
ylabel('DSP');
xlabel('Fréquence Normalisée par rapport à Rs');
legend('DSP','DSP de la chaîne de référence');
title('Figure 3 :Les DSP des signaux de la chaîne de référence et de la chaîne actuelle');

 
%1-c-
%Filtrage de réception
hr = fliplr(h);
y = filter(hr,1,x);

% Diagramme de l'oeil
oeil = eyediagram(y,2*Ns,2*Ns,Ns-1);
title('Figure 4 : Diagramme de lOeil');
xlabel('temps en (s)');


%1-d/

%TES & TEB

signal_echantillonne = y(Ns : Ns : end);
symboles_decides = zeros(1, length(signal_echantillonne));
for i=1:length(signal_echantillonne)
    
    if (signal_echantillonne(i) > 2*Ns)
        symboles_decides(i) = 3;
    elseif (signal_echantillonne(i) >= 0)
        symboles_decides(i) = 1;
    elseif (signal_echantillonne(i) < - 2*Ns)
        symboles_decides(i) = -3;
    else
        symboles_decides(i) = -1;
    end
end
%TES
bits_decides = reshape(de2bi((symboles_decides + 3)/2).',1,length(bits));
%TEB
TEB1 = length(find(bits_decides ~= bits))/length(bits)


%2.
%Implantation de la chaine avec bruit
TES_b = zeros(1,7);
TEB_bruit = zeros(1,7);

for i = 0:6
    sigma_n_carre = mean(x.^2) * Ns / (4 *10^(i/10));
    Bruit = sqrt(sigma_n_carre) * randn(1, length(x));
    signal_bruite = x + Bruit;
    y_b = filter(hr, 1, signal_bruite);
    y_echantillone = y_b(Ns : Ns : end);
    symboles_decides = zeros(1, length(y_echantillone));
    
    for j=1:length(y_echantillone)
        if (y_echantillone(j) > 2*Ns)
            symboles_decides(j) = 3;
        elseif (y_echantillone(j) >= 0)
            symboles_decides(j) = 1;
        elseif (y_echantillone(j) < -2*Ns)
            symboles_decides(j) = -3;
        else
            symboles_decides(j) = -1;
        end
    end
    %TES
    err = length(find(symboles ~= symboles_decides));
    TES_b(i+1) = err/length(symboles);
    %TEB
    bits_decides = reshape(de2bi((symboles_decides + 3)/2)',1,length(bits));
    TEB_b(i+1) = length(find(bits ~= bits_decides))/length(bits);
end

% TES en fonction de Eb/N0
figure
semilogy([0:6], TES_b, 'r-');
grid;
title("Figure 5 : TES");
xlabel('Eb/N0 (dB)');
ylabel('TES');


%3.
%TES Expérimental et TES Théorique
figure
semilogy([0:6], TES_b, 'r-*');
hold on
TES_theorique = (3/2)*qfunc(sqrt((4/5)*10.^([0:6]/10)));
semilogy([0 : 6], TES_theorique,'b-o');
grid
title('Figure 6 : Comparaison des TES');
legend('TES Expérimental','TES Théorique')
ylabel('TES');


%4.
% TEB
figure
semilogy([0:6], TEB_b, 'r-');
grid;
title('Figure 7 : TEB');
xlabel('Eb/N0 (dB)');
ylabel('TEB');

%5.
%Comparaison des TEB
figure
semilogy([0:6], TEB_b, 'r-*');
hold on
TEB_theorique = (3/4)*qfunc( sqrt( (4/5) * 10.^([0:6]/10)) );
semilogy([0 : 6], TEB_theorique,'b-o');
grid
title('Figure 8 : Comparaison des TEB');
legend('TEB Expérimental','TEB Théorique')
ylabel('TEB');