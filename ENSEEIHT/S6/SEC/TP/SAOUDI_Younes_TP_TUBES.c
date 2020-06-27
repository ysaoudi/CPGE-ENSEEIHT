/*------------------------------------------------------------------------------*/
/* ENSEEIHT - 1SN - Systèmes d'Exploitation Centralisés                     	*/
/* TP - Tubes et Rediréctions													*/
/* Auteur : Younes SAOUDI														*/
/* Groupe : 1SN -D                                                          	*/
/*------------------------------------------------------------------------------*/


#include <stdio.h>	  	/* entrées sorties */
#include <unistd.h>	  	/* pimitives de base : fork, ...*/
#include <stdlib.h>	  	/* exit */
#include <sys/wait.h> 	/* wait */
#include <string.h>	  	/* opérations sur les chaines */
#include <fcntl.h>		/* opérations sur les fichiers */
#include <signal.h> 	/* traitement des signaux */


int retour; //le PID du fils
/* 3 Commandes à éxecuter -> 2 pipes */
int pipe1[2];
int pipe2[2];

/**
 *Execution de la commande who et rediréction de son résultat vers l'entrée de la commande suivante (grep) 
 **/
void exec_who()
{

	/* rediriger sdout vers l'entrée pipe1[1] */
	/* écririture sur stdout => écririture sur pipe1[1] */
	dup2(pipe1[1], 1);
	
	// fermeture des pipes inutiles
	close(pipe1[0]);
	close(pipe1[1]);
	
	// execution de la commande who
	execlp("who", "who", NULL);
	
	/* on ne se retrouve ici que si exec échoue */
	/* perror : affiche un message relatif à l'erreur du dernier appel systàme */
	perror("Erreur execution who");
	exit(6);
}

/**
 *Execution de la commande grep $nom_utilisateur et rediréction de son résultat vers l'entrée de la commande suivante (wc -l)
 *@param utilisateur le nom de l'utilisateur
 **/
void exec_grep(char *utilisateur)
{
	/* rediriger stdin vers l'entrée pipe1[0] */
	/* lecture sur stdin => lecture sur pipe1[0] */
	dup2(pipe1[0], 0);
	
	/* rediriger sdout vers l'entrée pipe2[1] */
	/* écririture sur stdout => écririture sur pipe2[1] */
	dup2(pipe2[1], 1);
	
	// fermeture des pipes inutiles
	close(pipe1[0]);
	close(pipe1[1]);
	close(pipe2[0]);
	close(pipe2[1]);
	
	// execution de la commande grep $nom_utilisateur
	execlp("grep", "grep", utilisateur, NULL);
	
	/* on ne se retrouve ici que si exec échoue */
	/* perror : affiche un message relatif à l'erreur du dernier appel systàme */
	fprintf(stderr, "Erreur execution grep %s\n", utilisateur);
	exit(7);
}

/**
 *Execution de la commande wc -l et rediréction de son résultat vers la sortie standard stdout
 **/
void exec_wc_l()
{
	/* rediriger stdin vers l'entrée pipe2[0] */
	/* lecture sur stdin => lecture sur pipe2[0] */
	dup2(pipe2[0], 0);
	
	// fermeture des pipes inutiles
	close(pipe2[0]);
	close(pipe2[1]);
	
	// execution de la commance wx -l
	execlp("wc", "wc", "-l", NULL);
	
	/* on ne se retrouve ici que si exec échoue */
	/* perror : affiche un message relatif à l'erreur du dernier appel systàme */
	perror("Erreur execution wc -l");
	exit(8);
}

/**
 * @author Younes SAOUDI 1SN-D
 *Execution de la commande who | grep $nom_utilisateur | wc -l en utilisant des pipes. Le résultat est rédirigé vers la sortie standard stdout.
 *@param argv Contient, dans la position 1, le nom de l'utilisateur qu'utilisera la commande grep
 */
int main(int argc, char *argv[])
{
	char *utilisateur = argv[1]; //Le nom d'utilisteur donné en argument

	// création du pipe 1
	/* traiter systématiquement le retour des appels système */
	if (pipe(pipe1) == -1)
	{
		/* échec du pipe */
		perror("Erreur pipe1\n");
		exit(1);
	}

	// création d'un fils
	/* Bonne pratique : tester systématiquement le retour des appels système */
	if ((retour = fork()) == -1)
	{
		/* échec du fork */
		perror("Erreur fork1\n");
		exit(2);
	}
	else if (retour == 0)
	{
		/* fils */
		// execution de la commande who
		exec_who();
	}
	else
	{
		/* père */

		// création du pipe 2
		/* traiter systématiquement le retour des appels système */
		if (pipe(pipe2) == -1)
		{
			/* échec du pipe */
			perror("Erreur pipe2\n");
			exit(3);
		}

		// création d'un fils
		/* Bonne pratique : tester systématiquement le retour des appels système */
		if ((retour = fork()) == -1)
		{
			/* échec du fork */
			perror("Erreur fork2\n");
			exit(4);
		}
		else if (retour == 0)
		{
			/* fils */
			// execution de la commande grep $nom_utilisateur sur le résultat de la commande who
			exec_grep(utilisateur);
		}
		else
		{
			/* père */

			// fermeture des pipes inutiles
			close(pipe1[0]);
			close(pipe1[1]);

			// création d'un fils
			/* Bonne pratique : tester systématiquement le retour des appels système */
			if ((retour = fork()) == -1)
			{
				/* échec du fork */
				perror("Erreur fork3\n");
				exit(5);
			}
			else if (retour == 0)
			{
				/* fils */
				//execution de la commande wc -l sur le résultat de la commande grep $nom_utilisateur
				exec_wc_l();
			}
			else
			{
				/* père */
				wait(&retour); //Attendre la fin du dernier processus pour ne pas perturber l'affichage (ça ne fonctionne pas et je ne sais pas du tout pourquoi)
				exit(EXIT_SUCCESS);
			}
		}
	}
}
