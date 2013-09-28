package org.antispin.udma.ui.jfc;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.io.IOException;

import javax.imageio.spi.IIORegistry;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.antispin.imageio.plugins.dc6.DC6ImageReader;
import org.antispin.imageio.plugins.dc6.DC6ImageReaderSpi;
import org.antispin.udma.model.factory.IItemFactory;
import org.antispin.udma.model.impl.DefaultItemFactory;
import org.antispin.udma.model.repository.HibernateItemRepository;
import org.antispin.udma.model.repository.IItemRepository;
import org.antispin.udma.resource.D2ResourceFactory;
import org.antispin.udma.service.D2CharacterService;
import org.antispin.udma.ui.jfc.action.AboutAction;
import org.antispin.udma.ui.jfc.action.AutoCubeAction;
import org.antispin.udma.ui.jfc.action.CopyAction;
import org.antispin.udma.ui.jfc.action.CutAction;
import org.antispin.udma.ui.jfc.action.ImportAtmaStashAction;
import org.antispin.udma.ui.jfc.action.PasteAction;
import org.antispin.udma.ui.jfc.action.PreferencesAction;
import org.antispin.udma.ui.jfc.action.QuitAction;
import org.antispin.udma.ui.jfc.action.RedoAction;
import org.antispin.udma.ui.jfc.action.UndoAction;

public class Main {
	
	public static final String INVENTORY_PALETTE = "data\\global\\palette\\ACT1\\pal.dat";
	
	private static final IItemFactory itemFactory = new DefaultItemFactory();
	//private static final IItemRepository itemRepository = new ListItemRepository();
	private static final IItemRepository itemRepository = new HibernateItemRepository();
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO - find a way of passing this through the ImageIO API
		DC6ImageReader.setPalette(D2ResourceFactory.getPalette(INVENTORY_PALETTE));
		
		// TODO - register dc6 reader using ImageIO.scanForPlugins()
		IIORegistry.getDefaultInstance().registerServiceProvider(new DC6ImageReaderSpi());
		
		// force system look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			// ignore
		}

    	JTabbedPane tabbedPane = new JCharacterTabbedPane();
    	tabbedPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	    JItemList itemList = new JItemList(itemRepository);
	    
    	JPanel queryControls = new JPanel(new BorderLayout());
    	queryControls.add(new JComboBox(new String[] { "Recent Items", "Unique Items", "Set Items" }), BorderLayout.WEST);
    	queryControls.add(new JTextField(), BorderLayout.CENTER);
    	
    	JPanel searchPanel = new JPanel(new BorderLayout());
    	searchPanel.add(new JScrollPane(itemList), BorderLayout.CENTER);
    	searchPanel.add(queryControls, BorderLayout.SOUTH);
    	searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    	
    	final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabbedPane, searchPanel);
    	tabbedPane.addContainerListener(new ContainerListener() {
			public void componentAdded(ContainerEvent e) {
				splitPane.resetToPreferredSizes();			}
			public void componentRemoved(ContainerEvent e) {
				splitPane.resetToPreferredSizes();			}
    	});

    	JFrame mainFrame = new JFrame("Ultimate Diablo Muling Application");
    	mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	mainFrame.getContentPane().add(createMenuBar(mainFrame), BorderLayout.NORTH);
    	mainFrame.getContentPane().add(splitPane);
    	mainFrame.setSize(new Dimension(1280, 1024));
    	mainFrame.setVisible(true);
    	
    	D2CharacterService.startService();
	}
	
	private static JMenuBar createMenuBar(JFrame parent) {
		
		JMenu udmaMenu = new JMenu("Udma");
		udmaMenu.add(new AboutAction());
		udmaMenu.addSeparator();
		udmaMenu.add(new PreferencesAction());
		udmaMenu.addSeparator();
		udmaMenu.add(new QuitAction());
		
		JMenu importMenu = new JMenu("Import");
		importMenu.add(new ImportAtmaStashAction(parent, itemRepository, itemFactory));

		JMenu exportMenu = new JMenu("Export");
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.add(importMenu);
		fileMenu.add(exportMenu);
		
		JMenu editMenu = new JMenu("Edit");
		editMenu.add(new UndoAction());
		editMenu.add(new RedoAction());
		editMenu.addSeparator();
		editMenu.add(new CutAction());
		editMenu.add(new CopyAction());
		editMenu.add(new PasteAction());
		
		JMenu toolsMenu = new JMenu("Tools");
		toolsMenu.add(new AutoCubeAction(itemRepository, itemFactory));
		
    	JMenuBar menuBar = new JMenuBar();
    	menuBar.add(udmaMenu);
    	menuBar.add(fileMenu);
    	menuBar.add(editMenu);
    	menuBar.add(toolsMenu);

    	return menuBar;
	}
	
}