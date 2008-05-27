#include <winsock2.h>
#include <ws2tcpip.h>
#include <windows.h>
#include <wininet.h>
#include "exdll.h"
#include "dll.h"

#define Function(name) extern "C" void __declspec(dllexport) name(HWND hwnd, int string_size, char *variables, stack_t **stacktop)

Function(Ver)
{
	EXDLL_INIT();
	{
		char ret[MAX_PATH];
		popstring(ret);
		RetVar(mi_atoi(ret), "2.0");
	}
}

Function(GetLocalHostName)
{
	EXDLL_INIT();
	{
		char ret[MAX_PATH];
		popstring(ret);
		WSADATA wsaData;
		WORD wVersionRequested = MAKEWORD(1,1);
		WSAStartup(wVersionRequested, &wsaData);
		HOSTENT *HostInfo;
		char lpHostName[MAX_PATH]; // aguas!!!!
		gethostname(lpHostName, sizeof(lpHostName));
		HostInfo = gethostbyname(lpHostName);
		RetVar(mi_atoi(ret), lpHostName);
		WSACleanup();
	}
}

Function(GetLocalHostIP)
{
	EXDLL_INIT();
	{
		char ret[MAX_PATH];
		popstring(ret);
		WSADATA wsaData;
		WORD wVersionRequested = MAKEWORD(1,1);
		WSAStartup(wVersionRequested, &wsaData);
		HOSTENT *HostInfo;
		char buffer[MAX_PATH], lpHostName[MAX_PATH];
		gethostname(lpHostName, sizeof(lpHostName));
		HostInfo = gethostbyname(lpHostName);
		wsprintf(buffer, "%s", inet_ntoa(*(reinterpret_cast<in_addr*>(HostInfo->h_addr))) );
		RetVar(mi_atoi(ret), buffer);
		WSACleanup();
	}
}

Function(GetUrlCode)
{
	EXDLL_INIT();
	{
		char lpSite[MAX_PATH], lpFile[MAX_PATH], ret[MAX_PATH];
		popstring(lpSite);
		popstring(lpFile);
		popstring(ret);
		HINTERNET hISession = InternetOpen("Lobo Lunar", INTERNET_OPEN_TYPE_PRECONFIG, "", "", 0);
		if (hISession==NULL)
		{
			return;
		}
		HINTERNET hIConnect = InternetConnect(hISession,lpSite,INTERNET_DEFAULT_HTTP_PORT, NULL, NULL, INTERNET_SERVICE_HTTP, 0, NULL);
		if (hIConnect==NULL)
		{
			InternetCloseHandle(hISession);
			return;
		}
		HINTERNET hIRequest = HttpOpenRequest(hIConnect, "GET",lpFile, NULL, NULL, NULL, 0, NULL);
		if (hIRequest==NULL)
		{
			InternetCloseHandle(hISession);
			InternetCloseHandle(hIConnect);
			return;
		}
		if (HttpSendRequest(hIRequest, NULL, 0, NULL, 0) != TRUE)
		{
			InternetCloseHandle(hIRequest);
			InternetCloseHandle(hIConnect);
			InternetCloseHandle(hISession);
			return;
		}
		DWORD dwStatusCode;
		DWORD dwsize = sizeof(dwStatusCode);
		HttpQueryInfo (hIRequest,HTTP_QUERY_STATUS_CODE | HTTP_QUERY_FLAG_NUMBER, (LPVOID) &dwStatusCode, &dwsize, 0);
		char lpbuffer[MAX_PATH];
		wsprintf(lpbuffer, "%u", dwStatusCode);
		RetVar(mi_atoi(ret), lpbuffer);
		InternetCloseHandle(hISession);
		InternetCloseHandle(hIConnect);
		InternetCloseHandle(hIRequest);
	}
}

BOOL WINAPI DllMain(HINSTANCE hinstDLL,DWORD fdwReason,LPVOID lpReserved)
{
	return TRUE;
}