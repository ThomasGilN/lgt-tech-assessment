# Incident Severity and Escalation Standard

## Purpose

This standard gives Northstar Labs a consistent way to classify technology incidents, set response expectations, and involve the right people. Severity describes current business impact, not how difficult a problem appears to be. Severity can be raised or lowered as impact changes.

## Severity levels

### Severity 1 — Critical

A Severity 1 incident causes a complete outage of a customer-facing production service, a confirmed active security compromise, widespread inability to work across multiple departments, or a credible risk to life or safety. There is no acceptable workaround.

The Service Desk or monitoring team immediately pages the on-call incident commander and responsible technical team. Acknowledgment is expected within 10 minutes. A shared incident channel and bridge are opened, and stakeholder updates are issued every 30 minutes until service is stable. Executive and Communications contacts are engaged when customer impact is material.

### Severity 2 — High

A Severity 2 incident substantially degrades an important service, affects a full department, or causes customer impact for which a limited workaround exists. It may also cover a high-risk security event that is contained but not fully resolved.

The owning team is paged and should acknowledge within 30 minutes. Progress updates are provided at least every 60 minutes. The incident commander decides whether increasing impact requires reclassification as Severity 1.

### Severity 3 — Moderate

A Severity 3 incident affects a small group, interrupts a non-critical service, or has a practical workaround. The owning team responds during support hours with a target acknowledgment of four business hours. Examples include a shared printer failure, a team-specific integration problem, or intermittent access for several users.

### Severity 4 — Low

A Severity 4 incident has minor or individual impact and no significant security or operational risk. Examples include a single-user configuration issue or a request for guidance. It is handled through the normal Service Desk queue with a target acknowledgment of one business day.

## Reporting an incident

Report Severity 1 and suspected Severity 2 incidents by phone using the internal incident hotline. A support ticket alone is not sufficient for urgent incidents. Provide the affected service, observed impact, start time, geographic scope, known workaround, and a safe contact method. Do not include passwords, access tokens, or unnecessary personal data.

Employees should report suspicious security events even when impact is uncertain. Security Operations determines the security classification and coordinates containment.

## Roles and escalation

The incident commander coordinates priorities, assigns actions, approves stakeholder updates, and maintains the timeline. Technical leads diagnose and restore service. A communications lead prepares user-facing messages. The person who first reports an incident does not need to remain the incident commander.

If the assigned team does not acknowledge a Severity 1 page within 10 minutes, the paging system escalates to the secondary on-call engineer and operations manager. After another 10 minutes, it escalates to the technology duty executive. Responders must not delay escalation while searching for a perfect diagnosis.

Every Severity 1 and significant Severity 2 incident receives a blameless review within five business days. The review records impact, timeline, contributing conditions, corrective actions, owners, and due dates.
