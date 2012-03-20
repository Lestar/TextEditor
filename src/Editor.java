import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;

//import MyPanelTextArea.action;

import java.awt.event.*;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;


interface EditorEventListener 
{
	void fileNew();
	void fileOpen();
	void fileSave();
	void exit();
}

interface EditorKeyEventListener
{
	void Pressed(KeyEvent event);
	void Typed(KeyEvent event);
	void Released(KeyEvent event);
}

class SimpleFrame extends JFrame implements EditorEventListener, EditorKeyEventListener
{
	private static final long serialVersionUID = 1L;
	public int scr_w;
	public int scr_h;
	
	private MyPanelTextArea panelText;
	private MyPanelButtonArea panelButton;
	/*-----*/
	boolean ctrlXPressed = false;
	boolean ctrlVPressed = false;
	boolean backspacePressed = false;
	boolean deletePressed = false;
	String deletedText ="";
	String cuttedText = "";
	int pastedTextPosition = 0;
	/*-----*/
	public SimpleFrame()
    {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screen = kit.getScreenSize();
		scr_w = screen.width;
		scr_h = screen.height;
		setSize(scr_w/2,scr_h/2);
		setTitle("Text Editor");
		
		panelText = new MyPanelTextArea(this, this);
		panelButton = new MyPanelButtonArea(this);
		
		setLayout(new BorderLayout());
		add(panelButton, BorderLayout.SOUTH);
		add(panelText, BorderLayout.NORTH);
		
		
    }
	

	public MyPanelButtonArea getPanelButton() 
	{
		return panelButton;
	}

	public MyPanelTextArea getPanelText() 
	{
		return panelText;
	}
	
