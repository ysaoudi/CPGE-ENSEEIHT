#include <stdio.h>  /* entrées sorties */
#include <unistd.h> /* pimitives de base : fork, ...*/
#include <stdlib.h> /* exit */
#include <signal.h> /* traitement des signaux */
#include "readcmd.h"
#include <sys/wait.h>

/**
 * @author Younes SAOUDI 1SN-D
 * Question 5
 * Ajouter la possibilité du lancement de commandes en tâche de fond, spécifié par un & en fin de ligne.
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

        //MODIFICATION QUESTION 4
        if (strcmp(cmd->seq[0][0], "cd") == 0)
        {
            chdir(cmd->seq[0][1]);
            continue;
        }
        else if (strcmp(cmd->seq[0][0], "exit") == 0)
        {
            exit(0);
        }
        //FIN QUESTION 4

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
            //MODIFICATION QUESTION 5
            if ( cmd -> backgrounded == NULL){
                wait(NULL);
            } else {
                //RIEN
            }
            //FIN QUESTION 5
        }
    }
    return EXIT_SUCCESS;
}
