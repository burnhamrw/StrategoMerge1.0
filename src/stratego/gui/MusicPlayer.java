package stratego.gui;

import java.io.File;
import java.io.IOException;

import javax.print.attribute.standard.Media;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicPlayer implements Runnable{
	
	private static final String fileName= MainFrame.imageFolderRoot + "\\Music\\StrategoMusic.wav";
	private Clip clip;
	@Override
	public void run() {

		try {
			clip = AudioSystem.getClip();
			AudioInputStream stream = AudioSystem.getAudioInputStream(new File(fileName));
			clip.open(stream);
			clip.start();
			

			
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void close()
	{
		clip.stop();
	}

}
