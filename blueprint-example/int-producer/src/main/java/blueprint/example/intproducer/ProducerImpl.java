package blueprint.example.intproducer;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import blueprint.example.producer.api.Producer;

@Component
public class ProducerImpl implements Producer {

	private Timer timerEmulateDate = new Timer();
	private Long emulatedDate = (new Date()).getTime();

	@Override
	public Date produceDate() {
		return new Date(emulatedDate);
	}

	@PostConstruct
	public void start() {
		startFastTime();
	}


	@Override
	public Double produceTemperature() {
		Calendar cl = Calendar.getInstance();
		cl.setTimeInMillis(emulatedDate);
		int hour = cl.get(Calendar.HOUR_OF_DAY);
		switch (hour) {
		case 20:
		case 7:
		case 8:
			return (double) (ThreadLocalRandom.current().nextInt(50, 150 + 1)/10d);
		case 19:
		case 9:
		case 10:
			return (double) (ThreadLocalRandom.current().nextInt(100, 200 + 1)/10d);
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
			return (double) (ThreadLocalRandom.current().nextInt(250, 400 + 1)/10d);
		case 16:
		case 17:
		case 18:
			return (double) (ThreadLocalRandom.current().nextInt(150, 250 + 1)/10d);
		default:
			return (double) (ThreadLocalRandom.current().nextInt(50, 100 + 1)/10d);
		}
	}

	@Override
	public Integer produceLuminousFlux() {
		Calendar cl = Calendar.getInstance();
		cl.setTimeInMillis(emulatedDate);
		int hour = cl.get(Calendar.HOUR_OF_DAY);
		switch (hour) {
		case 20:
		case 7:
		case 8:
			return 5;
		case 19:
		case 9:
		case 10:
			return 10;
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
			return 20;
		case 16:
		case 17:
		case 18:
			return 15;
		default:
			return 0;
		}
	}


	private void startFastTime() {
		timerEmulateDate.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				// one hour per day by 1s.
				if(emulatedDate<100)
					emulatedDate=(new Date()).getTime();
				emulatedDate=emulatedDate+(60 * 60 * 1000);
			}
		}, 0, 1000);
	}
}
