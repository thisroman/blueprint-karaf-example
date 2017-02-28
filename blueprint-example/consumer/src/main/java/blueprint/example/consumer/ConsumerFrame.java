package blueprint.example.consumer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.commons.collections4.queue.CircularFifoQueue;

//import org.eclipse.gemini.blueprint.service.importer.ServiceProxyDestroyedException;
import org.springframework.stereotype.Component;

@Component
public class ConsumerFrame extends JFrame {

	private static final long serialVersionUID = 8941649782243422946L;

	private JLabel lblFlux;
	private JLabel lblDate;
	private JLabel lblTmpr;
	private JLabel textFlux;
	private JLabel textDate;
	private JLabel textTmpr;
	private JButton startButton;
	private JButton stopButton;

	private Timer timer = new Timer();
	private Queue<Integer> fifoFlux = new CircularFifoQueue<Integer>(4);
	private Queue<Double> fifoTmpr = new CircularFifoQueue<Double>(4);

	private RefreshListenerFlux refreshListenerFlux;
	private RefreshListenerDate refreshListenerDate;
	private RefreshListenerTmpr refreshListenerTmpr;

	public ConsumerFrame() {
		dataInit();
		initialize();
	}

	public void setRefreshListenerFlux(RefreshListenerFlux refreshListener) {
		this.refreshListenerFlux = refreshListener;
	}
	public void setRefreshListenerDate(RefreshListenerDate refreshListener) {
		this.refreshListenerDate = refreshListener;
	}
	public void setRefreshListenerTmpr(RefreshListenerTmpr refreshListener) {
		this.refreshListenerTmpr = refreshListener;
	}

	private void initialize() {
		setSize(500, 250);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 40, 123, 223, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 48, 28, 48, 48, 68, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);
		
		lblFlux = new JLabel("Luminous Flux:");
		GridBagConstraints gbc_textFieldFlux = new GridBagConstraints();
		gbc_textFieldFlux.insets = new Insets(0, 0, 0, 5);
		gbc_textFieldFlux.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldFlux.gridx = 1;
		gbc_textFieldFlux.gridy = 2;
		getContentPane().add(lblFlux, gbc_textFieldFlux);
		
		lblDate = new JLabel("Date:");
		GridBagConstraints gbc_textFieldDate = new GridBagConstraints();
		gbc_textFieldDate.insets = new Insets(0, 0, 0, 5);
		gbc_textFieldDate.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldDate.gridx = 1;
		gbc_textFieldDate.gridy = 1;
		getContentPane().add(lblDate, gbc_textFieldDate);
		
		lblTmpr = new JLabel("Temperature:");
		GridBagConstraints gbc_textFieldT = new GridBagConstraints();
		gbc_textFieldT.insets = new Insets(0, 0, 0, 5);
		gbc_textFieldT.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldT.gridx = 2;
		gbc_textFieldT.gridy = 2;
		getContentPane().add(lblTmpr, gbc_textFieldT);
		
		textFlux = new JLabel("No Flux Data");
		textFlux.setForeground(Color.GRAY);
		GridBagConstraints gbc_textFlux = new GridBagConstraints();
		gbc_textFlux.insets = new Insets(0, 0, 0, 5);
		gbc_textFlux.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFlux.gridx = 1;
		gbc_textFlux.gridy = 3;
		getContentPane().add(textFlux, gbc_textFlux);
		
		textDate = new JLabel("No Date Data");
		textDate.setForeground(Color.GRAY);
		GridBagConstraints gbc_textDate = new GridBagConstraints();
		gbc_textDate.insets = new Insets(0, 0, 0, 5);
		gbc_textDate.fill = GridBagConstraints.HORIZONTAL;
		gbc_textDate.gridx = 2;
		gbc_textDate.gridy = 1;
		getContentPane().add(textDate, gbc_textDate);
		
