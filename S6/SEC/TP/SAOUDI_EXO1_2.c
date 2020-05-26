#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <signal.h>

void handler_sigusr1(int sign_num)
{
    printf("\n  Processus de pid %d : J'ai reçu le signal %d\n",
           getpid(), sign_num);
    return;
}

void dormir(int nb_sec)
{
    int duree = 0;
    while (duree < nb_sec)
    {
        sleep(1);
        duree++;
    }
}

int main()
{
    sigset_t ens1, ens2;
    sigemptyset(&ens2);
    sigemptyset(&ens1);
    sigaddset(&ens2, SIGINT);
    sigaddset(&ens1, SIGUSR1);
    signal(SIGUSR1, handler_sigusr1);
    signal(SIGUSR2, handler_sigusr1);
    printf("\nJe suis le processus principal de pid %d\n", getpid());
    printf("\n Je masque SIGINT\n", getpid());
    sigprocmask(SIG_BLOCK, &ens2, NULL);
    printf("\n Je masque SIGUSR1\n", getpid());
    sigprocmask(SIG_BLOCK, &ens1, NULL);
    kill(getpid(), SIGUSR1);
    kill(getpid(), SIGUSR1);
    dormir(5);
    kill(getpid(), SIGUSR2);
    kill(getpid(), SIGUSR2);
    dormir(10);
    printf("\n Je démasque SIGUSR1\n", getpid());
    sigprocmask(SIG_UNBLOCK, &ens1, NULL);
    dormir(10);
    printf("\n Je démasque SIGINT\n", getpid());

    sigprocmask(SIG_UNBLOCK, &ens2, NULL);

    printf("\nTeminaison du programme\n");

    return EXIT_SUCCESS;
}
