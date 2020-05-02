all: compile arch clean
	
	
compile:
	g++ -c ./source/*.cpp
    
arch:
	ar r libsshell.a *.o

clean:
	rm *.o

dinamic:
	g++ -c -fPIC ./source/*.cpp
	g++ -shared -o libdsshell.so *.o
	rm *.o
