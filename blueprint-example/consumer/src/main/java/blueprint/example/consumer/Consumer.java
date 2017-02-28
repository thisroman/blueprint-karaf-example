package blueprint.example.consumer;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.swing.SwingUtilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import blueprint.example.producer.api.Producer;

@Component
public class Consumer {

	@Autowired
	public ConsumerFrame consumerFrame;
	@Autowired
	public Producer producer;

	@PostConstruct
	public void start() {
		System.out.println("Consumer starting...");	
		
		consumerFrame.setRefreshListenerFlux(new RefreshListenerFlux() {			
			@Override
			public Integer refresh() {
				return producer.produceLuminousFlux();
			}
		});
		consumerFrame.setRefreshListenerDate(new RefreshListenerDate() {			
			@Override
			public Date refresh() {
				return producer.produceDate();
			}
		});
		consumerFrame.setRefreshListenerTmpr(new RefreshListenerTmpr() {			
			@Override
			public Double refresh() {
				return producer.produceTemperature();
			}
		});
		
		SwingUtilities.invokeLater(
				new Runnable() {
					
					@Override
					public void run() {
						consumerFrame.setVisible(true);
					}
				}
				);
	}	

	public static void main(String[] args) {
		Consumer cc = new Consumer();
		cc.consumerFrame = new ConsumerFrame();
		cc.producer = new Producer() {
			
			@Override
			public Double produceTemperature() {
				return 10d;
			}

			@Override
			public Integer produceLuminousFlux() {
				return 10;
			}

			@Override
			public Date produceDate() {
				return new Date();
			}
		};
		cc.start();
	}
}
