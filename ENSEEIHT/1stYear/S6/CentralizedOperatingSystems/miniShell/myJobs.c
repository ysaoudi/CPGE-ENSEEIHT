#include <stdio.h>  /* entrées sorties */
#include <stdlib.h> /* exit */
#include <string.h>
#include "myJobs.h"
/**
 * @author Younes SAOUDI 1SN-D
 * Tout ce qui est relatif au stockage des informations des processus et leur affichage.
 **/

void initializeList(pList* processList){
    processList->size = 0;
    processList->pIDs = malloc(sizeof(int));
    processList->pStatuses = malloc(sizeof(pStatus));
    char ** array=(char **)malloc(sizeof(*array) );
    processList->commands = malloc(sizeof(*array));
}

bool pExists(pList processList, int pID){
    int index = 1;
    while (index <= processList.size){
        if(processList.pIDs[index] == pID){
            return true;
        }
        index ++;
    }
    return false;
}

void addCommand(pList* processList, int newpID, char* newcmd){
    int* newpList = (int *) realloc(processList->pIDs, (processList->size + 2) * sizeof(int));

    pStatus* newStatuses = realloc(processList->pStatuses, (processList->size + 2) * sizeof(pStatus));

    char** array = processList->commands;
    char** newCommands = (char **) realloc(processList->commands, (processList->size + 2) * sizeof(*array));
    
    if (newpList && newStatuses && newCommands){
        processList->pIDs = newpList;
        processList->size ++;
        processList->pIDs[processList->size] = newpID;

        processList->pStatuses = newStatuses;
        processList->pStatuses[processList->size] = Running;

        processList->commands = newCommands;
        processList->commands[processList->size] = newcmd;
    }
}

void updateCommand(pList* processList, int processID, pStatus newStatus){
    int index = 0;
    while (processList->pIDs[index] != processID){
        index ++;
    }
    processList->pStatuses[index] = newStatus;
}

void popCommand(pList *processList, int deadID){
    if(processList->size == 1){
        initializeList(processList);
    } else{
        if (processList -> pIDs[processList->size] != deadID){

            int index = 1;
            while (index < processList->size && processList->pIDs[index] != deadID){
                index ++;   
            }
                
            while(index < processList->size){
                processList->pIDs[index] = processList->pIDs[index + 1];
                processList->commands[index] = processList->commands[index + 1];
                processList->pStatuses[index] = processList->pStatuses[index + 1];
                index ++;                
            }
        }

        int* newpList = (int *) realloc(processList->pIDs, (processList->size - 1) * sizeof(int));

        pStatus* newStatuses = realloc(processList->pStatuses, (processList->size - 1) * sizeof(pStatus));
     
        char** array = processList->commands;
        char** newCommands = (char **) realloc(processList->commands, (processList->size) * sizeof(*array));
        
        if(newpList && newStatuses && newCommands){
            processList->pIDs = newpList;
            processList->size --;

            processList->pStatuses = newStatuses;

            processList->commands = newCommands;
        }
    }
}

void popDoneCommands(pList* processList){
    int index = 1;
    while (index <= processList->size){
        pStatus statusEnum = processList->pStatuses[index];
        char *status;
        switch (statusEnum){
            case Done: status = "Done";break;
            case Running: status = "Running"; break;
            case Stopped: status = "Stopped"; break;
        }
        if (strcmp(status, "Done") == 0){
            popCommand(processList, processList->pIDs[index]);
        }
        index ++;
    }
}

void jobs(pList* processList){

    int size = processList->size;

    for (int i = 1; i <= size; i++){

        int processID = processList->pIDs[i];
        pStatus statusEnum = processList->pStatuses[i];
        char *command = processList->commands[i];
        //printf("%s\n", command);
        //printf("OK COMMAND\n");
        char *status;

        switch (statusEnum){
            case Done: status = "Done";break;
            case Running: status = "Running"; break;
            case Stopped: status = "Stopped"; break;
        }
        printf("[%d]\t %d\t %s\t ", i, processID, status);
        if (strcmp(status, "Done") == 0){
            printf("\t ");
        }
        printf("%s\n", command);
    }
    popDoneCommands(processList);
}