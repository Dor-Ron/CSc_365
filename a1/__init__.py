from File import File
from Sort import Sort

from timeit import timeit, Timer

if __name__ == "__main__":
    
    file = File()
    sort = Sort()

    # write random numbers to file
    file.write_ten_random_arrays()

    # read file and save locally
    numbers = file.read_ten_random_arrays()

    template = """Original array: {}

    optimized bubble sort: {} \t {} 
    insertion sort: {} \t {}
    quick sort: {} \t {} 
   
    """

    for arr in numbers:
        ori = " ".join([str(num) for num in arr])
        t1 = Timer(lambda: sort.bubble(arr))
        t2 = Timer(lambda: sort.insertion(arr))
        t3 = Timer(lambda: sort.quick(arr))
        print(template.format(
            ori.split(" "),
            sort.bubble(arr), t1.timeit(number = 10000),
            sort.insertion(arr), t2.timeit(number = 10000),
            sort.quick(arr), t3.timeit(number = 10000)
        ))
