# Angry-Birds

## Project Description

This project is a replica of the Angry Birds game, developed in **Java using the libGDX framework** for desktop. 
<br>
Made by [Rishabh Jakhar](https://github.com/Rishabh4Jakhar/) and [Advik Gupta](https://github.com/NOVA2OP) as part of the **Advanced Programming 2024 course**.
<br><br>
The game features three levels, three types of birds, three types of pigs, and three types of materials. 
All the levels are simple enough to solve. The game is designed to be easy to play and understand, with the main goal being to destroy all the pigs in each level using the birds provided.
## How to Run

To run the project, follow these steps:

1. **Clone the repository**:
    ```sh
    git clone https://github.com/Rishabh4Jakhar/AngryBirds.git
    cd AngryBirds
    ```

2. **Import the project into your IDE**:
    - Open IntelliJ IDEA.
    - Select "Open" and navigate to the cloned repository.
    - Open the `build.gradle` file to import the project.

3. **Run the game**:
    - Navigate to the `lwjgl3` module.
    - Run the `DesktopLauncher` class.

4. **Build the project**:
    ```sh
    ./gradlew build
    ```

5. **Clean the project**:
    ```sh
    ./gradlew clean
    ```


## Assumptions

- JUNIT TESTING IS NOT IMPLEMENTED AS IT WAS NOT ABLE TO BE LOADED IN LIBGDX AND CLASS COMMENTS FOR THE SAME WERE NOT ANSWERED EVEN AFTER THE DEADLINE.
- The game is designed to be simple and easy to play.
- Only three levels are created.
- Three birds are created: RedBird, YellowBird, and BlueBird.
- Three pigs are used: normal pig, chief pig, and king pig. 
- Three types of materials are used: wood, ice, and stone. 
- To save a level, press the `I` key.
- On pressing `I` key, game is saved and can be loaded from the load menu screen in the main menu. 
- Current level, no of levels completed, and progress within current level, i.e, no of birds left and no of pigs left are saved and correctly loaded using ObjectOutput and ObjectInput streams (serialization).
- If pigs are rolling on the ground with a very low velocity for more than 2 seconds, they are killed.
- Birds/Pigs are killed if they go out of the screen.
- Pigs take damage on falling to the ground or being hit by birds calculated using velocity and mass.
- If all birds are used and level is not completed, game waits for 3 seconds before displaying level failed screen.
- A reset button is present in load menu if you want to unload the currently loaded game.
- Levels are locked and can be unlocked by completing the previous level.

## Bonus Features

- Yellow Bird: When the screen is touched, the bird **speeds up** and does more damage.
- Blue Bird: When the screen is touched, the bird **splits into three** smaller birds.
- TNT: When the TNT is hit, it **explodes** and damages all the pigs/blocks within a certain radius.

## Demo Video
[Youtube Video](https://www.youtube.com/watch?v=MskBtYtXlBQ)

## Preview
- **Main Menu**
<br>
<img src="https://github.com/user-attachments/assets/7ace0db9-3ffc-4f40-8c9c-997b2e13b221" height="300" width="533">

- **Level Selection Screen**
<br>
<img src="https://github.com/user-attachments/assets/c1a279c0-6147-4a97-8167-ee4873e09ae3" height="300" width="533">


- **Level 1 Preview**
<br>
<img src="https://github.com/user-attachments/assets/6d2781f3-6512-4680-87e3-897b31dbadac" height="300" width="533">

- **Level 2 Preview**
<br>
<img src="https://github.com/user-attachments/assets/a7017042-7cc8-4181-9515-71c18e395f83" height="300" width="533">

- **Level 3 Preview**
<br>
<img src="https://github.com/user-attachments/assets/0bf38e65-9371-4f23-ba75-5613c9c58829" height="300" width="533">

- **Load Menu**
<br>
<img src="https://github.com/user-attachments/assets/36b0d459-5809-4f0d-b174-adf8c332ef49" height="300" width="533">




## Online References

- [libGDX Documentation](https://libgdx.com/documentation/)
- [libGDX Official Github](https://github.com/libgdx/libgdx/)
- [gdx-liftoff](https://github.com/libgdx/gdx-liftoff)
- [Brent Aureli Code YouTube Playlist](https://www.youtube.com/watch?v=a8MPxzkwBwo&list=PLZm85UZQLd2SXQzsF-a0-pPF6IWDDdrXt&index=1)
- [LibGDX Beginner Tutorial](https://colourtann.github.io/HelloLibgdx/index.html)
- [Spriters Resource](https://www.spriters-resource.com/search/?q=Angry+Birds)
