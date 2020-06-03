#!/bin/bash
PRG="$0"
while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`"/$link"
  fi
done
s=`dirname "$PRG"`/..
curdir="`pwd`"
cd "$s"
s="`pwd`"
cd "$curdir"

if [ "$GATE_HOME" != "" ] 
then
  g="$GATE_HOME"
fi
g2=`grep "^gate.home=" $s/build.properties | grep = | cut -f 2 -d=`
if [ "$g2" != "" ]
then 
  g="$g2"
fi
if [ "$g" == "" ]
then
  g="../.."
fi

echo GATE_HOME=$g
echo AppDoc=$s
java $JAVA_OPTS  -cp "$g/lib/*":"$s/lib/*":"$s/AppDoc.jar":"$g/bin/gate.jar" -Dgate.home="$g" at.ofai.gate.appdoc.DocGenerator "$@"

