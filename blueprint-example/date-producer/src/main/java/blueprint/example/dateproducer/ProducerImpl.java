package blueprint.example.dateproducer;


import java.util.Date;

import org.springframework.stereotype.Component;

import blueprint.example.producer.api.Producer;

@Component
public class ProducerImpl implements Producer {

	@Override
	public Double produceTemperature() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer produceLuminousFlux() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date produceDate() {
		// TODO Auto-generated method stub
		return null;
	}

}
