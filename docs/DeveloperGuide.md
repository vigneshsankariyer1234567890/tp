---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md). 

--------------------------------------------------------------------------------------------------------------------

## **Introduction**

### **Purpose**
This document describes the architecture and system design of the Teletubbies desktop application
for Telemarketers and Telemarketer Supervisors.

### **Scope**
The document aims to comprehensively cover the software architecture and software design decisions for 
the implementation of Teletubbies. It is intended to serve as a guide for the developers, the designers, and the testers 
for the application.

The definitions for the emboldened terms can be found in the [_Glossary_](#glossary) Section.

### **Design Goals**
The developers work closely with telemarketers and telemarketer supervisors to better understand their needs, so that 
the requirements of the product can be as clear-cut as possible.

#### _User-Centricity_
Teletubbies can be used by both telemarketers and supervisors, and chooses the appropriate features according to the 
current user's **role**, so that the user can work effectively and efficiently to complete their tasks.

#### _Improved Workflow Efficiency_
Teletubbies is optimised for **Command-Line Interface** (CLI) usage, which streamlines workflow 
within the application by centralising the entering of commands in a single text input window.

#### _Seamless Data Integration_
Teletubbies allows telemarketers and their supervisors to share and merge data seamlessly. Synchronization of customer 
data between telemarketers and their supervisors is often a necessary aspect of their job, and Teletubbies provides
avenues to make this process easy and hassle-free.

#### _Data Safety and Recoverability_
With a large number of contacts stored in a contact list, it is vital that the user’s current progress is saved 
frequently to assist in data recovery in the event of unexpected system failure. Hence, the contact list is saved 
after each command issued by the user.

Additionally, in the event of human error, an undo command is available for users to revert to previous states 
within the session itself.

#### _Scalability & Maintainability_
Within a team of developers, it is important that the developers reduce dependencies within the project to increase 
testability and extensibility.

In addition, with requirements that constantly change, it is important to be able to easily modify features and 
functionalities that have already been written without disturbing other related components. Therefore, the main 
components of the application such as the `Logic`, `UI`, `Model`, and `Storage` are separated into decoupled modules.

Along with the strict adherence to software design principles, such as **Single Responsibility** and
**Separation of Concerns**, the modularity of the software design allows future developers to add features to Teletubbies 
without having to deal with tedious side-effects.

### **Glossary**
**_Command-line Interface (CLI)_**: A user interface that allows users to interact with a system through text commands.

**_Graphical User Interface (GUI)_**: A user interface that allows users to interact with a system through graphical icons.

**_User stories_**: Simple descriptions of features told from the perspective of the user.

**_Completion Status_**: A contact can be marked as either “completed” or “not completed”, indicating if the contact 
has been contacted.

**_Role_**: Users are assigned either the role of telemarketer or supervisor.

**_Single Responsibility Principle_**:  A software engineering principle that states that every module, class or 
function in a computer program should be responsible for and encapsulate only a single part of 
the program’s functionality.

**_Separation of Concerns Principle_**: A software engineering principle that states that programs should be separated 
into distinct sections which address concerns, or sets of information that affects the code of a computer program.

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* [_SE-EDU Remark Command tutorial_](https://nus-cs2103-ay2122s1.github.io/tp/tutorials/AddRemark.html)

--------------------------------------------------------------------------------------------------------------------

## **System Overview**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document can be found in the [diagrams](https://github.com/se-edu/addressbook-level3/tree/master/docs/diagrams/) folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

#### Main components of the architecture

`Main` has two classes called [`Main`](https://github.com/AY2122S1-CS2103T-W15-4/tp/blob/master/src/main/java/teletubbies/Main.java) and [`MainApp`](https://github.com/AY2122S1-CS2103T-W15-4/tp/blob/master/src/main/java/teletubbies/MainApp.java). The `Main` component,
* (At app launch) initializes the components in the correct sequence, and connects them up with each other.
* (At shut down) shuts down the components and invokes cleanup methods where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

The rest of the App consists of four components.

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

#### How the architecture components interact with each other

The Sequence Diagram below outlines how the components interact with each other, in a scenario where the user issues the command `delete p/87654321`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

**API** : [`Ui.java`](https://github.com/AY2122S1-CS2103T-W15-4/tp/blob/master/src/main/java/teletubbies/ui/Ui.java)

Here's a (partial) class diagram of the `UI` component:

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts such as `CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter`, etc. All these parts, including the `MainWindow` itself, inherit from the abstract `UiPart` class which captures the commonalities between the classes that represent parts of the visible GUI.

The `UI` component uses the JavaFX UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2122S1-CS2103T-W15-4/tp/blob/master/src/main/java/teletubbies/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2122S1-CS2103T-W15-4/tp/blob/master/src/main/resources/view/MainWindow.fxml).

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays each `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2122S1-CS2103T-W15-4/tp/blob/master/src/main/java/teletubbies/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

How the `Logic` component works:
1. When `Logic` is called upon to execute a command, it uses the `InputParser` class to parse the user command.
2. This results in a `Command` object which is executed by the `LogicManager`.
3. The command can communicate with the `Model` when it is executed (e.g. to add a person).
4. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `Logic` back to the `UI`.
5. The `CommandResult` object also contains a `UiConsumer` object, which consumes `MainWindow` from `UI`, and encapsulates instructions for 
the Command's UI effects (e.g. opening help menu, opening file select window).

The Sequence Diagram below illustrates the interactions within the `Logic` component for the `execute("delete p/87654321")` API call.

![Interactions Inside the Logic Component for the `delete p/87654321` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

In the diagram above, the creation of the `UiConsumer` object is represented. Since DeleteCommand does not have a UI effect, the consumer simply does nothing. For more details on how a UI effect is 
executed, please see [Commands with UI effects](#commands-with-ui-effects). Subsequent sequence diagrams for commands with no UI effect might omit the `UiConsumer` portion of `Command Result`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
1. When called upon to parse a user command, the `InputParser` class creates an `XYZCommandParser`. Here, `XYZ` is a placeholder for the specific command name, e.g. `AddCommandParser`.
2. The newly created `XYZCommandParser` class utilizes the other classes shown above, such as `ParserUtil`, `CliSyntax`, etc. to parse the user command and create a `XYZCommand` object, e.g. `AddCommand`.
3. The `InputParser` will then return the newly created `XYZCommand` object as a `Command` object.

All `XYZCommandParser` classes like `AddCommandParser` and `DeleteCommandParser` inherit from the `Parser` interface so that they can be treated similarly where possible, such as during testing.
Similarly, all `XYZCommand` classes inherit from the `Command` class.

### Model component

**API** : [`Model.java`](https://github.com/AY2122S1-CS2103T-W15-4/tp/blob/master/src/main/java/teletubbies/model/Model.java)

Here's a (partial) class diagram of the `Model` component:

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the contacts, i.e. all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g. results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' (e.g. the `Ui` component can be bound to this list so that the UI automatically updates when the data in the list change).
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` object.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components).

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2122S1-CS2103T-W15-4/tp/blob/master/src/main/java/teletubbies/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in json format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `teletubbies.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Commands with UI effects

Commands that require a UI effect will need to set up the `UiConsumer` functional interface that allows the commands to access the functionality
of `MainWindow`. The following is an example of a constructor for `CommandResult` that includes this set-up:

```java
return new CommandResult(SHOWING_HELP_MESSAGE, CommandResult.UiEffect.SHOW_HELP, new HelpUiConsumer());
```

The third argument of this method call (`new HelpUiConsumer()`), is a `UiConsumer` that uses the `handleHelp` method in `MainWindow`. More complicated effects
can be constructed with the exposed functions in `MainWindow` (See [Import/Export Features](#import--export-features)). The `UiConsumer` can be implemented 
either as a concrete class (like `HelpUiConsumer`) or as a lambda function. 

If the `UiEffect` type (the second constructor argument) does not exist for a new command that you want to add, this corresponding `UiEffect`
type should be added into the `UiEffect` enum in `CommandResult`.

On the other hand, if a command has no special UI response, the `UiEffect` type should be `NONE`.

#### Implementation (`help` command)

This section describes the implementation of the `help` command. The implementation of other commands that make use of UI effects/responses (e.g. 
file choosers, pop-up windows) will be similar.

![](images/HelpSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `ImportCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

**Step 1.** The user enters the `help` command in the command box. 

**Step 2.** A `CommandResult` object containing a `UiConsumer` is created and returned by the `HelpCommand`. The `UiConsumer` contains instructions for the UI effect of the help command.

**Step 3.** `MainWindow` calls `CommandResult#executeUiEffect` of the returned `CommandResult` object, which in turn call the `UiConsumer`. 

**Step 4.** The `UiConsumer` (which contains the implementation for the `help` command's) UI effect, calls `MainWindow#handleHelp` which invokes the help window pop-up.

#### Design Considerations

* **Alternative 1 (current implementation):** Use `UiConsumer` to encapsulate UI effects
    * Pros
      * Allows UI effects to be open for extension and closed for modification. Adding additional UI effects incur minimal cost.
      * UI effects can contain a combination of features exposed by `MainWindow` to create arbitrarily complex UI effects.
    * Cons
      * Creates significant coupling between `MainWindow` (UI) and `UiConsumer` (Logic).
      
* **Alternative 2 (previous implementation):** Use boolean flags in `CommandResult` to signify instructions for UiEffects
    * Pros
        * Ease of implementation
    * Cons
        * If a new UI effect needs to be allowed, both `MainWindow` and `CommandResult` will need to be modified with further boolean flags and checks for whether a UI effect has been enabled. This would be in violation of the open-closed principle, since this is not closed to modification.


### Import / Export Features

#### Implementation

The `import` and `export` mechanism is supported by all of the main components, specifically in the following ways:

* The `Ui` component is accessed in `CommandResult` through a UI consumer (`ThrowingConsumer<MainWindow>`). This allows the user to interact with the JavaFx FileChooser to select files to be imported/exported to.

* The execution of the `ImportCommand` and `ExportCommand` is distinct from other commands executed by `Logic` because it is passed to the UI consumer in the `CommandResult` due to their reliance on the UI file chooser.

* For import, the `Model` component is accessed to set the new AddressBook of contacts. On the other hand, export filters the AddressBook of the `Model` using the tags specified in the user command to retrieve contacts to be exported.

* Functions in `Storage` were used to write AddressBooks to JSON files as well as read and convert JSON files to AddressBook objects.

The following sequence diagram shows how the `import` operation works:

![](images/ImportSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `ImportCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

#### Design Considerations

**Aspect: How to allow commands to specify UI effects**

* **Alternative 1 (current choice):** Import/Export command can be executed by CLI command or menu bar button.
    * Pros: Users are given the flexibility of choosing either method to enter the command according to their preference.
    * Cons: Contacts to be exported are unable to be filtered by tags in the menu bar button.

* **Alternative 2:** Import and export are buttons in the menu bar only.
    * Pros: Similar to the layout of menu bars in Microsoft Office Applications, which might be familiar to users.
    * Cons: Target users can type fast and might prefer typing in commands. Contacts to be exported are unable to be filtered by tags.

### Profile feature

#### Implementation

Setting the user's profile is facilitated through the `ProfileCommand` and the `ProfileCommandParser` class, which
parses the user's input to create a new `UserProfile` object to be set in the current `Model` component.

The following sequence diagram shows how the `profile` operation works:

![ProfileSequenceDiagram](images/ProfileSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `ProfileCommandParser` and `ProfileCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

#### Design Considerations

**Aspect: Parameters of the `profile` command**

* **Alternative 1 (current choice):** Profile is set with both name and role as mandatory fields.
    * Pros: Ensures that both name and role are accounted for when changing the profile.
    * Cons: Both fields must be entered for the user to set the profile, which can be tedious if it has to be done multiple times.

* **Alternative 2 (possible future implementation):** Profile can be set with only the name, or both name and role.
    * Pros: Allows users to change their name alone without having to enter role as well.
    * Cons: The cost of implementation is not heavily outweighed by the explicit need for the feature, as there should be infrequent changes in the user's name.

### Mark contacts as done feature

#### Implementation

Marking contacts as done, or contacted, is assisted by `CompletionStatus`. It has a composition association with `Person`
and holds a boolean value indicating that the person has been contacted by the telemarketer.

The following sequence diagram shows how the `done` operation works:

![DoneSequenceDiagram](images/DoneSequenceDiagram.png)

### Delete contacts feature

The `delete` command allows the telemarketer to delete a contact using a contact's displayed index number or phone number. 
The user can delete a contact via a `delete i/1` or `delete p/87654321` input.
* delete using a contact's displayed index number by using the `i/` prefix.'
* delete using a contact's phone number by using the `p/` prefix.

The following activity diagram summarizes what happens when a user executes a delete command:

![DeleteActivityDiagram](images/DeleteActivityDiagram.png)

#### Design considerations

**Aspect: Parameters of the `delete` command**

Since the telemarketer is responsible for talking to customers on the phone to sell products, 
it will be useful for them to interact with their contact lists through the customer's phone number.

* **Alternative 1 (current choice):** Delete using either index or phone number.
    * Pros: Flexibility in method of deletion.
    * Cons: Slightly more complicated for user to delete a contact.

* **Alternative 2:** Delete via phone number only.
    * Pros: Implementation is more straightforward, as there is only one type of input to be expected.
    * Cons: Removes the convenience of deleting using a contact's index.

### Tag feature

The following section will describe the implementation of the tag feature. The implementation for remove tag feature is 
similar and hence won't be repeated in this section. 

The `TagCommand` allows user to tag contacts. A tag has a mandatory name and an optional value (both are case-sensitive).
It also requires a specification of indices of the contacts to tag. To allow batch tagging, this is done with a `Range`
(found in `commons/core/index`). A `Range` encapsulates a set of indices. The `TagCommandParser` supports ranges of the 
form `1,2,3` (Comma-separated) or `1-5` (Hyphen-separated). The parsing of range is handled by `ParserUtil#parseRange`.

The sequence diagram below represents the creation of a `TagCommand` object by the `TagCommandParser`. Details related to 
obtaining the tag's name, value and supervisor flag have been omitted from the diagram below.

![](images/TagParserSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `ProfileCommandParser` and `ProfileCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

After the creation of the `TagCommand`, it will be executed by `LogicManager`. Below is the sequence diagram for the execution 
of the `TagCommand`.

![](images/TagSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `ProfileCommandParser` and `ProfileCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

**Step 1.** `LogicManager` executes the `TagCommand`.

**Step 2.** `TagCommand` obtains the list of `Person`s corresponding to its range.

**Step 3.** For each person in the obtained person list a new tag is generated by generateNewTag() and added to the person's list of tags (or used to replace the existing 
tag if the person already has the tag). The new set of tags is used to instantiate the `editedPerson`. Details of adding the tag to the `editedPerson` has been 
omitted for brevity.

**Step 4.** The updated `editedPerson` is used to replace the original `person` with `setPerson`.

#### Design Considerations

Tags are used extensively in Teletubbies with a variety of purposes. Tags in Teletubbies are much more versatile than their
implementation in AB-3. Tags now have both names and values, and can also be set to be only editable by supervisors. This
allows for supervisors to create assignee tags for example, with name 'assignee' and value as the name of the assignee. 
The tag can also be set to be modifiable only by supervisors, so that employees won't be able to tamper with the manpower 
assignment. 

The completion status of a contact is also internally represented as a tag, with name 'CompletionStatus' and value
either 'INCOMPLETE', 'ONGOING' or 'COMPLETE'. Since the `filter` and `export` commands make use of tags, this allows users to filter and
export contacts by 'CompletionStatus', since CompletionStatus is also a tag after all. Supervisors can also export contacts corresponding
to a particular assignee for contact dissemination, which is a critical feature for supervisors.

**Aspect: How general (in terms of functionality) should tags be?**

* **Alternative 1 (current choice):** Users are free to set tag name, value and accessibility
    * Pros: 
      * Users have more configurability options for tags, which opens up use-cases and ultimately makes tags more useful.
      * Implementation for `filter` and `export` is simplified since only tags will be used to specifying contacts for these commands.
    * Cons: 
      * Commands pertaining to adding and removing tags now get more complex and risk being counter-intuitive since there are more configuration options. 

* **Alternative 2:** Tags only have a name attribute.
    * Pros: 
      * Ease of implementation (virtually identical to AB-3).
      * Lesser configurability of tags and hence simpler and easier to use tag commands.
    * Cons: 
      * Separate functionality will need to be created for assignee and completion status, which will function very similarly to tags, but are not tags.
      * `filter` and `export` commands will potentially be a lot more complex, since users can export by tags, completion status or assignee, and they can't be handled similarly.


### Filter feature

The following section will describe the implementation of the filter feature. The use of a `Predicate<Person>` here to 
filter the person list is similar to the implementation of the find feature. 

The sequence diagram below represents the creation of a `FilterCommand` object by the `FilterCommandParser`.

![](images/FilterParserSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `ProfileCommandParser` and `ProfileCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

`PersonHasTagsPredicate` is a subclass of Predicate<Person>. Below is the sequence diagram for the execution of the `FilterCommand`.

![](images/FilterSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `ProfileCommandParser` and `ProfileCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

The existing functionality of Java's `FilteredList` is leveraged for the implementation of the command. The execution of the
`FindCommand` is near identical except to `FilterCommand` for the use of `NameContainsKeywordsPredicate` instead of
`PersonHasTagsPredicate`.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, Logging, Testing, Configuration, Dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope


#### **Target User Profiles**


David Wong
* works as a **telemarketer** from home and makes internet calls on his computer
* likes to import/export data quickly
* wants to mark the contacts that he has already called and/or successfully marketed to 
* wants to keep track of his progress
* has a need to manage a significant number of contacts
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

<br>

Anne Goh 
* works as a **telemarketing supervisor** from home
* needs to pass lists of contacts to her subordinates to contact
* wants to monitor the performance of her subordinates
* has a need to manage a significant number of contacts
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

<br>

#### **Value Proposition**

* Able to import / export data into an easy-to-view format, useful for those who are not experienced at working with data files such as JSON / CSV / etc.
* Easy to add/annotate details on users to keep track of user defined metrics
* Ability to get analytics about metrics for progress-tracking


### User stories

Priorities: 
* High (must have) - `* * *`
* Medium (nice to have) - `* *`
* Low (unlikely to have) - `*`

| Priority | As a …​                              | I want to …​                  | So that I can…​                                                           |
| -------- | --------------------------------------- | -------------------------------- | ---------------------------------------------------------------------------- |
| `* * *`  | Telemarketer                            | import the list of customers     | easily view all the contacts I need to call                                  |
| `* * *`  | Telemarketer                            | export the list of customers     | send the list to my supervisor                                               |
| `* * *`  | Telemarketing Supervisor                | import the list of customers     | easily view the contacts completion status of my subordinates                |
| `* * *`  | Telemarketing Supervisor                | export the list of customers     | send the list to my subordinates for them to complete                        |
| `* * *`  | Telemarketer / Telemarketing Supervisor | save data from current session   | save my current progress to continue during the next session                 |
| `* * *`  | Telemarketer / Telemarketing Supervisor | load data from previous session  | pick up where I left off from my previous session                            |
| `* * *`  | Telemarketer                            | indicate my name under 'profile' | identify myself in progress reports for my supervisor                        |
| `* * *`  | Telemarketer / Telemarketing Supervisor | indicate my role under 'profile' | get access to the functionalities that cater to my specific job              |
| `* * *`  | Telemarketer                            | mark a contact as completed      | see that I have already called a contact successfully                        |
| `* * *`  | Telemarketing Supervisor                | add a contact                    | add contacts that need to be called by my subordinates                       |
| `* * *`  | Telemarketing Supervisor                | delete a contact                 | remove contacts that no longer need to be tracked or have been added wrongly |

*{ More to be added as new features are introduced }*

### Use cases

For all use cases below, the **System** is the `Teletubbies` application and the **Actor** is the user, unless specified otherwise.

#### Use case: Delete a person

**MSS**

1.  User requests to list persons
2.  Teletubbies shows a list of persons
3.  User requests to delete a specific person in the list
4.  Teletubbies deletes the person

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. Teletubbies shows an error message.

  Use case resumes at step 2.

#### Use case: Telemarketer workflow during a shift
* Actor: Telemarketer User
* Precondition: Telemarketer has obtained a list of customers assigned by Supervisor

**MSS**

1. User starts up the Teletubbies desktop application.
2. User selects and imports the JSON file containing the list of contacts for the shift from a file location.
3. Teletubbies displays the imported contacts.
4. User copies a customer’s contact number and makes the phone call
5. User marks the customer as completed
6. Teletubbies saves the file and displays that the call for the contact is completed.

Use case ends.

**Extensions**

* 3a. Teletubbies does not detect a JSON file in the specified file path.

  * 3a1. Teletubbies displays a message prompting the Telemarketer to try importing JSON file again.
  * 3a2. Telemarketer imports a JSON file containing the customers’ details
  * Steps 3a1 to 3a2 are repeated until a valid JSON file is selected
  
  Use case resumes from step 4.


* 3b. Teletubbies detects that the JSON file selected is not in the correct format.

    * 3b1. Teletubbies displays a message informing the Telemarketer that the file is not in the correct format.
    * 3b2. Telemarketer informs their Supervisor about the error

  Use case ends.


* 5a. Teletubbies detects that index used in the command is invalid.
  * 5a1. Teletubbies displays a message to inform Telemarketer about the input error
  * 5a2. Telemarketer re-enters the command with a correct index number for the contact
  * Steps 5a1 to 5a2 are repeated until a valid customer phone number is input
  
  Use case resumes from step 6.


### Non-Functional Requirements
1. New telemarketers should be able to easily use the application. (**Quality requirement**)
2. The application should save and load data fast, and startup should take a few seconds at the most. (**Performance requirement / Response time**)
3. Data should be saved frequently and automatically to allow for easy recovery on possible crashes. (**Disaster recovery**)
4. The product should work on all OS, and both 32-bit and 64-bit environments. (**Technical / Environment requirement**)
5. The application should be able to efficiently (without a noticeable slowdown) handle standard operations (add, delete, search, etc) for up to 10,000 users. (**Data requirement**, ie. volatility)
6. The product project is expected to adhere to a schedule that delivers a feature set every two weeks. (**Process requirements**)
7. The product is not required to handle in-app sharing of data across users on different devices. (**Project scope**)
8. The product should be available for potential testers to test the capabilities of the product / find any bugs or issues. (**Testability**)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the [jar file](https://github.com/AY2122S1-CS2103T-W15-4/tp/releases) and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete i/1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete i/0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete i/x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
