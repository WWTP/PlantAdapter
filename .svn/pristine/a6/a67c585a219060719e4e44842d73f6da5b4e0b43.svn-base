package gui;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SliderControl extends JFrame  {

	private List<ISliderListener> listeners;
	private JSlider slider;
	private JLabel percLabel;
	private JLabel valueLabel;
	
	private boolean eventLock;
	
	public SliderControl() {
		
		this.listeners = new ArrayList<ISliderListener>();

		JPanel panel = new JPanel();
		this.add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		this.percLabel = new JLabel();
		this.valueLabel = new JLabel();
		this.slider = new JSlider(JSlider.VERTICAL);
		
		panel.add(this.percLabel);
		panel.add(this.valueLabel);
		panel.add(this.slider);
		
		this.slider.addChangeListener(new SliderListener());
		
		this.eventLock = false;
		
		this.pack();
	}
	
	public void addSliderListener(ISliderListener listener) {
		this.listeners.add(listener);
	}
	
	public void setSliderValue(int perc) {
		this.eventLock = true;
		this.slider.setValue(perc);
		this.eventLock = false;
	}
	
	public void setValueLabelText(String text) {
		this.valueLabel.setText(text);
	}

	// TODO remove() etc.
	
	public interface ISliderListener {
		public void percChanged(float perc);
	}
	
	private class SliderListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			
			JSlider slider = (JSlider)e.getSource();
			float perc = ((float)slider.getValue()) / 100;
			// aggiorno label percentuale
			percLabel.setText(perc * 100 + "%");
				
			// Blocco la propagazione degli eventi solo esternamente...
			if (!eventLock) {
				for (ISliderListener lst : listeners) {
					lst.percChanged(perc);
				}
			}
		}
	}
}