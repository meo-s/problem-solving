# !/bin/bash

CHECKOUT_PY=checkout.py

if ! [ -f $CHECKOUT_PY ]; then
    echo Missing python file \"$CHECKOUT_PY\"
    exit
fi

if [ $# -eq 0 ]; then
    echo A problem id must be spcified to checkout
    exit
fi

python $CHECKOUT_PY $@
