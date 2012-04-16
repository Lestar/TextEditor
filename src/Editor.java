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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

interface EditorEventListener 
{
	void fileNew(SimpleFrame frame);
	void fileOpen(SimpleFrame frame);
	void fileSave(SimpleFrame frame);
	void fileSaveAs(SimpleFrame frame);
	void exit(SimpleFrame frame);
}

interface EditorKeyEventListener
{
	void Pressed(KeyEvent event);
	void Typed(KeyEvent event);
	void Released(KeyEvent event);
}

interface DocListener
{
	void changedUpdate(DocumentEvent e, MyPanelTextArea text);
	void insertUpdate(DocumentEvent e, MyPanelTextArea text);
	void removeUpdate(DocumentEvent e, MyPanelTextArea text);
}

interface EditorWindowListener
{
	void Activated(WindowEvent e) ;
	void Closed(WindowEvent e) ;
	void Closing(WindowEvent e) ;
	void Deactivated(WindowEvent e) ;
	void Deiconified(WindowEvent e) ;
	void Iconified(WindowEvent e) ;
	void Opened(WindowEvent e) ;
}

class docChange implements DocListener
{
	public void changedUpdate(DocumentEvent e, MyPanelTextArea text)
	{
		
	}
	public void insertUpdate(DocumentEvent e, MyPanelTextArea text)
	{
		try 
		{
			text.setDocText(text.getText().getText());
			text.getList().newPutString(e.getDocument().getText(e.getOffset(), e.getLength()),e.getOffset(),actionsHistory.action.ADD);
		} 
		catch (BadLocationException e1) 
		{
			e1.printStackTrace();
		}
	}
	public void removeUpdate(DocumentEvent e, MyPanelTextArea text)
	{
		if(e.getLength() == 1)
		{
			text.getList().newPutString(text.getDocText().substring(e.getOffset(), e.getOffset() + e.getLength()),e.getOffset(), actionsHistory.action.SUB);
			text.setDocText(text.getText().getText());
		}
		else
		{
			text.getList().newPutString(text.getDocText().substring(e.getOffset(), e.getOffset() + e.getLength()), e.getOffset(), actionsHistory.action.SUB);
		}
	}
}

