package ar.edu.utn.dds.k3003;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.micrometer.MicrometerPlugin;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransferenciasApp {

	public static void main(final String... args) {
		
		LoggerFactory.getLogger("ar.edu.utn.dds.k3003").atLevel(Level.INFO);
		
		log.info("starting up the server");

		final var metricsUtils = new DDMetricsUtils("transferencias");
		final var registry = metricsUtils.getRegistry();

		// Metricas
		final var myGauge = registry.gauge("dds.unGauge", new AtomicInteger(0));
		

		// Config
		final var micrometerPlugin = new MicrometerPlugin(config -> config.registry = registry);

		final var javalinServer = Javalin.create(config -> {
			config.registerPlugin(micrometerPlugin);
		});

		javalinServer.get("/", ctx -> ctx.result("Ok!"));
		
		javalinServer.get("/number/{number}", ctx -> {
			var number = ctx.pathParamAsClass("number", Integer.class).get();
			myGauge.set(number);
			ctx.result("updated number: " + number.toString());
		});
		
				
		javalinServer.post("/transaction", ctx -> {
			log.debug("procesando transferencia");
			var transferencia = ctx.bodyAsClass( TransferDTO.class);
			try {
				transferir(transferencia);
				registry.counter("dds.transferencias","status","ok").increment();
				log.info("transferencia ok!");
				ctx.result("ok!");	
			} catch  (ApprovalException ex) {
				log.warn("transferencia no aprobada");
				registry.counter("dds.transferencias","status","rejected").increment();
				ctx.result("no aprobada!").status(HttpStatus.NOT_ACCEPTABLE);				
			} catch  (Exception ex) {
				log.error("error en transferencia", ex);
				registry.counter("dds.transferencias","status","error").increment();
				ctx.result("error!").status(HttpStatus.INTERNAL_SERVER_ERROR);
			}	
		});

		
		javalinServer.start(7070);
	}

	private static void transferir(TransferDTO transferencia) throws ApprovalException  {
		if(transferencia.getDst().equals(transferencia.getSrc())) {
			throw new ApprovalException();
		}
		if(transferencia.getAmount() <= 0) {
			throw new IllegalArgumentException();
		}
		
	}
}
