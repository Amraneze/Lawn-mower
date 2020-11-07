FROM hseeberger/scala-sbt:graalvm-ce-20.0.0-java11_1.4.1_2.13.3

WORKDIR /var/lawn_mower

COPY . .

ENTRYPOINT sbt "$SBT_COMMAND"