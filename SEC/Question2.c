#include <stdio.h>  /* entrées sorties */
#include <unistd.h> /* pimitives de base : fork, ...*/
#include <stdlib.h> /* exit */
#include <signal.h> /* traitement des signaux */
#include "readcmd.h"
#include <sys/wait.h>

/**
 * @author Younes SAOUDI 1SN-D
 * Question 2
 * Construire une session simple (utilisant le code écrit pour la question 1) mettant en évidence le fait que l'affichage de l'invite précède ou se mêle à 
 * l'exécution du processus fils.
 **/
int main()
{
    char s[200];
    int retour;
    struct cmdline *cmd;

    while (1)
    {
        //MODIFICATION QUESTION 2
        printf("ysaoudi:");
        printf("%s$\n", getcwd(s,200));
        //FIN QUESTION 2
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
