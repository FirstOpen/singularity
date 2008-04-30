void RetVar(int nVar, char szValor[MAX_PATH])
{
	int UserVar;
	switch(nVar)
	{
	case 0: 
		{
			UserVar = INST_0; break;
		}
	case 1:
		{
			UserVar = INST_1; break;
		}
	case 2:
		{
			UserVar = INST_2; break;
		}
	case 3:
		{
			UserVar = INST_3; break;
		}
	case 4:
		{
			UserVar = INST_4; break;
		}
	case 5:
		{
			UserVar = INST_5; break;
		}
	case 6:
		{
			UserVar = INST_6; break;
		}
	case 7:
		{
			UserVar = INST_7; break;
		}
	case 8:
		{
			UserVar = INST_8; break;
		}
	case 9:
		{
			UserVar = INST_9; break;
		}
	case 10:
		{
			UserVar = INST_R0; break;
		}
	case 11:
		{
			UserVar = INST_R1; break;
		}
	case 12:
		{
			UserVar = INST_R2; break;
		}
	case 13:
		{
			UserVar = INST_R3; break;
		}
	case 14:
		{
			UserVar = INST_R4; break;
		}
	case 15:
		{
			UserVar = INST_5; break;
		}
	case 16:
		{
			UserVar = INST_R6; break;
		}
	case 17:
		{
			UserVar = INST_R7; break;
		}
	case 18:
		{
			UserVar = INST_R8; break;
		}
	case 19:
		{
			UserVar = INST_R9; break;
		}
	case 20:
		{
			UserVar = INST_CMDLINE; break;
		}
	case 21:
		{
			UserVar = INST_INSTDIR; break;
		}
	case 22:
		{
			UserVar = INST_OUTDIR; break;
		}
	case 23:
		{
			UserVar = INST_EXEDIR; break;
		}
	case 24:
		{
			UserVar = INST_LANG; break;
		}
	}
	setuservariable(UserVar, szValor);
}


