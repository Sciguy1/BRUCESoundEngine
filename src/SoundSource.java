package bruce.sound;

import org.lwjgl.openal.ALC11;

import java.awt.event.ActionEvent;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

/**
 * For in-game sound effects. Most, if not all, sources should be in mono for
 * placement in order to work correctly for position parameters. If you don't
 * care about position or any of those things, go ahead with stereo.
 * 
 * @author Camron Hughes
 *
 *         Used the following tutorials as a base:
 *
 */
public class SoundSource
{

	protected int sourceID;

	private String soundName;
	public String resourcePath;

	public boolean isPlaying;
	private boolean canFade = false;
	private boolean isLooping = false;

	private boolean hasAttenuation = false;

	public String getPath()
	{
		return resourcePath;
	}

	public boolean isLooping()
	{
		return isLooping;
	}

	public SoundSource()
	{
		sourceID = AL10.alGenSources();
		this.setDefault();
	}

	public SoundSource(String nameID, String resourcePath)
	{
		sourceID = AL10.alGenSources();
		this.resourcePath = resourcePath;
		this.soundName = nameID;

	}

	public String getNameID()
	{
		return soundName;
	}

	public void setNameID(String nameID)
	{
		this.soundName = nameID;
	}

	public void play()
	{
		AL10.alSourcePlay(sourceID);
	}

	/**
	 * Pauses the audio source.
	 */
	public void pause()
	{
		AL10.alSourcePause(sourceID);
	}

	/**
	 * Stops the current audio source from playing any further.
	 */
	public void stop()
	{
		AL10.alSourceStop(sourceID);
	}

	/**
	 * Will continue playing the audio source if it is not paused.
	 */
	public void continuePlaying()
	{
		AL10.alSourcePlay(sourceID);

	}

	/**
	 * Obtains the sourceID of the source.
	 * 
	 * @return
	 */
	public int getSourceID()
	{
		return sourceID;
	}

	/**
	 * Stops the audio source and proceeds to use the sourceID to delete the audio
	 * source.
	 */
	public void delete()
	{
		stop();
		AL10.alDeleteSources(sourceID);

	}

	/**
	 * Sets Gain/Volume of the current audio source
	 * 
	 * @param volume
	 */
	public void setVolume(float volume)
	{
		AL10.alSourcef(sourceID, AL10.AL_GAIN, volume);
	}

	/**
	 * Set whether or not the current audio source should be looping automatically
	 * 
	 * @param loopCondition
	 */
	public void setLooping(boolean loopCondition)
	{
		AL10.alSourcei(sourceID, AL10.AL_LOOPING, loopCondition ? AL10.AL_TRUE : AL10.AL_FALSE);
		this.isLooping = loopCondition;
	}

	public boolean isPlaying()
	{
		return AL10.alGetSourcei(sourceID, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}

	public void setPlaying(boolean newChoice)
	{
		this.isPlaying = newChoice;
		// return AL10.alGetSourcei(sourceID, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}

	/**
	 * Can set the position of where the source is being played (left or right) with
	 * the z being the in-between (going from either left to right or the other way).
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setPosition(float x, float y, float z)
	{
		AL10.alSource3f(sourceID, AL10.AL_POSITION, x, y, z);

	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setDirection(float x, float y, float z)
	{
		AL10.alSource3f(sourceID, AL10.AL_DIRECTION, x, y, z);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setVelocity(float x, float y, float z)
	{
		AL10.alSource3f(sourceID, AL10.AL_VELOCITY, x, y, z);
	}
	
	

	/**
	 * All initial defaults for the sound source are set here: gain 1, pitch 1 and
	 * position (0, 0, 0).
	 */
	public void setDefault()
	{
		AL10.alSourcef(sourceID, AL10.AL_GAIN, 1);
		AL10.alSourcef(sourceID, AL10.AL_PITCH, 1);
		AL10.alSource3f(sourceID, AL10.AL_POSITION, 0, 0, 0);
	}

	/**
	 * Sets the pitch of the source.
	 * 
	 * @param pitch
	 */
	public void setPitch(float pitch)
	{
		AL10.alSourcef(sourceID, AL10.AL_PITCH, 1);
	}

	/**
	 * Gives whether or not the source is affected by distance attenuation.
	 * 
	 * @return
	 */
	public boolean hasAttenuation()
	{
		return hasAttenuation;
	}

	/**
	 * Gives whether or not the source is affected by distance attenuation.
	 * 
	 * @return
	 */
	public void setAttenuation(boolean newAttenuation)
	{
		this.hasAttenuation = hasAttenuation;
	}

	/**
	 * If the attenuation model has been set with SoundManager and the source
	 * has been set up to accept attenuation, (affects gain of the source), 
	 * then the following can be applied to the source:
	 *  
	 * @param rollOffFactor - determines how quickly the gain decreases as distance increases.
	 *  (No attenuation means setting rollOffFactor to 0)
	 * @param referenceDistance - distance at which the gain will equal 1
	 * @param maxDistance - distance that the source stops being heard
	 */
	public void distanceAttenuation(int rollOffFactor, int referenceDistance, int maxDistance) {
		
		if(hasAttenuation) {
			 AL10.alSourcef(sourceID, AL10.AL_ROLLOFF_FACTOR, rollOffFactor);
		     AL10.alSourcef(sourceID, AL10.AL_REFERENCE_DISTANCE, referenceDistance);
		     AL10.alSourcef(sourceID, AL10.AL_MAX_DISTANCE, maxDistance);
		}
	    
	}

	/**
	 * Sets the resourcePath for the source. Should only be used sparingly.
	 * 
	 * @param string
	 */
	public void setPath(String currentPath)
	{

		this.resourcePath = currentPath;
	}

	/**
	 * Stops and rewinds the current source.
	 */
	public void rewind()
	{
		stop();
		AL10.alSourceRewind(sourceID);

	}

	// Might have and might not
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((soundName == null) ? 0 : soundName.hashCode());
		result = prime * result + ((resourcePath == null) ? 0 : resourcePath.hashCode());
		result = prime * result + sourceID;

		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof SoundSource)
		{
			SoundSource mySource = (SoundSource) obj;

			if ((mySource.getSourceID() == this.sourceID) && (mySource.resourcePath.equals(this.resourcePath)
					&& (mySource.getNameID().equals(this.soundName))))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString()
	{
		return " SoundSource [sourceID=" + sourceID + ", soundName=" + soundName + ", resourcePath=" + resourcePath
				+ "]";
	}

	// TODO ??
//    public void delay() {
//    	
//    }

	// Specific to entities in CheckEngine

//	//CAREFUL!
//	public void switchSource(SoundSource newSource) {
//		if(newSource != null) {
//			this.sourceID = newSource.sourceID;
//		}
//	}

}
