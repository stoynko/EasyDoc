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

  ---
- Index Page
<img width="2448" height="3714" alt="index" src="https://github.com/user-attachments/assets/a6c44b35-ad4e-44fb-9436-797fa6e2f036" />

- Register Page
<img width="2914" height="1670" alt="localhost_8080_register" src="https://github.com/user-attachments/assets/c8f61da6-3456-4750-990b-b5b2aa8a3f2a" />

- Login Page
<img width="2914" height="1670" alt="localhost_8080_login (1)" src="https://github.com/user-attachments/assets/38a2b94d-4f8c-47ac-9c16-e69888cf56fd" />

- Account Onboarding
<img width="2884" height="2390" alt="localhost_8080_onboarding_account (2)" src="https://github.com/user-attachments/assets/2509645c-07c0-4e6e-9684-017639100370" />

 ---

- Settings (Personal Information)
<img width="2884" height="2490" alt="localhost_8080_settings_personal-info" src="https://github.com/user-attachments/assets/45daa278-504b-4e0e-a32d-750f08cef25d" />

- Settings (Security Details)
<img width="2884" height="3096" alt="localhost_8080_settings_security" src="https://github.com/user-attachments/assets/76d38e9f-d6c6-4878-8d4b-b3669d706e17" />

- Settings (Delete Account)
<img width="2884" height="2342" alt="localhost_8080_settings_delete" src="https://github.com/user-attachments/assets/f6b0182c-9e9e-4ecc-9ce9-4a6e04b4ebc2" />

 ---

- Users panel (Admin)
<img width="2884" height="1976" alt="localhost_8080_users" src="https://github.com/user-attachments/assets/f2254346-2f67-4163-a367-43a1849ac48e" />

- Practitioner Applications (Admin)
<img width="2884" height="3896" alt="localhost_8080_applications" src="https://github.com/user-attachments/assets/9426f80a-6292-4885-8dbf-e73ab9d3e097" />

 ---
