#include <stdio.h>  /* entrées sorties */
#include <unistd.h> /* pimitives de base : fork, ...*/
#include <stdlib.h> /* exit */
#include <signal.h> /* traitement des signaux */
#include <sys/wait.h>
#include "readcmd.h"

/**
 * @author Younes SAOUDI 1SN-D
 * Question 1
 * Réaliser la boucle de base de l'interpréteur, en se limitant à des commandes simples (pas d'opérateurs de composition), sans motifs pour les noms de fichiers.
 **/
int main()
{
    int retour;
    struct cmdline *cmd;

    while (1)
    {

        cmd = readcmd();
        retour = fork();

        /* Bonne pratique : tester systématiquement le retour des appels système */
        if (retour < 0)
        { /* échec du fork */
            printf("Erreur fork\n");
            /* Convention : s'arrêter avec une valeur > 0 en cas d'erreur */
            exit(1);
        }

        /* fils */
        if (retour == 0)
        {
            execvp(cmd->seq[0][0], cmd->seq[0]); //Executer la commande
            exit(EXIT_SUCCESS);                  /* Terminaison normale (0 = sans erreur) */
        }

        /* pere */
        else
        {
            //Ne rien faire
        }
    }
    return EXIT_SUCCESS;
}
