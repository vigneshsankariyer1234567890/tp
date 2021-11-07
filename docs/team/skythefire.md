---
layout: page
title: Tan Ming Ann's Project Portfolio Page
---

### Project: Teletubbies

Teletubbies is a desktop application developed and designed for telemarketers and telemarketing supervisors to help manage their customer contacts. Teletubbies allow users to create a checklist based on a set of customer contact data, with additional features such as tagging and viewing statistics to help improve their workflow.

Given below are my contributions to the project.

* **New Feature**: Added the ability to add remarks to a customer.
  * What it does: Allows the user to add a remark to a customer using the `remark` command
  * Justification: The existing `tag` command is not ideal for comments longer then 1-2 words. Telemarketers may need to write down notes for certain customers such as “Need to call back for confirmation”.
  * Credits: Reused from [SE-EDU Remark Command Tutorial](https://nus-cs2103-ay2122s1.github.io/tp/tutorials/AddRemark.html)

* **Enhancements to existing features**:
  * Updated contacts such that only name and phone number are mandatory fields, to make the application more customized for telemarketing (Pull request [\#44](https://github.com/AY2122S1-CS2103T-W15-4/tp/pull/44))
  * Allow deletion of contacts by referencing their phone number (Pull request [\#38](https://github.com/AY2122S1-CS2103T-W15-4/tp/pull/38))
  * Disallow duplicate phone numbers in Teletubbies (Pull request [\#72](https://github.com/AY2122S1-CS2103T-W15-4/tp/pull/72))
  * Removed tag related features from `add` and `edit` commands, to allow tag handling to be done solely through the `tag` command (Pull request [\#160](https://github.com/AY2122S1-CS2103T-W15-4/tp/pull/160))

* **Code contributed**: [RepoSense link](https://nus-cs2103-ay2122s1.github.io/tp-dashboard/?search=skythefire&sort=groupTitle&sortWithin=title&since=2021-09-17&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=false&tabOpen=true&tabType=authorship&tabAuthor=skythefire&tabRepo=AY2122S1-CS2103T-W15-4%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false)

* **Documentation**:
  * User Guide:
    * Added documentation for the features `add`,`delete` and `remark`.
    * Did cosmetic tweaks to existing documentation of features.
  * Developer Guide:
    * Added implementation details and UML diagram of the `delete` feature.
    * Added implementation details of the autocomplete feature.

* **Community**:
  * Reported bugs and suggestions for other teams in the class (examples: [1](https://github.com/skythefire/ped/issues/1), [2](https://github.com/skythefire/ped/issues/4))
