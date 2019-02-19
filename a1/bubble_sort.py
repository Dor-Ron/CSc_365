def bubble_sort(arr):
    ''' bubble sort implementation'''

    # check if applicable
    if type(arr) == list and sorted(arr) != arr:

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