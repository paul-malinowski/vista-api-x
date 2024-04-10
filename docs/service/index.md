# VistA-API-X Service Documentation

[onboarding]: ./onboarding.md
[authentication-mechanisms]: ./authentication-mechanisms.md
[token-handling]: ./token-handling.md
[rest-endpoints]: ./rest-endpoints.md
[rpc-invocation]: ./rpc-invocation.md

[cds-apps-developer-wiki]:
    https://github.com/department-of-veterans-affairs/cds-apps-developer-wiki
[vista-office]:
    https://dvagov.sharepoint.com/sites/OITEPMOVistAOffice
[icr-process-and-standards]:
    https://dvagov.sharepoint.com/sites/OITEPMOVistAOffice/SitePages/VOICRManagement.aspx
[octo-sts-token-generator]:
    https://github.com/department-of-veterans-affairs/octo-sts-token-generator
[octo-vista-api-x]:
    https://github.com/department-of-veterans-affairs/octo-vista-api-x
[vivian-all-rpc]:
  https://vivian.worldvista.org/vivian-data/8994/All-RPC.html

### Table of Contents

- [Overview](#overview)
- [Intended Audience](#intended-audience)
- [Background](#background)
- [Development](#development)

### Links (All)

- VistA-API-X Service Documentation
  - [Onboarding][onboarding]
  - [Authentication Mechanisms][authentication-mechanisms]
  - [Token Handling][token-handling]
  - [REST Endpoints][rest-endpoints]
  - [RPC Invocation][rpc-invocation]
- Department of Veterans Affairs Sharepoint
  - VistA Office
    - [Home][vista-office]
    - [ICR Process and Standards][icr-process-and-standards]
- Department of Veterans Affairs Github
  - Repositories
    - [`cds-apps-developer-wiki`][cds-apps-developer-wiki]
    - [`octo-sts-token-generator`][octo-sts-token-generator]
    - [`octo-vista-api-x`][octo-vista-api-x]
- WorldVista
  - Vivian
    - [All RPC List][vivian-all-rpc]

## Overview

VistA-API-X is an intermediate service designed to handle parameterized Remote Procedure Call (RPC)
requests over HTTP using JSON. This service has been developed to simplify the process of
interacting with VistA systems by reducing the complexity associated with using specific RPC
libraries.

By acting as an intermediary, VistA-API-X allows developers to focus on the logic of their
applications, rather than the intricacies of the RPC communication. The service translates HTTP
requests into RPC calls, and RPC responses into HTTP responses, providing a seamless and intuitive
interface for developers to interact with VistA systems.

## Intended Audience

This guide is specifically tailored for developers who are working on system integrations and will
be using the VistA-API-X service to directly interface with VistA servers. The guide provides an
in-depth understanding of how to leverage the VistA-API-X service for RPC calls, data management,
and effective interaction with VistA systems using a JSON-based API over HTTP.

## Background

VistA (Veterans Health Information Systems and Technology Architecture) is an Electronic Health
Record (EHR) system developed by the United States Department of Veterans Affairs. It is one of the
most widely used EHR systems in the world, providing an integrated inpatient and outpatient health
information system and a full suite of clinical tools.

VistA supports the healthcare operations of the Veterans Health Administration's nationwide
healthcare system. Its comprehensive functionality and proven scalability and adaptability have led
to its adoption in many other healthcare settings around the world.

The VistA system is built around a client-server architecture, which allows for the efficient
exchange of information and the seamless integration of a multitude of healthcare services. The
VistA-API-X service extends this architecture, enabling developers to interact with VistA systems
directly using modern web technologies. It's important to note that the foundation of these RPC
calls lies in Delphi, the programming language used to develop the Computerized Patient Record
System (CPRS) Chart software. This connection provides a robust and time-tested basis for the
communication between the VistA-API-X service and VistA systems.

## Development

For developers working with the VistA-API-X service, the following key documentation areas are
crucial for a comprehensive understanding of its functionalities and integration processes:

- [Onboarding][onboarding]: Initial onboarding requirements.
- [Authentication Mechanisms][authentication-mechanisms]: Overview of the security protocols and
    authentication methods supported by the service.
- [Token Handling][token-handling]: Describes how to use an Identity and Access Management (IAM) SSOi
    Security Assertion Markup Language (SAML) token to retrieve a signed user profile JSON Web
    Token (JWT) from a dedicated endpoint provided by the Office of Connected Care (OCC). This
    document also describes how to use the `sts-token-generator` program for local development and
    testing.
- [REST Endpoints][rest-endpoints]: Identification of parameterized REST endpoints for
      authentication and functional calls.
- [RPC Invocation][rpc-invocation]: Usage, use cases, and examples of RPC invocation through this
      API service.

For more detailed reference please consult the following resources:

- Department of Veterans Affairs Github
  - Repositories
    - [`cds-apps-developer-wiki`][cds-apps-developer-wiki]: Detailed reference information for RPC, ICR, and test patient information.
- Department of Veterans Affairs Sharepoint
  - VistA Office
    - [Home][vista-office]: VistA related resources, reference, guides, and governance.
    - [ICR Process and Standards][icr-process-and-standards]: ICR Process, lists, and onboarding information.
- WorldVista
  - Vivian
    - [All RPC List][vivian-all-rpc]: Contains RPC details and links to source files for review.
