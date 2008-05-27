void SetOutPut(char UserRet[MAX_PATH], char OutputVal[MAX_PATH])
{
	int UserVar;
	if(lstrcmp(UserRet,".r0")==0)
	{
		UserVar = INST_0;
	}
	else if(lstrcmp(UserRet,".r1")==0)
	{
		UserVar = INST_1;
	}
	else if(lstrcmp(UserRet,".r2")==0)
	{
		UserVar = INST_2;
	}
	else if(lstrcmp(UserRet,".r3")==0)
	{
		UserVar = INST_3;
	}
	else if(lstrcmp(UserRet,".r4")==0)
	{
		UserVar = INST_4;
	}
	else if(lstrcmp(UserRet,".r5")==0)
	{
		UserVar = INST_5;
	}
	else if(lstrcmp(UserRet,".r6")==0)
	{
		UserVar = INST_6;
	}
	else if(lstrcmp(UserRet,".r7")==0)
	{
		UserVar = INST_7;
	}
	else if(lstrcmp(UserRet,".r8")==0)
	{
		UserVar = INST_8;
	}
	else if(lstrcmp(UserRet,".r9")==0)
	{
		UserVar = INST_9;
	}
	else if(lstrcmp(UserRet,".r10")==0)
	{
		UserVar = INST_R0;
	}
	else if(lstrcmp(UserRet,".r11")==0)
	{
		UserVar = INST_R1;
	}
	else if(lstrcmp(UserRet,".r12")==0)
	{
		UserVar = INST_R2;
	}
	else if(lstrcmp(UserRet,".r13")==0)
	{
		UserVar = INST_R3;
	}
	else if(lstrcmp(UserRet,".r14")==0)
	{
		UserVar = INST_R4;
	}
	else if(lstrcmp(UserRet,".r15")==0)
	{
		UserVar = INST_R5;
	}
	else if(lstrcmp(UserRet,".r16")==0)
	{
		UserVar = INST_R6;
	}
	else if(lstrcmp(UserRet,".r17")==0)
	{
		UserVar = INST_R7;
	}
	else if(lstrcmp(UserRet,".r18")==0)
	{
		UserVar = INST_R8;
	}
	else if(lstrcmp(UserRet,".r19")==0)
	{
		UserVar = INST_R9;
	}
	setuservariable(UserVar, OutputVal);
}



