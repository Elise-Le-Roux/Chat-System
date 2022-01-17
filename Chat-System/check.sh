#!/bin/bash 
if test $# -ne 2
then
    echo Il me faut 2 arguments
    exit 1
fi
if test ! -f $1
then
    echo Il me faut un ficher
    exit 2
fi
grep -w $2 ~temps.txt

pro=#?
if test pro -ne 0
then
    echo Joueur est pas un joueur validé
    exit 3
fi
somme() (
    tab[$1]
    
so=0
for i in {1..9}
do
    so=(expr $so + $tab[1])
done
)
moy1=$(expr $somme*1/9) 
       


tab=[*]
