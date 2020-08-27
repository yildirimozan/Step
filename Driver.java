
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 
import java.util.Enumeration;

public class Driver implements SerialPortEventListener {
static int yon;
static SerialPort serialPort;
private static final String PORT_NAMES[] = { 
        "/dev/tty.usbserial-A9007UX1", // Mac OS X
                    "/dev/ttyACM0", // Raspberry Pi
        "/dev/ttyUSB0", // Linux
        "COM3", // Windows
};
private BufferedReader input;
/** The output stream to the port */
private static OutputStream output;
/** Milliseconds to block while waiting for port open */
private static final int TIME_OUT = 2000;
/** Default bits per second for COM port. */
private static final int DATA_RATE = 9600;
public void initialize() throws IOException {

	CommPortIdentifier portId = null;
	Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

	//First, Find an instance of serial port as set in PORT_NAMES.
	while (portEnum.hasMoreElements()) {
		CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
		for (String portName : PORT_NAMES) {
			if (currPortId.getName().equals(portName)) {
				portId = currPortId;
				break;
			}
		}
	}
	if (portId == null) {
		System.out.println("Could not find COM port.");
		return;
	}

	try {
		serialPort = (SerialPort) portId.open(this.getClass().getName(),
				TIME_OUT);

		serialPort.setSerialPortParams(DATA_RATE,
				SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1,
				SerialPort.PARITY_NONE);

		input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
		output = serialPort.getOutputStream();

		serialPort.addEventListener(this);
		serialPort.notifyOnDataAvailable(true);
	} catch (Exception e) {
		System.err.println(e.toString());
	}

}

public synchronized void close() {
	if (serialPort != null) {
		serialPort.removeEventListener();
		serialPort.close();
	}
}

/**
* Handle an event on the serial port. Read the data and print it.
*/
public synchronized void serialEvent(SerialPortEvent oEvent) {
	if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
		try {
			String inputLine=input.readLine();
			System.out.println(inputLine);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

}

	public static void main(String[] args)throws IOException {
		// TODO Auto-generated method stub
		Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("SWT Slider (o7planning.org)");
        shell.setSize(400, 450);
        shell.setLayout(null);
        
        Driver main = new Driver();
        main.initialize();
        Thread t=new Thread() {
            public void run() {

                try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
            }
        };
        t.start();

        System.out.println("Started");
 
        Group group = new Group(shell, SWT.NONE);
		group.setBounds(25, 10, 325, 45);

        final Slider slider = new Slider(shell, SWT.HORIZONTAL);
        slider.setMaximum(730);
        slider.setMinimum(0);
        slider.setSelection(0);
        slider.setIncrement(10);
        
        final Button buton_aci = new Button(group, SWT.RADIO);
        buton_aci.setBounds(25, 18, 75, 20);
        buton_aci.setText("AÇI ÝÇÝN");
        
        final Button buton_hiz = new Button(group, SWT.RADIO);
        buton_hiz.setBounds(225, 18, 75, 20);
        buton_hiz.setText("HIZ ÝÇÝN");
        
        Label lbl_aci = new Label(shell, SWT.NONE);
        lbl_aci.setBounds(28, 55, 70, 20);
        lbl_aci.setText("AÇI");
		
		Label lbl_aci_min = new Label(shell, SWT.NONE);
		lbl_aci_min.setBounds(48, 100, 70, 18);
		lbl_aci_min.setText("0");
		
		Label lbl_aci_max = new Label(shell, SWT.NONE);
		lbl_aci_max.setBounds(290, 100, 70, 18);
		lbl_aci_max.setText("720");
          
        slider.setBounds(35, 80, 286, 20);
 
        final Label text_aci = new Label(shell, SWT.NONE);
         
        text_aci.setBounds(330, 80, 286, 20);
 
        slider.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                String logText_aci = "";
                logText_aci +=slider.getSelection();
                text_aci.setText(logText_aci);
            }
        });
        
        Label lbl_hiz = new Label(shell, SWT.NONE);
        lbl_hiz.setBounds(28, 150, 50, 20);
        lbl_hiz.setText("HIZ");
        
		Label lbl_hiz_min = new Label(shell, SWT.NONE);
		lbl_hiz_min.setBounds(48, 195, 70, 18);
		lbl_hiz_min.setText("0");
		
		Label lbl_hiz_max = new Label(shell, SWT.NONE);
		lbl_hiz_max.setBounds(290, 195, 70, 18);
		lbl_hiz_max.setText("100");
          
        final Slider slider_hiz = new Slider(shell, SWT.HORIZONTAL);
        slider_hiz.setMaximum(110);
        slider_hiz.setMinimum(0);
        slider_hiz.setSelection(0);
        slider_hiz.setIncrement(10);
        slider_hiz.setBounds(35, 175, 286, 20);
        
        final Label text_hiz = new Label(shell, SWT.NONE);
        text_hiz.setBounds(330, 175, 286, 20);
        
        slider_hiz.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                String logText_hiz = "";
                logText_hiz +=slider_hiz.getSelection();
                text_hiz.setText(logText_hiz);
            }
        });
		
        Group group_2 = new Group(shell, SWT.NONE);
		group_2.setBounds(25, 220, 325, 45);
        
        
        final Button buton_sol = new Button(group_2, SWT.RADIO);
        buton_sol.setBounds(25, 18, 75, 20);
        buton_sol.setText("SOL");
        
        final Button buton_sag = new Button(group_2, SWT.RADIO);
        buton_sag.setBounds(225, 18, 75, 20);
        buton_sag.setText("SAÐ");
        
        Label lbl_step = new Label(shell, SWT.NONE);
        lbl_step.setBounds(28, 275, 70, 20);
        lbl_step.setText("STEP MÝKTARI");
        
		Label lbl_step_min = new Label(shell, SWT.NONE);
		lbl_step_min.setBounds(48, 320, 70, 18);
		lbl_step_min.setText("0");
		
		Label lbl_step_max = new Label(shell, SWT.NONE);
		lbl_step_max.setBounds(290, 320, 70, 18);
		lbl_step_max.setText("200");
		
        final Slider slider_step = new Slider(shell, SWT.HORIZONTAL);
        slider_step.setMaximum(210);
        slider_step.setMinimum(0);
        slider_step.setSelection(0);
        slider_step.setIncrement(10);
        slider_step.setBounds(35, 300, 286, 20);
        
        final Label text_step = new Label(shell, SWT.NONE);
        text_step.setBounds(330, 300, 286, 20);
        
        slider_step.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                String logText_step = "";
                logText_step +=slider_step.getSelection();
                text_step.setText(logText_step);
            }
        });
        
        buton_aci.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				slider_hiz.setSelection(000);
				text_hiz.setText(Integer.toString(slider_hiz.getSelection()));
				slider_step.setSelection(000);
				text_step.setText(Integer.toString(slider_step.getSelection()));
				slider_hiz.setEnabled(false);
				slider.setEnabled(true);
				slider_step.setEnabled(false);
				buton_sag.setEnabled(false);
				buton_sol.setEnabled(false);
			}
		});
        buton_hiz.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				slider_hiz.setEnabled(true);
				slider.setSelection(000);
				text_aci.setText(Integer.toString(slider.getSelection()));
				slider.setEnabled(false);
				slider_step.setEnabled(true);
				buton_sag.setEnabled(true);
				buton_sol.setEnabled(true);
				
			}
		});

        buton_sag.addFocusListener(new FocusAdapter() {
			
			
			public void focusGained(FocusEvent e) {
				yon = 2;
			}
		});
        buton_sol.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				yon = 1;
			}
		});
        
        

        
		Button btn_gonder = new Button(shell, SWT.NONE);
		btn_gonder.setBounds(148, 352, 90, 30);
		btn_gonder.setText("Çalýþtýr");
		btn_gonder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(text_aci.getText().length()==2)
		        {
		            text_aci.setText("0" + text_aci.getText());
		        }
		        else if (text_aci.getText().length()==1)
		        {
		        	text_aci.setText("00" + text_aci.getText());
		        }
		        if (text_hiz.getText().length() == 2)
		        {
		        	text_hiz.setText("0" + text_hiz.getText());
		        }
		        else if (text_hiz.getText().length() == 1)
		        {
		        	 text_hiz.setText("00" + text_hiz.getText());
		        }
		        if (text_step.getText().length() == 2)
		        {
		        	text_step.setText("0" + text_step.getText());
		        }
		        else if (text_step.getText().length() == 1)
		        {
					text_step.setText("00" + text_step.getText());
		        }
		        String gonder = yon + text_hiz.getText()  + text_aci.getText()  + text_step.getText() + "\r";
				System.out.println(gonder);
				char[] char_array = gonder.toCharArray();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				for (int i = 0; i < 11; i++) {
					try {
						output.write(char_array[i]);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						output.flush();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
		});
		
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
	}

}
