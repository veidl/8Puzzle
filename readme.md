# 8 Puzzle

8-Puzzle is a sliding problem in a 3x3 grid, with the tiles numbered from zero to eight, zero being the empty space. It
is a classic problem for using heuristics. Most commonly used heuristic are: number of misplaced tiles and manhattan
distance. How they work and are applied in our solution will be covered later. Both heuristics are admissible, meaning
if it never overestimates the cost of reaching the goal.

The search algorithm used in this solution is A* with a priority queue. We also used both heuristics mentioned above
with an additional third heuristic which is weighted 97% manhattan and 3% misplaced tiles.  
Additionally we have an input.txt file which contains input states and goal states of different level of difficulties.

## A* search algorithm

The a-star algorithm is widely used in graph-traversal and path-finding due to its completeness and optimal efficiency.
It is an informed search algorithm and is formulated with weighted graphs. Meaning it aims to find the optimal solution
from a given graph with minimal cost (cost can vary depending on the problem: In our case least distance travelled).

In each iteration it aims to find the minimal cost solution for a given problem. This minimal solution can be found
using a priority queue. The algorithm will keep removing the top of the queue, meaning the highest priority solution,
until one of the top nodes is the goal solution.

## Heuristic

An approach of problem solving which is not optimal but sufficient enough to reach a short-term solution to a given
problem. They are used where reaching an optimal solution is either impossible or impractical. In psychology heuristics
are simple, efficient rules learned by evolutionary process that explain how people make their decisions and come to
conclusions.

### Admissible heuristic

An admissible heuristic is used to estimate the cost of reaching the goal state in an informed search algorithm, like A*
. In order for it to be admissible, the estimated cost must always be lower then or equal to the actual cost of reaching
the goal state.    
The evaluation function in A* (with n being the current node) is:

f(n) = g(n) + h(n)

f(n) … evaluation function    
g(n) … cost from start node to current node    
h(n)  … estimated cost from current node to goal

### Misplaced tiles

This heuristic is rather simple. With a given state of the grid, calculate the number of tiles which are misplaced with
a given goal state.

Example:  
Current State:     1,2,3,**5**,**4**,6,**0**,**7**,**8** <br/>  
Goal State:       **1,2,3,4,5,6,7,8,0** <br/>  
The number of misplaced tiles is 5, which are marked bold.

### Manhattan distance

The manhattan distance between two points can be calculated using this formula

Point 1: **(x1,y1)**  
Point 2: **(x2,y2)**

Calculated distance = ***|x1-x2| + |y1-y2|***
