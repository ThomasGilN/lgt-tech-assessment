# Password and MFA Recovery Policy

## Purpose

This policy explains how Northstar Labs employees and contractors recover access when they forget a password, lock an account, or lose access to a registered multi-factor authentication device. The Service Desk must verify a person's identity before changing credentials or resetting MFA. Managers cannot bypass identity verification on behalf of another user.

## Password requirements

Passwords must be at least 14 characters long. Long passphrases are encouraged. Passwords must not contain the employee's name, email address, or easily guessed company terms. A company password must never be reused for a personal account or shared with another person, including a manager or Service Desk technician.

Passwords are changed when compromise is suspected or when the Security team directs a reset. Routine scheduled password changes are not required. Password managers approved by IT may be used to generate and store unique credentials.

## Forgotten password or locked account

Employees who still have access to their registered MFA method should use the self-service recovery link on the Northstar Labs sign-in page. A successful self-service reset unlocks the account and invalidates active browser sessions within fifteen minutes.

If self-service recovery is unavailable, contact the Service Desk through the support portal or call the published internal support number. Do not send identity documents, passwords, recovery codes, or photographs through email or chat. The Service Desk verifies the requester using the approved identity-verification procedure and issues a time-limited recovery link. Recovery links expire after 30 minutes and may be used once.

Five failed sign-in attempts within ten minutes temporarily lock an account for fifteen minutes. Repeated lockouts should be reported because they may indicate an old saved password, a misconfigured device, or an attempted attack.

## Lost or replaced MFA device

If a registered phone or hardware token is lost, stolen, replaced, or wiped, report it to the Service Desk immediately. The Service Desk revokes the old authenticator before enrolling a replacement. Users must not approve unexpected MFA prompts while waiting for recovery.

When available, a previously registered backup hardware key can be used to sign in and register the replacement device. Recovery codes are for emergencies only, must be stored securely, and must not be kept in the same bag as the primary device.

The Service Desk may provide a temporary access pass after identity verification. A temporary pass expires after eight hours and is intended only to complete MFA enrollment. It is not a permanent alternative to MFA.

## Suspected compromise

Unexpected MFA prompts, an unrecognized password-reset notification, or successful sign-in activity from an unknown location must be reported as a security incident. Reject unexpected prompts, change the password from a trusted device, and contact the Service Desk. If a prompt was approved accidentally, call the Service Desk rather than waiting for an email response.

Privileged administrator accounts require a hardware security key. Recovery of a privileged account also requires approval from the Security Operations duty manager.
