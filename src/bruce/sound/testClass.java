package bruce.sound;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.openal.ALC;

public class testClass
{

	public static void main(String[] args)
	{

		SoundManager soundManager = new SoundManager();
		soundManager.setup();
		SoundSourceLoad soundLoader = new SoundSourceLoad();
		soundLoader.loadPaths("src\\game-tracks.txt");
		soundManager.setSources(soundLoader.extractPaths(), soundLoader.allSources);

		char c = ' ';

		while (c != 'q')
		{
			try
			{
				c = (char) System.in.read();
			} catch (IOException e)

			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			switch(c) {
			case 'p':
				if(soundManager.allClips.get("Megolovania").isPlaying()) {
					soundManager.allClips.get("Megolovania").pause();
				}
				else {
				
					soundManager.allClips.get("Megolovania").play();
				}
			 break;
			
			case 'r':
			    soundManager.allClips.get("Megolovania").rewind();
			    break;
			case 'i':
				soundManager.swapPlaying("TSFH");
				break;
			 
			 
			}
			
		}

//		// NOTE:
//		// Issue with trying to close the OpenAL device for some reason?
//		// The cleanUp method should take care of this, but it seems this is not
//		// working or the warning won't go away?
		soundManager.deleteAll(soundLoader.allSources);
		soundManager.cleanUp();

	}
}
