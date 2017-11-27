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

This week was dedicated to fine tuning the idea for the project, getting all of the team mates on the same page, and preparing the D1 Deliverable Presentation and documentation.

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


## Dev Log #7: Week of November 1st ##

### General description: ###
 
My main goal with my group for this week was to at minimum have some sort of demo working for our D2 Demo, and to complete the finish writing up the D2 document.

### What was completed? ###

This was largely successful, since we finished our D2 document and completed the main portion of our app. There was a large setback, however. The setback was our groups inability to get the server uploaded to the cloud resource for the purpose of successfully demoing all the back end functionality. 

### Comments: ###

The setback this week happened for two reasons. The first being that we did not have the database driver for the SQLite database, which cause the DB to be unable to function initially. The second issue is that we ran out of data for the cloud resource as a result of pushing our app so many times. We had to push the app continually to debug the first issue, and since we are using a free account, we did not know that Pivotal would lock us out at a certain data limit. 

This setback was unfortunate because it prevented us from demoing the extent to which our work was completed in the prototype demo. 


### Goals for next week: ###

- Attempt to finish all outstanding work from the previous weeks
- Make sure to upload the app to a cloud resource


## Dev Log #8: Week of November 8th ##

### General description: ###
 
My main goal for this week was to finish all of the outstanding working that has been building up for several weeks before our prototype demo. This included finishing the sorting based on a deadline, refining existing REST calls required for our apps core functionality and adding in PUT and DELETE requests for deleting Environments. I also needed to load the app to the Pivotal cloud resource.

### What was completed? ###

There was quite a lot of progress made onto theses requirements during this week. I implemented the majority of the code for the automatic sorting and refined the existing REST calls. Refining existing REST calls involved moving the existing functionality from Database compatible data structures (structures like lists and maps) to those designed specifically for our app. I was unable to get to the required PUT and DELETE requests since it is taking and extended amount of time to refine and finish the other requests.

Another major accomplishment was getting our app appropriately uploaded to the Cloud Resource. I ended up making another free account using our Groups email account, which allows to have several more free GBs of space to test and use our app. 

### Comments: ###

The main setbacks in refining existing REST calls revolve around creating the Database Helper class for updating the calls to a model used by the REST component and the front end. It is very time consuming to write code to appropriately parse and to test the DB Helper class. Additionally, as the Helper is being written, we Brad and I are also discovering issues with the DB that need to be rectified. For example, when adding a user, the database interface was not actually storing the user.

The issues with the DB stuff is creating a large slow down in work progress, b/c I have to wait for Brad to adjust the issues, but he is working both in school and outside, so sometimes it takes him a long time to address the issues. 


### Goals for next week: ###

- Finish the remaining REST calls
- Look into implementing more Design patterns 


## Dev Log #9: Week of November 15th ##

### General description: ###
 
The main purpose of this week was to finally finish the last two types of required REST calls and to look into implementing more design patterns into our app.

### What was completed? ###

This week was successful for the most part in terms of identifying design patterns, but I was unable to touch the last few REST calls yet. I identified that the Database helper class is actually following the Adapter Design pattern, which gives an added incentive to finish it. I also helped to rationalize that our app is divided into Client-Server, Layered and REST architectures. This was a very productive week in terms of preparing for the Oral Exam.

### Comments: ###

There are continued slow downs as a result of the conversations between Brad and I.


### Goals for next week: ###

- Prepare for the oral exams by writing D3, creating diagrams, creating a presentation and having review sessions with out group


## Dev Log #10: Week of November 22nd ##

### General description: ###
 
The main purpose of this week was to prepare for our oral exams.

### What was completed? ###

This week accomplished everything that was required. All the items for the oral exam were completed, including: writing the D3 Doc, creating the presentation for our oral exam and having multiple practice sessions with my group mates to prepare for questions that may be asked in the Oral exam.

### Comments: ###

There are no comments to make at this time.

### Goals for next week: ###

- Finish everything


## Dev Log #11: Week of November 29th ##

### General description: ###
 
The main purpose this week is to finish my portion of the entire app and prepare for our D4 deliverable.

### What was completed? ###

This week is going quite well. The functionality for the Server and Database Component are completed. I worked on resolving issues with the sorting algorithm for deadlines and made some refinements to the original sorting algorithm since it wasn't working appropriately. I also finished up implementing on the security aspects of the Server (ie. preventing users without a password from accessing the protected Environment, etc.). We seem to have a Server completed that can support the expected functional requirements of our app.

### Comments: ###

There were a two unexpected occurrences this week. The first was that the original sorting algorithm that we made for our app was not working appropriately. The app worked when choosing optimal options for a single user, but did not optimize the options for all users. This issue meant that if we chose the wrong user to start with the outputed groups would not be optimal. The second was that the I accidentally deleted the configuration file for the cloud version of the Server (as opposed to local). This issue was quite the setback as I had to recreate the configuration file, which took quite a long time original. Despite their unexpected nature, these setbacks were both solved. 

### Goals for next week: ###

- DONE!
