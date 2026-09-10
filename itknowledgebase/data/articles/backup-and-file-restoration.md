# Backup and File Restoration Policy

## Purpose and scope

Northstar Labs backs up designated company services so that business information can be recovered after accidental deletion, corruption, or a service failure. Backups reduce risk but do not replace correct storage practices. Employees are responsible for saving business files in approved managed locations.

Covered locations include managed team file spaces, approved cloud document libraries, production databases, and configuration repositories listed in the service catalog. Files stored only on a laptop's local desktop, Downloads folder, removable drive, personal cloud account, or unapproved application are not guaranteed to be backed up.

## Backup schedule and retention

Managed team file spaces and cloud document libraries are protected daily. Daily recovery points are retained for 30 days. Weekly recovery points are retained for 12 weeks. Production database backup schedules are defined by each service owner, but must include at least one daily recovery point and a documented recovery test.

Deleted items may also remain in a service's recycle bin for a limited period. The recycle bin is convenient but is not a substitute for backups, and users should not delay a restore request while waiting for an item to reappear.

Backups containing confidential data inherit the source system's security classification. Access to backup media and restore tooling is restricted to authorized operations personnel. Backups must not be copied to personal storage for convenience.

## Requesting a restoration

Submit a Service Desk request under **Data Services > File Restoration**. Include:

- The managed location or service name.
- The full folder and file name, if known.
- The approximate time when the file last appeared correctly.
- Whether the file was deleted, overwritten, or corrupted.
- The business impact and required deadline.
- The owner who can authorize access if the requester is not the file owner.

Do not include passwords, encryption keys, or unrelated confidential content in the ticket. The Service Desk confirms that the requester is allowed to receive the restored data before operations begins recovery.

Ordinary file restores have a target completion time of two business days. A restore supporting a declared Severity 1 or Severity 2 incident is prioritized through the incident process. Completion time depends on data volume, recovery-point availability, and integrity checks.

Restored data is normally placed in a temporary restricted folder so the requester can compare it with the current version. Operations does not overwrite current production data without an approved change and explicit service-owner authorization.

## Recovery limitations

A requested point in time may not exist, particularly for a file created and deleted between daily backups. Encrypted or corrupted source data may also be unrecoverable. Operations communicates the nearest available recovery point and records unsuccessful attempts.

Service owners must test representative restores at least annually. Production database owners perform recovery exercises according to their service continuity plan. A successful backup job alone is not evidence that an application can be fully restored.
