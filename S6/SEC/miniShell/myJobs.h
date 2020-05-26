#include <stdbool.h>
#ifndef _MYJOBS_H
#define _MYJOBS_H

    
    enum pStatus{Done, Running, Stopped};
    /**
     * Les différents status d'un processus quelconque
     * */
    typedef enum pStatus pStatus;

    
    struct pList{
        int* pIDs;
        int size;
        pStatus* pStatuses;
        char** commands;
    };
    /**
     * La table de processus et leur informations
     * */
    typedef struct pList pList;

    /**
     * Initialiser la liste de processus
     * @param processList La future liste de processus
     * */
    void initializeList(pList* processList);

    /**
     * Tester l'existence d'un processus dans le liste
     *  @param processList La liste des processus
     * @param pID Le processus dont on veut vérifier l'existence
     **/
    bool pExists(pList processList, int pID);

    /**
     * Ajouter un processus à la liste de processus
     * @param processList La liste de processus
     * @param newPID L'ID du nouveau processus
     * @param newCommand La commande qui a engendré ce processus
     * */
    void addCommand(pList* processList, int newpID, char* newcmd);

    /**
     * Supprimer un processus de la liste de processus
     * @param processList La liste de processus
     * @param deadID L'ID du nouveau processus
     * */
    void popCommand(pList* processList, int deadID);

    /**
     * Supprimer les processus terminés de la liste de processus
     * @param processList La liste de processus
     * */
    void popDoneCommands(pList* processList);

    /**
     * Modifier l'état d'un processus dans la liste de processus
     * @param processList
     * */
    void updateCommand(pList* processList, int processID, pStatus newStatus);

    /**
     * Mimiquer la commande jobs du shell (afficher les processus) 
     * */
    void jobs(pList* processList);

#endif