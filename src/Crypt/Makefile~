all:
	g++ -c *.cpp ./PrimalityTest/*.cpp ./GeneratePrime/*.cpp
	g++ -o project *.o -L./Sshell/ -L./md5/ -lsshell -lmd5
	rm *.o
	
dinamic:
	g++ -c *.cpp
	g++ -o dproject *.o -L./Sshell/ -ldsshell -Wl,-rpath,/home/sf/Workspace/c++/projects/Crypt/Sshell
	rm *.o

clean:
	rm -rf *~
