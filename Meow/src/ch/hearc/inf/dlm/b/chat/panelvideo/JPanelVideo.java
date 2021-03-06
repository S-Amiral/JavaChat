
package ch.hearc.inf.dlm.b.chat.panelvideo;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

import javax.swing.JPanel;

import ch.hearc.inf.dlm.b.chat.reseau.Application;
import ch.hearc.inf.dlm.b.chat.reseau.image.ImageSerializable;
import ch.hearc.inf.dlm.b.chat.reseau.spec.Application_I;

public class JPanelVideo extends JPanel implements JPanelVideo_I
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JPanelVideo()
		{
		blackAndWhite = false;
		mirror = true;

		geometry();
		control();
		appearance();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private void geometry()
		{
		// JComponent : Instanciation
		imageLocal = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
		imageExterne = new BufferedImage(300, 200, BufferedImage.TYPE_3BYTE_BGR);
		ThreadVideo threadVideo = new ThreadVideo(this);
		new Thread(threadVideo).start();

		// Layout : Specification
			{
			//			BorderLayout borderLayout = new BorderLayout();
			//			setLayout(borderLayout);

			// borderLayout.setHgap(20);
			// borderLayout.setVgap(20);
			}

		// JComponent : add
		//add(new JButton(),BorderLayout.CENTER);

		}

	private void control()
		{
		// rien
		}

	private void appearance()
		{
		// rien
		}

	@Override
	protected void paintComponent(Graphics g)
		{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		AffineTransform transform = g2d.getTransform();
		dessiner(g2d);
		g2d.setTransform(transform);
		}

	private void dessiner(Graphics2D g2d)
		{
		BufferedImage imageLocal2=imageLocal;
		BufferedImage imageExterne2=imageExterne;
		if (blackAndWhite)
			{
			imageExterne2 = setBlackAndWhite(imageExterne);
			imageLocal2 = setBlackAndWhite(imageLocal);
			}
		if (mirror)
			{
			g2d.scale(-1, 1);
			g2d.translate(-this.getWidth(), 0);

			g2d.drawImage(imageLocal2, 0, 0, this.getWidth() / 2, this.getHeight(), null);
			g2d.drawImage(imageExterne2, this.getWidth() / 2, 0, this.getWidth() / 2, this.getHeight(), this);
			}
		else
			{
			g2d.drawImage(imageLocal2, this.getWidth() / 2, 0, this.getWidth() / 2, this.getHeight(), null);
			g2d.drawImage(imageExterne2, 0, 0, this.getWidth() / 2, this.getHeight(), this);
			}


		}

	private BufferedImage setBlackAndWhite(BufferedImage bufferedImage)
		{
		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
		ColorConvertOp op = new ColorConvertOp(cs, null);

		return op.filter(bufferedImage, null);
		}

	@Override
	public void setLocalImage(BufferedImage image)
		{
		this.imageLocal = image;
		ImageSerializable serializableImage = new ImageSerializable(image);

		try
			{
			Application.getInstance().getRemote().setImage(serializableImage);
			Thread.sleep(1);
			}
		catch (Exception e)
			{
			e.printStackTrace();
			}
		repaint();



		}

	@Override
	public void setExternalImage(BufferedImage image)
		{
		this.imageExterne = image;
		repaint();
		}

	@Override
	public void toggleBlackAndWhite()
		{
		blackAndWhite = !blackAndWhite;
		}

	public void toggleMirror()
		{
		mirror = !mirror;
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Tools
	private BufferedImage imageLocal;
	private BufferedImage imageExterne;
	private boolean blackAndWhite;
	private boolean mirror;
	private Application_I application;

	}
