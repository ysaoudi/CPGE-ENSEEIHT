#include "utils.h"
#define VALUE_OF_VISITED_NODE (-18111999)

void bottom_up(int nleaves, struct node **leaves, int nnodes);

int main(int argc, char **argv)
{
  long t_start, t_end;
  int nnodes, nleaves;
  struct node **leaves;

  // Command line argument: number of nodes in the tree
  if (argc == 2)
  {
    nnodes = atoi(argv[1]);
  }
  else
  {
    printf("Usage:\n\n ./main n\n\nwhere n is the number of nodes in the tree.\n");
    return 1;
  }

  printf("\nGenerating a tree with %d nodes\n\n", nnodes);
  generate_tree(nnodes, &leaves, &nleaves);

  t_start = usecs();
  bottom_up(nleaves, leaves, nnodes);
  t_end = usecs();

  printf("Parallel time : %8.2f msec.\n\n", ((double)t_end - t_start) / 1000.0);

  check_result();
}

/* You can change the number and type of arguments if needed.     */
/* Just don't forget to update the interface declaration above.   */
void bottom_up(int nleaves, struct node **leaves, int nnodes)
{

  /* Implement this routine */
  int i;
  struct node *current_node;
#pragma omp parallel private(current_node)
  {
#pragma omp for
    for (i = 0; i < nleaves; i++)
    {
      current_node = leaves[i];
#pragma omp critical
      {
        while (current_node != NULL)
        {
          if (current_node->data == VALUE_OF_VISITED_NODE)
            break;

          process_node(current_node);
          current_node->data = VALUE_OF_VISITED_NODE;
          current_node = current_node->parent;
        }
      }
    }
  }
}

/** ANSWERS : Younes Saoudi 2SN-L3
 * Description : Threads will concurrently start from the leaves of the tree,
 * processing each node and changing its data attribute to VALUE_OF_VISITED_NODE
 * in order to mark it as a visited node and not process it multiple times.
 * This processing and data marking is done inside a critical section.
 * 
 * Execution Time for nnodes = 1000
 *  1 Thread : 100.14 ms
 *  2 Threads : 100.28 ms
 *  3 Threads : 116.62 ms
 **/