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
        return """
        DAG: {}

        Implicitly mapped prices: {}
        """.format(
            self.graph_dict,
            self.profit_dict
        )
                

dag = Directed_Acyclic_Graph("eg.txt")
print(dag.stringify())