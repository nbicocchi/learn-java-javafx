# 2D Tree Fractals

## What is a Tree Fractal?

Tree Fractals are geometric objects created using recursive algorithms (most of the time) and widely known for their creative and unusual shapes.
Here are some key features and concepts of fractal trees:

* `Self-similarity`: Every part of the tree resembles the entire tree. This is a fundamental property of every fractal.

* `Recursion`: As previously mentioned, fractal trees can be generated using recursive procedures, where a function calls itself to draw increasingly smaller branches.

* `Geometry`: A fractal tree is often drawn with fixed angles and constant scale ratios. For example, each branch might be a certain percentage of the length of the previous branch and oriented at a fixed angle to it.

* `Complexity`: Despite the simplicity of the rules that generate them, fractal trees can have extremely complex and detailed structures.

* `Applications`: Fractal trees can be used in various fields, including computer graphics to create realistic natural images (such as trees and plants), physics, biology, and art.

## How does this app work?

If you have never heard about these particular geometric figures, there is nothing better than creating one yourself to get to know them!
This app offers you just this, but with a little help form the developers and a little bit of spice just to make the experience worth your time.

As the application starts you can see there are some `Controls` that you can use to construct your own tree, such as:

* `Left Angle Spinner` and `Right Angle Spinner`: These two will determine the rotation each branch of the tree will have and are fundamental to make the tree possess a unique and captivating pattern.
  You can choose to let them be the same or you can have some fun experimenting with strange combinations of angles. You can choose angles between 0 and 150 degrees!

You could even stop here if you wanted to but if we add just two more features, the trees will become much more fun to play with!
These are the features previously mentioned:

* `Random Color Choice`: If you select this checkbox, your tree will have a semi-randomic color. Sometimes it can be bothersome to pick a color yourself, so it's been decided that this feature was a must have.
* `Pick Your Color`: Obviously to have a more complete experience, you can't miss on the opportunity to pick your favourite color and see what the tree will turn out to be!

The trees will be pretty plain if they were of just one color though, but do not worry because at every iteration (every branch) the color will be derived from the previous and it will also be slightly brighter!

At last i guess no one would want the trees to just spawn out of nowhere and that's why we added some simple animations, so that the tree can create itself when you click the button `Draw` (which will be unavailable until you choose a color)!
Obviously the time it takes for every branch to be created can be decided using the `Duration Spinner`, from a minimum of one second to maximum of three!

If by any chance you want to make the creation of the tree more fast and dynamic, you can also use the last `Textfield` on the righten side of the application.
This combined with the `Load` button will change the configuration of all the `Controls` just by writing a simple `String` that has to have these parameters:

  * `COLORNAME`: A string containing the name of a color (ex: red, green, blue, teal ...)
  * `LEFTANGLE` and `RIGHTANGLE`: Two integer numbers between 0 and 150.
  * `ANIMATIONDURATION`: An integer number between 1000 and 3000.

An example of valid string would be `green.20.130.1500`. Every parameter MUST be separated by a full stop (`.`). You can always save these strings on your computer for future purposes!

Last but not least, if you don't have any ideas, there is a series of configurations under the title `Choose A Tree Fractal` that can be a good starting point.
They are already beautiful as they are but with a touch of your imagination, they will surely become more and more unique! 

Now with all this information, you can start building your `2D Tree Fractal` right away!

Be sure to enjoy this application and let others know of this too, so that you can share you creations and have fun together!

## Authors

* `Luca Ferraro` (lucadamiano2003@gmail.com)
* `Mattia Gualtieri` (mattiagua03@gmail.com) 

