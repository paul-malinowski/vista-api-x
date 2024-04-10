# VistA-API-X Service Onboarding Documentation

[octo-vista-api-x]:
    https://github.com/department-of-veterans-affairs/octo-vista-api-x

### Table of Contents

- [Overview](#overview)
- [Project Setup](#project-setup)

### Links

- Department of Veterans Affairs Github
  - Repositories
    - [`octo-vista-api-x`][octo-vista-api-x]

## Overview

The onboarding process for the VistA-API-X Service includes the request for a project-specific API
key. This key enables access to necessary VistA security contexts and Remote Procedure Calls (RPCs)
essential for project requirements. Early identification of these elements is crucial for seamless
integration. Addressing dependencies listed in this document and completing all onboarding
requirements are vital steps for successful service integration.

## Project Setup

1. **Project Details Preparation:**

    Before initiating contact, it's important to prepare comprehensive details regarding the
    project. This preparation involves listing VistA station numbers, identifying the environments
    with which the project will interact, and specifying the VistA security contexts and RPCs
    required by the project. Having this information ready streamlines the onboarding process and
    ensures the project's needs are clearly communicated from the outset.

    For Clinical Decision Support (CDS) projects, detailing all required environments due to the
    often multiple active clinical settings is beneficial. This is best achieved by clarifying which
    tools will be necessary during different phases of application development. For example, if the
    project requires access to the Clinical Health API in a specific Lighthouse environment and
    needs to gather a SAML token from a certain IAM environment, then identifying both environments
    contextually is important in this initial step.

2. **Initial Contact:**

    With project details prepared, the next step is to reach out to the onboarding support as
    outlined in the README.md at the root of the [octo-vista-api-x][octo-vista-api-x] repository.
    This action connects the project with the necessary support and guidance for effectively
    navigating the onboarding process.

3. **Onboarding Process Engagement:**

    Following initial contact, the onboarding team will review the project details. This review
    includes assessing the need for any Integration Control Registrations (ICRs) and starting the
    process for issuing a project-level API key. This phase can take several days as the integration
    requirements of the project are thoroughly evaluated.

4. **API Key Registration and Compliance:**

    The initial provision of details for the development environment sets the stage for project
    integration across other environments. Should the scope or requirements of the project evolve,
    adjustments to the API key may be necessary to align with telemetry, process, and auditing
    standards. A new set of API keys might be issued to accommodate these changes, ensuring the
    project remains compliant and up to date.
