#include <stdio.h>  /* entrées sorties */
#include <unistd.h> /* pimitives de base : fork, ...*/
#include <stdlib.h> /* exit */
#include <signal.h> /* traitement des signaux */
#include "readcmd.h"
#include <sys/wait.h> /* wait */
#include <string.h>   /* opérations sur les chaînes */
#include <errno.h>
#include <fcntl.h>  /* opérations sur les fichiers */
#include "myJobs.h" //Contient tout ce qui est relatif au stockage des processus, leur informations ainsi que leur affichage

/**
 * @author Younes SAOUDI 1SN-D
 * miniShell
 **/

pList processList; //La liste des eventuels processus
bool executionFG;  //Verifier si l'execution se fait en FG
int pidCHILDFG;    //le PID du fils executant en FG
char *commandFG;   //Commande exécutée en FG

void suivi_pere(int sig)
{
    //Si l'execution se fait en FG et que CTRL Z est tapé
    if (executionFG && sig == SIGTSTP)
    {
        //executionFG = false;
        printf(" Suspension du processus\n");
        int fils = fork(); //Le père attend qu'un fils quelconque finisse, on va donc en créer un pour le terminer immédiatement (sinon le père attendre pidCHILDFG alors qu'il sera suspendu)
        if (fils == 0)
        { //Nouveau Fils
            exit(EXIT_SUCCESS);
        }
        else if (fils > 0)
        { //Père
            kill(fils, SIGKILL);
            kill(pidCHILDFG, SIGSTOP);
        }
        else
        {
            /* échec du fork */
            printf("Erreur fork\n");
            /* Convention : s'arrêter avec une valeur > 0 en cas d'erreur */
            exit(1);
        }
    }
    //Si l'execution se fait en FG et que CTRL C est tapé
    else if (executionFG && sig == SIGINT)
    {
        printf(" Terminaison du processus\n");
        executionFG = false;
        kill(pidCHILDFG, SIGKILL);
        if (pExists(processList, pidCHILDFG))
        {
            popCommand(&processList, pidCHILDFG);
        }
    }
    //Si l'execution ne se fait pas en FG et que CTRL C est tapé
    else if (!executionFG && sig == SIGINT)
    {
        NULL; //Ne rien faire, puisque le miniShell est libre!
    }
}

