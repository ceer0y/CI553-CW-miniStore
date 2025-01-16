package clients.cashier;

import catalogue.Basket;
import middle.MiddleFactory;
import middle.OrderProcessing;
import middle.StockReadWriter;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;


/**
 * View of the model 
 */
public class CashierView implements Observer
{
  private static final int H = 400;       // Height of window pixels
  private static final int W = 400;       // Width  of window pixels
  
  private static final String CHECK  = "Check";
  private static final String BUY    = "Buy";
  private static final String BOUGHT = "Bought/Pay";

  private final JLabel      pageTitle  = new JLabel();
  private final JLabel      theAction  = new JLabel();
  private final JTextField  theInput   = new JTextField();
  private final JTextArea   theOutput  = new JTextArea();
  private final JScrollPane theSP      = new JScrollPane();
  private final JButton     theBtCheck = new JButton( CHECK );
  private final JButton     theBtBuy   = new JButton( BUY );
  private final JButton     theBtBought= new JButton( BOUGHT );
  private final JButton theBtDarkMode = new JButton("Dark Mode");
  
  private final JButton theBtDiscount = new JButton ("10% Discount");
  

  private StockReadWriter theStock     = null;
  private OrderProcessing theOrder     = null;
  private CashierController cont       = null;
  private boolean isDarkMode = false; // Tracks whether Dark Mode is enabled
  
  /**
   * Construct the view
   * @param rpc   Window in which to construct
   * @param mf    Factor to deliver order and stock objects
   * @param x     x-coordinate of position of window on screen 
   * @param y     y-coordinate of position of window on screen  
   */
          
  public CashierView(  RootPaneContainer rpc,  MiddleFactory mf, int x, int y  )
  {
    try                                           // 
    {      
      theStock = mf.makeStockReadWriter();        // Database access
      theOrder = mf.makeOrderProcessing();        // Process order
    } catch ( Exception e )
    {
      System.out.println("Exception: " + e.getMessage() );
    }
    Container cp         = rpc.getContentPane();    // Content Pane
    Container rootWindow = (Container) rpc;         // Root Window
    cp.setLayout(null);                             // No layout manager
    rootWindow.setSize( W, H );                     // Size of Window
    rootWindow.setLocation( x, y );

    Font f = new Font("Monospaced",Font.PLAIN,12);  // Font f is

    pageTitle.setBounds( 110, 0 , 270, 20 );       
    pageTitle.setText( "Thank You for Shopping at MiniStrore" );                        
    cp.add( pageTitle );  
    
    theBtCheck.setBounds( 16, 25+60*0, 80, 40 );    // Check Button
    theBtCheck.addActionListener(                   // Call back code
      e -> cont.doCheck( theInput.getText() ) );
    cp.add( theBtCheck );                           //  Add to canvas

    theBtBuy.setBounds( 16, 25+60*1, 80, 40 );      // Buy button 
    theBtBuy.addActionListener(                     // Call back code
      e -> cont.doBuy() );
    cp.add( theBtBuy );                             //  Add to canvas

    theBtBought.setBounds( 16, 25+60*3, 80, 40 );   // Bought Button
    theBtBought.addActionListener(                  // Call back code
      e -> cont.doBought() );
    cp.add( theBtBought );                          //  Add to canvas
    
    // Add the Dark Mode button
    theBtDarkMode.setBounds(274, 314, 100, 40); // Set position and size
    theBtDarkMode.addActionListener(e -> toggleDarkMode(cp)); // Add action listener
    cp.add(theBtDarkMode); // Add to canvas
    
    // Adding discount button
    theBtDiscount.setBounds( 16, 25+60*2, 80, 40 ); // Position and size
    theBtDiscount.addActionListener(e -> cont.doDiscount()); // Add action listener
    cp.add(theBtDiscount); // Add to canvas

    theAction.setBounds( 110, 25 , 270, 20 );       // Message area
    theAction.setText( "" );                        // Blank
    cp.add( theAction );                            //  Add to canvas

    theInput.setBounds( 110, 50, 270, 40 );         // Input Area
    theInput.setText("");                           // Blank
    cp.add( theInput );                             //  Add to canvas

    theSP.setBounds( 110, 100, 270, 160 );          // Scrolling pane
    theOutput.setText( "" );                        //  Blank
    theOutput.setFont( f );                         //  Uses font  
    cp.add( theSP );                                //  Add to canvas
    theSP.getViewport().add( theOutput );           //  In TextArea
    rootWindow.setVisible( true );                  // Make visible
    theInput.requestFocus();                        // Focus is here
    
    setLightModeColors(cp);
    
  }

  /**
   * The controller object, used so that an interaction can be passed to the controller
   * @param c   The controller
   */

  public void setController( CashierController c )
  {
    cont = c;
  }

  /**
   * Update the view
   * @param modelC   The observed model
   * @param arg      Specific args 
   */
  @Override
  public void update( Observable modelC, Object arg )
  {
    CashierModel model  = (CashierModel) modelC;
    String      message = (String) arg;
    theAction.setText( message );
    Basket basket = model.getBasket();
    if ( basket == null )
      theOutput.setText( "Customers order" );
    else
      theOutput.setText( basket.getDetails() );
    
    theInput.requestFocus();               // Focus is here
  }

//Toggle Dark Mode
private void toggleDarkMode(Container cp) {
   isDarkMode = !isDarkMode; // Toggle dark mode state

   // Update colors based on the mode
   if (isDarkMode) {
       setDarkModeColors(cp);
       theBtDarkMode.setText("Light Mode"); // Update button label
   } else {
       setLightModeColors(cp);
       theBtDarkMode.setText("Dark Mode"); // Update button label
   }
}

//Set colors for Dark Mode
private void setDarkModeColors(Container cp) {
   pageTitle.setForeground(Color.WHITE);
   theAction.setForeground(Color.WHITE);
   theInput.setBackground(Color.BLACK);
   theInput.setForeground(Color.WHITE);
   theOutput.setBackground(Color.BLACK);
   theOutput.setForeground(Color.WHITE);
   theSP.setBackground(Color.BLACK);
   cp.setBackground(Color.DARK_GRAY);
}

//Set colors for Light Mode
private void setLightModeColors(Container cp) {
   pageTitle.setForeground(Color.BLACK);
   theAction.setForeground(Color.BLACK);
   theInput.setBackground(Color.WHITE);
   theInput.setForeground(Color.BLACK);
   theOutput.setBackground(Color.WHITE);
   theOutput.setForeground(Color.BLACK);
   theSP.setBackground(Color.WHITE);
   cp.setBackground(Color.decode("#eeeeee"));
}
  
}
