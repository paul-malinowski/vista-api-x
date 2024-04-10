# VistA-API-X System Authentication Mechanisms Documentation

[onboarding]: ./onboarding.md
[token-handling]: ./token-handling.md

[icr-process-and-standards]:
    https://dvagov.sharepoint.com/sites/OITEPMOVistAOffice/SitePages/VOICRManagement.aspx

### Table of Contents

- [Overview](#overview)
- [Project Setup](#project-setup)
- [System Level Authentication](#system-level-authentication)
    - [System Profile JWT Request](#system-profile-jwt-request)
    - [System RPC Invoke Request](#system-profile-rpc-invoke-request)
- [User Level Authentication](#user-level-authentication)
    - [User RPC Invoke Request](#user-profile-rpc-invoke-request)

### Links

- VistA-API-X Service Documentation
  - [Onboarding][onboarding]
  - [Token Handling][token-handling]
- Department of Veterans Affairs Sharepoint
  - VistA Office
    - [ICR Process and Standards][icr-process-and-standards]

## Overview

The VistA-API-X service employs two principal methods for authentication: [system level
authentication](#system-level-authentication) for backend processes, and [user level
authentication](#user-level-authentication) for interactive calls. Both methods necessitate an API
key that is linked to a project. This setup ensures secure access and data exchange with the
VistA-API-X services limited to a registered project API key.

## Project Setup

The initial onboarding for access to the VistA-API-X service is a manual process that requires
registration of project information and access requirements. This critical first step ensures that
each project is appropriately evaluated and granted the necessary permissions for integration. The
onboarding documentation, available at [Onboarding][onboarding], provides comprehensive details on
this process, guiding users through the necessary steps to get started.

For projects that may require Integration Control Registrations (ICRs), it's essential to review
your project with your Product Manager (PM) and Information Systems Security Officer (ISSO) to
determine the necessity of an ICR. The VA defines ICRs as agreements between VistA packages when
using the MUMPS (M) software development language, covering external references to another package's
files, routines, remote procedures, options, protocols, or other software components. The sharepoint
[ICR Process and Standards][icr-process-and-standards] provides VA staff and contractors with
information, guidance, and search access to ICR documents.

## System Level Authentication

For backend system level authentication, which is used for automated processes without user
interaction, the VistA-API-X service supports a secure method through which systems can
authenticate. This approach is suitable for scenarios where direct user authentication is not
feasible, such as automated data loading, reporting, and auditing tasks. It's important to note that
all operations, both read and write, must adhere to strict access controls as outlined in the ICR
process detailed above.

### System Profile JWT Request:

The endpoint `{{base_uri}}/auth/token` is used to obtain a system profile JWT, which acts as the
bearer authorization access token for system level API requests.  For more information about which
base URI to use see the specification [here](rest-endpoints.md#base-uri).

Below is an example of a valid request in the `dev` environment:

```bash
curl --request POST \
  --url https://dev.vista-api-x.va.gov/api/auth/token \
  --header 'Accept: application/json' \
  --header 'Content-Type: application/json' \
  --data '{
    "key": "{{api_key}}"
  }'
```

The response from this request will include the access token under `data.token`, as shown in the
example below:

```json
{
  "path": "/api/auth/token",
  "data": {
    "token": "{{system_profile_jwt}}"
  }
}
```

### System Profile RPC Invoke Request

Below is an example of a valid system request in the `dev` service environment, performing an RPC
invoke call to station `999` to retrieve user information for the `DUZ` making the call
(`DUZ:123456789`):

```bash
curl --request POST \
  --url https://dev.vista-api-x.va.gov/api/vista-sites/999/users/123456789/rpc/invoke \
  --header 'Authorization: Bearer {{system_profile_jwt}}' \
  --header 'Accept: application/json' \
  --header 'Content-Type: application/json' \
  --data '{
    "context": "SDECRPC",
    "rpc": "SDES GET USER PROFILE BY DUZ",
    "jsonResult": true,
    "parameters": [
      {
        "string": "123456789"
      }
    ]
  }'
```

The response to this request provides detailed user information, as illustrated in the example
below:

```json
{
  "path": "/vista-sites/999/users/123456789/rpc/invoke",
  "payload": {
    "User": {
      "Division": [
        {
          "Default": "",
          "Division": 999,
          "IEN": 999,
          "Name": "CAMP MASTER"
        }
      ],
      "IEN": 123456789,
      "Name": "Lamia,Brawne",
      "Primary Menu Option": "EVE",
      "Secondary Menu": [
        {
          "Option": "OR CPRS GUI CHART"
        }
      ]
    }
  }
}
```

## User Level Authentication

> [!TIP]
>
> Obtaining a token for use during development and testing is detailed in [Token
> Handling][token-handling].

For interactive applications and services, the VistA-API-X service facilitates user level
authentication through the collection of a valid SSOi user profile JWT. This token serves as the
bearer authorization access token for API calls made on behalf of a user. Additionally, a backend
process handling an API call must include both an authorization header and an `X-OCTO-VistA-API`
header for proper authentication.


### User Profile RPC Invoke Request

Below is an example of a valid user request in the `dev` service environment, performing an RPC
invoke call to station `999` to retrieve user information for the `DUZ` making the call
(`DUZ:123456789`):

```bash
curl --request POST \
  --url https://dev.vista-api-x.va.gov/api/vista-sites/999/users/123456789/rpc/invoke \
  --header 'Authorization: Bearer {{user_profile_jwt}}' \
  --header 'X-OCTO-VistA-API: {{api_key}}' \
  --header 'Accept: application/json' \
  --header 'Content-Type: application/json' \
  --data '{
    "context": "SDECRPC",
    "rpc": "SDES GET USER PROFILE BY DUZ",
    "jsonResult": true,
    "parameters": [
      {
        "string": "123456789"
      }
    ]
  }'
```

The result here is essentially the same example output defined in [System Profile RPC Invoke Request](#system-profile-rpc-invoke-request).
