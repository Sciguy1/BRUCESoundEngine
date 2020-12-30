
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

/**
 * Specifically for longer audio files, OST's, etc.
 * 
 * @author Camron Hughes
 *
 */
public class SoundTrack extends SoundSource
{

	// WaveData trackData = new WaveData();

	public SoundTrack()
	{
		super();
		this.setLooping(true);
	}

	public void makeStereo()
	{

	}

	/*
	 * Based around player position or location. If a 2D game, you can just use x
	 * and y, then set z to 0.
	 */
	public void fadeOut(float x, float y, float z)
	{
		// TODO - FINISHME
		// AL10.alSourcef(sourceID, AL10.AL_GAIN, );

	}

	/*
	 * Based around player position or location. If a 2D game, you can just use x
	 * and y, then set z to 0.
	 */
	public void fadeIn(float x, float y, float z)
	{
		// TODO - FINISHME
	}
}
