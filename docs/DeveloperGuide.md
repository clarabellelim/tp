---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# HealthSync Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

_{ list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well }_

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The **Architecture Diagram** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />


Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI Component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java).

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

#### Overview

The `MainWindow` serves as the primary container for all UI components. It is responsible for orchestrating interactions between different UI elements and ensuring a seamless user experience. Its layout is defined in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml), which specifies the structure and arrangement of UI elements using JavaFX's XML-based markup.

The UI consists of a `MainWindow`, which is composed of multiple UI parts, such as:
The UI consists of a `MainWindow`, which is composed of multiple UI parts, such as:

- `CommandBox` – Handles user input.
- `ResultDisplay` – Displays command execution results.
- `PersonListPanel` – Controls the list of people displayed on the left side.
- `PersonCard` – Represents a summarized view of a person in the `PersonListPanel`.
- `PersonDetailPanel` – Displays detailed attributes of the selected person on the right side.
- `PersonDetail` – Manages which details appear in the `PersonDetailPanel`.
- `StatusBarFooter` – Displays application status details.

All UI components, including `MainWindow`, inherit from the abstract `UiPart` class, which encapsulates common UI functionalities.

The `UI` component uses the **JavaFX UI framework**, and its layout is defined in `.fxml` files located in `src/main/resources/view`. For example, the layout of `MainWindow` is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml).

#### Responsibilities of the UI Component
The `UI` component:
- Executes user commands by interacting with the `Logic` component.
- Listens for changes in `Model` data and updates the UI accordingly.
- Maintains a reference to the `Logic` component, as the UI depends on `Logic` to process user actions.
- Displays `Person` objects retrieved from the `Model` component.

#### Layout & Functionality

- **Main Window (`MainWindow.fxml`)**: The central UI container, which defines the overall structure of the application. It organizes different UI elements and ensures smooth interaction between them.
    - `MainWindow` initializes and manages key UI components like `CommandBox`, `ResultDisplay`, `PersonListPanel`, and `PersonDetailPanel`.
    - It listens to user input and updates the display accordingly.

- **Left Panel:** `PersonListPanel` determines which people appear in the list, and each person’s summary is represented by a `PersonCard`.
- **Right Panel:** `PersonDetailPanel` displays a detailed view of the selected person, with `PersonDetail` managing which fields are shown.

This structure provides a **clear separation of concerns**, making it easier to extend and modify the UI while keeping the codebase maintainable.



### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**: \
Healthcare administrators in clinics

**Value proposition**: \
HealthSync allows healthcare staff to efficiently organize patient details and key contacts in one unified platform.
With quick access to updated information, administrators can easily connect with medical staff and patients' families, ensuring smooth communication and
prompt action, especially when managing recovery progress and treatment schedules.

