# VistA-API-X Service RPC Invocation Documentation

[onboarding]: ./onboarding.md
[rest-endpoints]: ./rest-endpoints.md
[rest-endpoints-rpc-invoke-endpoints]: ./rest-endpoints.md#rpc-invoke-endpoints

[cds-apps-developer-wiki]:
    https://github.com/department-of-veterans-affairs/cds-apps-developer-wiki
[vivian-all-rpc]:
  https://vivian.worldvista.org/vivian-data/8994/All-RPC.html

### Table of Contents

- [Overview](#overview)
- [Governance](#governance)
  - [Security Contexts](#security-contexts)
- [RPC Invocation](#rpc-invocation)
  - [Parameters](#parameters)
- [Reference](#reference)
- [Examples](#parameters)
  - [SDES GET USER PROFILE BY DUZ](#sdes-get-user-profile-by-duz)
  - [ORWU TOOLMENU](#orwu-toolmenu)
  - [VPR GET PATIENT DATA JSON](#vpr-get-patient-data-json)
  - [VPR GET PATIENT DATA JSON (Specific domain and id)](#vpr-get-patient-data-json-specific-domain-and-id)

### Links

- VistA-API-X Service Documentation
  - [Onboarding][onboarding]
  - [REST Endpoints][rest-endpoints]
- Department of Veterans Affairs Github
  - Repositories
    - [`cds-apps-developer-wiki`][cds-apps-developer-wiki]
- WorldVista
  - Vivian
    - [All RPC List][vivian-all-rpc]

## Overview

VistA-API-X facilitates Remote Procedure Call (RPC) interactions with VistA systems, acting as a
thin layer that initializes a new RPC session for every HTTP/RPC API call. This architecture
necessitates a cautious approach to the frequency and pattern of requests: rapid, consecutive
connections and disconnections can significantly affect performance, potentially impacting
production VistA environments. Developers are thus urged to craft software that establishes stable
connections to the VistA-API-X service, incorporating robust error handling and rate-limiting
mechanisms to mitigate these risks.

Given the sensitivity of the data handled by VistA systems, including Personally Identifiable
Information (PII) and Protected Health Information (PHI), security considerations are paramount.
Users of the VistA-API-X service must exercise utmost caution to restrict access to such sensitive
information. Operations such as iterating over patient records to locate specific individuals or
demographic groups are strongly discouraged, in favor of more secure, purpose-built tools for these
tasks. Furthermore, accurately representing caller information, including the Caller DUZ, is
critical for ensuring comprehensive audit trails and enforcing data access controls based on user
privileges.

## Governance

The Veterans Affairs (VA) system and, by extension, VistA, operate within a framework of stringent
regulatory requirements set forth by both the VA and the United States Federal Government. Access to
production VistA systems is granted strictly on a need-to-know basis, underscoring the
responsibility of each user to adhere to all applicable security protocols. This includes the
prohibition of code sharing, unauthorized account access, and any actions that compromise data
integrity, confidentiality, or system security.

Users are warned that inappropriate use of the system will lead to the termination of access
privileges and potential removal from the system. Acknowledgement of the terms of use, implied by
system access, underscores the user's agreement to these conditions and recognition of the legal
implications of misuse, which constitutes a Federal Crime.

This U.S. government-operated system is designed exclusively for authorized VA network users,
facilitating access and retrieval of information within the bounds of explicit authorization. The
system, supported by VA-funded computer networks, carries an expectation of privacy limited to
non-public governmental networks and systems. Activities on this system, including data
transmission, are subject to comprehensive oversight, which may encompass monitoring, recording,
retrieving, copying, auditing, inspecting, investigating, access restriction, blocking, tracking,
and disclosure to authorized VA and law enforcement personnel. Users consent to these terms by using
the system, acknowledging that unauthorized efforts to access, modify, disrupt, or exploit the
system are strictly forbidden and subject to legal and administrative penalties.

### Security Contexts

In alignment with VistA Office directives, applications interacting with VistA systems via RPCs are
required to employ a valid security context. This mandate ensures that operations performed on the
system are authorized and traceable to specific applications or administrative entities. The
definition and governance of these security contexts are facilitated through Integration Control
Registrations (ICRs), which delineate the permissions and restrictions for public and restricted
RPCs. ICRs serve as a regulatory framework, defining the scope of VistA interactions permissible for
each software package or administrative team. For detailed guidance on selecting the appropriate
security context for the application or script, please refer to the [Onboarding][onboarding]
documentation, which offers comprehensive insights into navigating the complexities of RPC calls
within the VistA ecosystem.

## RPC Invocation

> [!TIP]
>
> Details about the endpoint to use to perform an RPC invocation are detailed in [REST Endpoints /
> RPC Invoke Endpoints][rest-endpoints-rpc-invoke-endpoints].

> [!WARNING]
>
> Many of these example RPC calls are restricted.  The following examples will use the `CDS RPC
> CONTEXT` security context against station `500` and should not be performed against production
> VistA systems without a valid security context.

In the examples below we will be using the following RPC invoke URL template and should fill in the
following variables to match the appropriate user for the given station number:

- `{{station_number}}` should be `500` representing VistA station 500.
- `{{caller_duz}}` should be the DUZ of the person performing the RPC invocation and will have been
  provided during VistA station 500 user onboarding.

```
https://dev.vista-api-x.octo.va.gov/api/vista-sites/{{station_number}}/users/{{caller_duz}}/rpc/invoke
```

Authentication using a system profile JWT or user profile JWT needs to be provided for these calls.

> [!TIP]
>
> Obtaining a system profile JWT or user profile JWT is covered in [Authentication
> Mechanisms][authentication-mechanisms].

The following payload template demonstrates the required structure of an RPC invocation request:

```jsonc
{
  // The security context, specifying the execution environment for the RPC.
  "context": "...",
  // The name of the RPC function to invoke.
  "rpc": "...",
  // Boolean indicating whether the result should be in JSON format.
  "jsonResult": ...,
  // Required but optionally empty list of parameter objects.
  "parameters": [
    {
    // Parameter objects should define the type of parameter followed by the appropriate JSON
    // representation of the parameters value.
      "...": ...
    }
  ]
}
```

### Parameters

Parameter objects are single key mappings defining a type (the key) and a value (the value).  All
values are strings and type conversion is handled on the VistA side of the RPC function.

Parameter object types and values:
- `string`: A standard string value:
- `array`: An array of strings values.
- `namedArray`: A single dimension mapping of string keys and string values.

Here is a complex example demonstrating multiple parameter types and values:

```jsonc
{
    "context": "...",
    "rpc": "...",
    "jsonResult": ...,
    "parameters": [
        {
            // Perhaps a DFN?
            "string": "1234"
        },
        {
            // An ISO-8601 datetime including timezone
            "string": "2023-02-06T00:00:01-05:00",
        },
        {
            // An array of strings submitting multiple comments
            "array": [
                "Comment 1",
                "Comment 2",
            ]
        },
        {
            // A mapping containing domain and id keys
            "namedArray": {
                "domain": "xyz",
                "id": "12345",
            }
        }

    ]
}

```

> [!TIP]
>
> Always consult the RPC functions documentation to ensure the appropriate **datetime** format and
> **timezone** is being used.
>
> VistA supports several date and datetime standards depending on what function is being used and
> not every datetime will be ISO-8601.  Not every datetime with a timestamp can have a non-UTC
> timezone or be a naive datetime without a timezone defined.

>[!TIP]
>
> Many of the VistA RPCs have been written to support JSON and VistA-API-X requests can be flagged
> to return JSON payloads rather than raw string.  This is an optional feature provided by this
> service and the raw string can be interpreted by a client application as needed.
>
> See the following example RPC invocations that demonstrate both JSON and string return values:
>
> - **JSON**: [SDES GET USER PROFILE BY DUZ](#sdes-get-user-profile-by-duz)
> - **RAW**: [ORWU TOOLMENU](#orwu-toolmenu)
>

>[!TIP]
>
> Some, but not all, of the VistA RPCs can and should be called to return only the required data
> needed for processing.
>
> See the following example RPC invocations that demonstrate both full and selective results:
>
> - **Full**: [VPR GET PATIENT DATA JSON](#vpr-get-patient-data-json)
> - **Selective**: [VPR GET PATIENT DATA JSON (Specific `domain` and `id`)](#vpr-get-patient-data-json-specific-domain-and-id)

## Reference

There are several locations for VistA RPC specific information ranging from community contributed
documentation and catalogs, reading the source code, and local repositories with more contextual
information.

- Department of Veterans Affairs Github
  - Repositories
    - [`cds-apps-developer-wiki`][cds-apps-developer-wiki]: Detailed reference information for RPC, ICR, and test patient information.
- WorldVista
  - Vivian
    - [All RPC List][vivian-all-rpc]: Contains RPC details and links to source files for review.

## Examples

For the following examples only payloads are being presented rather than a tool based example.

Tools like `curl` can, however, be used with these payloads if you save the request JSON to a file named
`requests.json`.

```bash
curl --request POST \
  --url https://dev.vista-api-x.va.gov/api/vista-sites/{{station_number}}/users/{{caller_duz}}/rpc/invoke \
  --header 'Authorization: Bearer {{user_profile_jwt}}' \
  --header 'X-OCTO-VistA-API: {{api_key}}' \
  --header 'Accept: application/json' \
  --header 'Content-Type: application/json' \
  --data @request.json
```

### SDES GET USER PROFILE BY DUZ

In this example we will fetch a common accounts user profile at station `500` called `PROGRAMMER,ONE` identified by
DUZ `1`.

Request:

```jsonc
{
  "context": "SDECRPC",
  "rpc": "SDES GET USER PROFILE BY DUZ",
  "jsonResult": true,
  "parameters": [
    {
      "string": "1"
    }
  ]
}
```

Response:

```jsonc
{
  // ...
  "payload": {
    "User": {
      "Division": [
        {
          "Default": "",
          "Division": 500,
          "IEN": 500,
          "Name": "CAMP MASTER"
        }
      ],
      "IEN": 1,
      "Name": "PROGRAMMER,ONE",
      "Primary Menu Option": "EVE",
      "Secondary Menu": [
        {
          "Option": "OR CPRS GUI CHART"
        },
        // ...
      ],
      "Security Key": [
        {
          "Name": "XUPROG"
        },
        // ...
      ],
      "Station ID": 500
    }
  }
}
```

### ORWU TOOLMENU

In station `500` return the CPRS GUI configured tool menu for this station.

Request:

```
{
	"context": "OR CPRS GUI CHART",
	"rpc": "ORWU TOOLMENU",
	"jsonResult": false,
	"parameters": []
}
```

Response:

```
{
  // ...
  "payload": "Veteran Health Library=http://www.veteranshealthlibrary.org^Veteran Health Library=http://www.veteranshealthlibrary.org\nVistA Imaging Display=\"C:\\Program Files\\VistA\\Imaging\\MagImageDisplay.EXE\" %DFN %MREF %SRV %PORT^VistA Imaging Display=\"C:\\Program Files\\VistA\\Imaging\\MagImageDisplay.EXE\" %DFN %MREF %SRV %PORT\nVistA Imaging Capture=\"C:\\Program Files\\VistA\\Imaging\\MagImageCapture.exe\" %DFN %MREF %SRV %PORT^VistA Imaging Capture=\"C:\\Program Files\\VistA\\Imaging\\MagImageCapture.exe\" %DFN %MREF %SRV %PORT\nMental Health Assistant=\"C:\\Program files (x86)\\Vista\\YS\\MHA3\\YS_MHA.exe\" s=%SRV p=%PORT c=%DFN u=%DUZ m=%MREF^Mental Health Assistant=\"C:\\Program files (x86)\\Vista\\YS\\MHA3\\YS_MHA.exe\" s=%SRV p=%PORT c=%DFN u=%DUZ m=%MREF\nAfter Visit Summary=https://oitdvrappclin04.r01.med.va.gov/avs/avs^After Visit Summary=https://oitdvrappclin04.r01.med.va.gov/avs/avs\nAfter Visit Summary(exe)=\\\\r01dvrfpcavs.r01.med.va.gov\\AVS\\WebClientNew.exe stationNo=\"500\" userDuz=%DUZ url=\"https://oitdvrappclin04.r01.med.va.gov/avs/avs/avs.html\"^After Visit Summary(exe)=\\\\r01dvrfpcavs.r01.med.va.gov\\AVS\\WebClientNew.exe stationNo=\"500\" userDuz=%DUZ url=\"https://oitdvrappclin04.r01.med.va.gov/avs/avs/avs.html\"\nAfter Visit Summary (New Window)=chrome.exe https://oitdvrappclin04.r01.med.va.gov/avs/avs.html?stationNo=500&userDuz=%DUZ \"--new-window\"^After Visit Summary (New Window)=chrome.exe https://oitdvrappclin04.r01.med.va.gov/avs/avs.html?stationNo=500&userDuz=%DUZ \"--new-window\"\nCDS Console (DEV)=msedge.exe https://dev.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ^CDS Console (DEV)=msedge.exe https://dev.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ\nCDS Apps=SUBMENU 1^CDS Apps=SUBMENU 1\nCDS Console[1]=msedge.exe https://staging.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ^CDS Console[1]=msedge.exe https://staging.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ\nSTORM[1]=msedge.exe https://staging.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ&app=storm^STORM[1]=msedge.exe https://staging.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ&app=storm\nVirtual Care Manager[1]=msedge.exe https://staging.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ&app=vcm^Virtual Care Manager[1]=msedge.exe https://staging.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ&app=vcm\nCOVID-19 Patient Manager[1]=msedge.exe https://staging.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ&app=cpm^COVID-19 Patient Manager[1]=msedge.exe https://staging.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ&app=cpm\nVoogle[1]=msedge.exe https://staging.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ&app=voogle^Voogle[1]=msedge.exe https://staging.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ&app=voogle\nCRISTAL[1]=msedge.exe https://staging.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ&app=cristal^CRISTAL[1]=msedge.exe https://staging.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ&app=cristal\nMedPIC[1]=msedge.exe https://staging.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ&app=mpc^MedPIC[1]=msedge.exe https://staging.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ&app=mpc\n"
}
```

The response payload in this case is not JSON and is newline delimited information related to the
tool menu configuration at a specific station.

Text:

```
Veteran Health Library=http://www.veteranshealthlibrary.org^Veteran Health Library=http://www.veteranshealthlibrary.org
VistA Imaging Display="C:\Program Files\VistA\Imaging\MagImageDisplay.EXE" %DFN %MREF %SRV %PORT^VistA Imaging Display="C:\Program Files\VistA\Imaging\MagImageDisplay.EXE" %DFN %MREF %SRV %PORT
VistA Imaging Capture="C:\Program Files\VistA\Imaging\MagImageCapture.exe" %DFN %MREF %SRV %PORT^VistA Imaging Capture="C:\Program Files\VistA\Imaging\MagImageCapture.exe" %DFN %MREF %SRV %PORT
Mental Health Assistant="C:\Program files (x86)\Vista\YS\MHA3\YS_MHA.exe" s=%SRV p=%PORT c=%DFN u=%DUZ m=%MREF^Mental Health Assistant="C:\Program files (x86)\Vista\YS\MHA3\YS_MHA.exe" s=%SRV p=%PORT c=%DFN u=%DUZ m=%MREF
After Visit Summary=https://oitdvrappclin04.r01.med.va.gov/avs/avs^After Visit Summary=https://oitdvrappclin04.r01.med.va.gov/avs/avs
After Visit Summary(exe)=\\r01dvrfpcavs.r01.med.va.gov\AVS\WebClientNew.exe stationNo="500" userDuz=%DUZ url="https://oitdvrappclin04.r01.med.va.gov/avs/avs/avs.html"^After Visit Summary(exe)=\\r01dvrfpcavs.r01.med.va.gov\AVS\WebClientNew.exe stationNo="500" userDuz=%DUZ url="https://oitdvrappclin04.r01.med.va.gov/avs/avs/avs.html"
After Visit Summary (New Window)=chrome.exe https://oitdvrappclin04.r01.med.va.gov/avs/avs.html?stationNo=500&userDuz=%DUZ "--new-window"^After Visit Summary (New Window)=chrome.exe https://oitdvrappclin04.r01.med.va.gov/avs/avs.html?stationNo=500&userDuz=%DUZ "--new-window"
CDS Console (DEV)=msedge.exe https://dev.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ^CDS Console (DEV)=msedge.exe https://dev.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ
CDS Apps=SUBMENU 1^CDS Apps=SUBMENU 1
CDS Console[1]=msedge.exe https://staging.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ^CDS Console[1]=msedge.exe https://staging.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ
STORM[1]=msedge.exe https://staging.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ&app=storm^STORM[1]=msedge.exe https://staging.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ&app=storm
Virtual Care Manager[1]=msedge.exe https://staging.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ&app=vcm^Virtual Care Manager[1]=msedge.exe https://staging.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ&app=vcm
COVID-19 Patient Manager[1]=msedge.exe https://staging.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ&app=cpm^COVID-19 Patient Manager[1]=msedge.exe https://staging.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ&app=cpm
Voogle[1]=msedge.exe https://staging.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ&app=voogle^Voogle[1]=msedge.exe https://staging.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ&app=voogle
CRISTAL[1]=msedge.exe https://staging.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ&app=cristal^CRISTAL[1]=msedge.exe https://staging.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ&app=cristal
MedPIC[1]=msedge.exe https://staging.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ&app=mpc^MedPIC[1]=msedge.exe https://staging.cds.med.va.gov/smart-container/index.html?sta3n=500&duz=%DUZ&app=mpc
```

### VPR GET PATIENT DATA JSON

In station `500` return patient data information for patient ICN `38000527`.   This request uses
`namedArray` and this function ultimately supports selecting only specific data for return.

This request will return a full set of information for the specified patient.  In this case the
patient is being identified by the ICN (second part of the semicolon delimited field).  The format
of the parameters is further defined in [VPR GET PATIENT DATA JSON (Specific `domain` and
`id`)](#vpr-get-patient-data-json-specific-domain-and-id) below.

Request:

```jsonc
{
  "context": "LHS RPC CONTEXT",
  "rpc": "VPR GET PATIENT DATA JSON",
  "jsonResult": true,
  "parameters": [
    {
      "namedArray": {
        "patientId": ";38000527"
      }
    }
  ]
}
```

Response:

```jsonc
{
  // ...
  "payload": {
    "apiVersion": "1.01",
    "params": {
      "domain": "VEHU.FO-ALBANY.MED.VA.GOV",
      "systemId": "84F0"
    },
    "data": {
      "updated": "20240220133847",
      "totalItems": 35,
      "items": [
        {
          "addresses": [
            {
              "city": "BRIGADOON",
              "postalCode": "99999--020",
              "stateProvince": "NORTH DAKOTA",
              "streetLine1": "439 FIRST AVENUE"
            }
          ],
          "briefId": "S0704",
          "dateOfBirth": 19530125,
          "ethnicities": [
            {
              "ethnicity": ""
            }
          ],
          "exposures": [
            {
              "name": "No",
              "uid": "urn:va:agent-orange:N"
            },
            {
              "name": "No",
              "uid": "urn:va:ionizing-radiation:N"
            },
            {
              "name": "No",
              "uid": "urn:va:sw-asia:N"
            },
            {
              "name": "Unknown",
              "uid": "urn:va:head-neck-cancer:U"
            },
            {
              "name": "Unknown",
              "uid": "urn:va:mst:U"
            },
            {
              "name": "No",
              "uid": "urn:va:combat-vet:N"
            }
          ],
          "facilities": [
            {
              "code": 500,
              "latestDate": 20230712,
              "localPatientId": 100898,
              "name": "CAMP MASTER",
              "systemId": "84F0"
            }
          ],
          "familyName": "STAMM704",
          "fullName": "STAMM704,LANE844",
          "genderCode": "urn:va:pat-gender:M",
          "genderName": "Male",
          "givenNames": "LANE844",
          "icn": 38000527,
          "localId": 100898,
          "maritalStatuses": [
            {
              "code": "urn:va:pat-maritalStatus:L",
              "name": "Legally Separated"
            }
          ],
          "races": [
            {
              "race": ""
            }
          ],
          "ssn": 666000704,
          "telecoms": [
            {
              "telecom": "(999)774-3283",
              "usageCode": "H",
              "usageName": "home address"
            }
          ],
          "uid": "urn:va:patient:84F0:100898:100898",
          "veteran": {
            "isVet": 1,
            "serviceConnected": false
          }
        },
        {
          "entered": 20240119,
          "facilityCode": 500,
          "facilityName": "CAMP MASTER",
          "icdCode": "urn:10d:E78.00",
          "icdName": "Pure hypercholesterolemia, unspecified",
          "localId": 1371,
          "locationName": "CARDIOLOGY",
          "locationUid": "urn:va:location:84F0:195",
          "problemText": "High cholesterol (SCT 13644009)",
          "providerName": "RESNICK,MELISSA",
          "providerUid": "urn:va:user:84F0:520824749",
          "removed": false,
          "statusCode": "urn:sct:55561003",
          "statusName": "ACTIVE",
          "uid": "urn:va:problem:84F0:100898:1371",
          "unverified": false,
          "updated": 20240119
        },
        // ...
        {
          "clinicians": [
            {
              "name": "PROVIDER,ONE",
              "role": "S",
              "signedDateTime": 202310171857,
              "uid": "urn:va:user:84F0:983"
            }
          ],
          "content": "VERAPAMIL TAB  80MG\r\nTAKE ONE TABLET BY MOUTH EVERY DAY\r\nQuantity: 90 Refills: 3\r\n",
          "displayGroup": "O RX",
          "entered": 202310171856,
          "facilityCode": 998,
          "facilityName": "ABILENE (CAA)",
          "localId": 34891,
          "locationName": "PRIMARY CARE",
          "locationUid": "urn:va:location:84F0:32",
          "name": "VERAPAMIL TAB ",
          "oiCode": "urn:va:oi:2011",
          "oiName": "VERAPAMIL TAB ",
          "oiPackageRef": "668;99PSP",
          "providerName": "PROVIDER,ONE",
          "providerUid": "urn:va:user:84F0:983",
          "results": [
            {
              "uid": "urn:va:med:84F0:100898:34891"
            }
          ],
          "service": "PSO",
          "start": 20230105,
          "statusCode": "urn:va:order-status:actv",
          "statusName": "ACTIVE",
          "statusVuid": "urn:va:vuid:4500659",
          "stop": 20240106,
          "uid": "urn:va:order:84F0:100898:34891"
        }
      ]
    }
  }
}
```

### VPR GET PATIENT DATA JSON (Specific `domain` and `id`)

Further refining patient data selection when calling [VPR GET PATIENT DATA
JSON](#vpr-get-patient-data-json), down to the specific data type ()`domain`) and specific item
(`id`).

From the RPC source:

```
 ; RPC = VPR GET PATIENT DATA JSON
 ; where FILTER("patientId") = DFN or DFN;ICN
 ;       FILTER("domain")    = name of desired data type  (see VPRDJ0)
 ;       FILTER("text")      = boolean, to include document text [opt]
 ;       FILTER("start")     = start date.time of search         [opt]
 ;       FILTER("stop")      = stop date.time of search          [opt]
 ;       FILTER("max")       = maximum number of items to return [opt]
 ;       FILTER("id")        = single item id to return          [opt]
 ;       FILTER("uid")       = single record uid to return       [opt]
 ;       FILTER("nowrap")    = include line breaks in comments   [opt]
 ```

Request:

```jsonc
{
  "context": "LHS RPC CONTEXT",
  "rpc": "VPR GET PATIENT DATA JSON",
  "jsonResult": true,
  "parameters": [
    {
      "namedArray": {
        "patientId": ";38000527",
        "domain": "order",
        "id": "34891"
      }
    }
  ]
}
```

Response:

```jsonc
{
  // ...
  "payload": {
    "apiVersion": "1.01",
    "params": {
      "domain": "VEHU.FO-ALBANY.MED.VA.GOV",
      "systemId": "84F0"
    },
    "data": {
      "updated": "20240220134541",
      "totalItems": 1,
      "items": [
        {
          "clinicians": [
            {
              "name": "PROVIDER,ONE",
              "role": "S",
              "signedDateTime": 202310171857,
              "uid": "urn:va:user:84F0:983"
            }
          ],
          "content": "VERAPAMIL TAB  80MG\r\nTAKE ONE TABLET BY MOUTH EVERY DAY\r\nQuantity: 90 Refills: 3\r\n",
          "displayGroup": "O RX",
          "entered": 202310171856,
          "facilityCode": 998,
          "facilityName": "ABILENE (CAA)",
          "localId": 34891,
          "locationName": "PRIMARY CARE",
          "locationUid": "urn:va:location:84F0:32",
          "name": "VERAPAMIL TAB ",
          "oiCode": "urn:va:oi:2011",
          "oiName": "VERAPAMIL TAB ",
          "oiPackageRef": "668;99PSP",
          "providerName": "PROVIDER,ONE",
          "providerUid": "urn:va:user:84F0:983",
          "results": [
            {
              "uid": "urn:va:med:84F0:100898:34891"
            }
          ],
          "service": "PSO",
          "start": 20230105,
          "statusCode": "urn:va:order-status:actv",
          "statusName": "ACTIVE",
          "statusVuid": "urn:va:vuid:4500659",
          "stop": 20240106,
          "uid": "urn:va:order:84F0:100898:34891"
        }
      ]
    }
  }
}
```
