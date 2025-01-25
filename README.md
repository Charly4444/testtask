This is the solution for a test task i had to create input classification in java

To run the project as usual navigate to the directory of main package
"cd ./src/classifier"

Use the command to run -> 
"java MyClassifier.java -i <input_filepath> -o <output_dir>"

All files input and output are kept inside resources, hence a typical run case
java MyClassifier.java -i <../../resources/input.txt> -o <../../resources>
but any preferred directories may be used.

SWITCHES available
-s: provides short brief information
-f: provides more detailed information
-a: specifies to append new data, when this is not used output is overwritten. 