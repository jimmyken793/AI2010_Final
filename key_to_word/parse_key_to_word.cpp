/**
將發音相同的中文字放在同一個檔案中，檔名為「_發音按鍵_」。
由於斜線符號「/」不能做為檔名，因此，所有ㄥ發音的位置皆用「=」取代「/」。 
例如，「_c8_」這個檔存的是發音與「哈」相同的所有中文字；
「_2=_」則是與「等」發音相同者。 
**/

#include <stdio.h>
#include <wchar.h>
#include <wctype.h>
int main(){
	FILE *fin=fopen("phone.cin","r");
	FILE *fout;
	
	char file[100];
	wchar_t str[100];
	
	for(int i=0;i<26045;i++){
		fwscanf(fin,L"%S",str);
		sprintf(file,"_%s_",str);
		
		for(int i=0;file[i];i++)
			if(file[i]=='/')
				file[i]='=';

		fwscanf(fin,L"%s",str);
		
		fout=fopen(file,"a");
		fwprintf(fout,L"%s",str);
		fclose(fout);
	}
	
	fclose(fin);
	
	return 0;
}
