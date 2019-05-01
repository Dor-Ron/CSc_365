from sys import exit

class Directed_Acyclic_Graph(object):
    ''' ADT for a directed acyclic graph '''

    def __init__(self, filepath = ""):
        """ Constructor for DAG ADT """
        if not filepath:  # empty string
            print("Proper filepath not provided")
            exit(0)
        
        try:
            self.graph_dict = {}  # id -> dep
            self.size = len(self.graph_dict) 
            self.profit_dict = {}  # id -> $
            with open(filepath) as job_file:
                for line in job_file:
                    trisect = line.strip("\n").split(" ")
                    
                    # get dependencies
                    if "," in trisect[2]:  #
                        dependencies = trisect[2].split(",")
                    elif "~" in trisect[2]:
                        dependencies = None
                    else:
                        dependencies = [trisect[2]]
                    
                    # enter them into data
                    self.graph_dict[trisect[0]] = dependencies
                    self.profit_dict[trisect[0]] = int(trisect[1])
        except:
            print("An exception has occured while trying to initiate a graph")
            exit(0)


    def stringify(self):
        ''' toString method for dag '''
        return """
        DAG: {}

        Implicitly mapped prices: {}
        """.format(
            self.graph_dict,
            self.profit_dict
        )


    def find_positive_jobs(self):
        ''' 
        returns a list of all jobs/keys with positive profits.
        utilized for optimization, as jobs that are inherently
        costly should not be processed
        '''
        return [key for key in filter(lambda x: self.profit_dict[x] > 0,
            self.profit_dict.keys()
        )]


    def net_profit(self, job_id, visited_set):
        ''' 
        calculates net profit for a task_id recursively
        returns (net-profit, job-combination-set) tuple
        '''
        
        visited = visited_set
        visited.add(job_id) # add task to set
        if self.graph_dict[job_id] == None: # no dependencies
            return (self.profit_dict[job_id], visited) 
        
        if not visited_set:
            visited = set()

        summ = self.profit_dict[job_id]

        # case of dependencies ---> recursion
        for depen in self.graph_dict[job_id]:  
            if depen not in visited:
                visited.add(depen)
                net = self.net_profit(depen, visited)
                if type(net) == int:
                    summ += net
                    continue
                summ += net[0]

        return (summ, visited)