### User Stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                    | I want to …​                                                   | So that I can…​                                                                                      |
|----------|--------------------------------------------|----------------------------------------------------------------|------------------------------------------------------------------------------------------------------|
| `* * *`  | healthcare administrator                   | quickly locate a patient's emergency contacts                  | promptly notify their next of kin in life-threatening medical situations                             |
| `* * *`  | healthcare administrator                   | accurately upload and store hospital patients' contact details | ensure their information is reliably available for communication and record-keeping                  |
| `* * *`  | healthcare professional                    | quickly identify a patient's medical conditions and allergies  | understand the patient’s condition and provide safe and effective care                               |
| `* * *`  | new user                                   | have sample data to work with                                  | understand how to use the application                                                                |
| `* * *`  | healthcare administrator                   | schedule an appointment for a patient                          | keep track of upcoming visits to the clinic                                                          |
| `* * *`  | healthcare administrator                   | efficiently update and manage patient schedules                | ensure that doctors' time is managed well and appointments do not overlap                            |
| `* * *`  | healthcare administrator                   | identify patient allergies using tags                          | prescribe medicines safely and avoid malpractice                                                     |
| `* * *`  | healthcare administrator                   | have an overview of all current patients                       | quickly access and manage active patient records                                                     |
| `* * *`  | healthcare administrator                   | add an insurance tag to a patient                              | document their insurance details for billing and verification purposes                               |
| `* * *`  | healthcare administrator                   | add a medical condition tag to a patient                       | keep a clear medical history for safe treatment and reference                                        |
| `* * *`  | healthcare administrator                   | add a new patient to the system                                | begin tracking their appointments, contact, and medical details                                      |
| `* * *`  | healthcare administrator                   | edit a patient's details                                       | keep their records accurate and up to date                                                           |
| `* * *`  | healthcare administrator                   | find a patient by name                                         | quickly bring up a specific patient’s record                                                         |
| `* * *`  | user                                       | access help at any time                                        | see available commands and how to use them                                                           |
| `* *`    | healthcare administrator                   | undo the last operation                                        | recover from accidental changes or deletions                                                         |
| `* *`    | healthcare administrator                   | redo the last undone operation                                 | reverse an undo if it was done by mistake                                                            |
| `* *`    | healthcare administrator                   | delete a specific tag from a patient                           | remove outdated or incorrect medical information                                                     |
| `*`      | user with many patients                    | sort patients by name                                          | locate a patient quickly and efficiently                                                             |
| `*`      | healthcare administrator                   | archive a patient who has not visited in years                 | keep the list of current patients clean and manageable                                               |
| `*`      | healthcare administrator                   | unarchive a patient from the archive list                      | update their information if they return to the clinic                                                |
| `*`      | healthcare administrator                   | view a list of all archived patients                           | easily access records of patients who are no longer active                                           |
| `*`      | healthcare administrator                   | clear all patient entries                                      | reset the system when starting fresh or during testing phases                                        |
| `*`      | healthcare administrator                   | delete a patient from the system                               | remove incorrect or duplicated records                                                               |
| `*`      | user                                       | exit the application                                           | close the programme when I’m done using it                                                           |

### Use Cases

(For all use cases below, the **System** is `HealthSync` and the **Actor** is the `Healthcare Administrator`, unless specified otherwise.)

---

#### **UC01 - View Help Window**

**Main Success Scenario (MSS):**
1. Healthcare Administrator chooses to view the help window.
2. HealthSync displays the link to the user guide.
3. Use case ends.

---

#### **UC02 - Add Patient**

**Main Success Scenario (MSS):**
1. Healthcare Administrator chooses to add a new patient.
2. HealthSync prompts the user to enter:
    - Patient's name.
    - Patient's phone number.
    - Patient's email address.
    - Patient's residential address.
3. Healthcare Administrator enters the requested details.
4. HealthSync successfully adds the patient.
5. Use case ends.

**Extensions:**
- **3a.** Healthcare Administrator enters incomplete or invalid details (e.g. phone number contains letters).
    - **3a1.** HealthSync informs the user of the errors.
    - Use case resumes from step **2**.
- **3b.** A patient with the same unique identifier (i.e. name and phone number, name and email address) already exists.
    - **3b1.** HealthSync notifies the user of the duplication.
    - **3b2.** Healthcare Administrator can choose to:
        -  **3b2a.** Update the existing record:
          - **3b2a1.** Transition to **UC02 - Edit Patient Details**.
        -  **3b2b.** Cancel the addition:
          - **3b2b1.** Use case ends.
        -  **3b2c.** Provide corrected details:
          - **3b2c1.** Use case resumes from step **2**.
- **4a.** Healthcare Administrator chooses to undo the command.
    - **4a1.** Transition to **UC09 - Undo Previous Command**.

---

#### **UC03 - Update Patient Schedule**

**Main Success Scenario (MSS):**
1. Healthcare Administrator requests to update a patient’s appointment.
2. HealthSync prompts the user to enter:
    - Patient index.
    - New or modified appointment details (i.e. appointment date, time).
3. Healthcare Administrator inputs the updated appointment.
4. HealthSync updates the patient’s appointment successfully.
5. Use case ends.

**Extensions:**
- **3a.** The given index is invalid (out of range or does not exist).
    - **3a1.** HealthSync informs the user of the invalid index.
    - Use case resumes from step **2**.
- **3b.** The entered appointment details are invalid (e.g., overlapping appointments, incorrect format).
    - **3b1.** HealthSync displays an error message.
    - **3b2.** Healthcare Administrator corrects the details and resubmits.
    - Use case resumes from step **2**.
