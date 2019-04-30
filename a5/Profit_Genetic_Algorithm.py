from random import choice

class Profit_Genetic_Algorithm(object):
    ''' class for DAG profit model estimation finder using genetic algorithms '''
    
    MUTATION_RATE = 0.1
    N_GENERATIONS = 1000
    CROSSOVER_RATE = 0.8
    POPULATION_SIZE = 20

    def __init__(self, dag):
        ''' constructor for genetic algorithm '''

        # jobs will implicitly be mapped to individual gene indices
        self.ordered_positive_jobs = sorted(dag.find_positive_jobs())
        self.gene_size = len(self.ordered_positive_jobs)
        self.population = self.generate_initial_population()
        self.dp = {}
        self.most_fit_indivual = None
        

    def generate_initial_population(self):
        ''' creates initial population list of jobs '''

        pop = []
        for it in range(self.POPULATION_SIZE):
            individual = ""
            while len(individual) < self.gene_size:
                individual += choice(["0", "1"])
            pop.append(individual)
        return pop


    def mutate(self):
        ''' applies mutation to genes of population when applicable '''
        for idx in range(len(self.population)):
            if (choice(range(100)) / 100.0) < self.MUTATION_RATE:
                locus = choice(range(self.gene_size))
                indiv_array = list(self.population[idx])
                # print("before:\t" + self.population[idx])
                if indiv_array[locus] == "1":
                    indiv_array[locus] = "0"
                else:
                    indiv_array[locus] = "1"
                self.population[idx] = "".join(indiv_array)
                # print("after:\t" + self.population[idx])

        
    def calculate_fitness(self, individual):
        ''' 
        returns overall fitness (profit) of an individual
        recursively finds profit of dependencies when needed
        optimized using dynamic programming for repeated calls
        '''
        pass

