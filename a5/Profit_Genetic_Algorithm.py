from random import choice
from math import floor, factorial

class Profit_Genetic_Algorithm(object):
    ''' class for DAG profit model estimation finder using genetic algorithms '''
    
    MUTATION_RATE = 0.01
    N_GENERATIONS = 100
    CROSSOVER_RATE = 0.5  # low crossover cause great fitness function
    POPULATION_SIZE = 10

    def __init__(self, dag):
        ''' constructor for genetic algorithm '''

        # jobs will implicitly be mapped to individual gene indices
        self.dag = dag
        self.ordered_positive_jobs = sorted(dag.find_positive_jobs())
        self.gene_size = len(self.ordered_positive_jobs)
        self.population = self.generate_initial_population()
        self.highest_fitness = 0
        self.most_fit_indivual = None

        # fitnesses cache for optimization
        self.cache = {} 


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
        ''' returns overall fitness (profit) of an individual '''

        total = 0
        visited = set()

        for idx in range(len(individual)):
            if individual[idx] == "1":
                job_id = self.ordered_positive_jobs[idx]          
                if job_id in visited:
                    continue
                tup = self.dag.net_profit(job_id, visited)
                if type(tup) != int:
                    visited = set(list(visited) + list(tup[1]))
                    total += tup[0]

                else:
                    visited = set(list(visited) + [tup])
                    total += tup        
        return total


    def get_fitnesses(self):
        ''' returns list of implicitly mapped fitness scores for individuals in population '''
        
        ret = []
        for idx in range(len(self.population)):
            if self.population[idx] in self.cache:
                ret.append(self.cache[self.population[idx]])
                continue
            self.cache[self.population[idx]] = self.calculate_fitness(self.population[idx])
            ret.append(self.cache[self.population[idx]])
        return ret


    def natural_selection(self):
        ''' returns two parents from population with fitness influencing chance of getting selected '''

        hashmap = {indiv: fit for (indiv, fit) in zip(self.population, self.get_fitnesses())}
        sorted_keys_by_val = sorted(hashmap.items(), key = lambda pair: pair[1])[::-1]

        big_arr = []
        for idx in range(len(sorted_keys_by_val), 0, -1):
            big_arr += [sorted_keys_by_val[::-1][idx - 1][0]] * idx

        return [choice(big_arr) for _ in range(2)]



    def crossover(self, parents):
        ''' returns child gene based off of list of len 2 parents '''
        if (choice(range(100)) / 100.0) < self.CROSSOVER_RATE:
            pivot = choice(range(len(parents[0])))  # get pivot from len of indevs
            mom = parents[0][:pivot]
            dad = parents[1][pivot:]
            return mom + dad
        return parents[0]


    def generate_next_gen(self):
        ''' generates next generation of population '''

        hashmap = {indiv: fit for (indiv, fit) in zip(self.population, self.get_fitnesses())}
        sorted_keys_by_val = sorted(hashmap.items(), key = lambda pair: pair[1])[::-1]
        next_gen = []

        # fill first half of next gen based off of most fit parents
        idx = 0
        while len(next_gen) < self.POPULATION_SIZE // 2:
            for _ in range(self.population.count(sorted_keys_by_val[idx][0])):
                next_gen.append(sorted_keys_by_val[idx][0])
                if len(next_gen) == self.POPULATION_SIZE // 2: break
            idx += 1
        
        # Generate offspring for other half
        for idx in range(self.POPULATION_SIZE // 2):
            child = self.crossover(self.natural_selection())
            next_gen.append(child)

        self.population = next_gen
        self.mutate()
        print(self.population)


    def run_genetic_algorithm(self):
        ''' executes genetic algorithm '''
        print(self.population)
        for _ in range(self.N_GENERATIONS):
            self.generate_next_gen()

        hashmap = {indiv: fit for (indiv, fit) in zip(self.population, self.get_fitnesses())}
        sorted_keys_by_val = sorted(hashmap.items(), key = lambda pair: pair[1])[::-1]
        print(sorted_keys_by_val[0])