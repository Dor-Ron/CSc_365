def insertion_sort(arr):
    ''' insertion sort implementation'''
    
    # check if applicable
    if type(arr) == list and sorted(arr) != arr:
        
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