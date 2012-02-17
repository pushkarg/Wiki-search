#include<iostream>
#include <sstream>
#include<string>
using namespace std;

int main(){
string	 queries[30]={ "DNA", "Apple", "Epigenetics", "Hollywood", "Maya", "Microsoft", "Precision", "Tuscany", "99 balloons", "Computer Programming", "Financial meltdown", "Justin Timberlake", "Least Squares", "Mars robots", "Page six", "Roman Empire", "Solar energy", "Statistical Significance", "Steve Jobs", "The Maya", "Triple Cross", "US Constitution", "Eye of Horus", "Madam I’m Adam", "Mean Average Precision", "Physics Nobel Prizes", "Read the manual", "Spanish Civil War", "Do geese see god", "Much ado about nothing" };

for(int i=0;i<30;i++){
	while(queries[i].find(" ")!=string::npos ){
		int pos = queries[i].find(" ");
		queries[i].replace( pos ,pos +1, "%20");
	}
}

string url0 = "https://www.googleapis.com/customsearch/v1?key=AIzaSyC3psXHEJpBHuNXdWUMBuU6QmTam0YXwRg&cx=003124365989545633216:m49jkqxkn0e&q=";

string file = "> google/out";
string final_cmd;
	char num[5] ;
	std::stringstream ss[30];
for(int i=0;i<30;i++){
	
	ss[i]<<i;
	file = string("> google/out") + ss[i].str();
	for(int j=0;j<3;j++){
		if(j==0)
			final_cmd = string("curl \"") + url0 + queries[i] +string( "&alt=atom&start=1\"") + string(" ")+ file;
		else if(j==1)
			final_cmd = string("curl \"") + url0 + queries[i] +string( "&alt=atom&start=11\"") + string(" >")+ file;
		else
			final_cmd = string("curl \"") + url0 + queries[i] +string( "&alt=atom&start=21\"") + string(" >") +file;
		cout<<"\n"<<final_cmd;
	}
	
}

//string final_url = url0 + queries[29] + "r&alt=atom&start=11";

//cout<<"Final url : "<< final_cmd<<endl;
return 0;
}
