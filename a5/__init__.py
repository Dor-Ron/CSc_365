__author__ = "Dor Rondel"
__project__ = "CSc 365 a3: Max Profit of a Directed Acyclic Graph"


from Directed_Acyclic_Graph import Directed_Acyclic_Graph as DAG
from Profit_Genetic_Algorithm import Profit_Genetic_Algorithm as GA


def main():
    # Step 1 - Create Graph based off of input file
    dag = DAG("eg.txt")
    #print(dag.net_profit("t5", set()))

    # Step 2 - Run GA on DAG
    ga = GA(dag)
    print(ga.run_genetic_algorithm())


    # Step 3 - Print Results


if __name__ == "__main__":
    main()