#!/bin/sh

# Utility script for running ModelClassConverter

# Set default arguments
ACTION="help"
BRANCH="initial"
CONFIGURATION="configuration.xml"
DIRECTORY="converter"
EXECUTABLE="ModelClassConverter"

# Determine which action to perform
if [ -n "$1" ]
then
ACTION=$1
fi

# Determine which branch to use
if [ -n "$2" ]
then
BRANCH=$2
fi

# Determine which configuration to use
if [ -n "$3" ]
then
CONFIGURATION=$3
fi

echo "converter.sh"
echo "\tAction: $ACTION"
echo "\tBranch: $BRANCH"


# Run different actions

if [ "$ACTION" == "help" ]
then

echo "Usage:"
echo "./converter.sh [help|run|clean] [branch] [configuration]"
echo ""

elif [ "$ACTION" == "clean" ]
then

rm -Rf "$DIRECTORY"
echo "Cleaned"

elif [ "$ACTION" == "run" ]
then

if [ ! -f "$CONFIGURATION" ]
then

echo "Could not find configuration at location: $CONFIGURATION"
exit

fi

if [ ! -d "$DIRECTORY" ]
then
git clone git@github.com:topicusonderwijs/ModelClassConverter.git "$DIRECTORY"
else
echo "Already cloned repository"
fi

cd "$DIRECTORY"
git checkout "$BRANCH"

EXECUTABLE="$EXECUTABLE-$BRANCH"

if [ ! -f "target/$EXECUTABLE.jar" ]
then

mvn clean compile assembly:single -U
cd target
pwd
mv ModelClassConverter*jar "$EXECUTABLE.jar"
cd ..

else
echo "Already compiled executable"
fi

cd ..


java -cp "$DIRECTORY/target/$EXECUTABLE.jar" mcconverter.main.Main --formatted --configuration "$CONFIGURATION"


fi

