from sys import exit
from itertools import combinations

class Profit_Flow_Network(object):
    ''' class used to define the flow network used for '''

    def __init__(self, adj_list = None):
        ''' constuctor for network, optional adj_list param '''
        if type(adj_list) != dict or not adj_list:
            print("adj_list parameter must be of type <dict> and must not be empty")
            exit(0)
        
        # <id> -> [dep_0, dep_1, dep_2.... dep_n, $]
        # if adj_list[<id>][-1] == None, entry is source/sink node 
        self.adj_list = adj_list
        self.node_count = len(adj_list)
        self.positives = self.get_positive_profit_nodes()
        self.combos = self.make_combinations()
        self.solution = (0, 0)  # combo index for solution and the total profit propagated thru netowork
        self.dp = {}


    def get_positive_profit_nodes(self):
        ''' 
        Returns list of node_ids which have a positive profit.
        For optimization purposes, shouldn't waste time looking at
        jobs that knowingly bring in no income
        '''

        # Connect start to final list
        return []

    def make_combinations(self):
        ''' returns list of all possible combinations of jobs '''

        return []

    def find_solution(self):
        ''' 
        Recursively finds the ideal combination of jobs to take,
        Uses a brute-force approach optimized using dynamic programming techniques
        '''
        pass

    def print_solution(self):
        ''' prints solution for given adjacency list to STDOUT '''
        print()