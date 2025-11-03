---
  layout: default.md
  title: "Cameron Loh's Project Portfolio Page"
---

### Project: playbook.io

**playbook.io** is a platform that helps sports agents efficiently manage athletes, organizations, and contracts. It combines the speed of a Command Line Interface with the ease of a Graphical User Interface, enabling agents to stay organized, build stronger relationships, and focus on closing deals.

Given below are my contributions to the project.

---

* **New Feature**: Implemented the ability to edit athletes and organizations, and contributed to the contracts feature. [\#160](https://github.com/AY2526S1-CS2103T-F13-3/tp/pull/160)
    * **What it does:** Allows users to update athlete or organization details through the `edit-a` and `edit-o` commands, and ensures that contract information remains consistent when linked to these entities.
    * **Justification:** Editing and maintaining accurate data is vital for agents managing multiple clients and contracts. This feature improves usability and prevents users from needing to delete and re-create entities for simple updates.
    * **Highlights:** The feature required the design of new parser and descriptor classes (`EditAthleteCommandParser`, `EditOrganizationCommandParser`, etc.) and integration with the model layer while ensuring immutability of unique identifiers (e.g., name and sport). Special care was taken to ensure contracts remained logically consistent after edits.
    * **Credits:** Based on the architecture of the `Add` and `Delete` commands from AddressBook-Level 3, with extended validation and integration logic for multi-entity relationships.

---

* **Code Contributed:**
    * [RepoSense link](https://nus-cs2103-ay2526s1.github.io/tp-dashboard/?search=&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2025-09-19T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=cameronlzy&tabRepo=AY2526S1-CS2103T-F13-3%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false)

---

* **Project Management:**
    * Contributed to milestone planning and ensured feature branches (Edit, Contract) were merged on schedule.
    * Reviewed teammate PRs and provided technical feedback focused on parser consistency, validation logic, and code clarity.

---

* **Enhancements to existing features:**
    * Improved contract linking logic to prevent invalid references after entity deletions or edits. (Pull requests [\#115](https://github.com/AY2526S1-CS2103T-F13-3/tp/pull/115), [\#171](https://github.com/AY2526S1-CS2103T-F13-3/tp/pull/171))
    * Refined validation checks for fields such as `Email` and `Sport` using the Apache Commons Validator library.
    * Expanded unit and integration tests for `edit`, `contract`, and `parser` components to strengthen regression safety. (Pull request [\#202](https://github.com/AY2526S1-CS2103T-F13-3/tp/pull/202))

---

* **Documentation:**
    * **Developer Guide:**
        * Authored the **Edit Feature** design section, including sequence, class, and object diagrams. (Pull requests [\#245](https://github.com/AY2526S1-CS2103T-F13-3/tp/pull/245), [\#251](https://github.com/AY2526S1-CS2103T-F13-3/tp/pull/251))
        * Created the **AddAthleteCommand** and **EditAthleteCommand** sequence diagrams to visually explain command execution flow.
        * Documented design considerations such as identifier immutability and contract consistency.
        * Reviewed and standardized diagram formatting (based on `style.puml`) for clarity and consistent visual presentation.
    * **User Guide:**
        * Added usage instructions and examples for the `edit-a` and `edit-o` commands.
        * Edited formatting, tone, and examples across the document for readability and consistency.

---

* **Testing:**
    * Wrote and refactored JUnit tests for `Email`, `OrganizationEmail`, and parser classes to improve coverage and alignment with Apache Commons Validator.
    * Contributed to integration testing of contract-related commands, ensuring correct linkage between athletes, organizations, and contracts.

---

* **Community:**
    * Reviewed peer pull requests with feedback on code structure and logic.
    * Assisted in debugging test failures and resolving merge conflicts during feature integration.

---

* **Tools:**
    * Maintained and standardized PlantUML styling across all diagrams (`style.puml`).
    * Helped refine Gradle testing configurations for consistent results across development environments.
    * Used Apache Commons Validator to do email verification

---
