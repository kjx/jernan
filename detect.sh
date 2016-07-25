for i in *.java
do 
  N=`basename $i .java`
  if [ -f $N.class ]
    then echo .
    else echo "$N has no class"
  fi
done
