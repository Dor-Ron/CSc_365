class Sort(object):
    ''' 
    Class for first assignment, containing insertion, quick, and an 
    optimized bubble sort function. 
    '''

    def quick():
        ''' quicksort implementation'''

    def insertion(arr):
        ''' insertionsort implementation'''
        return arr


    def bubble(arr):
        ''' bubblesort implementation'''
        is_sorted = False
        last_unsorted = len(arr) - 1
        while not is_sorted:
            is_sorted = True
            for i in range(last_unsorted):
                if arr[i] > arr[i + 1]:
                    tmp = arr[i]
                    arr[i] = arr[i + 1]
                    arr[i + 1] = tmp
                    is_sorted = False
            last_unsorted -= 1
        return arr
