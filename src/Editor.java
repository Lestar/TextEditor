//import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;


interface EditorEventListener 
{
	void fileNew();
	void fileOpen();
	void fileSave();
	void fileSaveAs();
	void exit();
}

interface EditorKeyEventListener
{
	void Pressed(KeyEvent event);
	void Typed(KeyEvent event);
	void Released(KeyEvent event);
}

interface EditorWindowListener
{
	public void Activated(WindowEvent e) ;
	public void Closed(WindowEvent e) ;
	public void Closing(WindowEvent e) ;
	public void Deactivated(WindowEvent e) ;
	public void Deiconified(WindowEvent e) ;
	public void Iconified(WindowEvent e) ;
	public void Opened(WindowEvent e) ;
}


/*
interface EditorMouselistener
{
	public void mouseClicked(MouseEvent e); 
	public void mouseEntered(MouseEvent e); 
	public void mouseExited(MouseEvent e);
	public void mousePressed(MouseEvent e);
	public void mouseReleased(MouseEvent e);
}
*/

class SimpleFrame extends JFrame implements EditorEventListener, EditorKeyEventListener, EditorWindowListener, ClipboardOwner
{
	private static final long serialVersionUID = 1L;
	public int scr_w;
	public int scr_h;
	private MyPanelTextArea panelText;
	boolean isFileNameSetted = false;
	String fileName = "temp.tmp";
	String tempFileName = "temp.tmp";
	String temp = null;
	int caretTextPosition = 0;
	
