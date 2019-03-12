__author__ = "Dor Rondel"

import cv2
import numpy as np
from random import choice

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
for i in range(100):
    thresh[i, i] = i
    print(thresh[i, i])


# step three - read and lable components
height, width = thresh.shape[:2]
for row in range(height):
    for column in range(width):
        print(f"h {row} \t w {column}")

# step five - return greyscale to rgb
color_img = cv2.cvtColor(thresh, cv2.COLOR_GRAY2RGB)
for row in range(height):
    for column in range(width):
        color_img[row][column] = np.array([choice(range(255)) for _ in range(3)])


# step six - render new image with components
cv2.imshow('image', color_img)
cv2.waitKey(0)
cv2.destroyAllWindows()