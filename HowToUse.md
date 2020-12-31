# How to Use


Example code from testClass:
  	SoundManager soundManager = new SoundManager();
  	soundManager.setup();
  	SoundSourceLoad soundLoader = new SoundSourceLoad();
  	soundLoader.loadPaths("src\\game-tracks.txt");
  	soundManager.setSources(soundLoader.extractPaths(), soundLoader.allSources);
    
  SoundLoader will load sources from a text file with the following format for each line in the text file: <trackName>, <resourcePath>, <canLoop>.
  Modify the game-tracks.txt file accordingly, as the files used in the demo video will or have been deleted from the repo.
  You should add every .wav file you are going to use in your project early on, and later call the following at the termination of your game or engine: 
   
  soundManager.deleteAll(soundLoader.allSources);
  soundManager.cleanUp();
    
  These need to be called to "flush out" any additional resources, buffers, etc. OpenAL might be using/handling.
  (There is a warning that pops up right now, but ignore it for now.)
  
  
  (Will try to fix this problem soon with either Maven or something similar)
  
   Important Reference libraries/jars to include in your build path:
   
    lwjgl.jar
    lwjgl-natives-windows.jar (or whatever your OS might be)
    lwjgl-openal.jar
    lwjgl-openal-natives-windows.jar
  
