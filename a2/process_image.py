__author__ = "Dor Rondel"

import cv2
import numpy as np
from random import choice
from time import sleep

# step one - read image
filename = ""
while True:
    filename = str(input("""*************************************
Enter filename of image you'd like to
perform component labeling for in the 
active running directory:
**************************************
"""))

    if filename[filename.index(".") + 1:] in ["jpg", "png", "bmp", "jpeg", "tiff"]:
        # opencv doesnt support gif unfortunately
        break

# step two - convert to greyscale & binarize img
img = cv2.imread(filename, cv2.IMREAD_GRAYSCALE)
ret, thresh = cv2.threshold(img, 127, 255, cv2.THRESH_BINARY)
ret2, output_binary_img = cv2.threshold(img, 127, 255, cv2.THRESH_BINARY)


# step three - read and lable components
height, width = thresh.shape[:2]
label_counter = 1  # cant start at 0, as thats used in binary image
equiv = {}
bg = thresh[0, 0]
fg = 255 if bg == 0 else 0

print("part1\n")
for row in range(height):
    for col in range(width):

        # pixel is bg
        if thresh[row, col] == bg:
            output_binary_img[row, col] = bg
        else:
            # left and top are bg
            if thresh[row - 1, col] == bg and thresh[row, col - 1] == bg:
                output_binary_img[row, col] = label_counter
                label_counter += 1
                if label_counter == 255:
                    label_counter += 1
            # left is bg
            elif thresh[row - 1, col] != bg and thresh[row, col - 1] == bg:
                output_binary_img[row, col] = output_binary_img[row - 1, col]
            # top is bg
            elif thresh[row - 1, col] == bg and thresh[row, col - 1] != bg:
                output_binary_img[row, col] = output_binary_img[row, col - 1]
            else:  # both adjacent must be foreground
                output_binary_img[row, col] = output_binary_img[row - 1, col]
                if "l" + str(output_binary_img[row - 1, col]) in equiv:
                    equiv["l" + str(output_binary_img[row - 1, col])].add(output_binary_img[row, col - 1])
                else:
                    equiv["l" + str(output_binary_img[row - 1, col])] = set([output_binary_img[row, col - 1]])


print("part2\n")
v3 = {}
exists = set()
for key1 in equiv:
    for key2 in equiv:
        if key1 != key2:
            for elem in equiv[key1].copy():
                if elem in equiv[key2]:
                    if elem not in exists:
                        v3["l" + str(elem)] = set(list(equiv[key1]) + list(equiv[key2]))
                        exists.update(list(v3["l" + str(elem)]))
                    else:
                        for key3 in v3.copy():
                            if elem in v3[key3]:
                                
                                v3[key3].update(list(equiv[key1]))
                                exists.update(v3[key3])
                            
                else: 
                    if "l" + str(elem) in equiv:
                        final_set = equiv["l" + str(elem)]
                        while "l" + str(elem) in equiv and elem not in exists:
                            exists.add(elem)
                            final_set.update(list(equiv["l" + str(elem)]))
                            for elem2 in equiv["l" + str(elem)]:
                                if elem != elem2:
                                    elem = elem2
                                    break
                        
                        for elem in final_set.copy():
                            if elem in exists:
                                if "l" + str(elem) in equiv:
                                    for idx in equiv["l" +str(elem)].copy():
                                        if "l" + str(idx) in equiv:
                                            final_set.update(list(equiv["l" + str(idx)]))
                                    if "l" + str(elem) in v3:
                                        v3["l" + str(elem)].update(list(final_set))
                                        exists.update(v3["l" + str(elem)])
                            else:
                                v3["l" + str(elem)] = final_set
                                exists.update(list(v3))

print("part3\n")
print(v3)
x = True
while x:
    for node in exists:
        x = True
        print("1 " + str(node))
        for key in v3.keys():
            if node in v3[key]:
                print("2 " + str(node))
                x = False 
                break
        if x == True:
            if "l" not in str(node):
                print("adding" + str(node))
                print(equiv["l" + str(node)])
                v3["l" + str(min(equiv["l" + str(node)]))] = equiv["l" + str(node)]
            else:
                x = False
            
        


print("part4\n")
for key in v3.keys():
    for elem in v3[key]:
        if elem != min(v3[key]):
            output_binary_img = np.where(
                output_binary_img == elem,
                min(v3[key]),
                output_binary_img
            )

print("part5\n")
labels = {}
for row in range(height):
    for col in range(width):
        if "l" + str(output_binary_img[row, col]) not in labels:
            labels["l" + str(output_binary_img[row, col])] = [(row, col)]
        else:
            labels["l" + str(output_binary_img[row, col])].append((row, col))


if "l0" in labels:
    del labels["l0"]
if "l255" in labels:
    del labels["l255"]

colors_dict = {}
for label in labels.keys():
    colors_dict[label] = np.array([choice(range(256)) for _ in range(3)])

# step five - return greyscale to rgb
color_img = cv2.cvtColor(thresh, cv2.COLOR_GRAY2RGB)

print("part6\n")
# make final image out of colors
for key in labels.keys():
    for pxl_tpl in labels[key]:
        color_img[pxl_tpl[0]][pxl_tpl[1]] = colors_dict[key]

# step six - render new image with components
cv2.imshow('image', color_img)
cv2.waitKey(0)
cv2.destroyAllWindows()

