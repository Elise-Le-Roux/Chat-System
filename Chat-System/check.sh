#!/bin/bash

if test $# -ne 2
then
    echo "il me faut 2 paramètres"
    exit 1
fi

if test ! -f $1
then
    echo "il me faut un fichier"
    exit 2
fi

valide="pas ok"
for i in {1..9}
do
if test $2 = joueur$i
then
    valide="ok"
fi
done

j="ok"

if test $valide != $j
then
    echo "il me faut un joueur valide"
    exit 3
fi

moy1=`grep ^$2 $1 | tail -1 | awk '{print $2}'`


temps=`grep ^$2 $1 | head -n -1 | awk '{print $2}'`


tab=($temps)
addition=`expr ${tab[0]} + ${tab[1]} + ${tab[2]}`
let moy2=addition/3

if test $moy2 -eq $moy1
then
    echo "moy1 est correcte"
else
    echo "moy1 est incorrecte"
fi
echo "moy2=$moy2"
   

