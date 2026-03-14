# Test documentation
We have three separate test files for the three different activities we created,
meaning the classes in the "activities" folder: MainMenu.kt, Gallery.kt, Quiz.kt.
Both the Android framework "Espresso" has been used, as well as the Compose Testing API, when making these tests.

- In MainMenuTest.kt:

This class uses Espresso, and not Compose Testing API.
Here we test for if the two buttons redirect to the correct page (or at all).
We listen for navigation intents and match them to the correct activity for each one.
The test "openGallery" clicks the gallery button and check if we arrive at the Gallery activity.
The "openQuiz" test clicks the quiz button and check if we arrive at the Quiz activity.
Both of the tests pass.

- In QuizTest.kt:

This class usees Compose Testing API, and not Espresso.
Here we test if the score at the end of the quiz is accurate to how we performed.
To do this we have added testTags in Quiz.kt, which all three tests use.
The first test checks if 1 correct and 2 wrong answers gives the correct score of 1/3.
The second test check for 2/3, and the third test for 3/3 (using the same technique).
All three tests pass.

- In GalleryTest.kt:

This class uses both Espresso and the Compose Testing API.
It contains tests for checking image count after add/delete operations.
The first test checks by pressing the "add" button in the gallery, 
then we supply an image URI from our drawable resource, set the answer and check for count again (should be original count + 1).
It is executed with intent stubbing, so we don't have to actually leave the activity to do the operation.
The second test presses the "Delete" button, choosing an image to delete, then checking that the new count is the original count - 1.
Both of the tests passed.