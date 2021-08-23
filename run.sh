FILE_NAME="LeftRecursion.java"
# FILE_NAME="LeftFactoring.java"
# FILE_NAME="TopDownParsing.java"

javac $FILE_NAME -d out
java -cp out $FILE_NAME
