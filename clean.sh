for  file in `find . -name .svn`
do
 rm -rf $file 
done
for  file in `find . -name *.doc`
do
 rm -rf $file 
done

