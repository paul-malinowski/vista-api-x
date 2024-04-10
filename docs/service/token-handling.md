# VistA-API-X Service Token Handling Documentation

[authentication-mechanisms]: ./authentication-mechanisms.md
[octo-sts-token-generator]: https://github.com/department-of-veterans-affairs/octo-sts-token-generator

### Table of Contents

- [Overview](#overview)
- [User Profile JWT Request Using STS Token Endpoint](#user-profile-jwt-request-using-sts-token-endpoint)
  - [User Profile JWT Request Using STS Token Endpoint](#user-profile-jwt-request-using-sts-token-endpoint)
- [Generating User Profile JWT Locally](#generating-user-profile-jwt-locally)
  - [STS-Token-Generator User Profile JWT Request](#sts-token-generator-user-profile-jwt-request)

### Links

- VistA-API-X Service Documentation
  - [Authentication Mechanisms][authentication-mechanisms]
- Department of Veterans Affairs Github
  - Repositories
    - [`octo-sts-token-generator`][octo-sts-token-generator]

## Overview

This section provides detailed information on how Identity and Access Management (IAM) Single
Sign-On internal (SSOi) Security Token Service (STS) Security Assertion Markup Language (SAML)
tokens can be submitted to a dedicated Office of Connected Care (OCC) HTTP endpoint to retrieve a
user profile JSON Web Token (JWT). VistA-API-X expects this user profile JWT along with the
appropriate project API key to further refine interactive user-level access through this service.
The process facilitates secure and streamlined access to VistA-API-X services, ensuring that users
have the appropriate level of access based on their roles and the scopes defined in their JWT.

An example of how to make a request using a supported user profile JWT is detailed in
[Authentication Mechanisms][authentication-mechanisms].

## User Profile JWT Request Using STS Token Endpoint

The HTTP endpoint `https://staff.apps-{{iam_environment}}.va.gov/sts/ssoi/v1/jwt` is used to
retrieve a user profile JWT using the appropriate IAM SSOi STS SAML token.

To access a user profile, clients must submit a SAML token to the designated endpoint. The process
involves using a `text/plain` HTTP POST request, which can be performed using the `curl`
command-line tool as shown in the example below. This request targets the OCC's endpoint for
converting IAM SSOi STS SAML tokens tokens into JWTs, which are required for user-level access
within the VistA-API-X service framework.

### User Profile JWT Request using STS Token Endpoint:

Below is an example of a valid user profile JWT request against using the environment name
`staging`:

```bash
curl --request POST \
  --url https://staff.apps-staging.va.gov/sts/ssoi/v1/jwt \
  --header 'Content-Type: text/plain' \
  --header 'Referer: https://vse.va.gov' \
  --data '{{saml_token}}'
```

Upon successful submission, the response will be a JSON payload containing a `token` key, which
holds the JWT. This token encapsulates the user's identity and scope information necessary for
accessing and interacting with the VistA-API-X services at the required level of authorization.

```json
{
  "token": "{{user_profile_jwt}}"
}
```

This JWT must then be used for functional API calls to the VistA-API-X service, included as a bearer
token in the authorization header, to authenticate the user and authorize their actions within the
system.

## Generating User Profile JWT Locally

The `sts-token-generator` tool is designed to request an IAM Security Token Service (STS) SAML token
using a locally available PIV card. Once the SAML token is generated, the tool then performs a user
profile JWT request that can be used as authentication for the VistA-API-X service.

The repository [octo-sts-token-generator][octo-sts-token-generator] contains
releases of this software for use on VA workstations connected to VA VPN services.

### STS-Token-Generator User Profile JWT Request

Running the executable `sts-token-generator.exe --env <<env>>` from the extracted latest release zip
(`sts-token-generator.zip`) will read `env-{{iam_environment}}.properties` in the current directory,
read the locally available PIV card for use as an HTTPS client certificate, and make a token request
to the IAM STS token request endpoint defined in the properties file.  The STS SAML token is then
offered to the OCC user profile JWT endpoint the returned JWT is printed to standard output.

```powershell
PS C:\Users\SomeUser\Downloads\sts-token-generator> .\sts-token-generator.exe --env sqa
{"token":"{{user_profile_jwt}}"}
```
