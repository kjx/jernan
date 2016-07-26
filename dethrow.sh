for i in *.java
do 
  sed 's/throws Exception//' $i > $i.SEDDED
  mv $i.SEDDED $i
done
