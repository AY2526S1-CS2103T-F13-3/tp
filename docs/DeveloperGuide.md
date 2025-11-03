---
layout: default.md
title: "Developer Guide"
pageNav: 3
---

# Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

---

## **Acknowledgements**

This project was built upon the [_AddressBook-Level3 (AB3)_](https://github.com/nus-cs2103-AY2526S1/tp) codebase
provided by the National University of Singapore’s **CS2103T Software Engineering** module.

This project also makes use of the [_Apache Commons Validator_](https://commons.apache.org/proper/commons-validator/) library for email validation functionality, licensed under the Apache License 2.0.

---

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

---

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="400" />

The **_Architecture Diagram_** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [
`Main`](https://github.com/AY2526S1-CS2103T-F13-3/tp/blob/master/src/main/java/seedu/address/Main.java) and [
`MainApp`](https://github.com/AY2526S1-CS2103T-F13-3/tp/blob/master/src/main/java/seedu/address/MainApp.java)) is in
charge of the app launch and shut down.

- At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
- At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

- [_`User Interface`_](#ui-component): The user interface (UI) of the App.
- [_`Logic`_](#logic-component): The command executor.
- [_`Model`_](#model-component): Holds the data of the App in memory.
- [_`Storage`_](#storage-component): Reads data from, and writes data to, the hard disk.

[_`Commons`_](#common-classes) represents a collection of classes used by multiple other components.

<div style="page-break-before: always;"></div>

### How the architecture components interact with each other

The _Sequence Diagram_ below shows how the components interact with each other for the scenario where the user issues
the command `delete-a n/LeBron s/Basketball`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

- defines its _API_ in an `interface` with the same name as the Component.
- implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API
  `interface` mentioned in the previous point).

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using
the `LogicManager.java` class which follows the `Logic` interface.
Other components interact with a given component through its interface rather than the concrete class (reason: to
prevent outside components from being coupled to the implementation of a component), as illustrated in the (partial)
class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

<div style="page-break-before: always;"></div>

### UI component

The **API** of this component is specified in [
`Ui.java`](https://github.com/AY2526S1-CS2103T-F13-3/tp/blob/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component" width="800"/>

The UI consists of a `MainWindow` that is made up of parts e.g. `CommandBox`, `ResultDisplay`, `AthleteListPanel`,
`OrganizationListPanel`, `ContractListPanel`, `StatusBarFooter`, etc.
All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between
classes that represent parts of the visible GUI.

The `UI` component uses the JavaFX UI framework.
The layout of these UI parts is defined in matching `.fxml` files that are in the `src/main/resources/view` folder.
For example, the layout of the [
`MainWindow`](https://github.com/AY2526S1-CS2103T-F13-3/tp/blob/master/src/main/java/seedu/address/ui/MainWindow.java)
is specified in [
`MainWindow.fxml`](https://github.com/AY2526S1-CS2103T-F13-3/tp/blob/master/src/main/resources/view/MainWindow.fxml).

The `UI` component,

- executes user commands using the `Logic` component.
- listens for changes to `Model` data so that the UI can be updated with the modified data.
- keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
- depends on some classes in the `Model` component, as it displays `Athlete`, `Organization`, and `Contract` objects
  residing in the `Model`.

<div style="page-break-before: always;"></div>

### Logic component

**API** : [
`Logic.java`](https://github.com/AY2526S1-CS2103T-F13-3/tp/blob/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:
<puml src="diagrams/LogicClassDiagram.puml" width="550" height="375"/>

<div style="page-break-before: always;"></div>

The sequence diagrams below illustrate the interactions within the `Logic` component,
taking the `add-a` command as an example.

<puml src="diagrams/AlternateAddAthleteSD.puml" width="800" />
<puml src="diagrams/ParsingAddSequenceDiagram.puml" width="800" />

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it passes the user input to an `AddressBookParser` object, which
   in turn creates a parser that matches the command (e.g., `AddAthleteCommandParser` or `DeleteAthleteCommandParser`)
   and uses it to parse the command.
2. This results in a `Command` object (more precisely, an instance of one of its subclasses such as
   `AddAthleteCommand` or `DeleteAthleteCommand`) which is executed by the `LogicManager`.
3. The command communicates with the `Model` when executed (e.g., to add or delete an athlete).
   Although this is shown as a single step in the diagrams above for simplicity, the actual implementation involves
   multiple interactions between the `Command` and the `Model`.
4. The result of the command execution is encapsulated as a `CommandResult` object, which is returned back from `Logic`.

<div style="page-break-before: always;"></div>

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing user commands:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:

- When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser`
  (`XYZ` is a placeholder for the specific command name, e.g., `AddAthleteCommandParser`,
  `AddOrganizationCommandParser`,
  `AddContractCommandParser`, `DeleteAthleteCommandParser`, etc.), which uses the other classes shown above to parse
  the user command and create an `XYZCommand` object (e.g., `AddAthleteCommand`).
- All `XYZCommandParser` classes (e.g., `AddAthleteCommandParser`, `DeleteAthleteCommandParser`,
  `AddOrganizationCommandParser`)
  implement the `Parser` interface so that they can be treated uniformly (e.g., during testing).

<div style="page-break-before: always;"></div>

### Model component

**API** : [
`Model.java`](https://github.com/AY2526S1-CS2103T-F13-3/tp/blob/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="700" />

The `Model` component,

- stores the application data — i.e., all `Athlete`, `Organization`, and `Contract` objects, which are contained in
  `UniqueAthleteList`, `UniqueOrganizationList`, and `UniqueContractList` objects respectively.
- stores the currently “selected” data (e.g., results of a search query) as separate _filtered_ lists, which are exposed
  to the outside as unmodifiable `ObservableList<>` objects (for example, `ObservableList<Athlete>`). This allows the UI
  to automatically update when the underlying data changes.
- stores a `UserPrefs` object that represents the user’s preferences. This is exposed to the outside as a
  `ReadOnlyUserPrefs` object.
- does not depend on any of the other three components (as the `Model` represents domain data that should make sense
  independently of other layers).

### Storage component

**API** : [
`Storage.java`](https://github.com/AY2526S1-CS2103T-F13-3/tp/blob/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,

- can save both application data (including `Athlete`, `Organization`, and `Contract` information) and user preference
  data in JSON format, and read them back into corresponding objects.
- inherits from both `AddressBookStorage` and `UserPrefsStorage`, which means it can be treated as either one if only
  the functionality of one is needed.
- depends on some classes in the `Model` component, because the `Storage` component’s job is to save and retrieve
  objects that belong to the `Model`.

<div style="page-break-before: always;"></div>

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

### Data types and overflow protection

**Contract amount handling**

The application has been designed to handle large contract amounts that are common in professional sports:

* **Data type**: Contract amounts use `long` integers (64-bit) instead of `int` (32-bit)
* **Maximum value**: Up to 9,223,372,036,854,775,807 (approximately 9.2 quintillion)
* **Limit applied**: Both individual contract amounts and total contract amounts for an athlete or organization cannot
  exceed this maximum value
* **Overflow protection**: The `ensureNoTotalOverflow()` method validates total contract amounts using predicate-based
  filtering and checks both athlete and organization totals before adding new contracts
* **Validation logic**: Uses `sumForPredicate()` to calculate current totals and `wouldOverflow()` to detect if adding a
  new contract would exceed the maximum limit
* **UI display**: Contract totals are properly formatted using `NumberFormat` for large amounts

**Why `long` instead of `int`**

* **Real-world requirements**: Professional sports contracts can exceed $2 billion (`int` limit: ~2.1 billion)
* **Cumulative totals**: Popular athletes/organizations can have total contracts exceeding `int` limits
* **Future-proofing**: Protects against inflation and larger contract values
* **Error prevention**: Eliminates negative overflow display bugs in UI chips

<div style="page-break-before: always;"></div>

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes noteworthy details on how certain features are implemented.

---

### **[Implemented] Find command**

The **Find** command enables users to perform keyword-based searches across different data types within the system —
athletes, organizations, and contracts — using flexible, case-insensitive, and fuzzy matching.
It temporarily filters the displayed lists in memory without altering any saved data.

---

The `FindCommand` extends `Command` and is invoked with one required flag that specifies the **search scope**.
The following flags are supported:

| Flag  | Description                          |
|-------|--------------------------------------|
| `an/` | Finds athletes by name               |
| `as/` | Finds athletes by sport              |
| `on/` | Finds organizations by name          |
| `ca/` | Finds contracts by athlete name      |
| `co/` | Finds contracts by organization name |
| `cs/` | Finds contracts by sport             |

Example usage:

```
find an/Lionel
```

Upon execution, the command:

1. Validates that exactly one flag is present.
2. Extracts the keyword and normalizes it to lowercase.
3. Delegates filtering to an appropriate method in `Model`, depending on the flag.
4. Applies a **multi-tier fuzzy-matching predicate** combining substring and Levenshtein-distance checks.
5. Constructs a `CommandResult` reporting how many entities matched.

<puml src="diagrams/FindSequenceDiagram-Logic.puml" alt="FindSequenceDiagram-Logic" width="750" />

<div style="page-break-before: always;"></div>

---

#### Search scopes and behavior

Each flag maps to a `SearchScope` enum constant that defines:

- A label (e.g., “athletes”, “organizations”, “contracts”)
- The `UiTab` to display in the interface
- A custom `apply(Model, keyword)` implementation that calls the relevant model filter

This enum-based design ensures that each scope encapsulates its filtering logic neatly within a single override,
improving maintainability and readability.

<puml src="diagrams/FindScopeClassDiagram.puml" alt="FindScopeClassDiagram" width="700" />

<div style="page-break-before: always;"></div>

---

#### Matching logic

The matching mechanism performs **three-tier Levenshtein-based fuzzy matching** implemented within the command itself:

1. **Exact substring check** — returns a match if the keyword appears as a case-insensitive substring.
2. **Full-text Levenshtein match** — compares the entire field value against the keyword, allowing a small number of
   edits based on keyword length.
3. **Word-by-word Levenshtein match** — splits the text into words and matches each token individually.

This approach allows tolerant and human-friendly searches (e.g., `find an/leo` matches “Lionel Messi”;
`find co/arsnal` matches “Arsenal”).

<puml src="diagrams/FindMatchingActivityDiagram.puml" alt="FindMatchingActivityDiagram" width="700" />

<box type="info" seamless>
<strong>Note:</strong>
The command only affects filtered views in memory.
Persistent data stored on disk remains unchanged.
</box>

<div style="page-break-before: always;"></div>

---

#### Example flow

The following scenario demonstrates how a typical command executes:

**Step 1.** The user executes `find co/Arsenal`.  
**Step 2.** The parser constructs a `FindCommand` with scope `CONTRACT_ORGANIZATION` and keyword `Arsenal`.  
**Step 3.** The command invokes `model.updateFilteredContractList(predicate)`.  
**Step 4.** The UI’s observable list updates, displaying all contracts linked to organizations matching “Arsenal”.  
**Step 5.** A `CommandResult` reports the number of matching contracts and switches the active tab to **Contracts**.

<puml src="diagrams/FindActivityDiagram.puml" alt="FindActivityDiagram" width="500" />

---

#### Design considerations

**Aspect: How search scope is determined**

- **Alternative 1 (current choice):** Use an enum (`SearchScope`) with one method per scope.
    - Pros: Compact, type-safe, easily extensible for new scopes.
    - Cons: Slightly more complex when flags overlap semantically.
- **Alternative 2:** Use a single switch statement on the flag.
    - Pros: Simple and direct for small command sets.
    - Cons: Harder to extend; increases method size.

**Aspect: Matching method**

- **Alternative 1 (current choice):** Custom Levenshtein-based fuzzy match with multi-tier logic.
    - Pros: Natural partial and typo-tolerant matches without external libraries.
    - Cons: Slightly higher computational cost on large datasets.
- **Alternative 2:** Simple exact or tokenized substring match.
    - Pros: Faster and easier to reason about.
    - Cons: Misses partial and typo-tolerant results.

<div style="page-break-before: always;"></div>

### [Proposed] Edit feature

#### Proposed Implementation

The **Edit** feature enables users to update existing **athlete** and **organization** records in playbook.io —
without deleting and recreating them.
This feature improves data maintenance by allowing users to correct or update details such as contact information, age, and email, while keeping unique identifiers intact.

Contracts **cannot be edited** due to the absence of a unique identity field (contracts are uniquely defined by multiple parameters such as athlete, sport, organization, and duration). Editing any of these would make it impossible to reliably identify the original contract. Thus, the `edit-c` command is intentionally **unsupported**.

---

#### Parameters

##### For `edit-a` (Athlete)

| Parameter | Description                                                                                                                                                                                        |
|------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `n/NAME`   | Compulsory — Full name of the athlete (spaces allowed, case-insensitive; accepts alphabetic characters, hyphens, and apostrophes; must start with a letter; maximum of 50 characters, including spaces). |
| `s/SPORT`  | Compulsory — Sport of the athlete (spaces allowed, case-insensitive; alphabetic characters only; maximum of 50 characters).                                                                        |
| `a/AGE`    | Optional — Age of the athlete (positive integers only, ranging from 1 to 99).                                                                                                                      |
| `p/PHONE`  | Optional — Phone number of the athlete (8-digit Singapore phone number only; must start with 6, 8, or 9).                                                                                          |
| `e/EMAIL`  | Optional — Email address of the athlete (case-insensitive; must follow standard email format; maximum of 50 characters).                                                                           |

##### For `edit-o` (Organization)

| Parameter | Description |
|------------|-------------|
| `o/ORG_NAME` | Compulsory — Name of the organization (spaces allowed, case-insensitive; accepts alphanumeric characters, hyphens, apostrophes, and ampersands; must start with an alphanumeric character; maximum of 50 characters, including spaces). |
| `p/PHONE`  | Optional — Phone number of the organization (8-digit Singapore phone number only; must start with 6, 8, or 9). |
| `e/EMAIL`  | Optional — Email address of the organization (case-insensitive; must follow standard email format; maximum of 50 characters). |

Unspecified fields remain unchanged, and identifier fields (e.g., `NAME`, `SPORT`, or `ORG_NAME`) cannot be updated.  
The command requires at least one optional parameter. If none are provided, an error will be thrown.

#### Example usages

- `edit-a n/LeBron James s/Basketball p/98765432 e/lebron@newagency.com`
- `edit-o o/Nike p/91234567 e/partnerships@nikeglobal.com`

These commands:  
   •	Locate the target athlete or organization using their identifier fields (`NAME` + `SPORT` for athlete, `ORG_NAME` for organization)  
	•	Modify only the editable attributes  
	•	Validate and save the updated record via the Model  

<div style="page-break-before: always;"></div>

---
#### How it works

**Step 1.** The user executes `edit-a n/LeBron James s/Basketball e/lebron@newagency.com p/98765432` or
`edit-o o/Nike e/partners@nike.com p/91234567`.

**Step 2.** The parser constructs the corresponding command object:
- `EditAthleteCommand` for athletes, using `EditAthleteCommandParser`
- `EditOrganizationCommand` for organizations, using `EditOrganizationCommandParser`

Each parser builds an **Edit Descriptor**:
- `EditAthleteDescriptor` or `EditOrganizationDescriptor`
  which stores only the fields provided by the user (wrapped in `Optional<>`).

**Step 3.** The command locates the target record:
- `edit-a` finds the athlete using the pair `NAME` + `SPORT`
- `edit-o` finds the organization using `ORG_NAME`  
If no matching record exists, the command displays an error message and terminates.

**Step 4.** The command creates a new edited object:
- Copies all existing field values from the target
- Replaces only the editable fields provided in the descriptor
  (identifier fields — `NAME`, `SPORT` for athletes and `ORG_NAME` for organizations — remain unchanged)
- Validates all new field values using existing validators (`Email`, `Phone`, etc.)

**Step 5.** The command updates the model:
- Calls `model.setAthlete(targetAthlete, editedAthlete)`
  or `model.setOrganization(targetOrganization, editedOrganization)`
- The `Model` replaces the target entry with the new one in its internal list.

**Step 6.** The UI automatically refreshes:
- The `ObservableList` reflects the updated entity
- A success message is shown in the result display pane.

---

#### Object Diagram

The Object Diagram below illustrates how an **edit-a** command is executed.
The Class Diagram for this feature follows the same structure as the one for the *Add Athlete* command,
with corresponding modifications to support editable fields instead of creation.

<puml src="diagrams/EditObjectDiagram.puml" width="530" />

<div style="page-break-before: always;"></div>

#### Command format

> **Note:** Parameters enclosed in square brackets `[ ]` are optional.

**1. Edit Athlete**

```
edit-a n/NAME s/SPORT [p/PHONE] [e/EMAIL] [a/AGE]
```

- `n/` and `s/` together locate the athlete.
- Only non-identifier fields (e.g., `p/`, `e/`, `a/`) are editable.
- Example:
  ```
  edit-a n/LeBron James s/Basketball p/92345678 e/lebron@updated.com
  ```

**2. Edit Organization**

```
edit-o o/ORG_NAME [p/PHONE] [e/EMAIL]
```

- `o/` identifies the organization.
- Only `p/`, `e/` are editable.
- Example:
  ```
  edit-o o/Nike e/partners@nike.com
  ```
---

#### Design considerations

**Aspect: Identifier immutability**

- **Alternative 1 (current choice):** Prevent editing identifier fields (`NAME` and `SPORT` for athletes, `ORG_NAME` for organizations).
  - **Pros:** Ensures data integrity and avoids ambiguous lookups and duplicate keys.
  - **Cons:** Requires deletion and re-creation if identifiers need to change.

- **Alternative 2:** Allow editing identifiers with additional checks.
  - **Pros:** Flexible for rare rename cases.
  - **Cons:** Increases complexity and risk of identity collisions or inconsistent references.

**Aspect: Contract immutability**

- **Rationale:**
  Each contract is identified by a combination of attributes — athlete, sport, organization, start date, end date, and amount — rather than a single unique identity.
  Editing any of these would make the original contract indistinguishable, so contract editing is disabled by design.

- **Alternative approach (future consideration):**
  Introduce a unique `contractId` field to allow safe contract editing later.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

<div style="page-break-before: always;"></div>

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* sports agents who manage multiple athletes and their careers
* need to stay organized with many organizations (teams, sponsors, brands)
* prefer structured tools over manual spreadsheets or scattered files
* prefer desktop apps over other types
* can type fast
* prefer typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: The ultimate platform that empowers sports agents to stay organized, build stronger
relationships, and drive success for their athletes and partners.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​      | I want to …​                                                                                 | So that I can…​                                                      |
|----------|--------------|----------------------------------------------------------------------------------------------|----------------------------------------------------------------------|
| `* * *`  | new user     | see usage instructions                                                                       | refer to instructions when I forget how to use the App               |
| `* * *`  | sports agent | create a new athlete profile                                                                 | keep track of the athletes I currently represent                     |
| `* * *`  | sports agent | delete an athlete profile                                                                    | remove athletes I no longer represent                                |
| `* * *`  | sports agent | create a new organization                                                                    | store details of teams, sponsors, or brands I work with              |
| `* * *`  | sports agent | delete an organization                                                                       | remove organizations that are no longer relevant                     |
| `* * *`  | sports agent | upload a new contract                                                                        | track my athletes’ active agreements                                 |
| `* * *`  | sports agent | delete a contract                                                                            | keep my records up to date                                           |
| `* *`    | sports agent | search for an athlete, organization, or contract                                             | quickly find the information I need without scrolling                |
| `* *`    | sports agent | edit an athlete's profile information                                                        | update their details as their career progresses                      |
| `* *`    | sports agent | edit an organization's information                                                           | keep organization details current and accurate                       |
| `* *`    | sports agent | edit a contract's information                                                                | keep contract details current and accurate                           |
| `* *`    | sports agent | sort contracts by amount                                                                     | identify the most valuable deals quickly                             |
| `* *`    | sports agent | export contract data                                                                         | share reports with clients or use in other applications              |
| `*`      | sports agent | view analytics on contracts & deals                                                          | gain insights into performance and opportunities                     |
| `*`      | sports agent | link travel plans to competition and appearance schedules                                    | coordinate logistics efficiently and avoid scheduling conflicts      |
| `*`      | sports agent | manage and sync my athletes’ competition schedules and training events in a central calendar | coordinate effectively with teams, sponsors, and travel arrangements |
| `*`      | sports agent | set reminders for contract renewal dates                                                     | never miss important negotiation windows                             |
| `*`      | sports agent | track payment schedules within contracts                                                     | ensure timely payments and cash flow management                      |
| `*`      | sports agent | compare multiple contracts side by side                                                      | make informed decisions during negotiations                          |
| `*`      | sports agent | attach documents to contracts                                                                | keep all related paperwork in one place                              |
| `*`      | sports agent | receive notifications for contract milestones                                                | stay informed of important contract events automatically             |
| `*`      | sports agent | track commission earned from each contract                                                   | manage my own finances and business performance                      |

<div style="page-break-before: always;"></div>

### Use cases

(For all use cases below, the **System** is the `playbook.io` and the **Actor** is the `user`, unless specified
otherwise)

**Use case: UC01 - Add Athlete Profile**

**MSS**

1. Agent requests to add athlete
2. playbook.io creates and stores the new athlete profile

   Use case ends.

**Extensions**

* 1a. The given command is invalid
    * 1a1. playbook.io shows an error message

      Use case ends.

* 1b. Missing required parameter

    * 1b1. playbook.io shows an error message

      Use case ends.

* 1c. The given parameter is invalid

    * 1c1. playbook.io shows an error message

      Use case ends.

* 2a. Duplicate athlete

    * 2a1. playbook.io shows an error message

      Use case ends.

<div style="page-break-before: always;"></div>

**Use case: UC02 - Delete Athlete Profile**

**MSS**

1. Agent requests to delete athlete
2. playbook.io deletes the athlete profile

   Use case ends.

**Extensions**

* 1a. The given command is invalid
    * 1a1. playbook.io shows an error message

      Use case ends.

* 1b. Missing required parameter

    * 1b1. playbook.io shows an error message

      Use case ends.

* 1c. The given parameter is invalid

    * 1c1. playbook.io shows an error message

      Use case ends.

* 2a. No athlete found

    * 2a1. playbook.io shows an error message

      Use case ends.

* 2b. Athlete has active contracts

    * 2b1. playbook.io shows an error message

      Use case ends.

<div style="page-break-before: always;"></div>

**Use case: UC03 - Add Organization Profile**

**MSS**

1. Agent requests to add organization
2. playbook.io creates and stores the new organization profile

   Use case ends.

**Extensions**

* 1a. The given command is invalid
    * 1a1. playbook.io shows an error message

      Use case ends.

* 1b. Missing required parameter
    * 1b1. playbook.io shows an error message

      Use case ends.

* 1c. The given parameter is invalid

    * 1c1. playbook.io shows an error message

      Use case ends.

* 2a Duplicate organization

    * 2a1. playbook.io shows an error message

      Use case ends.

<div style="page-break-before: always;"></div>

**Use case: UC04 - Delete Organization**

**MSS**

1. Agent requests to delete an organization
2. playbook.io deletes the organization profile

   Use case ends.

**Extensions**

* 1a. The given command is invalid
    * 1a1. playbook.io shows an error message

      Use case ends.

* 1b. Missing required parameter

    * 1b1. playbook.io shows an error message

      Use case ends.

* 1c. The given parameter is invalid

    * 1c1. playbook.io shows an error message

      Use case ends.

* 2a. No organization found

    * 2a1. playbook.io shows an error message

      Use case ends.

* 2b. Organization has active contracts

    * 2b1. playbook.io shows an error message

      Use case ends.

<div style="page-break-before: always;"></div>

**Use case: UC05 - Add Contract**

**MSS**

1. Agent requests to add contract
2. playbook.io creates and stores the new contract

   Use case ends.

**Extensions**

* 1a. The given command is invalid
    * 1a1. playbook.io shows an error message

      Use case ends.

* 1b. Missing required parameter

    * 1b1. playbook.io shows an error message

      Use case ends.

* 1c. The given parameter is invalid

    * 1c1. playbook.io shows an error message

      Use case ends.

* 2a. Athlete does not exist

    * 2a1. playbook.io shows an error message

      Use case ends.

* 2b. Organization does not exist
    * 2b1. playbook.io shows an error message

      Use case ends.
  
* 2c. Athlete's total contract amount exceeds 9,223,372,036,854,775,807 
    * 2c1. playbook.io shows an error message

      Use case ends.
  
* 2d. Organization's total contract amount exceeds 9,223,372,036,854,775,807
   * 2d1. playbook.io shows an error message

      Use case ends.
  
* 2e. Both athlete's and organization's total contract amount exceeds 9,223,372,036,854,775,807
    * 2e1. playbook.io shows an error message

      Use case ends.
  
* 2f. Duplicate contract

    * 2f1. playbook.io shows an error message

      Use case ends.

<div style="page-break-before: always;"></div>

**Use case: UC06 - Delete Contract**

**MSS**

1. Agent requests to delete a contract
2. playbook.io deletes the contract

   Use case ends.

**Extensions**

* 1a. The given command is invalid
    * 1a1. playbook.io shows an error message

      Use case ends.

* 1b. Missing required parameter

    * 1b1. playbook.io shows an error message

      Use case ends.

* 1c. The given parameter is invalid

    * 1c1. playbook.io shows an error message

      Use case ends.

* 2a. No contract found

    * 2a1. playbook.io shows an error message

      Use case ends.

<div style="page-break-before: always;"></div>

**Use case: UC07 - Find Athlete**

**MSS**

1. Agent requests to find an athlete, specifying either the athlete’s name or sport as a parameter
2. playbook.io returns the list of athletes

   Use case ends.

**Extensions**

* 1a. The given command is invalid
    * 1a1. playbook.io shows an error message

      Use case ends.

* 1b. Missing required parameter

    * 1b1. playbook.io shows an error message

      Use case ends.

* 1c. The given parameter is invalid

    * 1c1. playbook.io shows an error message

      Use case ends.

* 2a. No athlete found

    * 2a1. playbook.io returns an empty list

      Use case ends.
  
<div style="page-break-before: always;"></div>

**Use case: UC08 - Find Organization**

**MSS**

1. Agent requests to find an organization, , specifying the organization’s name
2. playbook.io returns the list of organization

   Use case ends.

**Extensions**

* 1a. The given command is invalid
    * 1a1. playbook.io shows an error message

      Use case ends.

* 1b. Missing required parameter

    * 1b1. playbook.io shows an error message

      Use case ends.

* 1c. The given parameter is invalid

    * 1c1. playbook.io shows an error message

      Use case ends.

* 2a. No organization found

    * 2a1. playbook.io returns an empty list

      Use case ends.

<div style="page-break-before: always;"></div>

**Use case: UC09 - Find Contract**

**MSS**

1. Agent requests to find a contract, specifying either the athlete’s name, sport, or organization’s name as a parameter
2. playbook.io returns the list of contracts

   Use case ends.

**Extensions**

* 1a. The given command is invalid
    * 1a1. playbook.io shows an error message

      Use case ends.

* 1b. Missing required parameter

    * 1b1. playbook.io shows an error message

      Use case ends.

* 1c. The given parameter is invalid

    * 1c1. playbook.io shows an error message

      Use case ends.

* 2a. No contract found

    * 2a1. playbook.io returns an empty list

      Use case ends.

<div style="page-break-before: always;"></div>

**Use case: UC10 - Refresh Data**

**MSS**

1. Agent requests to refresh to clear any active search filters
2. playbook.io returns the latest lists of athletes, organizations, and contracts.

   Use case ends.

**Extensions**

* 1a. The given command is invalid
    * 1a1. playbook.io shows an error message

      Use case ends.

**Use case: UC11 - View Help Information**

**MSS**

1. Agent requests to open the help window
2. playbook.io opens the help window

   Use case ends.

**Extensions**

* 1a. The given command is invalid
    * 1a1. playbook.io shows an error message

      Use case ends.

**Use case: UC12 - Close Session**

**MSS**

1. Agent requests to exit the system
2. playbook.io closes the session

   Use case ends.

**Extensions**

* 1a. The given command is invalid
    * 1a1. playbook.io shows an error message

      Use case ends.

<div style="page-break-before: always;"></div>

### Non-functional requirements

1. Should work on any _mainstream OS_ as long as it has Java `17` installed.
2. Should be able to hold up to 1000 athletes, organizations, and contracts while maintaining an average response time
   of under 2 seconds for common operations (e.g., adding, deleting, or searching) under typical usage conditions.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands), typically
   around 50–60 words per minute (WPM), should be able to accomplish most of the tasks faster using commands than using
   the mouse.
4. Should validate all input data (e.g., names, emails, dates, amounts) and provide clear error messages when invalid
   input is detected.
5. Should allow the application to be packaged and distributed in a portable format (e.g., JAR or Docker container) for
   ease of deployment across environments.
6. Should allow a new user to perform basic operations (e.g., adding, deleting and finding athletes, organizations,
   and contracts) within 10 minutes of using the application, by following the provided user guide.
7. Should provide consistent response times (<2 seconds) for retrieval commands such as searching athletes,
   organizations, or contracts under normal usage load.
8. Should prevent duplicate records by enforcing unique key constraints (e.g., same athlete name + sport).

### Glossary

* **Athlete**: An individual sports performer managed by the agent, with contact details and sport specialization.
* **Command Line Interface**: A text-based interface that allows users to interact with the application by typing commands.
* **Contract**: A business agreement between an athlete and organization, including financial terms and duration.
* **Fuzzy Matching**: A search method that finds results even with typos or partial matches, using intelligent algorithms.
* **Graphical User Interface**: visual interface that allows users to interact with the application through graphical elements such as buttons, menus, and icons.
* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Organization**: Any business entity that contracts with athletes - teams, sponsors, agencies, brands, etc.
* **Sports Agent**: A professional who represents athletes in contract negotiations and career management.
* **User Interface**: The means by which users interact with the application, encompassing all interaction methods including graphical elements, text-based commands, and input mechanisms.

--------------------------------------------------------------------------------------------------------------------

## Appendix: Instructions for manual testing

Given below are instructions to test the app manually.

> **Note:** These instructions only provide a starting point for testers to work on;
> testers are expected to do more *exploratory* testing.

### Launch and shutdown

#### 1. Initial launch

1. Download the jar file and copy into an empty folder.
2. Open a terminal, navigate (`cd`) to that folder, and run:

   ```
   java -jar "[CS2103T-F13-3][playbookio].jar"
   ```

**Expected:**
Shows the GUI. The window size may not be optimum.

#### 2. Saving window preferences

1. Resize the window to an optimum size. Move the window to a different location. Close the window.
2. Re-launch the app by running the same command in the terminal:
```
   java -jar "[CS2103T-F13-3][playbookio].jar"
```

**Expected:**
The most recent window size and location is retained.

<div style="page-break-before: always;"></div>

### Adding an athlete

#### 1. Adding an athlete while all athletes are being shown

1. **Prerequisites:** Switch to the Athletes Tab by pressing **Cmd+1** (or **Ctrl+1** on Windows/Linux).
2. **Test case:** `add-a n/Lebron James s/Basketball a/40 p/99876543 e/James@example.com`  
   **Expected:** Athlete is added to the athlete list. Details of the added athlete shown in the result pane.
3. **Test case:** `add-a n/ s/Football a/39 p/87654321 e/cr7@example.com`  
   **Expected:** No athlete is added. Error details shown in the result pane.
4. **Other incorrect add-a commands to try:** `add-a`, `add-a n/Messi2 s/Football a/39 p/87654321 e/cr7@example.com`,
   `...`  
   **Expected:** Similar to previous.

### Deleting an athlete

#### 1. Deleting an athlete while all athletes are being shown

1. **Prerequisites:**
    - Switch to the Athletes Tab by pressing **Cmd+1** (or **Ctrl+1** on Windows/Linux).
    - Ensure the athlete to be deleted has no existing contracts.
2. **Test case:** `delete-a n/Lebron James s/Basketball`  
   **Expected:** Athlete is deleted from the list. Details of the deleted athlete shown in the result pane.
3. **Test case:** `delete-a n/Lebron James s/`  
   **Expected:** No athlete is deleted. Error details shown in the result pane.
4. **Other incorrect delete-a commands to try:** `delete-a`, `delete-a n/Lebron James s/Basket-ball`, `...`  
   **Expected:** Similar to previous.

### Adding an organization

#### 1. Adding an organization while all organizations are being shown

1. **Prerequisites:** Switch to the Organizations Tab by pressing **Cmd+2** (or **Ctrl+2** on Windows/Linux).
2. **Test case:** `add-o o/Nike p/98765432 e/partnerships@nike.com`  
   **Expected:** Organization is added to the organization list. Details of the added organization shown in the result
   pane.
3. **Test case:** `add-o o/Nike p/+6598765432 e/partnerships@nike.com`  
   **Expected:** No organization is added. Error details shown in the result pane.
4. **Other incorrect add-o commands to try:** `add-o`, `add-o o/&Nike p/98765432 e/partnerships@nike.com`, `...`  
   **Expected:** Similar to previous.

### Deleting an organization

#### 1. Deleting an organization while all organizations are being shown

1. **Prerequisites:**
    - Switch to the Organizations Tab by pressing **Cmd+2** (or **Ctrl+2** on Windows/Linux).
    - Ensure organization to be deleted has no existing contracts.
2. **Test case:** `delete-o o/Nike`  
   **Expected:** Organization is deleted from the list. Details of the deleted organization shown in the result pane.
3. **Test case:** `delete-o o/`  
   **Expected:** No organization is deleted. Error details shown in the result pane.
4. **Other incorrect delete-o commands to try:** `delete-o`, `delete-o o/Nike!`, `...`  
   **Expected:** Similar to previous.

### Adding a contract

#### 1. Adding a contract while all contracts are being shown

1. **Prerequisites:**
    - Switch to the Contracts Tab by pressing **Cmd+3** (or **Ctrl+3** on Windows/Linux).
    - Ensure the athlete and organization exist in the system.
2. **Test case:** `add-c n/LeBron James s/Basketball o/Nike sd/01012024 ed/01012025 am/50000000`  
   **Expected:** Contract is added to the contracts list. Details of the added contract shown in the result pane.
3. **Test case:** `add-c n/LeBron James s/Basketball o/Nike sd/01012024 ed/01012025 am/`  
   **Expected:** No contract is added. Error details shown in the result pane.
4. **Other incorrect add-c commands to try:** `add-c`,
   `add-c n/LeBron James s/Basketball o/Nike sd/01012024 ed/01012025 am/500.99`, `...`  
   **Expected:** Similar to previous.

<div style="page-break-before: always;"></div>

### Deleting a contract

#### 1. Deleting a contract while all contracts are being shown

1. **Prerequisites:** Switch to the Contracts Tab by pressing **Cmd+3** (or **Ctrl+3** on Windows/Linux).
2. **Test case:** `delete-c n/LeBron James s/Basketball o/Nike sd/01012024 ed/01012025 am/50000000`  
   **Expected:** Contract is deleted from the list. Details of the deleted contract shown in the result pane.
3. **Test case:** `delete-c n/LeBron James s/Basketball o/Nike sd/01012024 ed/ am/50000000`  
   **Expected:** No contract is deleted. Error details shown in the result pane.
4. **Other incorrect delete-c commands to try:** `delete-c`,
   `delete-c n/ s/Basketball o/Nike sd/01012024 ed/01012025 am/50000000`, `...`  
   **Expected:** Similar to previous.

### Finding an athlete

#### 1. Finding an athlete while all athletes are being shown

1. **Prerequisites:** Switch to the Athletes Tab by pressing **Cmd+1** (or **Ctrl+1** on Windows/Linux).
2. **Test case:** `find an/LeBron James`  
   **Expected:** Filtered list of athletes shown. Details of the filtered list shown in the result pane.
3. **Test case:** `find as/Basketball`  
   **Expected:** Filtered list of athletes shown. Details of the filtered list shown in the result pane.
4. **Test case:** `find as/`  
   **Expected:** No filtering occurs. Error details shown in the result pane.
5. **Other incorrect find commands to try:** `find an/`, `find`, `...`  
   **Expected:** Similar to previous.

### Finding an organization

#### 1. Finding an organization while all organizations are being shown

1. **Prerequisites:** Switch to the Organizations Tab by pressing **Cmd+2** (or **Ctrl+2** on Windows/Linux).
2. **Test case:** `find on/Nike`  
   **Expected:** Filtered list of organizations shown. Details of the filtered list shown in the result pane.
3. **Test case:** `find on/`  
   **Expected:** No filtering occurs. Error details shown in the result pane.
4. **Other incorrect find commands to try:** `find`, `...`  
   **Expected:** Similar to previous.

### Finding a contract

#### 1. Finding a contract while all contracts are being shown

1. **Prerequisites:** Switch to the Contracts Tab by pressing **Cmd+3** (or **Ctrl+3** on Windows/Linux).
2. **Test case:** `find ca/LeBron James`  
   **Expected:** Filtered list of contracts shown. Details of the filtered list shown in the result pane.
3. **Test case:** `find cs/Basketball`  
   **Expected:** Filtered list of contracts shown. Details of the filtered list shown in the result pane.
4. **Test case:** `find co/Nike`  
   **Expected:** Filtered list of contracts shown. Details of the filtered list shown in the result pane.
5. **Test case:** `find co/`  
   **Expected:** No filtering occurs. Error details shown in the result pane.
6. **Other incorrect find commands to try:** `find cs/`, `find ca/`, `...`  
   **Expected:** Similar to previous.

<div style="page-break-before: always;"></div>

### Saving data

#### 1. Dealing with missing/corrupted data files

##### 1.1 Simulate a missing file

1. Close playbook.io.
2. Navigate to the `data` folder.
3. Delete one or more JSON files (e.g., `athletelist.json`, `contractlist.json`, `organizationlist.json`).
4. Re-launch playbook.io.

**Expected:**

- If `contractlist.json` is not empty but one or more of the other files are missing, all JSON files are re-generated as
  empty lists.
- Otherwise, only the missing files are automatically generated as empty lists.

##### 1.2 Simulate a corrupted file

1. Open a JSON file (e.g., `athletelist.json`).
2. Add invalid content (e.g., remove a closing bracket or insert invalid characters).
3. Save the file and relaunch playbook.io.

**Expected:**
The app detects that the files are corrupted and loads empty lists for all entities.

> **⚠️ Important:**
> New JSON files are only created once data is written (e.g., after adding an athlete, contract, or organization).

#### 2. Normal save

##### 2.1 Simulate saving data after adding entries

1. Open playbook.io.
2. Add an athlete, contract, or organization
3. Close playbook.io.

**Expected:**
Data is persisted correctly in the JSON files.

--------------------------------------------------------------------------------------------------------------------

## Appendix: Effort

### Difficulty level

playbook.io is more complex than the original AddressBook Level 3 (AB3). While AB3 only manages one type of thing (Persons), our app handles **three different types**—Athletes, Organizations, and Contracts—each with their own details and connections to each other. This made the project much more challenging:

---

### Challenges faced

#### 1. Linking entities together

One of the hardest parts was making sure Athletes, Organizations, and Contracts stayed properly connected:

- **Deleting things**: When you try to delete an organization, what happens to its athletes and contracts? We had to handle this carefully.
- **Checking references**: When adding a contract, we need to make sure the athlete and organization actually exist.
- **Error messages**: When something goes wrong with these connections, we need to tell the user what happened clearly.

#### 2. Expanding the command system

Making commands work for three different types instead of one was tricky:

- **Avoiding copy-paste**: We didn't want to write the same code three times, so we had to find smart ways to reuse logic.
- **Different inputs**: Each entity type has different information, so the command parsers had to handle this.
- **Clear commands**: We needed command names that made sense and didn't confuse users.

<div style="page-break-before: always;"></div>

#### 3. Making the UI work

Showing three different types of lists in the interface was challenging:

- **Switching views**: Users need to easily switch between Athletes, Organizations, and Contracts.
- **Keeping things in sync**: The UI needs to always show the right information when you switch or make changes.
- **Smooth performance**: The app should still run fast even when displaying lots of data.

#### 4. Consistent error messages

We wanted all error messages to look and feel the same:

- **Standard format**: Every command should report errors in a similar way.
- **Code reviews**: We checked each other's code multiple times to make sure messages were consistent.
- **Documentation**: We made sure the error messages in the code matched what we wrote in the user guide.

---

### Effort required

The project took about **40-50% more work** than the baseline AB3:

| What We Did | How Much Effort |
|-------------|-----------------|
| Basic AB3 stuff | ~100% |
| Adding three entity types | +25% |
| Testing and fixing bugs | +15% |
| Documentation | +10% |
| **Total** | **~140-150%** |

We spent a lot of time on:
- Finding and fixing bugs when different parts of the code interacted
- Cleaning up code to keep it organized as things got more complex
- Writing tests for all the new features and how they work together
- Writing clear documentation for users and developers

---

### What we accomplished

#### Features that work
- You can add, view, and delete all three types (Athletes, Organizations, Contracts)
- You can search for any entity using different fields
- Entities are properly linked and validated

#### Good code quality
- Over 70% of our code is tested with automated tests
- Code is well-organized and easy to understand
- All commands give clear, consistent error messages

#### Good documentation
- User Guide explains how to use every command with examples
- Developer Guide explains how the code works with diagrams
- FAQ section answers common questions

<div style="page-break-before: always;"></div>

#### Good teamwork
- Everyone knew what they were responsible for
- We reviewed each other's code before merging
- We communicated regularly through meetings and updates

---

### What we reused from AB3

About **15-20% less work** thanks to reusing parts of AB3:

#### Code structure (~10% saved)
- **Commands**: We adapted AB3's command structure instead of starting from scratch
- **Data management**: We extended AB3's existing classes instead of rewriting everything
- **Storage**: We used AB3's file saving system and just added support for new entity types

#### User interface (~5% saved)
- **Lists**: We adapted AB3's list display for our three entity types
- **Cards**: We modified AB3's card design to show different information
- **Window layout**: We kept AB3's basic window structure

---

### What we learned

- **Plan first**: Spending time designing how entities connect saved us from rewriting code later
- **Build step by step**: Adding one entity type at a time helped us catch problems early
- **Test while coding**: Writing tests as we went found bugs faster
- **Document as you go**: Writing documentation while coding was easier than doing it all at the end


--------------------------------------------------------------------------------------------------------------------

## Appendix: Planned enhancements

**Team size:** 5

### 1. Streamline the editing workflow for existing entities

**Current issue:** To edit an entity's details, users must currently delete the entire entity and then re-add it with corrected information. This two-step process is cumbersome and risks data loss if the user forgets details from the original entry. The workflow is also unintuitive for users who expect a direct edit capability.

**Planned enhancement:** We plan to streamline the editing workflow by consolidating the delete-and-add process into a single, more intuitive operation. This enhancement will improve the existing edit capability by making it more user-friendly, reducing the risk of accidental data loss, and providing clearer feedback during the modification process.

---

### 2. Allow more flexible phone number formats for international support

**Current issue:** The current system is localized to Singapore and only accepts 8-digit phone numbers without spaces, country codes, or symbols (e.g., `91234567`). This restriction limits scalability and prevents international users or organizations from being added.

**Planned enhancement:** As we plan to expand playbook.io for international use, we will enhance the phone number validation logic to support a wider range of formats. The updated validation will accept optional country codes (e.g., `+65`, `+1`), spaces, and hyphens while still rejecting invalid characters.

<div style="page-break-before: always;"></div>

---

### 3. Increase character limits for text fields

**Current issue:** The current system restricts name, organization name, email, and sport fields to a fixed maximum length (e.g., 50 characters) due to UI display limitations. Longer names or organization titles may be truncated or misaligned in the interface, so the restriction was added to maintain readability and prevent layout issues.

**Planned enhancement:** In future iterations, we plan to enhance the UI to handle longer text inputs gracefully. Once the UI supports dynamic text wrapping and resizing, we will allow longer inputs for names, organization names, emails, and sports. This will better accommodate users with longer names or international organizations without causing UI overflow problems.

---

### 4. Enhance find command to support multiple flags and flexible search variations

**Current issue:** The current `find` command only supports searching by one flag at a time (e.g., `find an/James`). Users cannot combine multiple criteria (e.g., name and sport), and search matching is limited to exact or partial word matches. This makes filtering less efficient for large datasets.

**Planned enhancement:** We plan to enhance the `find` command to support multiple flags and more flexible search variations. Users will be able to search using a combination of attributes (e.g., name, sport, organization) within a single command. The command parser will be updated to handle multiple flag-value pairs and perform case-insensitive, partial, and multi-word matching.

---

### 5. Allow more variations and special characters in athlete and organization names

**Current issue:** Currently, the system restricts certain characters such as `/` in athlete and organization names. This limitation exists because these characters are used internally to detect command parameters, which can cause parsing errors or incorrect field detection. Additionally, the system only accepts ASCII characters, preventing users from entering names with accents, diacritics, or non-Latin scripts (e.g., "José García", "李明", "François Müller"). This limits the system's usability for international athletes and organizations.

**Planned enhancement:** We plan to refine the command parsing logic to properly handle special characters and non-ASCII characters in names while still correctly identifying prefixes. This will involve improving input tokenization and validation to distinguish between actual command prefixes (e.g., `n/`, `p/`) and similar characters within user-provided text, as well as expanding character encoding support to accept Unicode characters for international names.

---

### 6. Support more flexible date formats

**Current issue:** Currently, the system only accepts dates in the strict `DDMMYYYY` format with no spaces, hyphens, or slashes (e.g., `01012024`). This limitation can be unintuitive for users accustomed to different date formats and increases the likelihood of input errors when entering contract dates.

**Planned enhancement:** We plan to enhance the date validation logic to accept more flexible date formats. The updated system will support common variations such as `DD-MM-YYYY`, `DD/MM/YYYY`, and `DD MM YYYY` (e.g., `01-01-2024`, `01/01/2024`, `01 01 2024`). This will improve user experience by accommodating different date entry preferences while maintaining proper validation.

---

### 7. Implement realistic date range validation

**Current issue:** Currently, the system allows contracts to start and end at any date without validation against logical constraints. This means contracts can be created with start dates before an athlete's birth year, or with impossibly long durations spanning decades. Such invalid data entries compromise the integrity of the contract database and can lead to meaningless or erroneous records.

**Planned enhancement:** We plan to implement comprehensive date range validation to ensure all contract dates are realistic and logically sound. The updated system will validate that contract start dates do not precede the athlete's birth year, and that contract durations fall within reasonable bounds for professional sports agreements. This will prevent the creation of logically impossible contracts while providing clear feedback to users when invalid date ranges are entered.

---