- **4a.** Healthcare Administrator chooses to undo the command.
    - **4a1.** Transition to **UC09 - Undo Previous Command**.

---

#### **UC04 - List Patients**

**Main Success Scenario (MSS):**
1. Healthcare Administrator chooses to list all patients.
2. HealthSync displays the complete list of patients.
3. Use case ends.

---

#### **UC05 - Sort Patients by Name**

**Main Success Scenario (MSS):**
1. Healthcare Administrator requests to sort the list of patients by name.
2. HealthSync prompts the user to choose the sorting order:
    - Name (ascending alphabetical order).
    - Appointment (ascending lexicographical order).
3. Healthcare Administrator selects the preferred sorting order.
4. HealthSync sorts the patient list accordingly.
5. HealthSync displays the sorted patient list.
6. Use case ends.

**Extensions:**
- **3a.** The entered sorting order is invalid (i.e. not name or appointment).
    - **3a1.** HealthSync informs the user of the invalid sorting order.
    - Use case resumes from step **2**.
- **5a.** Healthcare Administrator chooses to undo the command.
    - **5a1.** Transition to **UC09 - Undo Previous Command**.

---

#### **UC06 - Edit Patient Details**

**Main Success Scenario (MSS):**
1. Healthcare Administrator requests to edit a patient’s details.
2. HealthSync prompts the user to enter the following details:
    - Index of the patient to be edited.
    - New information for the field to be updated.
3. Healthcare Administrator inputs the new information.
4. HealthSync asks for confirmation.
5. Healthcare Administrator confirms the edit.
6. HealthSync updates the patient’s details successfully.
7. Use case ends.

**Extensions:**
- **3a.** The given index is invalid (out of range).
    - **3a1.** HealthSync informs the user of the invalid index.
    - Use case resumes from step **2**.
- **3b.** The entered details are invalid (e.g. phone number contains letters).
    - **3b1.** HealthSync informs the user of the invalid details.
    - Use case resumes from step **2**.
- **4a.** Healthcare Administrator does not confirm the action.
    - **4a1.** HealthSync cancels the operation.
    - Use case ends.
- **4b.** Healthcare Administrator inputs an invalid response (not 'y' or 'n') when confirming.
    - **4b1.** HealthSync displays an error message indicating the invalid input.
    - **4b2.** Action is cancelled.
    - Use case ends.
- **6a.** Healthcare Administrator chooses to undo the command.
    - **6a1.** Transition to **UC09 - Undo Previous Command**.

---

#### **UC07 - Add an Emergency Contact**

**Main Success Scenario (MSS):**
1. Healthcare Administrator requests to add an emergency contact for a patient.
2. HealthSync prompts the user to enter:
    - Patient index.
    - Emergency contact’s name.
    - Emergency contact’s phone number.
    - Relationship to the patient.
3. Healthcare Administrator provides the details.
4. HealthSync adds the emergency contact to the patient’s record.
5. HealthSync confirms successful addition.
6. Use case ends.

**Extensions:**
- **3a.** The given index is invalid (out of range or does not exist).
    - **3a1.** HealthSync informs the user of the invalid index.
    - Use case resumes from step **2**.
- **3b.** The entered details are invalid (e.g. phone number contains letters).
    - **3b1.** HealthSync informs the user of the invalid details.
    - Use case resumes from step **2**.
- **5a.** Healthcare Administrator chooses to undo the command.
    - **5a1.** Transition to **UC09 - Undo Previous Command**.

---

#### **UC08 - Find Patients**

**Main Success Scenario (MSS):**
1. Healthcare Administrator chooses to find patients by name.
2. HealthSync prompts the Healthcare Administrator to enter keywords for the search.
3. Healthcare Administrator enters the keywords (case-insensitive).
4. HealthSync searches the patients' name and phone number for the given keywords.
5. HealthSync displays the list of patients whose details match at least one of the entered keywords.
6. Use case ends.

**Extensions:**
- **4a.** No patients match the given keywords.
    - **4a1.** HealthSync displays a message informing the user that 0 matches were found.
    - Use case ends.