		textTmpr = new JLabel("No Temperature Data");
		textTmpr.setForeground(Color.GRAY);
		GridBagConstraints gbc_textTmpr = new GridBagConstraints();
		gbc_textTmpr.insets = new Insets(0, 0, 0, 5);
		gbc_textTmpr.fill = GridBagConstraints.HORIZONTAL;
		gbc_textTmpr.gridx = 2;
		gbc_textTmpr.gridy = 3;
		getContentPane().add(textTmpr, gbc_textTmpr);	

		startButton = new JButton("Start Lisening");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timer = new Timer();
				refreshTask();
			}
		});
		GridBagConstraints gbc_StartButton = new GridBagConstraints();
		gbc_StartButton.gridx = 1;
		gbc_StartButton.gridy = 0;
		gbc_StartButton.insets = new Insets(0, 0, 0, 10);
		gbc_StartButton.anchor=GridBagConstraints.WEST;
		getContentPane().add(startButton, gbc_StartButton);

		stopButton = new JButton("Stop Lisening");
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timer.cancel();
			}
		});
		GridBagConstraints gbc_StopButton = new GridBagConstraints();
		gbc_StopButton.gridx = 2;
		gbc_StopButton.gridy = 0;
		gbc_StopButton.insets = new Insets(0, 0, 0, 10);
		gbc_StopButton.anchor=GridBagConstraints.WEST;
		getContentPane().add(stopButton, gbc_StopButton);
	}
	
	private void dataInit() {
		fifoFlux.add(0);
		fifoFlux.add(0);
		fifoFlux.add(0);
		fifoFlux.add(0);
		fifoTmpr.add(0d);
		fifoTmpr.add(0d);
		fifoTmpr.add(0d);
		fifoTmpr.add(0d);
	}

	private void refreshButtonActionPerformed() {		
		textFlux.setText("No data");
		textDate.setText("No data");
		textTmpr.setText("No data");
		try {
			Integer lisFluxData = refreshListenerFlux.refresh();
			textFlux.setText(String.valueOf(lisFluxData));
			fifoFlux.add(lisFluxData);
			Date lisDateData = refreshListenerDate.refresh();
			textDate.setText(new SimpleDateFormat("yyyy MMMM dd HH:mm:ss").format(lisDateData));
			Double lisTmprData = refreshListenerTmpr.refresh();
			textTmpr.setText(String.valueOf(lisTmprData));
			fifoTmpr.add(lisTmprData);
			repaint();
			
		//} catch (ServiceProxyDestroyedException ex) {
		} catch (Exception ex) {
			System.out.println("Connection to provider lost. Exception: " + ex.getMessage());
			textFlux.setText("Data lost");
			textDate.setText("Data lost");
			textTmpr.setText("Data lost");
		}
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		drawFlux(g);		
		drawTmpr(g);
	}

	private void drawFlux(Graphics g) {
		g.setColor(Color.BLACK);
		int xLine = 25;
		int yLine = 123+53+20;
		int[] xPointsFlux = new int[]{xLine+20,xLine+40,xLine+60, xLine+80 };
		int[] yPointsFlux = new int[]{yLine+(Integer)fifoFlux.toArray()[0],yLine+(Integer)fifoFlux.toArray()[1],yLine+(Integer)fifoFlux.toArray()[2],yLine+(Integer)fifoFlux.toArray()[3]};
		g.drawPolyline(xPointsFlux, yPointsFlux, 4);
	}

	private void drawTmpr(Graphics g) {
		g.setColor(Color.BLACK);
		int xLine = 150 ;
		int yLine = 123+53+30;
		int[] xPointsFlux = new int[]{xLine+20,xLine+40,xLine+60, xLine+80 };
		int[] yPointsFlux = new int[]{(int) (yLine+(Double)fifoTmpr.toArray()[0]),(int) (yLine+(Double)fifoTmpr.toArray()[1]),(int) (yLine+(Double)fifoTmpr.toArray()[2]),(int) (yLine+(Double)fifoTmpr.toArray()[3])};
		g.drawPolyline(xPointsFlux, yPointsFlux, 4);
	}

	private void refreshTask() {
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				refreshButtonActionPerformed();
			}
		}, 0, 1000);
	}
}
