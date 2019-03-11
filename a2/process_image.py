__author__ = "Dor Rondel"

import cv2
import numpy as np

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

img = cv2.imread(filename, cv2.IMREAD_GRAYSCALE)
ret, thresh = cv2.threshold(img, 127, 255, cv2.THRESH_BINARY)
for i in range(100):
    print(thresh[i, i])

cv2.imshow('image',thresh1)
cv2.waitKey(0)
cv2.destroyAllWindows()

# step two - convert image to greyscale
# step three - read and lable components
# step four - render new image with components
