# ActivityPlanning


The application is aimed at the inefficiency of daily work and helps to plan, track and analyze working hours.
Therefore, there are 3 main screens: plan, track and dashboard.

Managers are supposed to use the plan screen to fill out what percentage they expect their employees to spend on tasks.

Workers are supposed to use the track screen to measure how much time they spend on tasks.
The idea is that the worker can track how much time he actually spent on the task and how much manager expected.
Thus, at the end of the working day, the worker finishes the work by clicking on the “Finish” button and sees an encouraging success message or advice to be more effective in some types of tasks.

The dashboard screen is for both the manager and the employee: it is important to see the trends, the difference between the plan and reality for both of them.

Everything works offline: data is stored in the database. Neither plans nor tracking will be lost after the application is closed.

Time is measured in hours at plan screen and in minutes at track and dashboard screens.

For a better understanding of the idea, there are examples of actions that can be added to the plan screen.
Initial tracking is built automatically based on the planned actions when the user clicks the "Start Today" button on the track screen. Workers should press buttons only when they start / stop / continue during their work.
Dashboard is also automatically created based on the tracking results for today. Incomplete events are also taken into account.

## Getting Started

No special actions required. After cloning repository you can immediately start working with it.

## Libraries used

* [RxJava2] - Reactive extensions for easier threads managing
* [Room] - Persistence Library
* [blackfizz/EazeGraph] - Charts
* [Junit4] - Testing engine

## Versioning

I used [SemVer](http://semver.org/) for versioning.

## Authors

* **Anastasia Laushkina** - *Initial work and idea*

## Coming next features

* Progress bars at track screen
* Dashboards for random dates/periods
* Ability to compare the plan and reality at graphs
* System push notifications for worker when time for the task is running out
* Ability to edit tasks
* Plan events and warn worker when those events are approaching & integration with calendar
* Validation of the entire plan
* Tests & DI

## Acknowledgments

* Inspired by DIVA
