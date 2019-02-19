def quick_sort(arr):
    ''' quick sort api call function'''

    # check if applicable
    if type(arr) == list and sorted(arr) != arr:
        quick_sort2(arr, 0, len(arr) - 1)
    return arr

    
def quick_sort2(arr, lower, upper):
    ''' quick sort algorithm abstract level functionality implementation '''

    # array length is greater than 1
    if lower < upper or not arr:
        
        # get pivot
        pivot = partition(arr, lower, upper)

        # recursively sort smaller and larger subarrays
        quick_sort2(arr, lower, pivot - 1)
        quick_sort2(arr, pivot + 1, upper)


def find_pivot(arr, lower, upper):
    ''' finds pivot value for an array using the median of 3 approach '''

    # find center index of array
    center = (lower + upper) // 2

    # sort the values at the 3 indices
    triplet = sorted([arr[lower], arr[center], arr[upper]])

    # return the median vaue of the three indices
    if triplet[1] == arr[lower]:
        return lower
    elif triplet[1] == arr[center]:
        return center
    return upper


def partition(arr, lower, upper):
    ''' splits array up into smaller and larger halves and returns the parting pivot '''

    # get pivot information
    pivot_idx = find_pivot(arr, lower, upper)
    pivot_val = arr[pivot_idx]

    # prepare array for sorting and set left pointer
    arr[pivot_idx] = arr[lower]
    arr[lower] = pivot_val
    border = lower

    # iterate through array
    for i in range(lower, upper + 1):

        # if element being assessed is smaller than the pivot
        if arr[i] < pivot_val:

            # move left border up and swap element with current border value
            border += 1
            tmp = arr[i]
            arr[i] = arr[border]
            arr[border] = tmp

    # swap border and lower indices to ensure pivot is in the right place
    # as it was moved to the beginning to begin the sorting
    tmp = arr[lower]
    arr[lower] = arr[border]
    arr[border] = tmp

    # return index of where the pivot value is located
    return border
