/**
 * 
 */
package abyssworld.app;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import abyssworld.interfaces.GameScreenInterface;
import abyssworld.interfaces.ScreenState;
import abyssworld.objects.FinalScreen;
import abyssworld.objects.IntroductionScreen;
import abyssworld.objects.Level1Screen;
import abyssworld.objects.Level2Screen;
import abyssworld.objects.StaticScreen;

/**
 * @author Raishin
 *
 */
public class AbyssWorld {


	private int current_level = 0;
	final int width = 1440;
	final int height = 900;

	private List<GameScreenInterface> listScreens = new ArrayList<>();

	public void initGL() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, height, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	public void start() {

		IntroductionScreen introductionScreen = new IntroductionScreen();
		this.listScreens.add(introductionScreen);

		/*Level1Screen level1Screen = new Level1Screen();
		this.listScreens.add(level1Screen);

		Level2Screen level2Screen = new Level2Screen();
		this.listScreens.add(level2Screen);

		FinalScreen finalScreen = new FinalScreen();
		this.listScreens.add(finalScreen);*/

		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		initGL();
		
		while (!Display.isCloseRequested() && this.current_level < this.listScreens.size()) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			
			switch (this.listScreens.get(this.current_level).getState()) {
			case NEW:
				this.listScreens.get(this.current_level).init();
				break;
			case LOADING_RESOURCE:
				// Show loading screen
				break;
			case ONGOING:
				this.listScreens.get(this.current_level).display();
				break;
			case PASSED:
				this.current_level++;
				break;
			default:
				break;
			}

			Display.update();
			Display.sync(60); // cap fps to 60fps
		}
		Display.destroy();
	}

	public static void main(String[] argv) {
		AbyssWorld timerExample = new AbyssWorld();
		timerExample.start();
	}
}