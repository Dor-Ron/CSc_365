from bubble_sort import bubble_sort
from insertion_sort import insertion_sort
from quick_sort import *

class Sort(object):
    ''' 
    API class for first assignment, containing insertion, quick, and an 
    optimized bubble sort function
    '''

    def quick(self, arr):
        ''' quicksort implementation'''
        return quick_sort(arr)

    def insertion(self, arr):
        ''' insertionsort implementation'''
        return insertion_sort(arr)

    def bubble(self, arr):
        ''' bubblesort implementation'''
        return bubble_sort(arr)