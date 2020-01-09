#! /usr/bin/env sh

# My current fav parameters, these give a pleasant thunderous waterfall sound
RESPONSE=0.95
POLES=3

java -cp bin com.rowanskewes.CLI.BrownianNoise $RESPONSE $POLES < /dev/urandom | aplay -f cd

# Alternatively, print output to command line for visualization
#java -cp bin com.rowanskewes.CLI.BrownianNoise 0.7 3 print < /dev/urandom
