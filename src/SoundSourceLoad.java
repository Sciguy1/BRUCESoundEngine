package bruce.sound;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

/**
 * Loads the name of the sound source and the resource path from a text file.
 * 
 * @author Camron Hughes
 *
 */
public class SoundSourceLoad
{
	// public SoundHandler soundHandle = new SoundHandler();
	public List<SoundSource> allSources = new ArrayList<SoundSource>();
	public List<Integer> bufferList = new ArrayList<Integer>();
	public SoundManager soundManager = new SoundManager();

	/**
	 * Will load source paths from .txt file into an ArrayList of resourcePaths,
	 * then we can set properties for each track file within the there after Should
	 * only be called once!
	 * 
	 * Format <NAME>, <PATH>
	 * 
	 * Later : <NAME> <PATH> <SOUNDTYPE> <FADE>
	 */
	public void loadPaths(String textFile)
	{

		FileReader fr;
		BufferedReader br;
		try
		{
			fr = new FileReader(new File(textFile));
			br = new BufferedReader(fr);

			String line = " ";
			String[] subLine;

			line = br.readLine();
			while (line != null)
			{

				subLine = line.split(", ");

				instPath(subLine);

				line = br.readLine();
			}
			br.close();
			fr.close();

		} catch (FileNotFoundException e)
		{

			e.printStackTrace();
		} catch (IOException e)
		{

			System.err.println(
					"Unable to load sources from text file. Please check your resource path for your txt file.");
			e.printStackTrace();
		}

	}

	/**
	 * Initializes any buffers needed for sound resources. By default, all
	 * SoundSource do not loop, but can be changed on a case by case basis later on.
	 */
	private void instPath(String[] subLine)
	{
		// Create list of sources
		if (subLine.length > 5)
		{
			// Can't do anything
		} else
		{
			// TODO - Name should be unique for each track!
			SoundSource currentSource = new SoundSource();

			currentSource.setNameID(subLine[0]);

			currentSource.setPath(subLine[1]);

			if (subLine[2].equals("true"))
			{
				currentSource.setLooping(true);
			} else
			{
				currentSource.setLooping(false);
			}

			allSources.add(currentSource);

		}

	}

	/**
	 * Extracts the needed resourcePaths to setup buffers for SoundManager.
	 */
	public List<String> extractPaths()
	{

		List<String> resourcePaths = new ArrayList<String>();

		for (int i = 0; i < allSources.size(); i++)
		{
			resourcePaths.add(allSources.get(i).getPath());
		}

		return resourcePaths;
	}

	/**
	 * Provides info on all sources that have been loaded from the text file.
	 */
	public void info()
	{
		System.out.println("Sound Loader information: ");
		for (SoundSource s : allSources)
		{
			System.out.println(s.toString());
		}
	}

}
