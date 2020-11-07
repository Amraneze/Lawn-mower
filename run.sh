#!/bin/sh

usage() {
	echo "usage: run.sh -commands commands -file file"
  echo "command                  command that should be given to sbt"
  echo "file                     file path of the commands that should be given to sbt"
  echo "example:"
  echo "- with file path : ./run.sh -file ./tmp/mower-commands.txt"
  echo "- with commands as string : ./run.sh -commands \"5 5\\\n1 2 N\\\nLFLFLFLFF\\\n3 3 E\\\nFFRFFRFRRF\""
}

case "$1" in
    -commands) COMMANDS=$2 ;;
    -file)    FILE=$2 ;;
    *)
esac

PARSED_COMMAND="$(echo "$COMMANDS" | sed 's/\\/\\\\/g')"

echo "$PARSED_COMMAND"

if [ "-$COMMANDS" = "-" ] ; then
  export SBT_FILE="$FILE"
  export SBT_COMMAND="run --file $FILE";
else
  export SBT_COMMAND="run --commands \"$PARSED_COMMAND\"";
fi

docker-compose up --build