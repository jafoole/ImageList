# ImageList

Simple app that grabs image from Google and displays them (as per this example http://cl.ly/text/0E402B1O1o38). Im using this app to test out different Architectural models, 1st+3rd party libraries.



MVP: Im using this Model View Presenter pattern to structure the app display. useful for seperating out the logic of deciding what/when/how to present stuff from the actual implementation of the views. 


Android Support: using these 1st party libraries for layouts and views. very useful for adhearing to Material Design patterns (especially Coordinator Layout which helps 'coordinate' scrolling behaviors across different views)

Butterknife: easy reference of views defined in XML files in Java classes. Im finding very usefull but have not yet explored DataBinding which would make this redundant.

Picasso: image loading. might also look into using Glide

OkHttp: networking

Retrofit: fantastic networking. integrates will with rxJava

RxJava: reactive programming, good for async tasks/delayed execution of code blocks. Currently using to react to Networking events, but could be useful almost anywhere.(High learning curve, need to keep exploring)

EventBus: send messages to any almost any class across thread boundries. 

Parceler: uses annotations to make easily make a class implement Parcelable. useful for saving instance state

Dagger: dependency injection. its really great + easy to use once you get the hang of it

RetroLambda: use Java 8 Lambda expressions. At compile time it generates the classes that the lambdas represent. cuts the ammound of code in half at least.


ISSUE: Presenter is highly coupled to View when theres anything to do with lifecycle. Saving presenter state and registering/unregistering EventBus relies on the View to manage its lifecycle. Preferably there would be some way of doing this outside of the view. Since my Activity is not acting as view or Presenter, maybe lifecycle changes for presenters can be managed here. Problem with that approach is that as of right now, the activity has no handle on the presenters.
 


