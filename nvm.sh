NODE_VERSION=6.2.0

case "$(uname -s)" in

   Linux)
     echo 'Linux'
     source $NVM_DIR/nvm.sh
     nvm install $NODE_VERSION 
     nvm use $NODE_VERSION
     node_bin=`nvm which $NODE_VERSION`
     echo "PATH=$PATH:/$(dirname $node_bin)" >> env.properties
     ;;

   CYGWIN*|MINGW32*|MSYS*)
     echo 'MS Windows'
     nvm install $NODE_VERSION
     nvm use $NODE_VERSION
     echo "" >> env.properties 
     ;;

   Darwin)
     echo 'Mac OS X'
     ;;

   *)
     echo 'other OS' 
     ;;
esac