class fileEvents implements EditorEventListener
{
	public void fileNew(SimpleFrame frame) 
	{
		frame.setIsFileNameSetted(false);
		frame.setTitle("Text Editor");
		JFileChooser chooser = new JFileChooser();
		
		if(frame.getMyPanelTextArea().getIsChanged()) 
		{
			int selection = JOptionPane.showConfirmDialog(null,"Do you want save document?", "Warrning", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
			
			if(selection == JOptionPane.NO_OPTION)	
			{
				frame.getMyPanelTextArea().getText().setText("");
				frame.getMyPanelTextArea().setIsChanged(false);
				frame.setTitle("Text Editor");
			}
			
			if(selection == JOptionPane.YES_OPTION)
			{
				chooser.setCurrentDirectory(new File("."));
				
				int result = chooser.showSaveDialog(null);
				if (result == JFileChooser.APPROVE_OPTION)
				{
					frame.fileName = chooser.getSelectedFile().getPath();
					PrintWriter out;
					try 
					{
						out = new PrintWriter(new FileWriter(frame.fileName));
						String str = frame.getMyPanelTextArea().getText().getText();
						out.print(str);
						out.close();
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
				frame.getMyPanelTextArea().getText().setText("");
				frame.getMyPanelTextArea().setIsChanged(false);
				frame.setTitle("Text Editor");
			}
		}
		else
		{
			frame.getMyPanelTextArea().getText().setText("");
		}
	}
	public void fileOpen(SimpleFrame frame) 
	{
		JFileChooser chooser = new JFileChooser();
		
		chooser.setCurrentDirectory(new File("."));
			
		if(frame.getMyPanelTextArea().getIsChanged())
		{
			int selection = JOptionPane.showConfirmDialog(null,"Do you want save document?", "Warrning", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
			
			if(selection == JOptionPane.NO_OPTION)	
			{
				int result = chooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION)
				{
					frame.fileName = chooser.getSelectedFile().getPath();
					BufferedReader in;
					String buff;
					String str = "";
					try 
					{
						in = new BufferedReader(new FileReader(frame.fileName));
						while ((buff = in.readLine()) != null)
						{
							str += buff;
							str+= "\r\n";
						}
						frame.getMyPanelTextArea().getText().setText(str);
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
						String str = frame.getMyPanelTextArea().getText().getText();
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
						frame.getMyPanelTextArea().getText().setText(str);
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
					frame.getMyPanelTextArea().getText().setText(str);
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
		frame.setIsFileNameSetted(true);
		frame.setTitle("Text Editor" + frame.fileName);
	}
	public void fileSave(SimpleFrame frame)
	{
		File tempFile = new File(frame.tempFileName);
		if(tempFile.exists()) tempFile.delete();
		if(!frame.getIsFileNameSetted())
		{
			JFileChooser chooser = new JFileChooser();
			
			chooser.setCurrentDirectory(new File("."));
				
			int result = chooser.showSaveDialog(null);
			if (result == JFileChooser.APPROVE_OPTION)
			{
				frame.fileName = chooser.getSelectedFile().getPath();
				PrintWriter out;
				try 
				{
					out = new PrintWriter(new FileWriter(frame.fileName));
					String str = frame.getMyPanelTextArea().getText().getText();
					out.print(str);
					out.close();
					frame.getMyPanelTextArea().setIsChanged(false);
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
				out = new PrintWriter(new FileWriter(frame.fileName));
				String str = frame.getMyPanelTextArea().getText().getText();
				out.print(str);
				out.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		frame.setIsFileNameSetted(true);
		frame.setTitle("Text Editor - " + frame.fileName);
	}
	public void fileSaveAs(SimpleFrame frame) 
	{
		File tempFile = new File(frame.tempFileName);
		if(tempFile.exists()) tempFile.delete();
		JFileChooser chooser = new JFileChooser();
		
		chooser.setCurrentDirectory(new File("."));
			
		int result = chooser.showSaveDialog(null);
		if (result == JFileChooser.APPROVE_OPTION)
		{
			frame.fileName = chooser.getSelectedFile().getPath();
			PrintWriter out;
			try 
			{
				out = new PrintWriter(new FileWriter(frame.fileName));
				String str = frame.getMyPanelTextArea().getText().getText();
				out.print(str);
				out.close();
				frame.getMyPanelTextArea().setIsChanged(false);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		frame.setIsFileNameSetted(true);
		frame.setTitle("Text Editor - " + frame.fileName);
	}
	public void exit(SimpleFrame frame) 
	{
		File tempFile = new File(frame.fileName);
		if(tempFile.exists()) tempFile.delete();
		JFileChooser chooser = new JFileChooser();
		
		chooser.setCurrentDirectory(new File("."));
			
		if(frame.getMyPanelTextArea().getIsChanged()) 
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
					frame.fileName = chooser.getSelectedFile().getPath();
					PrintWriter out;
					try 
					{
						out = new PrintWriter(new FileWriter(frame.fileName));
						String str = frame.getMyPanelTextArea().getText().getText();
						out.print(str);
						out.close();
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
				else
				{
					return;
				}
			}
		}
		else
		{
			System.exit(0);
		}
			
	}
}

class keyActions implements ClipboardOwner
{
		public void undo(MyPanelTextArea panelText)
		{
			panelText.getList().newGetString();
		}
		public void redo(MyPanelTextArea panelText)
		{
			panelText.getList().newRestoreString();
		}
		public void copy(MyPanelTextArea panelText)
		{
			if(panelText.getText().getSelectedText() != null)
			{
				StringSelection stringSelection = new StringSelection( panelText.getText().getSelectedText() );
			    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			    clipboard.setContents(stringSelection,  this );
			}
		}
		public void cut(MyPanelTextArea panelText)
		{
			panelText.setAction(false);
			int cp;
			if (panelText.getText().getSelectedText() != null)
			{
				copy(panelText);
				cp = panelText.getText().getSelectionStart();
				panelText.getList().newPutString(panelText.getText().getSelectedText(), panelText.getText().getSelectionStart(), actionsHistory.action.SUB);
				if(panelText.getText().getSelectionStart() == 0)
				{
					panelText.getText().setText(panelText.getText().getText().substring(panelText.getText().getSelectedText().length(), panelText.getText().getText().length()));
					panelText.getText().setCaretPosition(cp);
					return;
				}
				if(panelText.getText().getSelectionStart() > 0 && panelText.getText().getSelectionStart() < panelText.getText().getText().length())
				{
					panelText.getText().setText(panelText.getText().getText().substring(0, panelText.getText().getSelectionStart()) + panelText.getText().getText().substring(panelText.getText().getSelectionStart() + panelText.getText().getSelectedText().length(), panelText.getText().getText().length()));
					panelText.getText().setCaretPosition(cp);
					return;
				}
				if(panelText.getText().getSelectionStart() == panelText.getText().getText().length())
				{
					panelText.getText().setText(panelText.getText().getText().substring(0, panelText.getText().getSelectionStart()));
					panelText.getText().setCaretPosition(cp);
					return;
				}
			}
			panelText.setAction(true);
		}
		public void paste(MyPanelTextArea panelText)
		{
			panelText.setAction(false);
			String clipboardText = null;
	        Transferable trans = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
	 
	        try 
	        {
	            if (trans != null && trans.isDataFlavorSupported(DataFlavor.stringFlavor)) 
	            {
	                clipboardText = (String) trans.getTransferData(DataFlavor.stringFlavor);
	                panelText.getList().newPutString(clipboardText, panelText.getText().getCaretPosition(), actionsHistory.action.ADD);
	            }
	        } 
	        catch (Exception e) 
	        {
	            e.printStackTrace();
	            panelText.setAction(true);
	        }
	        if(panelText.getText().getCaretPosition() == 0)
			{
	        	panelText.getText().setText(clipboardText + panelText.getText().getText());
	        	panelText.setAction(true);
	        	return;
			}
			if(panelText.getText().getCaretPosition() > 0 && panelText.getText().getCaretPosition() < panelText.getText().getText().length())
			{
				panelText.getText().setText(panelText.getText().getText().substring(0, panelText.getText().getCaretPosition()) + clipboardText + panelText.getText().getText().substring(panelText.getText().getCaretPosition(),panelText.getText().getText().length()));
				panelText.setAction(true);
				return;
			}
			if(panelText.getText().getCaretPosition() == panelText.getText().getText().length())
			{
				panelText.getText().setText(panelText.getText().getText() + clipboardText);
				panelText.setAction(true);
				return;
			}
		}
		public void delete(MyPanelTextArea panelText)
		{
			if(panelText.getText().getSelectedText() != null)
			{
				panelText.setAction(false);
				panelText.getText().setText(panelText.getText().getText().substring(0, panelText.getText().getSelectionStart()) + panelText.getText().getText().substring(panelText.getText().getSelectionEnd(), panelText.getText().getText().length()));
				panelText.getList().newPutString(panelText.getText().getSelectedText(), panelText.getText().getSelectionStart(), actionsHistory.action.SUB);
				panelText.setAction(true);
			}
		}
		public void backspace(MyPanelTextArea panelText)
		{
			if(panelText.getText().getSelectedText() != null)
			{
				panelText.setAction(false);
				panelText.getText().setText(panelText.getText().getText().substring(0, panelText.getText().getSelectionStart()) + panelText.getText().getText().substring(panelText.getText().getSelectionEnd(), panelText.getText().getText().length()));
				panelText.getList().newPutString(panelText.getText().getSelectedText(), panelText.getText().getSelectionStart(), actionsHistory.action.SUB);
				panelText.setAction(true);
			}
		}
		
		public void lostOwnership(Clipboard arg0, Transferable arg1) 
		{
			// TODO Auto-generated method stub
			
		}
		
}

class keyEvents implements EditorKeyEventListener
{
	protected SimpleFrame frame;
	keyEvents(SimpleFrame mFrame)
	{
		frame = mFrame;
	}
	public void Pressed(KeyEvent event)
	{
		if(event.getKeyCode() == KeyEvent.VK_N && event.isControlDown())
		{
			new fileEvents().fileNew(frame);
		}
		if(event.getKeyCode() == KeyEvent.VK_O && event.isControlDown())
		{
			new fileEvents().fileOpen(frame);
		}
		if(event.getKeyCode() == KeyEvent.VK_S && event.isControlDown())
		{
			new fileEvents().fileSave(frame);
		}
		if(event.getKeyCode() == KeyEvent.VK_S && event.isControlDown() && event.isShiftDown())
		{
			new fileEvents().fileSaveAs(frame);
		}
		if(event.getKeyCode() == KeyEvent.VK_Z && event.isControlDown())
		{
			frame.getMyPanelTextArea().setAction(false);
			new keyActions().undo(frame.getMyPanelTextArea());
			frame.getMyPanelTextArea().setAction(true);
		}
		if(event.getKeyCode() == KeyEvent.VK_Y && event.isControlDown())
		{
			frame.getMyPanelTextArea().setAction(false);
			new keyActions().redo(frame.getMyPanelTextArea());
			frame.getMyPanelTextArea().setAction(true);
		}
		
	}
	public void Typed(KeyEvent event)
	{
		
	}
	public void Released(KeyEvent event)
	{
		
	}
}

class SimpleFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	private int scr_w;
	private int scr_h;
	private MyPanelTextArea panelText;
	private boolean isFileNameSetted = false;
	String fileName = "temp.tmp";
	String tempFileName = "temp.tmp";
	String temp = null;
	int caretTextPosition = 0;
	
	SimpleFrame()
    {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screen = kit.getScreenSize();
		scr_w = screen.width;
		scr_h = screen.height;
		setSize(scr_w/2,scr_h/2);
		setTitle("Text Editor");
		
		panelText = new MyPanelTextArea(this, (EditorKeyEventListener) new keyEvents(this), (DocListener) new docChange());
		setLayout(new BorderLayout());
		add(panelText, BorderLayout.CENTER);
			
    }
	public MyPanelTextArea getMyPanelTextArea()
	{
		return panelText;
	}
	public boolean getIsFileNameSetted() 
	{
		return isFileNameSetted;
	}
	public void setIsFileNameSetted(boolean isFileNameSetted) 
	{
		this.isFileNameSetted = isFileNameSetted;
	}
	
}

class actionsHistory
{
	private ArrayList<actionText> actionsList;
	public static enum action {ADD, SUB};
	private int indexAction = 0;
	private SimpleFrame mainFrame;
	
	actionsHistory(SimpleFrame frame)
	{
		mainFrame = frame;
		actionsList = new ArrayList<actionText>();
	}
	
	public void newPutString(String str, int position, action type)
	{
		if(indexAction > 0)
		{
			while(actionsList.size() != indexAction)
			{
				actionsList.remove(actionsList.size() - 1);
			}
		}
		
		actionsList.add(new actionText(str, position, type));
		setIndexAction(indexAction + 1);
		mainFrame.getMyPanelTextArea().setIsChanged(true);
		mainFrame.setTitle("Text Editor - " + mainFrame.fileName + " *");
	}
	public void newGetString()
	{
		if (indexAction <= 0) return;
		actionText a = actionsList.get(indexAction-1);
		String temp = mainFrame.getMyPanelTextArea().getText().getText();
		indexAction--;
		mainFrame.getMyPanelTextArea().setIsChanged(true);
		mainFrame.setTitle("Text Editor - " + mainFrame.fileName + " *");
		if(a.actionType == action.ADD)
		{
			if(a.position == 0)
			{
				mainFrame.getMyPanelTextArea().getText().setText(temp.substring(a.str.length(), temp.length()));
				mainFrame.getMyPanelTextArea().getText().setCaretPosition(a.position);
				return;
			}
			if(a.position > 0 && a.position < temp.length())
			{
				mainFrame.getMyPanelTextArea().getText().setText(temp.substring(0, a.position) + temp.substring(a.position + a.str.length(), temp.length()));
				mainFrame.getMyPanelTextArea().getText().setCaretPosition(a.position);
				return;
			}
			if(a.position == temp.length())
			{
				mainFrame.getMyPanelTextArea().getText().setText(temp.substring(0, a.position));
				mainFrame.getMyPanelTextArea().getText().setCaretPosition(a.position);
				return;
			}
		}
		if(a.actionType == action.SUB)
		{
			if(a.position == 0)
			{
				mainFrame.getMyPanelTextArea().getText().setText(a.str + temp);
				mainFrame.getMyPanelTextArea().getText().setCaretPosition(a.position + a.str.length());
				return;
			}
			if(a.position > 0 && a.position < temp.length())
			{
				mainFrame.getMyPanelTextArea().getText().setText(temp.substring(0, a.position) + a.str + temp.substring(a.position, temp.length()));
				mainFrame.getMyPanelTextArea().getText().setCaretPosition(a.position + a.str.length());
				return;
			}
			if(a.position == temp.length())
			{
				mainFrame.getMyPanelTextArea().getText().setText(temp+a.str);
				mainFrame.getMyPanelTextArea().getText().setCaretPosition(a.position + a.str.length());
				return;
			}
		}
	}
	public void newRestoreString()
	{
		if(indexAction > actionsList.size()-1) return;
		actionText a = actionsList.get(indexAction);
		String temp = mainFrame.getMyPanelTextArea().getText().getText();
		indexAction++;
		mainFrame.getMyPanelTextArea().setIsChanged(true);
		mainFrame.setTitle("Text Editor - " + mainFrame.fileName + " *");
		if(a.actionType == action.ADD)
		{
			if(a.position == 0)
			{
				mainFrame.getMyPanelTextArea().getText().setText(a.str + temp);
				mainFrame.getMyPanelTextArea().getText().setCaretPosition(a.position + a.str.length());
				return;
			}
			if(a.position > 0 && a.position < temp.length())
			{
				mainFrame.getMyPanelTextArea().getText().setText(temp.substring(0, a.position));
				mainFrame.getMyPanelTextArea().getText().setCaretPosition(a.position + a.str.length());
				return;
			}
			if(a.position == temp.length())
			{
				mainFrame.getMyPanelTextArea().getText().setText(temp+a.str);
				mainFrame.getMyPanelTextArea().getText().setCaretPosition(a.position + a.str.length());
				return;
			}
		}
		if(a.actionType == action.SUB)
		{
			if(a.position == 0)
			{
				mainFrame.getMyPanelTextArea().getText().setText(temp.substring(a.str.length(), temp.length()));
				mainFrame.getMyPanelTextArea().getText().setCaretPosition(a.position);
				return;
			}
			if(a.position > 0 && a.position < temp.length())
			{
				mainFrame.getMyPanelTextArea().getText().setText(temp.substring(0, a.position) + temp.substring(a.position + a.str.length(), temp.length()));
				mainFrame.getMyPanelTextArea().getText().setCaretPosition(a.position);
				return;
			}
			if(a.position == temp.length())
			{
				mainFrame.getMyPanelTextArea().getText().setText(temp.substring(0, a.position));
				mainFrame.getMyPanelTextArea().getText().setCaretPosition(a.position);
				return;
			}
		}
	}
	public ArrayList<actionText> getActionList()
	{
		return actionsList;
	}
	public int getIndexAction() 
	{
		return indexAction;
	}
	public void setIndexAction(int indexAction) 
	{
		this.indexAction = indexAction;
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

class MyPanelTextArea extends JPanel
{
	private static final long serialVersionUID = 1L;
	private JEditorPane text;
	private JScrollPane scroll;
	private boolean isChanged = false;
	private Timer autosave;
	private String docText = "";
	private SimpleFrame mainFrame;
	private actionsHistory list;
	private boolean Action = true;
	
	public MyPanelTextArea(final SimpleFrame frame, final EditorKeyEventListener listener, final DocListener doc )
	{
		mainFrame = frame;
		text = new JEditorPane();
		list = new actionsHistory(mainFrame);
		text.addKeyListener(new KeyListener()
		{
			public void keyPressed(KeyEvent arg0) 
			{
				listener.Pressed(arg0);
			}
			public void keyReleased(KeyEvent arg0) 
			{
				listener.Released(arg0);
			}
			public void keyTyped(KeyEvent arg0) 
			{
				listener.Typed(arg0);
			}
			
		});
		text.getDocument().addDocumentListener(new DocumentListener()
		{
			public void changedUpdate(DocumentEvent e) 
			{
				if(Action)
				doc.changedUpdate(e, mainFrame.getMyPanelTextArea());
			}

			public void insertUpdate(DocumentEvent e) 
			{
				if(Action)
				doc.insertUpdate(e, mainFrame.getMyPanelTextArea());
			}

			public void removeUpdate(DocumentEvent e) 
			{
				if(Action)
				doc.removeUpdate(e, mainFrame.getMyPanelTextArea());
			}
			
		});
		setLayout(new BorderLayout());
		scroll = new JScrollPane(getText());
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
						if(frame.getIsFileNameSetted())
						{
							out = new PrintWriter(new FileWriter(frame.fileName));
						}
						else 
						{
							out = new PrintWriter(new FileWriter(frame.tempFileName));
						}
						String str = frame.getMyPanelTextArea().getText().getText();
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
		final MyPanelTextArea link = this;
		text.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent event) 
			{
				new popupMenu(link, event);
			}
		});
	}
	
	public boolean getIsChanged()
	{
		return isChanged;
	}
	public void setIsChanged(boolean b)
	{
		isChanged = b;
	}
	public JEditorPane getText() 
	{
		return text;
	}
	public actionsHistory getList()
	{
		return list;
	}

	public String getDocText() 
	{
		return docText;
	}

	public void setDocText(String docText) 
	{
		this.docText = docText;
	}
	public void setAction(boolean b)
	{
		Action = b;
	}
	public boolean getAction()
	{
		return Action;
	}
}

class popupMenu extends JPopupMenu
{
	private static final long serialVersionUID = 1L;
	private JPopupMenu popupMenu;
	private JMenuItem undo;
	private JMenuItem redo;
	private JMenuItem copy;
	private JMenuItem cut;
	private JMenuItem paste;
	private JMenuItem selectAll;
	@SuppressWarnings("null")
	popupMenu(final MyPanelTextArea text, MouseEvent event)
	{
		popupMenu = new JPopupMenu();
		undo = new JMenuItem("Undo");
		redo = new JMenuItem("Redo");
		copy = new JMenuItem("Copy");
		cut = new JMenuItem("Cut");
		paste = new JMenuItem("Paste");
		selectAll = new JMenuItem("Select All");
		
		undo.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				new keyActions().undo(text);				
			}
			
		});
		redo.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				new keyActions().redo(text);				
			}
			
		});
		copy.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				new keyActions().copy(text);				
			}
			
		});
		cut.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				new keyActions().cut(text);				
			}
			
		});
		paste.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				new keyActions().paste(text);				
			}
			
		});
		selectAll.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				text.getText().setSelectionStart(0);
				text.getText().setSelectionEnd(text.getText().getText().length());				
			}
			
		});
		
		popupMenu.add(undo);
		popupMenu.add(redo);
		popupMenu.add(copy);
		popupMenu.add(cut);
		popupMenu.add(paste);
		popupMenu.add(selectAll);
		if (SwingUtilities.isRightMouseButton(event))
		{
			Transferable trans = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
			if (trans == null && !trans.isDataFlavorSupported(DataFlavor.stringFlavor)) 
			{
				paste.setEnabled(false);
			}
			else
			{
				paste.setEnabled(true);
			}
			
			if(text.getList().getActionList().isEmpty())
			{
				undo.setEnabled(false);
			}
			else
			{
				undo.setEnabled(true);
			}
			
			if(text.getList().getActionList().isEmpty() || (!text.getList().getActionList().isEmpty() && text.getList().getIndexAction() == text.getList().getActionList().size()-1))
			{
				redo.setEnabled(false);
			}
			else
			{
				redo.setEnabled(true);
			}
			
			if(text.getText().getSelectedText() == null)
			{
				copy.setEnabled(false);
				cut.setEnabled(false);
			}
			else
			{
				copy.setEnabled(true);
				cut.setEnabled(true);
			}
			popupMenu.show(text.getText(), event.getX(), event.getY());
		}
	}
}

