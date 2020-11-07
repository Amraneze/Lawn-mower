FROM hseeberger/scala-sbt:graalvm-ce-20.0.0-java11_1.4.1_2.13.3
ARG SBT_FILE

WORKDIR /var/lawn_mower

COPY . .
COPY $SBT_FILE $SBT_FILE

ENTRYPOINT sbt "$SBT_COMMAND"