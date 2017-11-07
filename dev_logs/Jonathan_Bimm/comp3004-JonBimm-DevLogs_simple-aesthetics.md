# Jonathan Bimm's Dev Logs #

## General Dev Logs Description: ##

The dev logs are based on our weekly cycle of work, which starts on a Wednesday, and continues until the following Wednesday. At the start of each cycle, we have a team meeting, where we discuss how things are going in the development process, and what work should be expected the following week.

  The dev logs prescribe to the following format:
  - Dev log title for the development week ending on a certain date
  - Detailed description of what was expected for the week
  - Items that were completed for that particular week, and whether the week was considered successful
  - Comments on what was completed (if any), which include
      - Unexpected incidents
      - Setbacks
  - Small and basic list of goals for the following week

## Dev Log #1: Week of Sept 20th ##

### General description: ###

This week was dedicated to focusing fine tuning the idea for the project, getting all of the team mates on the same page, and preparing the D1 Deliverable Presentation and documentation.

### What was completed? ###

This particular week was successful. We refined our idea of what the project would look like, and everyone agreed on our general idea of what it is. We also submitted out documentation and had a strong presentation. It was a good week for the team.

### Comments: ###

None at this time.

### Goals for next week: ###

The goals for the following week are to create a general design for our project, and to set up the general tasks for each of our team members.


## Dev Log #2: Week of September 27th ##

### General description: ###

My goals for this week was to create a general design for our project and allocate group members to the tasks they will be pursuing for the rest of the term. We also had the goal of working on preliminary configuration and setup for the app.

### What was completed? (Was it successful?) ###

This week was also successful. We assigned everyone to particular tasks: I am working on the REST portion of the server, Brad is working on the database interface and the database of the server, Vishahan​ us working on the UI for the frontend, and Osama is working on the REST client component for the frontend. We also created a general overview of what was expected for the team, up until the week before the prototype demonstrations.

### Comments: ###

None at this time.

### Goals for next week: ###

- Think of authentication protocol (working with Brad)
- Create a simple REST server


## Dev Log #3: Week of October 4th ##

### General description: ###

My goals for this week was to get a basic REST server working. This entailed getting through all of the configuration and general setup to have an efficient REST Server working. The process of setting up the particular server also entailed choosing a server and having a preliminary idea of where the server would be uploaded when our app was properly running.

### What was completed? ###

This week was successful. I completed my task, choosing to use the Spring Framework to create the REST server, and to upload the server to the Pivotal cloud when a sufficient amount of development was completed (or the prototype demo need to be completed).

### Comments: ###

The process of setting up the server was a lot more time intensive then I had expected. I had to trouble shooting to get the server running since resources on setup were not as clear as I had originally anticipated. As a result, I think I will give myself more time for configuration based tasks in the future.

### Goals for next week: ###

- Make REST server work with db interface
- Make skeleton for all REST calls
- Think about algorithm for matching


## Dev Log #4: Week of October 11th ##

### General description: ###

My goals for this week was to think about the basic functionality of the REST server, including what calls to the server would be required by the front end to implement basic functionality and the matching algorithm for our app. I also had the expectation of attempting to merge at least one call of the db interface (was expected to be completed) with the classes enabling the REST calls.

### What was completed? ###

There were both successes and setbacks this week. I discussed and designed the algorithm at a high level with Brad and I implemented most of a basic skeleton for the REST calls (not all desired REST calls were included in the skeleton). The major setback of this week, was that the database and its interface were not completed and, therefore, a full call could not be implemented with the REST functionality and the database.

### Comments: ###

This was the first time that the team and I failed to reach expectations from the previous week. We are having a difficult time managing the expectations from this course and other courses. So, our weekly ambitions had to be relaxed quite a bit to accommodate the work our global workload.

### Goals for next week: ###

- Work on group sorting algorithm (try to complete if possible)
- Look into pivotal (Is it free and is it easy to use)
- Do preliminary work on deadline functionality of app (automatic sorting of groups when a deadline is reached)
- Demo my work completed along side everyone else in the group


## Dev Log #5: Week of October 18th ##

### General description: ###

My goals for this week was to work on key functionality for our app, including: completing work on the group matching algorithm, looking into Pivotal as a cloud solution, and understanding what is required to implement an automatic sorting function based on a deadline (sorting happens automatically on the deadline). This week we also decided to demo all the work each of us has completed up until this point.

### What was completed? ###

This week was mostly successful. I completed the foundational work on the group sorting algorithm (a single group could be appropriately sorted) and looked into Pivotal, which seemed to be a viable solution (has a free trial and is based on Cloud Foundry, which makes it easy to use). I also looked into ways to implement an automatic sorting feature based on a deadline, which I decided to implement using a cron based scheduling feature in Spring. We also all demoed our work to each other, and each of the individual parts seemed to be working well.

### Comments: ###

This week was better for the team and I, but we are generally still having difficulty balancing the workload for this course and other courses.

### Goals for next week: ###

- Get the full pipeline for our project working (this was decided in anticipation for the big demo coming up)
- Finish work on our prototype
- Finish work on group sorting algorithm
- Upload current code into Pivotal
- Finish work on deadline functionality
- Make REST server work with db interface
- Complete skeleton for all REST calls


## Dev Log #6: Week of October 25th ##
  
### General description: ###
  
My main goal (and the teams) for this week was to finish our individual development for each of our particular areas of development (UI, server, etc) and then combine our work during an extended weekly team meeting on the 25th of October. The server was also expected to be uploaded on the 25th. This week had a very demanding workload, which involved completing all the outstanding tasks I was expected to do throughout the development process until the week. The outstanding tasks include: implement the group sorting algorithm, complete functionality for automatic sorting based on a deadline and generally having all REST calls working that are needed for our prototype demo.

### What was completed? ###

This week was the largest failure in completing development goals I have had up until this point. We failed to be prepared to merge our work, and get the whole apps pipeline working by the end of our meeting on the 25th. I also did not complete all the REST calls for the prototype, or the automatic sorting based on a deadline that is required by our app. However, despite all the setbacks, the sorting algorithm is mostly complete this week and the database and the REST endpoints were successfully merged and tested. 

### Comments: ###

We were not prepared for our own goals due to midterm exams and assignments being due before and during reading week. There was also an unexpected issue uploading to Pivotal, which I learned was caused by a lack of a required database driver on the cloud machine. We are going to need to scramble to get everything working for the prototype demo.

### Goals for next week: ###

- Attempt to get some form of prototype working for the demo
- Finish the pdf for the D2 requirements
