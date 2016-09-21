#-------------------FLAGS------------------------
JFLAGS = -g
JC = javac

#------------------RULES-----------------------
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES =	Server.java

default: classes

classes:	$(CLASSES:.java=.class)

#-----------------CLEAN UP------------------
clean:
	$(RM) *.class
	$(RM) *.txt
	$(RM) new_*
