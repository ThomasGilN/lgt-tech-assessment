# Remote Access and VPN Guide

## Purpose and scope

Northstar Labs provides a managed virtual private network, called Northstar Secure Access, for employees and approved contractors who need to reach internal systems from outside a company office. Public web applications such as email and the support portal may not require the VPN. Internal administration consoles, engineering environments, and shared network drives require it.

## Requirements

Remote access is allowed only from a company-managed computer that has current security updates, disk encryption, endpoint protection, and an active device certificate. The user must sign in with an individual company account and complete multi-factor authentication. Personal computers, shared family devices, and public kiosk computers cannot connect to the corporate VPN.

Employees receive standard remote access automatically when their role requires it. Contractors require a named Northstar sponsor, a documented business purpose, an expiration date, and approval through the application access request process. Contractor VPN access expires after 90 days unless the sponsor renews it.

## Connecting

1. Connect the computer to a trusted internet connection.
2. Open the Northstar Secure Access client from the system tray or Applications folder.
3. Select the region nearest to the current physical location.
4. Sign in with the company account and approve the expected MFA prompt.
5. Confirm that the client shows **Connected** before opening an internal resource.

The VPN disconnects after twelve hours and requires a new sign-in. It may also request authentication after the computer wakes from a long sleep. Users must not configure the client to save passwords or bypass MFA.

## Network safety

When using hotel, airport, conference, or café Wi-Fi, connect to the VPN before accessing company data. Avoid networks that request installation of an unknown certificate or browser extension. A mobile hotspot is preferred when a public network behaves unexpectedly.

Split tunneling is enabled for ordinary internet traffic, but routes to internal systems remain protected. Users must not install personal VPN products on a managed computer because competing network filters can interrupt Northstar Secure Access.

## Troubleshooting

If authentication fails, verify that the computer's date and time are correct and that the expected MFA method is available. If the client reports that the device is not compliant, install pending operating-system updates, restart the computer, and try again. Compliance status may take up to fifteen minutes to refresh.

If the VPN connects but an internal service remains unavailable, record the service name, time, selected VPN region, and error message. Disconnect once, reconnect to another region, and retry. Do not repeatedly attempt sign-in if the account becomes locked.

For persistent problems, submit a Service Desk request under **Network > Remote Access**. A widespread VPN failure that prevents a department from working should be reported by phone and handled under the incident severity standard.

## Prohibited use

Users may not share a VPN session, lend a managed device to another person, expose an internal service through port forwarding, or disable security controls to obtain a connection. Suspected loss or theft of a connected device must be reported immediately.
