package blueprint.example.producer.api;

import java.util.Date;

public interface Producer {
	
	Double produceTemperature();
	
	Integer produceLuminousFlux();	
	
	Date produceDate();
	
}
