__author__ = "Dor Rondel"

# step one - read image
filename = ""
while True:
    filename = str(input("""*************************************
Enter filename of image you'd like to
perform component labeling for in the 
active running directory:
**************************************
"""))

    if filename[filename.index(".") + 1:] in ["jpg", "png", "bmp", "jpeg"]:
        break



# step two - convert image to greyscale
# step three - binarize image
# step four - read and lable components
# step five - render new image with components
