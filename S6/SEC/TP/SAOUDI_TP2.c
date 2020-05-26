#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <unistd.h>
#include <sys/wait.h>

int main(int argc, char *argv[]) {
  int codeTerm;
  pid_t pidFils, idFils;

  while (1){
  
      pidFils = fork();
      if (pidFils == -1) {
        printf("Erreur fork!\n");
        exit(1);
      }
      if (pidFils == 0) { 
        char buf[30]; 
        printf("Veuillez saisir la commande à éxecuter :\n");
        scanf("\%s",buf);
        execlp(buf,"",NULL);
        perror("Erreur!");
        exit(1);
      }
      else {   /* père */
        idFils=wait(&codeTerm);
        if (idFils == -1) {
          perror("wait ");
          exit(2);
        }
        if (codeTerm == 0) {
          printf("La commande a bien été exécutée!\n");
        } else {
          printf("L'exécution de la commande a échoué!\n");
        }
      }
  }
}