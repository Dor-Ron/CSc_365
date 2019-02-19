from random import randint

class File:
    ''' Class for  functions reading and writing numbers to text files '''

    def write_ten_random_arrays(self):
        ''' generates 10 separate lists of items and writes them to a file '''

        # generate 2d random int array
        numbers = []
        for i in range(10):
            numbers.append([randint(0, 1000) for _ in range(100)])
        numbers.append(range(100))

        # write rows of 2d array as lines to file
        with open("random_numbers.txt", "w") as file:
            for array in numbers:
                file.write(" ".join([str(num) for num in array]) + "\n")

    def read_ten_random_arrays(self):
        ''' returns 2d array of 10 randomly generated int arrays from file '''

        numbers = []
        
        with open("random_numbers.txt", "r") as file:
            for line in file:
                line = line[:-1]  # get rid of newline character
                numbers.append([int(num) for num in line.split(" ")])
        return numbers
