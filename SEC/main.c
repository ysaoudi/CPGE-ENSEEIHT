#include <stdio.h>  /* entrées sorties */
#include <unistd.h> /* pimitives de base : fork, ...*/
#include <stdlib.h> /* exit */
#include <signal.h> /* traitement des signaux */
#include "readcmd.h"
#include <sys/wait.h>
#include <string.h>
#include <errno.h>
#include "myJobs.h" //Contient tout ce qui est relatif au stockage des processus, leur informations ainsi que leur affichage

/**
 * @author Younes SAOUDI 1SN-D
 * miniShell
 **/

pList processList; //La liste des eventuels processus
bool executionFG;//Verifier si l'execution se fait en FG
int pidCHILDFG; //le PID du fils executant en FG
char* commandFG;//Commande exécutée en FG

void suivi_pere (int sig) {
    //Si l'execution se fait en FG et que CTRL Z est tapé
    if (executionFG && sig == SIGTSTP){
        //executionFG = false;
        printf(" Suspension du processus\n");
        kill(pidCHILDFG, SIGSTOP);
    } 
    //Si l'execution se fait en FG et que CTRL C est tapé
    else if (executionFG && sig == SIGINT){
        printf(" Terminaison du processus\n");
        executionFG = false;
        kill(pidCHILDFG, SIGKILL);
        if (pExists(processList, pidCHILDFG)){
        	popCommand(&processList, pidCHILDFG);
        }
    }
    //Si l'execution ne se fait pas en FG et que CTRL C est tapé
    else if (!executionFG && sig == SIGINT){
        NULL; //Ne rien faire, puisque le miniShell est libre!
    }
}

void suivi_fils (int sig) {

    int etat_fils, pid_fils;

    do {

        pid_fils = (int) waitpid(-1, &etat_fils, WNOHANG | WUNTRACED | WCONTINUED);

        if ((pid_fils == -1) && (errno != ECHILD)) {

            perror("waitpid");

            exit(EXIT_FAILURE);

        } else if (pid_fils > 0) {

            //if (WIFSTOPPED(etat_fils)) {
                /* traiter la suspension */
                //updateCommand(&processList, pid_fils, Interrupted);

            if (WIFCONTINUED(etat_fils)) {
                /* traiter la reprise */
                if (pExists(processList, pid_fils)) {
                    updateCommand(&processList, pid_fils, Running);
                }

            } else if ( WIFSTOPPED(etat_fils) || WIFSIGNALED(etat_fils)) {
                /* trailer l'arrêt */
                if (pExists(processList, pid_fils)) {
                    updateCommand(&processList, pid_fils, Stopped);
                }
            } else if (WIFEXITED(etat_fils)){
                if (pExists(processList, pid_fils)) {
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
    while (1)
    {
        //MODIFICATION QUESTION 2
        printf("\033[0;32m"); //VERT
        printf("ysaoudi");
        printf("\033[0m"); //DEFAUT
        printf(":");
        printf("\033[0;34m"); //BLEU
        printf("%s", getcwd(s,200)); //Repertoire de travail
        printf("\033[0m"); //DEFAUT
        printf("$ ");
        //FIN QUESTION 2
        cmd = readcmd();
        
        //MODIFICATION QUESTION 4 ET 6
        if (strcmp(cmd->seq[0][0], "cd") == 0){
            chdir(cmd->seq[0][1]);
            continue;
        } else if (strcmp(cmd->seq[0][0], "exit") == 0){
            exit(0);
        } else if (strcmp(cmd->seq[0][0], "list") == 0){
            jobs(&processList);
            continue;
        } else if (strcmp(cmd->seq[0][0], "stop") == 0){
            if (cmd->seq[0][1] == NULL){
                printf("Il faut préciser le numéro du processus!\n");
                continue;
            } else {
                char* strPID = malloc(sizeof(cmd -> seq [0][1]));
            	strcpy(strPID, cmd -> seq [0][1]);
                int PID;
                sscanf(strPID, "%d", &PID);

                if (pExists(processList, PID)){
                    //stop le processus
                    kill(PID, SIGSTOP);
                    continue;
                } else {
                    printf("Ce processus n'existe pas!\n");
                    continue;
                }
            }
        } else if (strcmp(cmd->seq[0][0], "bg") == 0){
            if (cmd->seq[0][1] == NULL){
                printf("Il faut préciser le numéro du processus!\n");
                continue;
            } else {
                char* strPID = malloc(sizeof(cmd -> seq [0][1])); //pid String
            	strcpy(strPID, cmd -> seq [0][1]);
                int PID;
                sscanf(strPID, "%d", &PID); //conversion du pid String en pid int
                
                if (pExists(processList, PID)){
        
                    //reprendre le processus mais en arrière plan
                    kill(PID, SIGCONT);
                    continue;
                } else {
                    printf("Ce processus n'existe pas!\n");
                    continue;
                }
            }
        } else if (strcmp(cmd->seq[0][0], "fg") == 0){
            if (cmd->seq[0][1] == NULL){
                printf("Il faut préciser le numéro du processus!\n");
                continue;
            } else {
                char* strPID = malloc(sizeof(cmd -> seq [0][1]));
            	strcpy(strPID, cmd -> seq [0][1]);
                int PID;
                sscanf(strPID, "%d", &PID);
                
                if (pExists(processList, PID)){
        
                    //reprendre le processus mais en avant plan
                    int etat_fils;
                    waitpid(PID, &etat_fils, WNOHANG | WUNTRACED | WCONTINUED);
                    if ( WIFSTOPPED(etat_fils) || WIFSIGNALED(etat_fils)) {
                        kill(PID, SIGCONT);
                        waitpid(PID, 0, 0);
                    	updateCommand(&processList, PID, Done);
                    }
                    
                    continue;
                } else {
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
            signal(SIGTSTP, SIG_IGN); //Ignorer CTRL Z
            signal(SIGINT, SIG_IGN); //Ignorer CTRL C
            signal(SIGCHLD, suivi_fils);

            if (execvp(cmd->seq[0][0], cmd->seq[0]) < 0) {
		        printf("%s: Unknown Command!\n", cmd->seq[0][0]);
		        exit(EXIT_FAILURE);
            } else{
                exit(EXIT_SUCCESS);
            }
            //execvp(cmd->seq[0][0], cmd->seq[0]); //Executer la commande
            //exit(EXIT_SUCCESS);                  /* Terminaison normale (0 = sans erreur) */
        }

        /* pere */
        else
        {
            //MODIFICATION QUESTION 5
            if ( cmd -> backgrounded == NULL){
                executionFG = true;
                pidCHILDFG = retour;
                char* commandFG = malloc(sizeof(cmd -> seq [0][0]));
            	strcpy(commandFG, cmd -> seq [0][0]);
            	addCommand(&processList, retour, commandFG);
            	updateCommand(&processList, retour, Done);
                wait(NULL);               
		
            } else {
            	
                executionFG = false;
            	char* newCommand = malloc(sizeof(cmd -> seq [0][0]));
            	strcpy(newCommand, cmd -> seq [0][0]);
                addCommand(&processList, retour, newCommand); //Ajout de la commande à liste de processus
            }
            //FIN QUESTION 5
        }
    }
    return EXIT_SUCCESS;
}
