package bruce.sound;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;

///
/**
 * Allows the use of names to play certain sound sources with OpenAL. 
 * Should only have one instance of this.
 * 
 * @author Camron Hughes
 * 
 *         The order in which the methods are placed matters, otherwise it may
 *         crash or yield undefined behavior: 
 *         1. setup() 
 *         2. initSoundMap(<allSources>) 
 *         3. setBufferList() 
 *         4. place()
 * 
 *         OR setSources(<resourcePaths>, <allSources>) will do all three
 * 
 *         Note: If you attempt to rewind, play, stop, etc. and get a
 *         NullPointerException, it is more than likely that the name you have
 *         given to a track/method either does not exist or has been named
 *         incorrectly.
 *
 */

public class SoundManager
{

	public HashMap<String, SoundSource> allClips = new HashMap<String, SoundSource>();
	public List<Integer> allBuffers = new ArrayList<Integer>();
	private boolean distanceModelSet = false;

	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	// The amount of buffers and sound clips should remain the same throughout.
	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------

	private long device;

	private long context;

	public void setup()
	{
		try
		{
			init();
			setListenerData();
		} catch (Exception e)
		{

			e.printStackTrace();
		}
	}

	public void init() throws Exception
	{
		this.device = ALC10.alcOpenDevice((ByteBuffer) null);
		if (Objects.isNull(device))
		{
			throw new IllegalStateException("Failed to open the default OpenAL device.");
		}
		ALCCapabilities deviceCaps = ALC.createCapabilities(device);
		this.context = ALC10.alcCreateContext(device, (IntBuffer) null);
		if (Objects.isNull(context))
		{
			throw new IllegalStateException("Failed to create OpenAL context.");
		}
		ALC10.alcMakeContextCurrent(context);
		AL.createCapabilities(deviceCaps);
	}

	/**
	 * Can only be applied ONCE. 
	 * 0 - Linear 
	 * 1 - Exponential 
	 * 2 - Inverse Distance 
	 * 3 - Linear Distance Clamped 
	 * 4 - Exponential Distance Clamped 
	 * 5 - Inverse Distance
	 * Clamped
	 */
	public void setDistanceModel(int choice)
	{

		if (distanceModelSet == false)
		{
			switch (choice)
			{
			case 0:
				AL10.alDistanceModel(AL11.AL_LINEAR_DISTANCE);
				break;
			case 1:
				AL10.alDistanceModel(AL11.AL_EXPONENT_DISTANCE);
			case 2:
				AL10.alDistanceModel(AL11.AL_INVERSE_DISTANCE);
			case 3:
				AL10.alDistanceModel(AL11.AL_LINEAR_DISTANCE_CLAMPED);
				break;
			case 4:
				AL10.alDistanceModel(AL11.AL_EXPONENT_DISTANCE_CLAMPED);
				break;
			case 5:
				AL10.alDistanceModel(AL11.AL_INVERSE_DISTANCE_CLAMPED);
				break;

			}

			distanceModelSet = true;
		}

	}

	/**
	 * Given the SoundSourceLoader is used, this can load resource paths and sound
	 * sources in one go, without the need for separate method calls.
	 * 
	 * @param resourcePaths
	 * @param allSources
	 */
	public void setSources(List<String> resourcePaths, List<SoundSource> allSources)
	{

		this.initSoundMap(allSources);
		this.setBufferList(resourcePaths);
		this.place();

	}

	/**
	 * Loads sound from a wave file into the buffer
	 * 
	 * @param resourcePath
	 * @return
	 */
	public int loadSound(String resourcePath)
	{
		int buffer = AL10.alGenBuffers();
		allBuffers.add(buffer);
		WaveData waveFile = WaveData.create(resourcePath);
		AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
		System.out.println("Current path to .wav : " + resourcePath + " \n Sample Rate : " + waveFile.samplerate);
		waveFile.dispose();
		return buffer;
	}

	/**
	 * Loads all sounds and creates buffers
	 * 
	 * @param sourcePaths
	 */
	public void setBufferList(List<String> sourcePaths)
	{
		// List<Integer> currentBuffers = new ArrayList<Integer>();
		for (int i = 0; i < sourcePaths.size(); i++)
		{
			// Check for a null resource path or non-existent file path
			allBuffers.add(loadSound(sourcePaths.get(i)));
		}

	}