void suivi_fils(int sig)
{

    int etat_fils, pid_fils;

    do
    {

        pid_fils = (int)waitpid(-1, &etat_fils, WNOHANG | WUNTRACED | WCONTINUED);

        if ((pid_fils == -1) && (errno != ECHILD))
        {

            perror("waitpid");

            exit(EXIT_FAILURE);
        }
        else if (pid_fils > 0)
        {

            //if (WIFSTOPPED(etat_fils)) {
            /* traiter la suspension */
            //updateCommand(&processList, pid_fils, Interrupted);

            if (WIFCONTINUED(etat_fils))
            {
                /* traiter la reprise */
                if (pExists(processList, pid_fils))
                {
                    updateCommand(&processList, pid_fils, Running);
                }
            }
            else if (WIFSTOPPED(etat_fils) || WIFSIGNALED(etat_fils))
            {
                /* trailer l'arrêt */
                if (pExists(processList, pid_fils))
                {
                    updateCommand(&processList, pid_fils, Stopped);
                }
            }
            else if (WIFEXITED(etat_fils))
            {
                if (pExists(processList, pid_fils))
                {
                    updateCommand(&processList, pid_fils, Done);
                }
            }
        }

    } while (pid_fils > 0);

    /* autres actions après le suivi des changements d'état */
}
int main()
{
    char s[200];
    int retour;
    struct cmdline *cmd;
    signal(SIGTSTP, suivi_pere);
    signal(SIGINT, suivi_pere);
    /* 
    sigset_t signaux;
    sigemptyset(&signaux);
    sigaddset(&signaux, 20); 
    */

    initializeList(&processList);
    signal(SIGCHLD, suivi_fils);
    int desc_ent, desc_res, dupdesc; /* descripteurs de fichiers */
    //int pipe_cmd[2];
    /*retour = pipe(pipe_cmd);
    //traiter systématiquement le retour des appels système */
    /*if (retour == -1)
    {
        // échec du pipe
        perror("Erreur pipe\n");
        exit(1);
    }*/

    while (1)
    {
        //MODIFICATION QUESTION 2
        printf("\033[0;32m"); //VERT
        printf("ysaoudi");
        printf("\033[0m"); //DEFAUT
        printf(":");
        printf("\033[0;34m");         //BLEU
        printf("%s", getcwd(s, 200)); //Repertoire de travail
        printf("\033[0m");            //DEFAUT
        printf("$ ");
        //FIN QUESTION 2
        cmd = readcmd();

        //MODIFICATION QUESTION 4 ET 6
        if (strcmp(cmd->seq[0][0], "cd") == 0)
        {
            chdir(cmd->seq[0][1]);
            continue;
        }
        else if (strcmp(cmd->seq[0][0], "exit") == 0)
        {
            exit(0);
        }
        else if (strcmp(cmd->seq[0][0], "list") == 0)
        {
            jobs(&processList);
            continue;
        }
        else if (strcmp(cmd->seq[0][0], "stop") == 0)
        {
            if (cmd->seq[0][1] == NULL)
            {
                printf("Il faut préciser le numéro du processus!\n");
                continue;
            }
            else
            {
                char *strPID = malloc(sizeof(cmd->seq[0][1]));
                strcpy(strPID, cmd->seq[0][1]);
                int PID;
                sscanf(strPID, "%d", &PID);

                if (pExists(processList, PID))
                {
                    //stop le processus
                    kill(PID, SIGSTOP);
                    continue;
                }
                else
                {
                    printf("Ce processus n'existe pas!\n");
                    continue;
                }
            }
        }
        else if (strcmp(cmd->seq[0][0], "bg") == 0)
        {
            if (cmd->seq[0][1] == NULL)
            {
                printf("Il faut préciser le numéro du processus!\n");
                continue;
            }
            else
            {
                char *strPID = malloc(sizeof(cmd->seq[0][1])); //pid String
                strcpy(strPID, cmd->seq[0][1]);
                int PID;
                sscanf(strPID, "%d", &PID); //conversion du pid String en pid int

                if (pExists(processList, PID))
                {

                    //reprendre le processus mais en arrière plan
                    kill(PID, SIGCONT);
                    continue;
                }
                else
                {
                    printf("Ce processus n'existe pas!\n");
                    continue;
                }
            }
        }
        else if (strcmp(cmd->seq[0][0], "fg") == 0)
        {
            if (cmd->seq[0][1] == NULL)
            {
                printf("Il faut préciser le numéro du processus!\n");
                continue;
            }
            else
            {
                char *strPID = malloc(sizeof(cmd->seq[0][1]));
                strcpy(strPID, cmd->seq[0][1]);
                int PID;
                sscanf(strPID, "%d", &PID);

                if (pExists(processList, PID))
                {

                    //reprendre le processus mais en avant plan
                    //waitpid(PID, &etat_fils, WNOHANG | WUNTRACED | WCONTINUED);
                    //if ( WIFSTOPPED(etat_fils) || WIFSIGNALED(etat_fils)) {
                    kill(PID, SIGCONT);
                    waitpid(PID, 0, 0);
                    updateCommand(&processList, PID, Done);
                    //}
                    continue;
                }
                else
                {
                    printf("Ce processus n'existe pas!\n");
                    continue;
                }
            }
        }

        //FIN QUESTION 4 ET 6

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
            signal(SIGTSTP, SIG_IGN);    //Ignorer CTRL Z
            signal(SIGINT, SIG_IGN);     //Ignorer CTRL C
            signal(SIGCHLD, suivi_fils); //Attribuer la handler suivi_fils au signal SIGCHLD

            //S'il y a redirection de stdin ou stdout
            if (cmd->in != NULL || cmd->out != NULL)
            {
                if (cmd->in != NULL && cmd->err == NULL) //S'il y a redirection de stdin vers un fichier
                {
                    /*ouvrir le fichier des entrées */
                    desc_ent = open(cmd->in, O_RDONLY);
                    /* traiter systématiquement les retours d'erreur des appels */
                    if (desc_ent < 0)
                    {
                        fprintf(stderr, "Erreur ouverture %s\n", cmd->in);
                        exit(1);
                    }
                    dupdesc = dup2(desc_ent, 0);
                    if (dupdesc == -1)
                    {
                        /* échec du dup2 */
                        perror("Erreur dup2\n");
                        exit(5);
                    }
                }
                if (cmd->out != NULL && cmd->err == NULL) //S'il y a rediréction de stdout vers un fichier
                {

                    /* ouvrir le fichier résultats */
                    desc_res = open(cmd->out, O_WRONLY | O_CREAT | O_TRUNC, 0640);
                    /* traiter systématiquement les retours d'erreur des appels */
                    if (desc_res < 0)
                    {
                        fprintf(stderr, "Erreur ouverture %s\n", cmd->out);
                        exit(2);
                    }
                    dupdesc = dup2(desc_res, 1);
                    if (dupdesc == -1)
                    {
                        /* échec du dup2 */
                        perror("Erreur dup2\n");
                        exit(5);
                    }
                }
            }
            //S'il y a un pipe
            if (cmd->seq[1] != NULL)
            {
                printf("u fucked up\n");
                int pipe_cmd[2];
                int retour_pipe = pipe(pipe_cmd);
                //traiter systématiquement le retour des appels système */
                if (retour_pipe == -1)
                {
                    // échec du pipe
                    perror("Erreur pipe\n");
                    exit(1);
                }
                int petit_fils = fork();
                if (petit_fils == -1)
                {
                    perror("Erreut fork\n");
                }
                else if (petit_fils == 0)
                {
                    close(pipe_cmd[0]);
                    //petit fils
                    dupdesc = dup2(pipe_cmd[1], 1);
                    if (dupdesc == -1)
                    {
                        /* échec du dup2 */
                        perror("Erreur dup3\n");
                        exit(5);
                    }
                    close(pipe_cmd[1]);
                    if(execvp(cmd->seq[0][0], cmd->seq[0]) < 0)
                    {
                        printf("%s: Unknown Command!\n", cmd->seq[0][0]);
                        exit(EXIT_FAILURE);
                    }
                    else
                    {
                        exit(EXIT_SUCCESS);
                    }
                }
                else
                {
                    //fils (père du petit fils)
                    dupdesc = dup2(pipe_cmd[0], 0);
                    if (dupdesc == -1)
                    {
                        /* échec du dup2 */
                        perror("Erreur dup3\n");
                        exit(5);
                    }
                    close(pipe_cmd[1]);
                    if (execvp(cmd->seq[1][0], cmd->seq[1]) < 0)
                    {
                        printf("%s: Unknown Command!\n", cmd->seq[1][0]);
                        exit(EXIT_FAILURE);
                    }
                    else
                    {
                        exit(EXIT_SUCCESS);
                    }
                }
            }

            //Execution de la commande (n'arrive jamais ici dans le cas d'un pipe)
            if (execvp(cmd->seq[0][0], cmd->seq[0]) < 0)
            {
                printf("%s: Unknown Command!\n", cmd->seq[0][0]);
                exit(EXIT_FAILURE);
            }
            else
            {
                exit(EXIT_SUCCESS);
            }
            //execvp(cmd->seq[0][0], cmd->seq[0]); //Executer la commande
            //exit(EXIT_SUCCESS);                  /* Terminaison normale (0 = sans erreur) */
        }

        /* pere */
        else
        {
            //MODIFICATION QUESTION 5
            if (cmd->backgrounded == NULL)
            {
                executionFG = true;
                pidCHILDFG = retour;
                char *commandFG = malloc(sizeof(cmd->seq[0][0]));
                strcpy(commandFG, cmd->seq[0][0]);
                addCommand(&processList, retour, commandFG);
                updateCommand(&processList, retour, Done);
                wait(NULL);
            }
            else
            {

                executionFG = false;
                char *newCommand = malloc(sizeof(cmd->seq[0][0]));
                strcpy(newCommand, cmd->seq[0][0]);
                addCommand(&processList, retour, newCommand); //Ajout de la commande à liste de processus
            }
            //FIN QUESTION 5
        }
    }
    return EXIT_SUCCESS;
}
