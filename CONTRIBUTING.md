# Contributing Guidelines

Thank you for your interest in contributing to the Enterprise Full-Stack Order Management System.  
This repository follows production-grade engineering practices inspired by large-scale distributed systems.

---

## Getting Started

1. Fork the repository and clone it locally.
2. Create a feature branch from `main`:
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. Make focused, well-scoped changes.
4. Ensure the application builds and all tests pass.
5. Open a Pull Request targeting the `main` branch.

---

## Development Guidelines

- Follow clean code principles and maintain clear service boundaries.
- Prefer loosely coupled, event-driven communication using Kafka.
- Keep each service independently deployable.
- Use meaningful naming and avoid unnecessary complexity.
- Document non-obvious design decisions where applicable.

---

## Testing

- Write unit and integration tests for new functionality.
- Ensure existing tests remain passing.
- Validate message flows and edge cases for asynchronous processing.

---

## Commit Message Convention

Use clear, descriptive commit messages:
```
feat: add order validation and idempotency handling
fix: resolve kafka consumer retry edge case
test: add integration tests for payment workflow
chore: update dependencies and configuration
```

---

## Pull Request Guidelines

- Keep PRs small and focused.
- Clearly describe what was changed and why.
- Reference related issues or design decisions when applicable.
- Ensure code is formatted and reviewed before submission.

---

## Reporting Issues

When reporting bugs or issues, please include:
- Steps to reproduce
- Expected vs actual behavior
- Relevant logs or error messages
- Environment details if applicable

---

## Code of Conduct

By participating in this project, you agree to follow the project's Code of Conduct.
