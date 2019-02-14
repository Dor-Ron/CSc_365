class Sort(object):
    ''' 
    Class for first assignment, containing insertion, quick, and an 
    optimized bubble sort function. 
    '''

    def quick():
        ''' quicksort implementation'''

    def insertion(arr):
        ''' insertionsort implementation'''

        # iterate through all elements
        for i in range(1, len(arr)):

            # keeps track of range of organized sublist
            sublist_tail_pos = i
            current_val = arr[i]

            # while there are elements left in sorted sublist
            # and subsequent element is smaller than new sublist tail 
            while sublist_tail_pos > 0 and arr[sublist_tail_pos - 1] > current_val:
                # shift elements to make room and place subsequent element in ordered sublist
                arr[sublist_tail_pos] = arr[sublist_tail_pos - 1]
                sublist_tail_pos -= 1
            
            # set found whole to the subsequent element
            arr[sublist_tail_pos] = current_val
        return arr


    def bubble(arr):
        ''' bubblesort implementation'''

        # for optimization 
        is_sorted = False
        last_unsorted = len(arr) - 1

        # continue until list is sorted
        while not is_sorted:

            # assume it is initially
            is_sorted = True

            # iterate through potentially unordered part of array
            for i in range(last_unsorted):

                # if items out of order and swap needs to be made
                if arr[i] > arr[i + 1]:

                    # swap elements
                    tmp = arr[i]
                    arr[i] = arr[i + 1]
                    arr[i + 1] = tmp

                    # continue sorting as possibility exists that array isnt sorted
                    is_sorted = False

            # adjust length of potentially unsorted sublist of array
            last_unsorted -= 1
        return arr
