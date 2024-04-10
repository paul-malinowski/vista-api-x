# VistA-API-X Service REST Endpoints Documentation

[authentication-mechanisms]: ./authentication-mechanisms.md
[rpc-invocation]: ./rpc-invocation.md

### Table of Contents

- [Base URI](#base-uri)
- [Authentication Endpoints](#authentication-endpoints)
- [Functional Endpoints](#functional-endpoints)
  - [Authentication Types](#authentication-types)
  - [RPC Invoke Endpoints](#rpc-invoke-endpoints)

### Links

  - VistA-API-X Service Documentation:
    - [Authentication Mechanisms][authentication-mechanisms]
    - [RPC Invocation][rpc-invocation]

## Base URI

The base URI for the VistA-API-X Service varies depending on the environment you are working with.
For development and testing purposes, two primary endpoints are provided: one for development
(`dev`) and another for staging (`staging`). Each of these environments has its own base URI
structured as follows:

- **Development Environment:** `https://dev.vista-api-x.va.gov/api/`
- **Staging Environment:** `https://staging.vista-api-x.va.gov/api/`

For applications and services moving towards production, the base URI changes to:

- **Production Environment:** `https://vista-api-x.va.gov/api/`

It's important to use the correct base URI corresponding to the environment you are working within
to ensure that API calls are properly directed and executed within the expected context.

## Authentication Endpoints

To interact with the VistA-API-X service through system level authentication, backend services must
first authenticate to obtain a token. This is achieved by utilizing the authentication endpoint
appended to the base URI. The endpoint for obtaining an authentication token is structured as
follows:

```
{{base_uri}}/auth/token
```

For example, in the development environment, the full URL for authentication would be:

```
https://dev.vista-api-x.va.gov/api/auth/token
```

This endpoint is used to secure an authentication token which is then utilized for subsequent API
requests to ensure they are authorized. The authentication process requires submitting the necessary
credentials, which will vary based on the specific requirements of the environment and the
application's configuration.

> [!TIP]
>
> Usage information and authentication details are covered in [Authentication
> Mechanisms][authentication-mechanisms].

## Functional Endpoints

### Authentication Types

When making requests to any functional endpoint the appropriate authentication headers must be used.

Authentication types, mainly system level and user level, are detailed in [Authentication
Mechanisms][authentication-mechanisms] along with all header information required for
functional authentication.

### RPC Invoke Endpoints

The VistA-API-X service facilitates remote procedure call (RPC) invocations through a dedicated
endpoint pattern, allowing for direct interaction with VistA sites. The RPC invoke endpoint is
designed to accommodate requests by utilizing the station number and the caller's DUZ (Digital User
Identifier). The structure for the RPC Invoke endpoint is as follows:

```
{{base_uri}}/vista-sites/{{station_number}}/users/{{caller_duz}}/rpc/invoke
```

In most testing scenarios, the `station_number` is set to `999`, representing the station number for
the test environment. The `caller_duz` parameter is crucial for identifying the user making the
request. For system level authentication, this DUZ is associated with a dedicated system user.
Conversely, when using user level authentication, the `caller_duz` should correspond to the DUZ of
the individual user making the requests.

For instance, in a development environment, a sample RPC invocation request using the following parameters:

- `station_number`: `999`
- `caller_duz`: `123456789`

Would produce to the following URL:

```
https://dev.vista-api-x.va.gov/api/vista-sites/999/users/123456789/rpc/invoke
```

Here, `123456789` would be the DUZ of the user or system account initiating the RPC call.

> [!TIP]
>
> Usage information, use cases, and examples are covered in [RPC Invocation][rpc-invocation].


> [!WARNING]
>
> It's vital to ensure that the correct caller DUZ is used in accordance with the authentication
> method employed to prevent unauthorized access or execution of system functions accessing
> sensitive data.

