# Patch Management and Maintenance Windows

## Purpose

Northstar Labs applies security and reliability updates to managed laptops, servers, network devices, and supported applications according to risk. Asset owners are responsible for keeping systems enrolled in approved management tools and for resolving failed deployments. Unsupported systems may be isolated from the network.

## Patch priorities

**Emergency patches** address active exploitation, a critical externally exposed weakness, or another risk designated by Security Operations. Deployment begins as soon as a safe update or mitigation is available, with a target of 24 hours. Normal change lead times may be shortened, but the action and approval must still be recorded.

**Critical patches** address severe vulnerabilities or major reliability defects without confirmed active exploitation. Internet-facing production systems must be patched within seven calendar days. Other managed systems must be patched within fourteen calendar days.

**Standard patches** include routine security, operating-system, browser, and application updates. They are deployed during the next regular maintenance cycle and no later than 30 calendar days after approval for use.

Security Operations may set a shorter deadline when exposure, data sensitivity, or vendor guidance increases risk.

## Maintenance windows

Employee laptops receive standard updates each Wednesday between 18:00 and 22:00 local time. Devices that are offline receive the update at their next connection. Users receive advance notice when an update requires a restart and may defer a standard restart for up to 24 hours.

The regular production server maintenance window is the second Sunday of each month from 01:00 to 05:00 UTC. Service owners must publish expected customer impact and verify monitoring after maintenance. A maintenance window permits approved work; it does not by itself authorize an undocumented production change.

Emergency security work can occur outside these windows. The incident commander or Security Operations duty manager coordinates communications when urgent remediation is likely to interrupt service.

## Testing and deployment

Where practical, patches are first deployed to a representative test group. System owners validate startup, authentication, monitoring, and critical business functions before wider rollout. A documented rollback or mitigation must exist for production changes with significant service risk.

Management tools record installation status. A device is considered patched only after the tool confirms the required update and, when applicable, the required restart. Downloading an update is not sufficient.

## Exceptions

An owner who cannot meet a deadline must submit a patch exception before the deadline. The request identifies affected assets, business justification, risk, compensating controls, remediation owner, and expiration date. Security Operations approves or rejects the exception. Exceptions expire after 30 days unless a shorter period is assigned.

Repeated failure to patch may result in network isolation or removal of production approval. End-of-life software must be upgraded, replaced, or covered by a time-limited risk acceptance from the responsible technology director and Security Operations.

## Reporting problems

Employees should leave managed computers powered on and connected during scheduled maintenance. Failed updates, repeated restart prompts, or loss of a critical function should be reported through **Device Support > Updates and Patching**. Widespread service disruption is handled through the incident severity and escalation process.
