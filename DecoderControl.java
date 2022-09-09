import javax.swing.*;
import java.awt.*;

public class DecoderControl {
	
	public static void main (String[] args) {
		new DecoderControl();
	}
	
	public DecoderControl () {
		initFrame();
	}

	String input;
	String base = "  ";
	String [] solution = new String[4];

	JTextArea outHex = new JTextArea();
	JTextArea outBin = new JTextArea();
	JTextArea outDec = new JTextArea();
	JTextArea outSig = new JTextArea();

	String [] baseOptions = {"  ", "0x", "0b"};
	JComboBox baseSelector = new JComboBox(baseOptions);
	JTextField inputText = new JTextField(32);
	
	String [] bitOptions = {"8", "16", "32"};
	JComboBox bitSelector = new JComboBox(bitOptions);
	
	public void setBase () {
		base = (String)baseSelector.getSelectedItem();
	}
	
	public void setBits () {
		int newbits = Integer.parseInt((String)bitSelector.getSelectedItem());
		BaseDecoder.num_bits = newbits;
	}
	
	public void convert () {
		BaseDecoder.resetError();
		input = (String)inputText.getText();
		switch (base) {
			case "  ": BaseDecoder.Dec.decode(input); break;
			case "0x": BaseDecoder.Hex.decode(input); break;
			case "0b": BaseDecoder.Bin.decode(input); break;
		}
		
		if (BaseDecoder.error_base) {
			errorDisplay(0);
			return;
		}
		if (BaseDecoder.error_lim) {
			errorDisplay(1);
			return;
		}
		
		solution = BaseDecoder.getSolution();
		outDec.setText(solution[0]);
		outSig.setText(solution[1]);
		outHex.setText(solution[2]);
		outBin.setText(solution[3]);
	}
	
	private void errorDisplay (int func) {
		switch (func) {
			case 0: outBin.setText(" INCORRECT INPUT FOR BASE "); break;
			case 1: outBin.setText(" INPUT EXCEEDS (2^n)-1 "); break;
		}
		outHex.setText(""); outDec.setText(""); outSig.setText("");
	}
	
	public void initFrame () {
		JFrame main = new JFrame();
		main.setTitle("Base Decoder");
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setBounds(0,0,479,315);
		main.setLayout(null);
		main.setResizable(false);
		main.getContentPane().setBackground(Color.black);
		
		// Fonts for number input and UI labels
		Font numFont = new Font("Consolas", Font.BOLD, 22);
		Font uiFont = new Font("Silom", Font.BOLD, 16);
		
		JButton calc = new JButton("Convert");
		JLabel bitLabel = new JLabel("bit");
		
		JLabel base2 = new JLabel("2"); base2.setFont(uiFont);
		JLabel base10 = new JLabel("10"); base10.setFont(uiFont);
		JLabel base10s = new JLabel("10"); base10s.setFont(uiFont);
		JLabel base16 = new JLabel("16"); base16.setFont(uiFont);

		base2.setBounds(419,119,20,20);
		base10.setBounds(197,75,20,20);
		base10s.setBounds(417,75,20,20);
		base16.setBounds(197,30,20,20);

		// organize location of input controls
		baseSelector.setBounds(15,20,45,40);
		inputText.setBounds(61,20,391,40);
		bitSelector.setBounds(15,70,45,40);
		bitLabel.setBounds(63,70,60,40);
		calc.setBounds(340,70,110,40);
		
		inputText.setFont(numFont);
		bitSelector.setFont(uiFont);
		baseSelector.setFont(uiFont);
		bitLabel.setFont(uiFont);
		calc.setFont(uiFont);
		
		// predefined settings for UniBuilder
		baseSelector.setSelectedIndex(0);
		bitSelector.setSelectedIndex(2);
		
		baseSelector.addActionListener(e -> setBase());
		bitSelector.addActionListener(e -> setBits());
		calc.addActionListener(e -> convert());
		
		outHex.setBounds(15,15,180,30); outHex.setEditable(false);
		outBin.setBounds(15,105,400,30); outBin.setEditable(false);
		outDec.setBounds(15,60,180,30); outDec.setEditable(false);
		outSig.setBounds(235,60,180,30); outSig.setEditable(false);
		
		outHex.setFont(numFont); outHex.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		outBin.setFont(numFont); outBin.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		outDec.setFont(numFont); outDec.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		outSig.setFont(numFont); outSig.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		
		// control panel components
		JPanel control = new JPanel();
		control.setBounds(1,1,463,120);
		control.setLayout(null);
		control.setBackground(Color.lightGray);

		control.add(inputText);
		control.add(baseSelector);
		control.add(bitSelector);
		control.add(bitLabel);
		control.add(calc);
		
		// result panel components
		JPanel results = new JPanel();
		results.setBounds(1,125,482,151);
		results.setLayout(null);
		results.setBackground(Color.lightGray);
		
		results.add(outHex); results.add(base16);
		results.add(outBin); results.add(base2);
		results.add(outDec); results.add(base10);
		results.add(outSig); results.add(base10s);

		main.add(control);
		main.add(results);
		main.setVisible(true);
	}
}