# morning-ritual-app
A daily morning ritual habit tracker andorid app group project for CSc 317 - Mobile Development

**Group Members:** Victor Gomes, Marko Kreso, and Abhishek Agarwal

## Overview
It keeps track of the morning routine that the user wants to keep track off every morning, with the ability to see their progress. When first opening the app they would be able to customize the things they want to keep track of everyday and see a help page. After that, every time they open the app, they will be able to check off the things in their morning ritual checklist and also take a picture for it. When they complete their daily routine an API call would bring up productivity and motivational quotes from the web, showing their streak and progress. They will also have the ability to share their progress with any of their contacts on their phone.

## UI Development Plan
The usual user interaction in the app will be, open the app after completing their morning ritual, check off the things that they did that day and take a picture of themselves completing it. They can then click complete to submit that to their statistics, bringing up a motivational quote and their stats so far.

![Image of UI plan](https://i.imgur.com/JcukOwD.png)

**Page 1:** The homepage is where the user will be spending most of the time, we can see that they will be able to check off their morning routine, take a picture, see the statistics, access the help page and complete their morning ritual.

**Page 2:** The statistics page will have a scrollable list view with every day that the user completed their daily ritual. Also a dropdown to select between weekly, monthly or yearly statistics, followed by the graph with the statistics and an option to share the graph.

**Page 3:** The daily ritual retrospective page, it opens when the user clicks in one of the daily retrospective buttons in the statistics page (Page 2), It will show the picture the user took that day (if any) and the things that they accomplished in a list.

**Page 4:** The completed ritual page, will gather a random motivational quote and include the monthly statistics graph, as well as the ability to share the graph.

**Other Pages:** Some other pages that are not drawn are the customization page that will customize their ritual and the help page, showing how to use the app.

## Technical Details Development Plan
**General:** We will try targeting API level 24 and a resolution of 1080x1920 and 1440 x 2880.

**To-do Fragment:** The app’s main display/todo fragment all the activities that a user has to do will be presented in a list view presenting each task. This list view will be attached to an adapter so that system resources can be preserved. The main page itself will be presented as a fragment. There will be buttons such as statistics, help, and “complete” these will place the current home/todo fragment on the backstack and replace fragment with a different UI. This would allow UI changes efficiently while also allowing the user to visit a previous fragment using the Android back button if they wish. There will be a picture button that will start an explicit intent using Media Store for the camera and that pictures bitmap will then be saved in the system for later use. 

**Help Fragment:** The help fragment will just give the user some info on how to use the app in a simple TextView. 

**Statistics Fragment:** The statistics button would launch a statistics page this page will feature a history of the users progress this will be presented in a listview using a ArrayAdapter to display the dates each ritual was completed. Selecting one of the dates will launch a summary page fragment describing what the user did that day. 

**Congratulations Fragment:** After the user finishes their todo list a congratulations fragment will popup congratulating the user on their achievements in a TextView and it will fetch an inspirational quote using a quotes API this api will retrieve the api using a JSON and we will likely parse through data to grab the quote and the author if available and present that in a TextView. The api works through a URL where we can specify what category of quote we wish to present which will be “inspire” in the case of this project. An AsynchTask will likely download this quote so that our API remains responsive. We will likely use another api to build and display a graph for the user will be able to share using a content provider (FileProvider) to grab the path of the image of the graph and an implicit intent to allow the user to select which application the user wishes to share this image with. 