	public SimpleFrame()
    {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screen = kit.getScreenSize();
		scr_w = screen.width;
		scr_h = screen.height;
		setSize(scr_w/2,scr_h/2);
		setTitle("Text Editor");
		
		panelText = new MyPanelTextArea(this, this);
		//panelButton = new MyPanelButtonArea(this);
		
		setLayout(new BorderLayout());
		//add(panelButton, BorderLayout.SOUTH);
		add(panelText, BorderLayout.CENTER);
		
		
    }
	public MyPanelTextArea getMyPanelTextArea()
	{
		return panelText;
	}
	//button's listeners---
	public void fileNew() 
	{
		isFileNameSetted = false;
		this.setTitle("Text Editor");
		JFileChooser chooser = new JFileChooser();
		
		if(panelText.isChanged) 
		{
			int selection = JOptionPane.showConfirmDialog(null,"Do you want save document?", "Warrning", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
			
			if(selection == JOptionPane.NO_OPTION)	
			{
				panelText.text.setText("");
				panelText.isChanged = false;
				this.setTitle("Text Editor");
			}
			
			if(selection == JOptionPane.YES_OPTION)
			{
				chooser.setCurrentDirectory(new File("."));
				
				int result = chooser.showSaveDialog(null);
				if (result == JFileChooser.APPROVE_OPTION)
				{
					fileName = chooser.getSelectedFile().getPath();
					PrintWriter out;
					try 
					{
						out = new PrintWriter(new FileWriter(fileName));
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
				panelText.isChanged = false;
				this.setTitle("Text Editor");
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
			
		if(panelText.isChanged)
		{
			int selection = JOptionPane.showConfirmDialog(null,"Do you want save document?", "Warrning", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
			
			if(selection == JOptionPane.NO_OPTION)	
			{
				int result = chooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION)
				{
					fileName = chooser.getSelectedFile().getPath();
					BufferedReader in;
					String buff;
					String str = "";
					try 
					{
						in = new BufferedReader(new FileReader(fileName));
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
		isFileNameSetted = true;
		this.setTitle("Text Editor" + fileName);
	}
	public void fileSave()
	{
		File tempFile = new File(tempFileName);
		if(tempFile.exists()) tempFile.delete();
		if(!isFileNameSetted)
		{
			JFileChooser chooser = new JFileChooser();
			
			chooser.setCurrentDirectory(new File("."));
				
			int result = chooser.showSaveDialog(null);
			if (result == JFileChooser.APPROVE_OPTION)
			{
				fileName = chooser.getSelectedFile().getPath();
				PrintWriter out;
				try 
				{
					out = new PrintWriter(new FileWriter(fileName));
					String str = panelText.text.getText();
					out.print(str);
					out.close();
					panelText.isChanged = false;
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
		else
		{
			PrintWriter out;
			try 
			{
				out = new PrintWriter(new FileWriter(fileName));
				String str = panelText.text.getText();
				out.print(str);
				out.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		isFileNameSetted = true;
		this.setTitle("Text Editor - " + fileName);
	}
	public void fileSaveAs() 
	{
		File tempFile = new File(tempFileName);
		if(tempFile.exists()) tempFile.delete();
		JFileChooser chooser = new JFileChooser();
		
		chooser.setCurrentDirectory(new File("."));
			
		int result = chooser.showSaveDialog(null);
		if (result == JFileChooser.APPROVE_OPTION)
		{
			fileName = chooser.getSelectedFile().getPath();
			PrintWriter out;
			try 
			{
				out = new PrintWriter(new FileWriter(fileName));
				String str = panelText.text.getText();
				out.print(str);
				out.close();
				panelText.isChanged = false;
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		isFileNameSetted = true;
		this.setTitle("Text Editor - " + fileName);
	}
	public void exit() 
	{
		File tempFile = new File(fileName);
		if(tempFile.exists()) tempFile.delete();
		JFileChooser chooser = new JFileChooser();
		
		chooser.setCurrentDirectory(new File("."));
			
		if(panelText.isChanged) 
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
					fileName = chooser.getSelectedFile().getPath();
					PrintWriter out;
					try 
					{
						out = new PrintWriter(new FileWriter(fileName));
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
	//key actions---
	public void undo()
	{
		panelText.newGetString();
	}
	public void redo()
	{
		panelText.newRestoreString();
	}
	public void copy()
	{
		if(panelText.text.getSelectedText() != null)
		{
			StringSelection stringSelection = new StringSelection( panelText.text.getSelectedText() );
		    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		    clipboard.setContents(stringSelection, (java.awt.datatransfer.ClipboardOwner) this );
		}
	}
	public void cut()
	{
		int cp;
		if (panelText.text.getSelectedText() != null)
		{
			copy();
			cp = panelText.text.getSelectionStart();
			panelText.newPutString(panelText.text.getSelectedText(), panelText.text.getSelectionStart(), MyPanelTextArea.action.SUB);
			if(panelText.text.getSelectionStart() == 0)
			{
				panelText.text.setText(panelText.text.getText().substring(panelText.text.getSelectedText().length(), panelText.text.getText().length()));
				panelText.text.setCaretPosition(cp);
				return;
			}
			if(panelText.text.getSelectionStart() > 0 && panelText.text.getSelectionStart() < panelText.text.getText().length())
			{
				panelText.text.setText(panelText.text.getText().substring(0, panelText.text.getSelectionStart()) + panelText.text.getText().substring(panelText.text.getSelectionStart() + panelText.text.getSelectedText().length(), panelText.text.getText().length()));
				panelText.text.setCaretPosition(cp);
				return;
			}
			if(panelText.text.getSelectionStart() == panelText.text.getText().length())
			{
				panelText.text.setText(panelText.text.getText().substring(0, panelText.text.getSelectionStart()));
				panelText.text.setCaretPosition(cp);
				return;
			}
		}
	}
	public void paste(KeyEvent event)
	{
		String clipboardText = null;
        Transferable trans = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
 
        try 
        {
            if (trans != null && trans.isDataFlavorSupported(DataFlavor.stringFlavor)) 
            {
                clipboardText = (String) trans.getTransferData(DataFlavor.stringFlavor);
                panelText.newPutString(clipboardText, panelText.text.getCaretPosition(), MyPanelTextArea.action.ADD);
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
	}
	public void paste(ActionEvent event)
	{
		String clipboardText = null;
        Transferable trans = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
 
        try 
        {
            if (trans != null && trans.isDataFlavorSupported(DataFlavor.stringFlavor)) 
            {
                clipboardText = (String) trans.getTransferData(DataFlavor.stringFlavor);
                panelText.newPutString(clipboardText, panelText.text.getCaretPosition(), MyPanelTextArea.action.ADD);
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        if(panelText.text.getCaretPosition() == 0)
		{
        	panelText.text.setText(clipboardText + panelText.text.getText());
        	return;
		}
		if(panelText.text.getCaretPosition() > 0 && panelText.text.getCaretPosition() < panelText.text.getText().length())
		{
			panelText.text.setText(panelText.text.getText().substring(0, panelText.text.getCaretPosition()) + clipboardText + panelText.text.getText().substring(panelText.text.getCaretPosition(),panelText.text.getText().length()));
			return;
		}
		if(panelText.text.getCaretPosition() == panelText.text.getText().length())
		{
			panelText.text.setText(panelText.text.getText() + clipboardText);
			return;
		}
	}
	public void delete()
	{
		if(panelText.text.getSelectedText() == null)
		{
			String str;
			if(panelText.text.getText().length() == panelText.text.getCaretPosition()) return;
			str = panelText.text.getText().substring(panelText.text.getCaretPosition(),panelText.text.getCaretPosition()+1);
			panelText.newPutString(str,panelText.text.getCaretPosition(),MyPanelTextArea.action.SUB);
		}
		else
		{
			panelText.newPutString(panelText.text.getSelectedText(), panelText.text.getSelectionStart(), MyPanelTextArea.action.SUB);
		}
	}
	public void backspace()
	{
		if(panelText.text.getSelectedText() != null)
		{
			panelText.newPutString(panelText.text.getSelectedText(), panelText.text.getSelectionStart(), MyPanelTextArea.action.SUB);
		}
		else
		{
			String str;
			str = panelText.text.getText().substring(panelText.text.getCaretPosition() - 1, panelText.text.getCaretPosition());
			panelText.newPutString(str,panelText.text.getCaretPosition()-1 ,MyPanelTextArea.action.SUB);
		}
	}
	//text listener---
	public void Pressed(KeyEvent event)
	{
		if(event.getKeyCode() == KeyEvent.VK_N && event.isControlDown())
		{
			fileNew();
		}
		if(event.getKeyCode() == KeyEvent.VK_O && event.isControlDown())
		{
			fileOpen();
		}
		if(event.getKeyCode() == KeyEvent.VK_S && event.isControlDown())
		{
			fileSave();
		}
		if(event.getKeyCode() == KeyEvent.VK_S && event.isControlDown() && event.isShiftDown())
		{
			fileSaveAs();
		}
		if(event.getKeyCode() == KeyEvent.VK_Z && event.isControlDown())
		{
			undo();
		}
		if(event.getKeyCode() == KeyEvent.VK_Y && event.isControlDown())
		{
			redo();
		}
		
		if(event.getKeyCode() == KeyEvent.VK_X && event.isControlDown())
		{
			cut();
		}		
		
		if(event.getKeyCode() == KeyEvent.VK_V && event.isControlDown())
		{
			paste(event);
		}
		
		if(event.getKeyCode() == KeyEvent.VK_C && event.isControlDown())
		{
			copy();
		}
		if(event.getKeyCode() == KeyEvent.VK_BACK_SPACE)
		{
			backspace();
		}
		if(event.getKeyCode() == KeyEvent.VK_DELETE)
		{
			delete();
		}
	}
	public void Typed(KeyEvent event)
	{
		if(event.isControlDown()) return;
		if(event.isAltDown()) return;
		if(event.getKeyChar() == (char)127)	return;
		if(event.getKeyChar() == '\b') return;
		if(event.getKeyChar() != KeyEvent.CHAR_UNDEFINED)
		{
			if(panelText.text.getSelectedText() != null)
			{
				String str;
				str = panelText.text.getSelectedText();
				panelText.newPutString(str,panelText.text.getCaretPosition(),MyPanelTextArea.action.SUB);
			}
			panelText.isChanged = true;
			String str;
			str = event.getKeyChar() + "";
			panelText.newPutString(str,panelText.text.getCaretPosition(),MyPanelTextArea.action.ADD);
		}	
	}
	public void Released(KeyEvent event)
	{
		
	}
	//window listener---
	public void Activated(WindowEvent e) 
	{
                      
	}
	public void Closed(WindowEvent e) 
	{
             
	}
	public void Closing(WindowEvent e) 
	{
          exit();             
	}
	public void Deactivated(WindowEvent e) 
	{
		
	}
	public void Deiconified(WindowEvent e) 
	{
                       
	}
	public void Iconified(WindowEvent e) 
	{
                       
	}
	public void Opened(WindowEvent e) 
	{
        
	}
	
	public void lostOwnership(Clipboard cb, Transferable trans) 
	{
		// TODO Auto-generated method stub
		
	}
	
}


class MyPanelTextArea extends JPanel
{
	private static final long serialVersionUID = 1L;
	public JEditorPane text;
	private JScrollPane scroll;
	private int indexAction = 0;
	public boolean isChanged = false;
	public ArrayList<actionText> actionList = new ArrayList<actionText>();
	public enum action {ADD, SUB};
	Timer autosave;
	private SimpleFrame mainFrame;
	
	public MyPanelTextArea(final SimpleFrame frame, final EditorKeyEventListener listener )
	{
		mainFrame = frame;
		text = new JEditorPane();
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
		
		setLayout(new BorderLayout());
		scroll = new JScrollPane(text);
		add(scroll, BorderLayout.CENTER);
		
		autosave = new Timer(5 * 1000, new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(isChanged)
				{
					PrintWriter out;
					try 
					{
						if(frame.isFileNameSetted)
						{
							out = new PrintWriter(new FileWriter(frame.fileName));
						}
						else 
						{
							out = new PrintWriter(new FileWriter(frame.tempFileName));
						}
						String str = frame.getMyPanelTextArea().text.getText();
						out.print(str);
						out.close();
					} 
					catch (IOException e1) 
					{
						e1.printStackTrace();
					}
				}
			}
			
		});
		autosave.start();
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
		isChanged = true;
		mainFrame.setTitle("Text Editor - " + mainFrame.fileName + " *");
	}
	
	public void newGetString()
	{
		if (indexAction <= 0) return;
		actionText a = actionList.get(indexAction-1);
		String temp = text.getText();
		indexAction--;
		isChanged = true;
		mainFrame.setTitle("Text Editor - " + mainFrame.fileName + " *");
		if(a.actionType == action.ADD)
		{
			if(a.position == 0)
			{
				text.setText(temp.substring(a.str.length(), temp.length()));
				text.setCaretPosition(a.position);
				return;
			}
			if(a.position > 0 && a.position < temp.length())
			{
				text.setText(temp.substring(0, a.position) + temp.substring(a.position + a.str.length(), temp.length()));
				text.setCaretPosition(a.position);
				return;
			}
			if(a.position == temp.length())
			{
				text.setText(temp.substring(0, a.position));
				text.setCaretPosition(a.position);
				return;
			}
		}
		if(a.actionType == action.SUB)
		{
			if(a.position == 0)
			{
				text.setText(a.str + temp);
				text.setCaretPosition(a.position + a.str.length());
				return;
			}
			if(a.position > 0 && a.position < temp.length())
			{
				text.setText(temp.substring(0, a.position) + a.str + temp.substring(a.position, temp.length()));
				text.setCaretPosition(a.position + a.str.length());
				return;
			}
			if(a.position == temp.length())
			{
				text.setText(temp+a.str);
				text.setCaretPosition(a.position + a.str.length());
				return;
			}
		}
	}
	
	public void newRestoreString()
	{
		if(indexAction > actionList.size()-1) return;
		actionText a = actionList.get(indexAction);
		String temp = text.getText();
		indexAction++;
		isChanged = true;
		mainFrame.setTitle("Text Editor - " + mainFrame.fileName + " *");
		if(a.actionType == action.ADD)
		{
			if(a.position == 0)
			{
				text.setText(a.str + temp);
				text.setCaretPosition(a.position + a.str.length());
				return;
			}
			if(a.position > 0 && a.position < temp.length())
			{
				text.setText(temp.substring(0, a.position));
				text.setCaretPosition(a.position + a.str.length());
				return;
			}
			if(a.position == temp.length())
			{
				text.setText(temp+a.str);
				text.setCaretPosition(a.position + a.str.length());
				return;
			}
		}
		if(a.actionType == action.SUB)
		{
			if(a.position == 0)
			{
				text.setText(temp.substring(a.str.length(), temp.length()));
				text.setCaretPosition(a.position);
				return;
			}
			if(a.position > 0 && a.position < temp.length())
			{
				text.setText(temp.substring(0, a.position) + temp.substring(a.position + a.str.length(), temp.length()));
				text.setCaretPosition(a.position);
				return;
			}
			if(a.position == temp.length())
			{
				text.setText(temp.substring(0, a.position));
				text.setCaretPosition(a.position);
				return;
			}
		}
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

public class Editor 
{
	public static void main(String[] args) 
	{
		final SimpleFrame frame = new SimpleFrame();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true); 
	    frame.addWindowListener(new WindowListener()
	    		{
	    			public void windowActivated(WindowEvent e) 
	    			{
	                     frame.Activated(e);     
	    			}
	    			public void windowClosed(WindowEvent e) 
	    			{
	    				frame.Closed(e);
	    			}
	    			public void windowClosing(WindowEvent e) 
	    			{
	    				frame.Closing(e);            
	    			}
	    			public void windowDeactivated(WindowEvent e) 
	    			{
	    				frame.Deactivated(e);
	    			}
	    			public void windowDeiconified(WindowEvent e) 
	    			{
	                    frame.Deiconified(e);      
	    			}
	    			public void windowIconified(WindowEvent e) 
	    			{
	                    frame.Iconified(e);       
	    			}
	    			public void windowOpened(WindowEvent e) 
	    			{
	    				frame.Opened(e);
	    			}
	    		});
	    
	    JMenuBar menuBar = new JMenuBar();
	    
	    JMenu fileMenu = new JMenu("File");
	    
	    JMenuItem newFile = new JMenuItem("New file                      Ctrl+N");
	    fileMenu.add(newFile);
	    JMenuItem openFile = new JMenuItem("Open file                     Ctrl+O");
	    fileMenu.add(openFile);
	    JMenuItem saveFile = new JMenuItem("Save file                     Ctrl+S");
	    fileMenu.add(saveFile);
	    JMenuItem saveAsFile = new JMenuItem("Save As file    Ctrl+Shift+S");
	    fileMenu.add(saveAsFile);
	    JMenuItem exit = new JMenuItem("Exit");
	    fileMenu.add(exit);
	    
	    JMenu editMenu = new JMenu("Edit");
	    
	    JMenuItem undo = new JMenuItem("Undo      Ctrl+Z");
	    editMenu.add(undo);
	    JMenuItem redo = new JMenuItem("Redo      Ctrl+Y");
	    editMenu.add(redo);
	    final JMenuItem cut = new JMenuItem("Cut      Ctrl+X");
	    editMenu.add(cut);
	    JMenuItem copy = new JMenuItem("Copy      Ctrl+C");
	    editMenu.add(copy);
	    JMenuItem paste = new JMenuItem("Paste      Ctrl+V");
	    editMenu.add(paste);
	    JMenuItem delete = new JMenuItem("Delete      Delete");
	    editMenu.add(delete);
	    JMenuItem selectAll = new JMenuItem("Select All     Ctlr+A");
	    editMenu.add(selectAll);
	    	        
	    menuBar.add(fileMenu);
	    menuBar.add(editMenu);
	    frame.setJMenuBar(menuBar);
	    
	    newFile.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e) 
			{
				frame.fileNew();
				
			}
	    	
	    });
	    openFile.addActionListener(new ActionListener()
	    {
	    	public void actionPerformed(ActionEvent e) 
	    	{
	    		frame.fileOpen();
			}
	    	
	    });
	    saveFile.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e) 
			{
				frame.fileSave();
			}
	    	
	    });
	    saveAsFile.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e) 
			{
				frame.fileSaveAs();
			}
	    	
	    });
	    exit.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e)
			{
				frame.exit();				
			}
	    	
	    });
	    
	    undo.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e)
			{
				frame.undo();		
			}
	    	
	    });
	  	redo.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e)
			{
				frame.redo();				
			}
	    	
	    });
	  	cut.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e) 
			{
				frame.cut();
			}
			
	    	
	    });
	  	copy.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e)
			{
				frame.copy();
			}
	    	
	    });
 	  	paste.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e)
			{
				frame.paste(e);
			}
	    	
	    });
	  	delete.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e)
			{
				frame.temp = frame.getMyPanelTextArea().text.getSelectedText();
				frame.caretTextPosition = frame.getMyPanelTextArea().text.getCaretPosition() - 1;
				frame.delete();
			}
	    	
	    });
	  	selectAll.addActionListener(new ActionListener()
	  	{
	  		public void actionPerformed(ActionEvent e) 
	  		{
				frame.getMyPanelTextArea().text.setSelectionStart(0);
				frame.getMyPanelTextArea().text.setSelectionEnd(frame.getMyPanelTextArea().text.getText().length());
			}
	  		
	  	});
	}
}
