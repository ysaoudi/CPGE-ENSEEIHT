#include "utils.h"
#include <omp.h>

void stacks_seq(stack_t *stacks, int n);
void stacks_par_critical(stack_t *stacks, int n);
void stacks_par_atomic(stack_t *stacks, int n);
void stacks_par_locks(stack_t *stacks, int n);


int main(int argc, char **argv){

  stack_t *stacks;
  int      i, j, n;
  long     t_start, t_end, save;

  
  if ( argc == 2 ) {
    n = atoi(argv[1]);    /* the number of stacks */
  } else {
    printf("Usage:\n\n ./main n\n\nwhere n is the number of stacks.\n");
    return 1;
  }

  printf("\n");
  
  init_stacks(&stacks, n);
  t_start = usecs();
  stacks_seq(stacks, n);
  t_end = usecs();
  printf("Sequential  version.       --------  time : %8.2f msec.     ",((double)t_end-t_start)/1000.0);
  check_result(stacks, n);
  free_stacks(&stacks, n);

  init_stacks(&stacks, n);
  t_start = usecs();
  stacks_par_critical(stacks, n);
  t_end = usecs();
  printf("Critical    version.       --------  time : %8.2f msec.     ",((double)t_end-t_start)/1000.0);
  check_result(stacks, n);
  free_stacks(&stacks, n);

  init_stacks(&stacks, n);
  t_start = usecs();
  stacks_par_atomic(stacks, n);
  t_end = usecs();
  printf("Atomic      version.       --------  time : %8.2f msec.     ",((double)t_end-t_start)/1000.0);
  check_result(stacks, n);
  free_stacks(&stacks, n);

  init_stacks(&stacks, n);
  t_start = usecs();
  stacks_par_locks(stacks, n);
  t_end = usecs();
  printf("Locks       version.       --------  time : %8.2f msec.     ",((double)t_end-t_start)/1000.0);
  check_result(stacks, n);
  free_stacks(&stacks, n);

  return 0;
  
}


void stacks_seq(stack_t *stacks, int n){

  int s;
  
  
  for(;;){

    /* Get the stack number s */
    s = get_random_stack();

    if(s==-1) break;
    
    /* Push some value on stack s */
    stacks[s].elems[stacks[s].cnt++] = process();

  }
  
}



void stacks_par_critical(stack_t *stacks, int n){

  int s, t;
  
  #pragma omp parallel private(s, t)
  {
    for(;;){

      /* Get the stack number s */
      s = get_random_stack();

      if(s==-1) break;
      t = process();
      #pragma omp critical
      {
        /* Push some value on stack s */
        stacks[s].elems[stacks[s].cnt++] = t;
      }
    }
  }
}




void stacks_par_atomic(stack_t *stacks, int n){

  int s, t;
  
  #pragma omp parallel private(s, t)
  {
    for(;;){

      /* Get the stack number s */
      s = get_random_stack();

      if(s==-1) break;
      #pragma omp atomic capture 
      { 
        t = stacks[s].cnt++;  
      }
      /* Push some value on stack s */
      stacks[s].elems[t] = process();
    }
  }
}



void stacks_par_locks(stack_t *stacks, int n){

  int s,i,t;
  
  omp_lock_t *myLocks;
  myLocks = (omp_lock_t*)malloc(n*sizeof(omp_lock_t));
  for(i=; i<n; i++) {
    omp_init_lock(myLock+i);
  }
  #pragma omp parallel private(s, t) 
  {
    for(;;){

      /* Get the stack number s */
      s = get_random_stack();

      if(s==-1) break;
      t = process();
      omp_set_lock(myLock+s);
      /* Push some value on stack s */
      stacks[s].elems[stacks[s].cnt++] = t;
      omp_unset_lock(myLocks+s);
    }
  }
  for(i=; i<n; i++) {
    omp_destroy_lock(myLock+i);
  }

}