class mainMenuBar extends JMenuBar
{
	private static final long serialVersionUID = 1L;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem newFile;
	private JMenuItem openFile;
	private JMenuItem saveFile;
	private JMenuItem saveAsFile;
	private JMenuItem exit;
	private JMenu editMenu;
	private JMenuItem undo;
	private JMenuItem redo;
	private JMenuItem cut;
	private JMenuItem copy;
	private JMenuItem paste;
	private JMenuItem delete;
	private JMenuItem selectAll;
	mainMenuBar(final SimpleFrame frame)
	{
		menuBar = new JMenuBar();
		//---Menu "File"---
		fileMenu = new JMenu("File");
	    
	    newFile = new JMenuItem("New file                      Ctrl+N");
	    fileMenu.add(newFile);
	    openFile = new JMenuItem("Open file                     Ctrl+O");
	    fileMenu.add(openFile);
	    saveFile = new JMenuItem("Save file                     Ctrl+S");
	    fileMenu.add(saveFile);
	    saveAsFile = new JMenuItem("Save As file    Ctrl+Shift+S");
	    fileMenu.add(saveAsFile);
	    exit = new JMenuItem("Exit");
	    fileMenu.add(exit);
	    //---Menu "Edit"---
	    editMenu = new JMenu("Edit");
	    
	    undo = new JMenuItem("Undo      Ctrl+Z");
	    editMenu.add(undo);
	    redo = new JMenuItem("Redo      Ctrl+Y");
	    editMenu.add(redo);
	    cut = new JMenuItem("Cut      Ctrl+X");
	    editMenu.add(cut);
	    copy = new JMenuItem("Copy      Ctrl+C");
	    editMenu.add(copy);
	    paste = new JMenuItem("Paste      Ctrl+V");
	    editMenu.add(paste);
	    delete = new JMenuItem("Delete      Delete");
	    editMenu.add(delete);
	    selectAll = new JMenuItem("Select All     Ctlr+A");
	    editMenu.add(selectAll);
	    	        
	    menuBar.add(fileMenu);
	    menuBar.add(editMenu);
	    
	    newFile.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e) 
			{
				new fileEvents().fileNew(frame);
				
			}
	    	
	    });
	    openFile.addActionListener(new ActionListener()
	    {
	    	public void actionPerformed(ActionEvent e) 
	    	{
	    		new fileEvents().fileOpen(frame);
			}
	    	
	    });
	    saveFile.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e) 
			{
				new fileEvents().fileSave(frame);
			}
	    	
	    });
	    saveAsFile.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e) 
			{
				new fileEvents().fileSaveAs(frame);
			}
	    	
	    });
	    exit.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e)
			{
				new fileEvents().exit(frame);				
			}
	    	
	    });
	    
	    undo.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e)
			{
				new keyActions().undo(frame.getMyPanelTextArea());		
			}
	    	
	    });
	  	redo.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e)
			{
				new keyActions().redo(frame.getMyPanelTextArea());				
			}
	    	
	    });
	  	cut.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e) 
			{
				new keyActions().cut(frame.getMyPanelTextArea());
			}
	    });
	  	copy.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e)
			{
				new keyActions().copy(frame.getMyPanelTextArea());
			}
	    	
	    });
 	  	paste.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e)
			{
				new keyActions().paste(frame.getMyPanelTextArea());
			}
	    	
	    });
	  	delete.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e)
			{
				frame.temp = frame.getMyPanelTextArea().getText().getSelectedText();
				frame.caretTextPosition = frame.getMyPanelTextArea().getText().getCaretPosition() - 1;
				new keyActions().delete(frame.getMyPanelTextArea());
			}
	    });
	  	selectAll.addActionListener(new ActionListener()
	  	{
	  		public void actionPerformed(ActionEvent e) 
	  		{
				frame.getMyPanelTextArea().getText().setSelectionStart(0);
				frame.getMyPanelTextArea().getText().setSelectionEnd(frame.getMyPanelTextArea().getText().getText().length());
			}
	  		
	  	});
	}
	public JMenuBar getMenuBar()
	{
		return menuBar;
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
	                      
	    			}
	    			public void windowClosed(WindowEvent e) 
	    			{
	    				
	    			}
	    			public void windowClosing(WindowEvent e) 
	    			{
	    				new fileEvents().exit(frame);           
	    			}
	    			public void windowDeactivated(WindowEvent e) 
	    			{
	    				
	    			}
	    			public void windowDeiconified(WindowEvent e) 
	    			{
	                         
	    			}
	    			public void windowIconified(WindowEvent e) 
	    			{
	                           
	    			}
	    			public void windowOpened(WindowEvent e) 
	    			{
	    				
	    			}
	    		});
	    
	    mainMenuBar menuBar = new mainMenuBar(frame);
	    frame.setJMenuBar(menuBar.getMenuBar());
	 }
}
