int mi_strlen(char* InputString)
{
	int i = 0;
	while(*InputString)
	{
		i++;
		InputString++;
	}
	return i;
}

void mi_strncpy(char* Buffer, char* StringToCopy, int NumberOfCharsToCopy)
{
	int index = 0;
	for(int i = 0; i < NumberOfCharsToCopy; i++)
	{
		Buffer[index] = StringToCopy[i];
		index++;
	}
	Buffer[index] = 0;
}

void mi_strcpy(char* Buffer, char* StringToCopy)
{
	int index = 0;
	for(int i = 0; i < mi_strlen(StringToCopy); i++)
	{
		Buffer[index] = StringToCopy[i];
		index++;
	}
	Buffer[index] = 0;
}

int mi_atoi(char *StringToInteger)
{
	unsigned int v=0;
	for (;;)
	{
		int c=*StringToInteger++ - '0';
		if (c < 0 || c > 9) break;
		v*=10;
		v+=c;
	}
  return (int)v;
}

int mi_strcmp(char* String1, char* String2)
{
	while(*String1 == * String2 && *String1)
	{
		String1++;
		String2++;
	}
	return *String1 - *String2;
}

