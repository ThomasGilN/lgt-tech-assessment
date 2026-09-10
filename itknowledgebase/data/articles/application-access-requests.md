# Application Access Request Policy

## Purpose

Northstar Labs grants application access according to least privilege: each person receives only the permissions required for current work. Access is assigned to an individual identity, is approved by accountable owners, and is reviewed periodically. Shared accounts are prohibited unless Security has documented a technical exception.

## Access categories

**Standard access** covers low-risk collaboration and productivity services included in an employee's role package. The hiring manager's approved onboarding request is sufficient for these services.

**Business-sensitive access** includes customer records, internal financial information, employee data, source-code repositories, and non-production administrative functions. It requires approval from the requester's manager and the application's business owner.

**Privileged access** includes production administration, security consoles, identity administration, database administration, and the ability to change access for other users. It requires the requester's manager, the application owner, and Security Operations to approve the request. Privileged access is issued through a separate administrator identity and requires a hardware security key.

## Submitting a request

Requests are submitted through the service catalog under **Access > Application Access**. A request must identify the person receiving access, the application, the requested role, the business reason, the manager, and the required end date if access is temporary. Requests such as “same access as another employee” are not sufficient; the required role must be named.

Access must never be requested by sending passwords, tokens, or confidential customer data in a support ticket. A manager can submit a request for a direct report, but the application owner still controls approval for sensitive roles.

## Approval and fulfillment targets

Standard catalog access is normally fulfilled within one business day. Business-sensitive access is normally fulfilled within two business days after all approvals are recorded. Privileged access has a target of three business days because Security Operations verifies training, MFA, and separation-of-duties requirements.

Production database access is privileged. It requires all three approvals, must have an expiration date no later than 90 days, and is read-only by default. Write or schema-change permission requires a separate justification linked to an approved change record.

Urgency does not remove approval requirements. For a declared incident, the incident commander may request time-limited emergency access through the break-glass procedure. Emergency access expires after four hours and is reviewed on the next business day.

## Reviews and removal

Application owners review business-sensitive access every six months and privileged access every quarter. Managers must request removal when responsibilities change rather than waiting for the next review. Temporary access expires automatically on the approved date.

People Operations initiates the offboarding process for departing workers. IT disables the primary identity according to the recorded departure time, which removes normal application sessions. Application owners remain responsible for transferring owned integrations, service credentials, and business records.

Rejected requests include a reason. The requester may submit a new request with corrected information, but must not ask the Service Desk to bypass the recorded decision.
