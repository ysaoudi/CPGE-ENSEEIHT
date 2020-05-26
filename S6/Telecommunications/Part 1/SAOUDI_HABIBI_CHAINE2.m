% ENSEEIHT - 1SN - Télécommunications
% Etudes de chaines de transmission en BdB
% Auteurs : Younes SAOUDI & Issam HABIBI

clear;
close all;
clc;


% II- Choix du Filtre de Réception

%Les données
bits_nbr = 10000;   %Nombre de bits générés
Ns = 8;             %Condition de shannon Fe>=2.Fmax et Fmax = 4/Ts
Te = 1;             %Durée d'echatillonnage
Fe = 1;             % Fréquence d'échantillonnage

% 1.a-
bits = randi([0,1], 1, bits_nbr);                    %Génération de l'information binaire
symboles = 2*bits - 1;                              %Mapping binaire (moyenne nulle)
Diracs = kron(symboles, [1 zeros( 1, Ns - 1)]); %Génération de la suite de Diracs pondérés par les symboles
h = ones(1, Ns);                                %Réponse impusionnelle du filtre de mise en forme


x = filter(h,1,Diracs); %Filtrage de mise en forme
hr = ones(1, Ns / 2) ;     %Filtrage de réception
y = filter(hr, 1, x);
figure;
plot(y);
title('Figure 1 : Signal en Sortie du Filtre de réception');

%1.b-
oeil = reshape(y, Ns, length(y)/Ns); %Diagramme de l'oeil
figure; 
plot(oeil);
axis([1 Ns -Ns-1 Ns+1]);
title("Figure 2 : Diagramme de l'oeil");
xlabel('Temps en secondes');

%1.c-
signal_echantillone = y( 3 * Ns/4 : Ns : end );
symboles_decides = sign(signal_echantillone);
TES = length(find(symboles_decides ~= symboles))/length(symboles)
bits_decides = (1+symboles_decides)/2;
TEB = length(find(bits_decides ~= bits))/length(bits)


%2-
%Chaine avec bruît
TEB_Exp = zeros(1,7);

for i=0:6
    F1 = mean( abs(x) .^2);    %Puissance du signal modulé
    sigma_n_carre1 = (F1 * Ns) / (2 * 10^(i/10));
    bruit1 = sqrt(sigma_n_carre1) * randn(1,length(x));
    z1 = x + bruit1;
    z2 = filter(hr, 1, z1);
    signal_echan2 = z2(3 * Ns/4 : Ns : end);    %échantillonnage en N_s
    symboles_decides_2 = sign(signal_echan2);
    bits_d2 = (symboles_decides_2 + 1) / 2;
    TEB_1=length( find(bits_d2 ~= bits) );
    TEB_Exp(1, i+1) = TEB_1 / length(bits);
end
figure;
semilogy([0:6],TEB_Exp,'r-');
title('Figure 3 : TEB Expérimental');

%3-
%TEB Expérimental et TEB Théorique
figure;
semilogy([0:6], TEB_Exp, 'r-');
hold on;
semilogy([0:6], qfunc( sqrt( 10.^ ([0:6]/10) ) ), 'b-');
hold off;
grid
title('Figure 4 : Comparaison des TEB');
legend('TEB Expérimental','TEB Théorique')
ylabel('TEB');