	public void fileNew() 
	{
		JFileChooser chooser = new JFileChooser();
		
		if(panelText.isChoosen) 
		{
			int selection = JOptionPane.showConfirmDialog(null,"Do you want save document?", "Warrning", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
			
			if(selection == JOptionPane.NO_OPTION)	
			{
				panelText.text.setText("");
				panelText.isChoosen = false;
			}
			
			if(selection == JOptionPane.YES_OPTION)
			{
				chooser.setCurrentDirectory(new File("."));
				
				int result = chooser.showSaveDialog(null);
				if (result == JFileChooser.APPROVE_OPTION)
				{
					String name = chooser.getSelectedFile().getPath();
					PrintWriter out;
					try 
					{
						out = new PrintWriter(new FileWriter(name));
						String str = panelText.text.getText();
						out.print(str);
						out.close();
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
				panelText.text.setText("");
				panelText.isChoosen = false;
			}
		}
		else
		{
			panelText.text.setText("");
		}
	}

	public void fileOpen() 
	{
		JFileChooser chooser = new JFileChooser();
		
		chooser.setCurrentDirectory(new File("."));
			
		if(panelText.isChoosen)
		{
			int selection = JOptionPane.showConfirmDialog(null,"Do you want save document?", "Warrning", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
			
			if(selection == JOptionPane.NO_OPTION)	
			{
				int result = chooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION)
				{
					String name = chooser.getSelectedFile().getPath();
					BufferedReader in;
					String buff;
					String str = "";
					try 
					{
						in = new BufferedReader(new FileReader(name));
						while ((buff = in.readLine()) != null)
						{
							str += buff;
							str+= "\r\n";
						}
						panelText.text.setText(str);
					} 
					catch (FileNotFoundException e) 
					{
					e.printStackTrace();
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
			}
			if(selection == JOptionPane.YES_OPTION)
			{
				int result = chooser.showSaveDialog(null);
				if (result == JFileChooser.APPROVE_OPTION)
				{
					String name = chooser.getSelectedFile().getPath();
					PrintWriter out;
					try 
					{
						out = new PrintWriter(new FileWriter(name));
						String str = panelText.text.getText();
						out.print(str);
						out.close();
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
					
				result = chooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION)
				{
					String name = chooser.getSelectedFile().getPath();
					BufferedReader in;
					String buff;
					String str = "";
					try 
					{
						in = new BufferedReader(new FileReader(name));
						while ((buff = in.readLine()) != null)
						{
							str += buff;
							str+= "\r\n";
						}
						panelText.text.setText(str);
					} 
					catch (FileNotFoundException e) 
					{
						e.printStackTrace();
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
			}
		}
		else
		{
			int result = chooser.showOpenDialog(null);
			if (result == JFileChooser.APPROVE_OPTION)
			{
				String name = chooser.getSelectedFile().getPath();
				BufferedReader in;
				String buff;
				String str = "";
				try 
				{
					in = new BufferedReader(new FileReader(name));
					while ((buff = in.readLine()) != null)
					{
						str += buff;
						str+= "\r\n";
					}
					panelText.text.setText(str);
				} 
				catch (FileNotFoundException e) 
				{
					e.printStackTrace();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
			
	}

	public void fileSave() 
	{
		JFileChooser chooser = new JFileChooser();
		
		chooser.setCurrentDirectory(new File("."));
			
		int result = chooser.showSaveDialog(null);
		if (result == JFileChooser.APPROVE_OPTION)
		{
			String name = chooser.getSelectedFile().getPath();
			PrintWriter out;
			try 
			{
				out = new PrintWriter(new FileWriter(name));
				String str = panelText.text.getText();
				out.print(str);
				out.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		panelText.isChoosen = false;
	}

	public void exit() 
	{
		JFileChooser chooser = new JFileChooser();
		
		chooser.setCurrentDirectory(new File("."));
			
		if(panelText.isChoosen) 
		{
			int selection = JOptionPane.showConfirmDialog(null,"Do you want save document?", "Warrning", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
			
			if(selection == JOptionPane.NO_OPTION)	
			{
				System.exit(0);
			}
				
			if(selection == JOptionPane.YES_OPTION)
			{
				chooser.setCurrentDirectory(new File("."));
					
				int result = chooser.showSaveDialog(null);
				if (result == JFileChooser.APPROVE_OPTION)
				{
					String name = chooser.getSelectedFile().getPath();
					PrintWriter out;
					try 
					{
						out = new PrintWriter(new FileWriter(name));
						String str = panelText.text.getText();
						out.print(str);
						out.close();
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
			}
		}
		else
		{
			System.exit(0);
		}
			
	}
	
	public void Pressed(KeyEvent event)
	{
		if(event.getKeyCode() == KeyEvent.VK_Z && event.isControlDown())
		{
			panelText.newGetString();
			panelText.isChoosen = true;
			return;
		}
		if(event.getKeyCode() == KeyEvent.VK_Y && event.isControlDown())
		{
			panelText.newRestoreString();
			panelText.isChoosen = true;
			return;
		}
		
		if(event.getKeyCode() == KeyEvent.VK_X && event.isControlDown())
		{
			if (panelText.text.getSelectedText() != null)
			{
				ctrlXPressed = true;
				cuttedText = panelText.text.getSelectedText();
			}
		}		
		
		if(event.getKeyCode() == KeyEvent.VK_V && event.isControlDown())
		{
			ctrlVPressed = true;
			pastedTextPosition = panelText.text.getCaretPosition();
		}
		
		if(event.getKeyCode() == KeyEvent.VK_BACK_SPACE)
		{
			if(panelText.text.getSelectedText() == null)
			{
				String str;
				panelText.isChoosen = true;
				str = panelText.text.getText().substring(panelText.text.getCaretPosition() - 1, panelText.text.getCaretPosition());
				panelText.newPutString(str,panelText.text.getCaretPosition(),MyPanelTextArea.action.SUB);
			}
			else
			{
				deletedText = panelText.text.getSelectedText();
				backspacePressed = true;
			}
			return;
		}
		if(event.getKeyCode() == KeyEvent.VK_DELETE)
		{
			if(panelText.text.getSelectedText() == null)
			{
				String str;
				panelText.isChoosen = true;
				if(panelText.text.getText().length() == panelText.text.getCaretPosition()) return;
				str = panelText.text.getText().substring(panelText.text.getCaretPosition(),panelText.text.getCaretPosition()+1);
				panelText.newPutString(str,panelText.text.getCaretPosition(),MyPanelTextArea.action.SUB);
			}
			else
			{
				deletedText = panelText.text.getSelectedText();
				deletePressed = true;
			}
		}
	}
	
	public void Typed(KeyEvent event)
	{
		if(event.isControlDown()) return;
		if(event.isAltDown()) return;
		if(event.getKeyChar() != KeyEvent.CHAR_UNDEFINED)// | event.getKeyChar() == KeyEvent.VK_SPACE)
		{
			if(panelText.text.getSelectedText() != null)
			{
				String str;
				str = panelText.text.getSelectedText();
				panelText.newPutString(str,panelText.text.getCaretPosition(),MyPanelTextArea.action.SUB);
			}
			panelText.isChoosen = true;
			String str;
			str = event.getKeyChar() + "";
			panelText.newPutString(str,panelText.text.getCaretPosition(),MyPanelTextArea.action.ADD);
		}	
	}
	
	public void Released(KeyEvent event)
	{
		if(ctrlXPressed)
		{
			panelText.newPutString(cuttedText, panelText.text.getCaretPosition(), MyPanelTextArea.action.SUB);
			panelText.isChoosen = true;
	        ctrlXPressed = false;
	        cuttedText = "";
			return;
		}
		if(ctrlVPressed)
		{
			String clipboardText;
	        Transferable trans = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
	 
	        try 
	        {
	            if (trans != null && trans.isDataFlavorSupported(DataFlavor.stringFlavor)) 
	            {
	                clipboardText = (String) trans.getTransferData(DataFlavor.stringFlavor);
	                panelText.newPutString(clipboardText, pastedTextPosition, MyPanelTextArea.action.ADD);
	            }
	        } 
	        catch (Exception e) 
	        {
	            e.printStackTrace();
	        }
			
	        panelText.isChoosen = true;
	        ctrlVPressed = false;
	        pastedTextPosition = 0;
			return;
		}
		if(backspacePressed)
		{
			panelText.newPutString(deletedText, panelText.text.getCaretPosition(), MyPanelTextArea.action.SUB);
			panelText.isChoosen = true;
	        backspacePressed = false;
	        deletedText = "";
			return;
		}
		if(deletePressed)
		{
			panelText.newPutString(deletedText, panelText.text.getCaretPosition(), MyPanelTextArea.action.SUB);
			panelText.isChoosen = true;
	        deletePressed = false;
	        deletedText = "";
			return;
		}
	}
}

class MyPanelButtonArea extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public MyPanelButtonArea(final EditorEventListener listener)
	{
		JButton ButtonNew = new JButton("New");
		ButtonNew.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					listener.fileNew();
				}
			}
		);
		add(ButtonNew);
		
		JButton ButtonOpen = new JButton("Open");
		ButtonOpen.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					listener.fileOpen();
				}
			}
		);
		add(ButtonOpen);
		
		JButton ButtonSave = new JButton("Save");
		ButtonSave.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					listener.fileSave();
				}
			}
		);
		add(ButtonSave);
		
		JButton ButtonExit = new JButton("Exit");
		ButtonExit.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					listener.exit();
				}
			}
		);
		add(ButtonExit);
	}
}

class MyPanelTextArea extends JPanel
{
	private static final long serialVersionUID = 1L;
	public JTextArea text;
	private JScrollPane scroll;
	private int indexAction = 0;
	public boolean isChoosen = false;
	public ArrayList<actionText> actionList = new ArrayList<actionText>();
	public enum action {ADD, SUB};
	
	public MyPanelTextArea(SimpleFrame frame, final EditorKeyEventListener listener )
	{
		text = new JTextArea(frame.scr_h/40,frame.scr_w/23);
		text.addKeyListener(new KeyListener()
		{
			public void keyPressed(KeyEvent e)
			{
				listener.Pressed(e);
			}
			
			public void keyTyped(KeyEvent e)
			{
				listener.Typed(e);
			}
			
			public void keyReleased(KeyEvent e)
			{
				listener.Released(e);
			}
		});

		scroll = new JScrollPane(text);
		add(scroll);
	}
	
	public void newPutString(String str, int position, action type)
	{
		if(indexAction > 0)
		{
			while(actionList.size() != indexAction)
			{
				actionList.remove(actionList.size() - 1);
			}
		}
		
		actionList.add(new actionText(str, position, type));
		indexAction++;
	}
	
	public void newGetString()
	{
		if (indexAction <= 0) return;
		actionText a = actionList.get(indexAction-1);
		String temp = text.getText();
		if(a.actionType == action.ADD)
		{
			String tempString1;
			String tempString2;
			if(a.position == 0)
			{
				tempString1 = "";
			}
			else
			{
				tempString1 = temp.substring(0,a.position);
			}
			if ((a.position + a.str.length() == temp.length())&& (a.position != 0))
			{
				text.setText(tempString1);
			}
			else
			{
				tempString2 = temp.substring(a.position + a.str.length(), temp.length());
				text.setText(tempString1 + tempString2);
			}
		}
		if(a.actionType == action.SUB)
		{
			String tempString1;
			String tempString2;
			if(a.position == 0)
			{
				tempString1 = "";
			}
			else
			{
				tempString1 = temp.substring(0,a.position) + a.str;
			}
			if((a.position + a.str.length() == temp.length()) && (a.position != 0))
			{
				tempString2 = a.str;
			}
			else
			{
				tempString2 = temp.substring(a.position, temp.length());
			}
			text.setText(tempString1 + tempString2);
		}
		text.setCaretPosition(a.position);
		indexAction--;
	}
	
	public void newRestoreString()
	{
		if(indexAction > actionList.size()-1) return;
		actionText a = actionList.get(indexAction);
		String temp = text.getText();
		int caretaPosition = 0;
		
		if(a.actionType == action.ADD)
		{
			String tempString1;
			String tempString2;
			
			if(a.position == 0)
			{
				tempString1 = a.str;
			}
			else
			{
				tempString1 = temp.substring(0,a.position) + a.str;
			}
			if ((a.position == temp.length()) && (a.position != 0))
			{
				tempString2 = "";
			}
			else
			{
				tempString2 = temp.substring(a.position, temp.length());
			}
			text.setText(tempString1 + tempString2);
			caretaPosition = tempString1.length();
		}
		if(a.actionType == action.SUB)
		{
			String tempString1;
			String tempString2;
			if(a.position == 0)
			{
				tempString1 = "";
			}
			else
			{
				tempString1 = temp.substring(0,a.position);
			}
			if(a.position == temp.length())
			{
				text.setText(tempString1);
				text.setCaretPosition(a.position);
			}
			else
			{
				tempString2 = temp.substring(a.position + a.str.length(), temp.length());
				text.setText(tempString1 + tempString2);
			}
			caretaPosition = tempString1.length();
		}
		indexAction++;
		text.setCaretPosition(caretaPosition);
	}

	class actionText
	{
		String str;
		int position;
		action actionType;
		public actionText(String text, int pos, action type)
		{
			str = text;
			position = pos;
			actionType = type;
		}
	}
	
}



public class Editor {

	public static void main(String[] args) 
	{
		SimpleFrame frame = new SimpleFrame();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true); 
	}

}
