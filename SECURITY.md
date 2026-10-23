# Security Policy

## Supported Versions

We release patches for security vulnerabilities in the following versions:

| Version | Supported |
| ------- | ---------- |
| 2.x (current TripKit Android releases from `main`) | :white_check_mark: |
| 1.x (legacy) | :warning: Best-effort fixes only |
| < 1.0 | :x: |

> **Note:** TripKit Android is distributed through JitPack. Update this matrix whenever the supported major version changes.

---

## Reporting a Vulnerability

We take the security of our software seriously. If you believe you have found a security vulnerability, please report it to us as described below.

### How to Report

**Please do not report security vulnerabilities through public GitHub issues.**

Instead, please email us at **[security@skedgo.com](mailto:security@skedgo.com)**.

You should receive a response within 48 hours. If for some reason you do not, please follow up via email to ensure we received your original message.

### What to Include

Please include the following information in your report:

- Type of issue (e.g., buffer overflow, SQL injection, cross-site scripting, etc.)
- Full paths of source file(s) related to the manifestation of the issue
- The location of the affected source code (tag/branch/commit or direct URL)
- Any special configuration required to reproduce the issue
- Step-by-step instructions to reproduce the issue
- Proof-of-concept or exploit code (if possible)
- Impact of the issue, including how an attacker might exploit it

This information will help us triage your report more quickly.

### What to Expect

After you submit a report, we will:

1. **Acknowledge** your email within 48 hours
2. **Investigate** the issue and confirm the vulnerability
3. **Keep you informed** of our progress toward a fix
4. **Release** a security patch as appropriate
5. **Credit** you in our release notes (if you wish to be named)

---

## Security Best Practices for Contributors

If you're contributing to this project, please follow these security guidelines:

### Code Review
- All code changes must go through Pull Requests
- PRs require approval from at least one maintainer before merging
- No direct commits to `main` or protected branches

### Dependencies
- Keep Gradle dependencies up to date (Android Gradle Plugin, Kotlin, RxJava, OkHttp, protobuf, Play Services, etc.)
- Review dependency bumps for known CVEs (Dependabot alerts are enabled)
- Align `compileSdk`/`targetSdk` with the latest Android API level used by TripGo/partners
- Verify third-party transit data feeds and serialization libraries before upgrading

### Secrets Management
- **Never** commit TripKit API keys, partner tokens, keystores, or signing passwords
- Sample modules (`TripKitSamples`, `TripKitAndroidSample`) must use placeholder keys stored in local `gradle.properties`
- Use secure secret storage (AWS SSM, GitHub Secrets, CI variables) for publishing credentials and release automation
- Review commits for accidentally included secrets before pushing; GitHub secret scanning is enabled

### Secure Coding
- Follow [OWASP Top 10](https://owasp.org/www-project-top-ten/) and Android security best practices
- Validate and sanitize all inputs from TripGo APIs, GTFS feeds, and client apps
- Use parameterized queries for SQLite/Room accessors inside TripKit persistence modules
- Implement proper authentication and authorization flows for TripKit backend calls (OAuth tokens, HMAC, etc.)
- Use HTTPS/TLS ≥ 1.2 and enforce `networkSecurityConfig` for all outbound requests, including WebSocket upgrades
- Dispose of RxJava subscriptions, timers, and coroutines to avoid resource leaks and potential denial-of-service conditions

### Android SDK Considerations
- Respect Android permission scopes; TripKit SDK must surface permission requirements to host apps rather than requesting silently
- Ensure cached trip data, schedules, and location history are encrypted when stored locally
- Harden background services and notifications (foreground service declarations, battery optimizations)
- Obfuscate release artifacts (R8/ProGuard) and keep consumer ProGuard files up to date
- Prevent debug tooling, logging endpoints, and feature flags from leaking into production builds

---

## Security Features

This project includes the following security measures:

- **Dependabot alerts** enabled for vulnerable dependencies
- **Secret scanning** enabled to prevent credential leaks
- **GitHub Actions `CI` workflow** runs unit tests (`./gradlew test -x :TripKitSamples:test`) on pull requests
- **Code review** required for all changes
- **Branch protection** rules enforced on `main/develop`

---

## Disclosure Policy

We follow a **coordinated disclosure** approach:

1. Security issues are privately investigated and patched
2. A security advisory is prepared but not published
3. We notify relevant parties (e.g., major users, downstream projects)
4. A patch release is made available
5. The security advisory is published after users have had time to update

We aim to complete this process within 90 days of the initial report, though complex issues may take longer.

---

## Security Update Policy

Security updates are released as:
- **Patch versions** (x.x.X) for currently supported versions
- **Security advisories** published on our GitHub Security Advisories page
- **Release notes** clearly marking security-related changes

---

## Additional Resources

- [OWASP Top 10](https://owasp.org/www-project-top-ten/)
- [CWE Top 25 Most Dangerous Software Weaknesses](https://cwe.mitre.org/top25/)
- [GitHub Security Best Practices](https://docs.github.com/en/code-security)

---

## Contact

For general security questions or concerns, please contact:
- **Email:** [security@skedgo.com](mailto:security@skedgo.com)

---

> **Last Updated:** 2025-11-14  
> **Version:** 1.1  
> 
> This security policy is maintained by the repository maintainers and reviewed regularly.