---

#### **UC09 - Archive Patient**

**Main Success Scenario (MSS):**
1. Healthcare Administrator chooses to archive a patient.
2. HealthSync prompts the Healthcare Administrator to enter the index of the patient to be archived.
3. Healthcare Administrator provides the patient index.
4. HealthSync archives the patient’s record.
5. HealthSync confirms successful archiving.
6. Use case ends.

**Extensions:**
- **3a.** The given index is invalid (out of range or does not exist).
    - **3a1.** HealthSync informs the user of the invalid index.
    - Use case resumes from step **2**.
- **5a.** Healthcare Administrator chooses to undo the command.
    - **5a1.** Transition to **UC09 - Undo Previous Command**.

---

#### **UC10 - List Archived Patients**

**Main Success Scenario (MSS):**
1. Healthcare Administrator chooses to view the list of archived patients.
2. HealthSync retrieves and displays the list of archived patients.
3. HealthSync confirms successful display of list of archived patients.
4. Use case ends.

**Extensions:**
- **2a.** Healthcare Administrator tries to use any command other than `find`, `unarchive`, or `list` in archive mode.
    - **2a1.** HealthSync informs the user that the command cannot be executed in archive mode.
    - **2a2.** Use case ends.

---

#### **UC11 - Unarchive Patient**

**Main Success Scenario (MSS):**
1. Healthcare Administrator chooses to unarchive a patient.
2. HealthSync prompts the user to enter the index of the patient to be unarchived.
3. Healthcare Administrator provides the patient index.
4. HealthSync unarchives the patient’s record.
5. HealthSync confirms successful unarchiving.
6. Use case ends.

**Extensions:**
- **2a.** The given index is invalid (out of range or does not exist).
    - **2a1.** HealthSync informs the user of the invalid index.
    - Use case resumes from step **2**.

---

#### **UC12 - Delete a Patient**

**Main Success Scenario (MSS):**
1. Healthcare Administrator requests to delete a patient’s contact.
2. HealthSync prompts the user to enter the index of the patient to be deleted.
3. Healthcare Administrator provides the patient index.
4. HealthSync asks for confirmation.
5. Healthcare Administrator confirms the deletion.
6. HealthSync deletes the patient’s contact from the system.
7. HealthSync confirms successful deletion.
8. Use case ends.

**Extensions:**
- **3a.** The given index is invalid (out of range or does not exist).
    - **3a1.** HealthSync informs the user of the invalid index.
    - Use case resumes from step **2**.
- **4a.** Healthcare Administrator does not confirm the action.
    - **4a1.** HealthSync cancels the operation.
    - Use case ends.
- **4b.** Healthcare Administrator inputs an invalid response (not 'y' or 'n') when confirming.
    - **4b1.** HealthSync displays an error message indicating the invalid input.
    - **4b2.** Action is cancelled.
    - Use case ends.
- **7a.** Healthcare Administrator chooses to undo the command.
    - **7a1.** Transition to **UC09 - Undo Previous Command**.

---

#### **UC13 - Clear Patient List**

**Main Success Scenario (MSS):**
1. Healthcare Administrator chooses to clear the entire patient list.
2. HealthSync asks for confirmation.
3. Healthcare Administrator confirms the edit.
4. HealthSync clears all patients from the system.
5. HealthSync confirms the list has been cleared.
6. Use case ends.

**Extensions:**
- **3a.** Healthcare Administrator does not confirm the action.
    - **3a1.** HealthSync cancels the operation.
    - Use case ends.
- **3b.** Healthcare Administrator inputs an invalid response (not 'y' or 'n') when confirming.
    - **3b1.** HealthSync displays an error message indicating the invalid input.
    - **3b3.** Action is cancelled.
    - Use case ends.
- **5a.** Healthcare Administrator chooses to undo the command.
    - **5a1.** Transition to **UC09 - Undo Previous Command**.

---

#### **UC14 - Add Tag to Patient**

**Main Success Scenario (MSS):**
1. Healthcare Administrator chooses to add tag(s) to a patient.
2. HealthSync prompts the user to enter:
    - Patient index.
    - Tag(s) to add.
