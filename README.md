Architectural Reqs: 
	DB-driven (scores and updateable splash screen instructions)
	JSON format (data) from UI to backend and vice-versa
	One UI *main* screen for game play (no popups)
	User-friendly design without the need for scrolling to play

Server Design:
	MySQL database with 2 tables:
		Splash screen instructions
		Scores (includes index: auto-increment, In-Game-Name, and Score)
	PHP backend to parse JSon from/to UI and Database

Platform: Android/Java runtime environment developed using Windows 7 OS and Android Studio IDE

___________________________________________________


The general design:
1) There are 4 screens/activities (see /main/java/com/android/codebreaker/*activity.java). 
2) There are 2 list adapters and respective row data managers. These are needed to handle the 2 lists in the app (game progress history and scores listing).
3) A few visual/text resource files (see /main/res).

Why build it?

This was my first Android app. I wanted to see just how easy/difficult it was to develop an app with only a few requirements. These requirements included
JSon parsing on the client, Server-side (db) interaction, and a few built in Android UI components (including a list). The entire project life-cycle from design to "Ship It" was about a month part-time. 

What did I encounter?

1) I was impressed with how well an Android app injested and parsed JSon data. The native libraries made accomplishing this a snap.
2) The Android Studio IDE was, well, ok. It certainly wasn't WYSIWYG. But, the current version of this IDE is much improved in this regard and the hardware emulators really work.
3) Compiling and debugging/testing was great and quick. (Yay Gradle)
4) I love the asynchronous feature (Android 2+) for server-side interaction. Big plus.
5) I'm certainly not a designer AT ALL, but I did like the speed in which my Android devices I tested with drew those graphics on the screen.

Conclusion: Building an Android app is fun and easy (assuming the developer has Java experience and is willing to spend some time ramping up/learning Android libs). Once a user *gets*
how all the pieces fit together building an app is not difficult, at all. (ie; I pretty much had the same amount of hair in the end as when I started)
