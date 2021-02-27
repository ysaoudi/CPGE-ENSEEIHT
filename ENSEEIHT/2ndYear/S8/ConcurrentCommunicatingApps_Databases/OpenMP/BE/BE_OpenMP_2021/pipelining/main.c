#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>
#include <string.h>
#include <math.h>
#include "omp.h"
#include "utils.h"

void pipeline(data *datas, resource *resources, int ndatas, int nsteps);

int main(int argc, char **argv)
{
  int n, i, s, d, ndatas, nsteps;
  long t_start, t_end;
  data *datas;
  resource *resources;

  // Command line arguments
  if (argc == 3)
  {
    ndatas = atoi(argv[1]); /* num of datas */
    nsteps = atoi(argv[2]); /* num of steps */
  }
  else
  {
    printf("Usage:\n\n ./main ndatas nsteps\n where ndatas is the number of data and nsteps the number of steps.\n");
    return 1;
  }

  init_data(&datas, &resources, ndatas, nsteps);

  /* Process all the data */
  t_start = usecs();
  pipeline(datas, resources, ndatas, nsteps);
  t_end = usecs();
  printf("Execution   time    : %8.2f msec.\n", ((double)t_end - t_start) / 1000.0);

  check_result(datas, ndatas, nsteps);

  return 0;
}


omp_lock_t* initiate_locks(int nsteps){
  omp_lock_t* locks = (omp_lock_t*) malloc(sizeof(omp_lock_t) * nsteps);
  
  for(int i = 0; i < nsteps; i++)
  {
    omp_init_lock(&locks[i]);
  }
  return locks;
}

void destroy_locks(omp_lock_t* locks, int nsteps){
  for(int i = 0; i < nsteps; i++)
  {
    omp_destroy_lock(&locks[i]);;
  }
}

void pipeline(data *datas, resource *resources, int ndatas, int nsteps)
{
  int d, s;
  omp_lock_t* locks = initiate_locks(nsteps);

#pragma omp parallel for private(s, d)
  /* Loop over all the data */
  for (d = 0; d < ndatas; d++)
  {
    /* Loop over all the steps */
    for (s = 0; s < nsteps; s++)
    {
      omp_set_lock(&locks[s]);
      process_data(datas, d, s, &(resources[s]));
      omp_unset_lock(&locks[s]);
    }
  }
  destroy_locks(locks, nsteps);
}


/** ANSWERS : Younes Saoudi 2SN-L3
 * Execution time for ndatas = 10 and nsteps = 10
 * 1 Thread : 2000.01 ms
 * 2 Threads : 1020.20 ms
 * 4 Threads : 628.25 ms
 **/