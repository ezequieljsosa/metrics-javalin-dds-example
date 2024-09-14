# Ejemplo de Javalin+Micrometer+Datadog 

Ejemplo de proyecto con un simple recolector de métricas de Micrometer configurado para una app en Javalin.

El ejemplo muestra una config mínima para tener:
1) Métricas de la JVM y otros recuros de la instancia local
2) Métricas custom, configurables desde la app y en relación al dominio específico u otro motivo
3) Tags comunes a las métricas para identificar la instancia/servicio/etc particular
4) Exposición del endpoint de métricas utlizando la API propia de Micrometer

## Ejecutar y testear el comportamiento localmente

Para levantar el servicio de ejemplo:

```bash
mvn clean package exec:java
```

## Integración con Datadog

Desde Datadog, para arrancar necesitamos crear una cuenta y obtener un token de acceso. En el proceso de obtener el token de acceso, les va a proponer que creen un agente, pero no hace falta para utilizar este repositorio (si necesitan obtener de las instrucciones la API KEY).
Vamos a usar el mecanismo de integracion mas basico, que consiste en enviar las metricas directamente a la plataforma.
En produccion, se debe implementar un agente que recopile informacion y envie periodicamente la información a DD.

La clave la tenemos que configurar en la variable de entorno DDAPI.