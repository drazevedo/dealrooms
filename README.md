# Dealrooms

Backend Java Spring Take Home Challenge: Deal Room Management System

## Overview

Build a RESTful API for a Deal Room Management System used for secure document sharing between borrowers, lenders and guests. The system must support file management operations and granular permissions. This challenge assesses your ability to design and build scalable backend systems with security features.
Expected Time: 6-8 hours  
Deadline: 3 days

## Business Context

A Deal Room (Virtual Data Room) is a secure online repository used during financial transactions, fundraising, IPOs, and other financial deals where sensitive information needs to be shared with multiple parties while maintaining strict access controls. Your system will manage the documents involved in these high-stakes transactions.

## Business Requirements

### Core Functionality

You need to build an advanced Deal Room Management System with:
Deal Room Context: Organize files within deal rooms for different transactions
Document Management: Secure upload, versioning, and sharing of confidential deal documents
Access Control: Granular permissions for different deal participants (buyers, sellers, advisors)

### User Stories

As an admin, I want to create separate deal rooms for specific deals
As an admin, I want to control which documents each party can access in the deal room
As an admin, I want only deal room members to have access to a specific deal room
As an admin, I want to be able to share specific files or folders to specific members of a deal room
As a borrower, I want to bulk upload financial documents within folders (can be a .zip)
As a lawyer, I want the files to have statuses I can change after review, say “REVIEWED”

### Technical Requirements

#### Technology Stack

- Java 21 or higher
- Spring Boot 3.x
- Spring Data Mongo
- Spring Security (for role-based deal room access control, can be Basic Authentication)
- Spring WebMVC
- MongoDB (should be easily swappable)
- Local File Storage (with abstraction for cloud storage, should be easily swappable)
- Maven or Gradle
- JUnit 5 & Mockito for testing

#### Deal Room Specific Business Rules

##### Access Control Hierarchy

- Deal Room Level: Overall access to the deal room
- Folder Level
- Document Level: Granular document permissions

##### Document Security Rules

- Download Restrictions: Based on user type and access
- View/Access Restrictions: For sensitive financial documents

##### Performance Requirements

###### Document Performance

- Upload up to 1GB files
- Bulk upload 100+ documents

### Implementation Requirements

#### Code Architecture

- Layered Architecture: Clear separation between deal room and document layers
- Domain-Driven Design: Rich domain models for deal room business logic
- SOLID Principles: Demonstrate understanding of SOLID principles
- Minimum 70% code coverage.

#### Documentation

- README.md with:
  - Deal room architecture overview
  - Security model explanation
  - Permission hierarchy documentation
  - API usage examples for common deal scenarios
- API Documentation: OpenAPI/Swagger required

### Evaluation Criteria

#### Core Deal Room Functionality

Deal room creation and management
Multi-party access control

#### Document Management for Deals

Secure document upload/download
Bulk operations

#### Permissions

Granular permission system

#### Performance & Scalability

Handle multiple concurrent deals
Large document handling
Bulk operations efficiency

#### Code Quality & Architecture

Clean, maintainable code
Proper design patterns
SOLID principles
Error handling

#### Testing

Comprehensive test coverage
Deal-specific edge cases

#### Bonus Points (Optional)

Vite React UI: Visualize a specific deal room, its permissions and users.

### Submission Guidelines

- Code Repository
  - Private GitHub/GitLab repository, shared with araposo@bridgemarketplace.com and ddias@bridgemarketplace.com.
  - Clear commit history showing progress
  - Branch strategy for features
- Documentation
  - Architecture diagram showing deal room structure
  - Permission model diagram
  - Database schema with all entities
- Evidence
  - Can be screenshots or images of the results
  - Sample documents
  - Multiple user roles and permissions
  - Postman collection for all endpoints
- Loom Video
  - Record an up to 5 minute video explaining the solution. Please keep to the time limit.

### Critical Success Factors

Your solution will be evaluated as "successful" if:
1. Deal Room Management: Complete deal room lifecycle implementation
2. Security: Robust security and permission system
3. Document Management: Secure document handling with versioning
4. Code Quality: Clean architecture appropriate for financial systems
