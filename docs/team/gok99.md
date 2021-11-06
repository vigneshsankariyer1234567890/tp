---
layout: page
title: Gokul Rajiv's Project Portfolio Page
---

### Project: Teletubbies

Teletubbies is a desktop application developed and designed for telemarketers and telemarketing supervisors to help manage their customer contacts. Teletubbies allow users to create a checklist based on a set of customer contact data, with additional features such as tagging and viewing statistics to help improve their workflow.

Given below are my contributions to the project.

* **Tag-related features**:
    * Improved tags with the addition of a tag value field and accessibility based on the user's role. (Pull request [\#71](https://github.com/AY2122S1-CS2103T-W15-4/tp/pull/71), [\#80](https://github.com/AY2122S1-CS2103T-W15-4/tp/pull/80))
      * Justification: It is likely that users would want to have tags with specific values (e.g `assignee: Jason`) or disallow their employees from editing these tags.
    * `tag` (for adding tags) and `tagrm` (for removing tags) that allows users to tag contacts in large batches with a range of indices. (Pull request [\#80](https://github.com/AY2122S1-CS2103T-W15-4/tp/pull/80))
    * `filter` and `export` commands that allow users to specify contacts with their tags. (Pull request [\#71](https://github.com/AY2122S1-CS2103T-W15-4/tp/pull/71), [\#80](https://github.com/AY2122S1-CS2103T-W15-4/tp/pull/80), [\#43](https://github.com/AY2122S1-CS2103T-W15-4/tp/pull/43))
      * Justification: Tags would be significantly more useful, if tagged users can easily be exported in batch. For example, a supervisor might want to export all contacts assigned to their employee Joanne.

* **Auto-complete feature**:
    * Users can press the [TAB] key after typing the name of a command to autofill recommended flags for the command.
      * Justification: Typing out flags for a command can be cumbersome. Autofill speeds up this process and allows users to avoid looking up the help message.

* **GUI key bindings**:
    * Set-up key bindings for [UP], [DOWN] (for command history) and [TAB] (for autocomplete) keys, to trigger actions on key-presses.
    
* **Enhancements to existing features**:
    * Improved Tag feature (as mentioned above) 
    * Improved workflow for creating commands with UI effects, with the help of `UiConsumer` which encapsulates changes to the UI (like a window pop-up), and reduces the overhead for generating new UI effects. (Pull requests [\#43](https://github.com/AY2122S1-CS2103T-W15-4/tp/pull/43), [\#182](https://github.com/AY2122S1-CS2103T-W15-4/tp/pull/182))


* **Code contributed**: [RepoSense link](https://nus-cs2103-ay2122s1.github.io/tp-dashboard/?search=gok99&sort=groupTitle&sortWithin=title&since=2021-09-17&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=false&tabOpen=true&tabType=authorship&tabAuthor=gok99&tabRepo=AY2122S1-CS2103T-W15-4%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false)

* **Project Management**:
    * Managed release `v1.2` on github
    * Implemented pre-push git hooks to run tests before code is pushed to github
* **Documentation**:
    * User Guide:
        * Added documentation for the features `tag`, `tagrm`, `export` and `filter`.
        * Added documentation for key bindings (convenience features).
    * Developer Guide:
        * Added implementation details of `Logic` and the `UiConsumer`, `tag`, and `filter` feature.

* **Community**:
    * PRs reviewed (with non-trivial review comments): (examples: [1](https://github.com/AY2122S1-CS2103T-W15-4/tp/pull/42), [2](https://github.com/AY2122S1-CS2103T-W15-4/tp/pull/38), [3](https://github.com/AY2122S1-CS2103T-W15-4/tp/pull/39))
    * Reported bugs and suggestions for other teams in the class (examples: [1](https://github.com/gok99/ped/issues/2), [2](https://github.com/gok99/ped/issues/1))
