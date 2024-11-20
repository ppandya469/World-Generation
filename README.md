# World Generation
<a href="https://github.com/github_username/repo_name">
    <img width="1278" alt="Screenshot 2024-05-25 at 3 56 43 PM" src="https://github.com/ppandya469/World-Generation/assets/141596211/f7a40a6c-7010-4033-905a-7530580bc3dd">
</a>

Implements a 2D array data structure to house a tile-based, interactive world exploration engine.  
Uses text-based tiles along with pseudo-randomized walls, hallways, and rooms to trap and kill players. Players must enter the trap rooms and collect all visible coins within 10 seconds to win the game.  
Also includes additional features like character swapping, HUD tile hovering, pause, reload, and quit options available in the main menu and throughout the game.

---

## Demo

[Watch the Demo](https://github.com/ppandya469/World-Generation/assets/141596211/a97be9b2-bb8b-4dc6-86a2-6b700d1f84c5)

---

## How It's Made:

**Tech Used:** Java  

### Development Process:
1. **2D Array World Design:**  
   The game world is represented using a 2D array, where each cell corresponds to a "tile." Tiles can represent walls, rooms, hallways, or interactive elements like coins and traps.  

2. **Randomized World Generation:**  
   Pseudo-random algorithms generate unique worlds each time the game starts. This includes creating interconnected hallways, hidden rooms, and strategically placed traps and coins.  

3. **Player Interaction:**  
   Players navigate the world using keyboard controls. Game mechanics include collecting coins, avoiding traps, and managing the timer.  

4. **Game Features:**  
   - **Main Menu:** Options to pause, reload, quit, or adjust game settings.
   - **HUD Interaction:** Tile-hovering capability displays relevant information about a specific tile, enhancing immersion.  
   - **Character Swap:** Allows players to switch between avatars mid-game for a personalized experience.  

5. **Timer and Win Condition:**  
   Players must collect all visible coins within 10 seconds to win. The timer dynamically updates on the HUD for real-time feedback.  

---

## Optimizations

1. **Efficient World Rendering:**  
   Initially, the rendering algorithm caused noticeable lag for large worlds. Refactored the code to only update visible tiles instead of the entire grid, significantly improving performance.  

2. **Memory Management:**  
   Optimized object creation by reusing tile objects for static elements like walls and hallways, reducing memory overhead.  

3. **Algorithm Improvements:**  
   Enhanced the pseudo-random generation algorithm to prevent overlapping rooms and disconnected hallways, ensuring seamless navigation.  

4. **Debugging Tools:**  
   Added a developer mode to visualize the 2D array, highlighting key areas such as traps and coin placements, which streamlined testing and debugging.  

---

## Lessons Learned:

1. **Dynamic Problem-Solving:**  
   Building a 2D world from scratch revealed challenges in designing randomized yet coherent environments. It emphasized the importance of balancing randomness with playability.  

2. **Algorithmic Thinking:**  
   Developing the generation logic for hallways and rooms strengthened my understanding of pathfinding and grid-based algorithms.  

3. **User-Centric Design:**  
   Incorporating features like tile-hovering and a customizable HUD highlighted the value of user feedback and accessibility in game design.  

4. **Debugging in Complex Systems:**  
   Debugging the interaction between tiles, traps, and the timer taught me to break down problems into smaller, manageable parts and systematically test solutions.  

This project was a rewarding experience that showcased the intersection of creativity and logic in game development. It reinforced my interest in designing interactive systems and provided valuable skills for future endeavors.