3. Healthcare Administrator enters the index and the tag(s).
4. HealthSync validates the index and tags.
5. HealthSync adds the valid tags to the patient and displays the updated tag list.
6. Use case ends.

**Extensions:**
- **3a.** The given index is invalid (out of range or does not exist).
    - **3a1.** HealthSync informs the user of the invalid index.
    - Use case resumes from step **2**.
- **3b.** The tag(s) input is invalid (e.g. tag name is not alphanumeric).
    - **3b1.** HealthSync informs the user of the invalid input.
    - Use case resumes from step **2**.
- **4a.** The tag(s) already exist.
    - **4a1.** HealthSync informs the user of the duplication.
    - Use case resumes from step **2**.
- **5a.** Healthcare Administrator chooses to undo the command.
    - **5a1.** Transition to **UC09 - Undo Previous Command**.

---

#### **UC15 - Delete Tag from Patient**

**Main Success Scenario (MSS):**
1. Healthcare Administrator chooses to delete tag(s) from a patient.
2. HealthSync prompts the user to enter:
    - Patient index.
    - Tag(s) to delete.
3. Healthcare Administrator enters the index and tag(s).
4. HealthSync checks if the tag(s) exist.
5. HealthSync removes valid tag(s) and shows the updated tag list.
6. Use case ends.

**Extensions:**
- **3a.** The given index is invalid (out of range or does not exist).
    - **3a1.** HealthSync informs the user of the invalid index.
    - Use case resumes from step **2**.
- **4a.** The tag(s) do not exist.
    - **4a1.** HealthSync warns the user of the invalid tag.
    - Use case resumes from step **2**.
- **5a.** Healthcare Administrator chooses to undo the command.
    - **5a1.** Transition to **UC09 - Undo Previous Command**.

---

#### **UC16 - Undo Previous Command**

**Main Success Scenario (MSS):**
1. Healthcare Administrator chooses to undo the most recent modifying command.
2. HealthSync checks if there is a previous state to revert to.
3. HealthSync reverts the application to the previous state.
4. HealthSync confirms the undo action.
5. Use case ends.

**Extensions:**
- **2a.** No previous state available to undo.
    - **2a1.** HealthSync informs the user that there is nothing to undo.
    - Use case ends.
- **2b.** Undo has already been used.
    - **2b1.** HealthSync informs the user that there is nothing to undo.
    - Use case ends.
- **2c.** The most recent command is a `find` or `list` command.
    - **2c1.** HealthSync informs the user that there is nothing to undo.
    - Use case ends.
- **2d.** The most recent command is an `archive` command.
    - **2d1.** HealthSync informs the user that undo cannot be performed for the archive command.
    - Use case ends.

---

#### **UC17 - Redo Last Undone Command**

**Main Success Scenario (MSS):**
1. Healthcare Administrator chooses to redo the last undone command.
2. HealthSync checks if there is a redo-able state.
3. HealthSync re-applies the previously undone command.
4. HealthSync confirms the redo action.
5. Use case ends.

**Extensions:**
- **2a.** No command available to redo.
    - **2a1.** HealthSync informs the user that there is nothing to redo.
    - Use case ends.

---

#### **UC18 - Exit Application**

**Main Success Scenario (MSS):**
1. Healthcare Administrator chooses to exit the application.
2. HealthSync saves the application state.
3. HealthSync closes the application.
4. Use case ends.

---

### Non-Functional Requirements

- Should work on any mainstream OS as long as it has **Java 17 or above** installed.
- Should be able to handle up to **50 concurrent patients** without noticeable sluggishness in performance for typical use.
- A user with **above-average typing speed** for regular English text (i.e., not code or system admin commands) should be able to accomplish most tasks **faster using commands than with the mouse**.
- **HealthSync must comply with relevant healthcare data protection regulations**, such as **PDPA**.
- The architecture should support **modular extensions**, allowing for additional features (e.g., appointment scheduling, integration with electronic health records).


--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_

--------------------------------------------------------------------------------------------------------------------

## **Glossary**

1. **Mainstream OS**: Windows, Linux, Unix, MacOS
2. **Private contact detail**: A contact detail that is not meant to be shared with others
