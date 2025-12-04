# EasyDoc

EasyDoc is a role-based medical management platform built with Spring Boot.  
It supports patients, doctors, and admins with:

- Account registration and email verification
- Doctor onboarding and admin approval
- Appointment scheduling and management
- Medical reports with draft reminders
- Electronic prescriptions via a dedicated microservice
- Profile photos and document uploads via Cloudinary

---

# Features

- User accounts & roles
  - Patient, Doctor, Admin roles with different dashboards and permissions
  - Account status tracking (e.g. INCOMPLETE, ACTIVE)
  - Email verification with tokens

- Doctor onboarding
  - Practitioners submit detailed applications
  - Admins review and approve/reject applications
  - Approved doctors are promoted and linked to user accounts

- Appointments
  - Patients can book appointments with doctors in allowed time windows
  - Configurable examination slot intervals
  - Validation for dates (no past dates) and time slots (within working hours)

- Reports
  - Doctors can create and edit medical reports associated with appointments
  - Draft status tracking and scheduled reminders for unfinished reports

- Prescriptions (external microservice)
  - Integration with a the prescription microservice via OpenFeign
  - Create, update and issue prescriptions for appointments
  - Add/remove medicaments using a shared medicament catalog

- Notifications
  - Email verification link on registration
  - Draft report reminder emails
  - Other transactional emails (extensible)

- Media
  - Cloudinary integration for profile photos and other uploads

- Infrastructure*
  - MySQL for relational data
  - Redis for HTTP sessions and caching
  - Event-driven side effects (emails, reminders) via Spring events and async listeners

---

# Tech Stack

- Java 17
- Spring Boot 3.4.0
  - Spring Web (REST + MVC)
  - Spring Data JPA (Hibernate)
  - Spring Security
  - Spring Validation (Jakarta)
  - Spring Mail
  - Spring Cache
  - Spring Session (Redis)
  - Spring Scheduling
- Spring Cloud OpenFeign

# Data & Infrastructure

- MySQL (primary database)
- Redis (session store + cache)
- Cloudinary (media storage)
- SMTP provider (Outlook/Yahoo/etc.) for email sending

# Frontend

- Thymeleaf templates with layout/fragments
- Spring Security Thymeleaf extras
- Vanilla JS + CSS modules
- Font Awesome icons
