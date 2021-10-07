# MELI-PROXY-es_CO

Proyecto que actua como intermediario para los request de los clientes, enviándolos hacia api.mercadolibe.com.
El proyecto implementa una solución de monitoreo de rendimiento de aplicaciones llamada [Elastic APM](https://www.elastic.co/es/apm/) .

## Prerrequisitos
 - Docker
 - Docker compose
 - 4GB ram libres

## Configuración del sistema operativo
De acuerdo con la [guía de Elasticsearch](https://www.elastic.co/guide/en/elasticsearch/reference/current/vm-max-map-count.html) , se debe ejecutar el siguiente comando como root:

```bash
sysctl -w vm.max_map_count=262144
```
## Ejecución
Se debe clonar este proyecto en el servidor donde se desee ejecutar, antes de iniciarlo se pueden parametrizar las variables de entorno del servicio proxy_meli en el archivo docker-compose.yml:
 - spring.profiles.active: perfil de spring boot activo.
 - MAX_REQUEST_IP: cantidad máxima de solicitudes por ip.
 - MAX_REQUEST_URL_PATH: cantidad máxima de solicitudes por path de destino.
 - MAX_REQUEST_IP_PATH: cantidad máxima de solicitudes por ip y path de destino.
 - elasticsearch.url: url servidor elasticsearch
 - elastic.apm.server-url: url servidor apm

Luego ejecutar el siguiente comando en la raíz de proyecto:
```bash
docker-compose up -d
```
## Uso
Para usar el proyecto, en el navegador de su preferencia ingrese a:
 - dirección_servidor:8080/meli , para verificar el servicio de proxy
 - dirección_servidor:5601 , para consultar las estadísticas de uso y otros datos de interés del servicio de proxy.

## Licencia
[MIT](https://choosealicense.com/licenses/mit/)

