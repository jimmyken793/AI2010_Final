/**
�N�o���ۦP������r��b�P�@���ɮפ��A�ɦW���u_�o������_�v�C
�ѩ�׽u�Ÿ��u/�v���వ���ɦW�A�]���A�Ҧ����o������m�ҥΡu=�v���N�u/�v�C 
�Ҧp�A�u_c8_�v�o���ɦs���O�o���P�u���v�ۦP���Ҧ�����r�F
�u_2=_�v�h�O�P�u���v�o���ۦP�̡C 
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
