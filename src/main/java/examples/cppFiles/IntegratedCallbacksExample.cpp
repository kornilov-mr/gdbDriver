#include <iostream>
using namespace std;
int main(){


    int arr[3];
    for(int i=0;i<3;i++){
        cin>>arr[i];
    }
    int g= -1;
    int h= arr[g];
    for(int i=0;i<2;i++){
        cout<<arr[i]<<endl;
    }
    cout<<h<<endl;
    cout<<"end";
}