	/**
	 * Helps set up the association with the buffers and sound sources Extremely
	 * important overall, but things keep getting switched around?
	 */
	public void place()
	{
		ArrayList<SoundSource> tempSources = new ArrayList<SoundSource>(allClips.values());

		for (int i = 0; i < tempSources.size(); i++)
		{
			AL10.alSourcei(tempSources.get(i).sourceID, AL10.AL_BUFFER, tempSources.get(i).sourceID);
			// Even though, technically, you should use the allBuffers here, but this works
			// better
			// for playability.

		}

	}

	public void initSoundMap(List<SoundSource> allSources)
	{
		for (SoundSource s : allSources)
		{
			allClips.put(s.getNameID(), s);
		}
	}

	/**
	 * Will clear the buffers and destroy ALC at the end. Note: Should *always* be
	 * called either on close of the game or in a similar vain.
	 */
	public void cleanUp()
	{
		for (int buffer : allBuffers)
		{
			AL10.alDeleteBuffers(buffer);
		}
		ALC.destroy();
	}

	/**
	 * Deletes all sources that were created externally
	 * 
	 * @param allSources
	 */
	public void deleteAll(List<SoundSource> allSources)
	{
		for (SoundSource s : allSources)
		{
			s.delete();
		}
	}

	/**
	 * Initializes the listener's perspective to all 0s.
	 */
	public void setListenerData()
	{
		AL10.alListener3f(AL10.AL_POSITION, 0, 0, 0);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
	}
	// ------------------------------------------------------------------------------//

	/**
	 * Plays the specified clip based on the sound source's name.
	 * 
	 * @param nameID
	 */
	public void playClip(String nameID)
	{
		allClips.get(nameID).play();
	}

	/**
	 * Pauses the specified clip based on the sound source's name.
	 * 
	 * @param nameID
	 */
	public void pauseClip(String nameID)
	{
		allClips.get(nameID).pause();

	}

	/**
	 * Stops the specified clip based on the sound source's name.
	 * 
	 * @param nameID
	 */
	public void stopClip(String nameID)
	{
		allClips.get(nameID).stop();
		allClips.get(nameID).setPlaying(false);

	}

	/**
	 * Pauses the current clip, rewinds it, and then will play the next clip.
	 * 
	 * @param nameOne
	 * @param nameTwo
	 */
	public void swapPlaying(String nameOne, String nameTwo)
	{
		// TODO -- Factor in any delay or fadeIn booleans
		allClips.get(nameOne).stop();
		allClips.get(nameOne).rewind();
		// allClips.get(nameTwo).rewind();
		allClips.get(nameTwo).play();
	}

	/**
	 * Will find the currently playing track, stop it, rewind it, and play the other
	 * track.
	 * 
	 * @param otherTrack
	 */
	public void swapPlaying(String otherTrack)
	{
		// TODO -- Factor in any delay or fadeIn booleans
		for (SoundSource s : allClips.values())
		{
			if (s.isPlaying())
			{
				s.stop();
				s.rewind();
			}
		}

		allClips.get(otherTrack).play();

	}

	/**
	 * Allows the clip to have a certain fade time, either depending on the distance
	 * attenuation or similar factors.
	 * 
	 * @param buffer
	 */
	public void setFadeClip(String name)
	{

	}

	/**
	 * Rewinds all audio sources in buffer that are not currently being played.
	 * 
	 */
	public void rewindAllUnplayed()
	{
		for (SoundSource s : allClips.values())
		{
			if (s.isPlaying == false)
			{
				s.rewind();
			}
		}
	}

	public void rewindClip(String name)
	{
		allClips.get(name).rewind();
	}

	// Info Methods//
	/**
	 * Provides information on what sources are currently being played.
	 */
	public void currentlyPlaying()
	{
		System.out.println("-------------------------");
		System.out.println(" Currently playing: ");
		for (SoundSource s : allClips.values())
		{
			if (s.isPlaying())
			{
				System.out.print(s.toString() + "\n");
			}
		}
		System.out.println("-------------------------");
	}

	/**
	 * Provides information on what sources are currently being used in all clips.
	 */
	@Override
	public String toString()
	{

		StringBuilder build = new StringBuilder();
		build.append("\n-------------------------");
		build.append("\n All current sound sources: \n");
		for (SoundSource s : allClips.values())
		{
			build.append(s.toString() + "\n");
		}
		currentlyPlaying();
		build.append("-------------------------\n");
		return build.toString();
	}

}
