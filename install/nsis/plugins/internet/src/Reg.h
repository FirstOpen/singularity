char* LeerREG(HKEY LlaveReg, char sRuta[MAX_PATH], char sCampo[MAX_PATH])
{
	HKEY phKey;
	DWORD dwType, dwLen;
	static char buffer[MAX_PATH];
	if (RegOpenKey(LlaveReg, sRuta, &phKey))
	{
		lstrcpy(buffer, "Error");
	}
	else
	{
		if (RegQueryValueEx(phKey, sCampo, NULL, &dwType, (LPBYTE)buffer, &dwLen))
		{
			lstrcpy(buffer, "Error");
		}
	}
	RegCloseKey(phKey);
	return buffer;
}
