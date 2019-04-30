from sys import exit

class Directed_Acyclic_Graph(object):
    ''' ADT for a directed acyclic graph '''

    def __init__(self, filepath = ""):
        """ Constructor for DAG ADT """
        if not filepath:  # empty string
            print("Proper filepath not provided")
            exit(0)
        
        try:
            self.graph_dict = {}
            self.size = len(self.graph_dict)
            self.profit_dict = {}
            with open(filepath) as job_file:
                for line in job_file:
                    trisect = line.strip("\n").split(" ")
                    if "," in trisect[2]:
                        dependencies = trisect[2].split(",")
                    elif "~" in trisect[2]:
                        dependencies = None
                    else:
                        dependencies = trisect[2]
                    
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