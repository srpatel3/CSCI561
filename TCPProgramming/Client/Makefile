#-------------------FLAGS------------------------
JFLAGS = -g
JC = javac

#------------------RULES-----------------------
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES =	\Client.java

default: classes

classes:	$(CLASSES:.java=.class)

#-----------------CLEAN UP------------------
clean:
	$(RM) *.class

